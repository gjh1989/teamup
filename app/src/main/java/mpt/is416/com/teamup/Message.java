package mpt.is416.com.teamup;

import java.sql.Timestamp;

/**
 * Created by User on 21/10/2015.
 */
public class Message {
    private String sid;
    private String cid;
    private String senderName;
    private Timestamp timeStamp;
    private String textMsg;

    Message(String sid, String cid, String senderName, Timestamp timeStamp, String textMsg){
        this.sid = sid;
        this.cid = cid;
        this.senderName = senderName;
        this.timeStamp = timeStamp;
        this.textMsg = textMsg;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }
}
