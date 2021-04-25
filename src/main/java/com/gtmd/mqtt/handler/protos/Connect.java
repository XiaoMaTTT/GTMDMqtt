package com.gtmd.mqtt.handler.protos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;

import java.util.Optional;

import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_ACCEPTED;

/**
 * @author admin
 */
public class Connect implements BaseMessageHandler {

    private final Logger log = LoggerFactory.getLogger(Connect.class);

    private ChannelHandlerContext ctx;

    @Override
    public void process(ChannelHandlerContext ctx, MqttMessage message) {
        this.ctx = ctx;
        MqttConnectMessage msg = (MqttConnectMessage) message;
        MqttConnectVariableHeader variableHeader = msg.variableHeader();
        //先列出可变头中的数据

        MqttConnectPayload payload = msg.payload();

        //再列出payload中的数据

        String clientId = payload.clientIdentifier();
        if (!Optional.ofNullable(clientId).isPresent()){
            //生成一个唯一的clientId
        }
        final String username = payload.userName();
        final byte[] password = payload.passwordInBytes();
        log.trace("process connect message. ClientID:{} ,username:{}",clientId, username);


        if (true){
            refuseConnect(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED);
        }

//        boolean isSessionAlreadyPresent = !msgCleanSessionFlag && result.alreadyStored;
        final MqttConnAckMessage ackMessage = MqttMessageBuilders.connAck()
                .returnCode(CONNECTION_ACCEPTED)
//                .sessionPresent(isSessionAlreadyPresent)
                .build();

        ctx.writeAndFlush(ackMessage);
    }

    private void refuseConnect(MqttConnectReturnCode code){
        MqttConnAckMessage refuseProto = MqttMessageBuilders.connAck()
                .returnCode(code)
                .sessionPresent(false).build();
        ctx.writeAndFlush(refuseProto);
    }
}
