package com.example.mymap;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText edtKinhDo;
    private EditText edtViDo;
    private Button btnAdd;
    private Button btnUpdate;
    private Button btnDelete;
    private Button btnCancel;
    private Button btnXem;
    private EditText edtTitle;

    Intent intent;

    private List<locationClass> localList;
    public static AppDatabase db;
    Marker m = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initView();
        localList = new ArrayList<>();
        intent = new Intent(MapsActivity.this, ListActivity.class);


        // allowMainThreadQueries : cho phép câu lệnh query chạy trực tiếp trên luồng chính
        // nếu truy vấn dữ liệu lớn sẽ gây lag, giật cho ứng dụng
        // vì vậy chúng ta nên mở luông riêng bằng AsyncTask để đảm bảo ứng dụng chay mượt

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "local2.db").allowMainThreadQueries().build();



        localList = db.localDAO().getAll();
        Toast.makeText(this, "Có " + localList.size() + " marker", Toast.LENGTH_LONG).show();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLocation();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delLocal();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForm();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLocation(m);
            }
        });

        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng dongQuan = new LatLng(21.0397577, 105.8045871);
//        mMap.addMarker(new MarkerOptions().position(dongQuan).title("Đông Quan"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(dongQuan));

        //Hiển thị các location có trong Sqlite
        for (int i = 0; i < localList.size(); i++) {
            locationClass lc = localList.get(i);
            mMap.addMarker(new MarkerOptions().position(new LatLng(lc.lat, lc.lng)).title(lc.tagName));
            Log.e("local", lc.id + " / " + lc.tagName + "");
        }

        //Hiển thị vị trí khi click vào item listview
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            LatLng nhan = new LatLng(bundle.getDouble("lat"), bundle.getDouble("lng"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(nhan));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("marker id", marker.getId());
                Toast.makeText(MapsActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();

                edtKinhDo.setText(String.valueOf(marker.getPosition().latitude));
                edtViDo.setText(String.valueOf(marker.getPosition().longitude));
                edtTitle.setText(marker.getTitle());
                m = marker;
                return false;

            }
        });

    }


    public void initView() {
        edtKinhDo = (EditText) findViewById(R.id.edtKinhDo);
        edtViDo = (EditText) findViewById(R.id.edtViDo);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnXem = (Button) findViewById(R.id.btnXem);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
    }

    public void updateLocation(Marker marker) {
        try {
            double lat = marker.getPosition().latitude;
            double lng = marker.getPosition().longitude;
            locationClass lc = db.localDAO().getLocation(lat, lng);

            double lat1 = Double.parseDouble(edtKinhDo.getText().toString().trim());
            double lng1 = Double.parseDouble(edtViDo.getText().toString().trim());
            String title = edtTitle.getText().toString();

            locationClass lc2 = new locationClass(lc.id, lat1, lng1, title);

            int result = db.localDAO().updateLocal(lc2);

            if (result > 0) {
                marker.remove();
                Toast.makeText(MapsActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();

                LatLng local = new LatLng(lc2.lat, lc2.lng);
                m = mMap.addMarker(new MarkerOptions().position(local).title(title));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(local));

            } else {
                Toast.makeText(MapsActivity.this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException error) {
            Toast.makeText(this, "Lỗi định dạng số", Toast.LENGTH_SHORT).show();
        }
    }

    public void addLocation() {
        try {
            if (m != null) {
                Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            } else {
                double lat = Double.parseDouble(edtKinhDo.getText().toString().trim());
                double lng = Double.parseDouble(edtViDo.getText().toString().trim());
                String title = edtTitle.getText().toString();

                locationClass lc = new locationClass();
                lc.id = new Random().nextInt();
                lc.lat = lat;
                lc.lng = lng;
                lc.tagName = title;

                long[] result = db.localDAO().insertAll(lc);
                Log.e("Thêm local", result[0] + "");
                if (result[0] > 0) {
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    clearForm();

                    LatLng local = new LatLng(lat, lng);
                    m = mMap.addMarker(new MarkerOptions().position(local).title(title));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(local));
                    //Chuyển màn hình
                    startActivity(intent);

                    Log.e("local id", lc.id + "");
                } else {
                    Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            } catch(NumberFormatException error){
                Toast.makeText(this, "Lỗi định dạng số", Toast.LENGTH_SHORT).show();
            } catch(SQLiteConstraintException ex){
                Toast.makeText(this, "Lỗi trùng id", Toast.LENGTH_SHORT).show();
            }

    }

    public void delLocal() {

        try {
            double lat = Double.parseDouble(edtKinhDo.getText().toString().trim());
            double lng = Double.parseDouble(edtViDo.getText().toString().trim());
            String title = edtTitle.getText().toString();

            locationClass lc = db.localDAO().getLocation(lat, lng);
            int result = db.localDAO().deleteLocal(lc);

            if (result > 0) {
                Toast.makeText(MapsActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                if (m != null) {
                    m.remove();
                }
                clearForm();
            } else {
                Toast.makeText(MapsActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(MapsActivity.this, "Hãy chọn địa điểm muốn xóa", Toast.LENGTH_SHORT).show();
        }

    }

    private void clearForm() {
        m = null;
        edtViDo.setText("");
        edtKinhDo.setText("");
        edtTitle.setText("");
    }


}
