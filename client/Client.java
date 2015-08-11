import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws Exception {
	Socket client = new Socket("localhost", 8000);
	PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
	out.println("God save the Awls!");
	out.println("How are you, server?");
	out.flush();
	out.close();
    }
}
