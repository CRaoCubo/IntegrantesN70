package com.example.renatoaugusto.trabalhon2;
import java.io.Serializable;

public class EntidadesCanceladas implements Serializable {

    private long id;
    private String nome;


    public String toString() {

        return nome + " - Cancelado";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
