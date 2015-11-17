package mpt.is416.com.teamup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddNewGroupActivity extends AppCompatActivity {
    private final String ANDROID_ID = "android_id";
    ImageView contactImgView;
    Button addParticipant;
    Button createGroup;
    private static final int CAMERA_PIC_REQUEST = 22;
    private final String TAG = AddNewGroupActivity.class.getSimpleName();
    private ArrayAdapter<String> participantAdapter;
    List<String> participantList = new ArrayList<>();
    ChatRoom chatRoom;
    String participantListStr = "";
    String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_group_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        participantAdapter = new ArrayAdapter<>(this, R.layout.item_participant,
                R.id.participant, participantList);
        ListView participantListView = (ListView)findViewById(R.id.participantList);
        participantListView.setAdapter(participantAdapter);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        contactImgView = (ImageView) findViewById(R.id.imgViewContactImage);
        contactImgView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"),
                        CAMERA_PIC_REQUEST);
            }

        });

        addParticipant = (Button) findViewById(R.id.addParticipants);
        addParticipant.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                goToSecondActivity();
            }

        });

        //deviceID = /*PreferenceManager.getDefaultSharedPreferences(this).getString("android_id", "noneExistedDevice")*/"1";
        deviceID = PreferenceManager.getDefaultSharedPreferences(this).getString(ANDROID_ID, null);

        /*
        createGroup = (Button) findViewById(R.id.createGroup);
        createGroup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                TextView groupName = (TextView)findViewById(R.id.groupNameText);
                String groupNameStr = groupName.getText().toString();

                if(groupNameStr.equals("")){
                    chatRoom = new ChatRoom("New Group", "abc", participantList);

                }else{
                    chatRoom = new ChatRoom(groupNameStr, "abc", participantList);
                }
                createGroup();
                MainActivity.aac.addChatRoom(chatRoom);
                MainActivity.aac.notifyDataSetChanged();
                goToCreateGroupActivity();
            }

        });
        */
    }

    /*public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_add_new_group_, container, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSecondActivity();
            }
        });
        return v;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_group, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_group) {
            TextView groupName = (TextView)findViewById(R.id.groupNameText);
            String groupNameStr = groupName.getText().toString();

            if(groupNameStr.equals("")){
                chatRoom = new ChatRoom("New Group", "abc", participantList);

            }else{
                chatRoom = new ChatRoom(groupNameStr, "abc", participantList);
            }
            createGroup();
            MainActivity.aac.addChatRoom(chatRoom);
            MainActivity.aac.notifyDataSetChanged();
            goToCreateGroupActivity();

            Toast.makeText(getApplicationContext(), "Adding Group", Toast.LENGTH_SHORT).show();
            return true;
        }
        /*
        else if (id == R.id.action_scan) {
            // Refer back to commit 9 for method to display output
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivity(intent);
            startActivityForResult(intent, 1);
            //IntentIntegrator integrator = new IntentIntegrator(this);

            integrator.addExtra("SCAN_WIDTH", 640);
            integrator.addExtra("SCAN_HEIGHT", 640);
            integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
            //customize the prompt message before scanning
            integrator.addExtra("PROMPT_MESSAGE", "Scanner Start!");
            integrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES);

            // integrator.initiateScan();
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    private void createGroup() {
        participantListStr = participantListStr + deviceID;
        String[] fetchInfo = {"createChat",chatRoom.getChatName(),chatRoom.getChatImage(),participantListStr};
        FetchUpdatesTask fetchUpdatesTask = new FetchUpdatesTask();
        fetchUpdatesTask.delegate = null;
        fetchUpdatesTask.execute(fetchInfo);
    }

    private void goToSecondActivity() {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
        startActivityForResult(intent, 1);
    }

    private void goToCreateGroupActivity() {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this,requestCode , Toast.LENGTH_LONG).show();
        /*if (requestCode ==1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("content");
                TextView formatTxt = (TextView) findViewById(R.id.qrID);
                formatTxt.setText("Content: " + result);
            }else{
                Toast.makeText(this,requestCode , Toast.LENGTH_LONG).show();
            }
        }*/

        //Toast.makeText(this,requestCode , Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case CAMERA_PIC_REQUEST:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        contactImgView.setImageBitmap(photo);
                    } catch (Exception e) {
                        Toast.makeText(this, "Couldn't load photo", Toast.LENGTH_LONG).show();
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    Log.i(TAG, "requestCode " + requestCode + " resultCode " + requestCode);
                    break;
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("content");
                    //TextView formatTxt = (TextView) findViewById(R.id.qrID);
                    //formatTxt.setText("Content: " + result);
                    participantList.add(result);
                    participantListStr = participantListStr + result + ",";
                    participantAdapter.notifyDataSetChanged();
                }
            default:
                Log.i(TAG, "requestCode " + requestCode);
                break;
        }
    }
}
