package com.mrz.gym;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.mrz.gym.data.GymDatabase;
import com.mrz.gym.data.entity.Usuario;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText usernameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private Button registerButton;
    private ExecutorService executorService;
    private GymDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = GymDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (!validateRegistration(username, email, password)) {
                Toast.makeText(this, "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show();
                return;
            }

            executorService.execute(() -> {
                // Verificar si el usuario ya existe
                Usuario existingUser = database.usuarioDao().findByUsername(username);
                Usuario existingEmail = database.usuarioDao().findByEmail(email);

                runOnUiThread(() -> {
                    if (existingUser != null) {
                        Toast.makeText(this, "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                    } else if (existingEmail != null) {
                        Toast.makeText(this, "El email ya está registrado", Toast.LENGTH_SHORT).show();
                    } else {
                        // Crear y guardar el nuevo usuario
                        Usuario newUser = new Usuario(username, email, password);
                        executorService.execute(() -> {
                            long userId = database.usuarioDao().insert(newUser);
                            runOnUiThread(() -> {
                                if (userId > 0) {
                                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, LoginActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    }
                });
            });
        });
    }

    private boolean validateRegistration(String username, String email, String password) {
        return username.length() >= 3 && 
               email.contains("@") && 
               password.length() >= 6;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
} 