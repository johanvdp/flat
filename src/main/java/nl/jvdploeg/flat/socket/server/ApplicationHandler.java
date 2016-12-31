package nl.jvdploeg.flat.socket.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import nl.jvdploeg.flat.Change;
import nl.jvdploeg.flat.ModelPublisher;
import nl.jvdploeg.flat.application.Application;
import nl.jvdploeg.flat.application.Command;
import nl.jvdploeg.flat.application.Response;
import nl.jvdploeg.flat.socket.JsonDecoder;
import nl.jvdploeg.flat.socket.JsonEncoder;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

  private static class ChangeConsumer implements Consumer<Change> {
    private final Disposable disposable;
    private final Channel channel;
    private final Observable<Change> observable;

    public ChangeConsumer(final Publisher<Change> publisher, final Channel channel) {
      this.channel = channel;
      observable = Observable.fromPublisher(publisher);
      disposable = observable.subscribe(this);
    }

    @Override
    public void accept(final Change change) throws Exception {
      final String json = JsonEncoder.encode(change).toString();
      channel.writeAndFlush(new TextWebSocketFrame(json));
    }

    public void dispose() {
      disposable.dispose();
    }
  }

  private class CommandHandler {

    public void receive(final Command command) throws Exception {
      application.getInput().accept(command);
    }
  }

  private static class ResponseConsumer implements Consumer<Response> {
    private final Disposable disposable;
    private final Channel channel;
    private final Observable<Response> observable;

    public ResponseConsumer(final Publisher<Response> responsePublisher, final Channel channel) {
      this.channel = channel;
      observable = Observable.fromPublisher(responsePublisher);
      disposable = observable.subscribe(this);
    }

    @Override
    public void accept(final Response response) throws Exception {
      final String json = JsonEncoder.encode(response).toString();
      channel.writeAndFlush(new TextWebSocketFrame(json));
    }

    public void dispose() {
      disposable.dispose();
    }
  }

  private static final Logger LOG = LoggerFactory.getLogger(ApplicationHandler.class);

  private static final AttributeKey<ChangeConsumer> CHANGE_CONSUMER = AttributeKey
      .newInstance("changeConsumer");
  private static final AttributeKey<CommandHandler> COMMAND_HANDLER = AttributeKey
      .newInstance("commandHandler");
  private static final AttributeKey<ResponseConsumer> RESPONSE_CONSUMER = AttributeKey
      .newInstance("responseConsumer");

  private final Application application;

  public ApplicationHandler(final Application application) {
    this.application = application;
  }

  @Override
  public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
    LOG.info("channelUnregistered");
    super.channelUnregistered(ctx);

    unregister(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * To know once a handshake was done you can intercept the
   * {@link ChannelInboundHandler#userEventTriggered(ChannelHandlerContext, Object)} and check if
   * the event was instance of {@link HandshakeComplete}, the event will contain extra information
   * about the handshake such as the request and selected subprotocol.
   * </p>
   */
  @Override
  public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt)
      throws Exception {
    LOG.info("userEventTriggered");
    if (evt instanceof HandshakeComplete) {
      final HandshakeComplete handshake = (HandshakeComplete) evt;
      LOG.info("handshake requestUri:" + handshake.requestUri() + ", selectedSubprotocol:"
          + handshake.selectedSubprotocol() + ", requestHeaders:"
          + handshake.requestHeaders().entries());

      final Channel channel = ctx.channel();
      register(channel);
    }

    super.userEventTriggered(ctx, evt);
  }

  private void checkChangeConsumer(final Channel channel) {
    final Attribute<ChangeConsumer> attribute = channel.attr(CHANGE_CONSUMER);
    ChangeConsumer consumer = attribute.get();
    if (consumer == null) {
      final ModelPublisher changePublisher = application.getModel().getPublisher();
      consumer = new ChangeConsumer(changePublisher, channel);
      attribute.set(consumer);
    }
  }

  private void checkCommandHandler(final Channel channel) {
    final Attribute<CommandHandler> attribute = channel.attr(COMMAND_HANDLER);
    CommandHandler handler = attribute.get();
    if (handler == null) {
      handler = new CommandHandler();
      attribute.set(handler);
    }
  }

  private void checkResponseConsumer(final Channel channel) {
    final Attribute<ResponseConsumer> attribute = channel.attr(RESPONSE_CONSUMER);
    ResponseConsumer consumer = attribute.get();
    if (consumer == null) {
      consumer = new ResponseConsumer(application.getOutput(), channel);
      attribute.set(consumer);
    }
  }

  private void disposeChangeConsumer(final ChannelHandlerContext ctx) {
    final Attribute<ChangeConsumer> attribute = ctx.channel().attr(CHANGE_CONSUMER);
    final ChangeConsumer consumer = attribute.getAndSet(null);
    if (consumer != null) {
      consumer.dispose();
    }
  }

  private void disposeCommandHandler(final ChannelHandlerContext ctx) {
    final Attribute<CommandHandler> attribute = ctx.channel().attr(COMMAND_HANDLER);
    attribute.getAndSet(null);
  }

  private void disposeResponseConsumer(final ChannelHandlerContext ctx) {
    final Attribute<ResponseConsumer> attribute = ctx.channel().attr(RESPONSE_CONSUMER);
    final ResponseConsumer consumer = attribute.getAndSet(null);
    if (consumer != null) {
      consumer.dispose();
    }
  }

  private void register(final Channel channel) {
    checkChangeConsumer(channel);
    checkResponseConsumer(channel);
    checkCommandHandler(channel);
  }

  private void unregister(final ChannelHandlerContext ctx) {
    disposeCommandHandler(ctx);
    disposeResponseConsumer(ctx);
    disposeChangeConsumer(ctx);
  }

  @Override
  protected void channelRead0(final ChannelHandlerContext ctx, final WebSocketFrame frame)
      throws Exception {
    LOG.info("channelRead0");

    if (frame instanceof TextWebSocketFrame) {
      final String json = ((TextWebSocketFrame) frame).text();
      final Object decoded = JsonDecoder.decode(json);
      if (decoded instanceof Command) {
        final Attribute<CommandHandler> attribute = ctx.channel().attr(COMMAND_HANDLER);
        final CommandHandler producer = attribute.get();
        producer.receive((Command) decoded);
      } else {
        final String message = "unsupported text content: " + json;
        throw new UnsupportedOperationException(message);
      }
    } else {
      final String message = "unsupported frame type: " + frame;
      throw new UnsupportedOperationException(message);
    }
  }
}
