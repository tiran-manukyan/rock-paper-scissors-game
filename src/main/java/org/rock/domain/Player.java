package org.rock.domain;

import org.rock.network.Communicator;

public record Player(String nickname, Communicator communicator) {
}
