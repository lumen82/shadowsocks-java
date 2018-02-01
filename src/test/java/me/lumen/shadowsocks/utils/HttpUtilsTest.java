package me.lumen.shadowsocks.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import me.lumen.shadowsocks.crypto.AESCrypto;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.Properties;

/** 
* HttpUtils Tester. 
* 
* @author <Authors name> 
* @since <pre>Jan 19, 2018</pre> 
* @version 1.0 
*/ 
public class HttpUtilsTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: parse_header(ByteBuf data) 
* 
*/ 
@Test
public void testParse_header() throws Exception {
    byte[] headerBytes = StringUtils.hexStringToBytes("030d7777772e62616964752e636f6d01bb");
    ByteBuf buf = Unpooled.copiedBuffer(headerBytes);
//    buf.writeBytes("www.google.com".getBytes());
//    buf.writeBytes(StringUtils.hexStringToBytes("0050"));
    HttpUtils.parse_header(buf);
}


/** 
* 
* Method: getIpV4(ByteBuf data) 
* 
*/ 
@Test
public void testGetIpV4() throws Exception { 
//TODO: Test goes here...
    byte[] bytes = StringUtils.hexStringToBytes("474554202f70726f647563742f3120485454502f312e310d0a486f73743a206c6f63616c686f73743a393939390d0a436f6e6e656374696f6e3a206b6565702d616c6976650d0a43616368652d436f6e74726f6c3a206d61782d6167653d300d0a557365722d4167656e743a204d6f7a696c6c612f352e3020285831313b204c696e7578207838365f363429204170706c655765624b69742f3533372e333620284b48544d4c2c206c696b65204765636b6f29204368726f6d652f36332e302e333233392e313038205361666172692f3533372e33360d0a557067726164652d496e7365637572652d52657175657374733a20310d0a4163636570743a20746578742f68746d6c2c6170706c69636174696f6e2f7868746d6c2b786d6c2c6170706c69636174696f6e2f786d6c3b713d302e392c696d6167652f776562702c696d6167652f61706e672c2a2f2a3b713d302e380d0a4163636570742d456e636f64696e673a20677a69702c206465666c6174652c2062720d0a4163636570742d4c616e67756167653a207a682d434e2c7a683b713d302e392c656e3b713d302e380d0a0d0a");
    String content = new String(bytes, "UTF-8");
    System.out.print(content);
} 

/** 
* 
* Method: getIpV6(ByteBuf data) 
* 
*/ 
@Test
public void testGetIpV6() throws Exception { 
//TODO: Test goes here...
    Properties prop = System.getProperties();
    String osName = prop.getProperty("os.name");
    String osArch = prop.getProperty("os.arch");
    String osVersion = prop.getProperty("os.version");
    System.out.println("osName: " + osName);
    System.out.println("osArch: " + osArch);
    System.out.println("osVersion: " + osVersion);

} 

/** 
* 
* Method: getPort(ByteBuf data) 
* 
*/ 
@Test
public void testGetPort() throws Exception { 
//TODO: Test goes here... 
} 


} 
