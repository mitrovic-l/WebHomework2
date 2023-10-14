package message;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String text;
    private String clientUsername;
    private boolean connectMessage;
    private Date date;

    public Message(String text, String clientUsername, boolean connectMessage) {
        this.text = text;
        this.clientUsername = clientUsername;
        this.connectMessage = connectMessage;
        this.date = new Date();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public boolean isConnectMessage() {
        return connectMessage;
    }

    public void setConnectMessage(boolean connectMessage) {
        this.connectMessage = connectMessage;
    }

    public String getDateString(){
        String datePattern = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        String sDate = simpleDateFormat.format(this.date);
        return sDate;
    }
}
