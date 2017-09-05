package sample;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Marcin on 22.08.2017.
 */
public class AtributtesOfConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;

    public AtributtesOfConnection(Socket socket, BufferedReader in, PrintWriter out, String name) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public String getName() {
        return name;
    }
}
