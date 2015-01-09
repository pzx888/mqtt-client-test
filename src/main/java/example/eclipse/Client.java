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
                       //hostΪ��������testΪclientid������MQTT�Ŀͻ���ID��һ���Կͻ���Ψһ��ʶ����ʾ��MemoryPersistence����clientid�ı�����ʽ��Ĭ��Ϊ���ڴ汣��
            client = new MqttClient(host, this.clientID,
                    new MemoryPersistence());
                       //MQTT����������
            options = new MqttConnectOptions();
                       //�����Ƿ����session,�����������Ϊfalse��ʾ�������ᱣ���ͻ��˵����Ӽ�¼����������Ϊtrue��ʾÿ�����ӵ������������µ��������
            options.setCleanSession(false);
                       //�������ӵ��û���
            options.setUserName(userName);
                       //�������ӵ�����
            options.setPassword(passWord.toCharArray());
            // ���ó�ʱʱ�� ��λΪ��
            options.setConnectionTimeout(30);//10
            // ���ûỰ����ʱ�� ��λΪ�� ��������ÿ��1.5*20���ʱ����ͻ��˷��͸���Ϣ�жϿͻ����Ƿ����ߣ������������û�������Ļ���
            options.setKeepAliveInterval(300);//20
//            topic = client.getTopic(myTopic);
//            options.setWill(topic, "test".getBytes(), 1, true);
            final String cid = this.clientID;
                        //���ûص�
            client.setCallback(new MqttCallback() {
 
                @Override
                public void connectionLost(Throwable cause) {
                                        //���Ӷ�ʧ��һ�����������������
                    System.out.println("connectionLost----------");
                }
 
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                                        //publish���ִ�е�����
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());
                }
 
                @Override
                public void messageArrived(String topicName, MqttMessage message)
                        throws Exception {
                                        //subscribe��õ�����Ϣ��ִ�е�������
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

