package org.rock.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface MatchmakingService {

    void runMatchmakingProcess() throws IOException, ExecutionException, InterruptedException;
}
