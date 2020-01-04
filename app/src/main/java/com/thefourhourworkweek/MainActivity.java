package com.thefourhourworkweek;

import android.os.Bundle;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
/*import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;*/

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Handler handler;
    WebView mywebview;
    ImageView image;
    int flag=1;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("life_cycle", "onCreate called: ");



        mywebview = (WebView) findViewById(R.id.webView1);
        image = findViewById(R.id.image);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Write whatever to want to do after delay specified (1 sec)
                mywebview.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
                mywebview.loadUrl("https://docs.google.com/document/d/1OxAQuaImXiTgNvJyK_IY8rPPJyoXlSU40si_RSvTL5Q/edit");

            }
        }, 5000);

    }
    @Override
    protected void onPause() {
        super.onPause();

        Log.d("life_cycle", "onPause called: ");
        if(flag==1){
            flag=0;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("life_cycle", "onStart called: ");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("life_cycle", "onResume called: ");


        if(flag==0){
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("life_cycle", "onStop called: ");
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("life_cycle", "onDestroy called: ");
    }

}