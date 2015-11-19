package mpt.is416.com.teamup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddNewGroupActivity extends AppCompatActivity {
    private final String ANDROID_ID = "android_id";
    ImageView contactImgView;
    Button addParticipant;
    EditText datePickerET;
    private static final int CAMERA_PIC_REQUEST = 2;
    private final String TAG = AddNewGroupActivity.class.getSimpleName();
    private ArrayAdapter<String> participantAdapter;
    List<String> participantList = new ArrayList<>();
    ChatRoom chatRoom;
    String participantListStr = "";
    String deviceID;
    String itemTitle;
    Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_group_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        participantAdapter = new ArrayAdapter<>(this, R.layout.item_participant,
                R.id.participant, participantList);
        ListView participantListView = (ListView) findViewById(R.id.participantList);
        participantListView.setAdapter(participantAdapter);

        contactImgView = (ImageView) findViewById(R.id.imgViewContactImage);
        contactImgView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToListOfImages();
            }

        });

        addParticipant = (Button) findViewById(R.id.addParticipants);
        addParticipant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToSecondActivity();
            }

        });

        datePickerET = (EditText) findViewById(R.id.selfDestructionInput);
        datePickerET.setInputType(InputType.TYPE_NULL);
        datePickerET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the current date as the default date in the picker
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewGroupActivity.this, new
                        DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                String dd, mm;
                                if (day < 10)
                                    dd = "0" + day;
                                else
                                    dd = Integer.toString(day);
                                if (month + 1 < 10)
                                    mm = "0" + month + 1;
                                else
                                    mm = Integer.toString(month + 1);
                                datePickerET.setText(new StringBuilder().append(dd).append("/")
                                        .append(mm).append("/").append(year));
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        //Toast.makeText(getApplicationContext(), selfDestructDate, Toast.LENGTH_SHORT).show();
        deviceID = PreferenceManager.getDefaultSharedPreferences(this).getString(ANDROID_ID, null);
    }

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

            if(itemTitle==null){
                Toast.makeText(getApplicationContext(), "Please Select an Image", Toast.LENGTH_SHORT).show();
                Log.i(TAG, itemTitle+1);
            } else if (datePickerET.getText().toString().length() <= 0) {
                Toast.makeText(getApplicationContext(), "Please set a date for self destruction", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, itemTitle + 2+datePickerET.getText().toString());
                if(groupNameStr.equals("")){
                    chatRoom = new ChatRoom("New Group", itemTitle, participantList,datePickerET.getText().toString());
                }else{
                    chatRoom = new ChatRoom(groupNameStr, itemTitle, participantList,datePickerET.getText().toString());
                }
                createGroup();
                MainActivity.aac.addChatRoom(chatRoom);
                MainActivity.aac.notifyDataSetChanged();
                goToCreateGroupActivity();
            }
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
        String[] fetchInfo = {"createChat",chatRoom.getChatName(),chatRoom.getChatImage(),participantListStr,chatRoom.getDate()};
        FetchUpdatesTask fetchUpdatesTask = new FetchUpdatesTask();
        fetchUpdatesTask.delegate = null;
        fetchUpdatesTask.execute(fetchInfo);
    }
    private void goToListOfImages() {
        Intent intent = new Intent(this, GridViewActivity.class);
        startActivity(intent);
        startActivityForResult(intent, 2);
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

    public Bitmap getImageBitmap() {
        return selectedImageBitmap;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("content");
                    //TextView formatTxt = (TextView) findViewById(R.id.qrID);
                    //formatTxt.setText("Content: " + result);
                    participantList.add(result);
                    participantListStr = participantListStr + result + ",";
                    participantAdapter.notifyDataSetChanged();
                }
                break;
            case 2:
                // When an Image is picked
                if  (resultCode == Activity.RESULT_OK) {
                    try{
                        /*Uri selectedImage = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap bitMapImage = BitmapFactory.decodeStream(imageStream);*/

                        //Bitmap bitMapImage = gridViewActivity.getSelectedBitMapImage();
                        //gridViewActivity = new GridViewActivity();
                        //selectedImageFromPreviousActivity = gridViewActivity.getSelectedImageItem();
                        itemTitle=data.getStringExtra("imageTitle").substring(13);

                        //gridViewActivity = new GridViewActivity();
                        //selectedImageFromPreviousActivity = gridViewActivity.getSpecificImage(itemTitle);
                        selectedImageBitmap = data.getParcelableExtra("result");
                        ImageView myImage = (ImageView) findViewById(R.id.imgViewContactImage);
                        myImage.setImageBitmap(selectedImageBitmap);

                        //Toast.makeText(this,  itemTitle, Toast.LENGTH_LONG).show();
                        //ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        //selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        //bArray = bos.toByteArray();
                    }catch (Exception e){
                        //Toast.makeText(this, e.toString(),Toast.LENGTH_LONG).show();
                        //Log.i(TAG, image.getTitle());
                        e.printStackTrace();
                    }
                } else {
                    //Toast.makeText(this, "You haven't picked Image"+resultCode,Toast.LENGTH_LONG).show();
                    Log.i(TAG, "You haven't picked Image" + resultCode);
                }
                break;
            default:
                Log.i(TAG, "requestCode " + requestCode);
                // Log.i(TAG, selectedImageFromPreviousActivity.getTitle());
                break;
        }
    }
}
