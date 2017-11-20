package yoga.in.co.srima;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Aug-17.
 */

public class Home extends Fragment implements View.OnClickListener {
    View view;
    ImageView imgfacebook, imgtwitter, imginstagram;
    TextView txtsignup;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    int user_id;
    RelativeLayout footer;//, rlAbout, rlGallery, rlTeacher, rlServices, rlVideos, rlChat;
    JSONArray productslist = null;
    JSONArray Totalproduct = null;
    int totalproductlength, addtolist;
    String totalproduct;
    String Id, Title, CheckParameter, Image;
    ArrayList<Holder> list = null;
    int pageno = 1;
    GridView gridview;
    int p_length;
    TextView emptylist;
    private ProgressDialog pDialog;
    //    boolean flag_loading = false;
//    SwipyRefreshLayout mSwipyRefreshLayout;
    String currentpage = "";
    HomeProgress homeProgress = null;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, container, false);
        gridview = (GridView) view.findViewById(R.id.productlist);
//        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        emptylist = (TextView) view.findViewById(R.id.emptylist);
        emptylist.setVisibility(View.GONE);
        txtsignup = (TextView) view.findViewById(R.id.txtsignup);
        imgfacebook = (ImageView) view.findViewById(R.id.imgfacebook);
        imgtwitter = (ImageView) view.findViewById(R.id.imgtwitter);
        imginstagram = (ImageView) view.findViewById(R.id.imginstagram);
        footer = (RelativeLayout) view.findViewById(R.id.footer);
//        rlAbout = (RelativeLayout) view.findViewById(R.id.rlbox1);
//        rlGallery = (RelativeLayout) view.findViewById(R.id.rlbox2);
//        rlTeacher = (RelativeLayout) view.findViewById(R.id.rlbox3);
//        rlServices = (RelativeLayout) view.findViewById(R.id.rlbox4);
//        rlVideos = (RelativeLayout) view.findViewById(R.id.rlbox5);
//        rlChat = (RelativeLayout) view.findViewById(R.id.rlbox6);
        txtsignup.setOnClickListener(this);
        imgfacebook.setOnClickListener(this);
        imgtwitter.setOnClickListener(this);
        imginstagram.setOnClickListener(this);
