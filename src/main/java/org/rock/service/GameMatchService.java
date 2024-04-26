package org.rock.service;

import org.rock.domain.GameResult;
import org.rock.domain.PlayerChoice;
import org.rock.network.Communicator;

public interface GameMatchService {

    PlayerChoice getPlayerChoice(Communicator communicator);

    GameResult determineWinner(PlayerChoice choice1, PlayerChoice choice2);
}
