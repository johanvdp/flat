package nl.jvdploeg.flat.socket.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import nl.jvdploeg.flat.application.Application;

public class ApplicationServerInitializer extends ChannelInitializer<SocketChannel> {

  private static final String PATH = "/websocket";

  private final Application application;
  private final SslContext sslContext;

  public ApplicationServerInitializer(final Application application, final SslContext sslContext) {
    this.application = application;
    this.sslContext = sslContext;
  }

  @Override
  public void initChannel(final SocketChannel ch) throws Exception {
    final ChannelPipeline pipeline = ch.pipeline();
    if (sslContext != null) {
      pipeline.addLast(sslContext.newHandler(ch.alloc()));
    }
    pipeline.addLast(new HttpServerCodec());
    pipeline.addLast(new HttpObjectAggregator(65536));
    pipeline.addLast(new WebSocketServerCompressionHandler());
    pipeline.addLast(new WebSocketServerProtocolHandler(PATH, null, true));
    pipeline.addLast(new ApplicationHandler(application));
  }
}
