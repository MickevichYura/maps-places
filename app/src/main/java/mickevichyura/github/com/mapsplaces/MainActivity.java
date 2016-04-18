package mickevichyura.github.com.mapsplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private GoogleApiClient mGoogleApiClient;

    private ListView listPlaces;

    private ImageView mImageView;

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listPlaces = (ListView) findViewById(R.id.listPlaces);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        requestPermission();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new place action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> itemHashMap = (HashMap<String, Object>) parent.getItemAtPosition(position);
            String titleItem = itemHashMap.get(TITLE).toString();
            String descriptionItem = itemHashMap.get(DESCRIPTION).toString();
            Toast.makeText(getBaseContext(),
                    "Вы выбрали " + titleItem + ". " + descriptionItem, Toast.LENGTH_SHORT).show();
        }
    };

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
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    ResultCallback<PlaceLikelihoodBuffer> cb = new ResultCallback<PlaceLikelihoodBuffer>() {
        @Override
        public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
            mPlacesList = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> hm;

            for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                hm = new HashMap<>();
                hm.put(TITLE, placeLikelihood.getPlace().getName()); // Название

                placePhotosAsync(placeLikelihood.getPlace().getId());
                hm.put(DESCRIPTION, placeLikelihood.getPlace().getAddress()); // Описание
                hm.put(ICON, R.drawable.mr_ic_cast_light); // Картинка
                mPlacesList.add(hm);
            }
            SimpleAdapter adapter = new SimpleAdapter(getContext(), mPlacesList,
                    R.layout.list_item, new String[]{TITLE, DESCRIPTION, ICON},
                    new int[]{R.id.text1, R.id.text2, R.id.img});

            listPlaces.setAdapter(adapter);
            listPlaces.setOnItemClickListener(itemClickListener);

            likelyPlaces.release();
        }
    };

    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
            = new ResultCallback<PlacePhotoResult>() {
        @Override
        public void onResult(PlacePhotoResult placePhotoResult) {
            if (!placePhotoResult.getStatus().isSuccess()) {

                return;
            }
            mImageView.setImageBitmap(placePhotoResult.getBitmap());
        }
    };

    private void placePhotosAsync(String placeId) {
        mImageView = (ImageView) findViewById(R.id.mImageView);
        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
                    @Override
                    public void onResult(PlacePhotoMetadataResult photos) {
                        if (!photos.getStatus().isSuccess()) {
                            return;
                        }

                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        if (photoMetadataBuffer.getCount() > 0) {
                            photoMetadataBuffer.get(0)
                                    .getScaledPhoto(mGoogleApiClient, mImageView.getWidth(),
                                            mImageView.getHeight())
                                    .setResultCallback(mDisplayPhotoResultCallback);
                        }
                        photoMetadataBuffer.release();
                    }
                });
    }

    private Context getContext() {
        return this;
    }

    private ArrayList<HashMap<String, Object>> mPlacesList;
    private static final String TITLE = "title"; // Верхний текст
    private static final String DESCRIPTION = "description"; // ниже главного
    private static final String ICON = "icon";  // будущая картинка

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {


            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(cb);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    TextView tv = (TextView) findViewById(R.id.tv);
                    if (tv != null) {
                        tv.setText("NO");
                    }
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}
