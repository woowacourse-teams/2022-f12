package com.woowacourse.f12.support;

public enum GitHubProfileFixtures {

    CORINNE_GITHUB("corinne-code", "corinne-token"),
    MINCHO_GITHUB("mincho-code", "mincho-token"),
    OHZZI_GITHUB("ohzzi-code", "ohzzi-token");

    private final String code;
    private final String token;

    GitHubProfileFixtures(final String code, final String token) {
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
