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

import java.util.ArrayList;
import java.util.List;

public class CapNhatNhanVien extends AppCompatActivity implements View.OnClickListener {

    private NhanVienDatasource nhanVienDatasource;
    private PhongBanDataSource phongBanDataSource;

    Button btnSubmit;
    EditText txtid, txtname;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_nhan_vien);

        nhanVienDatasource = new NhanVienDatasource(this);
        nhanVienDatasource.open();

        txtid = (EditText) findViewById(R.id.txtempid);
        txtname = (EditText) findViewById(R.id.txtName);
        btnSubmit = (Button) findViewById(R.id.btnBack);
        spinner = findViewById(R.id.spinner_phong_ban_update);



        btnSubmit.setOnClickListener(this);

        int id = getIntent().getIntExtra("result_id", 0);
        String name = getIntent().getStringExtra("result_name");
        String room = getIntent().getStringExtra("result_room");

        phongBanDataSource = new PhongBanDataSource(this);
        phongBanDataSource.open();

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

        // Hiển thị kết quả trên TextView
        txtid.setText(String.valueOf(id));
        txtname.setText(name);
        spinner.setSelection(adapter.getPosition(room));
    }

    @Override
    public void onClick(View v) {

        Intent intent  = getIntent();
        Bundle bundle = new Bundle();

        int id = Integer.parseInt(txtid.getText().toString());
        String ten = txtname.getText().toString();
        String phong = spinner.getSelectedItem().toString();
        NhanVien nv = new NhanVien(id, ten, phong);

        int update = nhanVienDatasource.updatePerson(nv);

        if (update > 0) {
            bundle.putSerializable("nv",nv);
            intent.putExtras(bundle);
            setResult(MainActivity.SAVE_NEW_EMPLOYEE,intent);
        } else {
            Toast.makeText(this, "Không tìm thấy dữ liệu!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}