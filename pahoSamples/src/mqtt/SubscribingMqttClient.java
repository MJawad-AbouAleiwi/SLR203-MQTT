import org.eclipse.paho.client.mqttv3.*;

public class SubscribingMqttClient implements MqttCallback {
    private MqttClient mqttClient;

    public static void main(String[] args) {
        new SubscribingMqttClient().run();
    }

    public void run() {
        String topic = "labs/paho-example-topic";
        int qos = 0;
        boolean cleanSession = true;
        String brokerURI = "tcp://localhost:1883";
        String clientId = "mj_sub";

        try {
            mqttClient = new MqttClient(brokerURI, clientId);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(cleanSession);
            mqttClient.setCallback(this);
            mqttClient.connect(connectOptions);
            System.out.println("Successfully Connected to broker.");

            mqttClient.subscribe(topic, qos);
            System.out.println("Successfully subscribed to the topic.");
            while (true) {
                System.out.println("Waiting for messages...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }
            }

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Topic Received: " + topic);
        System.out.println("Message Received: " + new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery complete: " + token.isComplete());
    }
}