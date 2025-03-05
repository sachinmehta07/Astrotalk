package com.app.astrotalk.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.astrotalk.R;
import com.app.astrotalk.adapter.ChatAdapter;
import com.app.astrotalk.databinding.ActivityChatToAstroBinding;
import com.app.astrotalk.utils.SharedPreferenceManager;

import java.util.List;
import java.util.Random;

public class ChatToAstroActivity extends AppCompatActivity {
    private ChatAdapter chatAdapter;
    private ActivityChatToAstroBinding activityChatToAstroBinding;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatToAstroBinding = ActivityChatToAstroBinding.inflate(getLayoutInflater());
        setContentView(activityChatToAstroBinding.getRoot());
        userId = getIntent().getStringExtra("userId");
        int userPic = getIntent().getIntExtra("profilePicUrl", 0);
        activityChatToAstroBinding.ivProfile.setImageResource(userPic); // Set default profile pic or load from URL
        activityChatToAstroBinding.txtUserName.setText(getIntent().getStringExtra("name"));

        chatAdapter = new ChatAdapter(this);
        activityChatToAstroBinding.chatRecyclerView.setAdapter(chatAdapter);
        activityChatToAstroBinding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        activityChatToAstroBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activityChatToAstroBinding.messageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userMessage = activityChatToAstroBinding.chatMessageInput.getText().toString().trim();

                if (userMessage.isEmpty()) {
                    Toast.makeText(ChatToAstroActivity.this, "Please add Message", Toast.LENGTH_SHORT).show();
                } else {
                    chatAdapter.addMessage(userMessage);
                    saveChatMessage(userId, userMessage);
                    // Simulate app response (you can replace this logic with your own)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String appResponse = getResponse(userMessage);
                            chatAdapter.addMessage(appResponse);

                            // Scroll to the last item in the RecyclerView (auto-scroll)
                            activityChatToAstroBinding.chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                        }
                    }, 500);

                    activityChatToAstroBinding.chatMessageInput.getText().clear();
                }

            }
        });
        loadChatMessages(userId);

    }

    private void saveChatMessage(String userId, String message) {
        // Use the userId as a key to store chat messages
        String key = "chat_" + userId;

        // Retrieve existing chat messages
        List<String> existingMessages = SharedPreferenceManager.getInstance(this).getChatMessages(key);

        // Append the new message
        existingMessages.add(message);

        // Save the updated messages
        SharedPreferenceManager.getInstance(this).setChatMessages(key, existingMessages);
    }

    private void loadChatMessages(String userId) {
        String key = "chat_" + userId;

        // Retrieve chat messages from SharedPreferences
        List<String> chatMessages = SharedPreferenceManager.getInstance(this).getChatMessages(key);

        // Update the RecyclerView
        chatAdapter.setChatList(chatMessages);
    }
    // Sample logic for generating app responses
    private String getResponse(String userMessage) {

        String[] keywords = {
                "career",
                "job",
                "profession",
                "employment",
                "vocation",
                "occupation",
                "work",
                "livelihood",
                "marriage",
                "wedding",
                "matrimony",
                "union",
                "nuptials",
                "spouse",
                "partner",
                "husband",
                "wife",
                "children",
                "kids",
                "offspring",
                "family",
                "love",
                "affection",
                "romance",
                "relationship",
                "passion",
                "emotion",
                "attachment",
                "money",
                "finance",
                "wealth",
                "currency",
                "funds",
                "capital",
                "savings",
                "health",
                "wellness",
                "fitness",
                "well-being",
                "physical condition",
                "future",
                "future prospects",
                "outlook",
                "prediction",
                "forecast",
                "projection",
                "girlfriend",
                "partner",
                "significant other",
                "companion",
                "mate",
                "hello",
                "hi",
                "hii",
                "morning",
                "afternoon",
                "evening",
                "hey",
                "hy",
                "help",
                "hyy",
        };
        // Check if any keyword is present in the user's message
        for (String keyword : keywords) {
            if (userMessage.toLowerCase().contains(keyword)) {
                // Return a specific response for the matched keyword
                return getSpecificResponse(keyword);
            }
        }

        // If no keyword is matched, return a general response
        return getDefaultResponse();
    }

