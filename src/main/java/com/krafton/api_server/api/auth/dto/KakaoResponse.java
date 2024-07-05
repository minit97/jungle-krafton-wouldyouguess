package com.krafton.api_server.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResponse {

    public Long id;
    public String username;
    public String email;

    public String tokenType;
    public String accessToken;
    public String idToken;
    public Integer expiresIn;
    public String refreshToken;
    public Integer refreshTokenExpiresIn;
    public String scope;
}