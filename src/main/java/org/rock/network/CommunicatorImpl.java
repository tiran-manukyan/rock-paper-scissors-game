package org.rock.network;

import java.io.*;
import java.net.Socket;

public final class CommunicatorImpl implements Communicator {

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public CommunicatorImpl(Socket socket) {
        this.socket = socket;
        try {
            out = createPrintWriter(socket);
            in = createBufferedReader(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(String value) {
        out.print(value);
        out.flush();
    }

    @Override
    public void writeln(String value) {
        out.print(value);
        out.print("\r\n");
        out.flush();
    }

    @Override
    public void writeln() {
        out.print("\r\n");
        out.flush();
    }

    @Override
    public String readLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        try (in; out; socket) {
        }
    }

    private static PrintWriter createPrintWriter(Socket socket) throws IOException {
        return new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
    }

    private static BufferedReader createBufferedReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}