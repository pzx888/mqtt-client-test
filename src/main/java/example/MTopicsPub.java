/**
 * 
 */
package example;

import java.util.LinkedList;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

/**
 * java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.MTopicsPub 10000 1 0
 * 多主题发布
 * @author pzx
 *
 */
public class MTopicsPub {
	public static void main(String []args) throws Exception {

        String user = env("ACTIVEMQ_USER", "guest");
        String password = env("ACTIVEMQ_PASSWORD", "guest");
        String host = env("ACTIVEMQ_HOST", "192.168.111.15");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "1883"));

        

        
        
        String DATA = "测试---2aaa-dsfsdfsdfsdfsdfsdfsdfsdfsd--测试";
        String body =DATA;// "";

        Buffer msg =  new UTF8Buffer(body); // new AsciiBuffer(body);
        int count = Integer.parseInt(arg(args, 0, "100"));
        int messages = Integer.parseInt(arg(args, 1, "1"));
        int start = Integer.parseInt(arg(args, 2, "0"));
        int from = start * count;
    	int to = from + count;
        
        for(int j = from; j<to; j++){
        	String destination = "pan"+j;
        	MQTT mqtt = new MQTT();
            mqtt.setHost(host, port);
            mqtt.setUserName(user);
            mqtt.setPassword(password);
            FutureConnection connection = mqtt.futureConnection();
            connection.connect().await();
            final LinkedList<Future<Void>> queue = new LinkedList<Future<Void>>();
            UTF8Buffer topic = new UTF8Buffer(destination);
            for( int i=1; i <= messages; i ++) {
            	Buffer msgi =  new UTF8Buffer(body+ "--"+i);
            	 connection.publish(topic, msgi, QoS.AT_LEAST_ONCE, true);
            	 //if( i % 1000 == 0 ) {
                    System.out.println(String.format("Sent %d messages.", i));
                //}
            }

            connection.disconnect().await();
        }
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
