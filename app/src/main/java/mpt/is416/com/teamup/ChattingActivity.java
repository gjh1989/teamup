package mpt.is416.com.teamup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by User on 13/10/2015.
 */
public class ChattingActivity extends AppCompatActivity implements FetchUpdatesTask.AsyncResponse{
    private final String TAG = ChattingActivity.class.getSimpleName();
    Button sendBtn;
    Toolbar toolbar;
    EditText sendMsg;
    Context context;
    MessageListAdapter msgListAdapter;
    ListView messageListView;
    String deviceID, cid, regId;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    Context applicationContext;
    GoogleCloudMessaging gcmObj;
    String[] sendParams = {};
    String chattingGroupInfo = "";
    private String rawJson;
    public static final String REG_ID = "regId";
    public static final String CHAT_ID = "cid";
    public static final String DEVICE_ID = "deviceId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        applicationContext = getApplicationContext();
        setContentView(R.layout.activity_chatting);
        Bundle bundle = this.getIntent().getExtras();
        String chatRoomTitle = bundle.getString("chatTitle");
        deviceID = bundle.getString("deviceID");
        cid = bundle.getString("chatRoomID");
        regId = PreferenceManager.getDefaultSharedPreferences(this).getString("regId", null);
        chattingGroupInfo = cid;
        toolbar = (Toolbar) findViewById(R.id.toolbar_chatting);
        toolbar.setTitle(chatRoomTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);

        registerReceiver(broadcastReceiver, new IntentFilter(
                "CHAT_MESSAGE_RECEIVED"));
        msgListAdapter = new MessageListAdapter(this);
        populateChatMessages();
        new FetchMessagesTask(context, msgListAdapter, deviceID).execute(chattingGroupInfo);

        sendBtn = (Button)findViewById(R.id.send_btn);
        sendMsg = (EditText)findViewById(R.id.message_sent);

        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                sendToDB(sendMsg.getText().toString().trim());
                //new SendMessage().execute(sendMsg.getText().toString().trim());
                clearEditText();
            }
        });

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle b = intent.getExtras();

            String message = b.getString("message");

            Log.i(TAG, " Received in Activity " + message + ", NAME = "
                    + ", dev ID = ");

            //sendToDB(message); // adding to db

        }
    };

    private void populateChatMessages(){

        messageListView = (ListView) this.findViewById(R.id.chat_messages);
        messageListView.setAdapter(msgListAdapter);

    }

    private void clearEditText(){
        sendMsg.clearFocus();
        sendMsg.setText("");
        hideKeyBoard(sendMsg);
    }
    private void sendToDB(String sentMsg){
        String[] params = {sentMsg};
        new SendMessage().execute(params);
        populateChatMessages();
    }
    private void hideKeyBoard(EditText edt) {
        InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }

    public class SendMessageToDB extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String msg = params[0];
            URL url = null;
            HttpURLConnection urlConnection = null;
            String BASE_URL = "http://teamup-jhgoh.rhcloud.com/messageManager.php?";
            String[] keys = {"method", "sid", "cid", "message"};
            //TO-DO: set 1 index to deviceID and 2 index to cid, NOW is testing according TO DB data
            String[] values = {"insertMessage", regId, cid, msg};
            HTTPUtil util = new HTTPUtil();
            try {

                url = util.buildURL(BASE_URL, keys, values);
                urlConnection = util.getConnection(url);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                    /*StringBuffer buffer = new StringBuffer();

                    if (inputStream == null) {
                        // Nothing to do.
                        return msg;
                    }*/
            } catch (Exception ex) {
                msg = "Message could not be stored to DB";
            }finally {
                if(urlConnection != null){
                    util.disconnect(urlConnection);
                }

            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            if (msg.equals("Message could not be sent")) {
                //update the status of the message to unsent

            }else{
                // set the sending time to current time
                Date date = new Date();
                Message message = new Message(regId, "cid", new Timestamp(date.getTime()), msg);
                msgListAdapter.addMessage(message, msgListAdapter.DIRECTION_OUTGOING);
                msgListAdapter.notifyDataSetChanged();

            }
        }
    }

    public class SendMessage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String msg = params[0];
            URL url = null;
            HttpURLConnection urlConnection = null;
            String BASE_URL = "http://teamup-jhgoh.rhcloud.com/messageManager.php?";
            String[] keys = {"method", "sid", "cid", "message"};
            //TO-DO: set 1 index to deviceID and 2 index to cid, NOW is testing according TO DB data
            String[] values = {"insertMessage", regId, cid, msg};
            HTTPUtil util = new HTTPUtil();
            try {

                url = util.buildURL(BASE_URL, keys, values);
                urlConnection = util.getConnection(url);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                    /*StringBuffer buffer = new StringBuffer();

                    if (inputStream == null) {
                        // Nothing to do.
                        return msg;
                    }*/
            } catch (Exception ex) {
                msg = "Message could not be sent";
            }finally {
                if(urlConnection != null){
                    util.disconnect(urlConnection);
                }

            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            if (msg.equals("Message could not be sent")) {
                //update the status of the message to unsent

            }else{
                // set the sending time to current time
                Date date = new Date();
                Message message = new Message(regId, "cid", new Timestamp(date.getTime()), msg);
                msgListAdapter.addMessage(message, msgListAdapter.DIRECTION_OUTGOING);
                msgListAdapter.notifyDataSetChanged();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chattings, menu);
        return true;
    }
