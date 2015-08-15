package awls.chat.view.console;

import awls.chat.view.ChatView;
import awls.chat.view.ChatViewFactory;
import awls.chat.view.UserListener;

public class ConsoleChatViewFactory implements ChatViewFactory {

    @Override
    public ChatView create(UserListener listener) {
        ConsoleChatView consoleChatView = new ConsoleChatView(listener);
        consoleChatView.start();
        return consoleChatView;
    }

}
