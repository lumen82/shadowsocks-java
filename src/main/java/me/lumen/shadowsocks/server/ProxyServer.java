package me.lumen.shadowsocks.server;

import com.google.gson.GsonBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import me.lumen.shadowsocks.crypto.CryptoTypeEnum;
import me.lumen.shadowsocks.model.Config;
import me.lumen.shadowsocks.utils.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Created by lumen on 18-1-13.
 */
public class ProxyServer {
    public static Config config;
    public static int UP_STREAM_BUF_SIZE = 16 * 1024;
    public static int DOWN_STREAM_BUF_SIZE = 32 * 1024;
    static {
        try {
            String configStr = FileUtil.readFileToString("config.json");
            config = new GsonBuilder().create().fromJson(configStr, Config.class);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void start(Config config) throws Exception{
        InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());
        EventLoopGroup bossGroup;
        EventLoopGroup workGroup;
        if (config.isLinux()){
            bossGroup = new EpollEventLoopGroup();
            workGroup = new EpollEventLoopGroup();
        }else {
            bossGroup = new NioEventLoopGroup();
            workGroup = new NioEventLoopGroup();
        }


        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup);
            if (config.isLinux()){
                bootstrap.channel(EpollServerSocketChannel.class);
            }else {
                bootstrap.channel(NioServerSocketChannel.class);
            }
                    bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getTimeout() * 1000)
                    .childOption(ChannelOption.SO_RCVBUF, DOWN_STREAM_BUF_SIZE)
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(DOWN_STREAM_BUF_SIZE));

            List<Config.PortInfoBean> ports = config.getPortInfo();
            Collection<Channel> serverChannels = new ArrayList<Channel>(ports.size());
            for (Config.PortInfoBean portInfoBean : ports){
                CryptoTypeEnum method = CryptoTypeEnum.getType(portInfoBean.getMethod());
                Channel serverChannel = bootstrap
                        .childHandler(new ChannelInitImpl(portInfoBean.getPassword(), method))
                        .bind(portInfoBean.getPort()).sync()
                        .channel();
                serverChannels.add(serverChannel);
            }
            for (Channel channel : serverChannels){
                channel.closeFuture().sync();
            }
        }finally {
            workGroup.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception{
        /* 判断操作系统 */
        Properties prop = System.getProperties();
        String osName = prop.getProperty("os.name").toLowerCase();
        if (osName.contains("linux")){
            config.setLinux(true);
        }
        new ProxyServer().start(config);
    }

}
