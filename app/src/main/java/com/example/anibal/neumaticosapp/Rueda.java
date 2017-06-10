package com.example.anibal.neumaticosapp;

/**
 * Created by Anibal on 31/05/2017.
 */

public class Rueda {
    private String marca;
    private String modelo;
    private String medida;
    private String stock;
    private String referencia;

    public Rueda(String marca, String modelo, String medida, String stock, String referencia) {
        this.marca = marca;
        this.modelo = modelo;
        this.medida = medida;
        this.stock = stock;
        this.referencia = referencia;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}