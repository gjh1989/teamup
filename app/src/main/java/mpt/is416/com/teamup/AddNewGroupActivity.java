package mpt.is416.com.teamup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AddNewGroupActivity extends AppCompatActivity {
    ImageView contactImgView;
    Button addParticipant;
    private static final int CAMERA_PIC_REQUEST = 22;
    private final String TAG = AddNewGroupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_group_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);

            }

        });

        addParticipant = (Button) findViewById(R.id.addParticipants);
        addParticipant.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                goToSecondActivity();
            }

        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_add_new_group_, container, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSecondActivity();
            }
        });

        return v;
    }

    private void goToSecondActivity() {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("content");
                TextView formatTxt = (TextView) findViewById(R.id.textView);
                formatTxt.setText("Content: " + result);
            }
        }*/

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
            default:
                Log.i(TAG, "requestCode " + requestCode);
                break;
        }
    }

}
