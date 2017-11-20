package yoga.in.co.srima;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 08-Aug-17.
 */

public class TeacherDetails extends Fragment implements View.OnClickListener {
    View view;
    String Id, Name = "", Email = "", Country = "", Phone = "", Detail = "", Image;
    TextView txtname, txtcontact, txtemail, txtdescription;
    TextView tvname, tvcontact, tvemail, tvdescription;
    Bitmap myBitmap;
    ImageView imgteacher;
    ImageView imgfacebook, imgtwitter, imginstagram;
    TextView txtsignup;
    RelativeLayout footer;
    int user_id;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    RelativeLayout btnback,rlfooter;
    ScrollView scv;
    private ProgressDialog pDialog;
    JSONArray productslist = null;
    int p_length;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog;
    TeacherProgress teacherProgress = null;
//WebView imgweb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.teacherdetails, container, false);
        txtname = (TextView) view.findViewById(R.id.txtname);
        txtcontact = (TextView) view.findViewById(R.id.txtcontact);
        txtemail = (TextView) view.findViewById(R.id.txtemail);
        txtdescription = (TextView) view.findViewById(R.id.txtdescription);
        tvname = (TextView) view.findViewById(R.id.tvname);
        tvcontact = (TextView) view.findViewById(R.id.tvcontact);
        tvemail = (TextView) view.findViewById(R.id.tvemail);
        tvdescription = (TextView) view.findViewById(R.id.tvdescription);
        imgteacher = (ImageView) view.findViewById(R.id.imgteacher);
        txtsignup = (TextView) view.findViewById(R.id.txtsignup);
        imgfacebook = (ImageView) view.findViewById(R.id.imgfacebook);
        imgtwitter = (ImageView) view.findViewById(R.id.imgtwitter);
        imginstagram = (ImageView) view.findViewById(R.id.imginstagram);
        btnback = (RelativeLayout) view.findViewById(R.id.btnback);
        rlfooter= (RelativeLayout) view.findViewById(R.id.rlfooter);
        scv = (ScrollView) view.findViewById(R.id.scv);
//        imgweb=(WebView)view.findViewById(R.id.imgweb);
        scv.setVisibility(View.GONE);
        txtsignup.setOnClickListener(this);
        imgfacebook.setOnClickListener(this);
        imgtwitter.setOnClickListener(this);
        imginstagram.setOnClickListener(this);
        btnback.setOnClickListener(this);
        rlfooter.setOnClickListener(this);
        Bundle b = this.getArguments();
        txtname.setText(b.getString("name"));
        Id = b.getString("id");
        footer = (RelativeLayout) view.findViewById(R.id.footer);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences.getInt("user_id", 0);
        if (user_id != 0) {
            footer.setVisibility(View.GONE);
        } else {
            footer.setVisibility(View.VISIBLE);
        }

//        txtemail.setClickable(true);
        txtemail.setPaintFlags(txtemail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        txtemail.setMovementMethod(LinkMovementMethod.getInstance());
//        String text = "<a href='http://www.google.com'> Google </a>";
//        txtemail.setText(Html.fromHtml(text));
        txtemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Intent> targetedShareIntents = new ArrayList<Intent>();
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(shareIntent, 0);
                if (!resInfo.isEmpty()) {
                    for (ResolveInfo resolveInfo : resInfo) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        targetedShareIntent.setType("text/plain");
                        targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SriMa enquiry");
                        targetedShareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Email});
                        if (TextUtils.equals(packageName, "com.google.android.gm")) {
                            targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                            targetedShareIntent.setPackage(packageName);
                            targetedShareIntents.add(targetedShareIntent);
                        }

                    }
                    Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()]));
                    startActivity(chooserIntent);

                }
            }
        });
        initpDialog();
        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

            Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

        } else {
            new TeacherProgress().execute();
        }

        return view;
    }

    public class TeacherProgress extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            //			list.clear();
            Name = "";
            Email = "";
            Country = "";
            Phone = "";
            Detail = "";
            JSONParser jParser = new JSONParser();
            // http://www.puyangan.com/api/products.php?show=featured
//			JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url)+"/api/products.php?show=all&page="+pageno);

            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/teachers?id=" + Id);
