package nl.jvdploeg.flat.socket.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.util.CharsetUtil;
import nl.jvdploeg.rx.DefaultPublisher;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketHandler extends SimpleChannelInboundHandler<Object>
    implements Publisher<String> {

  private static final Logger LOG = LoggerFactory.getLogger(WebSocketHandler.class);

  private final WebSocketClientHandshaker handshaker;
  private ChannelPromise handshakeFuture;
  private final DefaultPublisher<String> publisher = new DefaultPublisher<>();

  public WebSocketHandler(final WebSocketClientHandshaker handshaker) {
    this.handshaker = handshaker;
  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    LOG.info("channelActive");
    handshaker.handshake(ctx.channel());
    super.channelActive(ctx);
  }

  @Override
  public void channelRead0(final ChannelHandlerContext ctx, final Object msg) throws Exception {
    final Channel ch = ctx.channel();

    if (msg instanceof TextWebSocketFrame) {
      final TextWebSocketFrame frame = (TextWebSocketFrame) msg;
      final String text = frame.text();
      publisher.publishNext(text);
    } else if (msg instanceof CloseWebSocketFrame) {
      LOG.info("channelRead0 CloseWebSocketFrame");
      ch.close();
      publisher.publishComplete();
    } else if (msg instanceof FullHttpResponse) {
      final FullHttpResponse response = (FullHttpResponse) msg;
      if (!handshaker.isHandshakeComplete()) {
        handshaker.finishHandshake(ch, response);
        handshakeFuture.setSuccess();
        return;
      }
      final Throwable t = new IllegalStateException("unexpected response: [" + response.status()
          + ", " + response.content().toString(CharsetUtil.UTF_8) + ']');
      ch.close();
      publisher.publishError(t);
    } else {
      final Throwable t = new IllegalArgumentException("unexpected message: " + msg);
      ch.close();
      publisher.publishError(t);
    }
  }

  @Override
  public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
    LOG.info("channelUnregistered");
    super.channelUnregistered(ctx);
  }

  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    LOG.error("exceptionCaught", cause);
    if (!handshakeFuture.isDone()) {
      handshakeFuture.setFailure(cause);
    }
    ctx.close();
    publisher.publishError(cause);
  }

  @Override
  public void handlerAdded(final ChannelHandlerContext ctx) {
    LOG.info("handlerAdded");
    handshakeFuture = ctx.newPromise();
  }

  @Override
  public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
    LOG.info("handlerRemoved");
    super.handlerRemoved(ctx);
    publisher.publishComplete();
  }

  public ChannelFuture handshakeFuture() {
    return handshakeFuture;
  }

  @Override
  public void subscribe(final Subscriber<? super String> subscriber) {
    publisher.subscribe(subscriber);
  }
}
