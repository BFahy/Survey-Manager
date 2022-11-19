package Flawless_Feedback;

import org.apache.commons.lang3.StringUtils;
import sun.misc.Signal;
import sun.misc.SignalHandler;
// Apache string utils <-- import for strings
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.lang3.text.WordUtils;

import static java.lang.Integer.parseInt;

/****************************************************************
 PROGRAM:   Survey Manager/MainForm
 AUTHOR:    Brandon Fahy
 LOGON ID:  473203308
 DUE DATE:  20/10/2022

 FUNCTION:  The purpose of this program is to provide a survey to each client

 INPUT:     Sample surveys: SurveyByNetwork_SampleData.txt

 OUTPUT:    HashMap saving: Hashmap-Save.txt
 ****************************************************************/
public class MainForm extends JFrame implements ActionListener, MouseListener {

    //region Global variable declarations
    // Layout variable declaration
    SpringLayout myLayout = new SpringLayout();
    JButton btnSortQn, btnSortTopic, btnSortQuestion, btnExit, btnConnect, btnSend,
        btnPreOrder, btnInOrder, btnPostOrder, btnDisplay, btnSave;
    JLabel lblHeader, lblSurveyQuestions, lblSortBy, lblSearch, lblTopic, lblQn, lblQnNumber, lblOne, lblTwo,
            lblThree, lblFour, lblFive, lblLinkedList, lblBinaryTree, lblPreOrder, lblInOrder,
            lblPostOrder;
    JTextField txtSearch, txtTopic,  txtOne, txtTwo, txtThree, txtFour, txtFive, txtMessage;
    JTextArea txtQn, txtLinkedList, txtBinaryTree;

    int bTreeOrder = 0;

    // JTable variables
    JTable qnTable;
    MyModel qnModel;

    // JTable List of Object Array type
    ArrayList<Object[]> qnValues;

    // MergeSort variables
    ArrayList<Object[]> list;
    Object[] helper0, helper1, helper2, helper3, helper4, helper5, helper6, helper7;

    // Setting sorter for JTable search method
    TableRowSorter sorter;

    // Doubly linked list
    DLList dlList = new DLList();

    // Binary tree
    BTree binTree = new BTree();

    // Server
    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread1 client = null;
    private String serverName = "localhost";
    private int serverPort = 4444;

    // Variable defined for creating client forms
    int clientCount = 2;

    // DLL 2
    float responseNum;
    float responseAvg;
    int responseCount = 1;

    // Hook to run on shutdown process call
    Thread shutdownHook = new Thread(() -> {
        try {
            System.out.println("Shutting down");
            Thread.sleep(300);
        } catch (InterruptedException e) {
            System.out.println("Error shutting down: "  + e.getMessage());
        }

    });

    //endregion

