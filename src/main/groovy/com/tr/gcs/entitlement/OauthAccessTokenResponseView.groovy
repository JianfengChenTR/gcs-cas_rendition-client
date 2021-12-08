package com.tr.gcs.entitlement

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includeNames = true)
class OauthAccessTokenResponseView
{
    @JsonView([Views.V1])
    @JsonProperty('access_token')
    String accessToken

    @JsonView([Views.V1])
    @JsonProperty('token_type')
    String tokenType

    @JsonView([Views.V1])
    @JsonProperty('expires_in')
    long expiresInSeconds

    @SuppressWarnings(['EmptyClass', 'SpaceBeforeClosingBrace'])
    class Views
    {
        class V1
        {}
    }
}
