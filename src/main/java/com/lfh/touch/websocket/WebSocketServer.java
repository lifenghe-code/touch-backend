package com.lfh.touch.websocket;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lfh.touch.mapper.ChatDetailMapper;
import com.lfh.touch.model.domain.ChatDetail;
import com.lfh.touch.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@ServerEndpoint("/websocket/{uid}")
public class WebSocketServer {

//    private ChatDetailMapper chatDetailMapper;
//    @PostConstruct
//    void init(){
//        log.info("--------------");
//        this.chatDetailMapper = SpringContextUtils.getBean(ChatDetailMapper.class);
//        log.info(this.chatDetailMapper.toString());
//    }

    /**
     * 静态变量，用来记录当前在线连接数，线程安全的类。
     */
    private static AtomicInteger onlineSessionClientCount = new AtomicInteger(0);

    /**
     * 存放所有在线的客户端
     */
    private static Map<Integer, Session> onlineSessionClientMap = new ConcurrentHashMap<>();

    /**
     * 连接sid和连接会话
     */
    private Integer uid;
    private Session session;

    /**
     * 连接建立成功调用的方法。由前端<code>new WebSocket</code>触发
     *
     * @param uid     每次页面建立连接时传入到服务端的id，比如用户id等。可以自定义。
     * @param session 与某个客户端的连接会话，需要通过它来给客户端发送消息
     */
    @OnOpen
    public void onOpen(@PathParam("uid") Integer uid, Session session) {
        /**
         * session.getId()：当前session会话会自动生成一个id，从0开始累加的。
         */
        log.info("连接建立中 ==> session_id = {}， sid = {}", session.getId(), uid);
        //加入 Map中。将页面的sid和session绑定或者session.getId()与session
        //onlineSessionIdClientMap.put(session.getId(), session);
        onlineSessionClientMap.put(uid, session);

        //在线数加1
        onlineSessionClientCount.incrementAndGet();
        this.uid = uid;
        this.session = session;
        log.info("连接建立成功，当前在线数为：{} ==> 开始监听新连接：session_id = {}， sid = {},。", onlineSessionClientCount, session.getId(), uid);
    }

    /**
     * 连接关闭调用的方法。由前端<code>socket.close()</code>触发
     *
     * @param uid
     * @param session
     */
    @OnClose
    public void onClose(@PathParam("uid") Integer uid, Session session) {
        //onlineSessionIdClientMap.remove(session.getId());
        // 从 Map中移除
        onlineSessionClientMap.remove(uid);

        //在线数减1
        onlineSessionClientCount.decrementAndGet();
        log.info("连接关闭成功，当前在线数为：{} ==> 关闭该连接信息：session_id = {}， sid = {},。", onlineSessionClientCount, session.getId(), uid);
    }

    /**
     * 收到客户端消息后调用的方法。由前端<code>socket.send</code>触发
     * * 当服务端执行toSession.getAsyncRemote().sendText(xxx)后，前端的socket.onmessage得到监听。
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        /**
         * html界面传递来得数据格式，可以自定义.
         * {"sid":"user-1","message":"hello websocket"}
         */

        JSONObject jsonObject = JSONUtil.parseObj(message);
        ChatDetail bean = JSONUtil.toBean(jsonObject, ChatDetail.class);

        Integer toUid = bean.getReceiverUid();
        String msg = bean.getContent();
        log.info("服务端收到客户端消息 ==> fromSid = {}, toSid = {}, message = {}", uid, toUid, msg);

        /**
         * 模拟约定：如果未指定sid信息，则群发，否则就单独发送
         */
        if (toUid == null) {
            sendToAll(msg);
        } else {
            sendToOne(bean);
        }
    }

    /**
     * 发生错误调用的方法
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误，错误信息为：" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 群发消息
     *
     * @param message 消息
     */
    public void sendToAll(String message) {
        // 遍历在线map集合
        onlineSessionClientMap.forEach((onlineUid, toSession) -> {
            // 排除掉自己
            if (uid!=onlineUid) {
                log.info("服务端给客户端群发消息 ==> uid = {}, toUid = {}, message = {}", uid, onlineUid, message);
                toSession.getAsyncRemote().sendText(message);
            }
        });
    }



    /**
     * 指定发送消息
     * @param chatDetail
     */
    public void sendToOne(ChatDetail chatDetail) {
        // 通过sid查询map中是否存在
        Session fromSession = onlineSessionClientMap.get(chatDetail.getSenderUid());
        Session toSession = onlineSessionClientMap.get(chatDetail.getReceiverUid());
        Integer toUid = chatDetail.getReceiverUid();
        String message = chatDetail.getContent();
        if (toSession == null) {
            log.error("服务端给客户端发送消息 ==> toSid = {} 不存在, message = {}", toUid, message);
            return;
        }
        // 异步发送
        ChatDetailMapper chatDetailMapper = SpringContextUtils.getBean(ChatDetailMapper.class);
        log.info("--------------",chatDetailMapper.toString());
        int insert = chatDetailMapper.insert(chatDetail);
        chatDetail.setId(insert);
        log.info("服务端给客户端发送消息 ==> toSid = {}, message = {}", toUid, message);
        String jsonString = JSONUtil.toJsonStr(chatDetail);
        toSession.getAsyncRemote().sendText(jsonString);
        fromSession.getAsyncRemote().sendText(jsonString);
        /*
        // 同步发送
        try {
            toSession.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息失败，WebSocket IO异常");
            e.printStackTrace();
        }*/
    }


}