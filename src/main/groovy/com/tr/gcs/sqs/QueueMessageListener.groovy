package com.tr.gcs.sqs

import com.fasterxml.jackson.databind.ObjectMapper
import com.tr.gcs.JacksonConfig
import com.tr.gcs.acquisition.channle.broadcast.Notification
import com.tr.gcs.storage.StorageService
import org.apache.log4j.Logger

import javax.jms.JMSException
import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.TextMessage

class QueueMessageListener implements MessageListener
{
    final static Logger LOG = Logger.getLogger(this.class)

    @SuppressWarnings(['PrintStackTrace'])
    @Override
    void onMessage(Message message)
    {
        try
        {
            String textMessage = ((TextMessage) message).text
            ObjectMapper objectMapper = JacksonConfig.objectMapper()
            Notification notification = objectMapper.readValue(textMessage, Notification)
            String renditionGuid = notification.acquiredObject.renditionGuid
            LOG.info("Rendition guid: " + renditionGuid)

            StorageService.getRendition(renditionGuid)
            StorageService.downloadRenditionData(renditionGuid)
        }
        catch (JMSException e)
        {
            e.printStackTrace()
        }
    }
}
