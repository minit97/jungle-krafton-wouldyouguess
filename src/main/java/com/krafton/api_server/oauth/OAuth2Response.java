package com.krafton.api_server.oauth;

public interface OAuth2Response {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
