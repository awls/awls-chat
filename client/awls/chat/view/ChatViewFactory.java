package awls.chat.view;

public interface ChatViewFactory {

    ChatView create(UserListener listener);

}
