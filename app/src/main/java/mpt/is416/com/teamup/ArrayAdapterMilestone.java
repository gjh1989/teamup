package mpt.is416.com.teamup;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Elyza on 11/10/2015.
 */
public class ArrayAdapterMilestone extends BaseExpandableListAdapter {
    Context context;
    List<String> headerData;
    HashMap<String, List<Milestone>> data;
    ExpandableListView listView;

    public ArrayAdapterMilestone(Context context, List<String> headerData, HashMap<String,
            List<Milestone>> data) {
        this.context = context;
        this.headerData = headerData;
        this.data = data;
        listView = (ExpandableListView) ((Activity) context).findViewById(R.id.week_milestone_list);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.data.get(this.headerData.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {

        Milestone milestone = (Milestone) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_milestone, parent, false);
        }

        RelativeLayout idRL = (RelativeLayout) convertView.findViewById(R.id.milestone_id);
        TextView titleTV = (TextView) convertView.findViewById(R.id.milestone_title);
        TextView descriptionTV = (TextView) convertView.findViewById(R.id.milestone_description);
        TextView datetimeTV = (TextView) convertView.findViewById(R.id.milestone_date);
        TextView locationTV = (TextView) convertView.findViewById(R.id.milestone_location);
        idRL.setContentDescription(Integer.toString(milestone.getMilestoneId()));
        titleTV.setText(milestone.getTitle());
        descriptionTV.setText(milestone.getDescription());
        datetimeTV.setText(milestone.getDatetime());
        locationTV.setText(milestone.getLocation());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.data.get(this.headerData.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headerData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.headerData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        String header = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_milestone_list, parent, false);
        }
        TextView headerTV = (TextView) convertView.findViewById(R.id.milestone_week);
        headerTV.setText(header);
        listView.expandGroup(groupPosition); // ensures that groups are always expanded
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
