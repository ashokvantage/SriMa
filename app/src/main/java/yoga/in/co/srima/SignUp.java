package yoga.in.co.srima;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

/**
 * Created by ADMIN on 08-Aug-17.
 */

public class SignUp extends Fragment implements Constant {
    View view;
    TextView txtlogin;
    Fragment fragment;
    FragmentTransaction ft;
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Button btnRegister;
    String Email, Password, Username;
    EditText edtuser, edtemail, edtpass;
    private final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String responce_key, user_id, message;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_sign_up, container, false);
        MainActivity.mSelectedItem = 8;
        MainActivity.mMenuAdapter.notifyDataSetChanged();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString("page", "SignUP");
        editor.commit();
        initpDialog();
        txtlogin = (TextView) view.findViewById(R.id.txtlogin);
        btnRegister = (Button) view.findViewById(R.id.btnsubmit);
        edtuser = (EditText) view.findViewById(R.id.edtname);
        edtemail = (EditText) view.findViewById(R.id.edtemail);
        edtpass = (EditText) view.findViewById(R.id.edtpass);
        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new SignIn();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + MainActivity.add);
                ft.commit();
                MainActivity.add++;
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Username = edtuser.getText().toString();
                Email = edtemail.getText().toString();
                Password = edtpass.getText().toString();
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    if (Username.equalsIgnoreCase("")) {
                        edtuser.requestFocus();
                        edtuser.setText("");
                        edtuser.setError("Please fill UserName");
                    } else {
                        if (Email.equalsIgnoreCase("")) {
                            edtemail.requestFocus();
                            edtemail.setText("");
                            edtemail.setError("Please fill email");
                        } else {
                            if (!Email.matches(emailPattern)) {
                                edtemail.requestFocus();
                                edtemail.setText("");
                                edtemail.setError("Invalid email id.");
                            } else {
                                if (Password.equalsIgnoreCase("")) {
                                    edtpass.requestFocus();
                                    edtpass.setText("");
                                    edtpass.setError("Please fill password");
                                } else {
                                    if (Password.length() < 6) {
                                        edtpass.requestFocus();
                                        edtpass.setText("");
                                        edtpass.setError("minimum 6 characters");
                                    } else {
                                        initpDialog();
                                        new RegisterProgresh().execute();

                                    }
                                }
                            }
                        }
                    }

                }
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
        edtemail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtemail.setError(null);
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
    public class RegisterProgresh extends AsyncTask<String, String, String> {
//        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
              showpDialog();
        }


        @Override
        public String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber      checkuserlogin=1,user_login=emailid,user_pass=password
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/users?addnewuser=1&username=" + URLEncoder.encode(Username) + "&email=" + URLEncoder.encode(Email) + "&password=" + URLEncoder.encode(Password));
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
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        Double screenInches = Math.sqrt(x+y);

        Integer inch = screenInches.intValue();
        if(inch >= 5)
        {
            if (tabletSize) {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(220, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            } else {
                mDialog2 = alertDialog.show();
                mDialog2.getWindow().setLayout(550, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog2.setCancelable(false);
            }
        }
        else
        {
            mDialog2 = alertDialog.show();
            mDialog2.getWindow().setLayout(300, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mDialog2.setCancelable(false);
        }
    }

    protected void hidepDialog() {
        mDialog2.cancel();
    }
}
