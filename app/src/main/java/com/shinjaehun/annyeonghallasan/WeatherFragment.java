package com.shinjaehun.annyeonghallasan;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Created by shinjaehun on 2017-05-22.
 */

public class WeatherFragment extends Fragment {

    public WeatherFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

//        Calendar calendar = Calendar.getInstance();
//
//        new FetchWeatherTask(getActivity().getApplicationContext(), calendar).execute();

        return v;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
