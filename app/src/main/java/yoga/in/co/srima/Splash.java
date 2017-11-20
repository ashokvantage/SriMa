package yoga.in.co.srima;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {
    public static String device_id;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    int LoginId;
    Fragment fragment;
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Internet Connection Error");
            alertDialog.setMessage("Please connect to working Internet connection");
            alertDialog.setIcon(R.mipmap.launchicon);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Splash.this.finish();
                }
            });
            alertDialog.show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent in = new Intent(Splash.this, MainActivity.class);
                    startActivity(in);
                Splash.this.finish();
            }
        }, 2000);
    }
}
