import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
  // Text field for receiving radius
  private JTextField jtf = new JTextField();
  private JButton Exit = new JButton("Exit");
  private JButton Send = new JButton("Send");

  // Text area to display contents
  private JTextArea jta = new JTextArea();

  // IO streams
  private DataOutputStream toServer;
  private DataInputStream fromServer;

  public static void main(String[] args) {
    new Client();
  }

  public Client() {
    // Panel p to hold the label and text field
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(new JLabel("Enter radius"), BorderLayout.WEST);
    p.add(jtf, BorderLayout.CENTER);
    jtf.setHorizontalAlignment(JTextField.RIGHT);

    setLayout(new BorderLayout());
    add(Send, BorderLayout.WEST);
    add(Exit, BorderLayout.EAST);
    add(p, BorderLayout.NORTH);
    add(new JScrollPane(jta), BorderLayout.CENTER);


    Exit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        System.exit(0);
      }
    });

    setTitle("Client");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true); // It is necessary to show the frame here!

    try {
      // Create a socket to connect to the server
      Socket socket = new Socket("localhost", 8000);
      // Socket socket = new Socket("130.254.204.36", 8000);
      // Socket socket = new Socket("drake.Armstrong.edu", 8000);

      // Create an input stream to receive data from the server
      fromServer = new DataInputStream(socket.getInputStream());

      // Create an output stream to send data to the server
      toServer = new DataOutputStream(socket.getOutputStream());

      Send.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
          System.out.println("In");
          try {
            // Get the radius from the text fielderverSocket serverSocket = new ServerSocket(8000);
            Double input = Double.parseDouble(jtf.getText());

            // Send the radius to the server
            toServer.writeDouble(input);
            toServer.flush();

            System.out.println("test");
          }
          catch (IOException ex) {
            System.err.println(ex);
          }
        }
      });


      while (true) {
        // Receive radius from the client
        String input = fromServer.readUTF();

        jta.append("radius: " + input + '\n');
      }
    }
    catch (IOException ex) {
      jta.append(ex.toString() + '\n');
    }



  }
}