//    private String getSpecificResponse(String keyword) {
//        // Map specific responses for each keyword
//        // You can customize this based on your app's logic
//        switch (keyword) {
//            case "job":
//                return "Consider updating your resume and networking to find new job opportunities.";
//            case "profession":
//                return "Continue to develop your professional skills and seek advancement in your field.";
//            case "employment":
//                return "Explore different employment options and consider both full-time and part-time opportunities.";
//            case "vocation":
//                return "Discover your true calling and pursue a career path that aligns with your passions.";
//            case "occupation":
//                return "Engage in activities that enhance your skills and knowledge within your chosen occupation.";
//            case "career":
//                return "Focus on your skills and explore new opportunities in your career.";
//            case "marriage":
//                return "Building a strong foundation is crucial for a successful marriage. Communication is key.";
//            case "children":
//                return "Children bring joy and responsibility. Ensure a supportive environment for their growth.";
//            case "love":
//                return "Love is a beautiful journey. Communication, trust, and understanding are vital.";
//            case "money":
//                return "Financial stability is essential. Plan your expenses wisely and consider investments.";
//            case "health":
//                return "Maintain a healthy lifestyle. Regular exercise and a balanced diet contribute to well-being.";
//            case "future":
//                return "The future is uncertain, but you can shape it with your actions today.";
//            case "prediction":
//                return "Predicting the future is complex. Focus on the present and make the most of your opportunities.";
//            case "girlfriend":
//                return "A strong relationship is built on trust, communication, and mutual respect.";
//            case "wife":
//                return "Nurturing a strong marital bond requires love, patience, and open communication.";
//            case "hello":
//                return "Hello! How can I assist you today?";
//            case "hi":
//                return "Hello! How can I assist you today?";
//            case "hii":
//                return "Hello! How can I assist you today?";
//            case "hey":
//                return "Hello! How can I assist you today?";
//            case "hyy":
//                return "Hello! How can I assist you today?";
//            case "hy":
//                return "Hello! How can I assist you today?";
//            case "hyyy":
//                return "Hello! How can I assist you today?";
//            case "morning":
//                return "Good morning! I hope you have a wonderful day ahead.";
//            case "afternoon":
//                return "Good afternoon! How may I help you?";
//            case "evening":
//                return "Good evening! Is there anything I can assist you with?";
//            case "help":
//                return "Sure, I'm here to help. What do you need assistance with?";
//            // Add more cases for other keywords and their respective responses...
//            default:
//                return "I'm sorry, I'm not sure how to respond to that. How can I assist you?";
//        }
//    }

    private String getSpecificResponse(String keyword) {
        // Map specific responses for each keyword and their variations

        // Convert keyword to lowercase for case-insensitive comparison
        keyword = keyword.toLowerCase();

        if (keyword.contains("job")) {
            return "Consider updating your resume and exploring new job opportunities that align with your skills and interests. Analyzing the transits of planets like Jupiter and Saturn can offer insights into favorable periods for job searches. Remember, effort and perseverance are key.";
        } else if (keyword.contains("profession")) {
            return "Explore different employment options, considering both full-time and part-time opportunities. Analyzing the planetary positions in your 6th house can offer insights into suitable work environments and potential for job satisfaction. Remember, a positive attitude and adaptability are valuable assets in the job market.";
        } else if (keyword.contains("employment") || keyword.contains("work")) {
            return "Continue developing your professional skills and seek advancement in your field. Examining the placement of Rahu and Ketu in your birth chart can indicate potential challenges and opportunities within your chosen profession. Consulting a qualified astrologer can provide personalized guidance";
        } else if (keyword.contains("vocation") || keyword.contains("occupation")) {
            return "Discovering your true calling can lead to a fulfilling career. Analyzing your birth chart and understanding your dominant Dosha can provide guidance towards suitable vocations. Remember, self-discovery and exploration are crucial in finding your passion.";
        } else if (keyword.contains("career")) {
            return "Focus on your skills and explore new opportunities in your career. Analyzing the placements of planets like Mercury and Jupiter in your birth chart can offer insights into your professional strengths and potential career paths. Additionally, considering your dominant Dosha (body constitution) can help you choose suitable career options or suggest practices like meditation to enhance focus and productivity";
        } else if (keyword.contains("marriage") || keyword.contains("wedding") || keyword.contains("matrimony")) {
            return "Building a strong foundation is crucial for a successful marriage. Communication is key.";
        } else if (keyword.contains("spouse") || keyword.contains("partner") || keyword.contains("husband") || keyword.contains("wife")) {
            return "Nurturing a strong relationship requires love, patience, and open communication.";
        } else if (keyword.contains("children") || keyword.contains("kids") || keyword.contains("offspring") || keyword.contains("family")) {
            return "Children bring joy and responsibility. Ensure a supportive environment for their growth.";
        } else if (keyword.contains("love") || keyword.contains("affection") || keyword.contains("romance") || keyword.contains("relationship")) {
            return "Love is a beautiful journey. Communication, trust, and understanding are vital.";
        } else if (keyword.contains("money") || keyword.contains("finance") || keyword.contains("wealth") || keyword.contains("currency") || keyword.contains("funds") || keyword.contains("capital") || keyword.contains("savings")) {
            return "Financial stability is essential. Plan your expenses wisely and consider investments.";
        } else if (keyword.contains("health") || keyword.contains("wellness") || keyword.contains("fitness") || keyword.contains("well-being") || keyword.contains("physical condition")) {
            return "Maintain a healthy lifestyle. Regular exercise and a balanced diet contribute to well-being.";
        } else if (keyword.contains("future") || keyword.contains("future prospects") || keyword.contains("outlook") || keyword.contains("prediction") || keyword.contains("forecast") || keyword.contains("projection")) {
            return "The future is uncertain, but you can shape it with your actions today.";
        } else if (keyword.contains("girlfriend") || keyword.contains("significant other") || keyword.contains("companion") || keyword.contains("mate")) {
            return "A strong relationship is built on trust, communication, and mutual respect.";
        } else if (keyword.contains("hello") || keyword.contains("hi") || keyword.contains("hii") || keyword.contains("hey") || keyword.contains("hyy") || keyword.contains("hy") || keyword.contains("hyyy")) {
            return "Hello! How can I assist you today?";
        } else if (keyword.contains("morning")) {
            return "Good morning! I hope you have a wonderful day ahead.";
        } else if (keyword.contains("afternoon")) {
            return "Good afternoon! How may I help you?";
        } else if (keyword.contains("evening")) {
            return "Good evening! Is there anything I can assist you with?";
        } else if (keyword.contains("help")) {
            return "Sure, I'm here to help. What do you need assistance with?";
        }
        // Add more cases for other keywords and their variations...

        return "I'm sorry, I'm not sure how to respond to that. How can I assist you?";
    }

    private String getDefaultResponse() {
        // Provide a default response if no keyword is matched
        String[] responses = {
                "I'm sorry, I couldn't understand. Can you please ask in a different way?",
                "My apologies, I'm not sure how to respond to that. Could you provide more details?",
                "I'm here to assist you, but I'm having difficulty understanding. Can you rephrase your question?",
                "It seems I'm not familiar with that topic. Let's talk about something else.",
                "I appreciate your question, but I'm not equipped to answer that. Let's discuss another aspect.",
                "I'm here to provide guidance, but I'm uncertain about that. Feel free to ask another question."
        };

        return responses[new Random().nextInt(responses.length)];
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferenceManager.getInstance(this).setChatMessages(userId, chatAdapter.getChatList());
    }
}