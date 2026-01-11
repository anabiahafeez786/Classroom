package com.example.classroom;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class UserChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText etMessage;
    private ImageButton btnSend;

    private DatabaseReference chatRef;
    private ArrayList<Message> messageList;
    private ChatAdapter adapter;

    private String senderId;
    private String receiverId = "user2";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_chat, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);

        SharedPreferences prefs =
                requireActivity().getSharedPreferences("loginPrefs", getContext().MODE_PRIVATE);
        senderId = prefs.getString("userId", "user1");

        String chatId = senderId.compareTo(receiverId) < 0
                ? senderId + "_" + receiverId
                : receiverId + "_" + senderId;

        chatRef = FirebaseDatabase.getInstance()
                .getReference("chats")
                .child(chatId)
                .child("messages");

        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setStackFromEnd(true);

        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                chatRef.push().setValue(
                        new Message(senderId, receiverId, text, System.currentTimeMillis())
                );
                etMessage.setText("");
            }
        });

        listenMessages();
        return view;
    }

    private void listenMessages() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Message msg = snap.getValue(Message.class);
                    if (msg != null) messageList.add(msg);
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
