package com.example.proyectoindividual.Usuarios;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoindividual.LogueoActivity;
import com.example.proyectoindividual.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoindividual.Entidades.Usuario;
import com.example.proyectoindividual.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextMail, editTextPass, editTextName;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    Usuario usuario = new Usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editTextMail = findViewById(R.id.editTextTextPersonName);
        editTextPass = findViewById(R.id.editTextTextPersonName2);
        editTextName = findViewById(R.id.editTextTextPersonName3);
        progressDialog = new ProgressDialog(RegistroActivity.this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            //Verificar si usuario ya esta logueado

        }
    }

    public void registrarUsuario(View view){
        final String correo = editTextMail.getText().toString().trim();
        final String contrasena = editTextPass.getText().toString().trim();
        final String nombre = editTextName.getText().toString().trim();
        final String rol = "usuario pucp";
        progressDialog.setTitle("Registrando...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d("InfoApp","Se logró enviar el correo");
                    usuario = new Usuario(
                            nombre,
                            correo,
                            contrasena,
                            rol
                    );
                    Toast.makeText(RegistroActivity.this, "Registrado exitosamente", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(RegistroActivity.this, LogueoActivity.class);
                    startActivity(intent);
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d("InfoApp","A punto de iniciar el listener de DB...");
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {


                            } else {
                                //Mostrar error

                            }
                        }

                    });
                    progressDialog.dismiss();









                }else{

                    Toast.makeText(RegistroActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }

        });



    }
}