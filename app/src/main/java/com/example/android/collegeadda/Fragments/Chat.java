package com.example.android.collegeadda.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.collegeadda.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment {
    View rootView;

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

    }
}
