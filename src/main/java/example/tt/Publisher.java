package example.tt;


import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.DispatchQueue;
import org.fusesource.hawtdispatch.DispatchQueueProxy;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.util.LinkedList;

/**
 * java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.tt.Publisher 10000
 * Uses a Future based API to MQTT.
 */
class Publisher {

    public static void main(String []args) throws Exception {

        String user = env("ACTIVEMQ_USER", "guest");
        String password = env("ACTIVEMQ_PASSWORD", "guest");
        String host = env("ACTIVEMQ_HOST", "192.168.111.15");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "1883"));
    ///tokudu/aa2ca4b7b8399a58
       // final String destination = arg(args, 0, "androi/bb2ca4b7b8399a58");///topic/event
        final String destination = "pan";

        int messages = Integer.parseInt(arg(args, 0, "2"));
        int size = 256;

        String DATA = "测试---2aaa-dsfsdfsdfsdfsdfsdfsdfsdfsd--测试";
        String body =DATA;// "";
//        for( int i=0; i < size; i ++) {
//            body += DATA.charAt(i%DATA.length());
//        }
       Buffer msg =  new UTF8Buffer(body); // new AsciiBuffer(body);

        MQTT mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setUserName(user);
        mqtt.setPassword(password);
        //DispatchQueue  dq = DispatchQueueProxy.create(interfaceClass, target, queue);
        
        
//        mqtt.setClientId("ssss");
//        mqtt.setCleanSession(false);
//        
//        mqtt.setWillRetain(true);
//        mqtt.setWillTopic("uid");

        FutureConnection connection = mqtt.futureConnection();
        connection.connect().await();
        
 
       

        final LinkedList<Future<Void>> queue = new LinkedList<Future<Void>>();
        
        
        UTF8Buffer topic = new UTF8Buffer(destination);
        for( int i=1; i <= messages; i ++) {

            // Send the publish without waiting for it to complete. This allows us
            // to send multiple message without blocking..
//            queue.add(connection.publish(topic, msg, QoS.AT_LEAST_ONCE, true));
        	 Buffer msgi =  new UTF8Buffer(body+ "--"+i);
        	 connection.publish(topic, msgi, QoS.AT_LEAST_ONCE, true);
        	 
       
//            System.out.println(body);
   

            // Eventually we start waiting for old publish futures to complete
            // so that we don't create a large in memory buffer of outgoing message.s
//            if( queue.size() >= 1000 ) {
//                queue.removeFirst().await();
//            }

            //if( i % 1000 == 0 ) {
                System.out.println(String.format("Sent %d messages.", i));
            //}
        }

//        queue.add(connection.publish(topic, new AsciiBuffer("SHUTDOWN"), QoS.AT_LEAST_ONCE, false));
//        while( !queue.isEmpty() ) {
//            queue.removeFirst().await();
//        }
//
        connection.disconnect().await();

//        System.exit(0);
    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }

    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }

}