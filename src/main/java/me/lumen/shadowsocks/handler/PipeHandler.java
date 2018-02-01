package me.lumen.shadowsocks.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.Attribute;
import me.lumen.shadowsocks.model.Header;
import me.lumen.shadowsocks.server.ProxyServer;
import me.lumen.shadowsocks.utils.Constant;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * Created by lumen on 18-1-20.
 */
public class PipeHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = Logger.getLogger(PipeHandler.class);
    private Header header;
    private ByteBuf initBuf;

    private ChannelHandlerContext clientChannelCtx;
    private ChannelHandlerContext serverChannelCtx;

    public PipeHandler(Header header, ByteBuf buf) {
        this.header = header;
        this.initBuf = buf;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        this.serverChannelCtx = ctx;
        if (header != null){
            Bootstrap bootstrap = new Bootstrap();
            if (ProxyServer.config.isLinux()){
                bootstrap.channel(EpollSocketChannel.class);
            }else{
                bootstrap.channel(NioSocketChannel.class);
            }
                    bootstrap.remoteAddress(new InetSocketAddress(header.getAddr(), header.getPort()))
                    .option(ChannelOption.SO_RCVBUF, ProxyServer.DOWN_STREAM_BUF_SIZE)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(ProxyServer.DOWN_STREAM_BUF_SIZE))
                    .option(ChannelOption.AUTO_READ, true)
                    .handler(new IdleStateHandler(ProxyServer.config.getTimeout(), ProxyServer.config.getTimeout(), ProxyServer.config.getTimeout()))
                    .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                        @Override
                        public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
                            clientChannelCtx = channelHandlerContext;

                            /* 更改 serverChannel CONNECT_STAGE */
                            Attribute<Integer> attr = serverChannelCtx.channel().attr(Constant.CONNECT_STAGE);
                            attr.set(Constant.CONNECT_STAGE_STREAM);

                            if (initBuf.readableBytes() > 0){
                                logger.debug("write to server");
                                channelHandlerContext.writeAndFlush(initBuf);
                            }
                        }

                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                            logger.debug("read from server");
                            byteBuf.retain();
                            serverChannelCtx.write(byteBuf);
                        }

                        @Override
                        public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
                            logger.debug("read from server complete");
                            if (!serverChannelCtx.channel().isActive()){
                                logger.error("channel closed!");
                            }
                            serverChannelCtx.writeAndFlush(Unpooled.EMPTY_BUFFER);
                        }

                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            if (evt instanceof IdleStateEvent){
                                ctx.channel().close();
                            }else {
                                super.userEventTriggered(ctx, evt);
                            }
                        }
                    });
            bootstrap.group(serverChannelCtx.channel().eventLoop());
            bootstrap.connect();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Attribute<Integer> attr = serverChannelCtx.channel().attr(Constant.CONNECT_STAGE);
        if (Constant.CONNECT_STAGE_STREAM != attr.get())
            return;
        logger.debug("write to server");
        clientChannelCtx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        Attribute<Integer> attr = serverChannelCtx.channel().attr(Constant.CONNECT_STAGE);
        if (Constant.CONNECT_STAGE_STREAM != attr.get())
            return;
        logger.debug("write to server complete");
        clientChannelCtx.writeAndFlush(Unpooled.EMPTY_BUFFER);
    }
}