    /**
     * Contains setup for the main form -> Used for sending out survey to client forms
     */
    public MainForm()
    {
        // Creating clients to test response/Doubly linked list formatting
        for (int i = 0; i < clientCount; i++) {
            new ClientForm();
        }

        setSize(600, 500);
        setLocation(450, 150);
        setLayout(myLayout);

        //Graceful shutdown (Add logic so system does not instantly exit - remove system.exit(0)?)
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            try{
//                System.out.println("Graceful shutdown started");
//                Thread.sleep(5000);
//                System.out.println("Graceful shutdown complete");
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//            }
//        }));

        Runtime.getRuntime().addShutdownHook(shutdownHook);

        // UI Building
        LabelBuilder();
        TextFieldBuilder();
        ButtonBuilder();

        // JTable
        QuestionTable(myLayout);

        // Server related
        getParameters();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        setResizable(false);
        setVisible(true);
    }

    //region Helper Methods

    /**
     * For set up of all labels within main form
     */
    private void LabelBuilder()
    {
        // For use of apache import
        String headerText = "survey by network";

        lblHeader = ComponentBuilder.CreateALabel(StringUtils.toRootUpperCase(headerText), 185, 5, myLayout, this);
        lblHeader.setFont(new Font("Times New Roman", Font.BOLD,20));
        lblSearch = ComponentBuilder.CreateALabel("Search Question:", 20, 32, myLayout, this);

        // If not a part of JTable
        lblSurveyQuestions = ComponentBuilder.CreateALabel("Survey Questions", 100, 52, myLayout, this);
        lblSortBy = ComponentBuilder.CreateALabel("Sort by:", 40, 190, myLayout, this);

        lblTopic = ComponentBuilder.CreateALabel("Topic:", 370, 40, myLayout, this);
        lblQn = ComponentBuilder.CreateALabel("Qn:", 370, 60, myLayout, this);
        lblQnNumber = ComponentBuilder.CreateALabel("1", 370, 80, myLayout, this);
        lblOne = ComponentBuilder.CreateALabel("1:", 370, 110, myLayout, this);
        lblTwo = ComponentBuilder.CreateALabel("2:", 370, 130, myLayout, this);
        lblThree = ComponentBuilder.CreateALabel("3:", 370, 150, myLayout, this);
        lblFour = ComponentBuilder.CreateALabel("4:", 370, 170, myLayout, this);
        lblFive = ComponentBuilder.CreateALabel("5:", 370, 190, myLayout, this);

        lblLinkedList = ComponentBuilder.CreateALabel("Linked List:", 20, 235, myLayout, this);
        lblBinaryTree = ComponentBuilder.CreateALabel("Binary Tree:", 20, 310, myLayout, this);

        lblPreOrder = ComponentBuilder.CreateALabel("Pre-Order", 80, 382, myLayout, this);
        lblInOrder = ComponentBuilder.CreateALabel("In-Order", 230, 382, myLayout, this);
        lblPostOrder = ComponentBuilder.CreateALabel("Post-Order", 380, 382, myLayout, this);

    }

    /**
     * For set up of all text fields within main form
     */
    private void TextFieldBuilder()
    {
        txtSearch = ComponentBuilder.CreateATextField(20,125, 32, myLayout, this);

        txtTopic = ComponentBuilder.CreateATextField(15, 410, 40, myLayout, this);
//        txtTopic.setEditable(false);
        txtQn = ComponentBuilder.CreateATextArea(15,3, 410, 60, myLayout, this);
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        txtQn.setBorder(border);
        txtQn.setLineWrap(true);
        txtQn.setWrapStyleWord(true);
//        txtQn.setEditable(false);
        txtOne = ComponentBuilder.CreateATextField(15, 410, 110, myLayout, this);
//        txtOne.setEditable(false);
        txtTwo = ComponentBuilder.CreateATextField(15, 410, 130, myLayout, this);
//        txtTwo.setEditable(false);
        txtThree = ComponentBuilder.CreateATextField(15, 410, 150, myLayout, this);
//        txtThree.setEditable(false);
        txtFour = ComponentBuilder.CreateATextField(15, 410, 170, myLayout, this);
//        txtFour.setEditable(false);
        txtFive = ComponentBuilder.CreateATextField(15, 410, 190, myLayout, this);
//        txtFive.setEditable(false);

        txtLinkedList = ComponentBuilder.CreateATextArea(55, 3, 20, 252, myLayout, this);
        txtLinkedList.setBorder(border);
        txtLinkedList.setLineWrap(true);
        txtLinkedList.setEditable(false);

        txtBinaryTree = ComponentBuilder.CreateATextArea(55, 3, 20, 327, myLayout, this);
        txtBinaryTree.setBorder(border);
        txtBinaryTree.setLineWrap(true);
        txtBinaryTree.setEditable(false);

        txtMessage = ComponentBuilder.CreateATextField(55, 20, 435, myLayout, this);
        txtMessage.setBackground(Color.lightGray);
        txtMessage.setEditable(false);
    }

    /**
     * For set up of all buttons within main form
     */
    private void ButtonBuilder()
    {
        // If not a part of JTable
        btnSortQn = ComponentBuilder.CreateAButton("Qn #", 76, 18, 87, 190, this, myLayout, this);
        btnSortTopic = ComponentBuilder.CreateAButton("Topic", 76, 18, 165, 190, this, myLayout, this);
        btnSortQuestion = ComponentBuilder.CreateAButton("Question", 88, 18, 244, 190, this, myLayout, this);

        btnSend = ComponentBuilder.CreateAButton("Send", 150, 18, 400, 213, this, myLayout, this);
        btnConnect = ComponentBuilder.CreateAButton("Connect", 100, 18, 250, 213, this, myLayout, this);
        btnExit = ComponentBuilder.CreateAButton("Exit", 150, 18, 50, 213, this, myLayout, this);

        btnPreOrder = ComponentBuilder.CreateAButton("Display", 75, 18, 73, 402, this, myLayout, this);
        btnInOrder = ComponentBuilder.CreateAButton("Display", 75, 18, 217, 402, this, myLayout, this);
        btnPostOrder = ComponentBuilder.CreateAButton("Display", 75, 18, 373, 402, this, myLayout, this);

        //btnDisplay = ComponentBuilder.CreateAButton("Display", 75, 18, 500, 385, this, myLayout, this);
        btnSave = ComponentBuilder.CreateAButton("Save", 75, 18, 500, 405, this, myLayout, this);
    }

    /**
     * Containing set up and logic for JTable -> Search, column names and loading of sample data
     * @param myPanelLayout
     */
    public void QuestionTable(SpringLayout myPanelLayout)
    {
        // Create a panel to hold all other components
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        add(topPanel);

        // Create column names
        String columnNames[] =
                { "#", "Topic", "Question"};

        // Create some data
        qnValues = new ArrayList();

        // Test data before sample load
//        qnValues.add(new Object[] {"1","Requirements", "Test"});
//        qnValues.add(new Object[] {"2","Requirements", "Test2"});

        // Sample data load method
        LoadSampleData();

//        Testing model storing
//        System.out.println(Arrays.toString(qnValues.get(2)));
//        System.out.println(Arrays.toString(qnValues.get(5)));

        // constructor of JTable model
        qnModel = new MyModel(qnValues, columnNames);

        // Create a new table instance
        qnTable = new JTable(qnModel);

        qnTable.addMouseListener(this);

        // Search logic
        sorter = new TableRowSorter<>(qnModel);
        qnTable.setRowSorter(sorter);
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(txtSearch.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(txtSearch.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(txtSearch.getText());
            }
            public void search(String str)
            {
                if(str.length() == 0){
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(str));
                }
            }
        });