//        rlAbout.setOnClickListener(this);
//        rlGallery.setOnClickListener(this);
//        rlTeacher.setOnClickListener(this);
//        rlServices.setOnClickListener(this);
//        rlVideos.setOnClickListener(this);
//        rlChat.setOnClickListener(this);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences.getInt("user_id", 0);
        if (user_id != 0) {
            footer.setVisibility(View.GONE);
        } else {
            footer.setVisibility(View.VISIBLE);
        }
        editor = sharedpreferences.edit();
        editor.putString("page", "Home");
        editor.commit();
        initpDialog();
        gridview = (GridView) view.findViewById(R.id.productlist);
        list = new ArrayList<Holder>();
        homeProgress = new HomeProgress();
        homeProgress.execute();
        return view;
    }

    public class HomeProgress extends AsyncTask<String, String, String> {
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

            JSONParser jParser = new JSONParser();
            // http://www.puyangan.com/api/products.php?show=featured
//			JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url)+"/api/products.php?show=all&page="+pageno);

            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/home?show=10&page=" + pageno);
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
                    CheckParameter = c.getString("slug");
                    Image = c.getString("photo");
                    Holder h = new Holder();
                    if (Image.equalsIgnoreCase("")) {

                    } else {

                        URL url = new URL(Image);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setInstanceFollowRedirects(false);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        input = connection.getInputStream();

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        // opts.inJustDecodeBounds = true;
                        opts.inSampleSize = 1;
                        Bitmap myBitmap = BitmapFactory.decodeStream(input, null, opts);
                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                        //myBitmap.recycle();

                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        h.setBitmap(myBitmap);
                        connection.disconnect();
                    }
                    h.setId(Id);
                    h.setTitle(Title);
                    h.setSlug(CheckParameter);
                    h.setImage(Image);
                    list.add(h);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {

            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            hidepDialog();
            int currentPosition = gridview.getFirstVisiblePosition();
            gridview.setAdapter(new MyCustomAdapter(getActivity(), list));
//			gridview.setSelectionFromTop(currentPosition + 1, 0);
//			gridview.smoothScrollToPosition(currentPosition);
            if (p_length == 0) {
                emptylist.setVisibility(View.VISIBLE);
                gridview.setVisibility(View.GONE);
            } else {
                emptylist.setVisibility(View.GONE);
                gridview.setVisibility(View.VISIBLE);
            }

            gridview.setSelection(currentPosition);
//            mSwipyRefreshLayout.setRefreshing(false);
//            flag_loading = false;
        }

    }

    class MyCustomAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<Holder> list;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<Holder> list) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int paramInt) {
            return paramInt;
        }

        class ViewHolder {
            TextView txtname;
            RelativeLayout rlbox;
            ImageView imgphoto, img_icon;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.home_listitem, paramViewGroup, false);
                holder = new ViewHolder();

                holder.txtname = (TextView) paramView.findViewById(R.id.txtname);
                holder.rlbox = (RelativeLayout) paramView.findViewById(R.id.rlbox);
                holder.imgphoto = (ImageView) paramView.findViewById(R.id.imgphoto);
                holder.img_icon = (ImageView) paramView.findViewById(R.id.img_icon);
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            Holder h = list.get(paramInt);
            String title = h.getTitle();
            String Slug = h.getSlug();
            holder.txtname.setText(title);
            Bitmap bitmap = h.getBitmap();
            if (h.getImage().equalsIgnoreCase("")) {
                holder.imgphoto.setBackgroundResource(R.drawable.ic_menu_camera);
            } else {
//                holder.imgphoto.setImageBitmap(bitmap);
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                holder.imgphoto.setBackground(d);
            }
            for (int i = 0; i < 6; i++) {
                if (Slug.equalsIgnoreCase("Chat")) {
                    holder.img_icon.setBackgroundResource(R.mipmap.chaticons);
                } else if (Slug.equalsIgnoreCase("Videos")) {
                    holder.img_icon.setBackgroundResource(R.mipmap.videosicon);
                } else if (Slug.equalsIgnoreCase("Courses")) {
                    holder.img_icon.setBackgroundResource(R.mipmap.servicesicon);
                } else if (Slug.equalsIgnoreCase("Certified Teachers")) {
                    holder.img_icon.setBackgroundResource(R.mipmap.certifiedteachericon);
                } else if (Slug.equalsIgnoreCase("Gallery")) {
                    holder.img_icon.setBackgroundResource(R.mipmap.eventicon);
                } else if (Slug.equalsIgnoreCase("About Us")) {
                    holder.img_icon.setBackgroundResource(R.mipmap.aboutusicon);
                }
            }

            holder.rlbox.setTag(paramInt);
            holder.rlbox.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                    } else {
                        int pos1 = (Integer) vv.getTag();
                        Holder h1 = (Holder) list.get(pos1);
                        Fragment fragment;
                        FragmentTransaction ft;
                        int position = 0;
                        if (h1.getSlug().equalsIgnoreCase("About Us")) {
                            fragment = new AboutUs();
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frame, fragment);
                            ft.addToBackStack("add" + MainActivity.add);
                            ft.commit();
                            MainActivity.add++;
                            position = 1;
                        } else if (h1.getSlug().equalsIgnoreCase("Gallery")) {
                            fragment = new Gallery();
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frame, fragment);
                            ft.addToBackStack("add" + MainActivity.add);
                            ft.commit();
                            MainActivity.add++;
                            position = 2;
                        } else if (h1.getSlug().equalsIgnoreCase("Certified Teachers")) {
                            fragment = new TeacherProfile();
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frame, fragment);
                            ft.addToBackStack("add" + MainActivity.add);
                            ft.commit();
                            MainActivity.add++;
                            position = 6;
                        } else if (h1.getSlug().equalsIgnoreCase("Courses")) {
                            fragment = new Courses();
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frame, fragment);
                            ft.addToBackStack("add" + MainActivity.add);
                            ft.commit();
                            MainActivity.add++;
                            position = 4;
                        } else if (h1.getSlug().equalsIgnoreCase("Videos")) {
                            fragment = new Videos();
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frame, fragment);
                            ft.addToBackStack("add" + MainActivity.add);
                            ft.commit();
                            MainActivity.add++;
                            position = 3;
                        } else if (h1.getSlug().equalsIgnoreCase("Chat")) {
                            fragment = new ChatwithUs();
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.frame, fragment);
                            ft.addToBackStack("add" + MainActivity.add);
                            ft.commit();
                            MainActivity.add++;
                            position = 5;
                        }
                        MainActivity.mSelectedItem = position;
                        MainActivity.mMenuAdapter.notifyDataSetChanged();
                    }
                }
            });


            return paramView;
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
        int position = 0;
        switch (v.getId()) {
            case R.id.txtsignup:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    if (homeProgress != null)
                        homeProgress.cancel(true);
                    fragment = new SignUp();
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, fragment);
                    ft.commit();
                }
                break;
            case R.id.imgfacebook:
