package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReaderThread implements Runnable{

    private Socket socket;

    public ReaderThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getUser() {
        return socket;
    }

    public void setUser(Socket socket) {
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
        BufferedReader in = null;
        try {
            in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            while (true){
                String message = in.readLine();
                System.out.println(message);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("Socket zatvoren.");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
