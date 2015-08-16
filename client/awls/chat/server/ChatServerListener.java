package awls.chat.server;

import java.io.IOException;

public interface ChatServerListener {

    void receiveMessage(String message);

    void onErrorOccurred(IOException e);

    void onStopped();

}
