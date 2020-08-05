package com.example.proyectoindividual.Administrador;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoindividual.Entidades.Archivo;
import com.example.proyectoindividual.Entidades.uploadinfo;
import com.example.proyectoindividual.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class NuevoArchivoActivity extends AppCompatActivity {

    Button btnbrowse, btnupload, btnbrowsepdf;
    EditText txtdata ;
    ImageView imgview;
    Spinner spinner, spinner2;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    int File_Request_Code= 86;
    ProgressDialog progressDialog ;
    String tituloArchivo = "titulo_generico";
    String descripcionArchivo = "descripcion_generica";
    String nombreArchivo = "nombre_archivo";
    String tipoArchivo = "Foto";
    String categoriaArchivo = "ocio";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_archivo);

        storageReference = FirebaseStorage.getInstance().getReference("PDF");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        btnbrowse = findViewById(R.id.btn_browse);
        btnupload= findViewById(R.id.btn_upload);
        btnbrowsepdf = findViewById(R.id.btn_browsepdf);
        txtdata = findViewById(R.id.txt_data);
        imgview = findViewById(R.id.imageView);
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinnerCategoria);
        categoriaArchivo = spinner.getSelectedItem().toString();
        progressDialog = new ProgressDialog(NuevoArchivoActivity.this);// context name as per your project name

        // Definicion de tipo de archivo a enviar
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("Foto")){

                    btnbrowse.setVisibility(View.VISIBLE);
                    btnbrowsepdf.setVisibility(View.GONE);
                    tipoArchivo = "Foto";


                } else if (spinner.getSelectedItem().toString().equals("PDF")) {
                    btnbrowse.setVisibility(View.GONE);
                    btnbrowsepdf.setVisibility(View.VISIBLE);
                    tipoArchivo = "PDF";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                btnupload.setVisibility(View.VISIBLE);
                btnbrowsepdf.setVisibility(View.GONE);
            }

        });




        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
                nombreArchivo = getSaltString();

            }
        });

        btnbrowsepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), File_Request_Code);
                nombreArchivo = getSaltString();

            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Descripcion Incidencia
                descripcionArchivo = txtdata.getText().toString();
                //Selección categoría


                final Archivo archivo = new Archivo();
                archivo.setDescripcion(descripcionArchivo);
                archivo.setCategoria(categoriaArchivo);
                archivo.setFavorito("si");
                archivo.setTitulo(tituloArchivo);
                archivo.setTipo(tipoArchivo);
                archivo.setNombreArchivo(nombreArchivo);

                if (tipoArchivo.equals("Foto")){

                    UploadImage();

                } else if (tipoArchivo.equals("PDF")){

                    UploadPDF();
                }


                databaseReference.child("Archivos").push().setValue(archivo);

            }
        });


    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string. heroe
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        } else if (requestCode == File_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null){

            FilePathUri = data.getData();

        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Subiendo imagen...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(nombreArchivo + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String TempImageName = nombreArchivo;
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Imagen subida exitosamente ", Toast.LENGTH_LONG).show();
                            @SuppressWarnings("VisibleForTests")
                            uploadinfo imageUploadInfo = new uploadinfo(TempImageName, taskSnapshot.getUploadSessionUri().toString());
                            String ImageUploadId = databaseReference.push().getKey();
                            databaseReference.child("Images").child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    });
        }
        else {

            Toast.makeText(NuevoArchivoActivity.this, "Por favor seleccione una imagen o añada un nombre", Toast.LENGTH_LONG).show();

        }
    }

    public void UploadPDF() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Subiendo pdf...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(nombreArchivo + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String TempImageName = nombreArchivo;
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "PDF subido exitosamente ", Toast.LENGTH_LONG).show();
                            @SuppressWarnings("VisibleForTests")
                            uploadinfo imageUploadInfo = new uploadinfo(TempImageName, taskSnapshot.getUploadSessionUri().toString());
                            String ImageUploadId = databaseReference.push().getKey();
                            databaseReference.child("PDF").child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    });
        }
        else {

            Toast.makeText(NuevoArchivoActivity.this, "Por favor seleccione un archivo o añada un nombre", Toast.LENGTH_LONG).show();

        }
    }








}