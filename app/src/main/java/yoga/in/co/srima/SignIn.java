package yoga.in.co.srima;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

import yoga.in.co.srima.R;

/**
 * Created by ADMIN on 08-Aug-17.
 */

public class SignIn extends Fragment {
    View view;
    Button btnSubmit;
    String UserName, UserPassword;
    EditText edtuser, edtpass;
    String responce_key, user_id, message;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    TextView txtpassforgot;
    Button rl_close, rl_submit;
    EditText edt_forget_email;
    String Forgot_Email = "";
    private static final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog1, mDialog2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_login, container, false);
        btnSubmit = (Button) view.findViewById(R.id.btnsubmit);
        edtuser = (EditText) view.findViewById(R.id.edtuser);
        edtpass = (EditText) view.findViewById(R.id.edtpass);
        txtpassforgot = (TextView) view.findViewById(R.id.txtpassforgot);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        initpDialog();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserName = edtuser.getText().toString();
                UserPassword = edtpass.getText().toString();
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    if (UserName.equalsIgnoreCase("")) {
                        edtuser.requestFocus();
                        edtuser.setText("");
                        edtuser.setError("Please fill UserName");
                    } else {
                        if (UserPassword.equalsIgnoreCase("")) {
                            edtpass.requestFocus();
                            edtpass.setText("");
                            edtpass.setError("Please fill password");
                        } else {
                            if (UserPassword.length() < 6) {
                                edtpass.requestFocus();
                                edtpass.setText("");
                                edtpass.setError("minimum 6 characters");
                            } else {
                                initpDialog();
                                new LoginProgresh().execute();

                            }
                        }
                    }

                }

            }
        });
        txtpassforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.forgotpassword, null);
                rl_close = (Button) convertView.findViewById(R.id.btn_cancel);
                rl_submit = (Button) convertView.findViewById(R.id.btn_confirm);
                edt_forget_email = (EditText) convertView.findViewById(R.id.edt_forgot_email);
                alertDialog.setView(convertView);
                rl_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog1.cancel();
                    }
                });
                rl_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Forgot_Email = edt_forget_email.getText().toString();
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                            Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                        } else {
                            if (Forgot_Email.equalsIgnoreCase("")) {
                                edt_forget_email.requestFocus();
                                edt_forget_email.setText("");
                                edt_forget_email.setError("Please fill email");
                            } else {
                                if (!Forgot_Email.matches(emailPattern)) {
                                    edt_forget_email.requestFocus();
                                    edt_forget_email.setText("");
                                    edt_forget_email.setError("Invalid email id.");
                                } else {
                                    mDialog1.cancel();
                                    new ForgotPassword().execute();
                                }
                            }
                        }
                    }
                });
                edt_forget_email.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        // put the code of save Database here
                        edt_forget_email.setError(null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });
                mDialog1 = alertDialog.show();
            }
        });
        edtuser.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtuser.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        edtpass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtpass.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        return view;
    }

    public class LoginProgresh extends AsyncTask<String, String, String> {
//        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber      checkuserlogin=1,user_login=emailid,user_pass=password
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/login?loginuser=1&uname=" + URLEncoder.encode(UserName) + "&pwd=" + URLEncoder.encode(UserPassword));
            try {
                Iterator<String> iter = json.keys();
                while (iter.hasNext()) {
                    responce_key = iter.next();
                }
                if (responce_key.equalsIgnoreCase("responce")) {
                    JSONArray successArray = json.getJSONArray("responce");

                    int arrayLength = successArray.length();

                    if (arrayLength > 0) {

                        for (int i = 0; i < successArray.length(); i++) {

                            JSONObject registerObj = (JSONObject) successArray.get(i);
                            if (registerObj.has("uid")) {
                                user_id = registerObj.getString("uid");
                            }
                        }
                    }
                } else if (responce_key.equalsIgnoreCase("error")) {
                    JSONArray successArray = json.getJSONArray("error");

                    int arrayLength = successArray.length();

                    if (arrayLength > 0) {

                        for (int i = 0; i < successArray.length(); i++) {
                            message = successArray.get(i).toString();
                            user_id = "0";
                        }
                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hidepDialog();
            if (user_id.equalsIgnoreCase("0")) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage(message);
                alertDialog.setIcon(R.mipmap.launchicon);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
            } else {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("user_id", Integer.parseInt(user_id));
                editor.putString("page", "Home");
                editor.commit();
                Intent in = new Intent(getActivity(), MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
            }
        }

    }

    public class ForgotPassword extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber      checkuserlogin=1,user_login=emailid,user_pass=password
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/login?forgotpwd=1&forgotemail=" + Forgot_Email);
            try {
                Iterator<String> iter = json.keys();
                while (iter.hasNext()) {
                    responce_key = iter.next();
                }
                if (responce_key.equalsIgnoreCase("responce")) {
                    JSONArray successArray = json.getJSONArray("responce");

                    int arrayLength = successArray.length();

                    if (arrayLength > 0) {

                        for (int i = 0; i < successArray.length(); i++) {

                            message = successArray.get(i).toString();

                        }
                    }
                } else if (responce_key.equalsIgnoreCase("error")) {
                    JSONArray successArray = json.getJSONArray("error");

                    int arrayLength = successArray.length();

                    if (arrayLength > 0) {

                        for (int i = 0; i < successArray.length(); i++) {
                            message = successArray.get(i).toString();
                        }
                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdia.dismiss();
            pdia = null;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.invalid_credential, null);
            rl_close = (Button) convertView.findViewById(R.id.btn_close);
            TextView txtmessage = (TextView) convertView.findViewById(R.id.txtmessage);
            txtmessage.setText(message);
            alertDialog.setView(convertView);
            rl_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog1.cancel();
                }
            });
            mDialog1 = alertDialog.show();
        }
    }

    protected void initpDialog() {
        alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.progress, null);
        alertDialog.setView(convertView);
    }

    protected void showpDialog() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        Double screenInches = Math.sqrt(x + y);

        Integer inch = screenInches.intValue();
        if (inch >= 5) {
            if (tabletSize) {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(220, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            } else {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(550, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            }
        } else {
            mDialog2 = alertDialog.show();
            mDialog2.getWindow().setLayout(300, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mDialog2.setCancelable(false);
        }
    }

    protected void hidepDialog() {
        mDialog2.cancel();
    }
}
