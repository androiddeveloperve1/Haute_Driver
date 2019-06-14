package com.foodies.vedriver.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.foodies.vedriver.R;
import com.foodies.vedriver.activities.MainActivity;
import com.foodies.vedriver.activities.MyApplication;
import com.foodies.vedriver.activities.UploadDocumentActivity;
import com.foodies.vedriver.constants.PermissionConstants;
import com.foodies.vedriver.databinding.FragmentProfileBinding;
import com.foodies.vedriver.databinding.FragmentTasksBinding;
import com.foodies.vedriver.dialogs.ImageCaptureDialog;
import com.foodies.vedriver.dialogs.ResponseDialog;
import com.foodies.vedriver.interfaces.ImageOrGalarySelector;
import com.foodies.vedriver.model.ApiResponseModel;
import com.foodies.vedriver.model.UserModel;
import com.foodies.vedriver.network.APIInterface;
import com.foodies.vedriver.permission.PermissionHandlerListener;
import com.foodies.vedriver.permission.PermissionUtils;
import com.foodies.vedriver.prefes.MySharedPreference;
import com.foodies.vedriver.utils.MultipartUtils;
import com.mindorks.paracamera.Camera;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class FragmentProfile extends Fragment {


    @Inject
    APIInterface apiInterface;

    private UserModel userData;
    private FragmentProfileBinding binder;
    private ImageCaptureDialog mImageCaptureDialog;
    private boolean isGalary = true;
    private Camera camera;
    private Bitmap capturedImage;
    private ImageOrGalarySelector listener = new ImageOrGalarySelector() {
        @Override
        public void imageSelect() {
            isGalary = false;
            mImageCaptureDialog.dismissDoalog();
            handleImagesPick();
        }

        @Override
        public void gallerySelect() {
            isGalary = true;
            mImageCaptureDialog.dismissDoalog();
            handleImagesPick();
        }
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binder.setClickHandler(new Presenter());
        mImageCaptureDialog = new ImageCaptureDialog(getActivity(), listener);
        userData = MySharedPreference.getInstance(getActivity()).getUser();
        binder.tvName.setText(userData.getFname() + " " + userData.getLname());
        binder.tvMob.setText(userData.getMobile_no());
        Picasso.with(getActivity()).load(userData.getAvtar()).placeholder(R.drawable.ic_profile_placeholder).into(binder.imgProfile);
        View view = binder.getRoot();
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.getInstance().hendlePermissionForFragment(getActivity(), requestCode, permissions, grantResults);
    }

    void handleImagesPick() {
        if (isGalary) {
            try {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PermissionConstants.GALLERY_REQUEST_CODE);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "No Gallery Found", Toast.LENGTH_SHORT).show();
            }
        } else {
            camera = new Camera.Builder()
                    .resetToCorrectOrientation(true)
                    .setTakePhotoRequestCode(PermissionConstants.CAMERA_REQUEST_CODE)
                    .setDirectory("pics")
                    .setName("capture_image")
                    .setImageFormat(Camera.IMAGE_JPEG)
                    .setCompression(75)
                    .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                    .build(this);
            try {
                camera.takePicture();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionConstants.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setImageToView(camera.getCameraBitmap());
        } else if (requestCode == PermissionConstants.GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                if (mBitmap != null) {
                    setImageToView(mBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    void setImageToView(Bitmap mBitmap) {
        capturedImage = mBitmap;
        if (mBitmap != null) {
            binder.imgProfile.setImageBitmap(capturedImage);
            uploadImage(MultipartUtils.createFile(getActivity(), capturedImage, "driver_avtar"));

        } else {
            Toast.makeText(getActivity(), "Picture not taken!", Toast.LENGTH_SHORT).show();
        }

    }
    private void uploadImage(MultipartBody.Part part) {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(getActivity());
        ((MyApplication) getActivity().getApplication()).getConfiguration().inject(this);
        apiInterface.uploadProfile(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<UserModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(getActivity(), throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<UserModel> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            MySharedPreference.getInstance(getActivity()).setUser(response.getData());

                            ((MainActivity) getActivity()).updateProfile(response.getData().getAvtar());
                        } else {
                            ResponseDialog.showErrorDialog(getActivity(), response.getMessage());
                        }

                    }
                });
    }

    public class Presenter {
        public void onCollpase(View view) {
            binder.clAccountInfo.setVisibility(View.GONE);
            binder.ivExpand.setVisibility(View.VISIBLE);
        }

        public void onExpand(View view) {
            binder.clAccountInfo.setVisibility(View.VISIBLE);
            binder.ivExpand.setVisibility(View.GONE);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onImageCapture(View view) {

            PermissionUtils.getInstance().checkAllPermissionFragment(getActivity(), PermissionConstants.permissionArrayForImageCapture, new PermissionHandlerListener() {
                @Override
                public void onGrant() {
                    mImageCaptureDialog.showDoalog();
                }

                @Override
                public void onReject(ArrayList<String> remainsPermissonList) {
                    Log.e("@@@@@@@@@@@", "Permission Rejected");
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onRequestPermissionNow(String[] arr, int req) {
                    requestPermissions(PermissionConstants.permissionArrayForImageCapture, PermissionUtils.RequestCode);
                }

                @Override
                public void onRationalPermission(ArrayList<String> rationalPermissonList) {
                    super.onRationalPermission(rationalPermissonList);
                }
            });
        }


    }
}