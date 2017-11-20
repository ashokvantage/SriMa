package yoga.in.co.srima;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class AboutUs extends Fragment implements View.OnClickListener {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    JSONArray productslist = null;
    JSONArray Totalproduct = null;
    int totalproductlength, addtolist;
    String totalproduct;
    String Id, Title, subTitle;
    //	public static String Product_Id;
    String P_Id;
    ArrayList<Holder> list = null;
    //	public static String str_product;
    int pageno = 1;
    Fragment fragment;
    FragmentTransaction ft;
    int p_length;
    boolean flag_loading = false;
    private ProgressDialog pDialog;
    ImageView imgfacebook, imgtwitter, imginstagram;
    TextView txtsignup;
    RelativeLayout footer;
    int user_id;
    String currentpage = "";
    RelativeLayout rlbox1, rlbox2, rlbox3, rlbox4, rlbox5, rlbox6, rlbox7, rlbox8, rlbox9, rlbox10;
    TextView title1, title2, title3, title4, title5, title6, title7, title8, title9, title10;
    TextView subtitle1, subtitle2, subtitle3, subtitle4, subtitle5, subtitle6, subtitle7, subtitle8, subtitle9, subtitle10;
    ScrollView scv;
    //    ExpandableHeightGridView gridview;
    AboutUsProgress aboutUsProgress = null;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.aboutus_static, container, false);
//        gridview = (ExpandableHeightGridView)view.findViewById(R.id.productlist);

        txtsignup = (TextView) view.findViewById(R.id.txtsignup);
        imgfacebook = (ImageView) view.findViewById(R.id.imgfacebook);
        imgtwitter = (ImageView) view.findViewById(R.id.imgtwitter);
        imginstagram = (ImageView) view.findViewById(R.id.imginstagram);
        rlbox1 = (RelativeLayout) view.findViewById(R.id.rlbox1);
        rlbox2 = (RelativeLayout) view.findViewById(R.id.rlbox2);
        rlbox3 = (RelativeLayout) view.findViewById(R.id.rlbox3);
        rlbox4 = (RelativeLayout) view.findViewById(R.id.rlbox4);
        rlbox5 = (RelativeLayout) view.findViewById(R.id.rlbox5);
        rlbox6 = (RelativeLayout) view.findViewById(R.id.rlbox6);
        rlbox7 = (RelativeLayout) view.findViewById(R.id.rlbox7);
        title1 = (TextView) view.findViewById(R.id.title1);
        title2 = (TextView) view.findViewById(R.id.title2);
        title3 = (TextView) view.findViewById(R.id.title3);
        title4 = (TextView) view.findViewById(R.id.title4);
        title5 = (TextView) view.findViewById(R.id.title5);
        title6 = (TextView) view.findViewById(R.id.title6);
        title7 = (TextView) view.findViewById(R.id.title7);


        subtitle1 = (TextView) view.findViewById(R.id.subtitle1);
        subtitle2 = (TextView) view.findViewById(R.id.subtitle2);
        subtitle3 = (TextView) view.findViewById(R.id.subtitle3);
        subtitle4 = (TextView) view.findViewById(R.id.subtitle4);
        subtitle5 = (TextView) view.findViewById(R.id.subtitle5);
        subtitle6 = (TextView) view.findViewById(R.id.subtitle6);
        subtitle7 = (TextView) view.findViewById(R.id.subtitle7);

        scv = (ScrollView) view.findViewById(R.id.scv);
        scv.setVisibility(View.GONE);
        txtsignup.setOnClickListener(this);
        imgfacebook.setOnClickListener(this);
        imgtwitter.setOnClickListener(this);
        imginstagram.setOnClickListener(this);
        rlbox1.setOnClickListener(this);
        rlbox2.setOnClickListener(this);
        rlbox3.setOnClickListener(this);
        rlbox4.setOnClickListener(this);
        rlbox5.setOnClickListener(this);
        rlbox6.setOnClickListener(this);
        rlbox7.setOnClickListener(this);

        footer = (RelativeLayout) view.findViewById(R.id.footer);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences.getInt("user_id", 0);
        if (user_id != 0) {
            footer.setVisibility(View.GONE);
        } else {
            footer.setVisibility(View.VISIBLE);
        }
        editor = sharedpreferences.edit();
        editor.putString("page", "About");
        editor.commit();
        initpDialog();
        flag_loading = false;
        list = new ArrayList<Holder>();
        aboutUsProgress = new AboutUsProgress();
        aboutUsProgress.execute();


        return view;
    }

    public class AboutUsProgress extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (!flag_loading)
                showpDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            //			list.clear();

            JSONParser jParser = new JSONParser();
            // http://www.puyangan.com/api/products.php?show=featured
