package mpt.is416.com.teamup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by User on 13/10/2015.
 */
public class ChattingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chatting);
        toolbar.setTitle("Jacky");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        String[] chattingGroupInfo = {"OOAD","timestamp"};
        new FetchMessagesTask().execute(chattingGroupInfo);

    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";

            if(!msg.equals("")) {
                Toast.makeText(ChattingActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };
}
