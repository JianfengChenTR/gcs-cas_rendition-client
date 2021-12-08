package com.tr.gcs.acquisition.channle.broadcast

import java.time.Instant

class Notification
{
    Long broadcastId
    Long broadcastItemId
    String correlationId
    AcquisitionChannel acquisitionChannel
    AcquiredObject acquiredObject
    List<AcquiredObjectListItem> acquiredObjects
    String acquiredObjectsUrl
    String broadcastReasonCode
    String broadcastComments
    Instant broadcastStartDateTime
}
