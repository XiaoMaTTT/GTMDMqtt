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
        MqttConnectPayload payload = msg.payload();
        String clientId = payload.clientIdentifier();

        final String username = payload.userName();
        final byte[] password = payload.passwordInBytes();

//        if (authService(username,password)){//auth验证
//            refuseConnect(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD);
//            return;
//        }

        if (!isSupportVersion(variableHeader.version())){
            refuseConnect(MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION);
            return;
        }

//        if (isAllowNullClientId){}后续可以加一个配置项，是否允许空的clientId，如果允许，就生成一个唯一的clientId
        if (clientId == null || clientId.length() == 0){
            refuseConnect(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED);
            return;
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

    private boolean isSupportVersion(int version){
        return (version == MqttVersion.MQTT_3_1.protocolLevel()) ||
                (version == MqttVersion.MQTT_3_1_1.protocolLevel());
    }
}
