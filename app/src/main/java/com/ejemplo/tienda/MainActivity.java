package com.ejemplo.tienda;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.util.Log;
import android.widget.EditText;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Definir las constantes para las opciones del menú
    public static final int MENU_SEARCH = R.id.MENU_SEARCH; // No asignar valor por defecto
    public static final int MENU_DELETE_ALL = R.id.MENU_DELETE_ALL;
    public static final int MENU_EXIT = R.id.MENU_EXIT;
    public static final int MENU_ABOUT = R.id.MENU_ABOUT;
    public static final int MENU_VIEW_ID = R.id.menu_view;
    private static final int MENU_EDIT_ID = R.id.menu_edit;
    private static final int MENU_DELETE_ID = R.id.menu_delete;

    private List<Product> productList;
    private ProductAdapter productAdapter;

// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar la lista de productos
        productList = new ArrayList<>();

        // Inicializar el adaptador y asignarlo a la ListView
        productAdapter = new ProductAdapter(this, productList);
        ListView listViewProducts = findViewById(R.id.listViewProducts);
        listViewProducts.setAdapter(productAdapter);


        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selectedProduct = productList.get(position);
                showProductDetailsDialog(selectedProduct);
            }
        });

        // Registrar la ListView para el menú contextual
        registerForContextMenu(listViewProducts);

        // Manejar el clic en el botón para agregar un nuevo producto
        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para agregar un nuevo producto
                showAddProductDialog();
            }
        });
    }
