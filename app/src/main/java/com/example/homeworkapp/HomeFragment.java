package com.example.homeworkapp;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private RecyclerView recyclerView;
    private NewsListAdapter newsListAdapter;
    private LocationManager locationManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    public Double latitude = 0.0;

    public Double longtitude = 0.0;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(requireActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) this.requireContext().getSystemService(Context.LOCATION_SERVICE);
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.home_fragment, container, false);
        ProgressBar progressBar = root.findViewById(R.id.progressBar);
        TextView progress_text=root.findViewById(R.id.progressbar_text);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        checkLocationPermission();
        LocationManager locationManager = (LocationManager) this.requireContext().getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        assert lastKnownLocation != null;
//        this.latitude = lastKnownLocation.getLatitude();
//        this.longtitude = lastKnownLocation.getLongitude();
        this.latitude=34.02;
        this.longtitude=-118.28;
        Geocoder geocoder = new Geocoder(this.requireContext(), Locale.getDefault());
        String city;
        String state;
        TextView city_textview = root.findViewById(R.id.city_weather);
        TextView state_textview = root.findViewById(R.id.state_weather);
        TextView weather_textview = root.findViewById(R.id.weather);
        TextView temperature_text = root.findViewById(R.id.temperature_weather);
        ImageView weather_card_full = root.findViewById(R.id.card_background);
        try {
            List<Address> addresses = geocoder.getFromLocation(this.latitude, this.longtitude, 1);
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            Log.d("name", "onCreateView: " + city);
            mViewModel.setLocality(city);
            mViewModel.weather.observe(getViewLifecycleOwner(), weather -> {
                city_textview.setText(city);
                state_textview.setText(state);
                weather_textview.setText(weather.get(0));
                temperature_text.setText(weather.get(1));
                Picasso.with(this.requireContext()).load(weather.get(2)).into(weather_card_full);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        newsListAdapter = new NewsListAdapter(this.getContext());
        newsListAdapter.setMutableLiveData(mViewModel.newsList);
        recyclerView.setAdapter(newsListAdapter);
        mViewModel.newsList.observe(getViewLifecycleOwner(), newsDTOS -> {
            newsListAdapter.setList(newsDTOS);
            newsListAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            progress_text.setVisibility(View.INVISIBLE);
        });

        swipeRefreshLayout=root.findViewById(R.id.searchpage_refreshing);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.getNewsList();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        if (mViewModel.newsList.getValue()!=null) {
            List<NewsDTO> newsDTOS = mViewModel.newsList.getValue();
            mViewModel.newsList.setValue(newsDTOS);
        }
        super.onResume();
    }

}