//                Uri uri = Uri.parse("http://www.facebook.com/srimatransformational/"); // missing 'http://' will cause crashed
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    if (homeProgress != null)
                        homeProgress.cancel(true);
                    Intent in = new Intent(getActivity(), SocialPage.class);
                    startActivity(in);
                }
                break;
            case R.id.imgtwitter:
                break;
            case R.id.imginstagram:
                break;

//            case R.id.rlbox1:
//
//                fragment = new AboutUs();
//                ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.frame, fragment);
//                ft.addToBackStack("add" + MainActivity.add);
//                ft.commit();
//                MainActivity.add++;
//                position = 1;
//
//                break;
//            case R.id.rlbox2:
//                fragment = new Gallery();
//                ft = getActivity().getSupportFragmentManager().beginTransaction();
////                ft.replace(R.id.frame, fragment);
////                ft.commit();
//                ft.add(R.id.frame, fragment);
//                ft.addToBackStack("add" + MainActivity.add);
//                ft.commit();
//                MainActivity.add++;
//                position = 2;
//                break;
//            case R.id.rlbox3:
//                fragment = new TeacherProfile();
//                ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.frame, fragment);
//                ft.addToBackStack("add" + MainActivity.add);
//                ft.commit();
//                MainActivity.add++;
//                position = 6;
//                break;
//            case R.id.rlbox4:
//                fragment = new ChatwithUs();
//                ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.frame, fragment);
//                ft.addToBackStack("add" + MainActivity.add);
//                ft.commit();
//                MainActivity.add++;
//                position = 5;
//                break;
//            case R.id.rlbox5:
//                fragment = new Videos();
//                ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.frame, fragment);
//                ft.addToBackStack("add" + MainActivity.add);
//                ft.commit();
//                MainActivity.add++;
//                position = 3;
//                break;
//            case R.id.rlbox6:
//                fragment = new ChatwithUs();
//                ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.frame, fragment);
//                ft.addToBackStack("add" + MainActivity.add);
//                ft.commit();
//                MainActivity.add++;
//                position = 8;
//                break;
        }
        MainActivity.mSelectedItem = position;
        MainActivity.mMenuAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
//        new MyCustomAdapter(getActivity(), list).notifyDataSetChanged();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        currentpage = sharedpreferences.getString("page", "");
        if (currentpage.equalsIgnoreCase("Home")) {

        } else {
            list = new ArrayList<Holder>();
        }
    }
}
