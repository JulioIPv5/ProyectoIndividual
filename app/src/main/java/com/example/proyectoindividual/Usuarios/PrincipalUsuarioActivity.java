package com.example.proyectoindividual.Usuarios;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Toast;

        import com.example.proyectoindividual.Entidades.Archivo;
        import com.example.proyectoindividual.Administrador.NuevoArchivoActivity;
        import com.example.proyectoindividual.ListaArchivosAdapter;
        import com.example.proyectoindividual.LogueoActivity;
        import com.example.proyectoindividual.R;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.database.annotations.NotNull;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;

        import java.security.Principal;

public class PrincipalUsuarioActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Archivo[] listaArchivos;
    private StorageReference storageReference;
    private FirebaseStorage fStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_usuario);

        //Código de onCreate

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Archivos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Long longitudArchivos = dataSnapshot.getChildrenCount();
                    int longitud = longitudArchivos.intValue();
                    listaArchivos = new Archivo[longitud];
                    int contador = 0;

                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            final Archivo archivo = children.getValue(Archivo.class);
                            final String nombreRaroArchivo = children.getKey();  archivo.setApiKey(nombreRaroArchivo);
                            final StorageReference fStorage = FirebaseStorage.getInstance().getReference();
                            final ListaArchivosAdapter archivosAdapter = new ListaArchivosAdapter(listaArchivos, com.example.proyectoindividual.Usuarios.PrincipalUsuarioActivity.this, fStorage,
                                    1);

                            RecyclerView recyclerView = findViewById(R.id.recyclerView3);
                            recyclerView.setAdapter(archivosAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(com.example.proyectoindividual.Usuarios.PrincipalUsuarioActivity.this));
                            listaArchivos[contador] = archivo;
                            contador++;

                        }
                    }




                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(com.example.proyectoindividual.Usuarios.PrincipalUsuarioActivity.this,"Error Base de Datos",Toast.LENGTH_LONG).show();
            }
        });





    }              // <--- Final del onCreate

    //A partir de acá solo métodos

    public void abrirFavoritosActivity(View view){

       // Intent intent = new Intent(this, ListaArchivosFavoritosActivity.class);
        //startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarusuario,menu);
        //String nombreLogueado = usuario.getNombre();
        // menu.findItem(R.id.nombreUsuario).setTitle(nombreLogueado); Si se puede dar la bienvenida en
        return true;  }


    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.cerrarSesion:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(PrincipalUsuarioActivity.this, LogueoActivity.class));
                return true;
        }
        return onOptionsItemSelected(item);}

}