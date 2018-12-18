package com.example.android.collegeadda;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN=1;
    Intent intent;
    String user;
    String displayName;
    String dEmail;
    String number;
    String Stat;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        final EditText name = (EditText)findViewById(R.id.displayName);
        final EditText email = (EditText)findViewById(R.id.email);
        final EditText phone = (EditText)findViewById(R.id.contact);
        final EditText status = (EditText)findViewById(R.id.status);
        final Button create = (Button)findViewById(R.id.create);
        intent = new Intent(MainActivity.this,NavigationDrawer.class);
        mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user!=null){
                            Toast.makeText(MainActivity.this,"You're now signed in", Toast.LENGTH_SHORT).show();
                        }else {
                            startActivityForResult(
                                    AuthUI.getInstance()
                                            .createSignInIntentBuilder()
                                            .setAvailableProviders(providers)
                                            .build(),
                                    RC_SIGN_IN);
                        }
                    }
                };
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayName = name.getText().toString();
                if(displayName.length()==0)
                {
                    name.setError("Enter Name");
                }else if(email.getText().toString().length()==0)
                {
                    email.setError("Enter Email Address");
                }else if(phone.getText().toString().length()<10 || phone.getText().toString().length()>10)
                {
                    phone.setError("Enter Contact Number");
                }else
                {
                   create.setEnabled(false);
                   number = phone.getText().toString();
                   dEmail=email.getText().toString();
                   Stat = status.getText().toString();
                   createUser(displayName,dEmail,number,Stat);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                Toast.makeText(this,"You're Signed In", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this,"Signing Out", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    void createUser(String Name, String Email, String Phone, String Status)
    {
        user=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String path = "users/"+user+"/";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

        HashMap<String,Object> values = new HashMap<>();
        values.put("name",Name);
        values.put("email",Email);
        values.put("phoneNo",Phone);
        values.put("status",Status);

        ref.setValue(values).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(intent);
                finish();
            }
        });
    }
}



