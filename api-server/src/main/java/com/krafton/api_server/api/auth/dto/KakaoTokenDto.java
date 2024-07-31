package com.krafton.api_server.api.auth.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class KakaoTokenDto {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String id_token;
    private int expires_in;
    private int refresh_token_expires_in;
    private String scope;
}