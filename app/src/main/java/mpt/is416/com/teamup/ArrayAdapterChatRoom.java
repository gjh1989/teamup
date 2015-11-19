package mpt.is416.com.teamup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feng Xin on 8/10/2015.
 * Modified by Elyza on 21/10/2015.
 */
public class ArrayAdapterChatRoom extends ArrayAdapter<ChatRoom> {
    Context context;
    int layoutResourceId;
    List<ChatRoom> data = null;
    AddNewGroupActivity addNewGroupActivity;
    Bitmap imageBitmap;

    public ArrayAdapterChatRoom(Context context, int layoutResourceId, List<ChatRoom> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;

    }

    public void addChatRoom(ChatRoom ct){
        data.add(ct);
    }

    public ArrayList<ImageItem> getData(){
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = context.getResources().obtainTypedArray(R.array.schoolsPic);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgs.getResourceId(i, 1));
            imageItems.add(new ImageItem(bitmap,imgs.getString(i)));
        }
        return imageItems;
    }

    public Bitmap getBitmapImage(String imageTitle){
        //String chatImage = c.getChatImage();
        //GridViewActivity gridViewActivity = new GridViewActivity();
        ArrayList<ImageItem> allImage = getData();
        ImageItem imageItem = new ImageItem();
        for (int i = 0; i < allImage.size(); i++) {
            if(allImage.get(i).getTitle().substring(13).equals(imageTitle)){
                imageItem = new ImageItem(allImage.get(i).getImage(),imageTitle);

                //imageItem.setImage(allImage.get(i).getImage());
                //imageItem.setTitle(chatImage);
            }

        }
        return imageItem.getImage();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatRoom chatRoom = data.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_chatroom, parent, false);
        }

        ImageView chatImageIV = (ImageView) convertView.findViewById(R.id.chatImage);
        TextView chatNameTV = (TextView) convertView.findViewById(R.id.chatName);
        // TODO: Load image into Image View
        chatNameTV.setText(chatRoom.getChatName());
        chatImageIV.setImageBitmap(getBitmapImage(chatRoom.getChatImage()));

        return convertView;
    }
}
