package me.lumen.shadowsocks.crypto;


import me.lumen.shadowsocks.utils.DigestUtils;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by lumen on 18-1-16.
 */
public class AESCrypto implements Crypto{
    private StreamBlockCipher encrypt;
    private StreamBlockCipher decrypt;

    String cipherType;
    byte[] keyBytes;
    byte[] ivBytes;

    public AESCrypto(String cipherType, byte[] key, byte[] iv) {
        this.cipherType = cipherType;
        this.keyBytes = key;
        this.ivBytes = iv;

        try{
            AESEngine engine = new AESEngine();
            this.encrypt = new CFBBlockCipher(engine, iv.length * 8);
            ParametersWithIV parametersWithIV = new ParametersWithIV(new KeyParameter(key), iv);
            this.encrypt.init(true, parametersWithIV);

            this.decrypt = new CFBBlockCipher(engine, iv.length * 8);
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

    public String getCipherType() {
        return cipherType;
    }

    public byte[] getKeyBytes() {
        return keyBytes;
    }

    public byte[] getIvBytes() {
        return ivBytes;
    }

    public static byte[][] getKeyAndIV(String password, int keyLen, int ivLen){
        byte[] passwordBytes = password.getBytes();
        ByteArrayOutputStream arrayOutput = new ByteArrayOutputStream();
        int i = 0;
        while(arrayOutput.size() < (keyLen + ivLen)){
            if(i > 0){
                byte[] originalBytes = arrayOutput.toByteArray();
                byte[] newBytes = Arrays.copyOfRange(originalBytes, (i - 1) * 16, 16 * i);
                ByteArrayOutputStream arrayOutput2 = new ByteArrayOutputStream();
                try {
                    arrayOutput2.write(newBytes);
                    arrayOutput2.write(passwordBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] passwordMD5 = DigestUtils.encodeMD5(arrayOutput2.toByteArray());
                try {
                    arrayOutput.write(passwordMD5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                byte[] passwordMD5 = DigestUtils.encodeMD5(passwordBytes);
                try {
                    arrayOutput.write(passwordMD5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        byte[] bytes = arrayOutput.toByteArray();
        byte[] keyBytes = Arrays.copyOfRange(bytes, 0, keyLen);
        byte[] viBytes = Arrays.copyOfRange(bytes, keyLen, keyLen + ivLen);

        return new byte[][]{keyBytes, viBytes};
    }

}
