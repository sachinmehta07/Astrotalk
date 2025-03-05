package com.app.astrotalk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.app.astrotalk.R;
import com.app.astrotalk.activity.DashboardActivity;
import com.app.astrotalk.activity.LoginActivity;
import com.app.astrotalk.activity.SignupActivity;
import com.app.astrotalk.model.PoojaBookModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utils {
    public static String searchTextValue;

    public static boolean isUserLogin = false;


    //Add Your Sample Phone Number
    public static String vedicAstroPhoneNumberDummy = "8320577653";
    public static String loveAstroPhoneNumberDummy = "8320577653";
    public static String careerAstroPhoneNumberDummy = "8320577653";
    public static String marriageAstroPhoneNumberDummy = "8320577653";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

            return networkCapabilities != null &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }

        return false;
    }

    public static List<PoojaBookModel> getPoojaBookList() {

        List<PoojaBookModel> poojaBookList = new ArrayList<>();
        // Add data to poojaBookList
        poojaBookList.add(new PoojaBookModel(R.drawable.pooj_20, 1, "Ganesh Puja", "Ganesh Puja is a Hindu festival that celebrates the birth of Lord Ganesha, the elephant-headed god of wisdom and good beginnings. It is one of the most popular festivals in India and is celebrated by Hindus all over the world.", "Ganesh Puja", "Ganesh Puja is believed to bring good luck, prosperity, and wisdom. It is also a time for families to come together and celebrate.", "Lord Ganesha is the son of Lord Shiva and Parvati. He is known for his wisdom, intelligence, and ability to remove obstacles. He is also worshipped as the god of beginnings, new ventures, and education.", 2000));
        // Add more PoojaBookModel objects similarly
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_4, 2, "Durga Puja", "Durga Puja is a Hindu festival that celebrates the victory of good over evil. It is a ten-day festival that is celebrated in the month of September or October. The festival marks the victory of the goddess Durga over the demon Mahishasura.", "Durga Puja", "Durga Puja is believed to bring strength, courage, and victory. It is also a time for families to come together and celebrate.", "Goddess Durga is the slayer of demons. She is known for her strength, power, and courage. She is also worshipped as the mother goddess and the protector of the universe.", 1000));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_9, 3, "Diwali Puja", "Diwali is a Hindu festival that celebrates the victory of good over evil. It is a five-day festival that is celebrated in the month of October or November. The festival marks the return of Lord Rama to Ayodhya after his fourteen-year exile.", "Diwali", "Diwali is believed to bring good luck, prosperity, and happiness. It is also a time for families to come together and celebrate.", "Lord Rama is the seventh avatar of Lord Vishnu. He is known for his righteousness, courage, and devotion to his wife, Sita.", 1500));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_2, 4, "Navratri", "Navratri is a Hindu festival that celebrates the divine feminine. It is a nine-day festival that is celebrated in the month of September or October. The festival is dedicated to the nine forms of Durga.", "Navratri", "Navratri is believed to bring strength, courage, and prosperity. It is also a time for families to come together and celebrate.", "Durga, the slayer of demons, is worshipped in her nine forms during Navratri. Each form represents a different aspect of the divine feminine.", 2000));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_5, 5, "Maha Shivratri", "Maha Shivratri is a Hindu festival that celebrates the marriage of Lord Shiva and Parvati. It is celebrated on the 14th day of the dark half of the Hindu month of Phalguna, which usually falls in February or March.", "Maha Shivratri", "Maha Shivratri is believed to bring peace, prosperity, and liberation. It is also a time for devotees to offer prayers and seek blessings from Lord Shiva.", "Lord Shiva is one of the three principal deities of Hinduism. He is known as the destroyer and the regenerator, and is associated with power, transformation, and the cycle of life and death.", 2500));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_12, 6, "Krishna Janmashtami", "Krishna Janmashtami is a Hindu festival that celebrates the birth of Lord Krishna, the eighth avatar of Lord Vishnu. It is celebrated on the eighth day of the dark half of the Hindu month of Shraavana, which usually falls in August or September.", "Krishna Janmashtami", "Krishna Janmashtami is believed to bring love, peace, and happiness. It is also a time for families to come together and celebrate.", "Lord Krishna is one of the most popular Hindu deities. He is known for his playfulness, wisdom, and love for humanity.", 500));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_13, 7, "Raksha Bandhan", "Raksha Bandhan is a Hindu festival that celebrates the bond between brother and sister. It is celebrated on the full moon day of the Hindu month of Shraavana, which usually falls in August.", "Raksha Bandhan", "Raksha Bandhan is believed to strengthen the bond between siblings and to bring protection and prosperity.", "The festival is not directly associated with a specific deity, but some families may pray to Lord Yama, the god of death, for the protection of their brothers.", 2500));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_8, 8, "Holi", "Holi is a Hindu festival that celebrates the victory of good over evil and the arrival of spring. It is also known as the Festival of Colors.", "Holi", "Holi is believed to bring joy, happiness, and new beginnings. It is also a time for families and friends to come together and celebrate.", "The festival is associated with several Hindu deities, including Krishna, Shiva, and Parvati.", 300));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_9, 9, "Navami", "Navami is the ninth day of a nine-day Hindu festival, such as Navratri or Durga Puja. It is considered a particularly auspicious day for worship and fasting.", "Navami", "The benefits of Navami vary depending on the specific festival it falls within. However, it is generally believed to bring blessings, prosperity, and spiritual growth.", "The specific deity worshipped on Navami depends on the festival. In Navratri, it is the ninth form of Durga, called Siddhidatri.", 5000));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_11, 10, "Hanuman Jayanti", "Hanuman Jayanti is a Hindu festival that celebrates the birth of Lord Hanuman, the monkey god and a devoted follower of Lord Rama. It is celebrated on the fifteenth day of the bright half of the Hindu month of Chaitra, which usually falls in March or April.", "Hanuman Jayanti", "Hanuman Jayanti is believed to bring strength, courage, and devotion. It is also a time for devotees to offer prayers and seek blessings from Lord Hanuman.", "Lord Hanuman is known for his immense strength, loyalty, and unwavering devotion to Lord Rama. He is considered a symbol of courage, service, and selflessness.", 2500));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_14, 11, "Ram Navami", "Ram Navami is a Hindu festival that celebrates the birth of Lord Rama, the seventh avatar of Lord Vishnu. It is celebrated on the ninth day of the bright half of the Hindu month of Chaitra, which usually falls in March or April.", "Ram Navami", "Ram Navami is believed to bring righteousness, courage, and devotion. It is also a time for families to come together and celebrate.", "Lord Rama is considered an ideal son, brother, husband, and king. He is worshipped for his righteousness, courage, and devotion to his duties.", 3200));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_15, 12, "Ganga Dussehra", "Ganga Dussehra is a Hindu festival that celebrates the descent of the Ganges River from heaven to earth. It is celebrated on the tenth day of the bright half of the Hindu month of Jyeshtha, which usually falls in May or June.", "Ganga Dussehra", "Ganga Dussehra is believed to bring purification, peace, and prosperity. It is also a time for Hindus to offer prayers and bathe in the Ganges River, which is considered sacred.", "The Ganges River is considered a holy river in Hinduism and is believed to have the power to wash away sins.", 5500));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_16, 13, "Guru Purnima", "Guru Purnima is a Hindu festival that honors spiritual teachers and gurus. It is celebrated on the full moon day of the Hindu month of Ashadha, which usually falls in July.", "Guru Purnima", "Guru Purnima is believed to bring blessings, wisdom, and guidance. It is also a time for students to express their gratitude to their teachers.", "The festival is not directly associated with a specific deity, but it is a time to remember and honor all spiritual teachers who have guided us on our life's journey.", 1200));
        poojaBookList.add(new PoojaBookModel(R.drawable.pooja_17, 14, "Shravan", "Shravan is a holy month in the Hindu calendar, dedicated to Lord Shiva. It is the fifth month of the lunar year, usually falling in July and August. During this month, devotees observe various rituals and fasts to seek blessings from Lord Shiva.", "Shravan", "Observing Shravan is believed to bring blessings, peace, and good health. It is also a time for spiritual introspection and devotion.", "Lord Shiva is one of the three principal deities of Hinduism. He is associated with destruction, creation, and transformation.", 4500));
        return poojaBookList;
    }

    public static void showLoginDialog(Context context, String title, Runnable onLoginAction) {
        if (!(context instanceof Activity)) {
            return; // Ensure we have a valid Activity context
        }

        Activity activity = (Activity) context;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_exit, null);
        builder.setView(dialogView);

        TextView titleView = dialogView.findViewById(R.id.text_title);
        titleView.setText(title);

        // Create the dialog instance
        final AlertDialog dialog = builder.create();

        // Set transparent background and apply custom background drawable
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogView.setBackgroundResource(R.drawable.dialog_bg);

        Button btnYes = dialogView.findViewById(R.id.btn_yes);
        Button btnNo = dialogView.findViewById(R.id.btn_no);

        btnYes.setText(R.string.sign_in_login);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                context.startActivity(new Intent(context, SignupActivity.class));


            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }

    /**
     * Simplified version of login dialog without custom actions
     */
    public static void showLoginDialog(Context context, String title) {
        showLoginDialog(context, title, null);
    }

    /**
     * Shows login dialog with default title
     */
    public static void showLoginDialog(Context context) {
        showLoginDialog(context, context.getString(R.string.login_required));
    }
}
