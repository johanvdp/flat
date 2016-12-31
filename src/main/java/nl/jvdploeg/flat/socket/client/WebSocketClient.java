package nl.jvdploeg.flat.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.net.URI;

import javax.net.ssl.SSLException;

public final class WebSocketClient implements Closeable, Observer<String>, Publisher<String> {

  private class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    public MyChannelInitializer() {
    }

    @Override
    protected void initChannel(final SocketChannel ch) throws Exception {
      final ChannelPipeline p = ch.pipeline();
      if (sslContext != null) {
        p.addLast(sslContext.newHandler(ch.alloc(), host, port));
      }
      p.addLast(new HttpClientCodec(), new HttpObjectAggregator(8192),
          WebSocketClientCompressionHandler.INSTANCE, handler);
    }
  }

  private static final Logger LOG = LoggerFactory.getLogger(WebSocketClient.class);

  private static WebSocketClientHandshaker createHandshaker(final URI uri) {
    return WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true,
        new DefaultHttpHeaders());
  }

  private Channel ch;
  private EventLoopGroup group;
  private final WebSocketHandler handler;
  private Disposable subscription;
  private final MyChannelInitializer channelInitializer;
  private final String host;

  private final int port;

  private final SslContext sslContext;

  public WebSocketClient(final URI uri) throws SSLException {
    LOG.info("WebSocketClient " + uri);

    final String scheme = uri.getScheme();
    if (scheme == null || !(scheme.equalsIgnoreCase("ws") || scheme.equalsIgnoreCase("wss"))) {
      throw new IllegalArgumentException("uri scheme must be ws or wss.");
    }

    host = uri.getHost();
    if (host == null || host.isEmpty()) {
      throw new IllegalArgumentException("uri host must be provided.");
    }

    port = uri.getPort();
    if (port == -1) {
      throw new IllegalArgumentException("uri port must be provided.");
    }

    final boolean ssl = "wss".equalsIgnoreCase(scheme);
    if (ssl) {
      sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
          .build();
    } else {
      sslContext = null;
    }

    handler = new WebSocketHandler(createHandshaker(uri));
    channelInitializer = new MyChannelInitializer();
  }

  @Override
  public void close() {
    LOG.info("close");

    disposeSubscription();

    if (ch != null) {
      ch.writeAndFlush(new CloseWebSocketFrame());

      try {
        ch.closeFuture().sync();
      } catch (final InterruptedException ex) {
        LOG.info("close InterruptedException");
        Thread.currentThread().interrupt();
      } finally {
        ch = null;
      }
    }

    if (group != null) {
      group.shutdownGracefully();
      group = null;
    }
  }

  @Override
  public void onComplete() {
    LOG.info("onComplete");
    close();
  }

  @Override
  public void onError(final Throwable throwable) {
    LOG.error("onError", throwable);
    close();
  }

  @Override
  public void onNext(final String text) {
    LOG.info("onNext " + text);
    ch.writeAndFlush(new TextWebSocketFrame(text));
  }

  @Override
  public void onSubscribe(final Disposable subscription) {
    this.subscription = subscription;
  }

  public void open() throws SSLException {
    LOG.info("open");

    group = new NioEventLoopGroup();
    final Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(group).channel(NioSocketChannel.class).handler(channelInitializer);

    try {
      ch = bootstrap.connect(host, port).sync().channel();
      handler.handshakeFuture().sync();
    } catch (final InterruptedException ex) {
      LOG.info("open InterruptedException");
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public void subscribe(final Subscriber<? super String> subscriber) {
    if (handler == null) {
      throw new IllegalStateException("do not subscribe when handler not initialized");
    }
    handler.subscribe(subscriber);
  }

  private void disposeSubscription() {
    if (subscription != null) {
      if (!subscription.isDisposed()) {
        subscription.dispose();
      }
      subscription = null;
    }
  }
}
