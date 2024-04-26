package org.rock.app;

import org.rock.domain.Player;
import org.rock.domain.PlayerNicknameValidator;
import org.rock.network.Communicator;
import org.rock.network.CommunicatorImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.*;

import static org.rock.network.MessageSender.*;

public class GameServer {

    private final Configs configs;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final Set<String> playerNicknames = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final BlockingQueue<Player> players = new LinkedBlockingDeque<>();

    public GameServer() {
        configs = loadConfigs();
    }

    public void startServer() throws IOException {
        int port = configs.serverPort();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.printf("Game Server is running on port %d...%n", port);

        new Thread(() -> {
            while (true) {
                Socket socket = acceptSocket(serverSocket);
                Communicator communicator = new CommunicatorImpl(socket);
                Future<Player> player = executorService.submit(() -> acceptPlayer(communicator));

                try {
                    players.add(player.get(configs.playerActionTimeoutSeconds(), TimeUnit.SECONDS));
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        sendYourSessionClosed(communicator);
                        communicator.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public Player takePlayer() {
        try {
            return players.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void freeNickname(String nickname) {
        playerNicknames.remove(nickname);
    }

    public int getPlayerActionTimeoutSeconds() {
        return configs.playerActionTimeoutSeconds();
    }

    private Socket acceptSocket(ServerSocket serverSocket) {
        try {
            return serverSocket.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Player acceptPlayer(Communicator communicator) {
        sendActionWarningMessage(communicator, getPlayerActionTimeoutSeconds());
        sendWelcomeMessage(communicator);

        String playerNickname = getNickname(communicator);
        playerNicknames.add(playerNickname);

        return new Player(playerNickname, communicator);
    }

    private String getNickname(Communicator communicator) {
        while (true) {
            String playerNickname = communicator.readLine();

            String errorMessage = PlayerNicknameValidator.validateNickname(playerNickname);
            if (errorMessage != null) {
                sendNicknameInvalidMessage(communicator, errorMessage);
                continue;
            }

            if (!playerNicknames.contains(playerNickname)) {
                return playerNickname;
            }

            sendNicknameTakenMessage(communicator, playerNickname);
        }
    }

    private Configs loadConfigs() {
        try {
            return Configs.loadFromFile();
        } catch (Exception e) {
            System.err.println("An error occurred while loading configs from file: " + e.getMessage());
            return new Configs(7815, 30);
        }
    }
}
