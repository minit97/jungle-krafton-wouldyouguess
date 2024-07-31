package com.krafton.api_server.api.auth.dto;

import lombok.Data;

@Data
public class TempLogin {
    private String username;
    private String nickname;
}
