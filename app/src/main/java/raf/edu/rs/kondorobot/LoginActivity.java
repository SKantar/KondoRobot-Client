package raf.edu.rs.kondorobot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static boolean correct = false;


    @Bind(R.id.input_host) EditText _hostField;
    @Bind(R.id.input_port) EditText _portField;
    @Bind(R.id.btn_connect) Button _connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        _connectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private boolean try_connect(){
        try{
            String host = _hostField.getText().toString().trim();
            int port = Integer.valueOf(_portField.getText().toString().trim());

            Socket socket = new Socket(host, port);

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String command = "hello\n";
            outputStream.writeBytes(command);
            String response = inputStream.readLine();
            socket.close();
            if(response.trim().equals(command.trim()))
                return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(
            LoginActivity.this,
            R.style.AppTheme_Dark_Dialog
        );

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                correct = try_connect();
            }
        }).start();

        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    // On complete call either onLoginSuccess or onLoginFailed
                    if(correct)
                        onLoginSuccess();
                    else
                        onLoginFailed();
                    progressDialog.dismiss();
                }
            }, 3000);

    }

    public void onLoginSuccess() {
        _connectButton.setEnabled(true);


        String host = _hostField.getText().toString().trim();
        int port = Integer.valueOf(_portField.getText().toString().trim());

        Intent params = new Intent();
        params.putExtra("host", host);
        params.putExtra("port", port);

        setResult(RESULT_OK, params);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Connection failed", Toast.LENGTH_LONG).show();
        _connectButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String host = _hostField.getText().toString().trim();
        String port = _portField.getText().toString().trim();

        if (host.isEmpty() || !Patterns.IP_ADDRESS.matcher(host).matches()) {
            _hostField.setError("enter a valid email address");
            valid = false;
        } else {
            _hostField.setError(null);
        }

        if (port.isEmpty() || port.length() != 4) {
            _portField.setError("exactly 4 numbers");
            valid = false;
        } else {
            _portField.setError(null);
        }
        return valid;
    }

}
