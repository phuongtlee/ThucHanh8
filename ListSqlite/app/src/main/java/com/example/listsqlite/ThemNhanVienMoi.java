package com.example.listsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.listsqlite.Database.NhanVienDatasource;
import com.example.listsqlite.Database.PhongBanDataSource;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ThemNhanVienMoi extends AppCompatActivity implements View.OnClickListener{

    private NhanVienDatasource nhanVienDatasource;
    Button btnSubmit;
    EditText txtid, txtname;

    Spinner spinner;

    private PhongBanDataSource phongBanDataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien_moi);

        nhanVienDatasource = new NhanVienDatasource(this);
        nhanVienDatasource.open();

        phongBanDataSource = new PhongBanDataSource(this);
        phongBanDataSource.open();

        txtid = (EditText) findViewById(R.id.txtempid);
        txtname = (EditText) findViewById(R.id.txtName);
        btnSubmit = (Button) findViewById(R.id.btnBack);
        spinner = (Spinner) findViewById(R.id.spinner_phong_ban_insert);

        btnSubmit.setOnClickListener(this);

        PhongBanDataSource phongBanDataSource = new PhongBanDataSource(this);

        phongBanDataSource.open();

        List<PhongBan> departmentList = phongBanDataSource.danhsachPhong();

        ArrayList<String> departmentNames = new ArrayList<>();
        for (PhongBan department : departmentList) {
            departmentNames.add(department.getTenPhong());
        }

        phongBanDataSource.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedDepartmentName = (String) parentView.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
    @Override
    public void onClick(View v) {
        Intent intent  = getIntent();
        Bundle bundle = new Bundle();

        int id = Integer.parseInt(txtid.getText().toString());
        String ten = txtname.getText().toString();
        String phong = spinner.getSelectedItem().toString();
        NhanVien nv = new NhanVien(id, ten, phong);

        long insertId = nhanVienDatasource.createPerson(nv);

        bundle.putSerializable("nv",nv);
        intent.putExtras(bundle);
        setResult(MainActivity.SAVE_NEW_EMPLOYEE,intent);
        finish();
    }
}