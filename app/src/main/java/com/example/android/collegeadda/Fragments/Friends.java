package com.example.android.collegeadda.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.collegeadda.FragmentPageAdapter;
import com.example.android.collegeadda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Friends extends Fragment {

    View rootview;
    FragmentActivity myContext;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList = new ArrayList<>();

    public Friends() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getFriends();
        rootview = inflater.inflate(R.layout.fragment_friends, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView)rootview.findViewById(R.id.friendsListView);
        arrayAdapter = new ArrayAdapter<>(myContext,R.layout.just_text);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String uid = arrayList.get(position);
                Toast.makeText(myContext, uid, Toast.LENGTH_SHORT).show();
//id of selected user


            }
        });

    }

    void getFriends(){

        String authId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String ref = "chats/"+authId+"/friends/";
//        Toast.makeText(myContext, ref, Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(ref);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                arrayAdapter.clear();
                arrayList.clear();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    String friend = d.child("name").getValue(String.class);
                    arrayAdapter.add(friend);
                    arrayList.add(d.getKey());
//                    Toast.makeText(myContext, "Data Received "+friend , Toast.LENGTH_SHORT).show();

                }
//                Toast.makeText(myContext, "Data Received", Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


}
