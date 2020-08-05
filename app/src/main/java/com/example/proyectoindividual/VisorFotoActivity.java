package com.example.proyectoindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class VisorFotoActivity extends AppCompatActivity {
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_foto);

        Intent intent = getIntent();
        String fotoName = intent.getStringExtra("photoName");
        PhotoView photoView = findViewById(R.id.photo_view);
        storageReference = FirebaseStorage.getInstance().getReference();
        publicarImagen(fotoName, photoView);

    }


    public void publicarImagen (final String photoName, final PhotoView photoView){
        storageReference.child("Images").child(photoName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(VisorFotoActivity.this)
                        .load(uri)
                        .into(photoView);
            }

        }); }
}