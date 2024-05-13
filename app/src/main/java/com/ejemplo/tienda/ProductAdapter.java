package com.ejemplo.tienda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private AdapterView.OnItemClickListener onItemClickListener;

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_product, parent, false);
        }

        // Obtén referencias a las vistas en el diseño del elemento de la lista
        TextView textProductName = convertView.findViewById(R.id.textProductName);
        TextView textProductPrice = convertView.findViewById(R.id.textProductPrice);
        TextView textProductQuantity = convertView.findViewById(R.id.textProductQuantity);

        // Obtén el producto en la posición actual
        Product currentProduct = getItem(position);

        // Establece los valores en las vistas
        textProductName.setText(currentProduct.getName());
        textProductPrice.setText("Precio: " + formatPrice(currentProduct.getPrice())); // Formatea el precio
        textProductQuantity.setText("Cantidad: " + currentProduct.getQuantity());

        return convertView;
    }

    // Método para formatear el precio eliminando los decimales innecesarios
    private String formatPrice(double price) {
        // Verifica si el precio tiene decimales
        if (price == (long) price) {
            // Si no hay decimales, muestra el precio como un número entero
            return String.format("%d", (long) price);
        } else {
            // Si hay decimales, muestra el precio con dos decimales
            return String.format("%.2f", price);
        }
    }

    // Método para configurar el OnItemClickListener
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
