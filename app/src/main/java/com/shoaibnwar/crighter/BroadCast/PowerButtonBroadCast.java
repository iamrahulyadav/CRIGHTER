package com.shoaibnwar.crighter.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;

import com.shoaibnwar.crighter.Services.GPSTracker;
import com.shoaibnwar.crighter.Services.ImageCaptureService;
import com.shoaibnwar.crighter.Services.RecordingAudio;
import com.shoaibnwar.crighter.Preferences.SPref;
import com.shoaibnwar.crighter.URLS.APIURLS;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by gold on 8/11/2018.
 */

public class PowerButtonBroadCast extends BroadcastReceiver {

    public static boolean wasScreenOn = true;
    private int timer = 5;
    Context mContext;
    private Handler mHandler = new Handler();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int count = 0;
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // do whatever you need to do here
            wasScreenOn = false;
            SharedPreferences sharedPreferences = context.getSharedPreferences("time", 0);

            long firsTme = System.currentTimeMillis();
            long secondtime = System.currentTimeMillis();

            long preTime = SPref.getTingFirstTime(sharedPreferences,SPref.FIRST_TIME);
            long diff = firsTme-preTime;
            Log.e("TAG", "the difference is " + diff);

            if (20<diff && 5999>diff){
                Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(400);
                //mHandler = new Handler();
               // useHandler(context);

                context.startService(new Intent(context, ImageCaptureService.class));
                context.startService(new Intent(context, RecordingAudio.class));
                Log.e("LOB", "here power button is pressed");
                GPSTracker gpsTracker = new GPSTracker(context);
                Log.e("TAG", "the latitude are " + gpsTracker.latitude);
                Log.e("TAG", "the longitude are " + gpsTracker.longitude);

                callingHandlerTimer(context);

            }
            SPref.storingFirstTimeAndsecondTime(sharedPreferences, firsTme, secondtime);


        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // and do whatever you need to do here
            wasScreenOn = true;

        }else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            Log.e("LOB","userpresent");
            Log.e("LOB","wasScreenOn"+wasScreenOn);
        }
    }

    //Thread for starting mainActivity
    private Runnable mRunnableStartMainActivity = new Runnable() {
        @Override
        public void run() {

            timer--;
            mHandler = new Handler();
            mHandler.postDelayed(this, 1000);

            if (timer == 4){
                Log.e("Handler", " Calls");

                //mContext.startService(new Intent(mContext, ImageCaptureService.class));
            }
            if (timer == 3){
                Log.e("Handler", " Calls");

                //mContext.startService(new Intent(mContext, ImageCaptureService.class));
            }
            if (timer == 2) {
                Log.e("Handler", " Calls");

                //mContext.startService(new Intent(mContext, ImageCaptureService.class));
            }
            if (timer == 1) {
                Log.e("Handler", " Calls");

                //mContext.startService(new Intent(mContext, ImageCaptureService.class));
            }
            if (timer == 0) {
                Log.e("Handler", " Calls");
               // mContext.startService(new Intent(mContext, ImageCaptureService.class));
                mHandler.removeCallbacks(mRunnableStartMainActivity);
                timer = 5;
                SharedPreferences sharedPreferencesUser = mContext.getSharedPreferences(SPref.PREF_USER_CRED, 0);
                String userId = SPref.getStringPref(sharedPreferencesUser, SPref.USER_ID);
                SharedPreferences sharedPreferencesAudioPath = mContext.getSharedPreferences(SPref.PRE_AUDIO, 0);
                String audioPath = SPref.getStringPref(sharedPreferencesAudioPath, SPref.AUDIO_FILE_PATH);
                SharedPreferences sharedPreferencesVideoPath = mContext.getSharedPreferences(SPref.PREF_IMAGS, 0);
                String imagePath = SPref.getStringPref(sharedPreferencesVideoPath, SPref.IMAGE_PATH);
                GPSTracker gpsTracker = new GPSTracker(mContext);
                Log.e("TAG", "the preff data from userId "+ userId);
                Log.e("TAG", "the preff data from audio path "+ audioPath);
                Log.e("TAG", "the preff data from image path "+ imagePath);
                Log.e("TAG", "the preff data from latitude "+ gpsTracker.latitude);
                Log.e("TAG", "the preff data from longitude "+ gpsTracker.longitude);

                uploadingDataToServer(mContext, userId, audioPath, imagePath, String.valueOf(gpsTracker.latitude), String.valueOf(gpsTracker.longitude));
            }
        }
    };

    //handler for the starign activity
    Handler newHandler;
    public void useHandler(Context context){
        mContext = context;
        newHandler = new Handler();
        newHandler.postDelayed(mRunnableStartMainActivity, 1000);
    }



    private void uploadingDataToServer(Context context, String userID, String audioPath, String ImagePath, String lat, String lng)
    {
         //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(context, uploadId, APIURLS.BACKGROUND_DATA_URL)
                    .addFileToUpload(ImagePath, "img1") //Adding image file
                    .addFileToUpload(ImagePath, "img2") //Adding image file
                    .addFileToUpload(ImagePath, "img3") //Adding image file
                    .addFileToUpload(ImagePath, "img4") //Adding image file
                    .addFileToUpload(ImagePath, "img5") //Adding image file
                    .addFileToUpload(ImagePath, "img1") //Adding image file
                    .addFileToUpload(audioPath, "audio") //Adding audio file
                    .addParameter("post_lat", lat) //Adding text parameter to the request
                    .addParameter("post_lng", lng)
                    .addParameter("user_id", userID)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }
                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, Exception exception) {

                        }
                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            String responseFromServer = serverResponse.getBodyAsString();
                            Log.e("TAG", "the response from server for upload image: " + serverResponse.getBodyAsString());
                            if (responseFromServer!=null) {
                                try {
                                    JSONObject jObj = new JSONObject(responseFromServer);
                                    boolean error = jObj.getBoolean("error");
                                    if (!error) {

                                    } else {
                                        String errorMsg = jObj.getString("msg");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
        }
    }

    private void callingHandlerTimer(final Context mContext)
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferencesUser = mContext.getSharedPreferences(SPref.PREF_USER_CRED, 0);
                String userId = SPref.getStringPref(sharedPreferencesUser, SPref.USER_ID);
                SharedPreferences sharedPreferencesAudioPath = mContext.getSharedPreferences(SPref.PRE_AUDIO, 0);
                String audioPath = SPref.getStringPref(sharedPreferencesAudioPath, SPref.AUDIO_FILE_PATH);
                SharedPreferences sharedPreferencesVideoPath = mContext.getSharedPreferences(SPref.PREF_IMAGS, 0);
                String imagePath = SPref.getStringPref(sharedPreferencesVideoPath, SPref.IMAGE_PATH);
                GPSTracker gpsTracker = new GPSTracker(mContext);
                Log.e("TAG", "the preff data from userId "+ userId);
                Log.e("TAG", "the preff data from audio path "+ audioPath);
                Log.e("TAG", "the preff data from image path "+ imagePath);
                Log.e("TAG", "the preff data from latitude "+ gpsTracker.latitude);
                Log.e("TAG", "the preff data from longitude "+ gpsTracker.longitude);

                uploadingDataToServer(mContext, userId, audioPath, imagePath, String.valueOf(gpsTracker.latitude), String.valueOf(gpsTracker.longitude));

            }
        }, 5000);
    }

}