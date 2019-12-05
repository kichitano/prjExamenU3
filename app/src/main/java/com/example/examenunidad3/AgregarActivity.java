package com.example.examenunidad3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class AgregarActivity extends AppCompatActivity {

    private modeloHecho modelotema = new modeloHecho();
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReferenceProfilePic;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgressDialog;

    private int GALLERY = 1, CAMERA = 2;
    String message;
    private TextView tituloTema;
    private ImageButton fotoTema;
    private TextView descripcionTema;
    private TextView primerHecho;
    private TextView segundoHecho;
    private Button agregarHecho;
    private Button agregarnuevoTema;
    private String m_Text;
    private Uri uriPic;
    private String message2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_Text = "";
        setContentView(R.layout.activity_agregar);
        tituloTema = findViewById(R.id.tituloTema);
        fotoTema = findViewById(R.id.fotoTema);
        descripcionTema = findViewById(R.id.descripcionTema);
        primerHecho = findViewById(R.id.primerHecho);
        segundoHecho = findViewById(R.id.segundoHecho);
        agregarHecho = findViewById(R.id.btnAgregarHechoRelevante);
        agregarnuevoTema = findViewById(R.id.btnAgregarTema);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReferenceProfilePic = firebaseStorage.getReference();
        mProgressDialog = new ProgressDialog(AgregarActivity.this);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.OPCION_ELEGIDA);
        message2 = intent.getStringExtra(Adapter.OPCION_ELEGIDA);

        if (message != null && message.equals("agregar")){
            agregarnuevoTema.setText("AGREGAR NUEVO TEMA");
            agregarHecho.setVisibility(View.GONE);
            setFocusableClickable(true);
            tituloTema.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    promptForResult("Titulo del tema.", "Inserte un titulo.", new DialogInputInterface(){
                        EditText input;
                        public View onBuildDialog() {
                            input = new EditText(getApplicationContext());
                            View outView = input;
                            return outView;
                        }
                        public void onCancel() {
                            m_Text = "";
                        }
                        public void onResult(View v) {
                            m_Text = input.getText().toString();
                            if(!(m_Text.equals(""))){
                                tituloTema.setText(m_Text);
                                tituloTema.setTypeface(null, Typeface.BOLD_ITALIC);
                            }
                        }
                    });
                }
            });
            descripcionTema.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    promptForResult("Descripcion del tema.", "Inserte una descripcion.", new DialogInputInterface(){
                        EditText input;
                        public View onBuildDialog() {
                            input = new EditText(getApplicationContext());
                            View outView = input;
                            return outView;
                        }
                        public void onCancel() {
                            m_Text = "";
                        }
                        public void onResult(View v) {
                            m_Text = input.getText().toString();
                            if(!(m_Text.equals(""))){
                                descripcionTema.setText(m_Text);
                                descripcionTema.setTypeface(null, Typeface.BOLD_ITALIC);
                            }
                        }
                    });
                }
            });
            primerHecho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    promptForResult("Primer hecho.", "Inserte el primer hecho.", new DialogInputInterface(){
                        EditText input;
                        public View onBuildDialog() {
                            input = new EditText(getApplicationContext());
                            View outView = input;
                            return outView;
                        }
                        public void onCancel() {
                            m_Text = "";
                        }
                        public void onResult(View v) {
                            m_Text = input.getText().toString();
                            if(!(m_Text.equals(""))){
                                primerHecho.setText(m_Text);
                                primerHecho.setTypeface(null, Typeface.BOLD_ITALIC);
                            }
                        }
                    });
                }
            });
            segundoHecho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    promptForResult("Segundo hecho.", "Inserte el segundo hecho.", new DialogInputInterface(){
                        EditText input;
                        public View onBuildDialog() {
                            input = new EditText(getApplicationContext());
                            View outView = input;
                            return outView;
                        }
                        public void onCancel() {
                            m_Text = "";
                        }
                        public void onResult(View v) {
                            m_Text = input.getText().toString();
                            if(!(m_Text.equals(""))){
                                segundoHecho.setText(m_Text);
                                segundoHecho.setTypeface(null, Typeface.BOLD_ITALIC);
                            }
                        }
                    });
                }
            });
            fotoTema.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPictureDialog();
                }
            });
            agregarnuevoTema.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agregarTema();
                }
            });
        }else{
            agregarnuevoTema.setVisibility(View.GONE);
            setFocusableClickable(false);
            String idTema = intent.getStringExtra("idTema");
            modelotema = new modeloHecho();
            modelotema.setId(idTema);
            mDatabase = FirebaseDatabase.getInstance().getReference("hechos").child(modelotema.getId());
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        modelotema.setTitulo(dataSnapshot.child("titulo").getValue().toString());
                        modelotema.setDescripcion(dataSnapshot.child("descripcion").getValue().toString());
                        modelotema.setPrimerhecho(dataSnapshot.child("primerhecho").getValue().toString());
                        modelotema.setSegundohecho(dataSnapshot.child("segundohecho").getValue().toString());
                        //GET USER PHOTO
                        StorageReference picReference = storageReferenceProfilePic.child("temas/" + modelotema.getId() + ".jpg");
                        final long ONE_MEGABYTE = 1024 * 1024;
                        picReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inMutable = true;
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                                tituloTema.setText(modelotema.getTitulo());
                                descripcionTema.setText(modelotema.getDescripcion());
                                primerHecho.setText(modelotema.getPrimerhecho());
                                segundoHecho.setText(modelotema.getSegundohecho());
                                fotoTema.setImageBitmap(bmp);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {  }
            });

            agregarHecho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    promptForResult("Inserte un nuevo hecho.", "Reemplaze el hecho mas antiguo por uno nuevo.", new DialogInputInterface(){
                        EditText input;
                        public View onBuildDialog() {
                            input = new EditText(getApplicationContext());
                            View outView = input;
                            return outView;
                        }
                        public void onCancel() {
                            m_Text = "";
                        }
                        public void onResult(View v) {
                            m_Text = input.getText().toString();
                            if(!(m_Text.equals(""))){
                                modelotema.setPrimerhecho(modelotema.getSegundohecho());
                                modelotema.setSegundohecho(m_Text);
                                mDatabase.child("primerhecho").setValue(modelotema.getPrimerhecho());
                                mDatabase.child("segundohecho").setValue(modelotema.getSegundohecho());
                            }
                        }
                    });
                }
            });
        }
    }

    private void agregarTema() {
        final ProgressDialog progDailog = new ProgressDialog(AgregarActivity.this);
        final Map<String,Object> temaMap = new HashMap<>();

        modelotema.setTitulo(tituloTema.getText().toString());
        modelotema.setDescripcion(descripcionTema.getText().toString());
        modelotema.setPrimerhecho(primerHecho.getText().toString());
        modelotema.setSegundohecho(segundoHecho.getText().toString());

        final String temaID = mDatabase.push().getKey();

        temaMap.put("id",temaID);
        temaMap.put("titulo",modelotema.getTitulo());
        temaMap.put("descripcion",modelotema.getDescripcion());
        temaMap.put("primerhecho",modelotema.getPrimerhecho());
        temaMap.put("segundohecho",modelotema.getSegundohecho());


        if(uriPic != null){
            mProgressDialog.setMessage("Subiendo imagen...");
            mProgressDialog.show();
            StorageReference imageRef = storageReferenceProfilePic.child("temas/" + temaID + ".jpg");

            imageRef.putFile(uriPic)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressDialog.dismiss();
                            mDatabase.child("hechos").child(temaID).setValue(temaMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progDailog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Tema agregado con éxito.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        progDailog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Error al agregar tema.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successful
                            //hiding the progress dialog
                            mProgressDialog.dismiss();
                            //and displaying error message
                            Toast.makeText(AgregarActivity.this, exception.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                          //displaying percentage in progress dialog
                            mProgressDialog.setMessage("Subiendo " + ((int) progress) + "%...");
                        }
                    });
        }
    }

    public void setFocusableClickable(Boolean conf){
        tituloTema.setClickable(conf);
        tituloTema.setFocusable(conf);
        fotoTema.setClickable(conf);
        fotoTema.setFocusable(conf);
        descripcionTema.setClickable(conf);
        descripcionTema.setFocusable(conf);
        primerHecho.setClickable(conf);
        primerHecho.setFocusable(conf);
        segundoHecho.setClickable(conf);
        segundoHecho.setFocusable(conf);
    }

    private interface DialogInputInterface {
        View onBuildDialog();
        void onCancel();
        void onResult(View v);
    }

    private void promptForResult(String dlgTitle, String dlgMessage, final DialogInputInterface dlg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(dlgTitle);
        alert.setMessage(dlgMessage);
        final View v = dlg.onBuildDialog();
        v.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        if (v != null) { alert.setView(v);}
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dlg.onResult(v);
                dialog.dismiss();
                return;
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dlg.onCancel();
                dialog.dismiss();
                return;
            }
        });
        alert.show();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(AgregarActivity.this);
        pictureDialog.setTitle("Seleccione una acción.");
        String[] pictureDialogItems = {
                "Elija una foto de la galería.",
                "Tome una fotografía." };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uriPic = null;
        if (resultCode == new AgregarActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(AgregarActivity.this.getContentResolver(), contentURI);
                    uriPic = getImageUri(AgregarActivity.this, bitmap);
                    fotoTema.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AgregarActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            uriPic = getImageUri(AgregarActivity.this, thumbnail);
            fotoTema.setImageBitmap(thumbnail);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "fotoTema", null);
        return Uri.parse(path);
    }
}
