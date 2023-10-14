package main;

import message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static final int SERVER_PORT = 9090;
    public static BlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    public static ConcurrentHashMap<String, Socket> connectedUsers = new ConcurrentHashMap<>();
    public static CopyOnWriteArrayList<String> messageHistory = new CopyOnWriteArrayList<>();
    public static ConcurrentHashMap<String, String> forbiddenWords = new ConcurrentHashMap<>();
    private static String beautifyWord(String word){
        String ret = null;
        char[] seq = word.toCharArray();
        for (int i=1; i<word.length() - 1; i++){
            seq[i] = '*';
        }
        ret = String.valueOf(seq);
        return ret;
    }
    private static void addForbiddenWords(){
        forbiddenWords.put("programiranje", beautifyWord("programiranje"));
        forbiddenWords.put("ponedeljak", beautifyWord("ponedeljak"));
        forbiddenWords.put("domaci", beautifyWord("domaci"));
        forbiddenWords.put("pepsi", beautifyWord("pepsi"));
        forbiddenWords.put("mafija", beautifyWord("mafija"));
        forbiddenWords.put("kosarka", beautifyWord("kosarka"));
        forbiddenWords.put("fakultet", beautifyWord("fakultet"));
        forbiddenWords.put("etf", beautifyWord("etf"));
    }

    public static void main(String[] args) {
        addForbiddenWords();
        try{
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            Thread broadcast = new Thread( new BroadcastThread());
            broadcast.start();
            System.out.println("Server pokrenut na portu: " + SERVER_PORT);
            while (true){
                Socket client = serverSocket.accept();
                System.out.println("Server primio novu konekciju...");
                Thread serverThread = new Thread( new ServerThread(client));
                serverThread.start();
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("Problem sa povezivanjem na klijenta");
        }
    }


}

