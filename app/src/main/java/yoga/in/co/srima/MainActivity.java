package yoga.in.co.srima;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Defining Variables
    public static Toolbar toolbar;
    NavigationView navigationView;
    public static DrawerLayout drawerLayout;

    String[] title;
    String[] subtitle;
    int[] icon;
    public static ListView mDrawerList;
    public static MenuListAdapter mMenuAdapter;
    public static int mSelectedItem = 0;
    public static int add = 0;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    boolean doubleBackToExitPressedOnce = false;
    String currentpage = "";
    Fragment fragment;
    FragmentTransaction ft;

    Home home = new Home();
    //    AboutUs about = new AboutUs();
    AboutUs about = new AboutUs();
    Gallery gallery = new Gallery();
    Videos videos = new Videos();
    Courses courses = new Courses();
    ChatwithUs chatwithUs = new ChatwithUs();
    TeacherProfile teacherProfile = new TeacherProfile();
    Payment payment = new Payment();
    SignUp signUp = new SignUp();
    int shoplength;
    private Menu menu;
    SharedPreferences.Editor editor;
    public static String cart_item;
    int user_id;
    ImageView imgage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedpreferences.getInt("user_id", 0);
        if (user_id == 0) {
            title = new String[]{"Home", "About Us", "Gallery", "Videos", "Courses", "Chat with us", "Teacher Profile", "Payment", "Sign Up/Login"};
        } else {
            title = new String[]{"Home", "About Us", "Gallery", "Videos", "Courses", "Chat with us", "Teacher Profile", "Payment", "LOG OUT"};

        }


        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle("sdaf");
        getSupportActionBar().setLogo(R.mipmap.divider);
        View logoView = getToolbarLogoIcon(toolbar);
        ImageView imgdivider = (ImageView) toolbar.findViewById(R.id.imgdivider);
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logo clicked
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        imgdivider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                drawerLayout.openDrawer(Gravity.LEFT);
                return false;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.tool_bar, null);
        actionBar.setCustomView(customView);
//        Toolbar parent =(Toolbar) customView.getParent();
        toolbar.setPadding(10, 0, 0, 0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0, 0);

