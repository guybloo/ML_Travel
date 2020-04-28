package com.example.ml_travel;

import android.content.Context;
import android.os.Environment;

import com.github.ferortega.cf4j.data.DataSet;
import com.github.ferortega.cf4j.data.types.DataSetEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

/**
 * <p>This class implements the DataSet interface by random splitting the collaborative filtering ratings allocated
 * in a text file. Each line of the ratings file must have the following format:</p>
 * <pre>&lt;userId&gt;&lt;separator&gt;&lt;itemId&gt;&lt;separator&gt;&lt;rating&gt;</pre>
 * <p>Where &lt;separator&gt; is an special character that delimits ratings fields (semicolon by default).</p>
 * <p>Training and test ratings are selected randomly by the probability of an user and an item to belong to the test
 * set.</p>
 */
public class MyRandomSplitDataSet implements DataSet, Serializable {

    protected static final String DEFAULT_SEPARATOR = ";";

    /**
     * Raw stored ratings
     */
    protected ArrayList<DataSetEntry> ratings;

    /**
     * Raw stored test ratings
     */
    protected ArrayList<DataSetEntry> testRatings;


    /**
     * Generates a DataSet form a text file. The DataSet is loaded with a specific percentage of test items and test
     * users. This constructor allows to define an specific random seed to ensure the reproducibility of the
     * experiments.
     *
     * @param filename         File with the ratings.
     * @param testUsersPercent Percentage of users that will be of test.
     * @param testItemsPercent Percentage of items that will be of test.
     * @param seed             Seed applied to the random number generator.
     * @param separator        Separator char between file fields.
     * @throws IOException When the file is not accessible by the system with read permissions.
     */
    public MyRandomSplitDataSet(int file, double testUsersPercent, double testItemsPercent, String separator, long seed, Context context) throws IOException {

        Random rand = new Random(seed);

        ratings = new ArrayList<>();
        testRatings = new ArrayList<>();

        System.out.println("\nLoading dataset...");

        // Dataset reader
        BufferedReader datasetFile = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(file)));

        // Test selectors
        TreeMap<String, Boolean> testUsersFiltered = new TreeMap<>();
        TreeMap<String, Boolean> testItemsFiltered = new TreeMap<>();

        String line;
        int numLines = 0;
        while ((line = datasetFile.readLine()) != null) {
            if (line.equals(""))
                continue;
            //Loading feedback
            numLines++;
            if (numLines % 1000000 == 0) System.out.print(".");
            if (numLines % 10000000 == 0) System.out.println(numLines + " ratings");

            // Parse line
            String[] s = line.split(separator);
            String userId = s[0];
            String itemId = s[1];
            double rating = Double.parseDouble(s[2]);

            // Filtering entries.
            if (!testUsersFiltered.containsKey(userId)) {
                testUsersFiltered.put(userId, rand.nextFloat() <= testUsersPercent);
            }

            if (!testItemsFiltered.containsKey(itemId)) {
                testItemsFiltered.put(itemId, rand.nextFloat() <= testItemsPercent);
            }

            // Store rating
            if (testUsersFiltered.get(userId) && testItemsFiltered.get(itemId)) {
                testRatings.add(new DataSetEntry(userId, itemId, rating));
            } else {
                ratings.add(new DataSetEntry(userId, itemId, rating));
            }
        }

        datasetFile.close();
    }

    @Override
    public Iterator<DataSetEntry> getRatingsIterator() {
        return ratings.iterator();
    }

    @Override
    public Iterator<DataSetEntry> getTestRatingsIterator() {
        return testRatings.iterator();
    }

    @Override
    public int getNumberOfRatings() {
        return ratings.size();
    }

    @Override
    public int getNumberOfTestRatings() {
        return testRatings.size();
    }

    public void addEntry(String user, int item, double rating) {
        ratings.add(new DataSetEntry(user, String.valueOf(item), rating));
    }
}
