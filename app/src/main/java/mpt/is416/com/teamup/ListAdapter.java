package mpt.is416.com.teamup;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 8/10/2015.
 */
public class ListAdapter extends BaseAdapter {
    private ArrayList<SingleChatItem> chatItemList;
    public Context context;
    ListAdapter(Context c){
        context = c;
        chatItemList = new ArrayList<SingleChatItem>();
        Resources res = c.getResources();
        String[] groupChatTitles = res.getStringArray(R.array.group_chat_titles);
        String[] groupChatSections = res.getStringArray(R.array.group_chat_section);
        int[] imgs = {R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,R.drawable.pic5,R.drawable.pic6,R.drawable.pic7,R.drawable.pic8,R.drawable.pic9};

        for(int i=0; i<groupChatTitles.length;i++){

            chatItemList.add(new SingleChatItem(groupChatTitles[i],groupChatSections[i], imgs[i]));

        }
    }
    @Override
    public int getCount() {
        return chatItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TO-DO retrieve the primary key of single chat item in DB
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View singleChat = null;
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleChat = inflater.inflate(R.layout.single_chat_item, parent, false);
            TextView titles = (TextView) singleChat.findViewById(R.id.projectTitle);
            TextView sections = (TextView) singleChat.findViewById(R.id.projectSection);
            ImageView imgs = (ImageView) singleChat.findViewById(R.id.chatItemImageView);
            SingleChatItem sci = chatItemList.get(position);

            titles.setText(sci.groupChatTitle);
            sections.setText(sci.groupChatSection);
            imgs.setImageResource(sci.chatImg);
        }catch(Exception e){
            e.printStackTrace();
        }
        return singleChat;
    }
}
