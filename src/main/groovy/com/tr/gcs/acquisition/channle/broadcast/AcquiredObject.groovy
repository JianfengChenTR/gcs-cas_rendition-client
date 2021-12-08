/*
 * AcquiredObject.groovy
 *
 * Copyright 2019: Thomson Reuters Global Resources. All Rights Reserved.
 *
 * Proprietary and Confidential information of TRGR.
 * Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
 *
 */
package com.tr.gcs.acquisition.channle.broadcast

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode()
@ToString(includeNames = true, includePackage = false)
class AcquiredObject
{
    Long acquiredObjectId
    String acquiredObjectUuid
    String renditionGuid
    String gcsBucket
    String objectKey
    String metadataKey
    String type
    Long resourceGroupId
    Long acquisitionGroupId
}