//        // Generate icon
        icon = new int[]{R.mipmap.close, R.mipmap.close, R.mipmap.close, R.mipmap.close, R.mipmap.close, R.mipmap.close, R.mipmap.close, R.mipmap.close, R.mipmap.close};


        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerList = (ListView) findViewById(R.id.lst_menu_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ft = getSupportFragmentManager().beginTransaction();
        mMenuAdapter = new MenuListAdapter(MainActivity.this, title, icon);
        mDrawerList.setAdapter(mMenuAdapter);
        ft.replace(R.id.frame, home);
        ft.commit();
        mSelectedItem = 0;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString("page", "Home");
        editor.commit();

//        drawerLayout.openDrawer(Gravity.START);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
//        mDrawerList.setBackgroundColor(Color.BLACK);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Locate Position
                editor = sharedpreferences.edit();
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                currentpage = sharedpreferences.getString("page", "");
                ft = getSupportFragmentManager().beginTransaction();
                switch (position) {
                    case 0:

                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();
                        } else {
                            if (Gallery.galleryProgress != null)
                                Gallery.galleryProgress.cancel(true);
                            else if (Videos.videosProgress != null)
                                Videos.videosProgress.cancel(true);
                            else if (Courses.coursesProgress != null)
                                Courses.coursesProgress.cancel(true);
                            else if (TeacherProfile.teacherProgress != null)
                                TeacherProfile.teacherProgress.cancel(true);
                            if (currentpage.equalsIgnoreCase("Home")) {

                            } else {
                                ft.replace(R.id.frame, home);
                                editor.putString("page", "Home");
                                editor.commit();
                            }
                        }
                        break;
                    case 1:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                        } else {
                            if (Gallery.galleryProgress != null)
                                Gallery.galleryProgress.cancel(true);
                            else if (Videos.videosProgress != null)
                                Videos.videosProgress.cancel(true);
                            else if (Courses.coursesProgress != null)
                                Courses.coursesProgress.cancel(true);
                            else if (TeacherProfile.teacherProgress != null)
                                TeacherProfile.teacherProgress.cancel(true);
                            if (currentpage.equalsIgnoreCase("About")) {

                            } else {
                                ft.replace(R.id.frame, about);
                                editor.putString("page", "About");
                                editor.commit();
                            }
                        }

                        break;
                    case 2:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                        } else {
                            if (Gallery.galleryProgress != null)
                                Gallery.galleryProgress.cancel(true);
                            else if (Videos.videosProgress != null)
                                Videos.videosProgress.cancel(true);
                            else if (Courses.coursesProgress != null)
                                Courses.coursesProgress.cancel(true);
                            else if (TeacherProfile.teacherProgress != null)
                                TeacherProfile.teacherProgress.cancel(true);
                            if (currentpage.equalsIgnoreCase("Gallery")) {

                            } else {
                                ft.replace(R.id.frame, gallery);
                                editor.putString("page", "Gallery");
                                editor.commit();
                            }
                        }

                        break;

                    case 3:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                        } else {
                            if (Gallery.galleryProgress != null)
                                Gallery.galleryProgress.cancel(true);
                            else if (Videos.videosProgress != null)
                                Videos.videosProgress.cancel(true);
                            else if (Courses.coursesProgress != null)
                                Courses.coursesProgress.cancel(true);
                            else if (TeacherProfile.teacherProgress != null)
                                TeacherProfile.teacherProgress.cancel(true);
                            if (currentpage.equalsIgnoreCase("Videos")) {

                            } else {
                                ft.replace(R.id.frame, videos);
                                editor.putString("page", "Videos");
                                editor.commit();
                            }
                        }

                        break;
                    case 4:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                        } else {
                            if (Gallery.galleryProgress != null)
                                Gallery.galleryProgress.cancel(true);
                            else if (Videos.videosProgress != null)
                                Videos.videosProgress.cancel(true);
                            else if (Courses.coursesProgress != null)
                                Courses.coursesProgress.cancel(true);
                            else if (TeacherProfile.teacherProgress != null)
                                TeacherProfile.teacherProgress.cancel(true);
                            if (currentpage.equalsIgnoreCase("Courses")) {

                            } else {
                                ft.replace(R.id.frame, courses);
                                editor.putString("page", "Courses");
                                editor.commit();
                            }
                        }

                        break;

                    case 5:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                        } else {
                            if (Gallery.galleryProgress != null)
                                Gallery.galleryProgress.cancel(true);
                            else if (Videos.videosProgress != null)
                                Videos.videosProgress.cancel(true);
                            else if (Courses.coursesProgress != null)
                                Courses.coursesProgress.cancel(true);
                            else if (TeacherProfile.teacherProgress != null)
                                TeacherProfile.teacherProgress.cancel(true);


                            if (currentpage.equalsIgnoreCase("Chat")) {

                            } else {
                                ft.replace(R.id.frame, chatwithUs);
                                editor.putString("page", "Chat");
                                editor.commit();
                            }
                        }

                        break;

                    case 6:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                        } else {
                            if (Gallery.galleryProgress != null)
                                Gallery.galleryProgress.cancel(true);
                            else if (Videos.videosProgress != null)
                                Videos.videosProgress.cancel(true);
                            else if (Courses.coursesProgress != null)
                                Courses.coursesProgress.cancel(true);
                            else if (TeacherProfile.teacherProgress != null)
                                TeacherProfile.teacherProgress.cancel(true);
                            if (currentpage.equalsIgnoreCase("Teacher")) {

                            } else {
                                ft.replace(R.id.frame, teacherProfile);
                                editor.putString("page", "Teacher");
                                editor.commit();
                            }
                        }

                        break;
                    case 7:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                        } else {
                            if (Gallery.galleryProgress != null)
                                Gallery.galleryProgress.cancel(true);
                            else if (Videos.videosProgress != null)
                                Videos.videosProgress.cancel(true);
                            else if (Courses.coursesProgress != null)
                                Courses.coursesProgress.cancel(true);
                            else if (TeacherProfile.teacherProgress != null)
                                TeacherProfile.teacherProgress.cancel(true);
                            if (currentpage.equalsIgnoreCase("Payment")) {

                            } else {
                                ft.replace(R.id.frame, payment);
                                editor.putString("page", "Payment");
                                editor.commit();
                            }
                        }

                        break;


                    case 8:
                        if (user_id != 0) {
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(MainActivity.this);
                            }
                            builder.setTitle("Logout")
                                    .setMessage("Are you sure want to Logout?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putInt("user_id", 0);
                                            editor.commit();
                                            Intent in = new Intent(MainActivity.this, MainActivity.class);
                                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(in);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert).show();
                        } else {
                            if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                                Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                            } else {
                                if (currentpage.equalsIgnoreCase("SignUP")) {

                                } else {
                                    ft.replace(R.id.frame, signUp);
                                    editor.putString("page", "SignUP");
                                    editor.commit();
                                }
                            }
                        }
                        break;
                }

                ft.commit();
