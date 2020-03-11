import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.awt.*;
import java.util.Date;
import javax.swing.*;

public class Server extends JFrame {

  public static ResultSet rs = null;

  // Text area for displaying contents
  private JTextField jtf = new JTextField();
  private JTextArea jta = new JTextArea();
  private JButton Exit = new JButton("Exit");
  private JButton Send = new JButton("Send");

  public static void main(String[] args) {
    new Server();
  }

  public Server() {
    // Place text area on the frame
    setLayout(new BorderLayout());
    add(new JScrollPane(jta), BorderLayout.CENTER);
    add(Exit, BorderLayout.EAST);
    add(Send, BorderLayout.WEST);
    add(jtf, BorderLayout.SOUTH);

    setTitle("Server");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true); // It is necessary to show the frame here!


    Exit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        System.exit(0);
      }
    });

    try {
      //Database Connection
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","");
      Statement st=con.createStatement();

      // Create a server socket
      ServerSocket serverSocket = new ServerSocket(8000);
      jta.append("Server started at " + new Date() + '\n');

      while (true) {
        rs = st.executeQuery("select * from students");

        Socket s1=serverSocket.accept();
        myClient c = new myClient(s1);
        c.start();
      }
    }
    catch(IOException | ClassNotFoundException | SQLException ex) {
      System.err.println(ex);
    }
    
  }

  private class myClient extends Thread {
    //The socket the client is connected through
    private Socket socket;
    // The ip address of the client
    //The input and output streams to the client
    private DataInputStream inputFromClient;
     private DataOutputStream outputToClient;
    // The Constructor for the clientpublic
    myClient(Socket socket) throws IOException {
      this.socket = socket;

      inputFromClient = new DataInputStream(
              socket.getInputStream()
      );
      outputToClient = new DataOutputStream(
              socket.getOutputStream()
      );


    }/** The method that runs when the thread starts*/
    public void run() {
       String user = null;

      try {
        while (true) {

          if (user == null)
          {
            outputToClient.writeUTF("please enter your student number");

            Double message = inputFromClient.readDouble();
            if (rs.next())
            {
              if(rs.getDouble("STUD_ID") == message)
              {
                user = "yes";
                outputToClient.writeUTF("logged in");
              }
              else
              {
                outputToClient.writeUTF("student not found");
              }
            }
          }
          else
          {
            //Receive messages from the client.
            Double message = inputFromClient.readDouble();

            if (!message.isNaN()) jta.append(message + "\n");

            // TODO: replace with area of a circle.
            outputToClient.writeUTF("test");
          }
        }
      } catch (Exception e) {
        System.err.println(e + " on " + socket);
      }
    }}
}