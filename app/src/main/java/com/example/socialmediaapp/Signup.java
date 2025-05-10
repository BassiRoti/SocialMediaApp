package com.example.socialmediaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {
    EditText name, email, pass;
    TextView actchanger;
    Button signupbtn;
    SharedPreferences sp;

    private void init(){
        name=findViewById(R.id.etsignupname);
        email=findViewById(R.id.etsignupemail);
        pass=findViewById(R.id.etsignuppass);
        actchanger=findViewById(R.id.tvlogin);
        signupbtn=findViewById(R.id.signupbtn);
        sp=getSharedPreferences("authentication_data",MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();


        actchanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Signup.this, MainActivity.class);
                startActivity(i);
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uemail=email.getText().toString().trim();
                String upass=pass.getText().toString().trim();
                if(uemail.isEmpty() || upass.isEmpty()){
                    Toast.makeText(Signup.this, "Kindly enter all fields", Toast.LENGTH_SHORT).show();
                }

                FirebaseAuth auth= FirebaseAuth.getInstance();

                auth.createUserWithEmailAndPassword(uemail, upass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user=task.getResult().getUser();
                                   String id=user.getUid();
                                   DatabaseReference ref=FirebaseDatabase.getInstance().getReference("users").child(id);
                                    HashMap<String,Object> obj=new HashMap<>();
                                    obj.put("userID",id);
                                    obj.put("Username",name.getText().toString().trim());
                                    obj.put("Image","");
                                    obj.put("Bio","");

                                    ref.setValue(obj);

                                    SharedPreferences.Editor editor=sp.edit();
                                    editor.putString("user_name",name.getText().toString().trim());
                                    editor.putString("user_bio","");
                                    editor.putString("user_id",id);
                                    editor.apply();

                                    Toast.makeText(Signup.this, "Account Created", Toast.LENGTH_SHORT).show();
                                    name.setText("");
                                    email.setText("");
                                    pass.setText("");
                                }
                                else{
                                    Toast.makeText(Signup.this, "An Error occured", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}