/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dauren
 */

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReplyUtil extends Thread{
     private static final String TASK_QUEUE_REPLY_NAME = "task_queue_reply";
     
     public void run(){
     
         try {
                             
                 getReply();
                 System.out.println("a");
             
             
         } catch (IOException ex) {
             //Logger.getLogger(ReplyUtil.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("eerroor in getReply()");
         } catch (InterruptedException ex) {
             //Logger.getLogger(ReplyUtil.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("eerroor in getReply()");
         }
     }
     
     public void getReply() throws IOException, InterruptedException{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_REPLY_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(1);

        QueueingConsumer consumer2 = new QueueingConsumer(channel);
        channel.basicConsume(TASK_QUEUE_REPLY_NAME, false, consumer2);

        while (true) {
          QueueingConsumer.Delivery delivery = consumer2.nextDelivery();
          String message = new String(delivery.getBody());

          System.out.println(" [x] Receiving reply from worker::'" + message + "'");   
          //doWork(message); 
          //System.out.println(" [x] Done" );

          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
          
        }
    
}
}