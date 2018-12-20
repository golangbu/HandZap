package com.handzap.pruthwirajs.handzap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {
    EditText etBusinessAddress;
    private int PLACE_PICKER_REQUEST = 1;
    private GoogleApiClient mGoogleApiClient;
    private double longitude, latitude;
    private int requestType;
    EditText etUnitMesure;
    EditText etPayment;
    ImageButton back_btn;
    private Spinner spinnerCurrency;
    private BottomSheetDialog mBottomSheetDialog;
    RelativeLayout rl_tin_image;
    RelativeLayout rl_pan_image;
    private Uri uri;
    TextView tvPost;
    ProgressDialog loading = null;



    CharSequence[] array = {"No Preference", "E-payment", "Cash"};
    String[] countryNames={"India","China","Australia","America","New Zealand"};
    int flags[] = {R.drawable.india, R.drawable.china, R.drawable.us, R.drawable.us, R.drawable.china};



    private String[] permissionsList = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    private int PLACE_PICKER_REQUEST_LANDMARK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        etBusinessAddress = (EditText) findViewById(R.id.et_search_text);
        etUnitMesure = (EditText) findViewById(R.id.et_unit_mesure);
        etPayment = (EditText) findViewById(R.id.spinnerAvailability);
        spinnerCurrency = (Spinner) findViewById(R.id.spinnerCurrency);
        back_btn = (ImageButton) findViewById(R.id.back_btn);
        rl_tin_image = (RelativeLayout) findViewById(R.id.rl_tin_image);
        rl_pan_image = (RelativeLayout) findViewById(R.id.rl_pan_image);
        tvPost= (TextView) findViewById(R.id.post);



        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),flags,countryNames);
        spinnerCurrency.setOnItemSelectedListener(this);

        spinnerCurrency.setAdapter(customAdapter);

        connectGoogleClient();
        MPermission.checkPermissions(this, permissionsList, Constants.MULTIPLE_PERMISSIONS);

        etBusinessAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(PLACE_PICKER_REQUEST);

            }
        });
        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = new ProgressDialog(v.getContext());
                loading.setCancelable(true);
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.show();

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_tin_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
                //chooseAvatar();
            }
        });
        rl_pan_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
               // chooseAvatar();
            }
        });

        etPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alt_bld = new AlertDialog.Builder(MainActivity.this);
                //alt_bld.setIcon(R.drawable.icon);
                alt_bld.setTitle("Select a Payment method");
                alt_bld.setSingleChoiceItems(array, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
//                        Toast.makeText(getApplicationContext(),
//                                "Group Name = "+array[item], Toast.LENGTH_SHORT).show();
                        etPayment.setText(array[item]);
                        dialog.dismiss();// dismiss the alertbox after chose option

                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }
        });

        etUnitMesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FlowerGalleryActivity.class);
                startActivity(intent);
            }
        });



        //  etBusinessAddress.setOnTouchListener;
//        etBusinessAddress.set(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openMap(PLACE_PICKER_REQUEST);
//
//            }
//        });



//        etBusinessAddress.setOnTouchListener((v,event)->
//
//    {
//        final int DRAWABLE_LEFT = 0;
//        final int DRAWABLE_TOP = 1;
//        final int DRAWABLE_RIGHT = 2;
//        final int DRAWABLE_BOTTOM = 3;
//
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (event.getRawX() >= (etBusinessAddress.getRight() - etBusinessAddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                // your action here
//                openMap(PLACE_PICKER_REQUEST);
//
//                return true;
//            }
//        }
//        return false;
//    });
    }

    private void openMap(int requestType) {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(MainActivity.this), requestType);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void connectGoogleClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Snackbar.make(getApplicationContext(), connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                latitude = (place.getLatLng().latitude);
                longitude = (place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("");
                stBuilder.append(address);
                etBusinessAddress.setText(stBuilder.toString());
//                etBusinessAddress.setFocusableInTouchMode(false);
//                etBusinessAddress.setClickable(true);
//                etBusinessAddress.setFocusable(false);
               // etBusinessLocation.setText(latitude + ", " + longitude);
               // etBusinessLocalArea.setText(address);
            }
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                this.uri = result.getUri();
                if (false) {
                    //reloadAvatar();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                if (error != null) {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void getCurrentLocation() {
        GPSTracker gps = new GPSTracker(this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return;
            }
            case Constants.PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                return;
            }

            case Constants.MY_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent(requestType);
                } else {
                  //  Helper.showValidationSnackBar(mainProductLayout, "access denied");
                }
                break;
            }

            case Constants.MY_CAMERA_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent(requestType);
                } else {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private void galleryIntent(int type) {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cameraIntent(int type) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis()
                        + ".png");
        if (Build.VERSION.SDK_INT >= 24) {
         //   picUri = FileProvider.getUriForFile(EditBusinessActivity.this, EditBusinessActivity.this.getPackageName() + ".files", file);
        } else {
           // picUri = Uri.fromFile(file);
        }
       // intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        //startActivityForResult(intent, type);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        //
        //
        // Toast.makeText(getApplicationContext(), countryNames[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void showBottomSheetDialog() {
        mBottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseAvatar();
//            }
//        });
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.show();

//        tvViewAllChat.setOnClickListener(view14 -> {
//            if (mBottomSheetDialog != null) {
//                mBottomSheetDialog.dismiss();
//            }
//        }


        // });
    }

    private void chooseAvatar() {
        CropImage.activity()
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .setAspectRatio(1, 1)
                .setMinCropResultSize(192, 192)
                .start(this);
    }



}
