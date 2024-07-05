import org.eclipse.paho.client.mqttv3.*;

import static java.lang.Thread.sleep;

public class MqttClientMeasure {

    public static void main(String[] args) {

        String[] topics = {
            "/home/Lyon/sido/sht30/value",
            "/home/Lyon/sido/sht30/value2",
            "/home/Lyon/sido/dht22/value",
            "/home/Lyon/sido/dht22/value2"
        };

        int qos = 0;
        boolean cleanSession = true;
        boolean retained = true;
        String broker = "ws://localhost:9001";
        String clientId = "mj_sensors";

        try (MqttClient mqttClient = new MqttClient(broker, clientId))
        {

            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(cleanSession);
            System.out.println("Connecting to Mqtt Broker.");
            mqttClient.connect(connectOptions);
            System.out.println("Sucessfully Connected.");
            
            //record 4 values every 1 s for 5 s 
            for(int t = 0; t < 5; t++) 
            {
                for (int i = 0; i < 4; i++) {
                    int measured_value = (int) (Math.random() * 75);
                    String measured_value_string = String.valueOf(measured_value);
                    System.out.println("Measuring value " + measured_value + " to topic: " + topics[i]);

                    MqttMessage message = new MqttMessage(measured_value_string.getBytes());
                    message.setQos(qos);
                    message.setRetained(retained);
                    mqttClient.publish(topics[i], message);
                    System.out.println("Successfully the value is measured.");
                    System.out.println();
                }
                try{
                sleep(1000);
                }
                catch (Exception e)
                {

                }
            }
            mqttClient.disconnect();
            System.out.println("Disconnected.");

        } 
        catch (MqttException e) {
            e.printStackTrace();
        } 
    }
}
