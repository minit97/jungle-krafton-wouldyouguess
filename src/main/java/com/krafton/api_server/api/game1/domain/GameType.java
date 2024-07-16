package com.krafton.api_server.api.game1.domain;

public enum GameType {
    MODE1("캐치 라이어"),
    MODE2("AI 틀린그림찾기"),
    MODE3("얼굴 부위가 누군인가?");

    private String gameName;

    GameType(String gameName) {
        this.gameName = gameName;
    }
}
