package com.example.socialmediaapp;

import android.content.Intent;
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

public class Signup extends AppCompatActivity {
    EditText name, email, pass;
    TextView actchanger;
    Button signupbtn;

    private void init(){
        name=findViewById(R.id.etsignupname);
        email=findViewById(R.id.etsignupemail);
        pass=findViewById(R.id.etsignuppass);
        actchanger=findViewById(R.id.tvlogin);
        signupbtn=findViewById(R.id.signupbtn);
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

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
                                    Toast.makeText(Signup.this, "Account Created", Toast.LENGTH_SHORT).show();
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