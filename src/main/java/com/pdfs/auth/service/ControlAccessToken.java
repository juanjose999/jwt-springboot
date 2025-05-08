package com.pdfs.auth.service;

import java.util.ArrayList;
import java.util.List;

public class ControlAccessToken {
    public String email;
    public static List<ControlAccessToken> accessTokenList = new ArrayList<>();

    public ControlAccessToken(String email) {
        this.email = email;
    }

    public static boolean existUser(String email) {
        boolean r = accessTokenList.stream()
                .anyMatch(token -> token.email.equals(email));
        return r;
    }

    public static void addAccessToken(ControlAccessToken accessToken) {
        accessTokenList.add(accessToken);
    }

    public static void invalidateAllTokens(String email) {
        accessTokenList.removeIf(accessToken -> accessToken.email.equals(email));
    }
}
