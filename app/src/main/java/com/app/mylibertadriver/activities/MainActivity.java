package com.app.mylibertadriver.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.app.mylibertadriver.BuildConfig;
import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.ActivityMainBinding;
import com.app.mylibertadriver.databinding.NavHeaderMainBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.fragments.FragmentEarning;
import com.app.mylibertadriver.fragments.FragmentProfile;
import com.app.mylibertadriver.fragments.FragmentSupport;
import com.app.mylibertadriver.fragments.FragmentTasks;
import com.app.mylibertadriver.interfaces.ToolbarItemsClick;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.DriverModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.prefes.MySharedPreference;
import com.app.mylibertadriver.utils.FragmentTransactionUtils;
import com.app.mylibertadriver.utils.Statusbar;
import com.app.mylibertadriver.worker.UploadWorker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Inject
    APIInterface apiInterface;

    private TextView tv_tasks;
    private TextView tv_earnings;
    private TextView tv_profile;
    private TextView tv_help;
    private TextView tv_name;
    private TextView tv_mob;
    private TextView tv_app_version;
    private ImageView toolbar_refresh;
    private ImageView img_user;
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
            fragmentTasks.getTask();
            //startActivity(new Intent(MainActivity.this, TrackingActivity.class));
        }
    };

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
        img_user = navView.findViewById(R.id.img_user);

        tv_earnings = navView.findViewById(R.id.tv_earnings);
        tv_profile = navView.findViewById(R.id.tv_profile);
        tv_help = navView.findViewById(R.id.tv_help);
        NavHeaderMainBinding.bind(navView).setClickHandler(p);
        FragmentTransactionUtils.replaceFragmnet(MainActivity.this, R.id.container, fragmentTasks);
        changeTheToolbarTitle("Tasks");

    }

    @Override
    protected void onResume() {
        super.onResume();
        DriverModel driverModel = MySharedPreference.getInstance(this).getUser();
        Log.e("@@@@", "" + new Gson().toJson(driverModel));
        tv_name.setText(driverModel.getFname() + " " + driverModel.getLname());
        tv_mob.setText(driverModel.getMobile_no());
        updateProfile(driverModel.getAvtar());
    }


    public void updateProfile(String img) {
        Picasso.with(this).load(img).placeholder(R.drawable.ic_profile_placeholder).into(img_user);
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

    private void logOut() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(MainActivity.this);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.doLogout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(MainActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel response) {
                        progressDialog.dismiss();
                        Log.e("@@@@@@@@@@@Success", new Gson().toJson(response));
                        if (response.getStatus().equals("200")) {
                            MySharedPreference.getInstance(MainActivity.this).clearMyPreference();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finishAffinity();
                        } else {
                            ResponseDialog.showErrorDialog(MainActivity.this, response.getMessage());
                        }
                    }
                });
    }

    private void getUserProfile() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(MainActivity.this);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.getUserProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<DriverModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(MainActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<DriverModel> response) {
                        progressDialog.dismiss();
                        Log.e("@@@@@@@@@@@Success", new Gson().toJson(response));
                        if (response.getStatus().equals("200")) {
                            MySharedPreference.getInstance(MainActivity.this).setUser(response.getData());
                        } else {
                            ResponseDialog.showErrorDialog(MainActivity.this, response.getMessage());
                        }
                    }
                });
    }

    void startWorkes() {
        OneTimeWorkRequest req = new OneTimeWorkRequest.Builder(UploadWorker.class).build();
        WorkManager.getInstance().enqueue(req);
        WorkManager.getInstance().getWorkInfoByIdLiveData(req.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                Log.e("@@@@@@@", workInfo.getState().name());

            }
        });

    }

    public class Presenter {
        public void onLogout(View view) {
            logOut();

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
