import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket client = new Socket("localhost", 8000);
        BufferedReader serverInput = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter serverOut = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter userOut = new PrintWriter(new OutputStreamWriter(System.out));

        BlockingQueue<Object> actionQueue = new LinkedBlockingQueue<>();

        new Thread(() -> {
            try {
                String line;
                while ((line = serverInput.readLine()) != null) {
                    actionQueue.add(new Receive(line));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                String line;
                while ((line = userInput.readLine()) != null) {
                    actionQueue.add(new Send(line));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Object action;
                while ((action = actionQueue.take()) != null) {
                    if (action instanceof Send) {
                        serverOut.print(((Send) action).getMessage() + "\r\n");
                        serverOut.flush();
                    } else if (action instanceof Receive) {
                        userOut.println(((Receive) action).getMessage());
                        userOut.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

class Send {
    private final String message;

    Send(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

class Receive {
    private final String message;

    Receive(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
