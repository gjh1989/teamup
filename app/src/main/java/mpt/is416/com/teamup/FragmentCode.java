package mpt.is416.com.teamup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

//import android.app.Fragment;

/**
 * Created by JunHong on 2/10/2015.
 */
public class FragmentCode extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_qrcode, container, false);
        v.findViewById(R.id.testButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "button click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), AddGroupMember.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
