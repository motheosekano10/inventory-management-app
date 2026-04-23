package com.example.inventoryapp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
public class DashboardFragment extends Fragment {
    @Override public View onCreateView(LayoutInflater i, ViewGroup g, Bundle s) {
        View v = i.inflate(R.layout.fragment_dashboard, g, false);
        DatabaseHelper db = new DatabaseHelper(getContext());
        ((TextView)v.findViewById(R.id.tvInvVal)).setText("$" + String.format("%.2f", db.getInventoryValue()));
        ((TextView)v.findViewById(R.id.tvSalesTot)).setText("$" + String.format("%.2f", db.getTotalSales()));
        int low = db.getLowStock().size();
        ((TextView)v.findViewById(R.id.tvLowStock)).setText(low > 0 ? "⚠️ " + low + " items low" : "✅ Stock healthy");
        return v;
    }
}
