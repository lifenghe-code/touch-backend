package com.lfh.touch.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import com.lfh.touch.constant.MessageConstant;
import javax.swing.*;
@Slf4j
public class ChatClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) {
        log.info("Received message: {}", msg);
        switch (msg.getType()) {
            case MessageConstant.FRIEND_ADD_REQUEST:
                handleFriendRequest(ctx, msg);
                break;
            case MessageConstant.FRIEND_RESPONSE:
                handleFriendResponse(msg);
                break;
        }
    }

    private void handleFriendRequest(ChannelHandlerContext ctx, MessageProtocol msg) {
        SwingUtilities.invokeLater(() -> {
            int option = JOptionPane.showConfirmDialog(
                    null,
                    "用户 " + msg.getSenderId() + " 请求添加您为好友\n" +
                            "消息: " + msg.getContent(),
                    "新的好友请求",
                    JOptionPane.YES_NO_OPTION
            );

            // 发送响应
            MessageProtocol response = new MessageProtocol();
            response.setType("FRIEND_RESPONSE");
            response.setSenderId(msg.getReceiverId());
            response.setReceiverId(msg.getSenderId());
            response.setContent(option == JOptionPane.YES_OPTION ? "ACCEPTED" : "REJECTED");

            ctx.writeAndFlush(response);
        });
    }

    private void handleFriendResponse(MessageProtocol msg) {
        String result = msg.getContent();
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    null,
                    "用户 " + msg.getSenderId() + " " +
                            (result.equals("ACCEPTED") ? "接受" : "拒绝") +
                            "了您的好友请求"
            );
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("与服务器断开连接");
        // 可以在这里实现重连逻辑
    }
}
