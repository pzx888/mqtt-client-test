/**
 * 
 */
package example.eclipse;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.eclipse.Server 10
 * eclipse客户端
 * @author pzx
 *
 */
public class Server {
	
	private static final long serialVersionUID = 1L;  
  
    private MqttClient client;  
//    private String host = "tcp://127.0.0.1:1883";  
    private String host = "tcp://192.168.111.15:61621";  
    private String userName = "admin";  
    private String passWord = "password";  
    private MqttTopic topic;  
    private MqttMessage message;  
  
    private String myTopic = "tokudu/94c5e4079d174a44";  
  
    public Server() {  
  
        try {  
            client = new MqttClient(host, "Server",  
                    new MemoryPersistence());  
            connect();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
//        Container container = this.getContentPane();  
//        panel = new JPanel();  
//        button = new JButton("发布话题");  
//        button.addActionListener(new ActionListener() {  
//  
//            @Override  
//            public void actionPerformed(ActionEvent ae) {  
//                try {  
//                    MqttDeliveryToken token = topic.publish(message);  
//                    token.waitForCompletion();  
//                    System.out.println(token.isComplete()+"========");  
//                } catch (Exception e) {  
//                    e.printStackTrace();  
//                }  
//            }  
//        });  
//        panel.add(button);  
//        container.add(panel, "North");  
  
    }  
    
    private void PublishTopic()
    {
    	try
    	{
//    		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    		String str="测试---2aaa-dsfsdfsdfsdfsdfsdfsdfsdfsd--测试";
//    		while(true)
//    		{
//    			str = br.readLine();
//        		if(str.equalsIgnoreCase("exit"))
//        		{
//        			System.exit(0);
//        			return;
//        		}
        		
        		//if(message==null)
        		{
        			message = new MqttMessage();  
                    message.setQos(1);  
                    message.setRetained(true);  
                    System.out.println(message.isRetained()+"------ratained状态");  
        		}
        		
                message.setPayload(str.getBytes("UTF-8"));  
                
                MqttDeliveryToken token = topic.publish(message);  
                token.waitForCompletion();  
//                System.out.println(token.isComplete()+"========"); 
    		//}
    		 
        } 
    	catch (Exception e) 
    	{  
            e.printStackTrace();  
        } 
    }
  
    private void connect() {  
  
        MqttConnectOptions options = new MqttConnectOptions();  
        options.setCleanSession(false);  
        options.setUserName(userName);  
        options.setPassword(passWord.toCharArray());  
        // 设置超时时间  
        options.setConnectionTimeout(30);  //10
        // 设置会话心跳时间  
        options.setKeepAliveInterval(300);  //20
        try {  
            client.setCallback(new MqttCallback() {  
  
                @Override  
                public void connectionLost(Throwable cause) {  
                    System.out.println("connectionLost-----------");  
                }  
  
                @Override  
                public void deliveryComplete(IMqttDeliveryToken token) {  
                    System.out.println("deliveryComplete---------"+token.isComplete());  
                }  
  
                @Override  
                public void messageArrived(String topic, MqttMessage arg1)  
                        throws Exception {  
                    System.out.println("messageArrived----------");  
  
                }  
            });  
  
            topic = client.getTopic(myTopic);  
  
            client.connect(options);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int messages = Integer.parseInt(arg(args, 0, "10"));
		try
		{
			Server s = new Server();  
			for(int i=0;i<messages;i++)
			s.PublishTopic();
		}
		catch(Exception exp)
		{
			
		}
		

	}
	private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }

}

