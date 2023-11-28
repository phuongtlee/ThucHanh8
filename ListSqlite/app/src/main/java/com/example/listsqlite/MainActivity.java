package com.example.listsqlite;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.listsqlite.Database.NhanVienDatasource;
import com.example.listsqlite.Database.PhongBanDataSource;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    public static final int NEW_EMPLOYEE = 55;
    public static final int EDIT_EMPLOYEE = 56;
    public static final int SAVE_NEW_EMPLOYEE = 57;
    public static final int SAVE_EDIT_EMPLOYEE = 58;
    private NhanVienDatasource nhanVienDatasource;

    ArrayList<NhanVien> arrayList = new ArrayList<NhanVien>();
    ListView listView;

    int vitrichon = -1;
    ArrayAdapter<NhanVien> adapter;

    TextView tvAdd;

    ImageButton imageButton;
    AutoCompleteTextView autoCompleteTextView;

    Spinner spinner;

    private PhongBanDataSource phongBanDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<NhanVien>(this,android.R.layout.simple_list_item_1, arrayList);
        ArrayList<String> listName = new ArrayList<>();
        for (NhanVien i : arrayList){
            String name = i.getTennv();
            listName.add(name);
        }

        listView = (ListView) findViewById(R.id.lvNhanVien);
        tvAdd = findViewById(R.id.tv_add);
        imageButton = findViewById(R.id.imageButton);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        spinner = findViewById(R.id.spinner_phong_ban_main);

        autoCompleteTextView.addTextChangedListener( this);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listName));

        phongBanDataSource = new PhongBanDataSource(this);
        phongBanDataSource.open();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyWord = autoCompleteTextView.getText().toString().trim();
                doSearch(keyWord);
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNewEmployee();
            }
        });

        nhanVienDatasource = new NhanVienDatasource(this);
        nhanVienDatasource.open();

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               vitrichon = position;
               openContextMenu(listView);
               return true;
           }
        });

        registerForContextMenu(listView);

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
                if ("Moi chon phong ban".equals(selectedDepartmentName)){
                    loadEmployeeData();
                } else {
                    doSearchWithSpinner(selectedDepartmentName);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
//        loadEmployeeData();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.mnulistviewcontext, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnuNew){
            doNewEmployee();
        } else if (id == R.id.mnuUpdate){
            doEditEmployee();
        } else if (id == R.id.mnuDel){
            doDeleteEmployee();
        }
        return super.onContextItemSelected(item);
    }

    private void doDeleteEmployee() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm delete data")
                .setMessage("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (vitrichon != -1 && vitrichon < arrayList.size()) {
                            NhanVien selectedEmployee = arrayList.get(vitrichon);
                            int id = selectedEmployee.getManv();
                            String name = selectedEmployee.getTennv();
                            String room = selectedEmployee.getPhongban();

                            NhanVien nv = new NhanVien(id, name, room);

                            int deleteRow = nhanVienDatasource.deletePerson(nv);
                            loadEmployeeData();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void doEditEmployee(){
        if (vitrichon != -1 && vitrichon < arrayList.size()) {
            NhanVien selectedEmployee = arrayList.get(vitrichon);
            int id = selectedEmployee.getManv();
            String name = selectedEmployee.getTennv();
            String room = spinner.getSelectedItem().toString();

            Intent intent = new Intent(this, CapNhatNhanVien.class);
            intent.putExtra("result_id", id);
            intent.putExtra("result_name", name);
            intent.putExtra("result_room", room);
            startActivityForResult(intent, MainActivity.EDIT_EMPLOYEE);
        }
    }

    private void doNewEmployee() {
        Intent intent = new Intent(this, ThemNhanVienMoi.class);
        startActivityForResult(intent,MainActivity.NEW_EMPLOYEE);
    }

    private void doSearch(String keyWord){
        ArrayList<NhanVien> searchNhanVien = (ArrayList<NhanVien>) nhanVienDatasource.searchPeople(keyWord);

        arrayList.clear();
        arrayList.addAll(searchNhanVien);

        adapter.notifyDataSetChanged();

    }

    private void doSearchWithSpinner(String selectedDepartmentName){
        ArrayList<NhanVien> searchNhanVienWithDepartmentRoom = (ArrayList<NhanVien>) nhanVienDatasource.searchPeopleWithDepartmentRoom(selectedDepartmentName   );

        arrayList.clear();
        arrayList.addAll(searchNhanVienWithDepartmentRoom);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MainActivity.NEW_EMPLOYEE:
                if (resultCode == MainActivity.SAVE_NEW_EMPLOYEE)
                {
                    Bundle b = data.getExtras();
                    NhanVien p = (NhanVien) b.getSerializable("nv");
                    arrayList.add(p);
                    adapter.notifyDataSetChanged();
                }
                break;
            case MainActivity.EDIT_EMPLOYEE:
                if (resultCode == MainActivity.SAVE_EDIT_EMPLOYEE) {
                    Bundle b = data.getExtras();
                    int editedId = b.getInt("result_id");
                    String editedName = b.getString("result_name");

                    for (int i = 0; i < arrayList.size(); i++) {
                        NhanVien employee = arrayList.get(i);
                        if (employee.getManv() == editedId) {
                            employee.setTennv(editedName);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
                break;
        }
        loadEmployeeData();
    }


    private void loadEmployeeData() {
        arrayList.clear();
        arrayList.addAll(nhanVienDatasource.getAllPeople());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        nhanVienDatasource.open();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        nhanVienDatasource.close();
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String keyWord = autoCompleteTextView.getText().toString().trim();
        doSearch(keyWord);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}