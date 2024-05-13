import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame{

    private static final java.awt.Font Font = null;

    Socket socket;

    BufferedReader br;
    PrintWriter out;


    //Declare components
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    @SuppressWarnings("unused")
    private Font font=new Font("Roboto",java.awt.Font.PLAIN,20);

    // constructor
    public Client() {
        try {

            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);

            System.out.println("Connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());


            createGUI();
            handleEvents();
           
            startReading();
           // startWriting();

        } catch (Exception e) {
            // TODO: handle exception
        }

    }
         private void handleEvents() {
      

            messageInput.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                    // TODO Auto-generated method stub
                  //  throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    // TODO Auto-generated method stub
                   // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    // TODO Auto-generated method stub
                   
                  //  System.out.println("key released"+e.getKeyCode());
                    if(e.getKeyCode()==10) //enter ka key code 10 hota h
                    {
                        System.out.println("You have pressed enter button");

                        String contentToSend=messageInput.getText();
                        messageArea.append("Me : "+contentToSend+"\n");
                        out.println(contentToSend);
                        out.flush();
                        messageInput.setText("");
                        messageInput.requestFocus();
                    }
                }
                
            });
            
    }
        private void createGUI() {
       
            //gui code

        this.setTitle("Client Messager[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for component
        heading.setFont(Font);
        messageArea.setFont(Font);
        messageInput.setFont(Font);
        heading.setIcon(new ImageIcon("chat.png"));

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        messageArea.setEditable(false);

        //frame ka layout set krenge
        this.setLayout(new BorderLayout());

        //adding the component to frame
        this.add(heading,BorderLayout.NORTH);
       
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


        this.setVisible(true);
    }


    //start reading[method]
    public void startReading() {

        // thread -read krk deta rhega

        Runnable r1 = () -> {

            System.out.println("reader started....");

            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");

                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                   // System.out.println("Server : " + msg);
                   messageArea.append("Server : "+msg+"\n");
                }

            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("connection is closed");
            }

        };

        new Thread(r1).start();
    }


    //start writing[method]
    public void startWriting() {

        // thread - data user se lega then read krk clent tak
        Runnable r2 = () -> {

            System.out.println("Writer Started");

            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();

                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }

                // System.out.println("connection is closed");

            } catch (Exception e) {
                e.printStackTrace();
            }

        };

        new Thread(r2).start();

    }

    public static void main(String[] args) {

        System.out.println("This is client");

        new Client();
    }
}
