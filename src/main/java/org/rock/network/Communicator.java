package org.rock.network;

public interface Communicator extends AutoCloseable {

    void write(String value);

    void writeln(String value);

    void writeln();

    String readLine();
}
