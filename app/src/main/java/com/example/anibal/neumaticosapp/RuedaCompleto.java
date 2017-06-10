package com.example.anibal.neumaticosapp;

/**
 * Created by Anibal on 05/06/2017.
 */

public class RuedaCompleto {
    private String marca;
    private String modelo;
    private String medida;
    private String stock;
    private String referencia;
    private String idc;
    private String idv;
    private String tipo;



    public RuedaCompleto(String marca, String modelo, String medida, String stock, String referencia,String idc, String idv, String tipo) {
        this.marca = marca;
        this.modelo = modelo;
        this.medida = medida;
        this.stock = stock;
        this.referencia = referencia;
        this.idc=idc;
        this.idv=idv;
        this.tipo=tipo;

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


    public String getIdc() {
        return idc;
    }

    public void setIdc(String idc) {
        this.idc = idc;
    }

    public String getIdv() {
        return idv;
    }

    public void setIdv(String idv) {
        this.idv = idv;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
