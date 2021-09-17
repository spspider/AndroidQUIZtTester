package com.example.ttester_paukov;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by tanja on 03.05.2017.
 */

public class SettingsFragment extends Fragment{


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settingswondow,null);


        SharedPreferences settings = getContext().getSharedPreferences("", MODE_PRIVATE);


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
