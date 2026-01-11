package com.example.classroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    EditText etMessage;
    ImageButton btnSend;
    TextView tvResponse;

    OkHttpClient client = new OkHttpClient();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ai_tutor, container, false);

        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);
        tvResponse = view.findViewById(R.id.tvResponse);

        btnSend.setOnClickListener(v -> {
            String msg = etMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                sendMessage(msg);
            }
        });

        return view;
    }

    private void sendMessage(String userMessage) {

        tvResponse.setText("Thinking...");

        try {
            // ===== Updated JSON with role =====
            JSONObject textPart = new JSONObject();
            textPart.put("text", userMessage);

            JSONArray partsArray = new JSONArray();
            partsArray.put(textPart);

            JSONObject contentObj = new JSONObject();
            contentObj.put("role", "user"); // 🔥 Role is mandatory now
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
                public void onFailure(Call call, IOException e) {
                    requireActivity().runOnUiThread(() ->
                            tvResponse.setText("Error: " + e.getMessage())
                    );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseBody = response.body().string();

                    if (!response.isSuccessful()) {
                        requireActivity().runOnUiThread(() ->
                                tvResponse.setText("API Error:\n" + responseBody)
                        );
                        return;
                    }

                    try {
                        JSONObject json = new JSONObject(responseBody);
                        JSONArray candidates = json.getJSONArray("candidates");
                        JSONObject content =
                                candidates.getJSONObject(0).getJSONObject("content");
                        JSONArray parts = content.getJSONArray("parts");
                        String reply = parts.getJSONObject(0).getString("text");

                        requireActivity().runOnUiThread(() ->
                                tvResponse.setText(reply)
                        );

                    } catch (Exception e) {
                        requireActivity().runOnUiThread(() ->
                                tvResponse.setText("Parse Error:\n" + e.getMessage())
                        );
                    }
                }
            });

        } catch (Exception e) {
            tvResponse.setText("Request Error:\n" + e.getMessage());
        }
    }
}