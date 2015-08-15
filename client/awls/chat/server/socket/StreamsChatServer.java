package awls.chat.server.socket;

import awls.chat.server.ChatServer;
import awls.chat.server.ChatServerListener;
import awls.chat.utils.LineInput;

import java.io.*;

public class StreamsChatServer implements ChatServer {

    private final ChatServerListener listener;
    private final LineInput in;
    private final PrintWriter out;

    public StreamsChatServer(ChatServerListener listener, InputStream inputStream, OutputStream outputStream) {
        this.listener = listener;
        this.in = new LineInput(inputStream) {
            @Override
            protected void handleLine(String line) {
                StreamsChatServer.this.listener.receiveMessage(line);
            }

            @Override
            protected void handleError(IOException e) {
                e.printStackTrace();
            }
        };
        this.out = new PrintWriter(new OutputStreamWriter(outputStream));
    }

    public void start() {
        in.start();
    }

    public void stop() {
        in.stop();
    }

    @Override
    public void sendMessage(String message) {
        out.print(message);
        out.print("\r\n");
        out.flush();
    }

}
