package awls.chat.server;

import awls.chat.common.Component;

public interface ChatServer extends Component {

    void sendMessage(String message);

}
