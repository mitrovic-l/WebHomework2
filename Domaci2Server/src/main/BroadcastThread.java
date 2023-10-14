package main;

import message.Message;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class BroadcastThread implements Runnable{
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
        while (true){
            try {
                Message message = Main.messages.take();
                Main.connectedUsers.entrySet().forEach( user -> {
                    if (message.isConnectMessage() && message.getClientUsername().equals(user.getKey())) {
                        return;
                    }
                    try {
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(user.getValue().getOutputStream()), true);
                        out.println(message.getText());
                    } catch (IOException e) {
                        //e.printStackTrace();
                        System.err.println("Problem sa povezivanjem na klijenta");
                    }
                });
            } catch (InterruptedException e) {
                //e.printStackTrace();
                System.err.println("BroadcastThread: interrupt");
            }
        }
    }
}
