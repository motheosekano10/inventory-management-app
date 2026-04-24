package com.example.inventoryapp;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.VH> {
    ArrayList<Sale> list;
    public SaleAdapter(ArrayList<Sale> list) { this.list = list; }
    @Override public VH onCreateViewHolder(ViewGroup p, int v) { return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_sale, p, false)); }
    @Override public void onBindViewHolder(VH h, int pos) {
        Sale s = list.get(pos);
        h.product.setText(s.productName); h.qty.setText("Qty: " + s.quantity); h.total.setText("R" + String.format("%.2f", s.total)); h.date.setText(s.date);
    }
    @Override public int getItemCount() { return list.size(); }
    class VH extends RecyclerView.ViewHolder {
        TextView product, qty, total, date;
        VH(View v) { super(v); product=v.findViewById(R.id.tvProduct); qty=v.findViewById(R.id.tvQty); total=v.findViewById(R.id.tvTotal); date=v.findViewById(R.id.tvDate); }
    }
}
