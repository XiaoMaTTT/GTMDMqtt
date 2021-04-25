package com.gtmd.mqtt.handler.protos;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;

public class PingReq implements BaseMessageHandler {
    @Override
    public void process(ChannelHandlerContext ctx, MqttMessage message) {

    }
}
