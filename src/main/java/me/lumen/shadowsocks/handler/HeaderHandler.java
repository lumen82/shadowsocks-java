package me.lumen.shadowsocks.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import me.lumen.shadowsocks.model.Header;
import me.lumen.shadowsocks.utils.Constant;
import me.lumen.shadowsocks.utils.HttpUtils;
import org.apache.log4j.Logger;

/**
 * 1.解析 header
 * Created by lumen on 18-1-20.
 */
public class HeaderHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = Logger.getLogger(HeaderHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Attribute<Integer> attr = ctx.channel().attr(Constant.CONNECT_STAGE);
        if (Constant.CONNECT_STAGE_STREAM == attr.get()){
            ((ByteBuf)msg).retain();
            ctx.fireChannelRead(msg);
            return;
        }
        ByteBuf buf = (ByteBuf)msg;
        Header header = HttpUtils.parse_header(buf);
        if (header != null){
            attr.set(Constant.CONNECT_STAGE_CONNECTING);
            logger.error("request info: " + header.toString());
            buf.retain();
            ctx.pipeline().addLast(new PipeHandler(header, buf));
            /* 请求地址解析完成后移除该 Handler */
            ctx.pipeline().remove(this);
            ctx.fireChannelActive();
        }
    }
}
