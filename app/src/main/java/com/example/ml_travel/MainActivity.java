package com.example.ml_travel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.github.ferortega.cf4j.data.DataModel;
import com.github.ferortega.cf4j.data.DataSet;
import com.github.ferortega.cf4j.data.Item;
import com.github.ferortega.cf4j.data.RandomSplitDataSet;
import com.github.ferortega.cf4j.qualityMeasure.QualityMeasure;
import com.github.ferortega.cf4j.qualityMeasure.prediction.MSE;
import com.github.ferortega.cf4j.recommender.matrixFactorization.NMF;
import com.github.ferortega.cf4j.recommender.matrixFactorization.PMF;
import com.github.ferortega.cf4j.recommender.matrixFactorization.SVDPlusPlus;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private mlAlgo alg;
    private HashMap<Integer, Trip> items;
    private String user_id;
    private ArrayList<Rate> userRatings;
    private ArrayList<Rate> userRatingsForNow;
    private Trip search;
    private LocationTracker location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        location = new LocationTracker(this);
        search = new Trip();
        items = new HashMap<>();
        alg = new mlAlgo(this);
        try {
            load_items_index();
        } catch (IOException ignored) {
        }
        user_id = alg.getNewId();


        findViewById(R.id.btn_new_try).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                alg = new mlAlgo(getApplicationContext());
                user_id = alg.getNewId();


                Intent intent = new Intent(getApplicationContext(), FirstRating.class);
                intent.putExtra("list", alg.getAllItemsByPopularity(false));
                intent.putExtra("items_idx", items);
                startActivityForResult(intent, 100);            }
        });

        Intent intent = new Intent(this, FirstRating.class);
        intent.putExtra("list", alg.getAllItemsByPopularity(false));
        intent.putExtra("items_idx", items);
        startActivityForResult(intent, 100);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void forNow() {
        if(userRatings == null)
            return;
        location.startTracking();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                LocationInfo info = location.getInfo();
                while (location.getInfo().getAccuracy() > 50 || location.getInfo().getAccuracy() == 0) {
                    info = location.getInfo();
                }
                location.stopTracking();
                boolean south = false, center = false, north = false;
                if (info.getLatitude() > 29.4 && info.getLatitude() < 31.4)
                    south = true;
                if (info.getLatitude() > 31.2 && info.getLatitude() < 32.8)
                    center = true;
                if (info.getLatitude() > 32.3 && info.getLatitude() < 33.4)
                    north = true;

                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int month = calendar.get(Calendar.MONTH) + 1;

                ArrayList<Rate> results = new ArrayList<>();
                for (Rate rate : userRatings) {
                    Trip trip = items.get(rate.item);
                    if(month >=5 && month<=9)
                        if(!trip.summer)
                            continue;
                    if(month >=8 && month<=12)
                        if(!trip.spring)
                            continue;
                    if(month >=11 || month<=3)
                        if(!trip.winter)
                            continue;
                    if(month >=2 && month<=6)
                        if(!trip.fall)
                            continue;

                    if(north)
                        if(trip.area.contains("גליל") || trip.area.contains("חיפה"))
                            results.add(rate);
                    if(center)
                        if(trip.area.contains("השפלה") || trip.area.contains("השרון")|| trip.area.contains("החוף")|| trip.area.contains("שומרון")|| trip.area.contains("ירושלים"))
                            results.add(rate);
                    if(south)
                        if(trip.area.contains("המלח") || trip.area.contains("הנגב"))
                            results.add(rate);
                }
                userRatingsForNow = results;
                openResults(userRatings, userRatingsForNow);
            }
        });

    }
    private void load_items_index() throws IOException {

        BufferedReader datasetFile = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.items)));
        String line;

        while ((line = datasetFile.readLine()) != null) {
            if (line.equals(""))
                continue;

            Trip t = new Trip(line);
            items.put(t.index, t);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                HashMap<Integer, Integer> ratings = (HashMap<Integer, Integer>) data.getSerializableExtra("ratings");
                Set<Integer> keys = ratings.keySet();
                for (int key : ratings.keySet()) {
                    alg.addRate(user_id, key, ratings.get(key));
                }
                AsyncAlgFit fit = new AsyncAlgFit(this);
                fit.execute();
            }
        }
    }

    public void openResults(ArrayList<Rate> all,ArrayList<Rate> forNow) {
        Intent intent = new Intent(this, Results.class);
        intent.putExtra("items", items);
        intent.putExtra("all", all);
        intent.putExtra("for_now", forNow);
        startActivity(intent);
    }

    private class AsyncAlgFit extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public AsyncAlgFit(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("אחי, תן לי רגע לחשב פה חישובים");

            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {
            alg.fitAlg(10,100);
            alg.predictUserRates(user_id);
            userRatings = alg.getUserRatesByRate(false);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            forNow();
        }
    }
}
