package com.woowacourse.f12.support.fixture;

public enum GitHubProfileFixture {

    CORINNE_GITHUB("corinne-code", "corinne-token"),
    MINCHO_GITHUB("mincho-code", "mincho-token"),
    OHZZI_GITHUB("ohzzi-code", "ohzzi-token"),
    KLAY_GITHUB("klay-code", "klay-token"),
    ;

    private final String code;
    private final String token;

    GitHubProfileFixture(final String code, final String token) {
        this.code = code;
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public String getToken() {
        return token;
    }
}
