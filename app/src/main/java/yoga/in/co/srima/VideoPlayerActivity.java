package yoga.in.co.srima;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import yoga.in.co.srima.R;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
    public static ToggleButton play_stop;
    public static LinearLayout ll;
    RelativeLayout rlbelow;
    ProgressBar progressBar;
    ImageView imgclose;
    TextView txttitles, txtdetails;
    String Id, Title, Detail, Views, Video;
    //    Bitmap bitmap;
    JSONArray productslist = null;
    int p_length, position;
    AlertDialog.Builder alertDialog;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ll = (LinearLayout) findViewById(R.id.ll);
        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        play_stop = (ToggleButton) findViewById(R.id.play_stop);
        rlbelow = (RelativeLayout) findViewById(R.id.rlbelow);
        txttitles = (TextView) findViewById(R.id.txttitle);
        txtdetails = (TextView) findViewById(R.id.txtdetails);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgclose = (ImageView) findViewById(R.id.imgclose);
        initpDialog();
        new ProductProgressBar().execute();

        Intent in = getIntent();
        Id = in.getStringExtra("id");
//        Title = in.getStringExtra("title");
        Video = in.getStringExtra("video_url");
//        Duration = in.getStringExtra("time");
//        bitmap = (Bitmap) in.getParcelableExtra("bitmap");
        position = in.getIntExtra("position", 0);

        play_stop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    player.start();
                    play_stop.setVisibility(View.GONE);
                } else {
                    player.pause();
//                    play_stop.setVisibility(View.VISIBLE);

                }


            }
        });

        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);

        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(this, Uri.parse(Video));//"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                player.start();
                play_stop.setChecked(false);
                player.pause();
            }
        });


        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        VideoPlayerActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        if (tabletSize) {


//            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            ViewGroup.LayoutParams params = VideoPlayerActivity.ll.getLayoutParams();
            params.height = 450;
            params.width = width;
            VideoPlayerActivity.ll.setLayoutParams(params);
        } else {
            int width = displaymetrics.widthPixels;
            ViewGroup.LayoutParams params = VideoPlayerActivity.ll.getLayoutParams();
            params.height = 500;
            params.width = width;
            VideoPlayerActivity.ll.setLayoutParams(params);
        }

//        rlbelow.setBackgroundColor(Color.argb(200, 255, 255, 255));
        imgclose.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


//                Holder h = new Holder();
//                h.setId(Id);
//                h.setTitle(Title);
//                h.setDetail(Detail);
//                h.setViews(Views);
//                h.setVideo(Video);
//                h.setDuration(Duration);
//                h.setBitmap(bitmap);
//
//                Videos.list.set(position, h);
                player.stop();
                VideoPlayerActivity.this.finish();
                return true;
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
////        player.pause();
////        play_stop.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        player.pause();
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
        play_stop.setVisibility(View.GONE);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        player.stop();
        play_stop.setVisibility(View.VISIBLE);

    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();
        progressBar.setVisibility(View.GONE);
        play_stop.setChecked(true);

    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        Toast.makeText(this, "hello", Toast.LENGTH_LONG).show();
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    // End VideoMediaController.MediaPlayerControl
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

            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/videos?id=" + Id);
//            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/cart.php?user_id=" + LoginId + "&device_id=kuyoifylu");


            try {
                productslist = json.getJSONArray("responce");
                p_length = productslist.length();
                for (int i = 0; i < p_length; i++) {
                    JSONObject c = productslist.getJSONObject(i);
                    Id = c.getString("id");
                    Title = c.getString("title");
                    Detail = c.getString("detail");
                    Views = c.getString("view");
                    Video = c.getString("video");
                    Views = String.valueOf(Integer.parseInt(Views) + 1);
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
            txtdetails.setText(Html.fromHtml(Detail));
            txttitles.setText(Title);
        }

    }
    protected void initpDialog() {
        alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.progress, null);
        alertDialog.setView(convertView);
    }

    protected void showpDialog() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
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

}
