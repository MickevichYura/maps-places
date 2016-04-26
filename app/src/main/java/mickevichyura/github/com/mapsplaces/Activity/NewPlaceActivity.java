package mickevichyura.github.com.mapsplaces.Activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;

import mickevichyura.github.com.mapsplaces.R;

import static java.lang.String.format;

public class NewPlaceActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;

    EditText editTextName;
    EditText editTextAddress;
    EditText editTextWebsite;
    EditText editTextPhone;
    Button chooseCoordinatesButton;

    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        editTextName = (EditText) findViewById(R.id.edit_text_name);
        editTextAddress = (EditText) findViewById(R.id.edit_text_address);
        editTextWebsite = (EditText) findViewById(R.id.edit_text_website);
        editTextPhone = (EditText) findViewById(R.id.edit_text_phone);
        chooseCoordinatesButton = (Button) findViewById(R.id.choose_latlng);
        chooseCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int PLACE_PICKER_REQUEST = 1;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(NewPlaceActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    Toast.makeText(NewPlaceActivity.this, "Google play service are not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!isDataValid()) {
                        Snackbar.make(view, "Wrong data", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        AddPlaceRequest place =
                                new AddPlaceRequest(
                                        editTextName.getText().toString(),
                                        latLng,
                                        editTextAddress.getText().toString(),
                                        Collections.singletonList(Place.TYPE_STREET_ADDRESS),
                                        editTextPhone.getText().toString(),
                                        Uri.parse(editTextWebsite.getText().toString())
                                );

                        Places.GeoDataApi.addPlace(mGoogleApiClient, place)
                                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                                    @Override
                                    public void onResult(PlaceBuffer places) {
                                        Toast.makeText(NewPlaceActivity.this, places.getStatus().toString(), Toast.LENGTH_SHORT).show();
                                        System.out.println(places.get(0).getLatLng());
                                        places.release();
                                        finish();
                                    }
                                });
                    }

                }
            });
        }
    }

    protected boolean isDataValid() {
        return !(latLng == null || editTextName.length() == 0 || editTextAddress.length() == 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                latLng = place.getLatLng();
                chooseCoordinatesButton.setText(format("(%s, %s)", latLng.latitude, latLng.longitude));
            }
        }
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
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
