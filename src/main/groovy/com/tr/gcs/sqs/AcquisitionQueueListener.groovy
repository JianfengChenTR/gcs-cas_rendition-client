package com.tr.gcs.sqs

import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnection
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.services.sqs.AmazonSQSClientBuilder

import javax.jms.MessageConsumer
import javax.jms.Queue
import javax.jms.Session

class AcquisitionQueueListener
{
    static final String QUEUE_NAME = "a205813-acquisitionchannel-broadcast_demo-qa"

    static void listen()
    {
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.defaultClient()
        )
        SQSConnection connection = connectionFactory.createConnection()

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        Queue queue = session.createQueue(QUEUE_NAME)
        MessageConsumer consumer = session.createConsumer(queue)

        consumer.messageListener = new QueueMessageListener()
        connection.start()
        Thread.sleep(1000)

        // synchronously
//        connection.start()
//        Message receivedMessage = consumer.receive(1000)
//        if (receivedMessage != null)
//        {
//            println("Received: " + ((TextMessage) receivedMessage).getText())
//        }
//        connection.close()
    }
}
