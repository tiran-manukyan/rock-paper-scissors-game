package org.rock.service;

import org.rock.domain.GameResult;
import org.rock.domain.PlayerChoice;
import org.rock.network.Communicator;

import static org.rock.network.MessageSender.*;

public class GameMatchServiceImpl implements GameMatchService {

    @Override
    public PlayerChoice getPlayerChoice(Communicator communicator) {
        sendLetsStartMessage(communicator);
        sendAnnouncementInfoMessage(communicator);

        return validateAndGetChoice(communicator);
    }

    @Override
    public GameResult determineWinner(PlayerChoice choice1, PlayerChoice choice2) {
        if (choice1.equals(choice2)) {
            return GameResult.TIE;
        } else if ((choice1 == PlayerChoice.ROCK && choice2 == PlayerChoice.SCISSORS) ||
                (choice1 == PlayerChoice.PAPER && choice2 == PlayerChoice.ROCK) ||
                (choice1 == PlayerChoice.SCISSORS && choice2 == PlayerChoice.PAPER)
        ) {
            return GameResult.PLAYER1_WIN;
        } else {
            return GameResult.PLAYER2_WIN;
        }
    }

    private PlayerChoice validateAndGetChoice(Communicator communicator) {
        while (true) {
            sendEnterChoiceMessage(communicator);
            sendYourChoiceMessage(communicator);

            String choice = communicator.readLine();
            PlayerChoice playerChoice = PlayerChoice.getByNumber(choice);
            if (playerChoice != null) {
                sendYourChoiceMessage(communicator, playerChoice.name());

                return playerChoice;
            }

            sendUnknownChoiceMessage(communicator, choice);
        }
    }
}
