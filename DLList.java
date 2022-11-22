package Flawless_Feedback;

// A Node is a node in a doubly-linked list.
class DNode
{              // class for nodes in a doubly-linked list

    DNode prev;              // previous Node in a doubly-linked list
    DNode next;              // next Node in a doubly-linked list
    Object data;
    //public char data;       // data stored in this Node

    DNode()
    {                // constructor for head Node
        prev = this;           // of an empty doubly-linked list
        next = this;
        data = "Default";
        // data = 'H';           // not used except for printing data in list head
    }

    DNode(Object dataPass)
    {       // constructor for a Node with data
        prev = null;
        next = null;
        data = dataPass;
        //this.data = data;     // set argument data to instance variable data
    }

    public void append(DNode newNode)
    {  // attach newNode after this Node
        newNode.prev = this;
        newNode.next = next;
        if (next != null)
        {
            next.prev = newNode;
        }
        next = newNode;
//        System.out.println("Node with data " + newNode.data
//                + " appended after Node with data " + data);
    }

    public void insert(DNode newNode)
    {  // attach newNode before this Node
        newNode.prev = prev;
        newNode.next = this;
        prev.next = newNode;;
        prev = newNode;
//        System.out.println("Node with data " + newNode.data
//                + " inserted before Node with data " + data);
    }

    public void remove()
    {              // remove this Node
        next.prev = prev;                 // bypass this Node
        prev.next = next;
//        System.out.println("Node with data " + data + " removed");
    }
    public String toString(){
        return this.data + " - " + this.data;
    }
}

class DLList
{

    DNode head;
    float nodeAvg = 0;

    public DLList()
    {
        head = new DNode();
    }

    public DLList(String s1)
    {
        head = new DNode(s1);
    }

    public DNode find(String wrd1)
    {          // find Node containing x
        for (DNode current = head.next; current != head; current = current.next)
        {
            if (current.data.toString().compareToIgnoreCase(wrd1) == 0)
            {        // is x contained in current Node?
//                System.out.println("Data " + wrd1 + " found");
                return current;               // return Node containing x
            }
        }
//        System.out.println("Data " + wrd1 + " not found");
        return null;
    }

    //This Get method Added by Matt C
    public DNode get(int i)
    {
        DNode current = this.head;
        if (i < 0 || current == null)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        while (i > 0)
        {
            i--;
            current = current.next;
            if (current == null)
            {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        return current;
    }

    public String toString()
    {
        String str = "";
        if (head.next == head)
        {             // list is empty, only header Node
            return "List Empty";
        }
        str = "list content = ";
        for (DNode current = head.next; current != head && current != null; current = current.next)
        {
            str = str + current.data;
        }
        return str;
    }

    public String print()
    {                  // print content of list
        String msgLinkedList;
        int count = 0;
        if (head == null)
        {             // list is empty, only header Node
            msgLinkedList = "";
            return msgLinkedList;
        }
        msgLinkedList = "HEAD <->";
        for (DNode current = head.next; current != head; current = current.next)
        {
            if (count > 0)
            {
                msgLinkedList += "<-->";
            }
            msgLinkedList += " " + current.data;
            count++;

        }
        msgLinkedList += "<-> TAIL";
        return msgLinkedList;
    }
}

