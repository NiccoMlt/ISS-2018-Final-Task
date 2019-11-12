package it.unibo.utils;

import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;

public class mqttUtil {

	private static final String BROKER = "tcp://broker.hivemq.com:1883";

	private static HashMap<String, MqttClient> connections = new HashMap<>();
	
	private static void connect(QActor qa) {
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			MqttClient sampleClient = new MqttClient(BROKER, qa.getName(), persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			qa.println("Connecting to broker: " + BROKER);
			sampleClient.connect(connOpts);
			qa.println("Connected");
			connections.put(qa.getName(), sampleClient);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	private static void subscribe(QActor qa, String topic) {
		try {
			MqttClient sampleClient = connections.get(qa.getName());
			sampleClient.setCallback(new MqttUtilsToQA(qa));
			sampleClient.subscribe(topic);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public static void connectAndSubscribe(QActor qa, String topic) {
		connect(qa);
		subscribe(qa, topic);
	}

	private static void publish(QActor qa, String topic, String msg, int qos)
			throws MqttPersistenceException, MqttException {
		MqttMessage message = new MqttMessage(msg.getBytes());
		message.setQos(qos);
		connections.get(qa.getName()).publish(topic, message);
		qa.println("Message published on topic " + topic);
	}

	public static void sendMsgMqtt(QActor qa, String topic, String msgId, String msgReceiver, String msgPayload) {
		try {
			String msgToPublish = "";
			if (msgReceiver.equals("none")) {
				msgToPublish = QActorUtils.buildEventItem(qa.getName(), msgId, msgPayload).getPrologRep();
			} else {
				msgToPublish = QActorUtils
						.buildMsg(qa.getQActorContext(), qa.getName(), msgId, msgReceiver, "dispatch", msgPayload)
						.getDefaultRep();
			}
			publish(qa, topic, msgToPublish, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void disconnect(QActor qa) {
		try {
			connections.get(qa.getName()).disconnect();
			qa.println("Disconnected");
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	private static class MqttUtilsToQA implements MqttCallback {

		private QActor qa;

		public MqttUtilsToQA(QActor qa) {
			super();
			this.qa = qa;
		}

		public void messageArrived(String topic, MqttMessage msg) {
			String msgID = null;
			String msgType = null;
			String msgSender = null;
			String dest = null;
			String msgcontent = null;
			try {
//	    			 println("	%%% MqttUtils messageArrived on "+ topic + ": "+msg.toString());
				Struct msgt = (Struct) Term.createTerm(msg.toString());
//	 			 println("	%%% MqttUtils messageArrived msgt "+ msgt + " actor=" + actor.getName() ); 
				msgID = msgt.getArg(0).toString();
				msgType = msgt.getArg(1).toString();
				msgSender = msgt.getArg(2).toString();
				dest = msgt.getArg(3).toString();
				msgcontent = msgt.getArg(4).toString();
				if (qa != null) { // send a msg to itself (named without _ctrl)
					if (msgType.equals("event")) {
						qa.emitFromMqtt(msgID, msgcontent);
					} else {
//	 					 qa.sendMsgFromMqtt( msgSender, msgID,  actor.getName().replace("_ctrl", ""),  msgType,  msgcontent);
					}
				}
			} catch (Exception e) {
				qa.println("messageArrived ERROR " + e.getMessage());
			}
		}

		@Override
		public void connectionLost(Throwable cause) {
			qa.println("	%%% MqttUtils connectionLost  = " + cause.getMessage());
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
			// TODO Auto-generated method stub

		}
	}
}
