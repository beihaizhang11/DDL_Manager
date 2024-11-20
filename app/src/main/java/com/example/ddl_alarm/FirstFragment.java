package com.example.ddl_alarm;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ddl_alarm.databinding.FragmentFirstBinding;
import com.example.ddl_alarm.model.Major;
import com.example.ddl_alarm.model.Deadline;
import com.example.ddl_alarm.adapter.DeadlineAdapter;
import com.example.ddl_alarm.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private ArrayAdapter<Major> majorAdapter;
    private DeadlineAdapter deadlineAdapter;
    private List<Major> majors = new ArrayList<>();
    private List<Deadline> deadlines = new ArrayList<>();
    private Handler handler = new Handler();
    private static final int REFRESH_INTERVAL = 60000; // 1分钟刷新一次

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        setupSpinner();
        setupRecyclerView();
        startAutoRefresh();
        loadMajors();
        return binding.getRoot();

    }

    private void setupSpinner() {
        majorAdapter = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_spinner_item, majors);
        majorAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item);
        binding.majorSpinner.setAdapter(majorAdapter);
        binding.majorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadDeadlines(majors.get(position).getMajorId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRecyclerView() {
        deadlineAdapter = new DeadlineAdapter(deadlines);
        binding.deadlinesRecyclerView.setLayoutManager(
            new LinearLayoutManager(requireContext()));
        binding.deadlinesRecyclerView.setAdapter(deadlineAdapter);
    }

    private void loadMajors() {
        new Thread(() -> {
            List<Major> loadedMajors = DatabaseHelper.getAllMajors();
            requireActivity().runOnUiThread(() -> {
                majors.clear();
                majors.addAll(loadedMajors);
                majorAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, majors);
                majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.majorSpinner.setAdapter(majorAdapter);
                binding.majorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        loadDeadlines(majors.get(position).getMajorId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        deadlines.clear();
                        deadlineAdapter.notifyDataSetChanged();
                    }
                });
            });
        }).start();
    }

    private void loadDeadlines(int majorId) {
        new Thread(() -> {
            List<Deadline> loadedDeadlines = DatabaseHelper.getDeadlinesByMajor(majorId);
            requireActivity().runOnUiThread(() -> {
                deadlines.clear();
                deadlines.addAll(loadedDeadlines);
                deadlineAdapter.updateDeadlines(deadlines);
            });
        }).start();
    }

    private void startAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded() && !majors.isEmpty()) {
                    loadDeadlines(majors.get(
                        binding.majorSpinner.getSelectedItemPosition()).getMajorId());
                }
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        }, REFRESH_INTERVAL);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        binding = null;
    }

}