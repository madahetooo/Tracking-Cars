package com.carsecurity.carsecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    EditText etName, etEmailAddress, etPhoneNumber, etPasswordRegister, etPasswordRegisterConfirm;
    Button bRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference Xreference;
    private FirebaseUser Xuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        etName =  findViewById(R.id.etName);
        etEmailAddress =  findViewById(R.id.etEmailAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPasswordRegister =  findViewById(R.id.etPasswordRegister);
        etPasswordRegisterConfirm = findViewById(R.id.etPasswordRegisterConfirm);
        bRegister =  findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        Xreference = FirebaseDatabase.getInstance().getReference().child("User");


    }

    @Override
    public void onClick(View v) {

        final String str_EmailAddress = etEmailAddress.getText().toString().trim();
        final String str_PhoneNumber = etPhoneNumber.getText().toString().trim();
        final String str_PasswordRegister = etPasswordRegister.getText().toString().trim();
        String str_PasswordRegisterConfirm = etPasswordRegisterConfirm.getText().toString().trim();
        if (isValid(str_EmailAddress, str_PasswordRegister, str_PasswordRegisterConfirm, str_PhoneNumber)) {
            mAuth.createUserWithEmailAndPassword(str_EmailAddress, str_PasswordRegister).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Xuser = task.getResult().getUser();
                        UserProfileChangeRequest ProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(etName.getText().toString())
                                .build();
                        Xuser.updateProfile(ProfileChangeRequest);
                        DatabaseReference NewUser = Xreference.child(Xuser.getUid());
                        NewUser.child("UserId").setValue(Xuser.getUid());
                        NewUser.child("Name").setValue(etName.getText().toString());
                        NewUser.child("EmailAddress").setValue(str_EmailAddress);
                        NewUser.child("PhoneNumber").setValue(str_PhoneNumber);
                        NewUser.child("Password").setValue(str_PasswordRegister);
                        NewUser.child("State").setValue("1");
                        NewUser.child("imgs").setValue("");
                        Toast.makeText(Registration.this, R.string.registrationSuccess, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), Verification.class);
                        startActivity(i);
                        finish();

                    } else {
                        if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                            Toast.makeText(Registration.this, R.string.theEmailIsAlreadyTaken, Toast.LENGTH_SHORT).show();

                        }
                        Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }

    private boolean isValidPhone(String phone) {
        boolean valid = false;
        String PHONE_PATTERN = "^[+]?[0-9]{10,13}$";
        Pattern pattern = Pattern.compile(PHONE_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) valid = true;
        return valid;
    }

    private boolean isValidPassword(String pass) {// validation for password
        boolean valid = false;
        String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pass);
        if (matcher.matches()) valid = true;
        return valid;
    }

    private boolean isValidEmail(String email) {// validation for email
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,255}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public boolean isValid(String str_email, String pass, String pass_conf, String phone) {
        boolean valid = true;
        if (!isValidEmail(str_email)) {
            etEmailAddress.setError("Enter valid Email");
            valid = false;
        }
        if (!pass.equals(pass_conf)) {
            etPasswordRegisterConfirm.setError("Password not matched");
            valid = false;
        }
        if (!isValidPassword(pass)) {
            etPasswordRegister.setError("Use Numbers Upper and Lower case");
            valid = false;
        }
        if (!isValidPhone(phone)) {
            etPhoneNumber.setError("Enter valid phone number");
            valid = false;
        }

        return valid;
    }
}
