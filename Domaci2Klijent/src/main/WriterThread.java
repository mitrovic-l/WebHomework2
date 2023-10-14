package main;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriterThread implements Runnable{

    private Socket socket;

    public WriterThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        PrintWriter out = null;
        try {
            out = new PrintWriter( new OutputStreamWriter(socket.getOutputStream()), true);
            Scanner scanner = new Scanner(System.in);
            while (true){
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("!quit")){
                    out.println("!quit");
                    System.out.println("Napustili ste chat.");
                    break;
                }
                else
                    out.println(input);
            }

        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("WriterThread: problem sa socket-om");
        } finally {
            if (out != null)
                out.close();
        }
    }
}
