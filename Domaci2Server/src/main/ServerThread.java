package main;

import message.Message;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;

public class ServerThread implements Runnable{

    private Socket socket;

    public ServerThread(Socket socket) {
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
        PrintWriter out = null;

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String clientUsername = "";
            out.println("Ukucajte korisnicko ime: ");
            while (true){
                clientUsername = in.readLine();
                if (clientUsername == null)
                    return;
                if (Main.connectedUsers.containsKey(clientUsername)){
                    out.println("Korisnicko ime: " + clientUsername+ " je zauzeto. Molim Vas da izaberete drugacije korisnicko ime.");
                } else {
                    boolean connectMsg = true;
                    Main.messages.put( new Message("[INFO] Korisnik: " + clientUsername + " se konektovao.", clientUsername, connectMsg));
                    Main.connectedUsers.put(clientUsername, socket);
                    Main.messages.put( new Message("[INFO] Broj aktivnih korisnika: " + Main.connectedUsers.size(), clientUsername, false));
                    out.println("[INFO] Dobrodosli na PublicChat!");
                    if (Main.messageHistory.size() == 0){
                        out.println("[INFO] Nema neprocitanih poruka.");
                    }
                    else {
                        Iterator historyIterator = Main.messageHistory.iterator();
                        out.println("[INFO] Istorija poruka: ");
                        while (historyIterator.hasNext()) {
                            out.println(historyIterator.next());
                        }
                    }
                    break;
                }
            }

            while (true){
                String message = in.readLine();
                if (message == null)
                    return;
                if (message.equals("!quit")){
                    Message msg = new Message("[INFO] Korisnik: " + clientUsername + " je napustio chat.", clientUsername, true);
                    Main.messages.put(msg);
                    Main.connectedUsers.remove(clientUsername);
                    Main.messages.put(new Message("[INFO] Broj aktivnih korisnika: " + Main.connectedUsers.size(), clientUsername, true));
                    break;
                } else {
                    message = checkMessage(message);
                    Message msg = new Message(message, clientUsername, false);
                    msg.setText("---> " + clientUsername + ": " + message + "       [Sent at : " + msg.getDateString() + "]");
                    Main.messages.put(msg);
                    if (Main.messageHistory.size() >= 100)
                        Main.messageHistory.remove(0);
                    Main.messageHistory.add(msg.getText());
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("Problem sa povezivanjem na klijenta");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            close(this.socket, in, out);
        }
    }

    private   String checkMessage(String clientMessage) {
        if (clientMessage == null)
            return null;
        for (String s : clientMessage.split(" ")){
            if (Main.forbiddenWords.get(s.toLowerCase()) != null){
                // System.out.println("Uso u if da menja reci");
                clientMessage = clientMessage.replace(s, Main.forbiddenWords.get(s));
            }
        }
        return clientMessage;
    }

    public void close(Socket socket, BufferedReader in, PrintWriter out){
        try{
            if (socket != null)
                socket.close();
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("Greska pri diskonektovanju.");
        }
    }
}
