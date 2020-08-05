package com.example.proyectoindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VisorPdfActivity extends AppCompatActivity {

    StorageReference storageReference;
    String url;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_pdf);

        progressDialog = new ProgressDialog(VisorPdfActivity.this);
        progressDialog.setTitle("Obteniendo URL de archivo...");
        progressDialog.show();
        Intent intent = getIntent();
        String pdfName = intent.getStringExtra("pdfName");
        //PDFView pdfView = findViewById(R.id.pdfView);
        final WebView webView = findViewById(R.id.webView);
        //final ProgressBar progressBar = findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.VISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference();
        //String url = storageReference.child("PDF").child(pdfName).getDownloadUrl().toString();
        if (url == null) {

        storageReference.child("PDF").child(pdfName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                url = uri.toString();
                progressDialog.dismiss();
                //String urlFinal = "http://drive.google.com/viewerng/viewer?embedded=true&url=+"+url;
                //publicarPdf(pdfName,pdfView);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setBuiltInZoomControls(true);
                /*
                webView.setWebChromeClient(new WebChromeClient(){

                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);

                        getSupportActionBar().setTitle("Cargando...");
                        if(newProgress==100){

                            progressBar.setVisibility(View.GONE);
                            getSupportActionBar().setTitle("Aplicacion");
                        }
                    }
                });

                 */
                webView.loadUrl(url);
            }
        });
    }

    }


    /*
    public void publicarPdf (final String pdfName, final PDFView pdfView){
        storageReference.child("PDF").child(pdfName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                pdfView.fromUri(uri);
            }

        }); }

*/
}