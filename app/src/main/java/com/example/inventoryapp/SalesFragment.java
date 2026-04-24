package com.example.inventoryapp;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Calendar;
public class SalesFragment extends Fragment {
    @Override public View onCreateView(LayoutInflater i, ViewGroup g, Bundle s) {
        View v = i.inflate(R.layout.fragment_sales, g, false);
        DatabaseHelper db = new DatabaseHelper(getContext());
        Spinner sp = v.findViewById(R.id.spProd); EditText qty = v.findViewById(R.id.etQty); Button btn = v.findViewById(R.id.btnSave);
        ArrayList<Product> prods = db.getProducts();
        ArrayList<String> names = new ArrayList<>(); for(Product p : prods) names.add(p.name + " (R" + p.price + ")");
        sp.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, names));
        v.findViewById(R.id.etDate).setOnClickListener(c -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(getContext(), (d,y,m,day)-> ((EditText)c).setText(y+"-"+(m+1)+"-"+day), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });
        btn.setOnClickListener(c -> {
            if(prods.isEmpty()) { Toast.makeText(getContext(), "Add products first", Toast.LENGTH_SHORT).show(); return; }
            int idx = sp.getSelectedItemPosition(); Product p = prods.get(idx);
            int q = Integer.parseInt(qty.getText().toString());
            if(q > p.stock) { Toast.makeText(getContext(), "Not enough stock!", Toast.LENGTH_SHORT).show(); return; }
            db.recordSale(p.id, p.name, p.price, q);
            Toast.makeText(getContext(), "Sale recorded!", Toast.LENGTH_SHORT).show();
            qty.setText("");
        });
        return v;
    }
}
