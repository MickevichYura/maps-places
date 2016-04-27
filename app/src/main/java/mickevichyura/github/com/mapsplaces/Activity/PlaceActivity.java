package mickevichyura.github.com.mapsplaces.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;

import mickevichyura.github.com.mapsplaces.adapter.PhotoCardRecyclerViewAdapter;
import mickevichyura.github.com.mapsplaces.model.PhotoObject;
import mickevichyura.github.com.mapsplaces.model.PlaceObject;
import mickevichyura.github.com.mapsplaces.R;

public class PlaceActivity extends AppCompatActivity {

    private PlaceObject place;

    private GoogleApiClient mGoogleApiClient;

    private PhotoCardRecyclerViewAdapter mPhotoCardAdapter;

    private List<Bitmap> mListPhotos;
    private List<String> mListPhotosAuthors;
    private List<PhotoObject> mPhotoObjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mGoogleApiClient = MainActivity.mGoogleApiClient;

        mListPhotos = new ArrayList<>();
        mListPhotosAuthors = new ArrayList<>();
        mPhotoObjectList = new ArrayList<>();
        setAdapter();

        Intent intent = getIntent();
        place = (PlaceObject) intent.getSerializableExtra("place");
        if (place != null) {
            toolbar.setTitle(place.getName());
        }

        setSupportActionBar(toolbar);

        setContent(place);

        if (mPhotoObjectList.isEmpty())
            loadPlacePhotosAsync(place.getId());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + place.getName() + " " + place.getAddress());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });
        }
    }

    private void setAdapter() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        StaggeredGridLayoutManager sGridLayoutManager = new StaggeredGridLayoutManager(2, 1);

        recyclerView.setLayoutManager(sGridLayoutManager);

        mPhotoCardAdapter = new PhotoCardRecyclerViewAdapter(getBaseContext(), mPhotoObjectList);
        recyclerView.setAdapter(mPhotoCardAdapter);
    }

    public void setContent(PlaceObject place) {

        TextView tv = (TextView) findViewById(R.id.tv_name);
        tv.setText(place.getName());

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
            if (mListPhotos.size() > 0) {
                mPhotoObjectList.get(mListPhotos.size() - 1).setPhoto(placePhotoResult.getBitmap());
            }

            mPhotoCardAdapter.notifyDataSetChanged();
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
                                placePhotoMetadata.getScaledPhoto(mGoogleApiClient, 640, 640).setResultCallback(mDisplayPhotoResultCallback);
                            }
                        }

                        for (String s : mListPhotosAuthors) {
                            mPhotoObjectList.add(new PhotoObject(null, s));
                        }
                        mPhotoCardAdapter.notifyItemRangeInserted(0, mPhotoObjectList.size() - 1);

                        photoMetadataBuffer.release();

                    }
                });

        mPhotoCardAdapter.notifyDataSetChanged();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_update) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_place_picker) {
            int PLACE_PICKER_REQUEST = 1;
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
                Toast.makeText(PlaceActivity.this, "Google play service are not available", Toast.LENGTH_SHORT).show();
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                PlaceObject placeObject = new PlaceObject(place);
                Intent intent = new Intent(getBaseContext(), PlaceActivity.class);

                intent.putExtra("place", placeObject);
                startActivity(intent);
            }
        }
    }

}
