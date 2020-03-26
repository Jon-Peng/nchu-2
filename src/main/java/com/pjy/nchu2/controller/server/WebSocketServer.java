package com.pjy.nchu2.controller.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/imserver/{userId}")
@Component
public class WebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private static Session session;
    /**
     * 接收userId
     */
    private String userId = "";

    //连接成功方法
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
        } else {
            webSocketMap.put(userId, this);
            addOnlineCount();//在线数加一
        }
        System.out.println("用户连接：" + userId + " 在线人数：" + onlineCount);
        try {
            sendMessage("send成功");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("用户网络异常！");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            subOnlineCount();//在线数量减
        }
        System.out.println("用户退出：" + userId + "当前在线数：" + onlineCount);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("用户消息:" + userId + ",报文:" + message);
        if (StringUtils.isNotBlank(message)) {
            try {
                JSONObject jsonObject = JSON.parseObject(message);
                jsonObject.put("fromUserId", this.userId);//添加发送人
                String toUserId = jsonObject.getString("toUserId");

                if (StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());//发送消息
                } else {
                    System.out.println("用户不在线");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息
     * @return
     */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        System.out.println("发送消息到:"+userId+"，报文:"+message);
        if (StringUtils.isNotBlank(message) && webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else {
            System.out.println("用户不在线-"+userId);
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
