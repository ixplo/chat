package lesson8.chat;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ChatClient extends JFrame implements Runnable{
    private String name;
    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private JTextPane outTextPane;
    private JTextField inTextField;
    private boolean isOn;

    private ChatClient(String title, String name, Socket s, DataInputStream dis, DataOutputStream dos)  {
        super(title);
        this.name = name;
        socket = s;
        inStream = dis;
        outStream = dos;

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        outTextPane = new JTextPane();
        outTextPane.setEditable(false);
//        outTextPane.setParagraphAttributes();
        cp.add(BorderLayout.CENTER, new JScrollPane(outTextPane));
        cp.add(BorderLayout.SOUTH, inTextField = new JTextField());

        inTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String msg = inTextField.getText();
                    if(msg.substring(0,1).equals("/")) doCommand(msg);
                    outStream.writeUTF(msg);
                    outStream.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    isOn = false;
                }
                inTextField.setText("");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                isOn = false;
                try {
                    outStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400,500);
        setVisible(true);
        inTextField.requestFocus();
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        isOn = true;
        joinName(name);
        try {
            while (true) {
                String line = inStream.readUTF();
                AttributeSet mySet;
                mySet = StyleContext.getDefaultStyleContext().addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
                if(line.substring(0,3).equals("***")) mySet = StyleContext.getDefaultStyleContext().addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLUE);
                outTextPane.getDocument().insertString(outTextPane.getDocument().getLength(), line + "\n",mySet);
//                mySet = StyleContext.getDefaultStyleContext().addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLUE);
            }
        }catch (BadLocationException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            inTextField.setVisible(false);
            validate();
        }
    }

    private void joinName(String name) {
        try {
            outStream.writeUTF("/newnick " + name);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doCommand(String line) {
        if(line.contains("/nick")) {
            name = line.substring(line.indexOf("nick") + 5);
            this.setTitle("Name: " + name);
        }
        if(line.contains("/clear")) {
            outTextPane.setText("");
        }
    }

    public static void main(String[] args) throws IOException{
        if(args.length != 2){
            throw new RuntimeException("Syntax: ChatClient <host> <port>");
        }
        Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
        DataInputStream dis = null;
        DataOutputStream dos = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            String name = "User" + (new Random()).nextInt(100);
            new ChatClient("Name: " + name, name, socket, dis, dos);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if(dos != null) dos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                if(dis != null) dis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
