package com.tr.gcs

import com.tr.gcs.sqs.AcquisitionQueueListener
import org.apache.log4j.Logger

class RenditionClient
{
    final static Logger LOG = Logger.getLogger(this.class)

    static void main(String[] args)
    {
        LOG.info("Start main.. $args")
        AcquisitionQueueListener.listen()
    }
}
