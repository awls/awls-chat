package awls.chat.application;

import awls.chat.server.ChatServer;
import awls.chat.server.ChatServerFactory;
import awls.chat.server.ChatServerListener;
import awls.chat.view.ChatView;
import awls.chat.view.ChatViewFactory;
import awls.chat.view.UserListener;

import java.io.IOException;

public class ChatClient implements ChatServerListener, UserListener {

    private final ChatServer server;
    private final ChatView view;

    public static void serve(ChatServerFactory serverFactory, ChatViewFactory viewFactory) {
        new ChatClient(serverFactory, viewFactory).start();
    }

    private ChatClient(ChatServerFactory serverFactory, ChatViewFactory viewFactory) {
        server = serverFactory.create(this);
        view = viewFactory.create(this);
    }

    private void start() {
        server.start();
        view.start();
    }

    @Override
    public void receiveMessage(String message) {
        view.display(message);
    }

    @Override
    public void onUserInput(String line) {
        server.sendMessage(line);
    }

    @Override
    public void onErrorOccurred(IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onStopped() {
        server.stop();
        view.display("Disconnected from server...");
        view.stop();
    }
}
