package com.example.inventoryapp;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle s) {
        super.onCreate(s); setContentView(R.layout.activity_main);
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(item -> {
            Fragment f = null;
            if(item.getItemId() == R.id.navDash) f = new DashboardFragment();
            else if(item.getItemId() == R.id.navInv) f = new InventoryFragment();
            else if(item.getItemId() == R.id.navSales) f = new SalesFragment();
            else if(item.getItemId() == R.id.navRep) f = new ReportsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
            return true;
        });
        nav.setSelectedItemId(R.id.navDash);
    }
}
