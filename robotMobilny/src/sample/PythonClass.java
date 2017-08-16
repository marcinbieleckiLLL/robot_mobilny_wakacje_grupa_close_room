package sample;

import java.io.StringWriter;

import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by Marcin on 15.08.2017.
 */
public class PythonClass {

    public void runPythonScript(boolean isCameraOn, String ip){
        String[] arguments = {ip};

        Properties postProps = new Properties();
        Properties sysProps = System.getProperties();

        if(sysProps.getProperty("python.home") == null)
            sysProps.put("python.home", "C:\\jython2.5.0");

        Enumeration e = sysProps.propertyNames();
        while (e.hasMoreElements()){
            String name = (String)e.nextElement();
            if(name.startsWith("python")){
                postProps.put(name, System.getProperty(name));
            }
        }
        PythonInterpreter.initialize(sysProps, postProps, arguments);

        org.python.util.PythonInterpreter python = new org.python.util.PythonInterpreter(null, new PySystemState());

        StringWriter out = new StringWriter();
        python.setOut(out);
        python.execfile("pythonScript.py");
        String outputStr = out.toString();
        System.out.println(outputStr);
    }

    public static void sendDataToPythonScript(boolean isCameraOn, String ip) throws IOException {
        String fromClient;
        String toClient;

        ServerSocket server = new ServerSocket(8080);
        System.out.println("wait for connection on port 8080");

        boolean run = true;
        while(run) {
            Socket client = server.accept();
            System.out.println("got connection on port 8080");
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(),true);

            fromClient = in.readLine();
            System.out.println("received: " + fromClient);

            if(fromClient.equals("Hello")) {
                toClient = "olleH";
                System.out.println("send olleH");
                out.println(toClient);
                fromClient = in.readLine();
                System.out.println("received: " + fromClient);

                if(fromClient.equals("Bye")) {
                    toClient = "eyB";
                    System.out.println("send eyB");
                    out.println(toClient);
                    client.close();
                    server.close();
                    run = false;
                    System.out.println("socket closed");
                }
            }
        }
    }

    public static class sendData implements Runnable{

        @Override
        public void run() {
            try {
                sendDataToPythonScript(true, "1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
