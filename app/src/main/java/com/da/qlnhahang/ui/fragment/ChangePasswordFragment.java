package com.da.qlnhahang.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.da.qlnhahang.App;
import com.da.qlnhahang.databinding.FragmentChangePasswordBinding;
import com.da.qlnhahang.databinding.FragmentInfoBinding;
import com.da.qlnhahang.model.User;
import com.da.qlnhahang.utils.Constance;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = ((App) getContext().getApplicationContext()).user;
        binding.btnSave.setOnClickListener(v -> {
            String password = binding.edtPassword.getText().toString();
            String newPassword = binding.edtNewPassword.getText().toString();
            String confirmPassword = binding.edtConfirmPassword.getText().toString();
            if (password.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "Thông tin không được trống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(user.getPassword())) {
                Toast.makeText(getContext(), "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Mật khẩu mới không trùng", Toast.LENGTH_SHORT).show();
                return;
            }
            user.setPassword(newPassword);
            FirebaseDatabase.getInstance().getReference("users").child(user.getId()).setValue(user)
                    .addOnCompleteListener(task -> {
                        SharedPreferences.Editor editor = getContext().getSharedPreferences(Constance.PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                        editor.putString(Constance.PASSWORD, password);
                        editor.commit();
                        getActivity().getSupportFragmentManager().popBackStack();
                    });
        });

        binding.imBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }
}
