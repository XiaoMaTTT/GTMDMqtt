package com.gtmd.mqtt.handler;

import com.gtmd.mqtt.handler.protos.Connect;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;

public class MessageHandler {

    private Logger log = LoggerFactory.getLogger(MessageHandler.class);

    void messageProcess(ChannelHandlerContext ctx, MqttMessage msg){
        MqttMessageType type = msg.fixedHeader().messageType();
        switch (type){
            case CONNECT:
                new Connect().process(ctx, msg);
                break;
            case SUBSCRIBE:
//                processSubscribe((MqttSubscribeMessage) msg);
                break;
            case UNSUBSCRIBE:
//                processUnsubscribe((MqttUnsubscribeMessage) msg);
                break;
            case PUBLISH:
//                processPublish((MqttPublishMessage) msg);
                break;
            case PUBREC:
//                processPubRec(msg);
                break;
            case PUBCOMP:
//                processPubComp(msg);
                break;
            case PUBREL:
//                processPubRel(msg);
                break;
            case DISCONNECT:
//                processDisconnect(msg);
                break;
            case PUBACK:
//                processPubAck(msg);
                break;
            case PINGREQ:
//                MqttFixedHeader pingHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, AT_MOST_ONCE,
//                        false, 0);
//                MqttMessage pingResp = new MqttMessage(pingHeader);
//                channel.writeAndFlush(pingResp).addListener(CLOSE_ON_FAILURE);
                break;
            default:
                log.error("Unknown MessageType: {}", type);
                break;
//        }
        }

    }
}