// ...



    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Nuevo Producto");

        // Inflar el diseño personalizado para el diálogo
        View view = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        builder.setView(view);

        final EditText etProductName = view.findViewById(R.id.etProductName);
        final EditText etProductPrice = view.findViewById(R.id.etProductPrice);
        final EditText etProductQuantity = view.findViewById(R.id.etProductQuantity);

        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener los valores ingresados por el usuario
                String productName = etProductName.getText().toString().trim();
                String priceString = etProductPrice.getText().toString().trim();
                String quantityString = etProductQuantity.getText().toString().trim();

                // Verificar si el producto ya existe
                if (isProductAlreadyExists(productName)) {
                    // Si el producto ya existe, muestra un mensaje
                    Toast.makeText(MainActivity.this, "Producto ya existente", Toast.LENGTH_SHORT).show();
                } else {
                    // Si el producto no existe, continúa con la lógica para agregarlo
                    if (!productName.isEmpty() && !priceString.isEmpty() && !quantityString.isEmpty()) {
                        // Convertir el precio y la cantidad a números
                        double price = Double.parseDouble(priceString);
                        int quantity = Integer.parseInt(quantityString);

                        // Crear un nuevo producto y agregarlo a la lista
                        Product newProduct = new Product(productName, price, quantity);
                        productList.add(newProduct);
                        productAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Producto agregado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }

    // Método para verificar si el producto ya existe en la lista
    private boolean isProductAlreadyExists(String productName) {
        for (Product product : productList) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return true; // El producto ya existe
            }
        }
        return false; // El producto no existe
    }



    // Crear menú contextual para la ListView
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        if (item.getItemId() == R.id.menu_view) {
            // Lógica para ver un producto
            Log.d("MainActivity", "Prueba de log");
            Product selectedProduct = productList.get(position);
            showProductDetailsDialog(selectedProduct);
            Toast.makeText(this, "Ver producto: " + productList.get(position).getName(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.menu_edit) {

            // Lógica para editar un producto
            Product selectedProductForEdit = productList.get(position);
            showEditProductDialog(selectedProductForEdit);
            Toast.makeText(this, "Editar producto: " + productList.get(position).getName(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            // Lógica para eliminar un producto
            productList.remove(position);
            productAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private void showProductDetailsDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detalles del Producto");

        // Inflar el diseño personalizado para el diálogo
        View view = getLayoutInflater().inflate(R.layout.dialog_product_details, null);
        builder.setView(view);

        // Configurar los elementos de la ventana emergente con los detalles del producto
        TextView dialogTextProductName = view.findViewById(R.id.dialogTextProductName);
        TextView dialogTextProductPrice = view.findViewById(R.id.dialogTextProductPrice);
        TextView dialogTextProductQuantity = view.findViewById(R.id.dialogTextProductQuantity);

        dialogTextProductName.setText("Nombre: " + product.getName());
        dialogTextProductPrice.setText("Precio: " + formatPrice(product.getPrice()));
        dialogTextProductQuantity.setText("Cantidad: " + product.getQuantity());

        builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lógica para editar el producto
                showEditProductDialog(product);
            }
        });

        builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lógica para eliminar el producto
                productList.remove(product);
                productAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Cancelar", null);

        builder.show();
    }
    private void showEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Producto");

        // Inflar el diseño personalizado para el diálogo
        View view = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        builder.setView(view);

        final EditText etProductName = view.findViewById(R.id.etProductName);
        final EditText etProductPrice = view.findViewById(R.id.etProductPrice);
        final EditText etProductQuantity = view.findViewById(R.id.etProductQuantity);

        // Establecer los valores iniciales en los campos de texto
        etProductName.setText(product.getName());
        etProductPrice.setText(formatPrice(product.getPrice())); // Formatear el precio
        etProductQuantity.setText(String.valueOf(product.getQuantity()));

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener los valores ingresados por el usuario
                String productName = etProductName.getText().toString().trim();
                String priceString = etProductPrice.getText().toString().trim();
                String quantityString = etProductQuantity.getText().toString().trim();

                if (!productName.isEmpty() && !priceString.isEmpty() && !quantityString.isEmpty()) {
                    // Convertir el precio y la cantidad a números
                    double price = Double.parseDouble(priceString);
                    int quantity = Integer.parseInt(quantityString);

                    // Actualizar el producto existente con los nuevos valores
                    product.setName(productName);
                    product.setPrice(price);
                    product.setQuantity(quantity);

                    // Notificar al adaptador que los datos han cambiado
                    productAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }


    private String formatPrice(double price) {
        // Formatear el precio para eliminar los decimales si no son necesarios
        if (price == (long) price) {
            return String.format("%d", (long) price);
        } else {
            return String.format("%s", price);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log.d("MainActivity", "entrar al if");
        //Toast.makeText(this, "entre al if", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "ID del elemento: " + item.getItemId(), Toast.LENGTH_SHORT).show();

        if (item.getItemId() == MENU_SEARCH) {
            Log.d("MainActivity", "Se ha hecho clic en Buscar");
            // **Prueba**: Mostrar un mensaje de prueba en lugar de `showSearchDialog`
            showSearchDialog();
            //Toast.makeText(this, "Botón Buscar presionado", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (item.getItemId() == MENU_DELETE_ALL) {

            // Acción para el botón de vaciar lista
            clearProductList();
            return true;
        }

        if (item.getItemId() == MENU_EXIT) {
            // Acción para el botón de salir
            finish();
            return true;
        }

        if (item.getItemId() == MENU_ABOUT) {
            // Acción para el botón de acerca de
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showSearchDialog() {
        Log.d("MainActivity", "showSearchDialog called"); // Verifica en Logcat
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Buscar Producto");

        // Inflar el diseño personalizado para el diálogo
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String searchQuery = input.getText().toString().trim();
                // Lógica para buscar el producto
                searchProduct(searchQuery);
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void clearProductList() {
        // Elimina todos los productos de la lista
        productList.clear();
        // Notifica al adaptador que los datos han cambiado
        productAdapter.notifyDataSetChanged();
        // Muestra un mensaje indicando que la lista ha sido vaciada
        Toast.makeText(this, "Lista vaciada", Toast.LENGTH_SHORT).show();
    }

    private void searchProduct(String query) {
        List<Product> searchResults = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                // Agregar el producto a los resultados de búsqueda si el nombre coincide
                searchResults.add(product);
            }
        }

        if (!searchResults.isEmpty()) {
            // Mostrar los resultados de la búsqueda
            showSearchResultsDialog(searchResults);
        } else {
            // Mostrar un mensaje indicando que no se encontraron resultados
            Toast.makeText(this, "No se encontraron productos", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSearchResultsDialog(List<Product> searchResults) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resultados de la búsqueda");

        // Crear una cadena para mostrar los resultados de la búsqueda
        StringBuilder resultString = new StringBuilder();
        for (Product product : searchResults) {
            resultString.append("Nombre: ").append(product.getName()).append("\n");
            resultString.append("Precio: ").append(product.getPrice()).append("\n");
            resultString.append("Cantidad: ").append(product.getQuantity()).append("\n\n");
        }

        // Mostrar los resultados en un cuadro de diálogo
        builder.setMessage(resultString.toString());
        builder.setPositiveButton("Aceptar", null);
        builder.show();
    }







}
