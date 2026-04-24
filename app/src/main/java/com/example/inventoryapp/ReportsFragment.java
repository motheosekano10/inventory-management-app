package com.example.inventoryapp;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
public class ReportsFragment extends Fragment {
    @Override public View onCreateView(LayoutInflater i, ViewGroup g, Bundle s) {
        View v = i.inflate(R.layout.fragment_reports, g, false);
        DatabaseHelper db = new DatabaseHelper(getContext());
        RecyclerView rv = v.findViewById(R.id.rv);
        ArrayList<Sale> sales = db.getSales();
        rv.setLayoutManager(new LinearLayoutManager(getContext())); rv.setAdapter(new SaleAdapter(sales));
        double total = db.getTotalSales();
        ((TextView)v.findViewById(R.id.tvTotal)).setText("Total Sales: R" + String.format("%.2f", total));
        double profit = total * 0.3; // Simulated 30% margin
        ((TextView)v.findViewById(R.id.tvProfit)).setText("Est. Profit: R" + String.format("%.2f", profit));
        ProgressBar bar = v.findViewById(R.id.progressBar); bar.setProgress((int)(Math.min(total/10, 100)));
        Button export = v.findViewById(R.id.btnExport);
        export.setOnClickListener(c -> {
            try {
                File f = new File(getContext().getExternalFilesDir(null), "sales_report.csv");
                FileWriter w = new FileWriter(f); w.write("Product,Qty,Total,Date\n");
                for(Sale sl : sales) w.write(sl.productName+","+sl.quantity+","+sl.total+","+sl.date+"\n");
                w.close();
                Intent share = new Intent(Intent.ACTION_SEND); share.setType("text/csv"); share.putExtra(Intent.EXTRA_STREAM, android.net.Uri.fromFile(f));
                startActivity(Intent.createChooser(share, "Export CSV"));
            } catch(Exception e) { Toast.makeText(getContext(), "Export failed", Toast.LENGTH_SHORT).show(); }
        });
        return v;
    }
}
