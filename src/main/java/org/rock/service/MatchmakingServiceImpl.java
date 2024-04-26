package org.rock.service;

import org.rock.app.GameServer;
import org.rock.domain.GameResult;
import org.rock.domain.Player;
import org.rock.domain.PlayerChoice;
import org.rock.network.Communicator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.rock.network.MessageSender.*;

public class MatchmakingServiceImpl implements MatchmakingService {

    private final GameServer gameServer;
    private final GameMatchService gameMatchService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public MatchmakingServiceImpl(GameServer gameServer, GameMatchService gameMatchService) {
        this.gameServer = gameServer;
        this.gameMatchService = gameMatchService;
    }

    @Override
    public void runMatchmakingProcess() {
        while (true) {
            Player player1 = gameServer.takePlayer();
            sendWaitingForAnOpponent(player1.communicator());

            Player player2 = gameServer.takePlayer();

            executorService.execute(() -> {
                try {
                    startMatch(player1, player2);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    gameServer.freeNickname(player1.nickname());
                    gameServer.freeNickname(player2.nickname());
                }
            });
        }
    }

    private void startMatch(Player player1, Player player2) throws Exception {
        try (Communicator communicator1 = player1.communicator();
             Communicator communicator2 = player2.communicator()) {
            GameResult gameResult = play(player1, player2);

            if (gameResult == GameResult.PLAYER1_WIN) {
                sendYouWonMessage(communicator1);
                sendYouLostMessage(communicator2);
            } else {
                sendYouWonMessage(communicator2);
                sendYouLostMessage(communicator1);
            }
        }
    }

    private GameResult play(Player player1, Player player2) throws Exception {
        Communicator communicator1 = player1.communicator();
        Communicator communicator2 = player2.communicator();

        while (true) {
            Future<PlayerChoice> future1 = executorService.submit(() -> {
                sendOpponentInfoMessage(communicator1, player2.nickname());
                return getPlayerChoice(communicator1);
            });

            Future<PlayerChoice> future2 = executorService.submit(() -> {
                sendOpponentInfoMessage(communicator2, player1.nickname());
                return getPlayerChoice(communicator2);
            });

            PlayerChoice choice1 = waitForChoice(future1, communicator1, communicator2);
            PlayerChoice choice2 = waitForChoice(future2, communicator2, communicator1);

            GameResult gameResult = gameMatchService.determineWinner(choice1, choice2);

            if (gameResult == GameResult.TIE) {
                sendTieMessage(communicator1);
                sendTieMessage(communicator2);
            } else {
                return gameResult;
            }
        }
    }

    private PlayerChoice getPlayerChoice(Communicator communicator) {
        try {
            return gameMatchService.getPlayerChoice(communicator);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to get player choice", e);
        }
    }

    private PlayerChoice waitForChoice(Future<PlayerChoice> future, Communicator main, Communicator second) throws Exception {
        try {
            return future.get(gameServer.getPlayerActionTimeoutSeconds(), TimeUnit.SECONDS);
        } catch (Exception e) {
            sendYourSessionClosed(main);
            sendTheAnOpponentLeft(second);
            main.close();
            second.close();
            throw e;
        }
    }
}
