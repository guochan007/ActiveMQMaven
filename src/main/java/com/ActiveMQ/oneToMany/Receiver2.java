package com.ActiveMQ.oneToMany;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

//玩法：先启接收receiver1 2  然后再启sender   顺序反了 就收不到信息
public class Receiver2 {
    public static void main(String[] args) {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        Topic topic;
        // 消费者，消息接收者
        MessageConsumer consumer;
        connectionFactory = new ActiveMQConnectionFactory(
        		"tcp://localhost:61616");
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
          //此处修改为topic才能支持1对多发信息
            topic = session.createTopic("FirstTopic");
            consumer = session.createConsumer(topic);
            
        	MessageListener ml = new MessageListener() {
        		// 设置监听器
        		public void onMessage(Message message) {
        			TextMessage textMsg = (TextMessage) message;
        			try {
        				System.out.println("收到消息:" + textMsg.getText());
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		}
        	};
        	while(true){
        		consumer.setMessageListener(ml);
        	}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }
}
