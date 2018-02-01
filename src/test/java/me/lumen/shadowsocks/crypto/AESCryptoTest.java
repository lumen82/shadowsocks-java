package me.lumen.shadowsocks.crypto; 

import me.lumen.shadowsocks.utils.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* AESCrypto Tester. 
* 
* @author <Authors name> 
* @since <pre>Jan 16, 2018</pre> 
* @version 1.0 
*/ 
public class AESCryptoTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: encode(byte[] data) 
* 
*/ 
@Test
public void testEncode() throws Exception { 
//TODO: Test goes here...
    String cipherType = "AES/CFB/NoPadding";
    int keyLen = 16;
    int viLen = 16;
    byte[][] keyAndVi = AESCrypto.getKeyAndIV("key", keyLen, viLen);
    AESCrypto aes = new AESCrypto(cipherType, keyAndVi[0], StringUtils.hexStringToBytes("d8b84c781c0d442231bd4dbef9070957"));
    System.out.println(aes.getCipherType());
    byte[] plain = StringUtils.hexStringToBytes("300d0a0d0a300d0a0d0a");
    byte[] encodedData = aes.encode(plain, false);
    System.out.println("encoded data: " + Hex.toHexString(encodedData));
    byte[] decodedData = aes.decode(encodedData, false);
    System.out.println("decoded data: " + Hex.toHexString(decodedData));
} 



}
