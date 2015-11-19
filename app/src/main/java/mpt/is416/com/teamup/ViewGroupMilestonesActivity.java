package mpt.is416.com.teamup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Elyza on 7/10/2015.
 */
public class ViewGroupMilestonesActivity extends AppCompatActivity implements FetchUpdatesTask.AsyncResponse, DialogFragmentAddNewMilestone.DialogResponse {

    private final String TAG = ViewGroupMilestonesActivity.class.getSimpleName();
    private final String ANDROID_ID = "android_id";
    private final String CHAT_ID = "cid";
    private final String CHAT_NAME = "cname";
    private ArrayAdapterMilestone milestoneAdapter;
    private ExpandableListView listView;
    private List<String> headerData;
    private HashMap<String, List<Milestone>> data;
    private String rawJson;
    private String cid, cname;
    private Toolbar toolbar;
    private int mStackLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_milestones);
        toolbar = (Toolbar) findViewById(R.id.toolbar_milestones);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.milestones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_milestone:
                //Toast.makeText(getApplicationContext(), "Add Milestone", Toast.LENGTH_SHORT).show();
//                mStackLevel++;
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                Fragment prev = getSupportFragmentManager().findFragmentByTag("addMilestone");
//                if (prev != null) {
//                    ft.remove(prev);
//                }
//                ft.addToBackStack(null);
                DialogFragmentAddNewMilestone.newInstance(cid).show(getSupportFragmentManager(),
                        "addMilestone");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = this.getIntent().getExtras();
        cid = bundle.getString(CHAT_ID);
        cname = bundle.getString(CHAT_NAME);
        updateMilestones();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent returnIntent = new Intent(this, ChattingActivity.class);
        returnIntent.putExtra(CHAT_ID, cid);
        returnIntent.putExtra(CHAT_NAME, cname);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    // Methods to call from database
    private void updateMilestones() {
        String[] fetchInfo = {"getMilestoneByCid", cid};
        FetchUpdatesTask fetchUpdatesTask = new FetchUpdatesTask();
        fetchUpdatesTask.delegate = this;
        fetchUpdatesTask.execute(fetchInfo);
    }

    public void processFinish(String output) {
        rawJson = output;
        // Prepare the data and milestoneAdapter
        prepareMilestoneData();
        milestoneAdapter = new ArrayAdapterMilestone(this, headerData, data, R.id.week_milestone_list);
        listView = (ExpandableListView) findViewById(R.id.week_milestone_list);
        listView.setAdapter(milestoneAdapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
//                Toast.makeText(getApplicationContext(), headerData.get(groupPosition) + " : " +
//                                data.get(headerData.get(groupPosition)).get(childPosition).getTitle(),
//                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void processFinish(Milestone output) {
        Log.i(TAG, "processFinish(Milestone output)");
        // TODO: Call update instead
        int week = output.getWeek();
        if (week == 0) {
            if (headerData.contains("NOW")) {
                // add to the last...
                data.get("NOW").add(output);
            } else {
                headerData.add(0, "NOW");
                List<Milestone> milestoneList = new ArrayList<>();
                milestoneList.add(output);
                data.put("NOW", milestoneList);
            }
        } else {
            if (headerData.contains("WEEK " + week)) {
                // add to the last...
                data.get("WEEK " + week).add(output);
            } else {
                // find where to insert
                for (int i = 0; i < headerData.size(); i++) {
                    String currentWeekStr = headerData.get(i);
                    // escape parse exception for week == 0
                    if (currentWeekStr.equalsIgnoreCase("now"))
                        continue;
                    int currentWeek = Integer.parseInt(currentWeekStr.substring(currentWeekStr.lastIndexOf(" ") + 1));
                    if (week < currentWeek) {
                        headerData.add(i, "WEEK " + week);
                        break;
                    }
                }
                List<Milestone> milestoneList = new ArrayList<>();
                milestoneList.add(output);
                data.put("WEEK " + week, milestoneList);
            }
        }
        milestoneAdapter.notifyDataSetChanged();
    }

    private void prepareMilestoneData() {
        headerData = new ArrayList<>();
        data = new HashMap<>();

        try {
            // read JSON from assets folder
            //JSONObject json = new JSONObject(loadJSONfromAsset("samplemilestones.json"));
            JSONObject json = new JSONObject(rawJson);
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
                    String dateString = milestoneObj.getString("datetime");
                    if (dateString != null && !dateString.isEmpty() && !dateString.equalsIgnoreCase("null")) {
                        Date date = new Date();
                        date.setTime(Long.parseLong(dateString));
                        milestone.setDatetime(date);
                    }
                    milestone.setLocation(milestoneObj.getString("location"));
                    weeklyData.add(milestone);
                }
                if (week.getInt("week") != 0) {
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
