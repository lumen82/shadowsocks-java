package me.lumen.shadowsocks.model;

import java.util.List;

/**
 * 服务器配置
 * Created by lumen on 18-1-13.
 */
public class Config {
    private boolean isLinux;
    private int timeout;
    private List<PortInfoBean> portInfo;

    public boolean isLinux() {
        return isLinux;
    }

    public void setLinux(boolean linux) {
        isLinux = linux;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public List<PortInfoBean> getPortInfo() {
        return portInfo;
    }

    public void setPortInfo(List<PortInfoBean> portInfo) {
        this.portInfo = portInfo;
    }

    public static class PortInfoBean {
        private int port;
        private String password;
        private String method;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        @Override
        public String toString() {
            return "PortInfoBean{" +
                    "port='" + port + '\'' +
                    ", password='" + password + '\'' +
                    ", method='" + method + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Config{" +
                "isLinux=" + isLinux +
                ", timeout=" + timeout +
                ", portInfo=" + portInfo +
                '}';
    }
}
