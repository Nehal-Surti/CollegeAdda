package com.example.android.collegeadda;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN=1,REQUEST_IMAGE_GET=2;
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
    LinearLayout navigationView;
    TextView nameView ;
    String url;
    Button create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        final EditText name = (EditText)findViewById(R.id.displayName);
        final EditText email = (EditText)findViewById(R.id.email);
        final EditText phone = (EditText)findViewById(R.id.contact);
        final EditText status = (EditText)findViewById(R.id.status);
        create = (Button)findViewById(R.id.create);
        final ImageButton imageButton = (ImageButton)findViewById(R.id.imageView);
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
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
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


    void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_IMAGE_GET);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        ImageButton img = (ImageButton) findViewById(R.id.imageView);

        Drawable icon;
        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                Toast.makeText(this,"You're Signed In", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this,"Signing Out", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK)
        {
            Uri fullphotouri = data.getData();
            uploadPic(fullphotouri);
            try{
                InputStream inputStream = this.getContentResolver().openInputStream(fullphotouri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap preview = BitmapFactory.decodeStream(inputStream,null,options);
                icon = new BitmapDrawable(getResources(),preview);
            }catch (FileNotFoundException e)
            {
                 icon = getResources().getDrawable(R.color.colorPrimary);
            }
            img.setBackground(icon);
          /*LinearLayout navheader = (LinearLayout)findViewById(R.id.navheader);
            ImageView view = (ImageView)navheader.findViewById(R.id.image);
            view.setImageDrawable(icon);*/


        }
    }

    public void uploadPic(Uri uri)
    {
        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String path = "users/"+"profilePic/"+ user;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference(path);
        UploadTask uploadTask = storageReference.putFile(uri);
        create.setEnabled(false);
        create.setText("Uploading Pic ...");

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(MainActivity.this,"Upload Success", Toast.LENGTH_SHORT).show();
            }
        });
        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(task.isSuccessful()){
                    return storageReference.getDownloadUrl();
                }else{
                    throw task.getException();
                }

            }
        });
        task.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                url = task.getResult().toString();
                create.setEnabled(true);
                create.setText("Create");
            }
        });
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

   public void createUser(String Name, String Email, String Phone, String Status)
    {
        user=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String path = "users/"+user+"/";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

//        String way = "users/"+"profilePic/"+ user;
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.getReference(way);
//        Task picture = storageReference.getDownloadUrl();


        HashMap<String,Object> values = new HashMap<>();
        values.put("name",Name);
        values.put("email",Email);
        values.put("phoneNo",Phone);
        values.put("status",Status);

        if(url!=null){
            values.put("profilePic",url);
        }

        ref.setValue(values).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}



