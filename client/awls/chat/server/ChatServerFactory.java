package awls.chat.server;

public interface ChatServerFactory {

    ChatServer create(ChatServerListener listener);

}
