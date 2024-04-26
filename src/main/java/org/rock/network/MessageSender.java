package org.rock.network;

import org.rock.domain.PlayerChoice;

public class MessageSender {

    private static final String ANSI_ORANGE = "\033[33m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    public static void sendActionWarningMessage(Communicator communicator, int actionTimeout) {
        sendColoredMessage(communicator, ANSI_ORANGE,
                "YOU HAVE %d SECONDS FOR EACH ACTION, AFTER WHICH THE SESSION WILL BE CLOSED.".formatted(actionTimeout)
        );
        communicator.writeln();
    }

    public static void sendWelcomeMessage(Communicator communicator) {
        communicator.writeln("Welcome to the Rock-Paper-Scissors Game!");
        communicator.write("Please enter your nickname: ");
    }

    public static void sendNicknameTakenMessage(Communicator communicator, String nickname) {
        communicator.write(String.format("The '%s' nickname already taken, please enter another nickname: ", nickname));
    }

    public static void sendNicknameInvalidMessage(Communicator communicator, String errorMessage) {
        sendColoredMessage(communicator, ANSI_ORANGE, errorMessage + " Please enter valid nickname: ");
    }

    public static void sendEnterChoiceMessage(Communicator communicator) {
        communicator.writeln("Enter your choice by typing the corresponding number.");
        for (PlayerChoice choice : PlayerChoice.values()) {
            communicator.writeln("   - %s for %s".formatted(choice.number(), choice.name()));
        }
    }

    public static void sendYourChoiceMessage(Communicator communicator) {
        communicator.write("Choose the number: ");
    }

    public static void sendAnnouncementInfoMessage(Communicator communicator) {
        communicator.writeln("After both players have made their choices, the winner will be announced.");
    }

    public static void sendLetsStartMessage(Communicator communicator) {
        communicator.writeln();
        communicator.writeln("Let's Start!");
        communicator.writeln();
    }

    public static void sendTieMessage(Communicator communicator) {
        communicator.writeln("It's a tie!");
        communicator.writeln("Starting a new match...");
    }

    public static void sendYouWonMessage(Communicator communicator) {
        communicator.writeln();
        sendColoredMessage(communicator, ANSI_GREEN, "You Won!");
    }

    public static void sendYouLostMessage(Communicator communicator) {
        communicator.writeln();
        sendColoredMessage(communicator, ANSI_RED, "You Lost!");
    }

    public static void sendOpponentInfoMessage(Communicator communicator, String opponentNickname) {
        communicator.writeln("Your opponent is: " + opponentNickname);
    }

    public static void sendWaitingForAnOpponent(Communicator communicator) {
        communicator.writeln();
        communicator.writeln("Waiting for an opponent...");
    }

    public static void sendTheAnOpponentLeft(Communicator communicator) {
        communicator.writeln();
        sendColoredMessage(communicator, ANSI_RED, "The Opponent left the game!");
    }

    public static void sendYourSessionClosed(Communicator communicator) {
        communicator.writeln();
        sendColoredMessage(communicator, ANSI_RED,
                "The time for this action has elapsed. The session will be closed!"
        );
    }

    public static void sendYourChoiceMessage(Communicator communicator, String choice) {
        communicator.writeln("Your choice: " + choice);
    }

    public static void sendUnknownChoiceMessage(Communicator communicator, String choice) {
        sendColoredMessage(communicator, ANSI_ORANGE, "Unknown choice: " + choice);
        communicator.writeln();
    }

    private static void sendColoredMessage(Communicator communicator, String color, String message) {
        communicator.write(color);
        communicator.write(message);
        communicator.write(ANSI_RESET);
    }
}
