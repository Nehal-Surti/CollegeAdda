package com.example.android.collegeadda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent;
        if(isSigned()){
            intent  = new Intent(this,NavigationDrawer.class);
        }else {
            intent = new Intent(this,MainActivity.class);
        }



        startActivity(intent);
        finish();
    }

    boolean isSigned(){

        ////TODO this is annoying to fill details again and again
        // assume if user is logged in toh he has filled details once and bypass to navigation activity
        //https://firebase.google.com/docs/auth/android/manage-users
        // check this for it
        // actually u already know to check if user is signed in firebase auth so just apply that


        return false;
    }
}
