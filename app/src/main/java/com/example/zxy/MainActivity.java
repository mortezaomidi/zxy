package com.example.zxy;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * The most basic example of adding a map to an activity.
 */
@RuntimePermissions
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;
    MapboxMap mapboxMap;
    Button btnAddTiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        btnAddTiles = findViewById(R.id.addTiles);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        btnAddTiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivityPermissionsDispatcher.addExternalZXYWithPermissionCheck(MainActivity.this);

            }
        });

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
            this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void addExternalZXY() {
        // File file = new File("/sdcard/tiles/0/0/0.png");
        File file = new File("/sdcard/01-NGO_GIS/Tiles");
        if (file.exists()){
            Log.d("MyErr", file.getAbsolutePath());
        }
        if (file.canRead()) {
            Log.d("MyErr", "" + file.length());
            RasterSource rasterSource = new RasterSource("testTileSource",
                    new TileSet("tileset",
                            "file://" +  "sdcard/tiles/{z}/{x}/{y}.png")
            );
            mapboxMap.getStyle().addSource(rasterSource);
            RasterLayer rasterLayer = new RasterLayer("testTileLayer",
                    "testTileSource");
            mapboxMap.getStyle().addLayer(rasterLayer);
            btnAddTiles.setVisibility(View.INVISIBLE);
        }
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_external_storage_denied)
                .setPositiveButton("button_allow", (dialog, button) -> request.proceed())
                .setNegativeButton("button_deny", (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForCamera() {
        Toast.makeText(this, "OnPermissionDenied", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForCamera() {
        Toast.makeText(this, "OnNeverAskAgain", Toast.LENGTH_SHORT).show();
    }


    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


}