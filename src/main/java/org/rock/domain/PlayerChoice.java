package org.rock.domain;

public enum PlayerChoice {
    ROCK("1"),
    PAPER("2"),
    SCISSORS("3");

    private final String number;

    PlayerChoice(String number) {
        this.number = number;
    }

    public String number() {
        return number;
    }

    public static PlayerChoice getByNumber(String number) {
        for (PlayerChoice choice : PlayerChoice.values()) {
            if (choice.number.equals(number)) {
                return choice;
            }
        }
        return null;
    }
}
