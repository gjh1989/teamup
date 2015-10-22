package mpt.is416.com.teamup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feng Xin on 12/10/2015.
 * Modified by Elyza on 21/10/2015.
 */
public class FragmentChats extends Fragment {
    private final String TAG = FragmentChats.class.getSimpleName();
    private ArrayAdapterChatRoom chatRoomAdapter;
    private ListView listView;
    private ArrayList<ChatRoom> chatRooms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_list, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        // Prepare the data and chatRoomAdapter
        prepareChatRoomData();
        chatRoomAdapter = new ArrayAdapterChatRoom(getActivity(), R.layout.fragment_group_list, chatRooms);
        listView = (ListView) getActivity().findViewById(R.id.chatrooms_list);
        listView.setAdapter(chatRoomAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Initiate L2 Chat Activity
                Intent intent = new Intent(getActivity(), ChattingActivity.class);
                Bundle bundle = new Bundle();
                String deviceID = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("android_id","noneExistedDevice");
                String chatRoomName = chatRooms.get(position).getChatName();
                bundle.putString("chatTitle", chatRoomName);
                bundle.putString("deviceID", deviceID);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
    }

    private void prepareChatRoomData() {
        chatRooms = new ArrayList<>();

        try {
            // read JSON from assets folder
            JSONObject json = new JSONObject(loadJSONfromAsset("samplechatrooms.json"));
            JSONArray list = json.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject chatRoomObj = list.getJSONObject(i);
                ChatRoom chatRoom = new ChatRoom();
                chatRoom.setChatName(chatRoomObj.getString("cname"));
                chatRoom.setChatImage(chatRoomObj.getString("cimage"));
                JSONArray participantList = chatRoomObj.getJSONArray("participants");
                ArrayList<String> participants = new ArrayList<>();
                for (int j = 0; j < participantList.length(); j++) {
                    participants.add(participantList.getString(j));
                }
                chatRoom.setParticipants(participants);
                chatRooms.add(chatRoom);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private String loadJSONfromAsset(String fileName) {
        String json = null;
        try {
            InputStream inputStream = this.getActivity().getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return json;
    }
}
