package mpt.is416.com.teamup;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feng Xin on 20/10/2015.
 */
public class MessageListAdapter extends BaseAdapter {

    //TO-DO: get the list of messages from the DB
    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;
    public Context context;
    private List<Pair<Message, Integer>> messages;

    MessageListAdapter(Context c){
        this.context = c;
        messages = new ArrayList<Pair<Message, Integer>>();
    }

    public void addMessage(Message message, int direction) {
        messages.add(new Pair<Message, Integer>(message, direction));
    }

    public void clearArrayList(){
        messages.clear();
    }


    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Pair<Message, Integer> getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        // retrieve the primary key of single chat item in DB
        return position;
    }
    // get the direction of the message
    @Override
    public int getItemViewType(int position) {
        return messages.get(position).second;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int direction = getItemViewType(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView messageTime, textMsg, senderName = null;
        if(convertView == null){
            if(direction == 0){
                convertView = inflater.inflate(R.layout.single_chat_message, parent, false);
                convertView.findViewById(R.id.content_with_background)
                        .setBackgroundResource(R.drawable.out_message_bg);

                senderName = (TextView)convertView.findViewById(R.id.senderName);
            }else if(direction == 1){
                convertView = inflater.inflate(R.layout.right_single_chat_message, parent, false);
                convertView.findViewById(R.id.content_with_background)
                        .setBackgroundResource(R.drawable.in_message_bg);
            }
        }

        messageTime = (TextView)convertView.findViewById(R.id.textInfo);
        textMsg = (TextView)convertView.findViewById(R.id.textMessage);

        Message message = messages.get(position).first;
        textMsg.setText(message.getTextMsg());
        messageTime.setText(message.getTimeStamp().toString());
        if(message.getSenderName()!=null){
            if(senderName != null){
                senderName.setText(message.getSenderName());
            }
        }

        return convertView;
    }
}
