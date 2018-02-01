package me.lumen.shadowsocks.utils;

import io.netty.util.AttributeKey;

/**
 * 系统常量
 * Created by lumen on 18-1-31.
 */
public interface Constant {
    // TODO: 18-1-31 CONNECT_STAGE 连接状态
    AttributeKey<Integer> CONNECT_STAGE = AttributeKey.valueOf("connect-stage");
    /**
     * 连接状态来自shadowsocks python版
     * 本项目只用到了 0, 1, 4, 5
     */
    int CONNECT_STAGE_INIT = 0;
    int CONNECT_STAGE_ADDR = 1;
    int CONNECT_STAGE_UDP_ASSOC = 2;
    int CONNECT_STAGE_DNS = 3;
    int CONNECT_STAGE_CONNECTING = 4;
    int CONNECT_STAGE_STREAM = 5;
    int CONNECT_STAGE_DESTROYED = -1;
}
