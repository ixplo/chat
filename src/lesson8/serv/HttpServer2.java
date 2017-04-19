package lesson8.serv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer2 {
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(7070);
        try {

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new SocketDispatcher(socket)).start();
            }
//            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
