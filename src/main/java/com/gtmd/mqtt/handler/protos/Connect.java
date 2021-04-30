package com.gtmd.mqtt.handler.protos;

import com.gtmd.mqtt.service.Session;
import com.gtmd.mqtt.service.SessionService;
import io.netty.buffer.Unpooled;
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

    private final SessionService sessionService = new SessionService();

    private ChannelHandlerContext ctx;

    @Override
    public void process(ChannelHandlerContext ctx, MqttMessage message) {
        this.ctx = ctx;
        MqttConnectMessage msg = (MqttConnectMessage) message;

        MqttConnectVariableHeader variableHeader = msg.variableHeader();
        MqttConnectPayload payload = msg.payload();
        String clientId = payload.clientIdentifier();

        final boolean isCleanSession = variableHeader.isCleanSession();

        final String username = payload.userName();
        final byte[] password = payload.passwordInBytes();

        //关于会话的处理，这里面还缺少,session是否准备好，消息重发、消息暂存等方面的逻辑
        Session session;
        if (isCleanSession){
            session = sessionService.cleanAndNewSession(clientId, ctx.channel());
        }
        else {
            if (!sessionService.containSession(clientId)){
                session = sessionService.newSession(clientId, ctx.channel());
                sessionService.saveSession(clientId, session);
            } else {
                session = sessionService.getSession(clientId);
            }
        }

        //auth验证
//        if (authService(username,password)){
//            refuseConnect(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD);
//            return;
//        }

        //协议版本验证
        if (!isSupportVersion(variableHeader.version())){
            refuseConnect(MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION);
            return;
        }

//        if (isAllowNullClientId){}后续可以加一个配置项，是否允许空的clientId，如果允许，就生成一个唯一的clientId
        if (clientId == null || clientId.length() == 0){
            refuseConnect(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED);
            return;
        }

//        遗嘱消息的处理
        if (variableHeader.isWillFlag()){
            //moquette中把遗嘱消息定义成了Session中的一个静态内部类，只有topic、qos、payload、retained四个字段，u1s1，这么整更简洁，是否要改考虑一下
            MqttPublishMessage willMsg = new MqttPublishMessage(
                    new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.valueOf(msg.variableHeader().willQos()), msg.variableHeader().isWillRetain(), 0),
                    new MqttPublishVariableHeader(msg.payload().willTopic(), 0), Unpooled.buffer().writeBytes(payload.willMessageInBytes()));

            session.setWillMessage(willMsg);
        }

        final MqttConnAckMessage ackMessage = MqttMessageBuilders.connAck()
                .returnCode(CONNECTION_ACCEPTED)
                .sessionPresent(session.isPresent())
                .build();

        ctx.writeAndFlush(ackMessage);
    }

    private void refuseConnect(MqttConnectReturnCode code){
        MqttConnAckMessage refuseProto = MqttMessageBuilders.connAck()
                .returnCode(code)
                .sessionPresent(false).build();
        ctx.writeAndFlush(refuseProto);
    }

    private boolean isSupportVersion(int version){
        return (version == MqttVersion.MQTT_3_1.protocolLevel()) ||
                (version == MqttVersion.MQTT_3_1_1.protocolLevel());
    }
}
