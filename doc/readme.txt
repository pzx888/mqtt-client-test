�����ⷢ��
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.MTopicsPub 10000 1 0 --����1��ʾtopic��,����2��ʾ��topic��Ϣ��,����3 ��ʼ��
�����ⶩ��
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.MTopicsListener 100 100 0--����1 �߳���������2 ���߳̿ͻ�����������3 ��ʼ��
������ȡ������
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.MTopicsListenerCancel 100 100 0
�����Ĳ���Ҫ���ڶ��ĵĲ���1*����2

------------------
������ඩ��-����
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.tt.Publisher 10000 --������ʾ��Ϣ��
������ඩ��-����
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.ListenerMulti 100 100 0 --����1 �߳���������2 ���߳̿ͻ�����������3 ��ʼ��
������ඩ��-ȡ��
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.ListenerCancelMulti 100 100 0 --����1 �߳���������2 ���߳̿ͻ�����������3 ��ʼ��

eclipse:
����
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.eclipse.Server 10 --����1 ��Ϣ����
���Ľ���
java -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.eclipse.Client 10000 0 r --����1�������� ����2 ��ʼ��  ����3 r���� uȡ������


nohup java -Xmx10G -classpath ./mqtt-example-0.1-SNAPSHOT.jar example.ListenerMulti 4000 10 0 &