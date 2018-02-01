package me.lumen.shadowsocks.model;

/**
 * 封装请求地址
 * Created by lumen on 18-1-20.
 */
public class Header {
    private int type;
    private String addr;
    private int port;
    private int headLen;

    public Header(int type, String addr, int port, int headLen) {
        this.type = type;
        this.addr = addr;
        this.port = port;
        this.headLen = headLen;
    }

    public Header() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getHeadLen() {
        return headLen;
    }

    public void setHeadLen(int headLen) {
        this.headLen = headLen;
    }

    @Override
    public String toString() {
        return "Header{" +
                "type=" + type +
                ", addr='" + addr + '\'' +
                ", port=" + port +
                ", headLen=" + headLen +
                '}';
    }
}
