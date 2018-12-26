package com.example.android.collegeadda.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.android.collegeadda.ChatMessage;
import com.example.android.collegeadda.MessageAdapter;
import com.example.android.collegeadda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment {
    View rootView;
    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private EditText mMessageEditText;
    private Button mSendButton;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;

    ////TODO create text box and send button which saves     msg,firebase auth id,timestamp,anything else u find necessary

    public Chat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // idhr maat karna find view by id
        //bahaut tuts mein karenge idhr hi find view by id but its not good for performance and will make app laggy
        // do it in on view created
        rootView =  inflater.inflate(R.layout.fragment_chat, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //idhr kar find view by id
        // and in fragments dont use find view by id directly... call it on rootview
        // Button b = (Button)rootView.findViewById(id daal normal);
        // sirf fragments mein yeh karna not in activity
        mMessageEditText = (EditText) rootView.findViewById(R.id.messageEditText);
        mMessageListView = (ListView) rootView.findViewById(R.id.messageListView);
        mSendButton = (Button) rootView.findViewById(R.id.sendButton);

        List<ChatMessage> chatMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(getContext(), R.layout.item_messages, chatMessages);
        mMessageListView.setAdapter(mMessageAdapter);
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String send = "OpKQc8Stpyd79jDDU5vlQ0s2TWD2";
        String path = "chats/" + user;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference(path).child("chatMsgs/").child(send);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1000)});

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatMessage chatMessage = new ChatMessage(mMessageEditText.getText().toString(), "ABC",true);
                mMessagesDatabaseReference.push().setValue(chatMessage);
                mMessageEditText.setText("");
            }
        });
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                  ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                  mMessageAdapter.add(chatMessage);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
    }
}
