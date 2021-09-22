package com.smallacademy.userroles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText fullName,email,password,phone;
    Button registerBtn,goToLogin;
    boolean valid = true;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;


    CheckBox Admin , User;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);



        Admin = findViewById(R.id.isTeacher);
        User = findViewById(R.id.isStudent);



        User.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()){

                    Admin.setChecked(false);
                }

            }
        });


        Admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView1, boolean isChecked) {


                if (buttonView1.isChecked()){

                    User.setChecked(false);
                }

            }
        });




        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SignUp();

            }
        });


    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }


    private void SignUp(){


        checkField(fullName);
        checkField(email);
        checkField(password);
        checkField(phone);


        if (valid){

            mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                        FirebaseUser user = mAuth.getCurrentUser();

                        Toast.makeText(getApplicationContext() ,"SignUp Successfully", Toast.LENGTH_SHORT).show();
                        DocumentReference df = fStore.collection("Users").document(user.getUid());

                        Map<String,Object> userInfo = new HashMap<>();
                        userInfo.put("Full_name",fullName.getText().toString());
                        userInfo.put("Email",email.getText().toString());
                       userInfo.put("Password",password.getText().toString());
                        userInfo.put("Phone",phone.getText().toString());


                        if (Admin.isChecked()){

                            userInfo.put("isAdmin","1");

                        }
                        if (User.isChecked()){

                        userInfo.put("isUser","1");
                        }

                        df.set(userInfo);


                        if (Admin.isChecked()){

                            startActivity(new Intent(getApplicationContext(),AdminActivty.class));
                            finish();
                        }
                    if (User.isChecked()){

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }



                    }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "SignUp Error.!!", Toast.LENGTH_SHORT).show();
                }
            });
        }



        if (!(Admin.isChecked() || User.isChecked())){
            Toast.makeText(this, "Select the Account Type", Toast.LENGTH_SHORT).show();
            return;

        }
    }
}