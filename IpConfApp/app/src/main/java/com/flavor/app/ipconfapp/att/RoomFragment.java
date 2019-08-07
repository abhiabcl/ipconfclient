package com.flavor.app.ipconfapp.att;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;
import static com.flavor.app.ipconfapp.att.SettingFragment.MY_PREFS_NAME;

public class RoomFragment extends Fragment {

    String TAG = "IPCONF";
    private Button createConfBtn;
    private EditText bridgePhoneEt;
    private EditText hostCodeEt;
    private EditText partCodeEt;
    private Button terminConfBtn;
    private EditText confidEt;

    public RoomFragment() {
    }

    public static RoomFragment newInstance(String param1, String param2) {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        bridgePhoneEt = (EditText) view.findViewById(R.id.txtbridgePhone);
        hostCodeEt = (EditText) view.findViewById(R.id.txthostCode);
        partCodeEt = (EditText) view.findViewById(R.id.txtpartCode);

        confidEt = (EditText) view.findViewById(R.id.txtConfId);
        terminConfBtn = (Button) view.findViewById(R.id.btnTerminConf);

        createConfBtn = (Button) view.findViewById(R.id.btnCreateConf);
        createConfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "create conf click.");
                new HTTPAsyncTask().execute("create");
            }
        });

        terminConfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new HTTPAsyncTask().execute("terminate");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        return view;
    }

    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // params comes from the execute() call: params[0] is the url.
            // do post request with
            String resposne = null;
            try {
                Log.i(TAG, "Response from URL: " + RestAPIHandler.getCreateConfURL());

                if ( params[0].contains("create"))
                    resposne = RestAPIHandler.doHttpPost(RestAPIHandler.getCreateConfURL(), RestAPIHandler.createConfRequest("", "", ""));
                else {
                    String confid = confidEt.getText().toString();
                    resposne = RestAPIHandler.doHttpGet(RestAPIHandler.terminConfRequest(confid));
                }
                Log.i(TAG, "Response from createConf: " + resposne);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return  resposne;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            String roomUrl= null;
            if (!result.contains("error")) {
                RestAPIHandler.setConfId(RestAPIHandler.getMsg(result));
                confidEt.setText(RestAPIHandler.getMsg(result));
            } else {
                Toast.makeText(getContext(), "Error" , Toast.LENGTH_SHORT);
            }
        }
    }
}
