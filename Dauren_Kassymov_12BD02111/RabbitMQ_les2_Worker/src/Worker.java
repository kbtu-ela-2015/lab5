/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dauren
 */
//import java.io.IOException;


public class Worker{
    
    //private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv){
        ThreadWorkers myThread = new ThreadWorkers();
        myThread.start();
        
        ThreadWorkers myThread2 = new ThreadWorkers();
        myThread2.start();
        
        ThreadWorkers myThread3 = new ThreadWorkers();
        myThread3.start();
        
     /*   
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(1);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(TASK_QUEUE_NAME, false, consumer);

        while (true) {
          QueueingConsumer.Delivery delivery = consumer.nextDelivery();
          String message = new String(delivery.getBody());

          System.out.println(" [x] Received '" + message + "'");   
          doWork(message); 
          System.out.println(" [x] Done" );

          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
      */
        
        
        
  }
    
   
}