/*
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";

            if(!msg.equals("")) {
                Toast.makeText(ChattingActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_show_group_member:
                Toast.makeText(getApplicationContext(), "Show Group Members", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_show_group_milestones:
                Intent intent = new Intent(this, ViewGroupMilestonesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("chatRoomID", cid);
                intent.putExtras(bundle);
                Log.i(TAG, cid);
                startActivity(intent);
            default:
                Toast.makeText(getApplicationContext(), "Option with ID " + id + " is clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    public class storeRegIdinServer extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String msg = params[0];
            URL url = null;
            HttpURLConnection urlConnection = null;
            String BASE_URL = "http://teamup-jhgoh.rhcloud.com/messageManager.php?";
            String[] keys = {"regId"};
            //TO-DO: set 1 index to deviceID
            String[] values = {regId};
            HTTPUtil util = new HTTPUtil();
            try {

                url = util.buildURL(BASE_URL, keys, values);
                urlConnection = util.getConnection(url);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                    /*StringBuffer buffer = new StringBuffer();

                    if (inputStream == null) {
                        // Nothing to do.
                        return msg;
                    }*/
            } catch (Exception ex) {
                msg = "Message could not be sent";
            }finally {
                if(urlConnection != null){
                    util.disconnect(urlConnection);
                }

            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            if (msg.equals("Message could not be sent")) {
                //update the status of the message to unsent

            }else{

            }
        }
    }

    // Store RegId and Email entered by User in SharedPref
    /*private void storeRegIdinSharedPref(Context context, String regId,
                                        String deviceID) {
        String params[] = {regId, deviceID};
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putString(DEVICE_ID, deviceID);
        editor.commit();
        new storeRegIdinServer().execute(params);

    }*/
    // AsyncTask to register Device in GCM Server
    private void registerInBackground(final String deviceID) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    regId = gcmObj
                            .register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    //storeRegIdinSharedPref(applicationContext, regId, deviceID);
                    Toast.makeText(
                            applicationContext,
                            "Registered with GCM Server successfully.nn"
                                    + msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            applicationContext,
                            "Reg ID Creation Failed.nnEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    // Methods to call from database
    private void updateChats() {
        String[] fetchInfo = {"sendMessage", /*PreferenceManager
                .getDefaultSharedPreferences(this.getContext()).getString(ANDROID_ID,null)*/cid, regId};
        FetchUpdatesTask fetchUpdatesTask = new FetchUpdatesTask();
        fetchUpdatesTask.delegate = this;
        fetchUpdatesTask.execute(fetchInfo);
    }

    public void processFinish(String output) {
        rawJson = output;
        //Prepare the data and chatRoomAdapter
        prepareChatRoomData();

    }

    private void prepareChatRoomData() {

        try {
            // read JSON from assets folder
            //JSONObject json = new JSONObject(loadJSONfromAsset("samplechatrooms.json"));
            JSONObject json = new JSONObject(rawJson);
            JSONArray list = json.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject chatRoomObj = list.getJSONObject(i);
                ChatRoom chatRoom = new ChatRoom();
                chatRoom.setChatID(chatRoomObj.getString("cid"));
                chatRoom.setChatName(chatRoomObj.getString("cname"));
                chatRoom.setChatImage(chatRoomObj.getString("cimage"));
//                JSONArray participantList = chatRoomObj.getJSONArray("participants");
//                ArrayList<String> participants = new ArrayList<>();
//                for (int j = 0; j < participantList.length(); j++) {
//                    participants.add(participantList.getString(j));
//                }
//                chatRoom.setParticipants(participants);
                Log.i(TAG, chatRoom.toString());
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        applicationContext,
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            Toast.makeText(
                    applicationContext,
                    "This device supports Play services, App will work normally",
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }

    // When Application is resumed, check for Play services support to make sure
    // app will be running normally
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        //populateChatMessages();
        //new FetchMessagesTask(context, msgListAdapter, deviceID).execute(chattingGroupInfo);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}