        // Configure some of JTable's parameters
        qnTable.isForegroundSet();
        qnTable.setShowHorizontalLines(true);
        qnTable.setRowSelectionAllowed(true);
        qnTable.setColumnSelectionAllowed(true);
        add(qnTable);

        // Change the text and background colours
        qnTable.setSelectionForeground(Color.white);
        qnTable.setSelectionBackground(Color.lightGray);

        qnTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        qnTable.getColumnModel().getColumn(1).setPreferredWidth(66);
        qnTable.getColumnModel().getColumn(2).setPreferredWidth(200);

        // Add the table to a scrolling pane, size and locate
        JScrollPane scrollPane = qnTable.createScrollPaneForTable(qnTable);
        topPanel.add(scrollPane, BorderLayout.CENTER);
        topPanel.setPreferredSize(new Dimension(320, 110));
        myPanelLayout.putConstraint(SpringLayout.WEST, topPanel, 20, SpringLayout.WEST, this);
        myPanelLayout.putConstraint(SpringLayout.NORTH, topPanel, 72, SpringLayout.NORTH, this);
    }

    /**
     * Helper method for loading sample data to JTable
     */
    public void LoadSampleData()
    {
        try{
            BufferedReader br = new BufferedReader(new FileReader("SurveyByNetwork_SampleData.txt"));

            ArrayList<String> data = new ArrayList();
            String line;

            // To skip the first 8 listings
            for (int i = 0; i < 8; i++) {
                line = br.readLine();
            }

            while((line = br.readLine()) != null)
            {
                data.add(line);
                for (int i = 0; i < 7; i++) {
                    line = br.readLine();
                    data.add(line);
                }
                qnValues.add(new Object[] {data.get(0), data.get(1), data.get(2), data.get(3),
                                            data.get(4), data.get(5), data.get(6), data.get(7)});
//                Checking entire object is loaded
//                System.out.println(data.get(7));
                data.clear();
            }
            br.close();
        }
        catch (Exception e)
        {
            System.out.println("Error reading sample data: " + e.getMessage());
        }
    }

    /**
     * Unused helper method to clear question/answer fields
     */
    public void ClearFields()
    {
        txtQn.setText("");
        lblQnNumber.setText("");
        txtTopic.setText("");
        txtOne.setText("");
        txtTwo.setText("");
        txtThree.setText("");
        txtFour.setText("");
        txtFive.setText("");
    }
    //endregion

    //region Sorting logic

    /**
     * Bubble sort used to sort JTable arraylist of object array
     * @param arr ArrayList of type Object Array to be sorted
     */
    public static void BubbleSort(ArrayList<Object[]> arr)
    {
        for(int j=0; j<arr.size(); j++)
        {
            for(int i=j+1; i<arr.size(); i++)
            {
                if((arr.get(i)[0]).toString().compareToIgnoreCase(arr.get(j)[0].toString())<0)
                {
                    Object[] words = arr.get(j);
                    arr.set(j, arr.get(i));
                    arr.set(i, words);
                }
            }
//            System.out.println(arr.get(j)[0] + " - " + arr.get(j)[1]);
        }
    }

    /**
     * 1st method used to create mergesort
     * assigns values to global variables for list and recreates new array list before passing to second method
     * (Revise if additional time available)
     * @param arr
     */
    public void MSort(ArrayList<Object[]> arr)
    {
        list = arr;
        helper0 = new Object[list.size()];
        helper1 = new Object[list.size()];
        helper2 = new Object[list.size()];
        helper3 = new Object[list.size()];
        helper4 = new Object[list.size()];
        helper5 = new Object[list.size()];
        helper6 = new Object[list.size()];
        helper7 = new Object[list.size()];
        MergeSort(0, list.size() -1);
    }

    /**
     * 2nd method used to create mergesort
     * Recursive method for sorting until each section is split correctly before passing to third method
     * @param low Lowest size value of passed list (0)
     * @param high Size of list to be sorted
     */
    private void MergeSort(int low, int high)
    {
        if (low < high)
        {
            int middle = low+(high-low)/2;
            MergeSort(low, middle);
            MergeSort(middle+1, high);
            MergeS(low, middle, high);
        }
    }

    /**
     * 3rd method used to create mergesort
     * Called once sort has been split into even sections
     * Using helper variables to assign and sort each section (low/middle/high) through string comparisons
     * @param low Lowest size of list passed
     * @param middle Middle value set by 2nd method
     * @param high Highest value set by 2nd method
     */
    private void MergeS(int low, int middle, int high)
    {
        for (int i = low; i <= high; i++)
        {
            helper0[i] = list.get(i)[0];
            helper1[i] = list.get(i)[1];
            helper2[i] = list.get(i)[2];
            helper3[i] = list.get(i)[3];
            helper4[i] = list.get(i)[4];
            helper5[i] = list.get(i)[5];
            helper6[i] = list.get(i)[6];
            helper7[i] = list.get(i)[7];
        }

        int helperLeft = low;
        int helperRight = middle + 1;
        int current = low;

        while(helperLeft <= middle && helperRight <= high)
        {
            if(helper1[helperLeft].toString().compareToIgnoreCase(helper1[helperRight].toString())<0)
            {
                list.get(current)[0] = helper0[helperLeft];
                list.get(current)[1] = helper1[helperLeft];
                list.get(current)[2] = helper2[helperLeft];
                list.get(current)[3] = helper3[helperLeft];
                list.get(current)[4] = helper4[helperLeft];
                list.get(current)[5] = helper5[helperLeft];
                list.get(current)[6] = helper6[helperLeft];
                list.get(current)[7] = helper7[helperLeft];
                helperLeft++;
            }
            else
            {
                list.get(current)[0] = helper0[helperRight];
                list.get(current)[1] = helper1[helperRight];
                list.get(current)[2] = helper2[helperRight];
                list.get(current)[3] = helper3[helperRight];
                list.get(current)[4] = helper4[helperRight];
                list.get(current)[5] = helper5[helperRight];
                list.get(current)[6] = helper6[helperRight];
                list.get(current)[7] = helper7[helperRight];
                helperRight++;
            }
            current++;
        }
        while(helperLeft <= middle)
        {
            list.get(current)[0] = helper0[helperLeft];
            list.get(current)[1] = helper1[helperLeft];
            list.get(current)[2] = helper2[helperLeft];
            list.get(current)[3] = helper3[helperLeft];
            list.get(current)[4] = helper4[helperLeft];
            list.get(current)[5] = helper5[helperLeft];
            list.get(current)[6] = helper6[helperLeft];
            list.get(current)[7] = helper7[helperLeft];
            current++;
            helperLeft++;
        }
    }

    /**
     * Low efficiency sorting method through comparing each item, one at a time to one another
     * @param arr ArrayList of object arrays to sort
     */
    public static void InsertionSort(ArrayList<Object[]> arr)
    {
        int j;
        Object[] key;
        int i;

        for (j = 1; j < arr.size(); j++)
        {
            key = arr.get(j);
            for(i = j - 1; (i >= 0) && (arr.get(i)[2].toString().compareToIgnoreCase(key[2].toString()))>0; i--)
            {
                arr.set(i+1, arr.get(i));
            }
            arr.set(i+1, key);
        }
    }
    //endregion

    //region Methods for server

    /**
     * Method for connecting to separate server application
     * @param serverName Name of server
     * @param serverPort Port of server
     */
    public void connect(String serverName, int serverPort)
    {
        System.out.println("Establishing connection. Please wait ...");
        try
        {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            open();
        }
        catch (UnknownHostException uhe)
        {
            System.out.println("Host unknown: " + uhe.getMessage());
        }
        catch (IOException ioe)
        {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    /**
     * Method for handling sent questionnaires to client form
     */
    private void send()
    {
        try
        {
            if (!txtTopic.getText().equals("") && !txtQn.getText().equals("") && !txtOne.getText().equals("")
                    && !txtTwo.getText().equals("") && !txtThree.getText().equals("") && !txtFour.getText().equals("")
                    && !txtFive.getText().equals(""))
            {
                streamOut.writeUTF(txtTopic.getText());
                streamOut.writeUTF(txtQn.getText());
                streamOut.writeUTF(txtOne.getText());
                streamOut.writeUTF(txtTwo.getText());
                streamOut.writeUTF(txtThree.getText());
                streamOut.writeUTF(txtFour.getText());
                streamOut.writeUTF(txtFive.getText());
                streamOut.flush();
                responseNum = 0;
                responseCount = 1;
            } else
            {
                txtMessage.setText("** Unable to send if required fields are empty **");
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Sending error: " + ioe.getMessage());
            close();
        }
    }

    /**
     * Method for handling received strings from client forms
     * @param msg String received (1-5)
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
            println(msg);
            if (msg.matches("(0|[1-5])"))
            {
                responseNum += Integer.parseInt(msg);
                responseAvg = responseNum / responseCount;
                responseCount++;

                // DLL Logic ? - responseCount == (Number of clients to respond before displaying DLL + 1 (main))
                if (responseCount == clientCount + 1)
                {
                    dlList.head.append(new DNode(txtTopic.getText() + ", Qn " + lblQnNumber.getText() + ", " +
                            String.format("%.1f", responseAvg) + " "));
                    txtLinkedList.setText(dlList.print());
                }


            }
        }
    }

    /**
     * Helper method for server to set threads/streams
     */
    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client = new ChatClientThread1(this, socket);
        }
        catch (IOException ioe)
        {
            println("Error opening output stream: " + ioe);
        }
    }

    /**
     * Helper method for closing threads/stream
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
        client.close();
        client.stop();
    }

    /**
     * Method used for printing to set area of main form
     * @param msg Message to be displayed
     */
    void println(String msg)
    {
        //display.appendText(msg + "\n");
        txtMessage.setText(msg);
    }

    /**
     * Helper method for setting server name and port
     */
    public void getParameters()
    {
//        serverName = getParameter("host");
//        serverPort = Integer.parseInt(getParameter("port"));

        serverName = "localhost";
        serverPort = 4444;
    }

    //endregion

    //region Saving BTree
    /**
     * Method used for saving binary tree to a hashmap
     * Assigns key/value pair based on number & topic
     */
    public void saveHashmap()
    {
        try
        {
            PrintWriter writeFile = new PrintWriter(new FileWriter("Hashmap-Save.txt"));
            HashMap<Object, Object> bTreeHM = new HashMap<Object, Object>();

            String[] saveOrder = binTree.preorderTraverseTree(binTree.root).split(",");

            for (int i = 0; i <= saveOrder.length - 2; i++) {
                String[] temp = saveOrder[i].split("-");
                bTreeHM.put(temp[0], temp[1]);
            }
            writeFile.println(bTreeHM);
            writeFile.close();
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

// endregion

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnExit)
        {
            System.exit(0);
        }
        if (e.getSource() == btnSortQn)
        {
            BubbleSort(qnValues);
            qnModel.fireTableDataChanged();
        }
        if (e.getSource() == btnSortTopic)
        {
            MSort(qnValues);
            qnModel.fireTableDataChanged();
        }
        if (e.getSource() == btnSortQuestion)
        {
            InsertionSort(qnValues);
            qnModel.fireTableDataChanged();
        }

        // Adjust once networking is done to contain averages
        if (e.getSource() == btnSend)
        {
            send();
            responseCount = 1;
            responseAvg = 0;
            responseNum = 0;

            // Doubly Linked List - Temp display
            if(!txtTopic.getText().equals(""))
            {
                binTree.addNode(parseInt(lblQnNumber.getText()), txtTopic.getText());
//                dlList.head.append(new DNode(txtTopic.getText() + ", Qn " + lblQnNumber.getText() + ", "));
//                txtLinkedList.setText(dlList.print());
            }

            // Bin Tree - Temp display
//            if(!txtTopic.getText().equals(""))
//            {
//                binTree.addNode(parseInt(lblQnNumber.getText()), txtTopic.getText());
////                binTree.inOrderTraverseTree(binTree.root);
////                txtBinaryTree.setText(binTree.postOrderTraverseTree(binTree.root));
//            }


//            ClearFields();
        }

        if(e.getSource() == btnPreOrder)
        {
            txtBinaryTree.setText("PRE-ORDER: " + binTree.preorderTraverseTree(binTree.root));
            bTreeOrder = 1;
        }

        if(e.getSource() == btnInOrder)
        {
            txtBinaryTree.setText("IN-ORDER: " + binTree.inOrderTraverseTree(binTree.root));
            bTreeOrder = 2;
        }

        if(e.getSource() == btnPostOrder)
        {
            txtBinaryTree.setText("POST-ORDER: " + binTree.postOrderTraverseTree(binTree.root));
            bTreeOrder = 3;
        }

        if (e.getSource() == btnConnect)
        {
            connect(serverName, serverPort);
        }

        if (e.getSource() == btnSave)
        {
            saveHashmap();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // Temp variables used to assign from Object[] -> String -> String[]
        String temp1 = Arrays.toString(qnValues.get(qnTable.getSelectedRow()));
        String[] temp2 = temp1.split(",");

        // Assigning text fields based on JTable
        lblQnNumber.setText(qnTable.getValueAt(qnTable.getSelectedRow(), 0).toString());
        txtTopic.setText(qnTable.getValueAt(qnTable.getSelectedRow(), 1).toString());
        txtQn.setText(qnTable.getValueAt(qnTable.getSelectedRow(), 2).toString());

        // Using temp variable storing array of object split into Question number -> Topic .. -> Answer E to assign
        txtOne.setText(temp2[3].substring(1));
        txtTwo.setText(temp2[4].substring(1));
        txtThree.setText(temp2[5].substring(1));
        txtFour.setText(temp2[6].substring(1));
        txtFive.setText(temp2[7].substring(1, temp2[7].length() - 1));
    }

    //region Unnecessary mouselistener methods
    public void mousePressed(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {

    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }
    //endregion
}
