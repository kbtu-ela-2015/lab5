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

public class NewTask {
    private static final String TASK_QUEUE_NAME = "task_queue";
    private static final String TASK_QUEUE_REPLY_NAME = "task_queue_reply";
    
    public static void main(String[] argv) throws java.io.IOException {
    
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        String[] msg= {"10","second",".........."};
        //String[] msg= {"1","second","."};
        String message = getMessage(msg);
        
        channel.basicPublish( "", TASK_QUEUE_NAME, 
            MessageProperties.PERSISTENT_TEXT_PLAIN,
            message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
        
        // waiting reply from workers
        try {
            
            getReply();
            
        } catch (InterruptedException ex) {
            Logger.getLogger(NewTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //end reply
    }
    
    
public static void getReply() throws IOException, InterruptedException{
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

          System.out.println(" [x] Received reply from worker::'" + message + "'");   
          //doWork(message); 
          //System.out.println(" [x] Done" );

          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    
}
    
    
    
    
    
    
private static String getMessage(String[] strings){
    if (strings.length < 1)
        return "Hello World!";
    return joinStrings(strings, " ");
}

private static String joinStrings(String[] strings, String delimiter) {
    int length = strings.length;
    if (length == 0) return "";
    StringBuilder words = new StringBuilder(strings[0]);
    for (int i = 1; i < length; i++) {
        words.append(delimiter).append(strings[i]);
    }
    return words.toString();
}
}


