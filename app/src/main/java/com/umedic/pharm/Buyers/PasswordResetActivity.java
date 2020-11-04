package com.umedic.pharm.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umedic.pharm.Prevalent.Prevalent;
import com.umedic.pharm.R;

import java.util.HashMap;

public class PasswordResetActivity extends AppCompatActivity
{
    private String check = "";
    private TextView pageTitle, titleQuestions;

    private EditText phoneNo, firstQuestion, secondQuestion;
    private Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.page_title_reset_password);
        titleQuestions = findViewById(R.id.sec_questions_title);
        phoneNo = findViewById(R.id.find_phone_number);
        firstQuestion = findViewById(R.id.Question_one);
        secondQuestion = findViewById(R.id.Question_two);
        verifyBtn = findViewById(R.id.verify_btn);

    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNo.setVisibility(View.GONE);

        if (check.equals("settings"))
        {
            pageTitle.setText("set questions");
            titleQuestions.setText("Answer the security questions");
            verifyBtn.setText("set");

            displaySecurityAnswers();

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setSecurityQuestion();
                }
            });
        }
        else if (check.equals("login"))
        {
            phoneNo.setVisibility(View.VISIBLE);

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) 
                {
                    userVerification();
                }
            });
        }
    }

    private void setSecurityQuestion()
    {
        String answerOne = firstQuestion.getText().toString().toLowerCase();
        String answerTwo = secondQuestion.getText().toString().toLowerCase();
        if (firstQuestion.equals("") && secondQuestion.equals(""))
        {
            Toast.makeText(PasswordResetActivity.this, "please provide answers for both questions", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> securityQuestionMap = new HashMap<>();
            securityQuestionMap.put("answerOne", answerOne);
            securityQuestionMap.put("answerTwo", answerTwo);

            ref.child("Security Questions").updateChildren(securityQuestionMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(PasswordResetActivity.this, "Your security answers have been set", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(PasswordResetActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

        }
    }

    private void displaySecurityAnswers()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ansOne = snapshot.child("answerOne").getValue().toString();
                    String ansTwo = snapshot.child("answerTwo").getValue().toString();

                    firstQuestion.setText(ansOne);
                    secondQuestion.setText(ansTwo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userVerification()
    {
        final String phone = phoneNo.getText().toString();
        final String answerOne = firstQuestion.getText().toString().toLowerCase();
        final String answerTwo = secondQuestion.getText().toString().toLowerCase();

        if (!phone.equals("") && !answerOne.equals("") && !answerTwo.equals(""))
        {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if (snapshot.exists())
                    {
                        String mPhone = snapshot.child("phone").getValue().toString();

                        if (snapshot.hasChild("Security Questions"))
                        {
                            String ansOne = snapshot.child("Security Questions").child("answerOne").getValue().toString();
                            String ansTwo = snapshot.child("Security Questions").child("answerTwo").getValue().toString();

                            if (!ansOne.equals(answerOne))
                            {
                                Toast.makeText(PasswordResetActivity.this, "First answer is incorrect", Toast.LENGTH_SHORT).show();
                            }
                            else if (!ansTwo.equals(answerTwo))
                            {
                                Toast.makeText(PasswordResetActivity.this, "Second answer is incorrect", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordResetActivity.this);
                                builder.setTitle("new password");

                                final  EditText newPassword = new EditText(PasswordResetActivity.this);
                                newPassword.setHint("Enter new password");
                                builder.setView(newPassword);

                                builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (!newPassword.getText().toString().equals(""))
                                        {
                                            ref.child("password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(PasswordResetActivity.this, "Your password has been changed", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(PasswordResetActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                            }
                        }
                        else
                        {
                            Toast.makeText(PasswordResetActivity.this, "phone number does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(PasswordResetActivity.this, "Phone number does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            Toast.makeText(this, "Answer the questions", Toast.LENGTH_SHORT).show();
        }

        
    }
}