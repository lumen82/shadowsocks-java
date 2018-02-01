package me.lumen.shadowsocks.utils;

/**
 * Created by lumen on 18-1-16.
 */
public class StringUtils {

    public static String repeat(String str, int time){
        StringBuffer sb = new StringBuffer(str);
        for (int i = 0; i < time - 1; i++){
            sb.append(str);
        }
        return sb.toString();
    }

    public static String random(int length, String source){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++){
            int index = (int)(Math.random() * source.length());
            sb.append(source.charAt(index));
        }
        return sb.toString();
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
