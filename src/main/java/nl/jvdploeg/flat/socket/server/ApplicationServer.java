package nl.jvdploeg.flat.socket.server;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import nl.jvdploeg.flat.application.Application;

public final class ApplicationServer implements Runnable {

    public static SslContext createSslContext() throws CertificateException, SSLException {
        final SelfSignedCertificate ssc = new SelfSignedCertificate();
        final SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        return sslCtx;
    }

    private SslContext sslContext;
    private final int port;

    private final Application application;

    public ApplicationServer(final Application application, final int port) {
        this.application = application;
        this.port = port;
    }

    @Override
    public void run() {
        start();
    }

    public void setSslContext(final SslContext sslContext) {
        this.sslContext = sslContext;
    }

    public void start() {
        final EventLoopGroup listener = new NioEventLoopGroup(1);
        final EventLoopGroup workers = new NioEventLoopGroup();
        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(listener, workers) //
                    .channel(NioServerSocketChannel.class) //
                    .handler(new LoggingHandler(LogLevel.INFO)) //
                    .childHandler(new ApplicationServerInitializer(application, sslContext));

            final Channel ch = serverBootstrap.bind(port).sync().channel();

            ch.closeFuture().sync();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            listener.shutdownGracefully();
            workers.shutdownGracefully();
        }
    }
}
