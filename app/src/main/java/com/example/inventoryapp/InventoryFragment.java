package com.example.inventoryapp;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class InventoryFragment extends Fragment implements ProductAdapter.Listener {
    RecyclerView rv; ProductAdapter adapter; ArrayList<Product> list; DatabaseHelper db;
    @Override public View onCreateView(LayoutInflater i, ViewGroup g, Bundle s) {
        View v = i.inflate(R.layout.fragment_inventory, g, false);
        rv = v.findViewById(R.id.rv); db = new DatabaseHelper(getContext()); list = db.getProducts();
        rv.setLayoutManager(new LinearLayoutManager(getContext())); adapter = new ProductAdapter(list, this); rv.setAdapter(adapter);
        v.findViewById(R.id.btnAdd).setOnClickListener(c -> showAddDialog(null));
        return v;
    }
    @Override public void onResume() { super.onResume(); list.clear(); list.addAll(db.getProducts()); adapter.notifyDataSetChanged(); }
    @Override public void onEdit(Product p) { showAddDialog(p); }
    @Override public void onDelete(int id) {
        new AlertDialog.Builder(getContext()).setTitle("Delete?").setMessage("Remove this product?").setPositiveButton("Yes", (d,w)->{ db.deleteProduct(id); onResume(); }).setNegativeButton("No", null).show();
    }
    void showAddDialog(Product p) {
        View dv = LayoutInflater.from(getContext()).inflate(R.layout.dialog_product, null);
        EditText name = dv.findViewById(R.id.etName), price = dv.findViewById(R.id.etPrice), stock = dv.findViewById(R.id.etStock);
        Spinner cat = dv.findViewById(R.id.spCat);
        if(p != null) { name.setText(p.name); price.setText(String.valueOf(p.price)); stock.setText(String.valueOf(p.stock)); }
        new AlertDialog.Builder(getContext()).setTitle(p==null?"Add":"Edit").setView(dv).setPositiveButton("Save", (d,w)->{
            String n=name.getText().toString(), c=cat.getSelectedItem().toString();
            double pr = Double.parseDouble(price.getText().toString()); int st = Integer.parseInt(stock.getText().toString());
            if(p==null) db.addProduct(n,pr,st,c); else db.updateProduct(p.id,n,pr,st,c);
            onResume(); Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }).show();
    }
}
