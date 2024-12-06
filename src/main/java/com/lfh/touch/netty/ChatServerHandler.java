package com.lfh.touch.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j
public class ChatServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) {
        log.info("Received message: {}", msg);
        switch (msg.getType()) {
            case "LOGIN":
                handleLogin(ctx, msg);
                break;
            case "FRIEND_REQUEST":
                log.info(String.valueOf(msg));
                handleFriendRequest(msg);
                break;
            case "FRIEND_RESPONSE":
                handleFriendResponse(msg);
                break;
        }
    }

    private void handleLogin(ChannelHandlerContext ctx, MessageProtocol msg) {
        Integer userId = msg.getSenderId();
        NettyServer.addUserChannel(String.valueOf(userId), ctx.channel());
        System.out.println("User " + userId + " logged in");
    }

    private void handleFriendRequest(MessageProtocol msg) {
        Channel receiverChannel = NettyServer.getUserChannel(String.valueOf(msg.getReceiverId()));
        if (receiverChannel != null && receiverChannel.isActive()) {
            log.info("收到好友请求消息");
            log.info(String.valueOf(msg));
            receiverChannel.writeAndFlush(msg);
        } else {
            // 用户离线，存储到数据库等待用户上线后推送
            // saveFriendRequestToDatabase(msg);
        }
    }

    private void handleFriendResponse(MessageProtocol msg) {
        Channel senderChannel = NettyServer.getUserChannel(String.valueOf(msg.getReceiverId()));
        if (senderChannel != null && senderChannel.isActive()) {
            senderChannel.writeAndFlush(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 清理用户channel
        for (Map.Entry<String, Channel> entry : NettyServer.getUserChannels().entrySet()) {
            if (entry.getValue() == ctx.channel()) {
                NettyServer.removeUserChannel(entry.getKey());
                break;
            }
        }
    }
}
