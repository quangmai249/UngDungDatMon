package com.da.qlnhahang.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.da.qlnhahang.model.User;
import com.da.qlnhahang.App;
import com.da.qlnhahang.databinding.FragmentInfoBinding;
import com.google.firebase.database.FirebaseDatabase;

public class InfoFragment extends Fragment {
    private FragmentInfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = ((App) getContext().getApplicationContext()).user;
        binding.edtAddress.setText(user.getAddress());
        binding.edtName.setText(user.getName());
        binding.edtPhone.setText(user.getPhone());

        binding.btnSave.setOnClickListener(v -> {
            String address = binding.edtAddress.getText().toString();
            String name = binding.edtName.getText().toString();
            String phone = binding.edtPhone.getText().toString();
            if (address.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(getContext(), "Thông tin không được trống", Toast.LENGTH_SHORT).show();
                return;
            }
            user.setName(name);
            user.setPhone(phone);
            user.setAddress(address);
            FirebaseDatabase.getInstance().getReference("users").child(user.getId()).setValue(user)
                    .addOnCompleteListener(task -> {
                        getActivity().getSupportFragmentManager().popBackStack();
                    });
        });

        binding.imBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }
}
