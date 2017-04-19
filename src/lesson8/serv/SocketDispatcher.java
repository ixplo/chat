package lesson8.serv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketDispatcher implements Runnable{
    private Socket socket;

    public SocketDispatcher(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            streamer(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void streamer(Socket socket) throws IOException {
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
    }
}
