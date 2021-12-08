package com.tr.gcs.entitlement

import com.tr.gcs.util.CredentialCipher
import com.tr.gcs.util.OkHttpClientWrapper
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.apache.commons.codec.binary.Base64
import org.apache.log4j.Logger

class EntitlementClientCredentialsService
{
    final static Logger LOG = Logger.getLogger(this.class)

    // These values should be encrypted in the application context file.
    static String entitlementServiceTokenUrl = 'https://entitlement-qa.gcs.int.thomsonreuters.com/v1/token'
    static String entitlementServiceUserClientId = '5ee4e859-ad62-4228-b94c-c31e94c8d131'
    static String entitlementServiceUserClientSecretDecodeKey = '3LWKPho7sLY='
    static String entitlementServiceUserClientSecretEncodedValue = 'lwnFUaLDoHJki6XvjWUwvGzf5MApZ5O0'

    static OauthAccessTokenResponseView getOauthAccessTokenResponseView()
    {
        String entitlementServiceUserClientSecret = new String(CredentialCipher.decode(entitlementServiceUserClientSecretDecodeKey,
                Base64.decodeBase64(entitlementServiceUserClientSecretEncodedValue.bytes)))

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded")
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=$entitlementServiceUserClientId&client_secret=$entitlementServiceUserClientSecret")
        Request request = new Request.Builder()
                .url(entitlementServiceTokenUrl)
                .post(body)
                .build()

        OkHttpClientWrapper okHttpClientWrapper = new OkHttpClientWrapper()
        okHttpClientWrapper.execute(request, OauthAccessTokenResponseView)
    }
}
