package awls.chat.application;

import awls.chat.server.ChatServer;
import awls.chat.server.ChatServerFactory;
import awls.chat.server.ChatServerListener;
import awls.chat.view.ChatView;
import awls.chat.view.ChatViewFactory;
import awls.chat.view.UserListener;

public class ChatClient implements ChatServerListener, UserListener {

    private final ChatServer server;
    private final ChatView view;

    public static void serve(ChatServerFactory serverFactory, ChatViewFactory viewFactory) {
        new ChatClient(serverFactory, viewFactory);
    }

    private ChatClient(ChatServerFactory serverFactory, ChatViewFactory viewFactory) {
        view = viewFactory.create(this);
        server = serverFactory.create(this);
    }

    @Override
    public void receiveMessage(String message) {
        view.display(message);
    }

    @Override
    public void onUserInput(String line) {
        server.sendMessage(line);
    }

}
