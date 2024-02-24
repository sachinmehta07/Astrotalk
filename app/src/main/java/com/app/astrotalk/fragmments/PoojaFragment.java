package com.app.astrotalk.fragmments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.astrotalk.R;
import com.app.astrotalk.activity.DashboardActivity;
import com.app.astrotalk.databinding.FragmentPoojaBinding;

public class PoojaFragment extends Fragment {

    private FragmentPoojaBinding binding;

    public PoojaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentPoojaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Access views using binding
        binding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardActivity) requireActivity()).drawerOpen();
            }
        });
        // You can now use 'ivMenu' directly for menu click handling

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Release the binding when the view is destroyed
    }
}
