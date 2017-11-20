package yoga.in.co.srima;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class GalleryZoom extends Fragment implements View.OnClickListener {
    ImageView imgphoto;
    TextView txttilte, txtdetails;
    Bitmap myBitmap;
    RelativeLayout btnback,rlfooter;
    ImageView imgfacebook, imgtwitter, imginstagram;
    TextView txtsignup;
    Bundle b;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog;
    JSONArray productslist = null;
    String Id, Title, Detail, Image;
    RelativeLayout footer;
    int user_id;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    GalleryZoomProgress galleryZoomProgress = null;
//    WebView imgweb;
//    Toolbar toolbar;

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gallery_zoom);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_gallery_zoom, container, false);
        txttilte = (TextView) view.findViewById(R.id.txttitle);
        txtdetails = (TextView) view.findViewById(R.id.txtdetails);
        imgphoto = (ImageView) view.findViewById(R.id.imgphoto);
        btnback = (RelativeLayout) view.findViewById(R.id.btnback);
        rlfooter = (RelativeLayout) view.findViewById(R.id.rlfooter);
        imgfacebook = (ImageView) view.findViewById(R.id.imgfacebook);
        imgtwitter = (ImageView) view.findViewById(R.id.imgtwitter);
        imginstagram = (ImageView) view.findViewById(R.id.imginstagram);
        txtsignup = (TextView) view.findViewById(R.id.txtsignup);
//        imgweb = (WebView) view.findViewById(R.id.imgweb);
//        Intent in = getIntent();
        b = this.getArguments();
//        txttilte.setText(b.getString("title"));
//
//        txtdetails.setText(b.getString("details"));
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
        txtsignup.setOnClickListener(this);
        imgfacebook.setOnClickListener(this);
        imgtwitter.setOnClickListener(this);
        imginstagram.setOnClickListener(this);
        btnback.setOnClickListener(this);
        rlfooter.setOnClickListener(this);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        if (toolbar != null) {
//
//            setSupportActionBar(toolbar);
//        }
//        final Drawable upArrow = getResources().getDrawable(R.mipmap.back);
//        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        new Setphoto().execute();
        initpDialog();
        galleryZoomProgress = new GalleryZoomProgress();
        galleryZoomProgress.execute();
        return view;
    }

    public class GalleryZoomProgress extends AsyncTask<String, String, String> {
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

            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/galleries?id=" + Id);
//            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/cart.php?user_id=" + LoginId + "&device_id=kuyoifylu");


            try {


                productslist = json.getJSONArray("responce");
                int p_length = productslist.length();
                // looping of List
                for (int i = 0; i < p_length; i++) {
                    JSONObject c = productslist.getJSONObject(i);
                    //					Bitmap myBitmap = null;
                    InputStream input = null;
                    Id = c.getString("id");
                    Title = c.getString("title");
                    Detail = c.getString("detail");
                    Image = c.getString("photo");
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
////                        BitmapFactory.Options opts = new BitmapFactory.Options();
////                        // opts.inJustDecodeBounds = true;
////                        opts.inSampleSize = 1;
//                        myBitmap = BitmapFactory.decodeStream(input);
//                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
//                        //myBitmap.recycle();
//
//                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                        connection.disconnect();
//                    }


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
                                imgphoto.setBackgroundDrawable(new BitmapDrawable(getActivity().getApplicationContext().getResources(), bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }
//            Drawable d = new BitmapDrawable(getResources(), myBitmap);
//            imgphoto.setBackground(d);
            txttilte.setText(Title);
            txtdetails.setText(Html.fromHtml(Detail));
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

    @Override
    public void onClick(View v) {
        Fragment fragment;
        FragmentTransaction ft;
        switch (v.getId()) {
            case R.id.txtsignup:
                if (galleryZoomProgress != null)
                    galleryZoomProgress.cancel(true);
                fragment = new SignUp();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.commit();
                break;
            case R.id.imgfacebook:
                if (galleryZoomProgress != null)
                    galleryZoomProgress.cancel(true);
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
}
