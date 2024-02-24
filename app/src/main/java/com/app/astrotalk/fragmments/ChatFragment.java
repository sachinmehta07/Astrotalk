package com.app.astrotalk.fragmments;

import static android.view.View.GONE;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.astrotalk.R;
import com.app.astrotalk.activity.DashboardActivity;
import com.app.astrotalk.adapter.UserProfileAdapter;
import com.app.astrotalk.databinding.FragmentChatBinding;
import com.app.astrotalk.listeners.OnCategoryItemClick;
import com.app.astrotalk.model.AllCategory;
import com.app.astrotalk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
    private UserProfileAdapter userProfileAdapter;
    List<AllCategory> allUserBase = new ArrayList<>();

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentChatBinding.inflate(inflater, container, false);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData();
        initialization();

    }

    private void initialization() {

        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                if (Utils.isNetworkAvailable(requireActivity())) {
                    userProfileAdapter.filter(cs.toString());
                    binding.ivRemove.setVisibility(View.VISIBLE);
                    if (userProfileAdapter != null)
                        if ((userProfileAdapter.userProfiles.size() == 0)) {
                            binding.txNoResult.setVisibility(View.VISIBLE);
                        } else {
                            binding.txNoResult.setVisibility(GONE);
                        }
                } else {
                    Toast.makeText(requireActivity(), R.string.no_internet_connection_found_ncheck_your_connection, Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.toString().isEmpty()) {
                    binding.ivRemove.setVisibility(View.INVISIBLE);
                }
                // TODO Auto-generated method stub
            }
        });


        userProfileAdapter = new UserProfileAdapter(requireActivity());
        userProfileAdapter.setData(allUserBase, new OnCategoryItemClick() {
            @Override
            public void onItemClick(int position, AllCategory astrologer) {

            }
        });
        binding.rvUsersChat.setAdapter(userProfileAdapter);
    }
    public void setData() {

        allUserBase.add(new AllCategory(1, "Harsh", R.drawable.astro_1, "Vedic Astrologer", "5 years of experience", "Hindi, English", "Specialized in career and relationships"));
        allUserBase.add(new AllCategory(2, "Rahul", R.drawable.astro_2, "Vedic Astrologer", "8 years of experience", "Sanskrit, Hindi", "Expert in predictive astrology"));
        allUserBase.add(new AllCategory(3, "Sneha", R.drawable.astro_f_1, "Vedic Astrologer", "6 years of experience", "English, Tamil", "Specialized in health predictions"));
        allUserBase.add(new AllCategory(4, "Kiran", R.drawable.astro_3, "Vedic Astrologer", "7 years of experience", "Gujarati, Hindi", "Expert in financial astrology"));
        allUserBase.add(new AllCategory(5, "Arjun", R.drawable.astro_4, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Specialized in gemstone recommendations"));
        allUserBase.add(new AllCategory(6, "Sachin", R.drawable.astro_5, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Specialized in gemstone recommendations"));
        allUserBase.add(new AllCategory(7, "Darshan", R.drawable.astro_6, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Specialized in gemstone recommendations"));
        allUserBase.add(new AllCategory(8, "Neha", R.drawable.astro_f_2, "Vedic Astrologer", "9 years of experience", "Hindi, Punjabi", "Expert in spiritual guidance"));


        allUserBase.add(new AllCategory(1, "Jay", R.drawable.astro_c_4, "Career Astrologer", "6 years of experience", "English, Marathi", "Expert in career counseling and growth predictions"));
        allUserBase.add(new AllCategory(2, "Amit", R.drawable.astro_c_1, "Career Astrologer", "4 years of experience", "Hindi, English", "Specialized in job change predictions"));
        allUserBase.add(new AllCategory(3, "Priyanka", R.drawable.astro_f_c_7, "Career Astrologer", "7 years of experience", "Gujarati, English", "Expert in business and entrepreneurship predictions"));
        allUserBase.add(new AllCategory(4, "Raj", R.drawable.astro_c_5, "Career Astrologer", "5 years of experience", "Hindi, Tamil", "Specialized in leadership and management guidance"));
        allUserBase.add(new AllCategory(5, "Seema", R.drawable.astro_f_c_6, "Career Astrologer", "8 years of experience", "English, Telugu", "Expert in skill development predictions"));
        allUserBase.add(new AllCategory(6, "Sita", R.drawable.astro_f_3, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Specialized in education and academic predictions"));
        allUserBase.add(new AllCategory(7, "Sayam", R.drawable.atstro_c_2, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Specialized in education and academic predictions"));
        allUserBase.add(new AllCategory(8, "Pooja", R.drawable.astro_f_c_8, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Specialized in education and academic predictions"));


        allUserBase.add(new AllCategory(1, "Manoj", R.drawable.astro_m_1, "Marriage Astrologer", "7 years of experience", "Gujarati, Hindi", "Expert in match-making and compatibility analysis"));
        allUserBase.add(new AllCategory(2, "Sandeep", R.drawable.astro_m_2, "Marriage Astrologer", "5 years of experience", "English, Punjabi", "Specialized in love marriage predictions"));
        allUserBase.add(new AllCategory(3, "Deepa", R.drawable.astro_m_3, "Marriage Astrologer", "6 years of experience", "Hindi, Marathi", "Expert in family and relationship counseling"));
        allUserBase.add(new AllCategory(4, "Rahul", R.drawable.astro_m_4, "Marriage Astrologer", "8 years of experience", "English, Kannada", "Specialized in post-marriage guidance"));
        allUserBase.add(new AllCategory(5, "Pooja", R.drawable.astro_m_f_7, "Marriage Astrologer", "4 years of experience", "Gujarati, Hindi", "Expert in resolving marital conflicts"));
        allUserBase.add(new AllCategory(6, "Arun", R.drawable.astro_m_5, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Specialized in Kundli matching"));
        allUserBase.add(new AllCategory(7, "Arun", R.drawable.astro_m_6, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Specialized in Kundli matching"));
        allUserBase.add(new AllCategory(8, "Jinal", R.drawable.astro_m_f_8, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Specialized in Kundli matching"));

        allUserBase.add(new AllCategory(1, "Love", R.drawable.astro_l_1, "Love Astrologer", "4 years of experience", "Hindi, English", "Expert in love life guidance"));
        allUserBase.add(new AllCategory(2, "Vikram", R.drawable.astro_l_2, "Love Astrologer", "6 years of experience", "Telugu, English", "Specialized in relationship compatibility"));
        allUserBase.add(new AllCategory(3, "Suman", R.drawable.astro_l_3, "Love Astrologer", "5 years of experience", "Hindi, Marathi", "Expert in resolving love conflicts"));
        allUserBase.add(new AllCategory(4, "Piyush", R.drawable.astro_l_4, "Love Astrologer", "7 years of experience", "English, Punjabi", "Specialized in love horoscope analysis"));
        allUserBase.add(new AllCategory(5, "Aditya", R.drawable.astro_l_5, "Love Astrologer", "3 years of experience", "Hindi, Tamil", "Expert in love compatibility predictions"));
        allUserBase.add(new AllCategory(6, "Preeti", R.drawable.astro_l_f_6, "Love Astrologer", "8 years of experience", "Gujarati, English", "Specialized in love and romance predictions"));
        allUserBase.add(new AllCategory(6, "Ritika", R.drawable.astro_l_f_7, "Love Astrologer", "8 years of experience", "Gujarati, English", "Specialized in love and romance predictions"));
        allUserBase.add(new AllCategory(6, "Isha", R.drawable.astro_l_f_8, "Love Astrologer", "8 years of experience", "Gujarati, English", "Specialized in love and romance predictions"));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Release the binding when the view is destroyed
    }
}
