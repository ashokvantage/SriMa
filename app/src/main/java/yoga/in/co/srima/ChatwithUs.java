package yoga.in.co.srima;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import yoga.in.co.srima.R;

/**
 * Created by ADMIN on 08-Aug-17.
 */

public class ChatwithUs extends Fragment implements View.OnClickListener {
    View view;
    RelativeLayout rlbtnsend, rlbtnsend2, footer;
    EditText edtinput;
    ImageView imgfacebook, imgtwitter, imginstagram;
    TextView txtsignup;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    int user_id;
    String Number = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chat, container, false);
        rlbtnsend = (RelativeLayout) view.findViewById(R.id.rlbtnsend);
        rlbtnsend2 = (RelativeLayout) view.findViewById(R.id.rlbtnsend2);
        footer = (RelativeLayout) view.findViewById(R.id.footer);
        edtinput = (EditText) view.findViewById(R.id.edtinput);
        txtsignup = (TextView) view.findViewById(R.id.txtsignup);
        imgfacebook = (ImageView) view.findViewById(R.id.imgfacebook);
        imgtwitter = (ImageView) view.findViewById(R.id.imgtwitter);
        imginstagram = (ImageView) view.findViewById(R.id.imginstagram);
        txtsignup.setOnClickListener(this);
        imgfacebook.setOnClickListener(this);
        imgtwitter.setOnClickListener(this);
        imginstagram.setOnClickListener(this);
        footer = (RelativeLayout) view.findViewById(R.id.footer);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences.getInt("user_id", 0);
        if (user_id != 0) {
            footer.setVisibility(View.GONE);
        } else {
            footer.setVisibility(View.VISIBLE);
        }
        editor = sharedpreferences.edit();
        editor.putString("page", "Chat");
        editor.commit();
        edtinput.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtinput.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        rlbtnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Number = edtinput.getText().toString();
                Number = "Hi";
                boolean installed = appInstalledOrNot("com.whatsapp");
                if (installed) {
//                    System.out.println("App is already installed on your phone");
                    if (Number.equalsIgnoreCase("")) {
                        edtinput.requestFocus();
                        edtinput.setText("");
                        edtinput.setError("Please fill Phone Number");
                    } else {
                        try {
                            Uri uri = Uri.parse("smsto:" + "9815420173");
                            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                            i.setPackage("com.whatsapp");
                            startActivity(Intent.createChooser(i, edtinput.getText().toString()));
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(),"Please install whatsapp to chat",Toast.LENGTH_LONG).show();
                }


            }
        });
        rlbtnsend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    String whatsAppMessage = "ashok";
//
//                    Intent sendIntent = new Intent();
//                    sendIntent.setAction(Intent.ACTION_SEND);
//                    sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);
//                    sendIntent.setType("text/plain");
//
//                    // Do not forget to add this to open whatsApp App specifically
//                    sendIntent.setPackage("com.whatsapp");
//                    startActivity(sendIntent);
//                } catch (Exception e) {
//                    Toast.makeText(getActivity(), "Please install whatsapp to chat", Toast.LENGTH_SHORT)
//                            .show();
//                }
            }
        });
        return view;
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        FragmentTransaction ft;
        switch (v.getId()) {
            case R.id.txtsignup:
                fragment = new SignUp();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.commit();
                break;
            case R.id.imgfacebook:
                Intent in = new Intent(getActivity(), SocialPage.class);
                startActivity(in);
                break;
            case R.id.imgtwitter:
                break;
            case R.id.imginstagram:
                break;
        }
    }

}
