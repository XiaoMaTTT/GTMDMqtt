package com.gtmd.mqtt.service;

import io.netty.channel.Channel;

/**
 * @Author yuyongchao
 **/
public interface SessionService {
    public Session newSession(String clientId, Channel channel);

    public Session getSession(String clientId);

    public Session cleanAndNewSession(String clientId, Channel channel);

    public void saveSession(String clientId, Session session);

    public boolean containSession(String clientId);
}
