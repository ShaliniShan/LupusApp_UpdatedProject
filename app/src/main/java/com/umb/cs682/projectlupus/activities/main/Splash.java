package com.umb.cs682.projectlupus.activities.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.umb.cs682.projectlupus.R;
import com.umb.cs682.projectlupus.activities.common.Welcome;
import com.umb.cs682.projectlupus.util.SharedPreferenceManager;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.a_splash);

        final ImageView iv = (ImageView)findViewById(R.id.imageView);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);

        iv.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                //configApp();
                SharedPreferenceManager.initPrefs();//todo delete before distribution
                openApp();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

   /* private void configApp(){
        LupusMate.setAppContext(getApplicationContext());
        try {
            LupusMate.configureServices();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void openApp() {
        boolean isInit = SharedPreferenceManager.isFirstRun();
        Intent intent = new Intent();
        if(isInit){
            intent.setClass(this, Welcome.class);
        }else{
            intent.setClass(this, Home.class);
        }
        startActivity(intent);
    }
}
