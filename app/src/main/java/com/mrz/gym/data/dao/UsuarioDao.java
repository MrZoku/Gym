package com.mrz.gym.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.mrz.gym.data.entity.Usuario;

@Dao
public interface UsuarioDao {
    @Insert
    long insert(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE username = :username AND password = :password LIMIT 1")
    Usuario login(String username, String password);

    @Query("SELECT * FROM usuarios WHERE username = :username LIMIT 1")
    Usuario findByUsername(String username);

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario findByEmail(String email);
} 