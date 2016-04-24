package mickevichyura.github.com.mapsplaces.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mickevichyura.github.com.mapsplaces.Adapter.PhotoCardRecyclerViewAdapter;
import mickevichyura.github.com.mapsplaces.PhotoObject;
import mickevichyura.github.com.mapsplaces.PlaceObject;
import mickevichyura.github.com.mapsplaces.R;

public class PlaceActivity extends AppCompatActivity {

    private PlaceObject place;

    private GoogleApiClient mGoogleApiClient;

    private PhotoCardRecyclerViewAdapter photoCardAdapter;

    private List<Bitmap> mListPhotos;
    private List<String> mListPhotosAuthors;
    private List<PhotoObject> photoObjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mGoogleApiClient = MainActivity.mGoogleApiClient;

        mListPhotos = new ArrayList<>();
        mListPhotosAuthors = new ArrayList<>();
        photoObjectList = new ArrayList<>();
        setAdapter();

        Intent intent = getIntent();
        place = (PlaceObject) intent.getSerializableExtra("place");
        if (place != null) {
            toolbar.setTitle(place.getName());
        }
        setSupportActionBar(toolbar);

        setContent(place);

        loadPlacePhotosAsync(place.getId());

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

    private void setAdapter() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        StaggeredGridLayoutManager sGridLayoutManager = new StaggeredGridLayoutManager(2, 1);

        recyclerView.setLayoutManager(sGridLayoutManager);

        photoCardAdapter = new PhotoCardRecyclerViewAdapter(getBaseContext(), photoObjectList);
        recyclerView.setAdapter(photoCardAdapter);
    }

    public void setContent(PlaceObject place) {

        TextView tv = (TextView) findViewById(R.id.tv_name);
        tv.setText(place.toString());

        tv = (TextView) findViewById(R.id.tv_address);
        tv.setText(place.getAddress());

        tv = (TextView) findViewById(R.id.tv_website_uri);
        tv.setText(place.getWebsiteUri());

        tv = (TextView) findViewById(R.id.tv_phone_number);
        tv.setText(place.getPhoneNumber());
    }

    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
            = new ResultCallback<PlacePhotoResult>() {
        @Override
        public void onResult(PlacePhotoResult placePhotoResult) {
            if (!placePhotoResult.getStatus().isSuccess()) {
                return;
            }

            mListPhotos.add(placePhotoResult.getBitmap());
            if (mListPhotos.size() > 0){
                photoObjectList.get(mListPhotos.size() - 1).setPhoto(placePhotoResult.getBitmap());
            }

            photoCardAdapter.notifyDataSetChanged();
        }
    };

    private void loadPlacePhotosAsync(String placeId) {
        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
                    @Override
                    public void onResult(PlacePhotoMetadataResult photos) {
                        if (!photos.getStatus().isSuccess()) {
                            return;
                        }

                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        String author;
                        if (photoMetadataBuffer.getCount() > 0) {
                            for (PlacePhotoMetadata placePhotoMetadata : photoMetadataBuffer) {
                                author = String.format(getResources().getString(R.string.photo_card_author), Html.fromHtml(placePhotoMetadata.getAttributions().toString()).toString());
                                mListPhotosAuthors.add(author);
                                placePhotoMetadata.getScaledPhoto(mGoogleApiClient, 640, 640).setResultCallback(mDisplayPhotoResultCallback,
                                        1000, TimeUnit.SECONDS);
                            }
                        } else {
//                            mImageView.setImageResource(R.drawable.ic_plusone_small_off_client);
                        }

                        photoMetadataBuffer.release();

                        for (String s : mListPhotosAuthors) {
                            photoObjectList.add(new PhotoObject(null, s));
                        }
                        photoCardAdapter.notifyDataSetChanged();
                    }
                }, 5, TimeUnit.MINUTES);

        photoCardAdapter.notifyDataSetChanged();
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
