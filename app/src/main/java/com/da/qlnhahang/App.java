package com.da.qlnhahang;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.da.qlnhahang.model.Table;
import com.da.qlnhahang.model.User;
import com.da.qlnhahang.ui.LoginActivity;
import com.da.qlnhahang.ui.MainActivity;
import com.da.qlnhahang.utils.Constance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class App extends Application implements ValueEventListener {
    public User user;
    public MutableLiveData<ArrayList<Table>> tables = new MutableLiveData<>();
    public static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        loadTable();
    }

    private void loadTable() {
        FirebaseDatabase.getInstance().getReference("table").addValueEventListener(this);
    }

    public void login(String username, String password, Activity act) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.get().addOnCompleteListener(task -> {
            for (DataSnapshot sn: task.getResult().getChildren()) {
                User user = sn.getValue(User.class);
                user.setId(sn.getKey());
                if (username.equals(user.getUsername()) && password.equals(user.getPassword()) && user.getRole().equals("2")) {
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task1 -> {
                        user.setToken(task1.getResult());
                        App.this.user = user;
                        Intent intent = new Intent(act, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        act.finish();
                        SharedPreferences.Editor editor = getSharedPreferences(Constance.PREFERENCES_NAME, MODE_PRIVATE).edit();
                        editor.putString(Constance.USERNAME, username);
                        editor.putString(Constance.PASSWORD, password);
                        editor.commit();
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(user.getId())
                                .setValue(user);
                    });
                    return;
                }
            }
            if (act instanceof LoginActivity) {
                Toast.makeText(act, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(act, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                act.finish();
            }
        });
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        ArrayList<Table> data = new ArrayList<>();
        for (DataSnapshot sn: snapshot.getChildren()) {
            Table table = sn.getValue(Table.class);
            table.setId(sn.getKey());
            data.add(table);
        }
        tables.postValue(data);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
