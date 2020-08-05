package com.example.proyectoindividual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.proyectoindividual.Entidades.Usuario;
import com.example.proyectoindividual.Usuarios.PrincipalUsuarioActivity;
import com.example.proyectoindividual.Usuarios.RegistroActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class LogueoActivity extends AppCompatActivity {
String rol;
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logueo);
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }


    public void iniciarSesion(View view){

        List<AuthUI.IdpConfig> listaProveedores =
                Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()

                );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(listaProveedores)
                        .build(),
                1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Log.d("infoApp","inicio de sesion exitoso");

                //Detectar rol
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                DatabaseReference referencia2 = databaseReference.child("Usuarios").child(uid);

                if (rol == null){
                    Log.d("infoApp","Esperando datos");
                    referencia2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Log.d("infoApp","existe");
                                Usuario usuario = snapshot.getValue(Usuario.class);
                                rol = usuario.getRol();
                                Log.d("infoApp","rol:" + rol);
                                if (rol.equals("admin pucp")){
                                    Intent intent = new Intent(LogueoActivity.this, ListaArchivosPrincipalActivity.class);
                                    startActivity(intent);

                                } else {
                                    Intent intent = new Intent(LogueoActivity.this, PrincipalUsuarioActivity.class);
                                    startActivity(intent);
                                }
                            }else{
                                Log.d("infoApp","no existes");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            } else {

                Log.d("infoApp","inicio erroneo");
            }

        }

    }

    public void abrirRegistrosActivity(View view){

        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);


    }
}