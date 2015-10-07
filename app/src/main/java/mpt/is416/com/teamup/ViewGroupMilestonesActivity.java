package mpt.is416.com.teamup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Elyza on 7/10/2015.
 */
public class ViewGroupMilestonesActivity extends AppCompatActivity {

    private ArrayAdapter<String> gMilestoneAdapter;
    private final String TAG = ViewGroupMilestonesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_milestones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create some dummy data to simulate loading of milestones
        String[] data = {
                "Task 1", "Task 2", "Task 3", "Task 4", "Task 5", "Task 6", "Task 7", "Task 8",
                "Task 9", "Task 10", "Task 11", "Task 12"
        };

        List<String> groupMilestones = new ArrayList<>(Arrays.asList(data));

        //
        gMilestoneAdapter = new ArrayAdapter<>(this, R.layout.item_milestone,
                R.id.milestone_title, groupMilestones);
        ListView listView = (ListView) findViewById(R.id.milestone_list);
        listView.setAdapter(gMilestoneAdapter);

        Log.i(TAG, "onCreate executed");
    }

}
