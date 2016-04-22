package mickevichyura.github.com.mapsplaces.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import mickevichyura.github.com.mapsplaces.Place;
import mickevichyura.github.com.mapsplaces.R;

public class PlaceActivity extends AppCompatActivity {

    private ImageView mImageView;

    private Place place;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mImageView = (ImageView) findViewById(R.id.mImageView);
        mGoogleApiClient = MainActivity.mGoogleApiClient;

        Intent intent = getIntent();
        place = (Place) intent.getSerializableExtra("place");
        if (toolbar != null) {
            toolbar.setTitle(place.getName());
        }
        setSupportActionBar(toolbar);

        placePhotosAsync(place.getId());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Edit place action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
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
                            for (PlacePhotoMetadata placePhotoMetadata : photoMetadataBuffer) {
                                placePhotoMetadata.getScaledPhoto(mGoogleApiClient, mImageView.getWidth(),
                                        mImageView.getHeight())
                                        .setResultCallback(mDisplayPhotoResultCallback);
                            }
                        } else {
                            mImageView.setImageResource(R.drawable.ic_plusone_small_off_client);
                        }

                        photoMetadataBuffer.release();
                    }
                });
    }

}
