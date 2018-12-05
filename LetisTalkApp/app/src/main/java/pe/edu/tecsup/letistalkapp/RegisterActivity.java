package pe.edu.tecsup.letistalkapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText nombreInput;
    private EditText emailInput;
    private EditText direccionInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        nombreInput = findViewById(R.id.nombre_input);
        emailInput = findViewById(R.id.email_input);
        direccionInput = findViewById(R.id.direccion_input);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_send:
                sendPost();
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendPost() {
        Log.d(TAG, " sendPost()");

        String nombre = nombreInput.getText().toString();
        String email = emailInput.getText().toString();
        String direccion = direccionInput.getText().toString();

        if(nombre.isEmpty() || email.isEmpty() || direccion.isEmpty()){
            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        // Get currentuser from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "currentUser: " + currentUser);

        // Registrar a Firebase Database
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
        DatabaseReference postRef = postsRef.push();

        Post post = new Post();
        post.setId(postRef.getKey());
        post.setNombre(nombre);
        post.setEmail(email);
        post.setDireccion(direccion);
        post.setUserid(currentUser.getUid());

        postRef.setValue(post)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onSuccess");
                            Toast.makeText(RegisterActivity.this, "Registro guardado", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Log.e(TAG, "onFailure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void takePicture(View view){
    }

}


