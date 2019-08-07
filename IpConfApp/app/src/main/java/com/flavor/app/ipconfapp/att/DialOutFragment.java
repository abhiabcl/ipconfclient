package com.flavor.app.ipconfapp.att;

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

import static com.flavor.app.ipconfapp.att.RestAPIHandler.TAG;

public class DialOutFragment extends Fragment {

    private Button dialoutBtn;
    private EditText phoneEt;

    private Button btnshare;
    private EditText mailEt;

    private TextView urltoshareTxt;

    public DialOutFragment() {
    }

    public static DialOutFragment newInstance(String param1, String param2) {
        DialOutFragment fragment = new DialOutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialout, container, false);

        phoneEt = (EditText) view.findViewById(R.id.txtphone);
        dialoutBtn = (Button) view.findViewById(R.id.btnDialOut);

        mailEt = (EditText) view.findViewById(R.id.txtmail);
        btnshare = (Button) view.findViewById(R.id.btnshare);
        urltoshareTxt = (TextView) view.findViewById(R.id.urltoshareTxt);

        dialoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (phoneEt.getText().toString() != null && !phoneEt.getText().toString().isEmpty()){
                        new HTTPAsyncTask().execute(phoneEt.getText().toString() );
                        Toast.makeText(getContext(),phoneEt.getText().toString() + " Dialout."  ,Toast.LENGTH_SHORT);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mailEt.getText().toString() != null && !mailEt.getText().toString().isEmpty()){

                    }
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
                resposne = RestAPIHandler.doHttpGet(RestAPIHandler.dialOutRequest(RestAPIHandler.getConfId(), params[0]));
                Log.i(TAG, "Response from dialout: " + resposne);

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
                urltoshareTxt.setText(RestAPIHandler.dialOutRequest(RestAPIHandler.getConfId(), phoneEt.getText().toString()));
            } else {
                Toast.makeText(getContext(), "Error" , Toast.LENGTH_SHORT);
            }
        }
    }

}
