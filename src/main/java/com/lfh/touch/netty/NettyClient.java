package com.lfh.touch.netty;

import com.lfh.touch.constant.MessageConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j

public class NettyClient {
    private Integer userId;
    private Channel channel;
    private EventLoopGroup group;

    public NettyClient() {

    }
    public NettyClient(Integer userId) {
        this.userId = userId;
    }

    public void start() {
        group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new ChatClientHandler());
                        }
                    });

            channel = bootstrap.connect("127.0.0.1", 12999).sync().channel();

            // 发送登录消息
            // sendLoginMessage();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }

    private void sendLoginMessage() {
        MessageProtocol loginMsg = new MessageProtocol();
        loginMsg.setType("LOGIN");
        loginMsg.setSenderId(userId);
        channel.writeAndFlush(loginMsg);
        log.info("已发送登录消息，userId: {}", userId);
    }

    public void sendAddFriendRequest(Integer receiverId, String message) {
        log.info("发送好友请求消息");
        MessageProtocol request = new MessageProtocol();
        request.setType(MessageConstant.FRIEND_ADD_REQUEST);
        request.setSenderId(userId);
        request.setReceiverId(receiverId);
        request.setContent(message);
        request.setTimestamp(System.currentTimeMillis());

        channel.writeAndFlush(request);
    }

    public void close() {
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
    }
}
