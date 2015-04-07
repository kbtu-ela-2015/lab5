package com.cloudamqp.androidexample;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import com.cloudamqp.R;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;

import android.app.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityHome extends Activity {
    Connection connection;
    Channel channel;
    QueueingConsumer consumer;
    private boolean isConnected=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        	setupConnectionFactory();		
		publishToAMQP();
		setupPubButton();
		final Handler incomingMessageHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String message = msg.getData().getString("msg");
				TextView tv = (TextView) findViewById(R.id.textView);
				Date now = new Date();
				SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
				tv.append(ft.format(now) + ' ' + message + '\n');
			}
		};
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    connection = factory.newConnection();
                    channel = connection.createChannel();
                    channel.basicQos(1);
                    DeclareOk q = channel.queueDeclare();
                    channel.queueBind(q.getQueue(), "amq.fanout", "chat");
                    consumer = new QueueingConsumer(channel);
                    channel.basicConsume(q.getQueue(), true, consumer);
                    return Boolean.TRUE;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return Boolean.FALSE;
            }
            @Override
            public void onPostExecute(Boolean result){
                if (result.equals(Boolean.TRUE))
                    isConnected=true;
            }
        }.execute();
		subscribe(incomingMessageHandler, subscribeThread1);
        subscribe(incomingMessageHandler,subscribeThread2);
        subscribe(incomingMessageHandler,subscribeThread3);
        subscribe(incomingMessageHandler,subscribeThread4);
	}

	void setupPubButton() {
		Button button = (Button) findViewById(R.id.publish);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText et = (EditText) findViewById(R.id.text);
				publishMessage(et.getText().toString());
				et.setText("");
			}
		});
	}
	
	Thread subscribeThread1,subscribeThread2, subscribeThread3, subscribeThread4;
	Thread publishThread;
	@Override
	protected void onDestroy() {
		super.onDestroy();
		publishThread.interrupt();
		subscribeThread1.interrupt();
        subscribeThread2.interrupt();
	}
	
	private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();
	void publishMessage(String message) {
		//Adds a message to internal blocking queue
		try {
			Log.d("","[q] " + message);
			queue.putLast(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	ConnectionFactory factory = new ConnectionFactory();
	private void setupConnectionFactory() {
		String uri = "amqp://hvoqytyq:w-5W-Dnw1CSWkjsTneYgdKIS891bDWRh@lemur.cloudamqp.com/hvoqytyq";
		try {
			factory.setAutomaticRecoveryEnabled(false);	
			factory.setUri(uri);

		} catch (KeyManagementException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1){
            e1.printStackTrace();
        } catch (URISyntaxException e1){
            e1.printStackTrace();
        }

    }

	void subscribe(final Handler handler,Thread thread)
	{
        thread = new Thread(new Runnable() {
			@Override
			public void run() {
						while (true) {
                            if (!isConnected)
                                continue;
                            QueueingConsumer.Delivery delivery = null;
                            try {
                                delivery = consumer.nextDelivery();
                                String message = new String(delivery.getBody());
                                Log.d("","[r] " + message);
                                message = factorial(new BigInteger(message)).toString();

                                Message msg = handler.obtainMessage();
                                Bundle bundle = new Bundle();

                                bundle.putString("msg", message);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


						}
			}
		});
		thread.start();
	}

	public void publishToAMQP()
	{
		publishThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Connection connection = factory.newConnection();
						Channel ch = connection.createChannel();
						ch.confirmSelect();

						while (true) {
							String message = queue.takeFirst();
							try{
								ch.basicPublish("amq.fanout", "chat", null, message.getBytes());
								Log.d("", "[s] " + message);
								ch.waitForConfirmsOrDie();
							} catch (Exception e){
								Log.d("","[f] " + message);
								queue.putFirst(message);
								throw e;
							}
						}
					} catch (InterruptedException e) {
						break;
					} catch (Exception e) {
						Log.d("", "Connection broken: " + e.getClass().getName());
						try {
							Thread.sleep(5000); //sleep and then try again
						} catch (InterruptedException e1) {
							break;
						}
					}
				}
			}
		});
		publishThread.start();
	}
    public static BigInteger factorial(BigInteger n) {
        BigInteger result = BigInteger.ONE;

        while (!n.equals(BigInteger.ZERO)) {
            result = result.add(BigInteger.ONE);
            n = n.subtract(BigInteger.ONE);
        }

        return result;
    }
}
