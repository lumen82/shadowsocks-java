package me.lumen.shadowsocks.crypto;

/**
 * Created by lumen on 18-1-16.
 */
public enum  CryptoTypeEnum {
    AES_128_CFB("aes-128-cfb", 16, 16),
    AES_192_CFB("aes-192-cfb", 24, 16),
    AES_256_CFB("aes-256-cfb", 32, 16),
    AES_128_GCM("aes-128-gcm", 16, 16),
    AES_192_GCM("aes-192-gcm", 24, 24),
    AES_256_GCM("aes-256-gcm", 32, 32);


    public String type;
    public int keyLen;
    public int viLen;

    CryptoTypeEnum(String type, int keyLen, int viLen){
        this.type = type;
        this.keyLen = keyLen;
        this.viLen = viLen;
    }

    public static CryptoTypeEnum getType(String method){
        if (AES_128_CFB.type.equals(method)) return AES_128_CFB;
        if (AES_192_CFB.type.equals(method)) return AES_192_CFB;
        if (AES_256_CFB.type.equals(method)) return AES_256_CFB;
        if (AES_128_GCM.type.equals(method)) return AES_128_GCM;
        if (AES_192_GCM.type.equals(method)) return AES_192_GCM;
        if (AES_256_GCM.type.equals(method)) return AES_256_GCM;
        throw new IllegalArgumentException("given method not support");
    }

}
