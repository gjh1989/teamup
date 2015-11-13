package mpt.is416.com.teamup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Elyza on 13/11/2015.
 */
public class FragmentDeadlines extends Fragment implements FetchUpdatesTask.AsyncResponse {

    private final String TAG = FragmentDeadlines.class.getSimpleName();
    private final String ANDROID_ID = "android_id";
    private ArrayAdapterMilestone milestoneAdapter;
    private ExpandableListView listView;
    private List<String> headerData;
    private HashMap<String, List<Milestone>> data;
    private String rawJson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to hide menu events.
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deadlines, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMilestones();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMilestones();
    }

    // Methods to call from database
    private void updateMilestones() {
        String[] fetchInfo = {"getMilestoneByUid", /*PreferenceManager
                .getDefaultSharedPreferences(getActivity()).getString(ANDROID_ID, null)*/"3"};
        FetchUpdatesTask fetchUpdatesTask = new FetchUpdatesTask();
        fetchUpdatesTask.delegate = this;
        fetchUpdatesTask.execute(fetchInfo);
    }

    public void processFinish(String output) {
        rawJson = output;
        // Prepare the data and milestoneAdapter
        prepareMilestoneData();
        milestoneAdapter = new ArrayAdapterMilestone(getActivity(), headerData, data, R.id.user_milestone_list);
        listView = (ExpandableListView) getActivity().findViewById(R.id.user_milestone_list);
        listView.setAdapter(milestoneAdapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                Toast.makeText(getActivity().getApplicationContext(), headerData.get(groupPosition)
                        + " : " + data.get(headerData.get(groupPosition)).get(childPosition)
                        .getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void prepareMilestoneData() {
        headerData = new ArrayList<>();
        data = new HashMap<>();

        try {
            JSONObject json = new JSONObject(rawJson);
            JSONArray list = json.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject week = list.getJSONObject(i);
                JSONArray milestonesArray = week.getJSONArray("milestones");
                List<Milestone> weeklyData = new ArrayList<>();
                for (int j = 0; j < milestonesArray.length(); j++) {
                    JSONObject milestoneObj = milestonesArray.getJSONObject(j);
                    Milestone milestone = new Milestone();
                    milestone.setChatId(milestoneObj.getInt("cid"));
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
}
