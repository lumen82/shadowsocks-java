package me.lumen.shadowsocks.crypto;

/**
 * 加密解密接口
 * Created by lumen on 18-1-15.
 */
public interface Crypto {
    /**
     * 加密
     */
    byte[] encode(byte[] data, boolean end);

    /**
     * 解密
     */
    byte[] decode(byte[] data, boolean end);

}
