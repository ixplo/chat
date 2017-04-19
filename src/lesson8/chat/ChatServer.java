package lesson8.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private ChatServer(int port) throws IOException {
        ServerSocket service = new ServerSocket(port);
        try {
            while (true) {
                Socket s = service.accept();
                System.out.println("Accepted from " + s.getInetAddress());
                ChatHandler handler = new ChatHandler(s);
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            service.close();
        }
    }

    public static void main(String[] args) throws IOException {
        if(args.length != 1) throw new RuntimeException("Syntax: ChatServer <port>");
        new ChatServer(Integer.parseInt(args[0]));
    }
}
