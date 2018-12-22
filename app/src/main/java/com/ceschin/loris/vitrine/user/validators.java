package com.ceschin.loris.vitrine.user;

public class validators {

    public static boolean isUsernameValid (String username) {
        return username.length() > 4;
    }

    public static boolean isEmailValid (String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid (String password) {
        return password.length() > 4;
    }
}
