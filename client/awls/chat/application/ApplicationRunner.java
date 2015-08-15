package awls.chat.application;

import awls.chat.server.socket.SocketChatServerFactory;
import awls.chat.view.console.ConsoleChatViewFactory;

public class ApplicationRunner {

    public static void main(String[] args) {
        ChatClient.serve(new SocketChatServerFactory("localhost", 8000), new ConsoleChatViewFactory());
    }

}