//            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/cart.php?user_id=" + LoginId + "&device_id=kuyoifylu");


            try {
                productslist = json.getJSONArray("responce");
                p_length = productslist.length();
                // looping of List
                for (int i = 0; i < p_length; i++) {

                    JSONObject c = productslist.getJSONObject(i);
                    //					Bitmap myBitmap = null;
                    InputStream input = null;
                    Id = c.getString("id");
                    Name = c.getString("name");
                    Email = c.getString("email");
                    Phone = c.getString("phone");
                    Detail = c.getString("detail");
                    Image = c.getString("photo");
                    Holder h = new Holder();
//                    if (Image.equalsIgnoreCase("")) {
//
//                    } else {
//
//                        URL url = new URL(Image);
//                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                        connection.setDoInput(true);
//                        connection.setInstanceFollowRedirects(false);
//                        connection.setRequestMethod("GET");
//                        connection.connect();
//                        input = connection.getInputStream();
//
//                        BitmapFactory.Options opts = new BitmapFactory.Options();
//                        // opts.inJustDecodeBounds = true;
//                        opts.inSampleSize = 1;
//                        myBitmap = BitmapFactory.decodeStream(input, null, opts);
//                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
//                        //myBitmap.recycle();
//
//                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                        h.setBitmap(myBitmap);
//                        connection.disconnect();
//                    }

                    h.setId(Id);
                    h.setName(Name);
                    h.setEmail(Email);
                    h.setCountry(Country);
                    h.setPhone(Phone);
                    h.setDetail(Detail);
                    h.setImage(Image);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            hidepDialog();
//            imgteacher.setImageBitmap(myBitmap);
            if (Image.equalsIgnoreCase("")) {
//
            } else {
//                imgweb.loadData("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{width:100%25;} </style></head><body><img src=\"" + Image + "\"/></body></html>", "text/html", "UTF-8");
//                imgweb.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//                imgweb.setEnabled(false);
                Picasso.with(getActivity())
                        .load(Image)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
//                                holder.img.setImageBitmap(bitmap);
                                imgteacher.setBackgroundDrawable(new BitmapDrawable(getActivity().getApplicationContext().getResources(), bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }
            scv.setVisibility(View.VISIBLE);
//            Drawable d = new BitmapDrawable(getResources(), myBitmap);
//            imgteacher.setBackground(d);
            txtname.setText(Name);
            if (!Email.equalsIgnoreCase("")) {
                txtemail.setText(Email);
            } else {
                txtemail.setVisibility(View.GONE);
                tvemail.setVisibility(View.GONE);
            }
            if (!Phone.equalsIgnoreCase("")) {
                txtcontact.setText(Phone);
            } else {
                txtcontact.setVisibility(View.GONE);
                tvcontact.setVisibility(View.GONE);
            }
            if (Detail.toString().replaceAll("\\<.*?>","").equalsIgnoreCase("")) {
                txtdescription.setVisibility(View.GONE);
                tvdescription.setVisibility(View.GONE);
            } else {
                txtdescription.setText(Html.fromHtml((String) Detail).toString());
            }
//            Toast.makeText(getActivity(),Detail,Toast.LENGTH_LONG).show();


        }
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        FragmentTransaction ft;
        switch (v.getId()) {
            case R.id.txtsignup:
                if (teacherProgress != null)
                    teacherProgress.cancel(true);
                fragment = new SignUp();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.commit();
                break;
            case R.id.imgfacebook:
                if (teacherProgress != null)
                    teacherProgress.cancel(true);
                Intent in = new Intent(getActivity(), SocialPage.class);
                startActivity(in);
                break;
            case R.id.imgtwitter:
                break;
            case R.id.imginstagram:
                break;
            case R.id.btnback:
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();
                break;
            case R.id.rlfooter:
                break;
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
                mDialog = alertDialog.show();
                mDialog.getWindow().setLayout(220, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog.setCancelable(false);
            } else {
                mDialog = alertDialog.show();
                mDialog.getWindow().setLayout(550, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog.setCancelable(false);
            }
        } else {
            mDialog = alertDialog.show();
            mDialog.getWindow().setLayout(300, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mDialog.setCancelable(false);
        }
    }

    protected void hidepDialog() {
        mDialog.cancel();
    }

}