package com.example.proyectoindividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectoindividual.Entidades.Archivo;
import com.example.proyectoindividual.Entidades.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CategoriaActivity extends AppCompatActivity {

    Archivo[] listaaArchivos;
    private int DETALLES_INCIDENCIAS_PROPIAS = 2;
    String nombreUsuario;
    Usuario usuario = new Usuario();
    String categoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);



    }

    public void mostrarOcio(View view){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        databaseReference.child("Usuarios").child(uid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Archivo archivo = snapshot.getValue(Archivo.class);
                    //   autorIncidencia = snapshot.child("nombre").getValue().toString();
                    nombreUsuario = usuario.getNombre();

                    databaseReference.child("Archivos").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                Long longitudArchivos = dataSnapshot.getChildrenCount();
                                int longitud = longitudArchivos.intValue();
                                ArrayList listaMisArchivos = new ArrayList<Archivo>();
                                //Incidencia[longitud];
                                int contador = 0;

                                for (DataSnapshot children : dataSnapshot.getChildren()) {
                                    if (dataSnapshot.exists()) {
                                        final Archivo archivo = children.getValue(Archivo.class);
                                        final String nombreRaroIncidencia = children.getKey();
                                        archivo.setApiKey(nombreRaroIncidencia);

                                        if (archivo.getCategoria().equals("Ocio") || archivo.getCategoria().equals("ocio")) {

                                            listaMisArchivos.add(archivo);
                                            contador++;
                                        } else {
                                            contador = contador + 0;
                                        }




                                    }
                                }

                                int contador2 = listaMisArchivos.size();
                                int contador3   = contador2 +1 ;
                                // :C
                                listaaArchivos = new Archivo[contador2];

                                for (int x = 0; x < contador2; x++){

                                    listaaArchivos[x] = (Archivo) listaMisArchivos.get(x);

                                }
                                final StorageReference fStorage = FirebaseStorage.getInstance().getReference();
                                ListaArchivosAdapter archivosAdapter = new ListaArchivosAdapter(listaaArchivos, CategoriaActivity.this, fStorage,
                                        DETALLES_INCIDENCIAS_PROPIAS);
                                //RecyclerView recyclerView = findViewById(R.id.recyclerView2);
                                //ecyclerView.setAdapter(archivosAdapter);
                                //recyclerView.setLayoutManager(new LinearLayoutManager(CategoriaActivity.this));

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(CategoriaActivity.this, "Error Base de Datos", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
/*
    public void mostrarEducativo(View view){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        databaseReference.child("Usuarios").child(uid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Archivo archivo = snapshot.getValue(Archivo.class);
                    //   autorIncidencia = snapshot.child("nombre").getValue().toString();
                    nombreUsuario = usuario.getNombre();

                    databaseReference.child("Archivos").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                Long longitudArchivos = dataSnapshot.getChildrenCount();
                                int longitud = longitudArchivos.intValue();
                                ArrayList listaMisArchivos = new ArrayList<Archivo>();
                                //Incidencia[longitud];
                                int contador = 0;

                                for (DataSnapshot children : dataSnapshot.getChildren()) {
                                    if (dataSnapshot.exists()) {
                                        final Archivo archivo = children.getValue(Archivo.class);
                                        final String nombreRaroIncidencia = children.getKey();
                                        archivo.setApiKey(nombreRaroIncidencia);

                                        if (archivo.getCategoria().equals("Educativo")) {

                                            listaMisArchivos.add(archivo);
                                            contador++;
                                        } else {
                                            contador = contador + 0;
                                        }




                                    }
                                }

                                int contador2 = listaMisArchivos.size();
                                int contador3   = contador2 +1 ;
                                // :C
                                listaaArchivos = new Archivo[contador2];

                                for (int x = 0; x < contador2; x++){

                                    listaaArchivos[x] = (Archivo) listaMisArchivos.get(x);

                                }
                                final StorageReference fStorage = FirebaseStorage.getInstance().getReference();
                                ListaArchivosAdapter archivosAdapter = new ListaArchivosAdapter(listaaArchivos, CategoriaActivity.this, fStorage,
                                        DETALLES_INCIDENCIAS_PROPIAS);
                                RecyclerView recyclerView = findViewById(R.id.recyclerView2);
                                recyclerView.setAdapter(archivosAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(CategoriaActivity.this));

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(CategoriaActivity.this, "Error Base de Datos", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
*/


}