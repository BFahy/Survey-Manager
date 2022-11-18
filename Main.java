package Flawless_Feedback;

public class Main {

    public static void main(String[] args) {

        // To start server immediately instead of running separate Main class
        ChatServer server;
        if (args.length != 1)
        {
            //System.out.println("Usage: java ChatServer port");
            server = new ChatServer(4444);
        }
        else
        {
            server = new ChatServer(Integer.parseInt(args[0]));
        }

        new MainForm();
//        new ClientForm();



         }
}
