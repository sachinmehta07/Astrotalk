package com.app.astrotalk.fragmments;

import static android.view.View.GONE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.app.astrotalk.activity.ProfileActivity;
import com.app.astrotalk.adapter.CallProfileAdapter;
import com.app.astrotalk.databinding.FragmentCallBinding;
import com.app.astrotalk.listeners.OnCategoryItemClick;
import com.app.astrotalk.listeners.OnProfileClick;
import com.app.astrotalk.model.AstrolgerModel;
import com.app.astrotalk.model.UserReviewModel;
import com.app.astrotalk.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CallFragment extends Fragment {
    private FragmentCallBinding binding;
    private CallProfileAdapter userProfileAdapter;
    private String phoneNumber = "";
    List<AstrolgerModel> allUserBase = new ArrayList<>();
    private static final int REQUEST_CALL_PERMISSION = 1;
    private final List<UserReviewModel> UserReviewBaseDataList = new ArrayList<>();


    public CallFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentCallBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Access views using binding
        binding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardActivity) requireActivity()).drawerOpen();
            }
        });
        binding.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edSearch.setText("");
                binding.ivRemove.setVisibility(View.INVISIBLE);
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

    @SuppressLint("QueryPermissionsNeeded")
    private void initiateCall(String phoneNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (dialIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(dialIntent);
        } else {
            // Handle the case where the dialer app is not available.
            // You may want to show a message to the user.
        }
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


        userProfileAdapter = new CallProfileAdapter(requireActivity());
        Collections.shuffle(allUserBase);
        userProfileAdapter.setData(allUserBase, new OnCategoryItemClick() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onItemClick(int position, AstrolgerModel astrologer) {
                phoneNumber = astrologer.getPhoneNumber();
                if (requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, proceed with the call

                    if (phoneNumber != null) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        startActivity(callIntent);
                    }
                } else {
                    // Request permission if not granted
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                }

            }
        }, new OnProfileClick() {
            @Override
            public void onProfileClick(AstrolgerModel astrologer) {
                Gson gson = new Gson();

                String userReviewsJson = gson.toJson(astrologer.getUserReviews());
                Intent intent = new Intent(requireActivity(), ProfileActivity.class);
                intent.putExtra("profilePicUrl", astrologer.getImageD()); // Replace with the actual URL
                intent.putExtra("name", astrologer.getName());
                intent.putExtra("astrologyType", astrologer.getAstroType());
                intent.putExtra("experience", astrologer.getAstroExp());
                intent.putExtra("language", astrologer.getAstroLang());
                intent.putExtra("aboutAstrology", astrologer.getAstroAbout());
                intent.putExtra("userReviewsJson", userReviewsJson);
                intent.putExtra("Address", astrologer.getAddress());
                intent.putExtra("phoneNumber", astrologer.getPhoneNumber());
                intent.putExtra("userId", String.valueOf(astrologer.getId()));
                // Start the ProfileActivity
                startActivity(intent);
            }
        });
        binding.rvUsersChat.setAdapter(userProfileAdapter);
    }


    public void setData() {

        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_1, "Rahul", 5, "Amazing astrologer! Predictions were spot on and helped me navigate through career decisions."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_2, "Anuj", 4, "Good insights into personal relationships. Predicted some key events in my life accurately."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_3, "Kiran", 3, "Average experience. Some predictions were accurate, while others were off the mark."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_f_5, "Neha", 5, "Exceptional astrologer! Provided deep insights into my life, and the guidance was invaluable."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_4, "Vikram", 4, "Satisfactory predictions. Helped me understand the challenges ahead and how to overcome them."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_7, "Ram", 3, "Decent astrologer. Predictions were general, and some did not resonate with my experiences."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_8, "Amit", 5, "Highly recommended! Accurate predictions, and the astrologer has a deep understanding of Vedic astrology."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_9, "Sanjay", 4, "Impressive insights! The astrologer provided accurate predictions, and I found the session enlightening."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_10, "Rahul", 3, "Average experience. Some predictions resonated, while others seemed generic and unrelated."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_f_11, "Nisha", 5, "Exceptional astrologer! Predictions were detailed, and the guidance provided was immensely helpful."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_12_f, "Deepa", 4, "Satisfied with the predictions. The astrologer offered valuable insights into my career path and personal life."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_13_f, "Meera", 5, "Highly recommended! The astrologer accurately predicted major life events and provided practical advice."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_f_14, "Aarti", 3, "Decent predictions. Some insights were accurate, but others felt vague and open to interpretation."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_15_f, "Pooja", 4, "Good astrologer! Helped me gain clarity on certain aspects of my life, and the predictions were mostly accurate."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_16_f, "Anjali", 5, "Exceptional service! The astrologer provided in-depth insights, and the predictions were remarkably accurate."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_17, "Karan", 3, "Average predictions. Some aspects were accurate, but overall, the session lacked the depth I was expecting."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_18, "Aditya", 4, "Satisfied with the astrologer's guidance. Predictions were insightful, and the session was conducted professionally."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_19, "Juhi", 5, "Highly recommended! The astrologer's predictions were remarkably accurate, and the guidance provided was invaluable."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_20, "Vikas", 4, "Good astrologer! Predictions were mostly accurate, and the session helped me gain clarity on various aspects of my life."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_21, "Sayam", 3, "Average experience. The astrologer provided some accurate insights, but overall, the session lacked depth."));
        UserReviewBaseDataList.add(new UserReviewModel(R.drawable.tst_22, "Varun", 5, "Exceptional service! The astrologer's predictions were on point, and the guidance provided was extremely helpful."));

        Collections.shuffle(UserReviewBaseDataList);

        // Split the shuffled data into different lists
        List<UserReviewModel> vedicAstroListReview = new ArrayList<>(UserReviewBaseDataList.subList(0, 4));
        Collections.shuffle(vedicAstroListReview);

        List<UserReviewModel> careerAstroListReview = new ArrayList<>(UserReviewBaseDataList.subList(4, 8));
        Collections.shuffle(careerAstroListReview);

        List<UserReviewModel> loveListAstroReview = new ArrayList<>(UserReviewBaseDataList.subList(8, 12));
        Collections.shuffle(loveListAstroReview);

        List<UserReviewModel> marriageAstroListReview = new ArrayList<>(UserReviewBaseDataList.subList(12, 16));
        Collections.shuffle(marriageAstroListReview);


        allUserBase.add(new AstrolgerModel(1, "Harsh", R.drawable.astro_1, "Vedic Astrologer", "5 years of experience", "Hindi, English", "Employ a complex system based on ancient Hindu texts, focusing on karma and reincarnation.", "Surat, Gujarat, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        allUserBase.add(new AstrolgerModel(18, "Rahul", R.drawable.astro_m_4, "Marriage Astrologer", "8 years of experience", "English, Kannada", "Rahul is a Marriage Astrologer with 8 years of experience, proficient in English and Kannada. He specializes in post-marriage guidance.", "Bangalore, Karnataka, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        allUserBase.add(new AstrolgerModel(2, "Rahul", R.drawable.astro_2, "Vedic Astrologer", "8 years of experience", "Sanskrit, Hindi", "Rahul is an experienced Vedic Astrologer with 8 years of practice. Fluent in Sanskrit and Hindi, he specializes in predictive astrology.", "Mumbai, Maharashtra, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        allUserBase.add(new AstrolgerModel(3, "Sneha", R.drawable.astro_f_1, "Vedic Astrologer", "6 years of experience", "English, Tamil", "Sneha, a Vedic Astrologer with 6 years of experience, is proficient in English and Tamil. She excels in health predictions.", "Chennai, Tamil Nadu, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        allUserBase.add(new AstrolgerModel(4, "Kiran", R.drawable.astro_3, "Vedic Astrologer", "7 years of experience", "Gujarati, Hindi", "Kiran is a Vedic Astrologer with 7 years of expertise, specializing in financial astrology. Fluent in Gujarati and Hindi.", "Ahmedabad, Gujarat, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        allUserBase.add(new AstrolgerModel(5, "Arjun", R.drawable.astro_4, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Arjun, with 4 years of experience, is a Vedic Astrologer proficient in English and Kannada. He specializes in gemstone recommendations.", "Bangalore, Karnataka, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        allUserBase.add(new AstrolgerModel(6, "Sachin", R.drawable.astro_5, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Sachin, a Vedic Astrologer with 4 years of practice, excels in English and Kannada. His expertise lies in gemstone recommendations.", "Mysuru, Karnataka, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));

        allUserBase.add(new AstrolgerModel(19, "Pooja", R.drawable.astro_m_f_7, "Marriage Astrologer", "4 years of experience", "Gujarati, Hindi", "Pooja is a Marriage Astrologer with 4 years of experience, proficient in Gujarati and Hindi. She is an expert in resolving marital conflicts.", "Ahmedabad, Gujarat, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        allUserBase.add(new AstrolgerModel(20, "Arun", R.drawable.astro_m_5, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Arun is a Marriage Astrologer with 9 years of experience, fluent in Hindi and Tamil. He specializes in Kundli matching.", "Chennai, Tamil Nadu, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        allUserBase.add(new AstrolgerModel(21, "Arun", R.drawable.astro_m_6, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Arun is a Marriage Astrologer with 9 years of experience, fluent in Hindi and Tamil. He specializes in Kundli matching.", "Chennai, Tamil Nadu, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        allUserBase.add(new AstrolgerModel(22, "Jinal", R.drawable.astro_m_f_8, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Jinal is a Marriage Astrologer with 9 years of experience, proficient in Hindi and Tamil. She specializes in Kundli matching.", "Chennai, Tamil Nadu, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        allUserBase.add(new AstrolgerModel(7, "Jay", R.drawable.astro_c_4, "Career Astrologer", "6 years of experience", "English, Marathi", "Expert in career counseling and growth predictions", "Surat, Gujarat, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        allUserBase.add(new AstrolgerModel(8, "Amit", R.drawable.astro_c_1, "Career Astrologer", "4 years of experience", "Hindi, English", "Amit is a Career Astrologer with 4 years of experience, proficient in Hindi and English. He specializes in job change predictions.", "Mumbai, Maharashtra, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        allUserBase.add(new AstrolgerModel(9, "Priyanka", R.drawable.astro_f_c_7, "Career Astrologer", "7 years of experience", "Gujarati, English", "Priyanka, a Career Astrologer with 7 years of experience, is proficient in Gujarati and English. She excels in business and entrepreneurship predictions.", "Ahmedabad, Gujarat, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        allUserBase.add(new AstrolgerModel(10, "Raj", R.drawable.astro_c_5, "Career Astrologer", "5 years of experience", "Hindi, Tamil", "Raj is a Career Astrologer with 5 years of experience, fluent in Hindi and Tamil. He specializes in leadership and management guidance.", "Chennai, Tamil Nadu, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        // Adding the data to the VedicAstroList

        // Adding the data to the careerAstroList
        allUserBase.add(new AstrolgerModel(11, "Seema", R.drawable.astro_f_c_6, "Career Astrologer", "8 years of experience", "English, Telugu", "Seema, with 8 years of experience, is a Career Astrologer proficient in English and Telugu. She is an expert in skill development predictions.", "Hyderabad, Telangana, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        allUserBase.add(new AstrolgerModel(12, "Sita", R.drawable.astro_f_3, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Sita, a Career Astrologer with 3 years of practice, excels in Hindi and Kannada. She specializes in education and academic predictions.", "Bangalore, Karnataka, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        allUserBase.add(new AstrolgerModel(13, "Sayam", R.drawable.atstro_c_2, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Sayam is a Career Astrologer with 3 years of experience, fluent in Hindi and Kannada. He specializes in education and academic predictions.", "Bangalore, Karnataka, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        allUserBase.add(new AstrolgerModel(14, "Pooja", R.drawable.astro_f_c_8, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Pooja, with 3 years of experience, is a Career Astrologer proficient in Hindi and Kannada. She specializes in education and academic predictions.", "Bangalore, Karnataka, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        // Adding the data to the marriageAstroList
        allUserBase.add(new AstrolgerModel(15, "Manoj", R.drawable.astro_m_1, "Marriage Astrologer", "7 years of experience", "Gujarati, Hindi", "Expert in match-making and compatibility analysis", "Ahmedabad, Gujarat, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        allUserBase.add(new AstrolgerModel(16, "Sandeep", R.drawable.astro_m_2, "Marriage Astrologer", "5 years of experience", "English, Punjabi", "Sandeep is a Marriage Astrologer with 5 years of experience, proficient in English and Punjabi. He specializes in love marriage predictions.", "Chandigarh, Punjab, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        allUserBase.add(new AstrolgerModel(17, "Deepa", R.drawable.astro_m_3, "Marriage Astrologer", "6 years of experience", "Hindi, Marathi", "Deepa is a Marriage Astrologer with 6 years of experience, fluent in Hindi and Marathi. She is an expert in family and relationship counseling.", "Mumbai, Maharashtra, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        allUserBase.add(new AstrolgerModel(24, "Vikram", R.drawable.astro_l_2, "Love Astrologer", "6 years of experience", "Telugu, English", "Vikram is a Love Astrologer with 6 years of experience, proficient in Telugu and English. He specializes in relationship compatibility.", "Hyderabad, Telangana, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        allUserBase.add(new AstrolgerModel(25, "Suman", R.drawable.astro_l_3, "Love Astrologer", "5 years of experience", "Hindi, Marathi", "Suman is a Love Astrologer with 5 years of experience, fluent in Hindi and Marathi. She is an expert in resolving love conflicts.", "Mumbai, Maharashtra, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        allUserBase.add(new AstrolgerModel(26, "Piyush", R.drawable.astro_l_4, "Love Astrologer", "7 years of experience", "English, Punjabi", "Piyush is a Love Astrologer with 7 years of experience, proficient in English and Punjabi. He specializes in love horoscope analysis.", "Chandigarh, Punjab, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        allUserBase.add(new AstrolgerModel(27, "Aditya", R.drawable.astro_l_5, "Love Astrologer", "3 years of experience", "Hindi, Tamil", "Aditya is a Love Astrologer with 3 years of experience, fluent in Hindi and Tamil. He is an expert in love compatibility predictions.", "Chennai, Tamil Nadu, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        allUserBase.add(new AstrolgerModel(28, "Preeti", R.drawable.astro_l_f_6, "Love Astrologer", "8 years of experience", "Gujarati, English", "Preeti is a Love Astrologer with 8 years of experience, proficient in Gujarati and English. She specializes in love and romance predictions.", "Ahmedabad, Gujarat, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        allUserBase.add(new AstrolgerModel(29, "Ritika", R.drawable.astro_l_f_7, "Love Astrologer", "8 years of experience", "Gujarati, English", "Ritika is a Love Astrologer with 8 years of experience, proficient in Gujarati and English. She specializes in love and romance predictions.", "Ahmedabad, Gujarat, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));

        // Adding the data to the loveAstroList
        allUserBase.add(new AstrolgerModel(23, "Sameer", R.drawable.astro_l_1, "Love Astrologer", "4 years of experience", "Hindi, English", "Expert in love life guidance", "Mumbai, Maharashtra, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        allUserBase.add(new AstrolgerModel(30, "Isha", R.drawable.astro_l_f_8, "Love Astrologer", "8 years of experience", "Gujarati, English", "Isha is a Love Astrologer with 8 years of experience, proficient in Gujarati and English. She specializes in love and romance predictions.", "Ahmedabad, Gujarat, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        allUserBase.add(new AstrolgerModel(7, "Darshan", R.drawable.astro_6, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Darshan is a Vedic Astrologer with 4 years of experience, fluent in English and Kannada. He specializes in gemstone recommendations.", "Hubli, Karnataka, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        allUserBase.add(new AstrolgerModel(8, "Neha", R.drawable.astro_f_2, "Vedic Astrologer", "9 years of experience", "Hindi, Punjabi", "Neha, with 9 years of experience, is a Vedic Astrologer proficient in Hindi and Punjabi. She excels in providing spiritual guidance.", "Amritsar, Punjab, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted after request, make the call
                if (phoneNumber != null) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                    startActivity(callIntent);
                }

            } else {
                // Handle permission denied case
                Toast.makeText(requireActivity(), "Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Release the binding when the view is destroyed
    }
}
