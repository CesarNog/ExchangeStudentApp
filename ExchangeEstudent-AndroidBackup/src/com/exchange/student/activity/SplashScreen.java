package com.exchange.student.activity;
 
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.exchange.student.R;
import com.exchange.student.database.MySQLiteHelper;
import com.exchange.student.database.UserDataSource;
import com.exchange.student.utils.PreferencesUtils;
 
public class SplashScreen extends Activity {
	
    private static int backCounter = 0;
    
    protected boolean calledFrom3Party;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);        
         
        /**
         * Showing splashscreen while making network calls to download necessary
         * data before launching the app Will use AsyncTask to make http call
         */
        new PrefetchData().execute();
        
        final boolean firstRun = PreferencesUtils.isFirstRunFindContacts(getApplicationContext());
        final String firstActivity = PreferencesUtils.recoverFirstActivity(getApplicationContext());
        
        // Wait screen
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
            	
            	Intent intent = null;
                if (LoginActivity.class.toString().equals(firstActivity)) {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    PreferencesUtils.FIRST_ACTIVITY = LoginActivity.class;
                } else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    PreferencesUtils.FIRST_ACTIVITY = MainActivity.class;
                }

                startActivity(intent);            
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, firstRun ? 3000 : MainActivity.class.toString().equals(firstActivity) ? 500 : 800);
    }
 
    /**
     * Async Task to make http call
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	
            /*
             * Will make http call here This call will download required data
             * before launching the app 
             * example: 
             * 1. Downloading and storing in SQLite 
             * 2. Downloading images 
             * 3. Fetching and parsing the xml / json 
             * 4. Sending device information to server 
             * 5. etc.,
             */
            
        	/*JSONParser jsonParser = new JSONParser();
        	JSONObject json = jsonParser
                    .getJSONFromUrl("http://api.androidhive.info/game/game_stats.json");
 
            Log.e("Response: ", "> " + json);
 
            if (json != null) {
                try {
                    JSONObject jObj = json.getJSONObject("game_stat");
                    now_playing = jObj.getString("now_playing");
                    earned = jObj.getString("earned");
 
                    Log.e("JSON", "> " + now_playing + earned);
 
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
 
            }*/
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            
            // After completing http call
            // will close this activity and launch first main activity
            /*Intent i = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(i);*/
 
            // close this activity
            //finish();
        }
 
    }    
	
    @Override
    public void onBackPressed() {
        backCounter--;
        // mantem controle dos backpressed, no m�ximo 3 retornos, ap�s isso sai do app
        if (backCounter < 0) {
            backCounter = 0;
            if (this.calledFrom3Party) {
                super.onBackPressed();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}