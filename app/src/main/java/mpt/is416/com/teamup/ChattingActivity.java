package mpt.is416.com.teamup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Feng Xin on 13/10/2015.
 */
public class ChattingActivity extends AppCompatActivity {
    private final String TAG = ChattingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chatting);
        // TODO: Set to chat name
        toolbar.setTitle("Jacky");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // TODO: Get from previous activity
        String[] chattingGroupInfo = {"OOAD","timestamp"};
        // TODO: AsyncTask to send to database
//        new FetchMessagesTask().execute(chattingGroupInfo);
        Button sendBtn = (Button) findViewById(R.id.send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add message from EditText to ListView
                // TODO: Send message to database
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chattings, menu);
        return true;
    }

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
