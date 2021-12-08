package com.tr.gcs.storage

import com.tr.gcs.entitlement.EntitlementClientCredentialsService
import com.tr.gcs.entitlement.OauthAccessTokenResponseView
import com.tr.gcs.util.OkHttpClientWrapper
import okhttp3.Request

@SuppressWarnings("DuplicateStringLiteral")
class StorageService
{
    static String contentResourceBaseUrl = 'https://contentresource-qa.gcs.int.thomsonreuters.com'
    static String outputDir = 'C:/dev/Acquisition/wip'

    static void getRendition(String renditionGuid)
    {
        OauthAccessTokenResponseView tokenView = EntitlementClientCredentialsService.oauthAccessTokenResponseView
        String url = getRenditionEndpoint(renditionGuid)
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "$tokenView.tokenType $tokenView.accessToken")
                .build()

        OkHttpClientWrapper okHttpClientWrapper = new OkHttpClientWrapper()
        okHttpClientWrapper.executeAndSaveResponseBody(request, outputDir + '/rendition.json')
    }

    static String getRenditionEndpoint(String renditionGuid)
    {
        "${contentResourceBaseUrl}/v1/renditions/${renditionGuid}"
    }

    static void downloadRenditionData(String renditionGuid)
    {
        OauthAccessTokenResponseView tokenView = EntitlementClientCredentialsService.oauthAccessTokenResponseView
        String url = getRenditionDataEndpoint(renditionGuid)
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "$tokenView.tokenType $tokenView.accessToken")
                .build()
        OkHttpClientWrapper okHttpClientWrapper = new OkHttpClientWrapper()
        okHttpClientWrapper.executeAndSaveData(request, outputDir)
    }

    static String getRenditionDataEndpoint(String renditionGuid)
    {
        "${contentResourceBaseUrl}/v3/renditions/${renditionGuid}/data"
    }
}
