package Flawless_Feedback;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Helper class used by form to build JTable
 */
public class MyModel extends AbstractTableModel
{
    ArrayList<Object[]> al;
    String[] header;
    int col;
    // Constructor method used by MyModel
    public MyModel(ArrayList<Object[]> obj, String[] header)
    {
        // Retrieve parameters and assign them to set variables for use
        this.header = header;
        al = obj;
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
        return super.getColumnClass(columnIndex); // Set it to the default class
    }

    // Overwrite add method for use to add to table
    void add(String qNo, String topic, String question)
    {
        // Defines an array containing 3 strings
        Object[] item = new Object[3];
        item[0] = qNo;
        item[1] = topic;
        item[2] = question;
        al.add(item);
        // Method for updating GUI fulled from AbstractTableModel library
        fireTableDataChanged();
    }
}
