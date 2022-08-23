package com.da.qlnhahang.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.da.qlnhahang.model.User;
import com.da.qlnhahang.utils.Constance;
import com.da.qlnhahang.App;
import com.da.qlnhahang.databinding.FragmentSettingBinding;
import com.da.qlnhahang.ui.LoginActivity;
import com.da.qlnhahang.ui.MainActivity;
import com.google.firebase.database.FirebaseDatabase;

public class SettingFragment extends Fragment {
    private FragmentSettingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLogout.setOnClickListener(v -> {
            User user = ((App) getContext().getApplicationContext()).user;
            user.setToken("");
            FirebaseDatabase.getInstance().getReference("users")
                    .child(user.getId())
                    .setValue(user);
            getContext().getSharedPreferences(Constance.PREFERENCES_NAME, Context.MODE_PRIVATE).edit().clear().commit();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        MainActivity act = (MainActivity) getActivity();
        binding.tvInfo.setOnClickListener(v -> {
            act.showFm(act.fmInfo);
        });

        binding.tvChangePassword.setOnClickListener(v -> {
            act.showFm(new ChangePasswordFragment());
        });
    }

}
