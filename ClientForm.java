package Flawless_Feedback;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/****************************************************************
 PROGRAM:   Survey Manager/ClientForm
 AUTHOR:    B Fahy
 DUE DATE:  20/10/2022

 FUNCTION:  The purpose of this program is to receive a survey from main form and send a response back (1-5)

 INPUT:     Received via server, sent by MainForm hub

 OUTPUT:    Sent via server to main hub in form of response to survey (1-5)

 ****************************************************************/
public class ClientForm extends JFrame implements ActionListener {
    SpringLayout myLayout = new SpringLayout();
    JLabel lblHeader, lblText, lblTopic, lblQn, lblOne, lblTwo,
            lblThree, lblFour, lblFive, lblAnswer;
    JTextField txtTopic,  txtOne, txtTwo, txtThree, txtFour, txtFive, txtAnswer, txtMessage;
    JTextArea txtQn;
    JButton btnSubmit, btnExit, btnConnect;

    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread2 client2 = null;
    private String serverName = "localhost";
    private int serverPort = 4444;

    int count = 0;
    String[] tempQn = new String[7];

    /**
     * Client form setup, used to receive surveys from main form
     */
    public ClientForm()
    {
        setSize(270, 355);
        setLocation(200, 150);
        setLayout(myLayout);

        LabelBuilder();
        TextFieldBuilder();
        ButtonBuilder();

        getParameters();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();
            }
        });

        setResizable(false);
        setVisible(true);
    }

    /**
     * Label setup for form
     */
    private void LabelBuilder()
    {
        lblHeader = ComponentBuilder.CreateALabel("Survey Questions", 55, 5, myLayout, this);
        lblHeader.setFont(new Font("Times New Roman", Font.BOLD,20));

        lblText = ComponentBuilder.CreateALabel("Enter your answer and click 'Submit'", 15, 35, myLayout, this);
        lblText.setFont(new Font("Times New Roman", Font.ITALIC,13));

        lblTopic = ComponentBuilder.CreateALabel("Topic:", 20, 60, myLayout, this);
        lblQn = ComponentBuilder.CreateALabel("Question:", 20, 80, myLayout, this);
        lblOne = ComponentBuilder.CreateALabel("1:", 20, 130, myLayout, this);
        lblTwo = ComponentBuilder.CreateALabel("2:", 20, 150, myLayout, this);
        lblThree = ComponentBuilder.CreateALabel("3:", 20, 170, myLayout, this);
        lblFour = ComponentBuilder.CreateALabel("4:", 20, 190, myLayout, this);
        lblFive = ComponentBuilder.CreateALabel("5:", 20, 210, myLayout, this);

        lblText = ComponentBuilder.CreateALabel("Your Answer:", 15, 236, myLayout, this);
    }

    /**
     * Text field setup for form
     */
    private void TextFieldBuilder()
    {
        txtTopic = ComponentBuilder.CreateATextField(15, 80, 60, myLayout, this);
        txtTopic.setEditable(false);
        txtTopic.setBackground(Color.WHITE);
        txtQn = ComponentBuilder.CreateATextArea(15,3, 80, 80, myLayout, this);
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        txtQn.setBorder(border);
        txtQn.setLineWrap(true);
        txtQn.setEditable(false);
        txtQn.setWrapStyleWord(true);
        txtOne = ComponentBuilder.CreateATextField(15, 80, 130, myLayout, this);
        txtOne.setEditable(false);
        txtOne.setBackground(Color.WHITE);
        txtTwo = ComponentBuilder.CreateATextField(15, 80, 150, myLayout, this);
        txtTwo.setEditable(false);
        txtTwo.setBackground(Color.WHITE);
        txtThree = ComponentBuilder.CreateATextField(15, 80, 170, myLayout, this);
        txtThree.setEditable(false);
        txtThree.setBackground(Color.WHITE);
        txtFour = ComponentBuilder.CreateATextField(15, 80, 190, myLayout, this);
        txtFour.setEditable(false);
        txtFour.setBackground(Color.WHITE);
        txtFive = ComponentBuilder.CreateATextField(15, 80, 210, myLayout, this);
        txtFive.setEditable(false);
        txtFive.setBackground(Color.WHITE);

        txtAnswer = ComponentBuilder.CreateATextField(5, 93, 236, myLayout, this);


        txtMessage = ComponentBuilder.CreateATextField(23, 10, 292, myLayout, this);
        txtMessage.setBackground(Color.lightGray);
        txtMessage.setEditable(false);
    }

    /**
     * Button setup for form
     */
    private void ButtonBuilder()
    {
        btnSubmit = ComponentBuilder.CreateAButton("Submit", 75, 20, 5, 265, this, myLayout, this);
        btnExit = ComponentBuilder.CreateAButton("Exit", 75, 20, 175, 265, this, myLayout, this);
        btnConnect = ComponentBuilder.CreateAButton("Connect", 85, 20, 85, 265, this, myLayout, this);
    }

    /**
     * Helper method for clearing fields once response to survey is sent
     */
    public void ClearFields()
    {
        txtQn.setText("");
//        lblQnNumber.setText("");
        txtTopic.setText("");
        txtOne.setText("");
        txtTwo.setText("");
        txtThree.setText("");
        txtFour.setText("");
        txtFive.setText("");
    }

    // Server methods

    /**
     * Connection method for establishing connection to server
     * @param serverName Server name to connect to (localhost)
     * @param serverPort Server port to use
     */
    public void connect(String serverName, int serverPort)
    {
        println("Establishing connection. Please wait ...");
        try
        {
            socket = new Socket(serverName, serverPort);
            println("Connected: " + socket);
            open();
        }
        catch (UnknownHostException uhe)
        {
            println("Host unknown: " + uhe.getMessage());
        }
        catch (IOException ioe)
        {
            println("Unexpected exception: " + ioe.getMessage());
        }
    }

    /**
     * Used to send response back to main form (1-5 limited)
     */
    private void send()
    {
        try
        {
            if (txtAnswer.getText().equals("1") || txtAnswer.getText().equals("2") || txtAnswer.getText().equals("3") ||
                    txtAnswer.getText().equals("4") || txtAnswer.getText().equals("5"))
            {
                streamOut.writeUTF(txtAnswer.getText());
                streamOut.flush();
                ClearFields();
            } else {
                txtMessage.setText("** Unable to send if answer is not valid (1-5) **");
            }

        }
        catch (IOException ioe)
        {
            println("Sending error: " + ioe.getMessage());
            close();
        }
    }

    /**
     * Method for receiving survey from main form and assigning to correct fields for display
     * @param msg Messages received
     */
    public void handle(String msg)
    {

        if (msg.equals(".bye"))
        {
            println("Good bye. Press EXIT button to exit ...");
            close();
        }
        else
        {
            if (msg.equals("1") || msg.equals("2") || msg.equals("3") || msg.equals("4") || msg.equals("5"))
            {
                System.out.println("Response: " + msg);
                println(msg);
            } else {
                System.out.println("Received: " + msg);
                println(msg);
                tempQn[count] = msg;
                count++;
                System.out.println(count);
                if (count == 7) {
                    txtTopic.setText(tempQn[0]);
                    txtQn.setText(tempQn[1]);
                    txtOne.setText(tempQn[2]);
                    txtTwo.setText(tempQn[3]);
                    txtThree.setText(tempQn[4]);
                    txtFour.setText(tempQn[5]);
                    txtFive.setText(tempQn[6]);
                    count = 0;
                }
            }
        }
    }

    /**
     * Method for opening streams/threads
     */
    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client2 = new ChatClientThread2(this, socket);
        }
        catch (IOException ioe)
        {
            println("Error opening output stream: " + ioe);
        }
    }

    /**
     * Method for closing streams/threads
     */
    public void close()
    {
        try
        {
            if (streamOut != null)
            {
                streamOut.close();
            }
            if (socket != null)
            {
                socket.close();
            }
        }
        catch (IOException ioe)
        {
            println("Error closing ...");
        }
        client2.close();
        client2.stop();
    }

    /**
     * Prints to bottom of form text area
     * @param msg
     */
    void println(String msg)
    {
        //display.appendText(msg + "\n");
        txtMessage.setText(msg);
    }

    /**
     * Helper method for getting server variables
     */
    public void getParameters()
    {
//        serverName = getParameter("host");
//        serverPort = Integer.parseInt(getParameter("port"));

        serverName = "localhost";
        serverPort = 4444;
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnExit)
        {
            dispose();
        }

        if (e.getSource() == btnConnect)
        {
            connect(serverName, serverPort);
        }

        if(e.getSource() == btnSubmit)
        {
            send();


            txtAnswer.setText("");
        }
    }
}
