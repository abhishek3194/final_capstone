package com.example.poornima.clickforchange;

import android.Manifest;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ServerSideAPIs.UploadClickedImage;
import Utils.GPSTracker;


public class NewsFeedActivity extends AppCompatActivity {


    public final String APP_TAG = "CAMERA_LOCATION_TAG";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    public String photoFileName;

    public File image_file;

    private LocationManager locationManager;



    double longitude=30;
    double latitude=30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        BottomBar mBottomBar;

        getSupportActionBar().setTitle("Click For Change");
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.Bottombaritemone) {
                    homeFragment f = new homeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else {
                    if (menuItemId == R.id.Bottombaritemtwo) {
                        uploadFragment f = new uploadFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                    } else {
                        if (menuItemId == R.id.Bottombaritemthree) {
                            cameraFragment f = new cameraFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                        } else {
                            if (menuItemId == R.id.Bottombaritemfour) {
                                notificationFragment f = new notificationFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                            } else {
                                if (menuItemId == R.id.Bottombaritemfive) {
                                    profileFragment f = new profileFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                                }

                            }
                        }
                    }
                }


            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
            }
        });
        mBottomBar.mapColorForTab(0, "#616161");
        mBottomBar.mapColorForTab(1, "#616161");
        mBottomBar.mapColorForTab(2, "#616161");
        mBottomBar.mapColorForTab(3, "#616161");
        mBottomBar.mapColorForTab(4, "#616161");

        BottomBarBadge unread;
        unread = mBottomBar.makeBadgeForTabAt(3, "#2196F3", 13);
        unread.show();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new NewsFeedFragment())
                    .commit();
        }

        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation())
        {
            latitude = gps.getLatitude(); // returns latitude
            longitude = gps.getLongitude(); // returns longitude

            Log.e(APP_TAG, String.valueOf(latitude));
            Log.e(APP_TAG, String.valueOf(longitude));
        }
        else
        {
            gps.showSettingsAlert();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_feed, menu);
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
    public void openEditProfile(View view) {
        Intent intent = new Intent(this, editProfile.class);
        startActivity(intent);
    }
    public void ClickPicture(View view) {


        photoFileName = (new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())) + ".jpg";

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap photo;
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                Log.e("Camera", photoFileName);
                // by this point we have the camera photo on disk
                photo = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                photo = null;
            }
        } else {
            photo = null;
        }
            ImageView imageView = (ImageView)findViewById(R.id.list_item_icon);

            Log.e("CameraDemo", "Pic saved");

        imageView = (ImageView) findViewById(R.id.list_item_icon);

        Log.e("CameraDemo", "Pic saved");
        photo = null;


        if (resultCode == RESULT_OK) {
            //Log.v("code",encoded);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = (Location) lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

                if(photo!=null)
                {
                    Uri takenPhotoUri = getPhotoFileUri(photoFileName);

                    Log.e("Camera", photoFileName);
                    // by this point we have the camera photo on disk
                    photo = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                    // RESIZE BITMAP, see section below
                    // Load the taken image into a preview

                    Log.e(APP_TAG, image_file.getAbsolutePath());
                    geoTag(image_file.getAbsolutePath(), latitude, longitude);

                    imageView.setImageBitmap(photo);

                    UploadClickedImage uploader = new UploadClickedImage(photo, this, UserData.SESSION_USER, latitude, longitude);
                    uploader.execute();
                }

            }
            else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.activity_news_feed);
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED)
        {
            Toast.makeText(this,"Camera Exited",Toast.LENGTH_LONG).show();
            Log.d(APP_TAG,"Camera Exited");
        }

    }


    public Uri getPhotoFileUri(String fileName) {

            image_file = new File(LoginActivity.imagesFolder.getPath() + File.separator + fileName);
            // Return the file target for the photo based on filename
            Log.e(APP_TAG,Uri.fromFile(image_file).toString());
            return Uri.fromFile(image_file);

    }

    // Returns true if external storage for photos is available
   /* private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }*/

    /*private void showAlert()
    {
        Log.e("ALERT:","inside showAlert()");

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Enable Location").setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }*/


    public void geoTag(String filename, double latitude, double longitude){

        ExifInterface exif;

        try {
            exif = new ExifInterface(filename);
            int num1Lat = (int)Math.floor(latitude);
            int num2Lat = (int)Math.floor((latitude - num1Lat) * 60);
            double num3Lat = (latitude - ((double)num1Lat+((double)num2Lat/60))) * 3600000;

            int num1Lon = (int)Math.floor(longitude);
            int num2Lon = (int)Math.floor((longitude - num1Lon) * 60);
            double num3Lon = (longitude - ((double)num1Lon+((double)num2Lon/60))) * 3600000;


            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, num1Lat+"/1,"+num2Lat+"/1,"+num3Lat+"/1000");
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, num1Lon+"/1,"+num2Lon+"/1,"+num3Lon+"/1000");

            Log.e("Geotag",exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            Log.e("Geotag",exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));

            if (latitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (longitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }

            exif.saveAttributes();
        } catch (IOException e) {
            Log.e("PictureActivity", e.getLocalizedMessage());
        }

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);
            return rootView;
        }
    }

    private Boolean exit = false;

    public static  File credentialsFile = new File(LoginActivity.dataFolder, "secret");
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();

            FileWriter writer = null;
            try {
                writer = new FileWriter(credentialsFile);
                writer.write(LoginActivity.username+"\n");
                writer.append(LoginActivity.password);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3000);

        }

    }

}
