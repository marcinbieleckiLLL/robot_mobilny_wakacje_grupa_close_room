package sample;

import Canny.RobotOrders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static sample.Controller.ip;

/**
 * Created by Marcin on 15.08.2017.
 */
public class Connection {

    public static ListOfAtributtesOfConnection listOfAtributtesOfConnection = new ListOfAtributtesOfConnection();

    public static void sendMessage(String Message, PrintWriter out){
        out.println(Message);
    }

    public static String receivMessage(BufferedReader in) throws IOException {
        return in.readLine();
    }

    public static void createClient(Controller.Properties properties) throws IOException {
        Socket socketClient = new Socket(ip, properties.port);
        BufferedReader in = doConnectionMalformation(socketClient, properties);
        while(true) {
            System.out.println(receivMessage(in));
        }
    }

    public static void createServer(Controller.Properties properties) throws IOException {
        System.out.print("1111");
        ServerSocket server = new ServerSocket(properties.port);
        Socket socket = server.accept();
        BufferedReader in = doConnectionMalformation(socket, properties);
        while(true) {
            String order = receivMessage(in);
            System.out.println(order);
            RobotOrders.sendOrder(order);
        }
    }

    public static class runClient implements Runnable{
        Controller.Properties properties;

        public runClient(Controller.Properties properties) {
            this.properties = properties;
        }
        @Override
        public void run() {
            try {
                createClient(properties);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class runServer implements Runnable{
        Controller.Properties properties;

        public runServer(Controller.Properties properties) {
            this.properties = properties;
        }

        @Override
        public void run() {
            try {
                createServer(properties);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static BufferedReader doConnectionMalformation(Socket socket, Controller.Properties properties) throws IOException {
        System.out.println(properties.name + " got connection on port " + String.valueOf(properties.port));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        listOfAtributtesOfConnection.add(new AtributtesOfConnection(socket, in, out, properties.name));

        return in;
    }
}
