/*
 * Copyright (C) 2013 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thefourhourworkweek;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

/**
 * Main Activity. Inflates main activity xml.
 */
public class MyActivity extends AppCompatActivity implements WSCallerVersionListener{

    private static final long GAME_LENGTH_MILLISECONDS = 3000;

    private InterstitialAd interstitialAd;
    private CountDownTimer countDownTimer;
    private Button retryButton;
    private boolean gameIsInProgress;
    private long timerMilliseconds;
    WebView mywebview;
    ImageView image;
    int flag=0;
    boolean isForceUpdate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        new GooglePlayStoreAppVersionNameLoader(getApplicationContext(), this).execute();

        // Initialize the Mobile Ads SDK.

        /*Sample*/
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        /*Live*/
        // MobileAds.initialize(this, "ca-app-pub-9801494850714879~3294395916");

        // Create the InterstitialAd and set the adUnitId.
        interstitialAd = new InterstitialAd(this);

       /*Sample*/
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

       /*Live*/
        // interstitialAd.setAdUnitId("ca-app-pub-9801494850714879/5509495719");

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame();
            }
        });
        mywebview = (WebView) findViewById(R.id.webView1);
        image = findViewById(R.id.image);
        // Create the "retry" button, which tries to show an interstitial between game plays.
        retryButton = findViewById(R.id.retry_button);
        //retryButton.setVisibility(View.INVISIBLE);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flag=1;
                showInterstitial();


            }
        });
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
              //  Toast.makeText(MyActivity.this, "ad failed to load", Toast.LENGTH_SHORT).show();


            }

            public void onAdLoaded() {
              //  Toast.makeText(MyActivity.this, "ad loaded", Toast.LENGTH_SHORT).show();

                //showInterstitial();
            }


            @Override
            public void onAdOpened() {
                super.onAdOpened();
              //  Toast.makeText(MyActivity.this, "ad open", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
              //  Toast.makeText(MyActivity.this, "ad impression", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
               // Toast.makeText(MyActivity.this, "ad close", Toast.LENGTH_SHORT).show();
                mywebview.setVisibility(View.VISIBLE);
               // image.setVisibility(View.GONE);
                mywebview.loadUrl("https://docs.google.com/document/d/1OxAQuaImXiTgNvJyK_IY8rPPJyoXlSU40si_RSvTL5Q/edit");


            }
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Toast.makeText(MyActivity.this, "ad clicked", Toast.LENGTH_SHORT).show();

            }

        });

        startGame();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }


    private void createTimer(final long milliseconds) {
        // Create the game timer, which counts down to the end of the level
        // and shows the "retry" button.
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        final TextView textView = findViewById(R.id.timer);

        countDownTimer = new CountDownTimer(milliseconds, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                timerMilliseconds = millisUnitFinished;
                textView.setText("seconds remaining: " + ((millisUnitFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                gameIsInProgress = false;
                textView.setText("done!");
                retryButton.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    public void onResume() {
        // Start or resume the game.
        super.onResume();

        if (gameIsInProgress) {
            resumeGame(timerMilliseconds);

            Log.d("life_cycle", "onResume called: ");


           /* if(flag==0){
                finish();
            }*/
        }
    }

    @Override
    public void onPause() {
        // Cancel the timer if the game is paused.
        countDownTimer.cancel();

      /*  Log.d("life_cycle", "onPause called: ");
        if(flag==1){
            flag=0;
        }*/

        super.onPause();
    }

   /* @Override
    protected void onStop() {
        super.onStop();
        Log.d("life_cycle", "onStop called: ");
        finish();

    }*/

    private void showInterstitial() {


            // Show the ad if it's ready. Otherwise toast and restart the game.
            if (interstitialAd != null && interstitialAd.isLoaded()) {
                interstitialAd.show();


            } else {
                //  Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
                startGame();
            }


    }

    private void startGame() {

        if(flag==1) {
            // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
            if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
                AdRequest adRequest = new AdRequest.Builder().build();
                interstitialAd.loadAd(adRequest);
            }

            retryButton.setVisibility(View.INVISIBLE);
            resumeGame(GAME_LENGTH_MILLISECONDS);
        }

    }

    private void resumeGame(long milliseconds) {
        // Create a new timer for the correct length and start it.
        gameIsInProgress = true;
        timerMilliseconds = milliseconds;
        createTimer(milliseconds);
        countDownTimer.start();
    }

    @Override
    public void onGetResponse(boolean isUpdateAvailable) {
        Log.e("ResultAPPMAIN", String.valueOf(isUpdateAvailable));
        if (isUpdateAvailable) {
            showUpdateDialog();
        }
    }

    /**
     * Method to show update dialog
     */
    public void showUpdateDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyActivity.this);

        alertDialogBuilder.setTitle(MyActivity.this.getString(R.string.app_name));
        alertDialogBuilder.setMessage("Update available");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isForceUpdate) {
                    finish();
                }
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }
}
