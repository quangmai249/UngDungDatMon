package com.da.qlnhahang.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.da.qlnhahang.App;
import com.da.qlnhahang.R;
import com.da.qlnhahang.databinding.ActivityLoginBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.btnLogin.setOnClickListener(view -> {
            String username = binding.edtUsername.getText().toString();
            String password = binding.edtPassword.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Thông tin đăng nhập trống", Toast.LENGTH_SHORT).show();
                return;
            }
            ((App) getApplicationContext()).login(username, password, this);
        });
    }
}
