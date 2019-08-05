package de.realliferpg.app.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.fragments.CBSFragment;
import de.realliferpg.app.fragments.ChangelogFragment;
import de.realliferpg.app.fragments.ErrorFragment;
import de.realliferpg.app.fragments.ImprintFragment;
import de.realliferpg.app.fragments.InfoFragment;
import de.realliferpg.app.fragments.MainFragment;
import de.realliferpg.app.fragments.MarketFragment;
import de.realliferpg.app.fragments.PlayerBuildingsFragment;
import de.realliferpg.app.fragments.PlayerDonationFragment;
import de.realliferpg.app.fragments.PlayerFragment;
import de.realliferpg.app.fragments.PlayerStatsFragment;
import de.realliferpg.app.fragments.PlayersListFragment;
import de.realliferpg.app.fragments.RechnerGunsFragment;
import de.realliferpg.app.fragments.RechnerSellFragment;
import de.realliferpg.app.fragments.RechnerSumFragment;
import de.realliferpg.app.fragments.RechnerToolFragment;
import de.realliferpg.app.fragments.RechnerVehiclesFragment;
import de.realliferpg.app.fragments.SettingsFragment;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.helper.PreferenceHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.PlayerInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentInteractionInterface, RequestCallbackInterface {

    private Fragment currentFragment;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Singleton.getInstance().setContext(getApplicationContext());

        PreferenceHelper preferenceHelper = new PreferenceHelper();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        // Load Main fragment
        if (preferenceHelper.getPlayerAPIToken().equals("")) {
            switchFragment(new SettingsFragment());

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle(getString(R.string.str_noApiToken));
            alertDialog.setMessage(getString(R.string.str_noApiTokenInfo));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else {
            switchFragment(new MainFragment());
        }

        View header = navigationView.getHeaderView(0);
        ImageButton imageButton = header.findViewById(R.id.ib_nav_scanCode);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsFragment settingsFragment = new SettingsFragment();
                switchFragment(settingsFragment);
            }
        });
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        DrawerLayout drawer = findViewById(R.id.layout_main);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // TODO beim Zurückgehen laden die Controls nicht, daher wird vorerst der "Zurück"-Button disabled
            /*
            if (count == 0) {
                super.onBackPressed();
            } else if (count == 1) {
                finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
            */
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_imprint:
                switchFragment(new ImprintFragment());
                break;
            case R.id.nav_changelog:
                switchFragment(new ChangelogFragment());
                break;
            case R.id.nav_overview:
                switchFragment(new MainFragment());
                break;
            case R.id.nav_info:
                switchFragment(new InfoFragment());
                break;
            case R.id.nav_player:
                switchFragment(new PlayerFragment());
                break;
            case R.id.nav_playerslist:
                switchFragment(new PlayersListFragment());
                break;
            case R.id.nav_marketprices:
                switchFragment(new MarketFragment());
                break;
            case R.id.nav_cbs:
                switchFragment(new CBSFragment());
            case R.id.nav_rechnertool:
                switchFragment(new RechnerToolFragment());
                break;
            case R.id.nav_settings:
                switchFragment(new SettingsFragment());
                break;
            case R.id.nav_website: {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.realliferpg.de"));
                startActivity(browserIntent);
                break;
            }
            case R.id.nav_forum: {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forum.realliferpg.de"));
                startActivity(browserIntent);
                break;
            }
            case R.id.nav_twitter: {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/A3ReallifeRPG"));
                startActivity(browserIntent);
                break;
            }
            case R.id.nav_facebook: {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/RealLifeRPGCommunity/"));
                startActivity(browserIntent);
                break;
            }
        }
        return true;
    }

    public void switchFragment(Fragment newFragment) {

        NavigationView navigationView = findViewById(R.id.nav_view);

        if (newFragment instanceof SettingsFragment) {
            navigationView.setCheckedItem(R.id.nav_settings);
        } else if (newFragment instanceof ErrorFragment) {
            for (int i = 0; i < navigationView.getMenu().size(); i++) {
                navigationView.getMenu().getItem(i).setChecked(false);
            }
        }

        Singleton.getInstance().dismissSnackbar();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.include_main_content, newFragment);

        boolean addToBack = true;

        if (currentFragment == null && newFragment instanceof MainFragment) {
            addToBack = false;
        }

        if (addToBack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();

        DrawerLayout drawer = findViewById(R.id.layout_main);
        drawer.closeDrawer(GravityCompat.START);
        currentFragment = newFragment;
    }

    @Override
    public void onFragmentInteraction(Class type, Uri uri) {

        switch (uri.toString()) {
            case "open_settings":
                switchFragment(new SettingsFragment());
                break;
            case "open_error":
                switchFragment(new ErrorFragment());
                break;
            case "update_login_state":
                PlayerInfo playerInfo = Singleton.getInstance().getPlayerInfo();

                ImageView ivProfilePic = findViewById(R.id.iv_nav_icon);
                TextView tvInfo = findViewById(R.id.tv_nav_info);
                TextView tvHead = findViewById(R.id.tv_nav_head);

                Picasso.get().load(playerInfo.avatar_full).into(ivProfilePic);
                tvHead.setText(R.string.str_logged_in);
                tvInfo.setText(playerInfo.name);
                break;
        }

        if (type.equals(PlayerFragment.class)) {
            switch (uri.toString()) {
                case "fragment_player_change_to_stats": {
                    changePlayerFragment(new PlayerStatsFragment());
                    break;
                }
                case "fragment_player_change_to_donation": {
                    changePlayerFragment(new PlayerDonationFragment());
                    break;
                }
                case "fragment_player_change_to_buildings": {
                    changePlayerFragment(new PlayerBuildingsFragment());
                    break;
                }
            }
        }

        if (type.equals(RechnerToolFragment.class)){
            switch (uri.toString()){
                case "fragment_calc_change_to_vehicles": {
                    changeRechnerFragment(new RechnerVehiclesFragment());
                    break;
                }
                case "fragment_calc_change_to_guns": {
                    changeRechnerFragment(new RechnerGunsFragment());
                    break;
                }
                case "fragment_calc_change_to_sell": {
                    changeRechnerFragment(new RechnerSellFragment());
                    break;
                }
                case "fragment_calc_change_to_sum": {
                    changeRechnerFragment(new RechnerSumFragment());
                    break;
                }

            }
        }

    }

    void changePlayerFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.include_player_content, fragment);
        transaction.commit();
    }

    void changeRechnerFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.include_calc_content, fragment);
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.getAction().equals("com.google.zxing.client.android.SCAN")) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    Singleton.getInstance().setScanResponse(result.getContents());
                    if(currentFragment instanceof SettingsFragment){
                        ((SettingsFragment) currentFragment).onFragmentInteraction(MainActivity.class, Uri.parse("scan_response"));
                    }

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("pref_api_token", result.getContents());
                    editor.apply();
                }
            }
        }
    }

    @Override
    public void onResponse(RequestTypeEnum type, JSONObject response) {
        try{
            ApiHelper apiHelper = new ApiHelper(this);
            boolean result = apiHelper.handleResponse(type,response);

            if(result){
                ((CallbackNotifyInterface) currentFragment).onCallback(type);
            }else{
                // TODO handle this case, should not happen but you never know
            }
        }catch (Exception e){
            Singleton.getInstance().setErrorMsg(e.getMessage());
            switchFragment(new ErrorFragment());
        }
    }
}
