package com.example.ymittal.backendlesslogin.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.ymittal.backendlesslogin.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class LoginActivity extends AppCompatActivity {

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private TextView tvRegister;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private CheckBox checkboxRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initViews();

        if (Backendless.UserService.CurrentUser() != null)
            Toast.makeText(LoginActivity.this, Backendless.UserService.CurrentUser().getUserId(),
                    Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        checkboxRemember = (CheckBox) findViewById(R.id.checkboxRemember);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClicked();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginSuccess() {
        if(isGooglePlayServicesOK()){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
    }

    private void onLoginButtonClicked() {
        Toast.makeText(LoginActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
        Backendless.UserService.login(etEmail.getText().toString(),
                etPassword.getText().toString(),
                new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        Toast.makeText(LoginActivity.this, R.string.email_login_success,
                                Toast.LENGTH_SHORT).show();
                        loginSuccess();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(LoginActivity.this, getString(R.string.login_fail) + fault.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }, checkboxRemember.isChecked());
    }

    public boolean isGooglePlayServicesOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LoginActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(
                    LoginActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}