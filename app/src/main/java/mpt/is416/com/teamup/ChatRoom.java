package mpt.is416.com.teamup;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feng Xin on 8/10/2015.
 * Modified by Elyza on 21/10/2015.
 */
public class ChatRoom {
    private String chatName;
    private String chatImage;
    private String chatID;
    private List<String> participants;
    private Bitmap bitMap;
    //private GridViewActivity gridViewActivity;

    public ChatRoom(String chatName,String chatImage,List<String> participants) {
        this.participants = participants;
        this.chatImage = chatImage;
        this.chatName = chatName;
    }

    public ChatRoom(String chatName,String chatImage,List<String> participants,Bitmap bitmap) {
        this.participants = participants;
        this.chatImage = chatImage;
        this.chatName = chatName;
        this.bitMap = bitmap;
    }

    public ChatRoom() {
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }
    public Bitmap getBitmap() {
        return bitMap;
    }

    public void setBitmap(Bitmap bitMap) {
        this.bitMap = bitMap;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatImage() {
        return chatImage;
    }

    public void setChatImage(String chatImage) {
        this.chatImage = chatImage;
    }

    public Bitmap getBitmapImage(String itemTitle){
        GridViewActivity gridViewActivity = new GridViewActivity();
        ArrayList<ImageItem> allImage = gridViewActivity.getData();
        //ImageItem selectedImage = new ImageItem(itemTitle);
        ImageItem selectedImage = null;
        //TypedArray imgs = getResources().obtainTypedArray(R.array.pictures);
        for (int i = 0; i < allImage.size(); i++) {
            if(allImage.get(i).getTitle().equals(itemTitle)){
                selectedImage = allImage.get(i);
            }

            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            //imageItems.add(new ImageItem(bitmap,imgs.getString(i)));
        }
        return selectedImage.getImage();



    }

    public void setBitmapImage(Bitmap image){


    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
