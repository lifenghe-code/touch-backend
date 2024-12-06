package com.lfh.touch.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class NettyServer {
    private static final int PORT = 12999;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    // 存储在线用户的channel
    private static final Map<String, Channel> userChannels = new ConcurrentHashMap<>();
    public void start() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加编解码器
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            // 添加业务处理器
                            pipeline.addLast(new ChatServerHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(PORT).sync();
            log.info("Server started on port " + PORT);
            //关闭通道
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    // 获取所有用户Channel的Map
    public static Map<String, Channel> getUserChannels() {
        return userChannels;
    }
    // 获取用户Channel
    public static Channel getUserChannel(String userId) {
        return userChannels.get(userId);
    }

    // 添加用户Channel
    public static void addUserChannel(String userId, Channel channel) {
        userChannels.put(userId, channel);
    }

    // 移除用户Channel
    public static void removeUserChannel(String userId) {
        userChannels.remove(userId);
    }
    // 检查用户是否在线
    public static boolean isUserOnline(String userId) {
        Channel channel = userChannels.get(userId);
        return channel != null && channel.isActive();
    }

    // 获取在线用户数量
    public static int getOnlineUserCount() {
        return userChannels.size();
    }

    // 获取所有在线用户ID
    public static Set<String> getOnlineUserIds() {
        return new HashSet<>(userChannels.keySet());
    }

    // 广播消息给所有在线用户
    public static void broadcastMessage(MessageProtocol message) {
        userChannels.values().forEach(channel -> {
            if (channel.isActive()) {
                channel.writeAndFlush(message);
            }
        });
    }

    // 清理断开的连接
    public static void cleanInactiveChannels() {
        userChannels.entrySet().removeIf(entry -> !entry.getValue().isActive());
    }
}
