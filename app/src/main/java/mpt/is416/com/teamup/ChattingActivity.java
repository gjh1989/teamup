package mpt.is416.com.teamup;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by User on 13/10/2015.
 */
public class ChattingActivity extends AppCompatActivity {
    Button sendBtn;
    Toolbar toolbar;
    EditText sendMsg;
    Context context;
    MessageListAdapter msgListAdapter;
    ListView messageListView;
    String deviceID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_chatting);
        Bundle bundle = this.getIntent().getExtras();
        String chatRoomTitle = bundle.getString("chatTitle");
        deviceID = bundle.getString("deviceID");
        toolbar = (Toolbar) findViewById(R.id.toolbar_chatting);
        toolbar.setTitle(chatRoomTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);
        String[] chattingGroupInfo = {"1","1"};
        msgListAdapter = new MessageListAdapter(this);
        messageListView = (ListView) this.findViewById(R.id.chat_messages);
        messageListView.setAdapter(msgListAdapter);
        new FetchMessagesTask(context, msgListAdapter, deviceID).execute(chattingGroupInfo);

        sendBtn = (Button)findViewById(R.id.send_btn);
        sendMsg = (EditText)findViewById(R.id.message_sent);

        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendToDB(sendMsg.getText().toString().trim());
                clearEditText();
            }
        });

    }

    private void clearEditText(){
        sendMsg.clearFocus();
        sendMsg.setText("");
        hideKeyBoard(sendMsg);
    }
    private void sendToDB(String sentMsg){
        String[] params = {sentMsg};
        new SendMessage().execute(params);
    }
    private void hideKeyBoard(EditText edt) {
        InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }

    public class SendMessage extends AsyncTask<String, Void, String> {


            @Override
            protected String doInBackground(String... params) {
                String errorMsg = "";
                try {
                    //TO-DO call http to update DB
                    return params[0];
                } catch (Exception ex) {
                    errorMsg = "Message could not be sent";
                }
                return errorMsg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (msg.equals("Message could not be sent")) {
                    //update the status of the message to unsent

                }else{
                    // set the sending time to current time
                    Date date = new Date();
                    Message message = new Message(deviceID, "cid", new Timestamp(date.getTime()), msg);
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
                startActivity(intent);
            default:
                Toast.makeText(getApplicationContext(), "Option with ID " + id + " is clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}
