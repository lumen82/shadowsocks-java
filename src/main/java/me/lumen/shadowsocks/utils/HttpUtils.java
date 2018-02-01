package me.lumen.shadowsocks.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.CharsetUtil;
import me.lumen.shadowsocks.model.Header;
import org.apache.log4j.Logger;

/**
 * Created by lumen on 18-1-18.
 */
public class HttpUtils {
    public static final int ADDRTYPE_IPV4 = 0x01;
    public static final int ADDRTYPE_IPV6 = 0x04;
    public static final int ADDRTYPE_HOST = 0x03;
    public static final int ADDRTYPE_AUTH = 0x10;
    public static final int ADDRTYPE_MASK = 0xF;

    private static Logger logger = Logger.getLogger(HttpUtils.class);

    public static Header parse_header(ByteBuf data){
        int addrType = data.readByte() & ADDRTYPE_MASK;
        String destStr = "";
        int destPort = 0;
        int headerLen = 0;
        if (addrType == ADDRTYPE_IPV4){
            if (data.readableBytes() >= 7){
                destStr = getIpV4(data);
                destPort = getPort(data);
            }else {
                logger.error("header is too short");
            }
        }else if (addrType == ADDRTYPE_IPV6){
            if (data.readableBytes() >= 19){
                destStr = getIpV6(data);
                destPort = getPort(data);
            }else {
                logger.error("header is too short");
            }
        }else if (addrType == ADDRTYPE_HOST){
            if (data.readableBytes() > 1){
                int addrLen = data.readByte() & 0xFF;
                if (data.readableBytes() >= addrLen + 2){
                    destStr = data.readBytes(addrLen).toString(CharsetUtil.UTF_8);
                    destPort = getPort(data);
                }else {
                    logger.error("header is too short");
                }
            }else {
                logger.error("header is too short");
            }
        }else {
            logger.error("unsupported addr type : " + addrType);
        }
        Header header = new Header(addrType, destStr, destPort, headerLen);
        logger.error(header.toString());
        return header;
    }

    /**
     * 获取ip
     * @param data
     * @return
     */
    public static String getIpV4(ByteBuf data){
        String ipStr = "";
        for (int i = 0; i < 4; i++){
            ipStr += data.readUnsignedByte() + ".";
        }
        ipStr = ipStr.substring(0, ipStr.length() - 1);
        return ipStr;
    }

    /**
     * 获取ip地址
     * @param data
     * @return
     */
    public static String getIpV6(ByteBuf data){
        String ipStr = "";
        for (int i = 0; i < 8; i++){
            int num = data.readUnsignedShort();
            ipStr += Integer.toHexString(num) + ":";
        }
        ipStr = ipStr.substring(0, ipStr.length() -1);
        return ipStr;
    }

    /**
     * 获取端口(byte[2])
     * @param data
     * @return
     */
    public static int getPort(ByteBuf data){
        return data.readUnsignedShort();
    }
}
