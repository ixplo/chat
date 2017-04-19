package lesson8.chat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ChatHandler extends Thread{

    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private boolean isOn;

    private static List<ChatHandler> handlers = Collections.synchronizedList(new ArrayList<ChatHandler>());

    ChatHandler(Socket s) throws IOException{
        socket = s;
        inStream = new DataInputStream(new BufferedInputStream(s.getInputStream()));
        outStream = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
    }

    @Override
    public void run() {
        isOn = true;
        try {
            handlers.add(this);
            while (isOn) {
                String msg = inStream.readUTF();
                if(msg.substring(0,1).equals("/")) doCommand(msg);
                else broadcast("[" + this.getName() +"] " + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            broadcast("*** " + this.getName() + " left the chat");
            handlers.remove(this);
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void doCommand(String line) {
        if(line.contains("/nick")) {
            String newNick = line.substring(line.indexOf("nick")+5);
            broadcast("*** " + this.getName() + " changes nick to " + newNick);
            this.setName(newNick);
        }
        if(line.contains("/newnick")) {
            String name = line.substring(line.indexOf("newnick") + 8);
            this.setName(name);
            broadcast("*** " + this.getName() + " joined the chat");
        }
    }

    private static void broadcast(String massage) {
        synchronized (handlers){
            Iterator<ChatHandler> it = handlers.iterator();
            while (it.hasNext()){
                ChatHandler c = it.next();
                try {
                    synchronized (c.outStream) {
                        c.outStream.writeUTF(massage);
                    }
                    c.outStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    c.isOn = false;
                }
            }
        }
    }
}
