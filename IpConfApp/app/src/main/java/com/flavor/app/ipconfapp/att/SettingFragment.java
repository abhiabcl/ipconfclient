package com.flavor.app.ipconfapp.att;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment {
    String TAG = "IPCONF";
    static String MY_PREFS_NAME = "IPCONF_PREFS_DATA";

    public SettingFragment() {}

    private Button saveBtn;
    private EditText serverurlEt;

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        serverurlEt = (EditText) view.findViewById(R.id.txtserferurl);
        saveBtn = (Button) view.findViewById(R.id.btnSave);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if (prefs != null) {
            String server_url = prefs.getString("SERVER_URL", "");//"No name defined" is the default value.
            serverurlEt.setText(server_url);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "save btn click!");
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("SERVER_URL", serverurlEt.getText().toString());
                editor.apply();
            }
        });


        return view;
    }
}
