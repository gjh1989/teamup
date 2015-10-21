package mpt.is416.com.teamup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Elyza on 7/10/2015.
 */
public class ViewGroupMilestonesActivity extends AppCompatActivity implements FetchUpdatesTask.AsyncResponse {

    private final String TAG = ViewGroupMilestonesActivity.class.getSimpleName();
    private final String ANDROID_ID = "android_id";
    private ArrayAdapterMilestone milestoneAdapter;
    private ExpandableListView listView;
    private List<String> headerData;
    private HashMap<String, List<Milestone>> data;
    private String rawJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_milestones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_milestones);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.milestones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_milestone) {
            Toast.makeText(getApplicationContext(), "Add Milestone", Toast.LENGTH_SHORT).show();
            DialogFragment fragment = new DialogFragmentAddNewMilestone();
            fragment.show(getSupportFragmentManager(), "addMilestone");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "enter onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        // Stuff to do, dependent on requestCode and resultCode
        if (requestCode == 1)  // 1 is an arbitrary number, can be any int
        {
            if (resultCode == RESULT_OK) {
                // Now do what you need to do after the dialog dismisses.
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMilestones();
    }

    // Methods to call from database
    private void updateMilestones() {
        String[] fetchInfo = {"getMilestoneByUid", /*PreferenceManager
                .getDefaultSharedPreferences(this).getString(ANDROID_ID, null)*/"1"};
        FetchUpdatesTask fetchUpdatesTask = new FetchUpdatesTask();
        fetchUpdatesTask.delegate = this;
        fetchUpdatesTask.execute(fetchInfo);
    }

    public void processFinish(String output) {
        rawJson = output;
        // Prepare the data and milestoneAdapter
        prepareMilestoneData();
        milestoneAdapter = new ArrayAdapterMilestone(this, headerData, data);
        listView = (ExpandableListView) findViewById(R.id.week_milestone_list);
        listView.setAdapter(milestoneAdapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                Toast.makeText(getApplicationContext(), headerData.get(groupPosition) + " : " +
                                data.get(headerData.get(groupPosition)).get(childPosition).getTitle(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void prepareMilestoneData() {
        headerData = new ArrayList<>();
        data = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            // read JSON from assets folder
            //JSONObject json = new JSONObject(loadJSONfromAsset("samplemilestones.json"));
            JSONObject json = new JSONObject(rawJson);
            JSONObject list = json.getJSONObject("list");
            for (int i = 0; i <= 16; i++) {
                List<Milestone> weeklyData = new ArrayList<>();
                if (list.has(Integer.toString(i))) {
                    JSONArray milestonesArray = list.getJSONArray(Integer.toString(i));
                    for (int j = 0; j < milestonesArray.length(); j++) {
                        JSONObject milestoneObj = milestonesArray.getJSONObject(j);
                        Milestone milestone = new Milestone();
                        milestone.setWeek(i);
                        milestone.setMilestoneId(milestoneObj.getInt("msid"));
                        milestone.setTitle(milestoneObj.getString("title"));
                        milestone.setDescription(milestoneObj.getString("desc"));
                        milestone.setDatetime(sdf.parse(milestoneObj.getString("datetime")));
                        milestone.setLocation(milestoneObj.getString("location"));
                        weeklyData.add(milestone);
                    }
                }
                if (!weeklyData.isEmpty()) {
                    if (i == 0) {
                        headerData.add("NOW");
                    } else {
                        headerData.add("WEEK " + Integer.toString(i));
                    }
                    data.put(headerData.get(headerData.size() - 1), weeklyData);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    /*private String loadJSONfromAsset(String fileName) {
        String json = null;
        try {
            InputStream inputStream = this.getAssets().open(fileName);
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
    }*/
}
