package com.carsecurity.carsecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Verification extends AppCompatActivity implements View.OnClickListener {
    EditText etPhoneRequest;
    Button bVerify;
    DatabaseReference reference;
    String PhoneNumber;
    private FirebaseUser user;
    private String mVerificationId;
    TextView skipText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        etPhoneRequest =  findViewById(R.id.etPhoneRequest);
        skipText=findViewById(R.id.skipText);
        skipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bVerify =  findViewById(R.id.bVerify);
        bVerify.setOnClickListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");


    }
    private boolean isValidPhone(String phone) {
        boolean valid = false;
        String PHONE_PATTERN = "^[+]?[0-9]{10,13}$";
        Pattern pattern = Pattern.compile(PHONE_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) valid = true;
        return valid;
    }
    public void RequestCode() {
        if (etPhoneRequest==null||etPhoneRequest.getText().toString().isEmpty() ){
            etPhoneRequest.setError("Enter valid phone number");
            return;
        }
        if (!isValidPhone(etPhoneRequest.getText().toString()))
            return;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                etPhoneRequest.getText().toString(), 60, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        user.updatePhoneNumber(credential);
                        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(), R.string.verificationFaild + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        mVerificationId = verificationId;
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String verificationId) {
                        super.onCodeAutoRetrievalTimeOut(verificationId);
                        Toast.makeText(Verification.this, R.string.codeTimeOut + verificationId, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


    @Override
    public void onClick(View view) {
        if (view == bVerify) {
            Toast.makeText(getApplicationContext(), R.string.sending, Toast.LENGTH_SHORT).show();
            RequestCode();

        }
        if (view==skipText){
            Intent intent =new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
