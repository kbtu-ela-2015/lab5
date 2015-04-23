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
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadWorkers extends Thread{

    private static final String TASK_QUEUE_NAME = "task_queue";
    private static final String TASK_QUEUE_REPLY_NAME = "task_queue_reply";
    
    
    public void run(){
       System.out.println("MyThread running");
       
       ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException ex) {
            Logger.getLogger(ThreadWorkers.class.getName()).log(Level.SEVERE, null, ex);
        }
        Channel channel = null;
        try {
            channel = connection.createChannel();
        } catch (IOException ex) {
            Logger.getLogger(ThreadWorkers.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        } catch (IOException ex) {
            Logger.getLogger(ThreadWorkers.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        try {
            channel.basicQos(1);
        } catch (IOException ex) {
            Logger.getLogger(ThreadWorkers.class.getName()).log(Level.SEVERE, null, ex);
        }

        QueueingConsumer consumer = new QueueingConsumer(channel);
        try {
            channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
        } catch (IOException ex) {
            Logger.getLogger(ThreadWorkers.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
          QueueingConsumer.Delivery delivery = null;
           try {
               delivery = consumer.nextDelivery();
           } catch (InterruptedException ex) {
               Logger.getLogger(ThreadWorkers.class.getName()).log(Level.SEVERE, null, ex);
           } catch (ShutdownSignalException ex) {
               Logger.getLogger(ThreadWorkers.class.getName()).log(Level.SEVERE, null, ex);
           } catch (ConsumerCancelledException ex) {
               Logger.getLogger(ThreadWorkers.class.getName()).log(Level.SEVERE, null, ex);
           }
          String message = new String(delivery.getBody());

          System.out.println(" [x] Received '" + message + "'");   
           try { 
               
               
               doWork(message);
           } catch (InterruptedException ex) {
               Logger.getLogger(ThreadWorkers.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(ThreadWorkers.class.getName()).log(Level.SEVERE, null, ex);
           }
          System.out.println(" [x] Done" );

           try {
               channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
           } catch (IOException ex) {
               Logger.getLogger(ThreadWorkers.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
    }

private static String doWork(String task) throws InterruptedException, IOException {
        //for (char ch: task.toCharArray()) {
          //  if (ch == '.') Thread.sleep(1000);
        //}
        String result="empty result";
        long threadId = Thread.currentThread().getId();
        System.out.println("in thread>>>>"+threadId);
        
        if(task.equalsIgnoreCase("")) return result;
        if(task==null) return result;
        //if(task.charAt(0)=='f' || task.charAt(0)=='1') return result;
        
        if(task.charAt(0)=='f')
            result="factorial of "+task.substring(1)+">>"+factor(task.substring(1)).toString();
        else if(task.charAt(0)=='p'){
            
            if(isPrime(task.substring(1).toString()))
                result=task.substring(1).toString()+" is prime";
            else
                result=task.substring(1).toString()+" is not prime";
        }else{
            result="exponent "+task.substring(1)+">>"+exp(task.substring(1)).toString();
        }
        // sending result
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(TASK_QUEUE_REPLY_NAME, true, false, false, null);
        //String[] msg= {"10","second",".........."};
        //String[] msg= {"1","second","."};
        //String message ="rvr";// getMessage(msg);
        
        channel.basicPublish( "", TASK_QUEUE_REPLY_NAME, 
            MessageProperties.PERSISTENT_TEXT_PLAIN,
            result.getBytes());
        System.out.println(" [x] Sent reply to client:: '" + result + "'");

        channel.close();
        connection.close();
        
        return result;
    }


    private static BigInteger exp(String n){
	  StringTokenizer st = new StringTokenizer(n,"^");
		BigInteger number = new BigInteger(st.nextToken());
		BigInteger pow = new BigInteger(st.nextToken());
		BigInteger ans = BigInteger.ONE;
      BigInteger cnt = BigInteger.ONE;
      
      pow = pow.add(BigInteger.ONE);
       
      
      while(true){
           ans = ans.multiply(number);
           cnt = cnt.add(BigInteger.ONE);
           if(cnt.equals(pow)) break;
       }
      return ans;
  }
    
    private static boolean isPrime(String n){
	  boolean ans;
	  BigInteger number = new BigInteger(n);
		if(number.isProbablePrime(100)==true){
			ans = true;
		}else
			ans = false;
		return ans;
  }
    
    
    private static BigInteger factor(String n){
	  BigInteger ans = BigInteger.ONE;
      BigInteger cnt = BigInteger.ONE;
      BigInteger number = new BigInteger(n);
      number = number.add(BigInteger.ONE);
      while(true){
          ans = ans.multiply(cnt);
          cnt = cnt.add(BigInteger.ONE);
          if(cnt.equals(number)) break;
      }
      return ans;
  }

}
