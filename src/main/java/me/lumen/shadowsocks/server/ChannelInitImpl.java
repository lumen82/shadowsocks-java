package me.lumen.shadowsocks.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import me.lumen.shadowsocks.crypto.CryptoTypeEnum;
import me.lumen.shadowsocks.handler.ByteToMsgDecoderImpl;

/**
 * Created by lumen on 18-1-20.
 */
public class ChannelInitImpl extends ChannelInitializer {
    private String pwd;
    private CryptoTypeEnum method;

    public ChannelInitImpl(String pwd, CryptoTypeEnum method) {
        this.pwd = pwd;
        this.method = method;
    }

    protected void initChannel(Channel channel) throws Exception {
        /* 设置 channel timeout */
        channel.pipeline().addLast(new IdleStateHandler(
                ProxyServer.config.getTimeout(),
                ProxyServer.config.getTimeout(),
                ProxyServer.config.getTimeout()));
        /* 添加编解码器 */
        channel.pipeline().addLast(new ByteToMsgDecoderImpl(pwd, method));
    }
}
