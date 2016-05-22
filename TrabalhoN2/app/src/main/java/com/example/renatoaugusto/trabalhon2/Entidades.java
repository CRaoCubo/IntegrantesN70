package com.example.renatoaugusto.trabalhon2;
import java.io.Serializable;

public class Entidades implements Serializable {
    private long id;
    private String nome;
    private String data;
    private String local;
    private String participantes;
    private String descricao;
    private String tipo;
    private String adicionarTipo;


    public String toString() {

        return nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getParticipantes() {
        return participantes;
    }

    public void setParticipantes(String participantes) {
        this.participantes = participantes;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAdicionarTipo() {
        return adicionarTipo;
    }

    public void setAdicionarTipo(String adicionarTipo) {
        this.adicionarTipo = adicionarTipo;
    }
}