//			JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url)+"/api/products.php?show=all&page="+pageno);

            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/about?show=10&page=" + pageno);
//            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/cart.php?user_id=" + LoginId + "&device_id=kuyoifylu");


            try {
                Totalproduct = json.getJSONArray("totalrecords");
                totalproductlength = Totalproduct.length();

                for (int i = 0; i < totalproductlength; i++) {
                    JSONObject ob = Totalproduct.getJSONObject(i);
                    totalproduct = ob.getString("total");
                }


                productslist = json.getJSONArray("responce");
                p_length = productslist.length();
                // looping of List
                for (int i = 0; i < p_length; i++) {

                    addtolist = addtolist + 1;
                    JSONObject c = productslist.getJSONObject(i);
                    //					Bitmap myBitmap = null;
                    InputStream input = null;
                    Id = c.getString("id");
                    Title = c.getString("title");
                    subTitle = c.getString("subtitle");
                    Holder h = new Holder();
                    h.setId(Id);
                    h.setTitle(Title);
                    h.setSubTitle(subTitle);
                    list.add(h);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            hidepDialog();

            flag_loading = false;
            for (int i = 0; i < p_length; i++) {
                Holder h = list.get(i);
                Title = h.getTitle();
                subTitle = h.getSubTitle();
                if (i == 0) {
                    title1.setText(h.getTitle());
                    subtitle1.setText(h.getSubTitle());
                    if (Title.equalsIgnoreCase(subTitle)) {
                        subtitle1.setVisibility(View.GONE);
                    } else {
                        subtitle1.setVisibility(View.VISIBLE);
                    }
                } else if (i == 1) {
                    title2.setText(h.getTitle());
                    subtitle2.setText(h.getSubTitle());
                    if (Title.equalsIgnoreCase(subTitle)) {
                        subtitle2.setVisibility(View.GONE);
                    } else {
                        subtitle2.setVisibility(View.VISIBLE);
                    }
                } else if (i == 2) {
                    title3.setText(h.getTitle());
                    subtitle3.setText(h.getSubTitle());
                    if (Title.equalsIgnoreCase(subTitle)) {
                        subtitle3.setVisibility(View.GONE);
                    } else {
                        subtitle3.setVisibility(View.VISIBLE);
                    }
                } else if (i == 3) {
                    title4.setText(h.getTitle());
                    subtitle4.setText(h.getSubTitle());
                    if (Title.equalsIgnoreCase(subTitle)) {
                        subtitle4.setVisibility(View.GONE);
                    } else {
                        subtitle4.setVisibility(View.VISIBLE);
                    }
                } else if (i == 4) {
                    title5.setText(h.getTitle());
                    subtitle5.setText(h.getSubTitle());
                    if (Title.equalsIgnoreCase(subTitle)) {
                        subtitle5.setVisibility(View.GONE);
                    } else {
                        subtitle5.setVisibility(View.VISIBLE);
                    }
                } else if (i == 5) {
                    title6.setText(h.getTitle());
                    subtitle6.setText(h.getSubTitle());
                    if (Title.equalsIgnoreCase(subTitle)) {
                        subtitle6.setVisibility(View.GONE);
                    } else {
                        subtitle6.setVisibility(View.VISIBLE);
                    }
                } else if (i == 6) {
                    title7.setText(h.getTitle());
                    subtitle7.setText(h.getSubTitle());
                    if (Title.equalsIgnoreCase(subTitle)) {
                        subtitle7.setVisibility(View.GONE);
                    } else {
                        subtitle7.setVisibility(View.VISIBLE);
                    }
                }
                scv.setVisibility(View.VISIBLE);
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
                mDialog = alertDialog.show();
                mDialog.getWindow().setLayout(220, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog.setCancelable(false);
            } else {
                mDialog = alertDialog.show();
                mDialog.getWindow().setLayout(550, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mDialog.setCancelable(false);
            }
        }
        else
        {
            mDialog = alertDialog.show();
            mDialog.getWindow().setLayout(300, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mDialog.setCancelable(false);
        }
    }

    protected void hidepDialog() {
        mDialog.cancel();
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        FragmentTransaction ft;
        Holder h1;
        Bundle b;
        switch (v.getId()) {
            case R.id.txtsignup:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    if (aboutUsProgress != null)
                        aboutUsProgress.cancel(true);
                    fragment = new SignUp();
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, fragment);
                    ft.commit();
                }
                break;
            case R.id.imgfacebook:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    if (aboutUsProgress != null)
                        aboutUsProgress.cancel(true);
                    Intent in = new Intent(getActivity(), SocialPage.class);
                    startActivity(in);
                }
                break;
            case R.id.imgtwitter:
                break;
            case R.id.imginstagram:
                break;
            case R.id.rlbox1:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    h1 = (Holder) list.get(0);
                    b = new Bundle();
                    b.putString("id", h1.getId());
                    b.putString("title", h1.getTitle());
                    fragment = new AboutUs_Details();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + MainActivity.add);
                    ft.commit();
                    MainActivity.add++;
                }
                break;
            case R.id.rlbox2:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    h1 = (Holder) list.get(1);
                    b = new Bundle();
                    b.putString("id", h1.getId());
                    b.putString("title", h1.getTitle());
                    fragment = new AboutUs_Details();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + MainActivity.add);
                    ft.commit();
                    MainActivity.add++;
                }
                break;
            case R.id.rlbox3:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    h1 = (Holder) list.get(2);
                    b = new Bundle();
                    b.putString("id", h1.getId());
                    b.putString("title", h1.getTitle());
                    fragment = new AboutUs_Details();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + MainActivity.add);
                    ft.commit();
                    MainActivity.add++;
                }
                break;
            case R.id.rlbox4:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    h1 = (Holder) list.get(3);
                    b = new Bundle();
                    b.putString("id", h1.getId());
                    b.putString("title", h1.getTitle());
                    fragment = new AboutUs_Details();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + MainActivity.add);
                    ft.commit();
                    MainActivity.add++;
                }
                break;
            case R.id.rlbox5:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    h1 = (Holder) list.get(4);
                    b = new Bundle();
                    b.putString("id", h1.getId());
                    b.putString("title", h1.getTitle());
                    fragment = new AboutUs_Details();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + MainActivity.add);
                    ft.commit();
                    MainActivity.add++;
                }
                break;
            case R.id.rlbox6:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    h1 = (Holder) list.get(5);
                    b = new Bundle();
                    b.putString("id", h1.getId());
                    b.putString("title", h1.getTitle());
                    fragment = new AboutUs_Details();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + MainActivity.add);
                    ft.commit();
                    MainActivity.add++;
                }
                break;
            case R.id.rlbox7:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    h1 = (Holder) list.get(6);
                    b = new Bundle();
                    b.putString("id", h1.getId());
                    b.putString("title", h1.getTitle());
                    fragment = new AboutUs_Details();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + MainActivity.add);
                    ft.commit();
                    MainActivity.add++;
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        new MyCustomAdapter(getActivity(), list).notifyDataSetChanged();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        currentpage = sharedpreferences.getString("page", "");
        if (currentpage.equalsIgnoreCase("About")) {

        } else {
            list = new ArrayList<Holder>();
        }
    }
}
