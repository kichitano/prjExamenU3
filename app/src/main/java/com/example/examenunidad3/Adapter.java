package com.example.examenunidad3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.TemasViewHolder>{

    public static String OPCION_ELEGIDA = "";
    List<modeloHecho> temas;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
    private Context mContext;

    public Adapter(List<modeloHecho> temas) {
        this.temas = temas;
    }

    @NonNull
    @Override
    public TemasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tema_fila, parent,false);
        mContext = parent.getContext();
        TemasViewHolder temasViewHolder = new TemasViewHolder(v);
        return  temasViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TemasViewHolder holder, int position) {
        final modeloHecho tema = temas.get(position);
        holder.tituloTema.setText(tema.getTitulo());
        holder.descripcionTema.setText(tema.getDescripcion());
        //GET USER PHOTO
        StorageReference picReference = storageReferenceProfilePic.child("temas/"+ tema.getId() + ".jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        picReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                holder.fotoTema.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        holder.tarjetaTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AgregarActivity.class);
                intent.putExtra("idTema",tema.getId());
                OPCION_ELEGIDA = "editar";
                intent.putExtra(OPCION_ELEGIDA, "editar");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return temas.size();
    }

    public static class TemasViewHolder extends RecyclerView.ViewHolder{
         ImageView fotoTema;
         TextView tituloTema;
         TextView descripcionTema;
         CardView tarjetaTema;

        public TemasViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoTema = itemView.findViewById(R.id.tema_imagen);
            tituloTema = itemView.findViewById(R.id.tema_titulo);
            descripcionTema = itemView.findViewById(R.id.tema_descripcion);
            tarjetaTema = itemView.findViewById(R.id.tajetaTema);
        }
    }
}
