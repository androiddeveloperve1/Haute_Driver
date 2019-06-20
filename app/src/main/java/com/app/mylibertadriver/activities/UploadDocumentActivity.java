package com.app.mylibertadriver.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.PermissionConstants;
import com.app.mylibertadriver.databinding.ActivityUploadDocumentBinding;
import com.app.mylibertadriver.dialogs.ImageCaptureDialog;
import com.app.mylibertadriver.interfaces.ImageOrGalarySelector;
import com.app.mylibertadriver.model.DriverModel;
import com.app.mylibertadriver.permission.PermissionHandlerListener;
import com.app.mylibertadriver.permission.PermissionUtils;
import com.app.mylibertadriver.utils.MultipartUtils;
import com.app.mylibertadriver.viewmodeles.UploadDocViewModel;
import com.mindorks.paracamera.Camera;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;

public class UploadDocumentActivity extends AppCompatActivity {
    ActivityUploadDocumentBinding binder;
    private boolean isGalary = true;
    private ImageCaptureDialog mImageCaptureDialog;
    private Camera camera;
    private Bitmap imageCapturedDriveLicence;
    private Bitmap imageCapturedInsurance;
    private boolean isInsuranceImage = false;
    private UploadDocViewModel uploadDocViewModel;
    private String refNumber;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_upload_document);
        binder.setHandler(new Listener());
        uploadDocViewModel = ViewModelProviders.of(this).get(UploadDocViewModel.class);
        mImageCaptureDialog = new ImageCaptureDialog(this, listener);
    }

    void uploadSuccess() {
        final Dialog editDialog = new Dialog(this);
        editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editDialog.setContentView(R.layout.dialog_upload_success);
        TextView tv_ref_number = editDialog.findViewById(R.id.tv_ref_number);
        tv_ref_number.setText(refNumber);

        editDialog.findViewById(R.id.tv_okay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog.dismiss();
                startActivity(new Intent(UploadDocumentActivity.this, MainActivity.class));
                finishAffinity();
            }
        });

        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editDialog.getWindow().setGravity(Gravity.CENTER);
        editDialog.setCancelable(true);
        editDialog.show();

    }

    void handleImagesPick() {
        if (isGalary) {
            try {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PermissionConstants.GALLERY_REQUEST_CODE);
            } catch (Exception e) {
                Toast.makeText(this, "No Gallery Found", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionConstants.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setImageToView(camera.getCameraBitmap());
        } else if (requestCode == PermissionConstants.GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                if (mBitmap != null) {
                    setImageToView(mBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this.getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.getInstance().hendlePermission(UploadDocumentActivity.this, requestCode, permissions, grantResults);
    }

    void setImageToView(Bitmap mBitmap) {
        if (mBitmap != null) {
            if (isInsuranceImage) {
                binder.imgInsuranceDel.setVisibility(View.VISIBLE);
                imageCapturedInsurance = mBitmap;
                binder.imgLicencePreview.setImageResource(R.drawable.ic_upload_placeholder);
                binder.imgInsurancePreview.setImageBitmap(mBitmap);

            } else {
                binder.imgLicenceDel.setVisibility(View.VISIBLE);
                imageCapturedDriveLicence = mBitmap;
                binder.imgLicencePreview.setImageBitmap(mBitmap);
                binder.imgInsurancePreview.setImageResource(R.drawable.ic_upload_placeholder);
            }
        } else {
            if (isInsuranceImage) {
                binder.imgInsuranceDel.setVisibility(View.GONE);
                binder.imgInsurancePreview.setImageResource(R.drawable.ic_upload_placeholder);
                imageCapturedInsurance = null;
            } else {
                binder.imgLicenceDel.setVisibility(View.GONE);
                binder.imgLicencePreview.setImageResource(R.drawable.ic_upload_placeholder);
                imageCapturedDriveLicence = null;
            }
            Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
        }


    }

    public class Listener {

        public void onBack(View e) {
        }

        public void pickInsuranceImage(View e) {

            PermissionUtils.getInstance().checkAllPermission(UploadDocumentActivity.this, PermissionConstants.permissionArrayForImageCapture, new PermissionHandlerListener() {
                @Override
                public void onGrant() {
                    isInsuranceImage = true;
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

        public void pickDrivingLicImage(View e) {

            PermissionUtils.getInstance().checkAllPermission(UploadDocumentActivity.this, PermissionConstants.permissionArrayForImageCapture, new PermissionHandlerListener() {
                @Override
                public void onGrant() {
                    isInsuranceImage = false;

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


        public void delInsurance(View e) {
            binder.imgInsurancePreview.setImageResource(R.drawable.ic_upload_placeholder);
            imageCapturedInsurance = null;
            binder.imgInsuranceDel.setVisibility(View.GONE);
        }

        public void delLicence(View e) {
            binder.imgLicenceDel.setVisibility(View.GONE);
            binder.imgLicencePreview.setImageResource(R.drawable.ic_upload_placeholder);
            imageCapturedDriveLicence = null;
        }

        public void onUpload(View e) {
            if (imageCapturedDriveLicence != null && imageCapturedInsurance != null) {
                MultipartBody.Part[] part = new MultipartBody.Part[]{MultipartUtils.createFile(UploadDocumentActivity.this, imageCapturedInsurance, "insurance"), MultipartUtils.createFile(UploadDocumentActivity.this, imageCapturedDriveLicence, "driverlicense")};
                uploadDocViewModel.uploadImage(UploadDocumentActivity.this, part).observe(UploadDocumentActivity.this, new Observer<DriverModel>() {
                    @Override
                    public void onChanged(DriverModel driverModel) {
                        refNumber = driverModel.getEmployee_id();
                        uploadSuccess();
                    }
                });
            } else {
                Toast.makeText(UploadDocumentActivity.this, "Please select the document to upload", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
