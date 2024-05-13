package com.ejemplo.tienda;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Inicializar TextView para la descripción
        TextView textViewDescription = findViewById(R.id.textViewDescription);
        // Establecer la descripción
        //textViewDescription.setText("Soy un entusiasta de la programación y un ingeniero en sistemas.");

        // Inicializar ImageView para la foto del programador
        ImageView imageViewPhoto = findViewById(R.id.imageViewProfile);
        // Establecer la imagen de la foto del programador
        imageViewPhoto.setImageResource(R.drawable.profile_picture);

        // Inicializar el botón de regresar
        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finalizar esta actividad y volver a la actividad anterior (MainActivity)
                finish();
            }
        });
    }
}
