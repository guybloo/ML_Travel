package com.example.ml_travel;

import android.content.Context;

import com.github.ferortega.cf4j.data.DataModel;
import com.github.ferortega.cf4j.data.Item;
import com.github.ferortega.cf4j.data.RandomSplitDataSet;
import com.github.ferortega.cf4j.data.User;
import com.github.ferortega.cf4j.qualityMeasure.QualityMeasure;
import com.github.ferortega.cf4j.qualityMeasure.prediction.MSE;
import com.github.ferortega.cf4j.recommender.matrixFactorization.NMF;
import com.github.ferortega.cf4j.recommender.matrixFactorization.PMF;
import com.github.ferortega.cf4j.recommender.matrixFactorization.SVDPlusPlus;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class mlAlgo implements Serializable {

    private double testUsers;
    private double testItems;
    private String dataFilePath;
    private Context context;
    private DataModel model;
    private MyRandomSplitDataSet set;
    private ArrayList<Rate> userRatings;
    private SVDPlusPlus svd;

    public String getNewId()
    {
        int id = model.getNumberOfUsers();
        while(model.findUserIndex(String.valueOf(id)) > 0)
        {
        }
        return String.valueOf(id);
    }
    public mlAlgo(Context cnt) {
        this(cnt, 0, 0);
    }

    public mlAlgo(Context cnt, double tu, double ti) {
        this(cnt, tu, ti, "data.csv");
    }

    public mlAlgo(Context cnt, double tu, double ti, String dataF) {
        testUsers = tu;
        testItems = ti;
        dataFilePath = dataF;
        context = cnt;
        userRatings = new ArrayList<>();
        loadSet();
    }

    public void loadSet() {
        String separator = ",";
        long seed = 43;
        try {
            set = new MyRandomSplitDataSet(R.raw.data, testUsers, testItems, separator, seed, context);
        } catch (IOException ignored) {
        }
        model = new DataModel(set);
    }

    public void predictUserRates(String id)
    {
        int idx = model.findUserIndex(id);
        for (int i = 0; i < model.getNumberOfItems(); i++) {
            userRatings.add(new Rate(id, i, svd.predict(idx, i)));
        }
    }

    public ArrayList<Rate> getUserPredictedRatings()
    {
        return userRatings;
    }

    public void fitAlg(int numF, int numI) {
        model = new DataModel(set);
        svd = new SVDPlusPlus(model, numF, numI);
        svd.fit();
    }

    public void addRate(String id, int item, double rate) {
        set.addEntry(id, item, rate);
    }

    public ArrayList<Rate> getUserRatesByRate(final boolean ascend) {
        ArrayList<Rate> temp = new ArrayList<>(userRatings);

        Collections.sort(temp, new Comparator<Rate>() {
            @Override
            public int compare(Rate u1, Rate u2) {
                if (ascend) {
                    return Double.compare(u1.rate, u2.rate);
                }
                return Double.compare(u2.rate, u1.rate);
            }
        });

        return temp;
    }

    public void checkAlgs() {
        long seed = 43;

        SVDPlusPlus svd = new SVDPlusPlus(model, 10, 100);
        svd.fit();

        PMF pmf = new PMF(model, 10, 100, 0.1, 0.01, seed);
        pmf.fit();

        NMF nmf = new NMF(model, 10, 100, seed);
        nmf.fit();

        QualityMeasure mse;

        mse = new MSE(pmf);
        System.out.println("\nMSE (PMF): " + mse.getScore());

        mse = new MSE(nmf);
        System.out.println("MSE (NMF): " + mse.getScore());

        mse = new MSE(svd);
        System.out.println("\nMSE (SVD): " + mse.getScore());
    }

    public ArrayList<Item> getAllItemsByPopularity(final boolean ascend)
    {
        Item[] it = model.getItems();
        ArrayList<Item> list = new ArrayList<Item>(Arrays.asList(it));

        Collections.sort(list, new Comparator<Item>() {
            @Override
            public int compare(Item u1, Item u2) {
                if (ascend) {
                    return Double.compare(u1.getRatingAverage(), u2.getRatingAverage());
                }
                return Double.compare(u2.getRatingAverage(), u1.getRatingAverage());
            }
        });

        Collections.sort(list, new Comparator<Item>() {
            @Override
            public int compare(Item u1, Item u2) {
                if (ascend) {
                    return Double.compare(u1.getNumberOfRatings(), u2.getNumberOfRatings());
                }
                return Double.compare(u2.getNumberOfRatings(), u1.getNumberOfRatings());
            }
        });

        return list;
    }
}
