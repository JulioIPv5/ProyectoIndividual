package com.example.proyectoindividual;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectoindividual.Entidades.Archivo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ListaArchivosAdapter extends RecyclerView.Adapter<ListaArchivosAdapter.ArchivoViewHolder> {

    Archivo[] listaArchivos;
    private Context contexto;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private int condicionDetalles;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    public ListaArchivosAdapter(Archivo[] lista, Context contexto, StorageReference storageReference, int condicionDetalles){
        this.listaArchivos = lista;
        this.contexto = contexto;
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.storageReference = storageReference;
        this.condicionDetalles = condicionDetalles;}

    public static class ArchivoViewHolder extends RecyclerView.ViewHolder {
        public TextView tituloArchivo;
        public ImageView fotoArchivo;
        public TextView  categoriaArchivo;
        public TextView  descripcionArchivo;
        public Button buttonArchivo, buttonEliminar;

        public ArchivoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tituloArchivo = itemView.findViewById(R.id.txt_nombre);
            this.fotoArchivo = itemView.findViewById(R.id.imageViewFoto);
            this.categoriaArchivo = itemView.findViewById(R.id.txt_categoria);
            this.descripcionArchivo = itemView.findViewById(R.id.txt_descripcion);
            this.buttonArchivo = itemView.findViewById(R.id.btn_abrir);
            this.buttonEliminar = itemView.findViewById(R.id.btn_borrar);
        } }



    @NonNull
    @Override
    public ArchivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexto).inflate(R.layout.activity_lista_principal_rv,parent,false);
        ArchivoViewHolder archivoViewHolder = new ArchivoViewHolder(itemView);
        return archivoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivoViewHolder holder, int position) {
        final Archivo archivo = listaArchivos[position];
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String tituloArchivo = archivo.getTitulo(); holder.tituloArchivo.setText(tituloArchivo);
        String categoriaArchivo = archivo.getCategoria(); holder.categoriaArchivo.setText(categoriaArchivo);
        String descripcionArchivo = archivo.getDescripcion(); holder.descripcionArchivo.setText(descripcionArchivo);
        String tipoArchivo = archivo.getTipo();
        if (tipoArchivo.equals("foto") || tipoArchivo.equals("Foto")){
            publicarImagen(archivo.getNombreArchivo() + ".jpg", holder);

            if (condicionDetalles == 1) {
                holder.buttonArchivo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(contexto,VisorFotoActivity.class);
                        intent.putExtra("photoName",archivo.getNombreArchivo()+".jpg");
                        contexto.startActivity(intent);}
                });
                holder.buttonEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String apiKeyArchivo = archivo.getApiKey();
                        databaseReference.child("Archivos").child(apiKeyArchivo).removeValue();
                    } });

            }
        } else if (tipoArchivo.equals("PDF")){
            holder.buttonArchivo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contexto,VisorPdfActivity.class);
                    intent.putExtra("pdfName",archivo.getNombreArchivo()+".pdf");
                    contexto.startActivity(intent);}
            });
            holder.buttonEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String apiKeyArchivo = archivo.getApiKey();
                    databaseReference.child("Archivos").child(apiKeyArchivo).removeValue();
                } });
        }


    }

    public void publicarImagen (final String photoName, final ListaArchivosAdapter.ArchivoViewHolder holder){
        storageReference.child("Images").child(photoName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(contexto)
                        .load(uri)
                        .into(holder.fotoArchivo); }
        }); }


    @Override
    public int getItemCount() {
        return listaArchivos.length;
    }
}
