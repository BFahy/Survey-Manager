package Flawless_Feedback;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ComponentBuilder {
        /**
         * JLabel for creating a label within application.
         * @param text Set contents of label
         * @param xPos X Position within application
         * @param yPos Y Position within application
         * @param layout Set layout used
         * @param frame Related to frame usage
         * @return Returns label when called upon
         */
        public static JLabel CreateALabel(String text, int xPos, int yPos, SpringLayout layout, JFrame frame)
        {
            JLabel myNewLabel = new JLabel(text);
            layout.putConstraint(SpringLayout.WEST,myNewLabel,xPos,SpringLayout.WEST,frame);
            layout.putConstraint(SpringLayout.NORTH,myNewLabel,yPos,SpringLayout.NORTH,frame);
            frame.add(myNewLabel);
            return myNewLabel;
        }

        public static JLabel CreateALabel(String text, int xPos, int yPos, SpringLayout layout,
                                          JFrame frame, Component component)
        {
            JLabel myNewLabel = new JLabel(text);
            layout.putConstraint(SpringLayout.WEST,myNewLabel,xPos,SpringLayout.WEST,component);
            layout.putConstraint(SpringLayout.NORTH,myNewLabel,yPos,SpringLayout.NORTH,component);
            frame.add(myNewLabel);
            return myNewLabel;
        }

        /**
         * JLabel for creating a button within application
         * @param text Set button contents
         * @param width Width of button
         * @param height Height of button
         * @param xPos X Position within application
         * @param yPos Y Position within application
         * @param listener Where the buttons action will go to
         * @param layout Set layout to be used
         * @param frame Related to frame usage
         * @return Returns JButton from method
         */
        public static JButton CreateAButton(String text, int width, int height, int xPos, int yPos,
                                            ActionListener listener, SpringLayout layout, JFrame frame)
        {
            JButton myNewButton = new JButton(text);
            myNewButton.addActionListener(listener);
            myNewButton.setPreferredSize(new Dimension(width,height));
            layout.putConstraint(SpringLayout.WEST,myNewButton,xPos,SpringLayout.WEST,frame);
            layout.putConstraint(SpringLayout.NORTH,myNewButton,yPos,SpringLayout.NORTH,frame);
            frame.add(myNewButton);
            return myNewButton;
        }

        public static JButton CreateAButton(String text, int width, int height, int xPos, int yPos,
                                            ActionListener listener, SpringLayout layout, JFrame frame, Component component)
        {
            JButton myNewButton = new JButton(text);
            myNewButton.addActionListener(listener);
            myNewButton.setPreferredSize(new Dimension(width,height));
            layout.putConstraint(SpringLayout.WEST,myNewButton,xPos,SpringLayout.WEST,component);
            layout.putConstraint(SpringLayout.NORTH,myNewButton,yPos,SpringLayout.NORTH,component);
            frame.add(myNewButton);
            return myNewButton;
        }

        public static JTextField CreateATextField(int size, int xPos, int yPos, SpringLayout layout, JFrame frame)
        {
            JTextField myNewTextField = new JTextField(size);
            layout.putConstraint(SpringLayout.WEST,myNewTextField,xPos,SpringLayout.WEST,frame);
            layout.putConstraint(SpringLayout.NORTH,myNewTextField,yPos,SpringLayout.NORTH,frame);
            frame.add(myNewTextField);
            return myNewTextField;
        }

        /**
         * Text field creator within JLabel
         * @param size Size of text field
         * @param xPos X Position within application
         * @param yPos Y Position within application
         * @param layout Layout used for organising
         * @param frame Relate to frame usage
         * @param component Where the text field will be placed relative to
         * @return Returns a new text field
         */
        public static JTextField CreateATextField(int size, int xPos, int yPos, SpringLayout layout,
                                                  JFrame frame, Component component)
        {
            JTextField myNewTextField = new JTextField(size);
            layout.putConstraint(SpringLayout.WEST,myNewTextField,xPos,SpringLayout.WEST,component);
            layout.putConstraint(SpringLayout.NORTH,myNewTextField,yPos,SpringLayout.NORTH,component);
            frame.add(myNewTextField);
            return myNewTextField;
        }

        public static JTextArea CreateATextArea(int sizeX, int sizeY, int xPos, int yPos, SpringLayout layout,
                                                JFrame frame)
        {
            JTextArea myNewTextArea = new JTextArea(sizeY, sizeX);
            layout.putConstraint(SpringLayout.WEST,myNewTextArea,xPos,SpringLayout.WEST,frame);
            layout.putConstraint(SpringLayout.NORTH,myNewTextArea,yPos,SpringLayout.NORTH,frame);
            frame.add(myNewTextArea);
            return myNewTextArea;
        }

        public static JTextArea CreateATextArea(int sizeX, int sizeY, int xPos, int yPos, SpringLayout layout,
                                                JFrame frame, Component component)
        {
            JTextArea myNewTextArea = new JTextArea(sizeY, sizeX);
            layout.putConstraint(SpringLayout.WEST,myNewTextArea,xPos,SpringLayout.WEST,component);
            layout.putConstraint(SpringLayout.NORTH,myNewTextArea,yPos,SpringLayout.NORTH,component);
            frame.add(myNewTextArea);
            return myNewTextArea;
        }

    //---------------------------------------------------------------------------------------------------
    // Source: http://www.dreamincode.net/forums/topic/231112-from-basic-jtable-to-a-jtable-with-a-tablemodel/
    // class that extends the AbstractTableModel
    //---------------------------------------------------------------------------------------------------
    class MyModel extends AbstractTableModel
    {
        ArrayList<Object[]> al;

        String[] header;

        int col;

        // constructor
        MyModel(ArrayList<Object[]> obj, String[] header)
        {
            // save the header
            this.header = header;
            // and the data
            al = obj;
            // get the column index
            col = this.findColumn("");
        }

        public int getRowCount()
        {
            return al.size();
        }

        public int getColumnCount()
        {
            return header.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex)
        {
            return al.get(rowIndex)[columnIndex];
        }

        public String getColumnName(int index)
        {
            return header[index];
        }

        public Class getColumnClass(int columnIndex)
        {
            if (columnIndex == col)
            {
                return Boolean.class; // For every cell in column 7, set its class to Boolean.class
            }
            return super.getColumnClass(columnIndex); // Otherwise, set it to the default class
        }

        // a method to add a new line to the table
        void add(String qNo, String topic, String question)
        {
            // make it an array[3] as this is the way it is stored in the ArrayList
            // (not best design but we want simplicity)
            Object[] item = new Object[3];
            item[0] = qNo;
            item[1] = topic;
            item[2] = question;
            al.add(item);
            // inform the GUI that I have change
            fireTableDataChanged();
        }
    }
}
