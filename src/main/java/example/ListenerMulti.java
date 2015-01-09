/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example;

import java.net.URISyntaxException;

import org.fusesource.hawtbuf.*;
import org.fusesource.mqtt.client.*;

/**
 *  java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.ListenerMulti 100 100 0
 * Uses an callback based interface to MQTT.  Callback based interfaces
 * are harder to use but are slightly more efficient.
 */
public class ListenerMulti extends Thread{
	private int num;
	private int per;
	public ListenerMulti(int num, int per){
		this.num = num;
		this.per = per;
	}
	public void run(){
		int per = this.per;
		int i = 0;
		for(int j=0;j<per;j++){
		Task task = new Task();
		try {
			task.regis(num*per+j);
			Thread.sleep(10);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		}
		//synchronized (ListenerMulti.class) {
//          while(true)
//			try {
//				i++;
//				
//				synchronized (ListenerMulti.class) {
//					System.out.println(this.num+"get the lock "+i);
//				ListenerMulti.class.wait();
//				System.out.println(this.num+"after wait "+i);
//				}
//				//System.out.println(this.num+"thread sleep "+i++);
//				//Thread.sleep(Integer.MAX_VALUE); 
//			} catch (InterruptedException e) {
//				//e.printStackTrace();
//			}
		//}
	}

    public static void main(String []args) throws Exception {
    	int count = Integer.parseInt(arg(args, 0, "2000"));
    	int per = Integer.parseInt(arg(args, 1, "10"));
    	int start = Integer.parseInt(arg(args, 2, "0"));
    	int from = start * count;
    	int to = from + count;
    	for(int i=from;i<to;i++){
    		
    		ListenerMulti t = new ListenerMulti(i,per);
    		t.start();
    		Thread.sleep(100);
    		
    	}
    	while(true)
			try {
				
				synchronized (ListenerMulti.class) {
				System.out.println("get the lock ");
				ListenerMulti.class.wait();
				System.out.println("after wait ");
				}
				//System.out.println(this.num+"thread sleep "+i++);
				//Thread.sleep(Integer.MAX_VALUE); 
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}

    }
    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }

    
   
}
class Task{
	
	String user = env("ACTIVEMQ_USER", "guest");
    String password = env("ACTIVEMQ_PASSWORD", "guest");
    String host = env("ACTIVEMQ_HOST", "192.168.111.15");
    int port = Integer.parseInt(env("ACTIVEMQ_PORT", "1883"));
    final String destination = "pan";
    
    public Task(){
    	super();
    }

    public void regis(int i) throws URISyntaxException, InterruptedException{
    	MQTT mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setUserName(user);
        mqtt.setPassword(password);
        mqtt.setClientId("dddd"+i);
        mqtt.setCleanSession(false);
        final int num = i;


        final CallbackConnection connection = mqtt.callbackConnection();
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
                if( "SHUTDOWN".equals(body)) {
                    long diff = System.currentTimeMillis() - start;
                    System.out.println(String.format("Received %d in %.2f seconds", count, (1.0*diff/1000.0)));
                    connection.disconnect(new Callback<Void>() {
                        @Override
                        public void onSuccess(Void value) {
                            System.exit(0);
                        }
                        @Override
                        public void onFailure(Throwable value) {
                            value.printStackTrace();
                            System.exit(-2);
                        }
                    });
                } else {
                    if( count == 0 ) {
                        start = System.currentTimeMillis();
                    }
                    //if( count % 1000 == 0 ) {
                        System.out.println(String.format(num+"Received %d messages.", count));
                    //}
//                    if( count >= 9999 ) {
//                        System.out.println(String.format(num+"Received %d messages.", count));
//                    }
                    count ++;
                }
                ack.run();
            }
        });
        connection.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                Topic[] topics = {new Topic(destination, QoS.AT_LEAST_ONCE)};
                connection.subscribe(topics, new Callback<byte[]>() {
                    public void onSuccess(byte[] qoses) {
                    }
                    public void onFailure(Throwable value) {
                        value.printStackTrace();
                        //System.exit(-2);
                    }
                });
//                UTF8Buffer[] topics1 = {new UTF8Buffer(destination)};
//                connection.unsubscribe(topics1, new Callback<Void>() {//×¢Ïú
//                    public void onSuccess(Void qoses) {
//                    }
//                    public void onFailure(Throwable value) {
//                        value.printStackTrace();
//                        System.exit(-2);
//                    }
//                });
            }
            @Override
            public void onFailure(Throwable value) {
                value.printStackTrace();
                //System.exit(-2);
            }
        });

        // Wait forever..
//        synchronized (Task.class) {
//            while(true)
//            	Task.class.wait();
//        }
    }
    
    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }

    
}