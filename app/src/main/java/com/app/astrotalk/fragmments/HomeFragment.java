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
import com.app.astrotalk.model.AstrolgerModel;
import com.app.astrotalk.model.UserReviewModel;
import com.app.astrotalk.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private VedicCategoryAdapter vedicCategoryAdapter;
    private LoveCategoryAdapter loveCategoryAdapter;
    private MarriageCategoryAdapter marriageCategoryAdapter;
    private CareerCategoryAdapter careerCategoryAdapter;
    private List<AstrolgerModel> VedicAstroList;
    private List<AstrolgerModel> careerAstroList;
    private List<AstrolgerModel> marriageAstroList;
    private List<AstrolgerModel> loveAstroList;
    private List<UserReviewModel> vedicAstroListReview;
    private final List<UserReviewModel> UserReviewBaseDataList = new ArrayList<>();
    private List<UserReviewModel> careerAstroListReview;
    private List<UserReviewModel> loveListAstroReview;
    private List<UserReviewModel> marriageAstroListReview;
    private ActivityResultLauncher<Intent> voiceRecognizer;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // You can now use 'menuIcon' directly
        binding.edSearch.setCursorVisible(true);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Initialization();
        onClickListeners();
        loadData();
        setData();


        voiceRecognizer = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        ArrayList<String> resultList = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (resultList != null && resultList.size() > 0) {

                            String recognizedText = resultList.get(0);
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

    private void onClickListeners() {

        binding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardActivity) requireActivity()).drawerOpen();
            }
        });

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

    private boolean requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            requireActivity().setResult(RESULT_OK, intent);
            voiceRecognizer.launch(intent);
            // When permission is not granted by the user, show them a message explaining why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.RECORD_AUDIO)) {
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

        VedicAstroList = new ArrayList<>();
        careerAstroList = new ArrayList<>();
        marriageAstroList = new ArrayList<>();
        loveAstroList = new ArrayList<>();


        vedicCategoryAdapter = new VedicCategoryAdapter(requireActivity());
        careerCategoryAdapter = new CareerCategoryAdapter(requireActivity());
        marriageCategoryAdapter = new MarriageCategoryAdapter(requireActivity());
        loveCategoryAdapter = new LoveCategoryAdapter(requireActivity());

        vedicAstroListReview = new ArrayList<>();
        careerAstroListReview = new ArrayList<>();
        loveListAstroReview = new ArrayList<>();
        marriageAstroListReview = new ArrayList<>();

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
                        if ((vedicCategoryAdapter.homeList.isEmpty() && careerCategoryAdapter.homeList.isEmpty()) && (loveCategoryAdapter.homeList.isEmpty() && marriageCategoryAdapter.homeList.isEmpty())) {
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

    public void loadData() {

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
        vedicAstroListReview.addAll(UserReviewBaseDataList.subList(0, 4));
        Collections.shuffle(vedicAstroListReview);

        careerAstroListReview.addAll(UserReviewBaseDataList.subList(4, 8));
        Collections.shuffle(careerAstroListReview);

        loveListAstroReview.addAll(UserReviewBaseDataList.subList(8, 12));
        Collections.shuffle(loveListAstroReview);

        marriageAstroListReview.addAll(UserReviewBaseDataList.subList(12, 16));
        Collections.shuffle(marriageAstroListReview);

        //Sample Data Change Phone Number here
        VedicAstroList.add(new AstrolgerModel(1, "Harsh", R.drawable.astro_1, "Vedic Astrologer", "5 years of experience", "Hindi, English", "Employ a complex system based on ancient Hindu texts, focusing on karma and reincarnation.", "Surat, Gujarat, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));

        // Adding the data to the VedicAstroList
        VedicAstroList.add(new AstrolgerModel(2, "Rahul", R.drawable.astro_2, "Vedic Astrologer", "8 years of experience", "Sanskrit, Hindi", "Rahul is an experienced Vedic Astrologer with 8 years of practice. Fluent in Sanskrit and Hindi, he specializes in predictive astrology.", "Mumbai, Maharashtra, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        VedicAstroList.add(new AstrolgerModel(3, "Sneha", R.drawable.astro_f_1, "Vedic Astrologer", "6 years of experience", "English, Tamil", "Sneha, a Vedic Astrologer with 6 years of experience, is proficient in English and Tamil. She excels in health predictions.", "Chennai, Tamil Nadu, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        VedicAstroList.add(new AstrolgerModel(4, "Kiran", R.drawable.astro_3, "Vedic Astrologer", "7 years of experience", "Gujarati, Hindi", "Kiran is a Vedic Astrologer with 7 years of expertise, specializing in financial astrology. Fluent in Gujarati and Hindi.", "Ahmedabad, Gujarat, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        VedicAstroList.add(new AstrolgerModel(5, "Arjun", R.drawable.astro_4, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Arjun, with 4 years of experience, is a Vedic Astrologer proficient in English and Kannada. He specializes in gemstone recommendations.", "Bangalore, Karnataka, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        VedicAstroList.add(new AstrolgerModel(6, "Sachin", R.drawable.astro_5, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Sachin, a Vedic Astrologer with 4 years of practice, excels in English and Kannada. His expertise lies in gemstone recommendations.", "Mysuru, Karnataka, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        VedicAstroList.add(new AstrolgerModel(7, "Darshan", R.drawable.astro_6, "Vedic Astrologer", "4 years of experience", "English, Kannada", "Darshan is a Vedic Astrologer with 4 years of experience, fluent in English and Kannada. He specializes in gemstone recommendations.", "Hubli, Karnataka, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));
        VedicAstroList.add(new AstrolgerModel(8, "Neha", R.drawable.astro_f_2, "Vedic Astrologer", "9 years of experience", "Hindi, Punjabi", "Neha, with 9 years of experience, is a Vedic Astrologer proficient in Hindi and Punjabi. She excels in providing spiritual guidance.", "Amritsar, Punjab, India", Utils.vedicAstroPhoneNumberDummy, vedicAstroListReview));

        // Adding the data to the careerAstroList
        careerAstroList.add(new AstrolgerModel(7, "Jay", R.drawable.astro_c_4, "Career Astrologer", "6 years of experience", "English, Marathi", "Expert in career counseling and growth predictions", "Surat, Gujarat, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        careerAstroList.add(new AstrolgerModel(8, "Amit", R.drawable.astro_c_1, "Career Astrologer", "4 years of experience", "Hindi, English", "Amit is a Career Astrologer with 4 years of experience, proficient in Hindi and English. He specializes in job change predictions.", "Mumbai, Maharashtra, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        careerAstroList.add(new AstrolgerModel(9, "Priyanka", R.drawable.astro_f_c_7, "Career Astrologer", "7 years of experience", "Gujarati, English", "Priyanka, a Career Astrologer with 7 years of experience, is proficient in Gujarati and English. She excels in business and entrepreneurship predictions.", "Ahmedabad, Gujarat, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        careerAstroList.add(new AstrolgerModel(10, "Raj", R.drawable.astro_c_5, "Career Astrologer", "5 years of experience", "Hindi, Tamil", "Raj is a Career Astrologer with 5 years of experience, fluent in Hindi and Tamil. He specializes in leadership and management guidance.", "Chennai, Tamil Nadu, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        careerAstroList.add(new AstrolgerModel(11, "Seema", R.drawable.astro_f_c_6, "Career Astrologer", "8 years of experience", "English, Telugu", "Seema, with 8 years of experience, is a Career Astrologer proficient in English and Telugu. She is an expert in skill development predictions.", "Hyderabad, Telangana, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        careerAstroList.add(new AstrolgerModel(12, "Sita", R.drawable.astro_f_3, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Sita, a Career Astrologer with 3 years of practice, excels in Hindi and Kannada. She specializes in education and academic predictions.", "Bangalore, Karnataka, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        careerAstroList.add(new AstrolgerModel(13, "Sayam", R.drawable.atstro_c_2, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Sayam is a Career Astrologer with 3 years of experience, fluent in Hindi and Kannada. He specializes in education and academic predictions.", "Bangalore, Karnataka, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));
        careerAstroList.add(new AstrolgerModel(14, "Pooja", R.drawable.astro_f_c_8, "Career Astrologer", "3 years of experience", "Hindi, Kannada", "Pooja, with 3 years of experience, is a Career Astrologer proficient in Hindi and Kannada. She specializes in education and academic predictions.", "Bangalore, Karnataka, India", Utils.careerAstroPhoneNumberDummy, careerAstroListReview));


        // Adding the data to the marriageAstroList
        marriageAstroList.add(new AstrolgerModel(15, "Manoj", R.drawable.astro_m_1, "Marriage Astrologer", "7 years of experience", "Gujarati, Hindi", "Expert in match-making and compatibility analysis", "Ahmedabad, Gujarat, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        marriageAstroList.add(new AstrolgerModel(16, "Sandeep", R.drawable.astro_m_2, "Marriage Astrologer", "5 years of experience", "English, Punjabi", "Sandeep is a Marriage Astrologer with 5 years of experience, proficient in English and Punjabi. He specializes in love marriage predictions.", "Chandigarh, Punjab, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        marriageAstroList.add(new AstrolgerModel(17, "Deepa", R.drawable.astro_m_3, "Marriage Astrologer", "6 years of experience", "Hindi, Marathi", "Deepa is a Marriage Astrologer with 6 years of experience, fluent in Hindi and Marathi. She is an expert in family and relationship counseling.", "Mumbai, Maharashtra, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        marriageAstroList.add(new AstrolgerModel(18, "Rahul", R.drawable.astro_m_4, "Marriage Astrologer", "8 years of experience", "English, Kannada", "Rahul is a Marriage Astrologer with 8 years of experience, proficient in English and Kannada. He specializes in post-marriage guidance.", "Bangalore, Karnataka, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        marriageAstroList.add(new AstrolgerModel(19, "Pooja", R.drawable.astro_m_f_7, "Marriage Astrologer", "4 years of experience", "Gujarati, Hindi", "Pooja is a Marriage Astrologer with 4 years of experience, proficient in Gujarati and Hindi. She is an expert in resolving marital conflicts.", "Ahmedabad, Gujarat, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        marriageAstroList.add(new AstrolgerModel(20, "Arun", R.drawable.astro_m_5, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Arun is a Marriage Astrologer with 9 years of experience, fluent in Hindi and Tamil. He specializes in Kundli matching.", "Chennai, Tamil Nadu, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        marriageAstroList.add(new AstrolgerModel(21, "Arun", R.drawable.astro_m_6, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Arun is a Marriage Astrologer with 9 years of experience, fluent in Hindi and Tamil. He specializes in Kundli matching.", "Chennai, Tamil Nadu, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));
        marriageAstroList.add(new AstrolgerModel(22, "Jinal", R.drawable.astro_m_f_8, "Marriage Astrologer", "9 years of experience", "Hindi, Tamil", "Jinal is a Marriage Astrologer with 9 years of experience, proficient in Hindi and Tamil. She specializes in Kundli matching.", "Chennai, Tamil Nadu, India", Utils.marriageAstroPhoneNumberDummy, marriageAstroListReview));

        // Adding the data to the loveAstroList
        loveAstroList.add(new AstrolgerModel(23, "Sameer", R.drawable.astro_l_1, "Love Astrologer", "4 years of experience", "Hindi, English", "Expert in love life guidance", "Mumbai, Maharashtra, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        loveAstroList.add(new AstrolgerModel(24, "Vikram", R.drawable.astro_l_2, "Love Astrologer", "6 years of experience", "Telugu, English", "Vikram is a Love Astrologer with 6 years of experience, proficient in Telugu and English. He specializes in relationship compatibility.", "Hyderabad, Telangana, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        loveAstroList.add(new AstrolgerModel(25, "Suman", R.drawable.astro_l_3, "Love Astrologer", "5 years of experience", "Hindi, Marathi", "Suman is a Love Astrologer with 5 years of experience, fluent in Hindi and Marathi. She is an expert in resolving love conflicts.", "Mumbai, Maharashtra, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        loveAstroList.add(new AstrolgerModel(26, "Piyush", R.drawable.astro_l_4, "Love Astrologer", "7 years of experience", "English, Punjabi", "Piyush is a Love Astrologer with 7 years of experience, proficient in English and Punjabi. He specializes in love horoscope analysis.", "Chandigarh, Punjab, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        loveAstroList.add(new AstrolgerModel(27, "Aditya", R.drawable.astro_l_5, "Love Astrologer", "3 years of experience", "Hindi, Tamil", "Aditya is a Love Astrologer with 3 years of experience, fluent in Hindi and Tamil. He is an expert in love compatibility predictions.", "Chennai, Tamil Nadu, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        loveAstroList.add(new AstrolgerModel(28, "Preeti", R.drawable.astro_l_f_6, "Love Astrologer", "8 years of experience", "Gujarati, English", "Preeti is a Love Astrologer with 8 years of experience, proficient in Gujarati and English. She specializes in love and romance predictions.", "Ahmedabad, Gujarat, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        loveAstroList.add(new AstrolgerModel(29, "Ritika", R.drawable.astro_l_f_7, "Love Astrologer", "8 years of experience", "Gujarati, English", "Ritika is a Love Astrologer with 8 years of experience, proficient in Gujarati and English. She specializes in love and romance predictions.", "Ahmedabad, Gujarat, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));
        loveAstroList.add(new AstrolgerModel(30, "Isha", R.drawable.astro_l_f_8, "Love Astrologer", "8 years of experience", "Gujarati, English", "Isha is a Love Astrologer with 8 years of experience, proficient in Gujarati and English. She specializes in love and romance predictions.", "Ahmedabad, Gujarat, India", Utils.loveAstroPhoneNumberDummy, loveListAstroReview));

    }

    private void visitProfile(AstrolgerModel astrologer) {

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


    private void setData() {

        vedicCategoryAdapter.setData(VedicAstroList, new OnCategoryItemClick() {
            @Override
            public void onItemClick(int position, AstrolgerModel astrologer) {
                visitProfile(astrologer);
            }
        });
        marriageCategoryAdapter.setData(marriageAstroList, new OnCategoryItemClick() {
            @Override
            public void onItemClick(int position, AstrolgerModel astrologer) {
                visitProfile(astrologer);
            }
        });
        careerCategoryAdapter.setData(careerAstroList, new OnCategoryItemClick() {
            @Override
            public void onItemClick(int position, AstrolgerModel astrologer) {
                visitProfile(astrologer);
            }
        });

        loveCategoryAdapter.setData(loveAstroList, new OnCategoryItemClick() {
            @Override
            public void onItemClick(int position, AstrolgerModel astrologer) {

                visitProfile(astrologer);


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