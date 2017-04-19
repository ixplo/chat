package lesson8.serv;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public static void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(7070);
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            String html = "<html>" +
                    "<head><title>Chat server</title><head>" +
                    "<body><h1>Привет, нубас</h1>" +
                    "<br/>" +
                    "<img src='http://m.top55.info/fileadmin/images/top55_news/bk_info_orig_41267_1447994871.jpg'>" +
                    "</body>" +
                    "</html>";
            String header = "HTTP/1.1 200 OK\n" +
                    "Content-Type: text/html; charset=utf-8\n" +
                    "Content-Length: " + html.length() + "\n" +
                    "Connection: close\n\n";
            String result = header + html;
//            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));
//            out.write(result);
            outputStream.write(result.getBytes());
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