//                mDrawerList.setBackgroundColor(getResources().getColor(R.color.SelectedColor));
//                if (position != 5)
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                    Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                } else {
                    mSelectedItem = position;
                    mMenuAdapter.notifyDataSetChanged();
                }

//                mDrawerList.setItemChecked(position, true);
//                for (int i = 0; i < mDrawerList.getChildCount(); i++) {
//                    if (position == i) {
//                        mDrawerList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.SelectedColor));
//                    } else {
//                        mDrawerList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
//                    }
//                }
                // Get the title followed by the position
//                getSupportActionBar().setTitle("Emerald Jewellers");
                // Close drawer
                drawerLayout.closeDrawers();
            }
        });


        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
/*
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
//                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                LoginId = sharedpreferences.getInt("LoginId", 0);
//                if (LoginId == 0) {
//                    title = new String[]{"Signin", "Cart", "Order", "Categories", "Recents", "Features", "All", "Settings"};
//                    mMenuAdapter.notifyDataSetChanged();
//                    mMenuAdapter = new MenuListAdapter(MainActivity.this, title);
//                    mDrawerList.setAdapter(mMenuAdapter);
//                } else {
//                    title = new String[]{"Signin", "Cart", "Order", "Categories", "Recents", "Features", "All", "Settings", "Logout"};
//                    mMenuAdapter.notifyDataSetChanged();
//                    mMenuAdapter = new MenuListAdapter(MainActivity.this, title);
//                    mDrawerList.setAdapter(mMenuAdapter);
//                }
            }
        };
*/

        //Setting the actionbarToggle to drawer layout
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
//
//        //calling sync state is necessay or else your hamburger icon wont show up
//        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();
//            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//            page = sharedpreferences.getString("page", "");
//            if (page.equalsIgnoreCase("Categories")) {
//                Categories.txtstore.setText(Categories.store);
//            } else if (page.equalsIgnoreCase("Recents")) {
//                Recents.txtstore.setText(Categories.store);
//            } else if (page.equalsIgnoreCase("Features")) {
//                Features.txtstore.setText(Categories.store);
//            } else if (page.equalsIgnoreCase("All")) {
//                All.txtstore.setText(Categories.store);
//            } else {
//            }

        } else {
            // Otherwise, ask user if he wants to leave :)
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public class MenuListAdapter extends BaseAdapter {

        // Declare Variables
        Context context;
        String[] mTitle;
        //    String[] mSubTitle;
        int[] mIcon;
        LayoutInflater inflater;
//        View itemView;

        public MenuListAdapter(Context context, String[] title, int[] icon) {
            this.context = context;
            this.mTitle = title;
//        this.mSubTitle = subtitle;
            this.mIcon = icon;
        }

        @Override
        public int getCount() {
            return mTitle.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitle[position];
        }

        class ViewHolder {
            TextView txtTitle;
            TextView txtSubTitle;
            ImageView imgIcon;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Declare Variables
            ViewHolder holder;
            if (convertView == null) {
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                itemView = inflater.inflate(R.layout.drawer_list_item, parent,
//                        false);

                convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
                holder = new ViewHolder();

                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
                holder.txtSubTitle = (TextView) convertView.findViewById(R.id.subtitle);
                holder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // Set the results into TextViews
            holder.txtTitle.setText(mTitle[position]);

//        txtSubTitle.setText(mSubTitle[position]);

            // Set the results into ImageView
            if (position == 0) {
                holder.imgIcon.setImageResource(mIcon[position]);
                holder.imgIcon.setVisibility(View.VISIBLE);
            } else {
                holder.imgIcon.setImageResource(mIcon[position]);
                holder.imgIcon.setVisibility(View.GONE);
            }

//            if (position == 0)
//                itemView.setBackgroundColor(context.getResources().getColor(R.color.SelectedColor));
//            if (mSelectedItem != 5) {
            if (position == mSelectedItem) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.SelectedColor));
            } else {
//                itemView.setBackgroundColor(Color.BLACK);
                convertView.setBackgroundColor(context.getResources().getColor(R.color.UnSelectedColor));
            }
//            }
            holder.imgIcon.setTag(position);
            holder.imgIcon.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            });
            return convertView;
        }
    }

    public static View getToolbarLogoIcon(Toolbar toolbar) {
        //check if contentDescription previously was set
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
        View logoIcon = null;
        if (potentialViews.size() > 0) {
            logoIcon = potentialViews.get(0);
        }
        //Clear content description if not previously present
        if (hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
    }

}