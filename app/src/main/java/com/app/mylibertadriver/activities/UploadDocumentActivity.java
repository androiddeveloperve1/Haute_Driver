package com.app.mylibertadriver.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
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
import com.app.mylibertadriver.prefes.MySharedPreference;
import com.app.mylibertadriver.utils.MultipartUtils;
import com.app.mylibertadriver.viewmodeles.UploadDocViewModel;
import com.mindorks.paracamera.Camera;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.MultipartBody;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public class UploadDocumentActivity extends AppCompatActivity {
    private static final String LICENCE = "1";
    private static final String INSURANCE = "0";
    ActivityUploadDocumentBinding binder;
    private boolean isGalary = true;
    private ImageCaptureDialog mImageCaptureDialog;
    private Camera camera;
    private Bitmap imageCapturedDriveLicence;
    private Bitmap imageCapturedInsurance;
    private boolean isInsuranceImage = false;
    private UploadDocViewModel uploadDocViewModel;
    private String refNumber;
    private DriverModel driverModel;
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
        driverModel = MySharedPreference.getInstance(this).getUser();
        if (driverModel.getIs_document_upload().equals("1")) {
            binder.statusInsurance.setVisibility(View.VISIBLE);
            binder.statusDriver.setVisibility(View.VISIBLE);
            handleLayout();
        }

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
                binder.imgInsurancePreview.setImageBitmap(mBitmap);

            } else {
                binder.imgLicenceDel.setVisibility(View.VISIBLE);
                imageCapturedDriveLicence = mBitmap;
                binder.imgLicencePreview.setImageBitmap(mBitmap);
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

    void handleLayout() {
        if (driverModel.getDriverlicense().getStatus().equals("0")) {
            binder.statusDriver.setBackgroundResource(R.drawable.pending_bg);
            binder.statusDriver.setText("Pending");
        } else if (driverModel.getDriverlicense().getStatus().equals("1")) {
            binder.statusDriver.setBackgroundResource(R.drawable.approved_bg);
            binder.statusDriver.setText("Approved");
        } else if (driverModel.getDriverlicense().getStatus().equals("2")) {
            binder.statusDriver.setBackgroundResource(R.drawable.reject_bg);
            binder.statusDriver.setText("Reject");
        }
        Picasso.with(UploadDocumentActivity.this).load(driverModel.getDriverlicense().getPath()).resize(200, 200).onlyScaleDown().placeholder(R.drawable.placeholder_squre).into(getTarget(driverModel.getDriverlicense().getPath(),LICENCE));


        //new DownloadImage().execute(driverModel.getDriverlicense().getPath(), LICENCE);
        if (driverModel.getInsurance().getStatus().equals("0")) {
            binder.statusInsurance.setBackgroundResource(R.drawable.pending_bg);
            binder.statusInsurance.setText("Pending");
        } else if (driverModel.getInsurance().getStatus().equals("1")) {
            binder.statusInsurance.setBackgroundResource(R.drawable.approved_bg);
            binder.statusInsurance.setText("Approved");
        } else if (driverModel.getInsurance().getStatus().equals("2")) {
            binder.statusInsurance.setBackgroundResource(R.drawable.reject_bg);
            binder.statusInsurance.setText("Reject");
        }

        Picasso.with(UploadDocumentActivity.this).load(driverModel.getInsurance().getPath()).resize(200, 200).onlyScaleDown().placeholder(R.drawable.placeholder_squre).into(getTarget(driverModel.getInsurance().getPath(),INSURANCE));

        //new DownloadImage().execute(driverModel.getInsurance().getPath(), INSURANCE);
    }

    private Target getTarget(final String url,final String status) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap result, Picasso.LoadedFrom from) {

                Log.e("@@@@@@", "Image loded");
                if (status.equals(INSURANCE)) {
                    imageCapturedInsurance = result;

                    binder.imgInsurancePreview.setImageBitmap(imageCapturedInsurance);
                    if (!driverModel.getInsurance().getStatus().equals("1")) {
                        binder.rlInsurance.setClickable(true);
                        binder.imgInsuranceDel.setVisibility(View.VISIBLE);
                    } else {
                        binder.rlInsurance.setClickable(false);
                        binder.imgInsuranceDel.setVisibility(View.GONE);
                    }

                } else if (status.equals(LICENCE)) {
                    imageCapturedDriveLicence = result;
                    binder.imgLicencePreview.setImageBitmap(imageCapturedDriveLicence);
                    if (!driverModel.getDriverlicense().getStatus().equals("1")) {
                        binder.rlDriver.setClickable(true);
                        binder.imgLicenceDel.setVisibility(View.VISIBLE);
                    } else {
                        binder.rlDriver.setClickable(false);
                        binder.imgLicenceDel.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.e("@@@@@@", "Image failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.e("@@@@@@", "prepare Image loded");

            }
        };
        return target;
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
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onRequestPermissionNow(String[] arr, int req) {
                    requestPermissions(PermissionConstants.permissionArrayForImageCapture, PermissionUtils.RequestCode);
                }

                @Override
                public void onRationalPermission(ArrayList<String> rationalPermissonList) {
                    PermissionUtils.firePerimisionActivity(UploadDocumentActivity.this);


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
                MultipartBody.Part[] part = new MultipartBody.Part[]{MultipartUtils.createFile(UploadDocumentActivity.this, imageCapturedInsurance, "insurance", "insurance.jpg"), MultipartUtils.createFile(UploadDocumentActivity.this, imageCapturedDriveLicence, "driverlicense", "drivimglic.jpg")};
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

    /*private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "___________";
        private String status;

        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                Log.e(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            status = params[1];
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                if (status.equals(INSURANCE)) {
                    imageCapturedInsurance = result;

                    binder.imgInsurancePreview.setImageBitmap(imageCapturedInsurance);
                    if (!driverModel.getInsurance().getStatus().equals("1")) {
                        binder.rlInsurance.setClickable(true);
                        binder.imgInsuranceDel.setVisibility(View.VISIBLE);
                    } else {
                        binder.rlInsurance.setClickable(false);
                        binder.imgInsuranceDel.setVisibility(View.GONE);
                    }

                } else if (status.equals(LICENCE)) {
                    imageCapturedDriveLicence = result;
                    binder.imgLicencePreview.setImageBitmap(imageCapturedDriveLicence);
                    if (!driverModel.getDriverlicense().getStatus().equals("1")) {
                        binder.rlDriver.setClickable(true);
                        binder.imgLicenceDel.setVisibility(View.VISIBLE);
                    } else {
                        binder.rlDriver.setClickable(false);
                        binder.imgLicenceDel.setVisibility(View.GONE);
                    }

                }
            }

        }
    }*/
}
