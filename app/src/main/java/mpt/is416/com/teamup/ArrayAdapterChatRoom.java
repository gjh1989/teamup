package mpt.is416.com.teamup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Feng Xin on 8/10/2015.
 * Modified by Elyza on 21/10/2015.
 */
public class ArrayAdapterChatRoom extends ArrayAdapter<ChatRoom> {
    Context context;
    int layoutResourceId;
    List<ChatRoom> data = null;

    public ArrayAdapterChatRoom(Context context, int layoutResourceId, List<ChatRoom> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    public void addChatRoom(ChatRoom ct){
        data.add(ct);
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
        return convertView;
    }
}
