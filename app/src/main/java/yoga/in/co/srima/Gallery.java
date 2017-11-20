package yoga.in.co.srima;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class Gallery extends Fragment implements View.OnClickListener {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    GridView gridview;
    JSONArray productslist = null;
    JSONArray Totalproduct = null;
    int totalproductlength = 0, addtolist = 0;
    String totalproduct;
    String Id, Title, Detail, Image;
    //	public static String Product_Id;
    String P_Id;
    ArrayList<Holder> list = null;
    //	public static String str_product;
    int pageno = 1;
    Fragment fragment;
    FragmentTransaction ft;
    TextView emptylist;
    int p_length;
    boolean flag_loading = false;
    SwipyRefreshLayout mSwipyRefreshLayout;
    private ProgressDialog pDialog;
    ImageView imgfacebook, imgtwitter, imginstagram;
    TextView txtsignup;
    RelativeLayout footer;
    int user_id;
    String currentpage = "";
    public static GalleryProgress galleryProgress = null;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.gallery, container, false);
        gridview = (GridView) view.findViewById(R.id.productlist);
        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        emptylist = (TextView) view.findViewById(R.id.emptylist);
        emptylist.setVisibility(View.GONE);
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
        editor.putString("page", "Gallery");
        editor.commit();
        initpDialog();
        flag_loading = false;
        list = new ArrayList<Holder>();
        pageno = 1;
        addtolist = 0;
        galleryProgress = new GalleryProgress();
        galleryProgress.execute();


        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Log.d("MainActivity", "Refresh triggered at "
                        + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                Toast.makeText(getActivity(), "End of the slide", Toast.LENGTH_LONG).show();
                mSwipyRefreshLayout.setRefreshing(false);
            }
        });
        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                        if (!flag_loading) {
                            flag_loading = true;

                            if (addtolist == Integer.parseInt(totalproduct)) {
                                mSwipyRefreshLayout.setRefreshing(false);
                            } else {
                                if (addtolist < Integer.parseInt(totalproduct)) {
                                    mSwipyRefreshLayout.setRefreshing(true);
                                    pageno = pageno + 1;
                                    new GalleryProgress().execute();
                                } else {
                                    mSwipyRefreshLayout.setRefreshing(false);
                                }
                            }
                        }

                    }
                }
            }
        });
        return view;
    }

    public class GalleryProgress extends AsyncTask<String, String, String> {
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

            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/galleries?show=10&page=" + pageno);
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
////                        BitmapFactory.Options opts = new BitmapFactory.Options();
////                        // opts.inJustDecodeBounds = true;
////                        opts.inSampleSize = 1;
//                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
//                        //myBitmap.recycle();
//
//                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                        h.setBitmap(myBitmap);
//                        connection.disconnect();
//                    }

                    h.setId(Id);
                    h.setTitle(Title);
                    h.setDetail(Detail);
                    h.setImage(Image);
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
//            hidepDialog();
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
            hidepDialog();
            gridview.setSelection(currentPosition);
            mSwipyRefreshLayout.setRefreshing(false);
            flag_loading = false;
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
            TextView txttitle;
            ImageView img;
            RelativeLayout rl;
//            WebView imgweb;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            final ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.gallery_listitems, paramViewGroup, false);
                holder = new ViewHolder();

                holder.txttitle = (TextView) paramView.findViewById(R.id.txttitle);
                holder.img = (ImageView) paramView.findViewById(R.id.img);
                holder.rl = (RelativeLayout) paramView.findViewById(R.id.rl);
//                holder.imgweb = (WebView) paramView.findViewById(R.id.imgweb);
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            Holder h = list.get(paramInt);
            String title = h.getTitle();
            title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
            holder.txttitle.setText(title);
            Bitmap bitmap = h.getBitmap();
            Image = h.getImage();
            if (Image.equalsIgnoreCase("")) {
                holder.img.setBackgroundResource(R.drawable.ic_menu_camera);
            } else {
//                holder.img.setImageBitmap(bitmap);
//                Bitmap bmResized=getResizedBitmap(bitmap,200,150);
//                holder.img.setImageBitmap(bmResized);
//                Drawable d = new BitmapDrawable(getResources(), bitmap);
//                holder.img.setBackground(d);
//                Picasso
//                        .with(getActivity())
//                        .load(Image)
//                        .resize(200, 120)
//                        .centerInside()
//                        .into(holder.img);
                Picasso.with(getActivity())
                        .load(Image)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
//                                holder.img.setImageBitmap(bitmap);
                                holder.img.setBackgroundDrawable(new BitmapDrawable(getActivity().getApplicationContext().getResources(), bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }
//            if (Image.equalsIgnoreCase("")) {
//
//            } else {
//                holder.imgweb.loadData("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{width:100%25;} </style></head><body><img src=\"" + Image + "\"/></body></html>", "text/html", "UTF-8");
//                holder.imgweb.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//                holder.imgweb.setEnabled(false);
//            }

            holder.rl.setTag(paramInt);
            holder.rl.setOnClickListener(new OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                    } else {
                        int pos1 = (Integer) vv.getTag();
                        Holder h1 = (Holder) list.get(pos1);
                        P_Id = h1.getId();
                        Bundle b = new Bundle();
                        b.putString("id", P_Id);
                        fragment = new GalleryZoom();
                        fragment.setArguments(b);
                        ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.frame, fragment);
                        ft.addToBackStack("add" + MainActivity.add);
                        ft.commit();
                        MainActivity.add++;
                    }
//                    Intent in = new Intent(getActivity(), GalleryZoom.class);
//                    in.putExtra("image_url", h1.getImage());
//                    in.putExtra("title", h1.getTitle());
//                    in.putExtra("details", h1.getDetail());
//                    startActivity(in);
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
                if (galleryProgress != null)
                    galleryProgress.cancel(true);
                fragment = new SignUp();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.commit();
                break;
            case R.id.imgfacebook:
                if (galleryProgress != null)
                    galleryProgress.cancel(true);
                Intent in = new Intent(getActivity(), SocialPage.class);
                startActivity(in);
                break;
            case R.id.imgtwitter:
                break;
            case R.id.imginstagram:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        new MyCustomAdapter(getActivity(), list).notifyDataSetChanged();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        currentpage = sharedpreferences.getString("page", "");
        if (currentpage.equalsIgnoreCase("Gallery")) {

        } else {
            list = new ArrayList<Holder>();
        }
    }
}
