package awls.chat.view.console;

import awls.chat.utils.LineInput;
import awls.chat.view.ChatView;
import awls.chat.view.UserListener;

import java.io.IOException;

public class ConsoleChatView implements ChatView {

    private final UserListener listener;
    private final LineInput in;

    public ConsoleChatView(UserListener listener) {
        this.listener = listener;
        this.in = new LineInput(System.in, true) {
            @Override
            protected void handleLine(String line) {
                ConsoleChatView.this.listener.onUserInput(line);
            }

            @Override
            protected void handleError(IOException e) {
                e.printStackTrace();
            }

            @Override
            protected void handleStopped() {

            }
        };
    }

    @Override
    public void start() {
        in.start();
    }

    @Override
    public void stop() {
        in.stop();
    }

    @Override
    public void display(String message) {
        System.out.println(message);
    }

}
