多主题发布
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.MTopicsPub 10000 1 0 --参数1表示topic数,参数2表示单topic消息数,参数3 开始段
多主题订阅
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.MTopicsListener 100 100 0--参数1 线程数，参数2 单线程客户端数，参数3 开始段
多主题取消订阅
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.MTopicsListenerCancel 100 100 0
发布的参数要等于订阅的参数1*参数2

------------------
单主题多订阅-发布
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.tt.Publisher 10000 --参数表示消息数
单主题多订阅-订阅
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.ListenerMulti 100 100 0 --参数1 线程数，参数2 单线程客户端数，参数3 开始段
单主题多订阅-取消
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.ListenerCancelMulti 100 100 0 --参数1 线程数，参数2 单线程客户端数，参数3 开始段

eclipse:
发布
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.eclipse.Server 10 --参数1 消息数量
订阅接收
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.eclipse.Client 10000 0 r --参数1订阅数量 参数2 开始断  参数3 r订阅 u取消订阅


nohup java -Xmx10G -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.ListenerMulti 4000 10 0 &