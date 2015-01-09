package example.eclipse;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
 
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
 

 
/**
 * java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.eclipse.Client 10000 r
 * @author pzx
 *
 */
public class Client {
 
    
 
    private String host = "tcp://192.168.0.117:1883";
    private String userName = "admin";
    private String passWord = "password";
    private String clientID;
    private String type;
 
    private MqttTopic topic;
 
    private MqttClient client;
 
    private String myTopic = "tokudu/94c5e4079d174a44"; 
 
    private MqttConnectOptions options;
 
    private ScheduledExecutorService scheduler;
 
    public Client(String clientID,String type ){
    	this.clientID = clientID;
    	this.type = type;
    	init();
    	connect();
    }
 
    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
 
            @Override
            public void run() {
                if(!client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }
 
    private void init() {
        try {
                       //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(host, this.clientID,
                    new MemoryPersistence());
                       //MQTT的连接设置
            options = new MqttConnectOptions();
                       //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(false);
                       //设置连接的用户名
            options.setUserName(userName);
                       //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(30);//10
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(300);//20
//            topic = client.getTopic(myTopic);
//            options.setWill(topic, "test".getBytes(), 1, true);
            final String cid = this.clientID;
                        //设置回调
            client.setCallback(new MqttCallback() {
 
                @Override
                public void connectionLost(Throwable cause) {
                                        //连接丢失后，一般在这里面进行重连
                    System.out.println("connectionLost----------");
                }
 
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                                        //publish后会执行到这里
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());
                }
 
                @Override
                public void messageArrived(String topicName, MqttMessage message)
                        throws Exception {
                                        //subscribe后得到的消息会执行到这里面
                    System.out.println(cid+"messageArrived----------"+new String(message.getPayload(),"UTF-8"));
                    
                }
            });
           
//            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    private void connect() {
//    	final String clid = this.clientID;
//    	final String type = this.type;
//        new Thread(new Runnable() {
// 
//            @Override
//            public void run() {
                try {
                	
                    client.connect(options);
                    if(this.type.equals("r"))
                    	client.subscribe(myTopic, 1);
                    else
                    	client.unsubscribe(myTopic);
                    System.out.println("connect----------"+this.clientID);
                } catch (Exception e) {
                    e.printStackTrace();
                    
                }
//            }
//        }).start();
    }
 
    public static void main(String[] args) throws InterruptedException{
    	Client c;
    	int size = Integer.parseInt(arg(args, 0, "500"));
    	String type = arg(args, 2, "u");
    	int start = Integer.parseInt(arg(args, 1, "1"));
    	int from = start * size;
    	int to = from + size;
    	for(int i=from;i<to;i++){
    		c = new Client("eclipse"+i,type);
    		Thread.sleep(20);
    	}
    		
    }
    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }
}

