package dev.moutamid.elitetimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    private Utils utils = new Utils();
    private static final String TAG = "RegistrationActivity";
    private Context context = RegistrationActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_registration);
        EditText password = (EditText) findViewById(R.id.passwordEdittext);
        EditText email = (EditText) findViewById(R.id.emailEdittext);
        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty())
                    Toast.makeText(RegistrationActivity.this, "Please enter email!", Toast.LENGTH_SHORT).show();
                else if (password.getText().toString().isEmpty())
                    Toast.makeText(RegistrationActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(RegistrationActivity.this, TimerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    utils.storeBoolean(context, Constants.IS_LOGGED_IN, true);
                    finish();
                    startActivity(intent);

                }
            }
        });

        findViewById(R.id.signUpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty())
                    Toast.makeText(RegistrationActivity.this, "Please enter email!", Toast.LENGTH_SHORT).show();
                else if (password.getText().toString().isEmpty())
                    Toast.makeText(RegistrationActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(RegistrationActivity.this, TimerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    utils.storeBoolean(context, Constants.IS_LOGGED_IN, true);
                    finish();
                    startActivity(intent);
                }
            }
        });

    }
}