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

    //    Date date = new Date();
//    Message message1 = new Message("m1", "cr1", new Timestamp(date.getTime()), "message1");
    //Message message2 = new Message("m2", "cr2", new Timestamp(date.getTime()), "message2");
    MessageListAdapter(Context c){
        this.context = c;
        messages = new ArrayList<Pair<Message, Integer>>();
//        messages.add(new Pair(message1,1));
        //messages.add(new Pair(message2,1));
    }

    public void addMessage(Message message, int direction) {
        messages.add(new Pair<Message, Integer>(message, direction));
        //notifyDataSetChanged();
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

        if(convertView == null){
            if(direction == 0){
                convertView = inflater.inflate(R.layout.single_chat_message, parent, false);
                convertView.findViewById(R.id.content_with_background)
                        .setBackgroundResource(R.drawable.out_message_bg);
            }else if(direction == 1){
                convertView = inflater.inflate(R.layout.right_single_chat_message, parent, false);
                convertView.findViewById(R.id.content_with_background)
                        .setBackgroundResource(R.drawable.in_message_bg);
            }
        }

        //LinearLayout linearLayoutOuter = (LinearLayout)convertView.findViewById(R.id.message_content);
        //LinearLayout linearLayoutInner = (LinearLayout)convertView.findViewById(R.id.content_with_background);
        TextView messageTime = (TextView)convertView.findViewById(R.id.textInfo);
        TextView textMsg = (TextView)convertView.findViewById(R.id.textMessage);

        Message message = messages.get(position).first;
        //LinearLayout.LayoutParams paramsOuter = (LinearLayout.LayoutParams)linearLayoutOuter.getLayoutParams();
        // set the message to left if incoming, right if outgoing

        // Incoming when direction is 0
        /*if(direction == 0){
            ((RelativeLayout)convertView.findViewById(R.id.single_message)).setGravity(Gravity.START);
            ((LinearLayout)convertView.findViewById(R.id.content_with_background)).setBackgroundResource(R.drawable.out_message_bg);
            //paramsOuter.gravity = RelativeLayout.ALIGN_PARENT_LEFT;
            //linearLayoutInner.setBackgroundResource(R.drawable.out_message_bg);
        }else if(direction == 1){
        // Outgoing when direction is 1
            ((RelativeLayout)convertView.findViewById(R.id.single_message)).setGravity(Gravity.END);
            //((RelativeLayout)convertView.findViewById(R.id.single_message))
            ((LinearLayout)convertView.findViewById(R.id.content_with_background)).setBackgroundResource(R.drawable.in_message_bg);
            //paramsOuter.gravity = RelativeLayout.ALIGN_PARENT_RIGHT;
            //linearLayoutInner.setBackgroundResource(R.drawable.in_message_bg);
        }*/

        //linearLayoutOuter.setLayoutParams(paramsOuter);
        textMsg.setText(message.getTextMsg());
        messageTime.setText(message.getTimeStamp().toString());

        return convertView;
    }
}
