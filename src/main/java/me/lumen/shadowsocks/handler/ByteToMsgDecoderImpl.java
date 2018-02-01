package me.lumen.shadowsocks.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import me.lumen.shadowsocks.crypto.AESCrypto;
import me.lumen.shadowsocks.crypto.AESGCMCrypto;
import me.lumen.shadowsocks.crypto.Crypto;
import me.lumen.shadowsocks.crypto.CryptoTypeEnum;
import me.lumen.shadowsocks.utils.Constant;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;

import java.util.List;

/**
 * 编解码器
 * 解密入站数据
 * 加密出站数据
 * Created by lumen on 18-1-20.
 */
public class ByteToMsgDecoderImpl extends ByteToMessageCodec {
    private static Logger logger = Logger.getLogger(ByteToMsgDecoderImpl.class);
    private Crypto crypto;
    private CryptoTypeEnum method;

    /* 存储加密解密所需的key和iv */
    private byte[][] key_iv;

    private boolean stageInit = true;

    /* 第一次加密时头部拼接iv信息 */
    private boolean firstEncode = true;

    public ByteToMsgDecoderImpl(String password, CryptoTypeEnum method) {
        this.method = method;
        switch (method){
            case AES_128_CFB:
            case AES_192_CFB:
            case AES_256_CFB:
            case AES_128_GCM:
            case AES_192_GCM:
            case AES_256_GCM:
                key_iv = AESCrypto.getKeyAndIV(password,
                        method.keyLen,
                        method.viLen);
                break;
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Attribute<Integer> attr = ctx.channel().attr(Constant.CONNECT_STAGE);
        attr.set(Constant.CONNECT_STAGE_INIT);
        logger.debug("channel is active");
        super.channelActive(ctx);
    }

    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        ByteBuf msg = (ByteBuf) o;
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        logger.debug("receive from server: " + Hex.toHexString(bytes));
        bytes = crypto.encode(bytes, true);
        if (firstEncode){
            byteBuf.writeBytes(key_iv[1]);
            firstEncode = false;
        }
        byteBuf.writeBytes(bytes);
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
        logger.debug("received msg from client");
        ByteBuf buf = byteBuf;
        if (crypto == null){    /* init crypto */
            buf.readBytes(key_iv[1]);
            switch (method){
                case AES_128_CFB:
                case AES_192_CFB:
                case AES_256_CFB:
                    crypto = new AESCrypto(method.type, key_iv[0], key_iv[1]);
                    break;
                case AES_128_GCM:
                case AES_192_GCM:
                case AES_256_GCM:
                    crypto = new AESGCMCrypto(method.type, key_iv[0], key_iv[1]);
                    break;
            }
        }
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        bytes = crypto.decode(bytes, true);
        ByteBuf msgDecodeBuf = channelHandlerContext.alloc().buffer();
        msgDecodeBuf.writeBytes(bytes);
        msgDecodeBuf.retain();
        list.add(msgDecodeBuf);
        if (stageInit){
            stageInit = false;
            channelHandlerContext.pipeline().addLast(new HeaderHandler());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        /* 记录错误日志 */
        logger.error(cause);
        /* 关闭channel */
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){/* 连接超时 关闭channel */
            ctx.channel().close();
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
