package com.example.socialmediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {
    TextView email, pass;
    Button loginbtn;
    TextView activity_changer;

    private void init(){
        email=findViewById(R.id.etemail);
        pass=findViewById(R.id.etpass);
        loginbtn=findViewById(R.id.loginbtn);
        activity_changer=findViewById(R.id.tvsignup);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            Intent i=new Intent(MainActivity.this, MainConfigurator.class);
            startActivity(i);
            finish();

            Toast.makeText(this, "Already logged in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        activity_changer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Signup.class);
                startActivity(i);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uemail=email.getText().toString().trim();
                String upass=pass.getText().toString().trim();
                if(uemail.isEmpty() || upass.isEmpty()){
                    Toast.makeText(MainActivity.this, "Kindly enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(uemail, upass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent i=new Intent(MainActivity.this, MainConfigurator.class);
                                    startActivity(i);
                                    finish();
                                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Error logging in", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


    }
}