package com.umb.cs682.projectlupus.activities.common;

/**
 * Created by Shalini Shanmugam on 4/2/2016.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.umb.cs682.projectlupus.R;
import com.umb.cs682.projectlupus.activities.main.Home;
import com.umb.cs682.projectlupus.config.LupusMate;
import com.umb.cs682.projectlupus.service.ProfileService;
import com.umb.cs682.projectlupus.util.Constants;
import com.umb.cs682.projectlupus.util.Utils;
import com.umb.cs682.projectlupus.util.SharedPreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import com.google.android.backup;
import java.nio.channels.FileChannel;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.umb.cs682.projectlupus.util.SharedPreferenceManager;
import de.greenrobot.dao.DaoException;


public class Share_Data extends Activity implements View.OnClickListener {
    private static final String TAG = ".activities.sharedata";
    private static final String SAMPLE_DB_NAME = "LupusMateDb";
    //private static final String SAMPLE_TABLE_NAME = "Info";
    TextView t3;
    private String user = "shalini";
    private String pass = "fl!wer123";
    private String host = "users.cs.umb.edu";
    private int portNum = 22;
    String username;
    private ProfileService profileService;
    private CharSequence sdTitle;

    @Override
    public Intent getParentActivityIntent() {
        Intent newIntent = null;
        newIntent = new Intent(this, getIntent().getClass());
        return newIntent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_export_database);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.button1).setOnClickListener(this);
        t3 = (TextView) findViewById(R.id.share_text);
        t3.setMovementMethod(LinkMovementMethod.getInstance());
        final LupusMate lupusMate = (LupusMate) getApplicationContext();
        profileService = lupusMate.getProfileService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                exportDB();
                break;
                  }
    }

    private void exportDB() {

        boolean sent_data = SharedPreferenceManager.getBooleanPref(Constants.DONE_SHARED_DATA);
        if (sent_data) {
            Toast.makeText(this, "Your health information is already shared with your doctor", Toast.LENGTH_LONG).show();
            Log.d("Database already shared", "Sh_pref true");
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            return;
        }

        Context mContext = getApplicationContext();
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        File currentDB = new File(mContext.getDatabasePath("LupusMateDb").toString());

        if (sd.exists())
        {
            Log.d("Backup:", "sd existing");
            Log.d("Backup:", sd.getAbsolutePath());
            Log.d("Backup:", data.getAbsolutePath());
            String state = Environment.getExternalStorageState();
            File backupDBDir = new File(Environment.getExternalStorageDirectory().toString() + "/LupusMate_Data");
            //File currentDB = new File(currentDBPath);

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                Log.d("Backup:", "Ext storage is mounted");
                try {

                    if (!backupDBDir.isDirectory()) {
                        Log.d("Backup:", "Creating dir");
                        backupDBDir.mkdirs();
                        if (backupDBDir.isDirectory()) {
                            Log.d("backup:", "Director created!!!!");
                        } else {
                            Log.d("backup", "Directory not created!!!!");
                            backupDBDir = mContext.getFilesDir();
                            if (!backupDBDir.isDirectory()) {
                                Log.d("backup:", "Directory still not created");
                            } else {
                                Log.d("Backup:", backupDBDir.getAbsolutePath());
                            }
                        }
                    }


                    File backupDBFile = new File(backupDBDir, "/LupusMateBackup");

                    // LupusMateBackup = new SimpleDateFormat("yyyyMMddhhmm'.txt'").format(new Date());
                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDBFile).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        Log.d("Backup:", "Database copied");
                        new AsyncTask<Void, Void, List<String>>() {
                            @Override
                            protected List<String> doInBackground(Void... params) {
                                try {
                                    filesftp("shalini", "fl!wer123", "users.cs.umb.edu", 22);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                        }.execute();//fileSftp();
                        Toast.makeText(this, "Dr Ramon is now sharing your health information!", Toast.LENGTH_LONG).show();

                        Log.d("Setting sh_pref", "true");
                        SharedPreferenceManager.setBooleanPref(TAG, Constants.DONE_SHARED_DATA, true);

                    } else {
                        Log.d("backup", "DB not existing");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public void filesftp(String user, String pass, String host, int portNum) throws Exception {

        JSch jsch = new JSch();
        Session session = null;

       try {

            session = jsch.getSession(user, host, portNum);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(pass);
            Log.d("server", "before Connected");
            session.connect();
            Log.d("server", "Connected");
            Channel channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;

            // use the put method , if you are using android remember to remove "file://" and use only the relative path
            sftp.put("/storage/emulated/0/LupusMate_Data/LupusMateBackup", "/home/shalini/Documents");
          //  sftp.cd("/home/shalini/Documents");
            String LmbName = null;
           try {
               username = profileService.getProfileData().getUserName();
           }catch(DaoException e){
               username = "";
           }
           sdTitle = username;
          // actionBar.setTitle(sdTitle);
        /*    Long tsLong = System.currentTimeMillis()*1000L;
            String ts = tsLong.toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(tsLong);
            String stime = sdf.format(cal.getTime());
            */
           Long tsLong = System.currentTimeMillis()/1000;
           //String ts = tsLong.toString();
           String sTime =  getDateCurrentTimeZone(tsLong);
            LmbName ="/home/shalini/Documents/LupusMateBackup " + sdTitle  + sTime;
            sftp.rename("/home/shalini/Documents/LupusMateBackup",LmbName);
            sftp.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }



    public  String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }
}