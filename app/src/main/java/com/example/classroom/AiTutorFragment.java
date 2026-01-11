package com.example.classroom;

 import android.os.Bundle;
 import android.os.Handler;
 import android.os.Looper;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.EditText;
 import android.widget.ImageButton;
 import android.widget.ScrollView;
 import android.widget.TextView;

 import androidx.annotation.NonNull;
 import androidx.annotation.Nullable;
 import androidx.fragment.app.Fragment;

 import org.json.JSONArray;
 import org.json.JSONObject;

 import java.io.IOException;

 import okhttp3.Call;
 import okhttp3.Callback;
 import okhttp3.MediaType;
 import okhttp3.OkHttpClient;
 import okhttp3.Request;
 import okhttp3.RequestBody;
 import okhttp3.Response;

 public class AiTutorFragment extends Fragment {

     private static final String API_KEY = "AIzaSyB86_6mPGPn6RDbzrfMznhQjGgA1g2JZsU";
     private static final String API_URL =
             "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=";

     private EditText etMessage;
     private ImageButton btnSend;
     private TextView tvResponse;
     private ScrollView scrollView;

     private final OkHttpClient client = new OkHttpClient();
     private final StringBuilder chatHistory = new StringBuilder();

     @Nullable
     @Override
     public View onCreateView(
             @NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState
     ) {
         View view = inflater.inflate(R.layout.fragment_ai_tutor, container, false);

         // ✅ BIND VIEWS
         scrollView = view.findViewById(R.id.scrollView);
         etMessage = view.findViewById(R.id.etMessage);
         btnSend = view.findViewById(R.id.btnSend);
         tvResponse = view.findViewById(R.id.tvResponse);

         btnSend.setOnClickListener(v -> {
             String msg = etMessage.getText().toString().trim();
             if (msg.isEmpty()) return;

             chatHistory.append("You: ").append(msg).append("\n\n");
             tvResponse.setText(chatHistory.toString());
             scrollDown();

             etMessage.setText("");
             btnSend.setEnabled(false);

             sendRequest(msg);
         });

         return view;
     }

     private void sendRequest(String userMessage) {
         try {
             JSONObject textPart = new JSONObject();
             textPart.put("text", userMessage);

             JSONArray partsArray = new JSONArray();
             partsArray.put(textPart);

             JSONObject contentObj = new JSONObject();
             contentObj.put("role", "user");
             contentObj.put("parts", partsArray);

             JSONArray contentsArray = new JSONArray();
             contentsArray.put(contentObj);

             JSONObject requestBodyJson = new JSONObject();
             requestBodyJson.put("contents", contentsArray);

             RequestBody body = RequestBody.create(
                     requestBodyJson.toString(),
                     MediaType.parse("application/json")
             );

             Request request = new Request.Builder()
                     .url(API_URL + API_KEY)
                     .post(body)
                     .build();

             client.newCall(request).enqueue(new Callback() {

                 @Override
                 public void onFailure(@NonNull Call call, @NonNull IOException e) {
                     if (!isAdded()) return;
                     requireActivity().runOnUiThread(() -> {
                         chatHistory.append("AI: Network error\n\n");
                         tvResponse.setText(chatHistory.toString());
                         scrollDown();
                         btnSend.setEnabled(true);
                     });
                 }

                 @Override
                 public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                     if (!isAdded()) return;

                     String body = response.body().string();

                     if (!response.isSuccessful()) {
                         requireActivity().runOnUiThread(() -> {
                             chatHistory.append("AI: API error\n\n");
                             tvResponse.setText(chatHistory.toString());
                             scrollDown();
                             btnSend.setEnabled(true);
                         });
                         return;
                     }

                     try {
                         JSONObject json = new JSONObject(body);
                         JSONArray candidates = json.getJSONArray("candidates");
                         JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
                         JSONArray parts = content.getJSONArray("parts");
                         String reply = parts.getJSONObject(0).getString("text");

                         requireActivity().runOnUiThread(() -> {
                             chatHistory.append("AI: ").append(reply).append("\n\n");
                             tvResponse.setText(chatHistory.toString());
                             scrollDown();
                             btnSend.setEnabled(true);
                         });

                     } catch (Exception e) {
                         requireActivity().runOnUiThread(() -> {
                             chatHistory.append("AI: Parse error\n\n");
                             tvResponse.setText(chatHistory.toString());
                             scrollDown();
                             btnSend.setEnabled(true);
                         });
                     }
                 }
             });

         } catch (Exception e) {
             chatHistory.append("AI: Request error\n\n");
             tvResponse.setText(chatHistory.toString());
             scrollDown();
             btnSend.setEnabled(true);
         }
     }

     private void scrollDown() {
         scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
     }
 }
