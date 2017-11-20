package yoga.in.co.srima;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yoga.in.co.srima.R;

public class AboutUs_Details extends Fragment implements ImageGetter {
    TextView txttitle;//, txtdetails;
    //    Toolbar toolbar;
    String Title, Details, Id, Image = "";
    JSONArray productslist = null;
    RelativeLayout btnback;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog;
    Bitmap myBitmap;
    RelativeLayout footer,rlfooter;
    int user_id;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    WebView web;
//    ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.about_us__details, container, false);

        txttitle = (TextView) view.findViewById(R.id.txttitle);
//        txtdetails = (TextView) view.findViewById(R.id.txtdescription);
        btnback = (RelativeLayout) view.findViewById(R.id.btnback);
        web = (WebView) view.findViewById(R.id.web);
//        img = (ImageView) view.findViewById(R.id.img);

//        Intent in = getIntent();
//        Id = in.getStringExtra("id");
        Bundle b = this.getArguments();
        Id = b.getString("id");
        footer = (RelativeLayout) view.findViewById(R.id.footer);
        rlfooter=(RelativeLayout) view.findViewById(R.id.rlfooter);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences.getInt("user_id", 0);
        if (user_id != 0) {
            footer.setVisibility(View.GONE);
        } else {
            footer.setVisibility(View.VISIBLE);
        }
        initpDialog();
        new ProductProgressBar().execute();
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();
            }
        });
        rlfooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return view;
    }

    public class ProductProgressBar extends AsyncTask<String, String, String> {

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

            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/about?id=" + Id);
//            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/cart.php?user_id=" + LoginId + "&device_id=kuyoifylu");


            try {


                productslist = json.getJSONArray("responce");
                // looping of List
                for (int i = 0; i < productslist.length(); i++) {

                    JSONObject c = productslist.getJSONObject(i);
                    //					Bitmap myBitmap = null;
                    InputStream input = null;
                    Title = c.getString("title");
                    Details = c.getString("detail");
                    // ================get image from html tag==============

                    String ptr = "src\\s*=\\s*([\"'])?([^\"']*)";
                    Pattern pp = Pattern.compile(ptr);
                    Matcher m = pp.matcher(Details);
                    if (m.find()) {
                        Image = m.group(2); //Result
                    }
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
                        myBitmap = BitmapFactory.decodeStream(input, null, opts);
                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                        //myBitmap.recycle();

                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        connection.disconnect();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            hidepDialog();
            txttitle.setText(Title);


//            URLImageParser p = new URLImageParser(txtdetails, getActivity());
//            Spanned htmlSpan = Htm l.fromHtml(Details, p, null);
//            txtdetails.setText(htmlSpan);
//            if (Image.equalsIgnoreCase("")) {
//                txtdetails.setText(Html.fromHtml(Details));
//            } else {
//                Drawable d = new BitmapDrawable(getResources(), myBitmap);
//                img.setBackground(d);
////                Spanned spanned = Html.fromHtml(Details, AboutUs_Details.this, null);
////                txtdetails.setText(spanned);
//                txtdetails.setText(Html.fromHtml(Details.replaceAll("<img.+?>", "")));
//            }

            String message ="<font color='white'>"+Details+"</font></font>";

//            web.loadData(Details, "text/html", "UTF-8");
//            web.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//            web.setEnabled(false);
//            web.setBackgroundColor(Color.TRANSPARENT);
//            web.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

            String htmlText = "<html>"+ Details+" </html>";
            WebSettings settings = web.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            web.loadData(htmlText, "text/html; charset=utf-8", "utf-8");


        }

    }

    @Override
    public Drawable getDrawable(String arg0) {
        // TODO Auto-generated method stub
//        int id = 0;
//
//
//        id = R.mipmap.event;
//        LevelListDrawable d = new LevelListDrawable();
//        Drawable empty = getResources().getDrawable(id);
//        d.addLevel(0, 0, empty);
//        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());


        LevelListDrawable d = new LevelListDrawable();
        Drawable drawable = new BitmapDrawable(getResources(), myBitmap);


        d.addLevel(0, 0, drawable);
        d.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return d;
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

