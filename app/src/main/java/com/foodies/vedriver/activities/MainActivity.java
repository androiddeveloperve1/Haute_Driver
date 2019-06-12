package com.foodies.vedriver.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.foodies.vedriver.BuildConfig;
import com.foodies.vedriver.R;
import com.foodies.vedriver.databinding.ActivityMainBinding;
import com.foodies.vedriver.databinding.NavHeaderMainBinding;
import com.foodies.vedriver.fragments.FragmentEarning;
import com.foodies.vedriver.fragments.FragmentProfile;
import com.foodies.vedriver.fragments.FragmentSupport;
import com.foodies.vedriver.fragments.FragmentTasks;
import com.foodies.vedriver.interfaces.ToolbarItemsClick;
import com.foodies.vedriver.model.UserModel;
import com.foodies.vedriver.prefes.MySharedPreference;
import com.foodies.vedriver.utils.FragmentTransactionUtils;
import com.foodies.vedriver.utils.Statusbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_tasks;
    private TextView tv_earnings;
    private TextView tv_profile;
    private TextView tv_help;
    private TextView tv_name;
    private TextView tv_mob;
    private TextView tv_app_version;
    private ImageView toolbar_refresh;
    private DrawerLayout drawer;
    ToolbarItemsClick toolClicks = new ToolbarItemsClick() {
        @Override
        public void onHumburgurCick(View v) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }

        @Override
        public void onRefreshClick(View v) {

        }
    };
    private UserModel userModel;
    private FragmentTasks fragmentTasks = new FragmentTasks();
    private FragmentEarning fragmentEarning = new FragmentEarning();
    private FragmentProfile fragmentProfile = new FragmentProfile();
    private FragmentSupport fragmentSupport = new FragmentSupport();
    private ActivityMainBinding binder;
    private Presenter p = new Presenter();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Statusbar.changeStatusColor(this);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_main);
        drawer = binder.drawerLayout;
        userModel = MySharedPreference.getInstance(this).getUser();
        Log.e("@@@@", "" + new Gson().toJson(userModel));
        binder.cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.onLogout(view);
            }
        });
        binder.includeLayout.setToolClick(toolClicks);
        View navView = binder.navView.getHeaderView(0);
        tv_tasks = navView.findViewById(R.id.tv_tasks);
        tv_app_version = findViewById(R.id.tv_app_version);
        toolbar_refresh = findViewById(R.id.toolbar_refresh);
        tv_app_version.setText("App ver " + BuildConfig.VERSION_NAME);
        tv_name = navView.findViewById(R.id.tv_name);
        tv_mob = navView.findViewById(R.id.tv_mob);
        tv_name.setText(userModel.getFname() + " " + userModel.getLname());
        tv_mob.setText(userModel.getMobile_no());
        tv_earnings = navView.findViewById(R.id.tv_earnings);
        tv_profile = navView.findViewById(R.id.tv_profile);
        tv_help = navView.findViewById(R.id.tv_help);
        NavHeaderMainBinding.bind(navView).setClickHandler(p);
        changeTheToolbarTitle("Tasks");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backAlert();
        }
    }

    void changeTheToolbarTitle(String title) {
        binder.includeLayout.setTitle(title);
    }

    void backAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure, want to exit from app ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finishAffinity();


            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    public class Presenter {
        public void onLogout(View view) {
            MySharedPreference.getInstance(MainActivity.this).clearMyPreference();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finishAffinity();
        }

        public void onNavigationClick(View view) {
            drawer.closeDrawer(GravityCompat.START);
            int id = view.getId();

            switch (id) {
                case R.id.tv_tasks:
                    tv_tasks.setTextColor(getResources().getColor(R.color.black));
                    tv_earnings.setTextColor(getResources().getColor(R.color.gray_text));
                    tv_profile.setTextColor(getResources().getColor(R.color.gray_text));
                    tv_help.setTextColor(getResources().getColor(R.color.gray_text));
                    changeTheToolbarTitle("Tasks");
                    toolbar_refresh.setVisibility(View.VISIBLE);
                    FragmentTransactionUtils.replaceFragmnet(MainActivity.this, R.id.container, fragmentTasks);
                    break;
                case R.id.tv_earnings:
                    tv_tasks.setTextColor(getResources().getColor(R.color.gray_text));
                    tv_earnings.setTextColor(getResources().getColor(R.color.black));
                    tv_profile.setTextColor(getResources().getColor(R.color.gray_text));
                    tv_help.setTextColor(getResources().getColor(R.color.gray_text));
                    changeTheToolbarTitle("Earnings");
                    toolbar_refresh.setVisibility(View.GONE);
                    FragmentTransactionUtils.replaceFragmnet(MainActivity.this, R.id.container, fragmentEarning);
                    break;
                case R.id.tv_profile:
                    tv_tasks.setTextColor(getResources().getColor(R.color.gray_text));
                    tv_earnings.setTextColor(getResources().getColor(R.color.gray_text));
                    tv_profile.setTextColor(getResources().getColor(R.color.black));
                    tv_help.setTextColor(getResources().getColor(R.color.gray_text));
                    changeTheToolbarTitle("Profile");
                    toolbar_refresh.setVisibility(View.GONE);
                    FragmentTransactionUtils.replaceFragmnet(MainActivity.this, R.id.container, fragmentProfile);
                    break;
                case R.id.tv_help:
                    tv_tasks.setTextColor(getResources().getColor(R.color.gray_text));
                    tv_earnings.setTextColor(getResources().getColor(R.color.gray_text));
                    tv_profile.setTextColor(getResources().getColor(R.color.gray_text));
                    tv_help.setTextColor(getResources().getColor(R.color.black));
                    changeTheToolbarTitle("Help and support");
                    toolbar_refresh.setVisibility(View.GONE);
                    FragmentTransactionUtils.replaceFragmnet(MainActivity.this, R.id.container, fragmentSupport);
                    break;

            }

        }

    }

}
