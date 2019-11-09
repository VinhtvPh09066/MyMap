package com.example.mymap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private EditText edtTitle1;
    private EditText edtKinhDo1;
    private EditText edtViDo1;
    private Button btnAdd1;
    private Button btnUpdate1;
    private Button btnDelete1;
    private Button btnCancel1;
    private ListView listview;

    List<locationClass> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();

        list = MapsActivity.db.localDAO().getAll();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                list.get(i);

                Intent intent = new Intent(ListActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tag", list.get(i).tagName);
                bundle.putDouble("lat", list.get(i).lat);
                bundle.putDouble("lng", list.get(i).lng);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    public void initView(){
//        edtTitle1 = (EditText) findViewById(R.id.edtTitle1);
//        edtKinhDo1 = (EditText) findViewById(R.id.edtKinhDo1);
//        edtViDo1 = (EditText) findViewById(R.id.edtViDo1);
//        btnAdd1 = (Button) findViewById(R.id.btnAdd1);
//        btnUpdate1 = (Button) findViewById(R.id.btnUpdate1);
//        btnDelete1 = (Button) findViewById(R.id.btnDelete1);
//        btnCancel1 = (Button) findViewById(R.id.btnCancel1);
        listview = (ListView) findViewById(R.id.listview);
    }
}
