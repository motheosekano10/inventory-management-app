package com.example.inventoryapp;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {
    ArrayList<Product> list;
    public interface Listener { void onEdit(Product p); void onDelete(int id); }
    Listener listener;
    public ProductAdapter(ArrayList<Product> list, Listener l) { this.list = list; this.listener = l; }
    @Override public VH onCreateViewHolder(ViewGroup p, int v) { return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_product, p, false)); }
    @Override public void onBindViewHolder(VH h, int pos) {
        Product p = list.get(pos);
        h.name.setText(p.name); h.price.setText("R" + p.price); h.stock.setText("Stock: " + p.stock); h.cat.setText(p.category);
        h.edit.setOnClickListener(v -> listener.onEdit(p));
        h.delete.setOnClickListener(v -> listener.onDelete(p.id));
    }
    @Override public int getItemCount() { return list.size(); }
    class VH extends RecyclerView.ViewHolder {
        TextView name, price, stock, cat; Button edit, delete;
        VH(View v) { super(v); name=v.findViewById(R.id.tvName); price=v.findViewById(R.id.tvPrice); stock=v.findViewById(R.id.tvStock); cat=v.findViewById(R.id.tvCat); edit=v.findViewById(R.id.btnEdit); delete=v.findViewById(R.id.btnDelete); }
    }
}
