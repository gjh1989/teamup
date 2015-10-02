package mpt.is416.com.teamup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import android.app.Fragment;

/**
 * Created by JunHong on 2/10/2015.
 */
public class FragmentCode extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_qrcode, container, false);
        return v;
    }
}
