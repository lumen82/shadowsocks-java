package me.lumen.shadowsocks.crypto;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithSalt;

/**
 * Created by lumen on 18-1-31.
 */
public class AESGCMCrypto implements Crypto {

    private final int NONCE_LEN = 12;
    private final int TAG_LEN = 16;

    private GCMBlockCipher encrypt;
    private GCMBlockCipher decrypt;

    String cipherType;
    byte[] keyBytes;
    byte[] ivBytes;

    public AESGCMCrypto(String cipherType, byte[] key, byte[] iv) {
        this.cipherType = cipherType;
        this.keyBytes = key;
        this.ivBytes = iv;

        try{
            AESEngine engine = new AESEngine();
//            ParametersWithIV parametersWithIV = new ParametersWithIV(new KeyParameter(key), iv);
            CipherParameters parametersWithIV = new AEADParameters(new KeyParameter(key), key.length, iv);
//            ParametersWithSalt parametersWithIV = new ParametersWithSalt(new KeyParameter(key), iv);
            this.encrypt = new GCMBlockCipher(engine);
            this.encrypt.init(true, parametersWithIV);

            this.decrypt = new GCMBlockCipher(engine);
            this.decrypt.init(false, parametersWithIV);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public byte[] encode(byte[] data, boolean end) {
        byte[] result = new byte[data.length];
        encrypt.processBytes(data, 0, data.length, result, 0);
        return result;
    }

    public byte[] decode(byte[] data, boolean end) {
        byte[] result = new byte[data.length];
        decrypt.processBytes(data, 0, data.length, result, 0);
        return result;
    }
}
