package org.rock.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerNicknameValidator {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 15;
    private static final Pattern pattern = Pattern.compile("[a-zA-Z0-9_]+");

    public static String validateNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            return "Nickname cannot be empty.";
        }

        if (nickname.length() < MIN_LENGTH) {
            return "Nickname is too short.";
        }

        if (nickname.length() > MAX_LENGTH) {
            return "Nickname is too long.";
        }

        Matcher matcher = pattern.matcher(nickname);
        if (!matcher.matches()) {
            return "Nickname contains invalid characters.";
        }

        return null;
    }
}
