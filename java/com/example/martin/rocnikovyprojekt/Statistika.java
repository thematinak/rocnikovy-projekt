package com.example.martin.rocnikovyprojekt;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import Tabs.PlaceholderFragment;
import classes.zapas.Hit;
import classes.zapas.Match;
import classes.zapas.Vymena;

public class Statistika extends AppCompatActivity {

    public static final int NUM_TABS = 6;
    public static final String STAT_FILE = "Stat.tmp";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Match match;
    private Vymena vymena;
    private boolean playable;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistika);


        File parentFile = this.getFilesDir();
        File f = new File(parentFile, "TMP.tmp");
        if (f.exists()) {
            loadFromfile(f);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistika, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Statistika Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.martin.rocnikovyprojekt/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Statistika Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.martin.rocnikovyprojekt/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment x = PlaceholderFragment.newInstance(position + 1);
            x.getArguments().putParcelable("data", match.getParcelable());
            return x;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return NUM_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Serve";
                case 1:
                    return "Serve Map";
                case 2:
                    return "Forehand";
                case 3:
                    return "Forehand Map";
                case 4:
                    return "Backhand";
                case 5:
                    return "Backhand Map";
                default:
                    return "error";
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        File parentFile = this.getFilesDir();
        File f = new File(parentFile, STAT_FILE);
        if (f.exists()) {
            f.delete();
        }
    }

    private void loadFromfile(File f) {
        Scanner sc = null;
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String p1 = sc.next();
        String p2 = sc.next();

        int numberOfsets = sc.nextInt();
        int serve = sc.nextInt();

        match = new Match(p1, p2, numberOfsets, serve, null, null, null, null, null);
        vymena = new Vymena(match.getServe(), match.getInGameScore());
        Intent i = getIntent();


        while (sc.hasNext()) {
            float x = sc.nextFloat() / 10000;
            float y = sc.nextFloat() / 10000;
            String type = sc.next();
            boolean winner = sc.nextBoolean();
            Paint p = new Paint();
            Hit hit = new Hit(x, y, p);
            hit.setType(type);
            hit.setWinner(winner);
            float sirka = i.getFloatExtra("sirka", -1);
            float vyska = i.getFloatExtra("vyska", -1);
            if (hit.isOut(sirka, vyska, vymena) || type.equals("net")) {
                hit.setOut();
                p.setColor(Color.RED);
            }
            if (winner) {
                p.setColor(Color.GREEN);
            }

            if (vymena.add(hit)) {
                match.add(vymena);
                vymena = new Vymena(match.getServe(), match.getInGameScore());
            }
        }
        sc.close();
        playable = !match.isEndOfMath();
    }

}
