package mpt.is416.com.teamup;

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Elyza on 7/10/2015.
 */
public class ViewGroupMilestonesActivity extends AppCompatActivity {

    private final String TAG = ViewGroupMilestonesActivity.class.getSimpleName();
    private ArrayAdapterMilestone milestoneAdapter;
    private ExpandableListView listView;
    private List<String> headerData;
    private HashMap<String, List<Milestone>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_milestones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    // TODO: Update with common method to call from database
    private void prepareMilestoneData() {
        headerData = new ArrayList<>();
        data = new HashMap<>();

        try {
            // read JSON from assets folder
            JSONObject json = new JSONObject(loadJSONfromAsset("samplemilestones.json"));
            JSONArray list = json.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject week = list.getJSONObject(i);
                JSONArray milestonesArray = week.getJSONArray("milestones");
                List<Milestone> weeklyData = new ArrayList<>();
                for (int j = 0; j < milestonesArray.length(); j++) {
                    JSONObject milestoneObj = milestonesArray.getJSONObject(j);
                    Milestone milestone = new Milestone();
                    milestone.setWeek(week.getInt("week"));
                    milestone.setMilestoneId(milestoneObj.getInt("msid"));
                    milestone.setTitle(milestoneObj.getString("title"));
                    milestone.setDescription(milestoneObj.getString("description"));
                    milestone.setDatetime(milestoneObj.getString("datetime"));
                    milestone.setLocation(milestoneObj.getString("location"));
                    weeklyData.add(milestone);
                }
                if (Integer.toString(week.getInt("week")) != null) {
                    headerData.add("WEEK " + Integer.toString(week.getInt("week")));
                } else {
                    headerData.add("NOW");
                }
                data.put(headerData.get(i), weeklyData);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private String loadJSONfromAsset(String fileName) {
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
    }
}
