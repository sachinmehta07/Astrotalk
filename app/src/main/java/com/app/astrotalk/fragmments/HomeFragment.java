package com.app.astrotalk.fragmments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.activity.result.contract.ActivityResultContracts;

import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.astrotalk.R;
import com.app.astrotalk.activity.DashboardActivity;
import com.app.astrotalk.activity.ProfileActivity;
import com.app.astrotalk.adapter.CareerCategoryAdapter;
import com.app.astrotalk.adapter.LoveCategoryAdapter;
import com.app.astrotalk.adapter.MarriageCategoryAdapter;
import com.app.astrotalk.adapter.VedicCategoryAdapter;
import com.app.astrotalk.databinding.FragmentHomeBinding;
import com.app.astrotalk.listeners.OnCategoryItemClick;
import com.app.astrotalk.model.AllCategory;
import com.app.astrotalk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ProgressDialog progressDialog;
    //new Changes
    private boolean isLoading = false;
    private FragmentHomeBinding binding;

    //Category Wise Data
    private VedicCategoryAdapter vedicCategoryAdapter;
    private LoveCategoryAdapter loveCategoryAdapter;
    private MarriageCategoryAdapter marriageCategoryAdapter;
    private CareerCategoryAdapter careerCategoryAdapter;

    private ActivityResultLauncher<Intent> voiceRecognizer;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Access views using binding
        binding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardActivity) requireActivity()).drawerOpen();
            }
        });


        // You can now use 'menuIcon' directly
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        binding.edSearch.setCursorVisible(true);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Initialization();
        onClickListeners();
        apiHomeData();

        voiceRecognizer = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        ArrayList<String> resultList = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (resultList != null && resultList.size() > 0) {

                            String recognizedText = resultList.get(0);
                            setSearchResult(recognizedText);
                            binding.edSearch.setText(recognizedText);

                            //Utils.searchTextValue = recognizedText;
                        }
                    }
                }
            }
        });
    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        requireActivity().setResult(RESULT_OK, intent);
        voiceRecognizer.launch(intent);
    }

    public void setSearchResult(String value) {
//        bindingte.txSS.setText(value);
        //binding.txAllCategoriesList.setText(value);
    }

    private void onClickListeners() {

        binding.edSearch.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start, int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) {// for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z ]+")) {
                            return cs;
                        } else if (cs.toString().matches("[0-9]+")) {
                            Toast.makeText(requireActivity(), "Numbers are not allowed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity(), "Special characters are not allowed", Toast.LENGTH_SHORT).show();
                        }
                        return "";
                    }
                }
        });

        binding.ivMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(requireActivity())) {
//                    PermissionManager.Builder().key(2100).permission(new PermissionManager().getStoragePermissionList()).askAgain(true).askAgainCallback(response -> PermissionManager.showDialog(getActivity(), response)).callback(allPermissionsGranted -> {
//                        if (allPermissionsGranted) {
//                            displaySpeechRecognizer();
//                        } else {
//                            PermissionManager.showSettingDialog(getActivity());
//                        }
//                    }).ask(getActivity());

                    if (requestAudioPermissions()) {
                        displaySpeechRecognizer();
                    }
                } else {
                    Toast.makeText(requireActivity(), R.string.no_internet_connection_found_ncheck_your_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edSearch.setText("");
                binding.ivRemove.setVisibility(View.INVISIBLE);
                binding.ivMic.setVisibility(View.VISIBLE);
            }
        });

    }

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private boolean requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // When permission is not granted by the user, show them a message explaining why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(requireActivity(), "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                // Give the user the option to still opt-in for the permissions
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show a dialog to grant permission to record audio
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }

            // Permission not granted yet
            return false;
        } else {
            // Permission is already granted
            return true;
        }
    }

    private void Initialization() {

        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                if (Utils.isNetworkAvailable(requireActivity())) {

                    vedicCategoryAdapter.filter(cs.toString());
                    careerCategoryAdapter.filter(cs.toString());
                    loveCategoryAdapter.filter(cs.toString());
                    marriageCategoryAdapter.filter(cs.toString());

                    binding.ivRemove.setVisibility(View.VISIBLE);
                    binding.ivMic.setVisibility(View.INVISIBLE);

                    if (vedicCategoryAdapter != null && careerCategoryAdapter != null && loveCategoryAdapter != null && marriageCategoryAdapter != null) {
                        if ((vedicCategoryAdapter.homeList.size() == 0 && careerCategoryAdapter.homeList.size() == 0) && ( loveCategoryAdapter.homeList.size() == 0 && marriageCategoryAdapter.homeList.size() == 0)) {
                            binding.txNoResult.setVisibility(View.VISIBLE);
                            binding.txtVedicAstrology.setVisibility(GONE);
                            binding.txtLoveAstrology.setVisibility(GONE);
                            binding.txtCareerAstro.setVisibility(GONE);
                            binding.txtMarriageAstro.setVisibility(GONE);
                        } else {
                            binding.txtVedicAstrology.setVisibility(View.VISIBLE);
                            binding.txtLoveAstrology.setVisibility(View.VISIBLE);
                            binding.txtCareerAstro.setVisibility(View.VISIBLE);
                            binding.txtMarriageAstro.setVisibility(View.VISIBLE);
                            binding.txNoResult.setVisibility(GONE);
                        }
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
                    binding.ivMic.setVisibility(View.VISIBLE);
                }
                // TODO Auto-generated method stub
            }
        });

    }

    private void apiHomeData() {

        binding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardActivity) requireActivity()).drawerOpen();
            }
        });

        List<AllCategory> VedicAstroList = new ArrayList<>();
        List<AllCategory> careerAstroList = new ArrayList<>();
        List<AllCategory> marriageAstroList = new ArrayList<>();
        List<AllCategory> loveAstroList = new ArrayList<>();

        vedicCategoryAdapter = new VedicCategoryAdapter(requireActivity());
        careerCategoryAdapter = new CareerCategoryAdapter(requireActivity());
        marriageCategoryAdapter = new MarriageCategoryAdapter(requireActivity());
        loveCategoryAdapter = new LoveCategoryAdapter(requireActivity());


        // Vedic Astrologers
        VedicAstroList.add(new AllCategory(1, "Harsh", R.drawable.astro_1, "Vedic Astrologer", "5 years of experience", "Hindi, English", "Specialized in career and relationships"));
        VedicAstroList.add(new AllCategory(2, "Rahul", R.drawable.astro_2, "Vedic Astrologer", "8 years of experience", "Sanskrit, Hindi", "Expert in predictive astrology"));
        VedicAstroList.add(new AllCategory(3, "Sneha", R.drawable.astro_f_1, "Vedic Astrologer", "6 years of experience", "English, Tamil", "Specialized in health predictions"));
        VedicAstroList.add(new AllCategory(4, "Kiran", R.drawable.astro_3, "Vedic Astrologer", "7 years of experience", "Gujarati, Hindi", "Expert in financial astrology"));
        VedicAstroList.add(new AllCategory(5, "Arjun", R.drawable.astro_4, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Specialized in gemstone recommendations"));
        VedicAstroList.add(new AllCategory(6, "Sachin", R.drawable.astro_5, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Specialized in gemstone recommendations"));
        VedicAstroList.add(new AllCategory(7, "Darshan", R.drawable.astro_6, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Specialized in gemstone recommendations"));
        VedicAstroList.add(new AllCategory(8, "Neha", R.drawable.astro_f_2, "Vedic Astrologer", "9 years of experience", "Hindi, Punjabi", "Expert in spiritual guidance"));

// Career Astrologers
        careerAstroList.add(new AllCategory(1, "Jay", R.drawable.astro_c_4, "Career Astrologer", "6 years of experience", "English, Marathi", "Expert in career counseling and growth predictions"));
        careerAstroList.add(new AllCategory(2, "Amit", R.drawable.astro_c_1, "Career Astrologer", "4 years of experience", "Hindi, English", "Specialized in job change predictions"));
        careerAstroList.add(new AllCategory(3, "Priyanka", R.drawable.astro_f_c_7, "Career Astrologer", "7 years of experience", "Gujarati, English", "Expert in business and entrepreneurship predictions"));
        careerAstroList.add(new AllCategory(4, "Raj", R.drawable.astro_c_5, "Career Astrologer", "5 years of experience", "Hindi, Tamil", "Specialized in leadership and management guidance"));
        careerAstroList.add(new AllCategory(5, "Seema", R.drawable.astro_f_c_6, "Career Astrologer", "8 years of experience", "English, Telugu", "Expert in skill development predictions"));
        careerAstroList.add(new AllCategory(6, "Sita", R.drawable.astro_f_3, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Specialized in education and academic predictions"));
        careerAstroList.add(new AllCategory(7, "Sayam", R.drawable.atstro_c_2, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Specialized in education and academic predictions"));
        careerAstroList.add(new AllCategory(8, "Pooja", R.drawable.astro_f_c_8, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Specialized in education and academic predictions"));

// Marriage Astrologers
        marriageAstroList.add(new AllCategory(1, "Manoj", R.drawable.astro_m_1, "Marriage Astrologer", "7 years of experience", "Gujarati, Hindi", "Expert in match-making and compatibility analysis"));
        marriageAstroList.add(new AllCategory(2, "Sandeep", R.drawable.astro_m_2, "Marriage Astrologer", "5 years of experience", "English, Punjabi", "Specialized in love marriage predictions"));
        marriageAstroList.add(new AllCategory(3, "Deepa", R.drawable.astro_m_3, "Marriage Astrologer", "6 years of experience", "Hindi, Marathi", "Expert in family and relationship counseling"));
        marriageAstroList.add(new AllCategory(4, "Rahul", R.drawable.astro_m_4, "Marriage Astrologer", "8 years of experience", "English, Kannada", "Specialized in post-marriage guidance"));
        marriageAstroList.add(new AllCategory(5, "Pooja", R.drawable.astro_m_f_7, "Marriage Astrologer", "4 years of experience", "Gujarati, Hindi", "Expert in resolving marital conflicts"));
        marriageAstroList.add(new AllCategory(6, "Arun", R.drawable.astro_m_5, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Specialized in Kundli matching"));
        marriageAstroList.add(new AllCategory(7, "Arun", R.drawable.astro_m_6, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Specialized in Kundli matching"));
        marriageAstroList.add(new AllCategory(8, "Jinal", R.drawable.astro_m_f_8, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Specialized in Kundli matching"));

// Love Astrologers
        loveAstroList.add(new AllCategory(1, "Love", R.drawable.astro_l_1, "Love Astrologer", "4 years of experience", "Hindi, English", "Expert in love life guidance"));
        loveAstroList.add(new AllCategory(2, "Vikram", R.drawable.astro_l_2, "Love Astrologer", "6 years of experience", "Telugu, English", "Specialized in relationship compatibility"));
        loveAstroList.add(new AllCategory(3, "Suman", R.drawable.astro_l_3, "Love Astrologer", "5 years of experience", "Hindi, Marathi", "Expert in resolving love conflicts"));
        loveAstroList.add(new AllCategory(4, "Piyush", R.drawable.astro_l_4, "Love Astrologer", "7 years of experience", "English, Punjabi", "Specialized in love horoscope analysis"));
        loveAstroList.add(new AllCategory(5, "Aditya", R.drawable.astro_l_5, "Love Astrologer", "3 years of experience", "Hindi, Tamil", "Expert in love compatibility predictions"));
        loveAstroList.add(new AllCategory(6, "Preeti", R.drawable.astro_l_f_6, "Love Astrologer", "8 years of experience", "Gujarati, English", "Specialized in love and romance predictions"));
        loveAstroList.add(new AllCategory(6, "Ritika", R.drawable.astro_l_f_7, "Love Astrologer", "8 years of experience", "Gujarati, English", "Specialized in love and romance predictions"));
        loveAstroList.add(new AllCategory(6, "Isha", R.drawable.astro_l_f_8, "Love Astrologer", "8 years of experience", "Gujarati, English", "Specialized in love and romance predictions"));


        Log.d("TAG", "apiHomeData: " + VedicAstroList.size());
        vedicCategoryAdapter.setData(VedicAstroList, new OnCategoryItemClick() {
            @Override
            public void onItemClick(int position, AllCategory astrologer) {

                Intent intent = new Intent(requireActivity(), ProfileActivity.class);
                intent.putExtra("profilePicUrl", astrologer.getImageD()); // Replace with the actual URL
                intent.putExtra("name", astrologer.getName());
                intent.putExtra("astrologyType", astrologer.getAstroType());
                intent.putExtra("experience", astrologer.getAstroExp());
                intent.putExtra("language", astrologer.getAstroLang());
                intent.putExtra("aboutAstrology", astrologer.getAstroAbout());

                // Start the ProfileActivity
                startActivity(intent);
            }
        });

        marriageCategoryAdapter.setData(marriageAstroList, new OnCategoryItemClick() {
            @Override
            public void onItemClick(int position, AllCategory astrologer) {

                Intent intent = new Intent(requireActivity(), ProfileActivity.class);
                intent.putExtra("profilePicUrl", astrologer.getImageD()); // Replace with the actual URL
                intent.putExtra("name", astrologer.getName());
                intent.putExtra("astrologyType", astrologer.getAstroType());
                intent.putExtra("experience", astrologer.getAstroExp());
                intent.putExtra("language", astrologer.getAstroLang());
                intent.putExtra("aboutAstrology", astrologer.getAstroAbout());

                // Start the ProfileActivity
                startActivity(intent);
            }
        });


        careerCategoryAdapter.setData(careerAstroList, new OnCategoryItemClick() {
            @Override
            public void onItemClick(int position, AllCategory astrologer) {

                Intent intent = new Intent(requireActivity(), ProfileActivity.class);
                intent.putExtra("profilePicUrl", astrologer.getImageD()); // Replace with the actual URL
                intent.putExtra("name", astrologer.getName());
                intent.putExtra("astrologyType", astrologer.getAstroType());
                intent.putExtra("experience", astrologer.getAstroExp());
                intent.putExtra("language", astrologer.getAstroLang());
                intent.putExtra("aboutAstrology", astrologer.getAstroAbout());

                // Start the ProfileActivity
                startActivity(intent);
            }
        });

        loveCategoryAdapter.setData(loveAstroList, new OnCategoryItemClick() {
            @Override
            public void onItemClick(int position, AllCategory astrologer) {

                Intent intent = new Intent(requireActivity(), ProfileActivity.class);
                intent.putExtra("profilePicUrl", astrologer.getImageD()); // Replace with the actual URL
                intent.putExtra("name", astrologer.getName());
                intent.putExtra("astrologyType", astrologer.getAstroType());
                intent.putExtra("experience", astrologer.getAstroExp());
                intent.putExtra("language", astrologer.getAstroLang());
                intent.putExtra("aboutAstrology", astrologer.getAstroAbout());

                // Start the ProfileActivity
                startActivity(intent);
            }
        });

        binding.rvVedicAstrology.setAdapter(vedicCategoryAdapter);
        binding.rvCareerAstro.setAdapter(careerCategoryAdapter);
        binding.rvLoveAstrology.setAdapter(loveCategoryAdapter);
        binding.rvMarriageAstro.setAdapter(marriageCategoryAdapter);


        //SetText of Voice Speech To Text
        if (Utils.searchTextValue != null && Utils.searchTextValue.length() > 0) {
            binding.edSearch.setText(Utils.searchTextValue);
            Utils.searchTextValue = "";
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Release the binding when the view is destroyed
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_RECORD_AUDIO) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay!
                displaySpeechRecognizer();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(requireActivity(), "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
            }

        }
    }
}