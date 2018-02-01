package me.lumen.shadowsocks.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lumen on 18-1-16.
 */
public class DigestUtils {
    public static byte[] encodeMD5(byte[] origin){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(origin);
            return md.digest();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return  null;
    }

}
