package com.krafton.api_server.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfo {

    @JsonProperty("id")
    public Long id;

    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {

        @JsonProperty("profile")
        public Profile profile;

        @JsonProperty("name")
        public String name;

        @JsonProperty("email")
        public String email;

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Profile {

            @JsonProperty("nickname")
            public String nickName;

            @JsonProperty("thumbnail_image_url")
            public String thumbnailImageUrl;

            @JsonProperty("profile_image_url")
            public String profileImageUrl;

        }
    }

}