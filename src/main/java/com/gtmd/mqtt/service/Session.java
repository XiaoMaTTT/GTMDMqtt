package com.gtmd.mqtt.service;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

import java.io.Serializable;

/**
 * @author admin
 */
public class Session implements Serializable {

    private static final long serialVersionUID = 8407152335876505029L;

    private String clientId;

    private Channel channel;

    private boolean present;

    private boolean cleanSession;

    private MqttPublishMessage willMessage;

    public Session(String clientId, Channel channel, boolean cleanSession, MqttPublishMessage willMessage) {
        this.clientId = clientId;
        this.channel = channel;
        this.cleanSession = cleanSession;
        this.willMessage = willMessage;
        this.present = false;
    }

    public Session() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public MqttPublishMessage getWillMessage() {
        return willMessage;
    }

    public void setWillMessage(MqttPublishMessage willMessage) {
        this.willMessage = willMessage;
    }
}
