package awls.chat.server.socket;

import awls.chat.server.ChatServer;
import awls.chat.server.ChatServerFactory;
import awls.chat.server.ChatServerListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketChatServerFactory implements ChatServerFactory {

    private final String host;
    private final int port;

    public SocketChatServerFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public ChatServer create(ChatServerListener listener) {
        try {
            Socket socket = new Socket(host, port);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            return new StreamsChatServer(listener, inputStream, outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to server: " + e, e);
        }
    }

}
