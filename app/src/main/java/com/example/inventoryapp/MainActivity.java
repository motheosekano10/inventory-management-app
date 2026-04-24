package com.example.inventoryapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        if (nav != null) {
            nav.setOnItemSelectedListener(item -> {
                Fragment f = null;
                int id = item.getItemId();
                if (id == R.id.navDash) {
                    f = new DashboardFragment();
                } else if (id == R.id.navInv) {
                    f = new InventoryFragment();
                } else if (id == R.id.navSales) {
                    f = new SalesFragment();
                } else if (id == R.id.navRep) {
                    f = new ReportsFragment();
                }
                
                if (f != null) {
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, f)
                        .commit();
                }
                return true;
            });
            nav.setSelectedItemId(R.id.navDash);
        }
    }
}
