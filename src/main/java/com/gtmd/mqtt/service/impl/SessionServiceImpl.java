package com.gtmd.mqtt.service.impl;

import com.gtmd.mqtt.service.Session;
import com.gtmd.mqtt.service.SessionService;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 * 客户端和服务端可以保存会话状态，以支持跨网络连接的可靠消息传输。这个标志位用于控制会话状态的生存时间。
 *
 * 如果清理会话（CleanSession）标志被设置为0，服务端必须基于当前会话（使用客户端标识符识别）的状态恢复与客户端的通信。
 * 如果没有与这个客户端标识符关联的会话，服务端必须创建一个新的会话。在连接断开之后，当连接断开后，客户端和服务端必须保存会话信息 [MQTT-3.1.2-4]。
 * 当清理会话标志为0的会话连接断开之后，服务端必须将之后的QoS 1和QoS 2级别的消息保存为会话状态的一部分，
 * 如果这些消息匹配断开连接时客户端的任何订阅 [MQTT-3.1.2-5]。
 * 服务端也可以保存满足相同条件的QoS 0级别的消息。
 *
 * 如果清理会话（CleanSession）标志被设置为1，客户端和服务端必须丢弃之前的任何会话并开始一个新的会话。
 * 会话仅持续和网络连接同样长的时间。与这个会话关联的状态数据不能被任何之后的会话重用 [MQTT-3.1.2-6]。
 */
public class SessionServiceImpl implements SessionService {

    private Map<String, Session> sessionStore = new ConcurrentHashMap<>();

    public void cleanSession(String clientId){
        //清除旧session的存储，并关闭旧的连接
        sessionStore.remove(clientId);
        sessionStore.get(clientId).getChannel().close();
    }
    
    @Override
    public Session newSession(String clientId, Channel channel){return null;}

    @Override
    public Session getSession(String clientId){return null;}
    
    @Override
    public Session cleanAndNewSession(String clientId, Channel channel){
        cleanSession(clientId);
        Session session = newSession(clientId, channel);
        saveSession(clientId,session);
        return session;}

    @Override
    public void saveSession(String clientId, Session session){
        sessionStore.put(clientId, session);
        session.setPresent(true);
    }

    @Override
    public boolean containSession(String clientId){
        return sessionStore.containsKey(clientId);
    }
}
