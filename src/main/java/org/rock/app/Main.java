package org.rock.app;

import org.rock.service.GameMatchServiceImpl;
import org.rock.service.MatchmakingService;
import org.rock.service.MatchmakingServiceImpl;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        GameServer gameServer = new GameServer();
        gameServer.startServer();

        MatchmakingService matchmakingService = new MatchmakingServiceImpl(gameServer, new GameMatchServiceImpl());

        matchmakingService.runMatchmakingProcess();
    }
}
