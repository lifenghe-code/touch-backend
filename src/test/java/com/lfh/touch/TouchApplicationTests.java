package com.lfh.touch;

import com.lfh.touch.netty.NettyClient;
import com.lfh.touch.netty.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@Slf4j
@SpringBootTest
class TouchApplicationTests {

    @Test
    public static void main(String[] args) {
        // 启动服务器
        new Thread(() -> {
            NettyServer server = new NettyServer();
            server.start();
        }).start();

        // 启动客户端

        Integer userId = 1;
        NettyClient client = new NettyClient(userId);
        client.start();

        // 发送好友请求示例
        client.sendAddFriendRequest(1, "我是user1，请添加我为好友");
    }

}
