package de.realliferpg.app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.fragments.ChangelogFragment;
import de.realliferpg.app.fragments.ImprintFragment;
import de.realliferpg.app.fragments.InfoFragment;
import de.realliferpg.app.fragments.MainFragment;
import de.realliferpg.app.objects.PlayerInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ImprintFragment.OnFragmentInteractionListener,
        ChangelogFragment.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener, InfoFragment.OnFragmentInteractionListener {


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Singleton.getInstance().setContext(getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        // Load Main fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MainFragment imprintFragment = new MainFragment();
        transaction.replace(R.id.include_main_content, imprintFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_imprint) {
            ImprintFragment imprintFragment = new ImprintFragment();
            transaction.replace(R.id.include_main_content, imprintFragment);
        } else if (id == R.id.nav_changelog) {
            ChangelogFragment changelogFragment = new ChangelogFragment();
            transaction.replace(R.id.include_main_content, changelogFragment);
        } else if (id == R.id.nav_overview) {
            MainFragment mainFragment = new MainFragment();
            transaction.replace(R.id.include_main_content, mainFragment);
        } else if (id == R.id.nav_info) {
            InfoFragment infoFragment = new InfoFragment();
            transaction.replace(R.id.include_main_content, infoFragment);
        } else if (id == R.id.nav_website) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.realliferpg.de"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_forum) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forum.realliferpg.de"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_twitter) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/A3ReallifeRPG"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_facebook) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/RealLifeRPGCommunity/"));
            startActivity(browserIntent);
        }

        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("MainActivity","Fragment interaction");

        switch (uri.toString()){
            case "update_login_state":{
                PlayerInfo playerInfo = Singleton.getInstance().getPlayerInfo();

                TextView tvInfo = findViewById(R.id.tv_nav_info);
                TextView tvHead = findViewById(R.id.tv_nav_head);

                tvHead.setText(R.string.str_logged_in);
                tvInfo.setText(playerInfo.name);

                break;
            }
        }
    }
}
