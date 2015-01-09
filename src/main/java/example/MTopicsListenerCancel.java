/**
 * 
 */
package example;

import java.net.URISyntaxException;
import java.util.LinkedList;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

/**
 * java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.MTopicsListenerCancel 100 100 0
 * 多主题订阅
 * @author pzx
 *
 */
public class MTopicsListenerCancel extends Thread{
	private int num;
	private int per;
	public MTopicsListenerCancel(int num,int per){
		this.num = num;
		this.per = per;
	}
	public void run(){
		int per = this.per;
		for(int j=0;j<per;j++){
			Task3 task = new Task3();
		try {
			task.regis(num*per+j);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		
	}

    public static void main(String []args) throws Exception {
    	int count = Integer.parseInt(arg(args, 0, "10"));
    	int per = Integer.parseInt(arg(args, 1, "10"));
    	int start = Integer.parseInt(arg(args, 2, "0"));
    	int from = start * count;
    	int to = from + count;
    	for(int i=from;i<to;i++){
    		
    		MTopicsListenerCancel t = new MTopicsListenerCancel(i,per);
    		t.start();
    		Thread.sleep(100);
    	}
    	synchronized (MTopicsListenerCancel.class) {
            while(true)
  			try {
  				System.out.println("get the lock ");
  				MTopicsListenerCancel.class.wait();
  				System.out.println("after wait ");
  			} catch (InterruptedException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
        }

    }
    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }

    
   
}
class Task3{
	
	String user = env("ACTIVEMQ_USER", "guest");
    String password = env("ACTIVEMQ_PASSWORD", "guest");
    String host = env("ACTIVEMQ_HOST", "192.168.111.15");
    int port = Integer.parseInt(env("ACTIVEMQ_PORT", "1883"));
    
    
    public Task3(){
    	super();
    }

    public void regis(int i) throws URISyntaxException, InterruptedException{
    	MQTT mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setUserName(user);
        mqtt.setPassword(password);
        mqtt.setClientId("cccc"+i);
        mqtt.setCleanSession(false);
        final int num = i;
        final String destination = "pan"+i;


        final CallbackConnection connection = mqtt.callbackConnection();
        
        connection.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                UTF8Buffer[] topics1 = {new UTF8Buffer(destination)};
                connection.unsubscribe(topics1, new Callback<Void>() {//注销
                    public void onSuccess(Void qoses) {
                    }
                    public void onFailure(Throwable value) {
                        value.printStackTrace();
                        //System.exit(-2);
                    }
                });
            }
            @Override
            public void onFailure(Throwable value) {
                value.printStackTrace();
                //System.exit(-2);
            }
        });
        
        connection.listener(new org.fusesource.mqtt.client.Listener() {
            long count = 0;
            long start = System.currentTimeMillis();

            public void onConnected() {
            }
            public void onDisconnected() {
            }
            public void onFailure(Throwable value) {
                value.printStackTrace();
                //System.exit(-2);
            }
            public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
                String body = msg.utf8().toString();
                
                    if( count == 0 ) {
                        start = System.currentTimeMillis();
                    }
                    //if( count % 10 == 0 ) {
                        System.out.println(String.format(num+"Received %d messages.", count));
                    //}
//                    if( count >= 9999 ) {
//                        System.out.println(String.format(num+"Received %d messages.", count));
//                    }
                    count ++;
                
                ack.run();
            }
        });

    }
    
    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }

    
}
