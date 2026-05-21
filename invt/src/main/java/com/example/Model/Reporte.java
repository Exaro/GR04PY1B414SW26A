package com.example.Model;

import java.util.Date;

public class Reporte {
    private String idReporte;
    private Date fechaIni;
    private Date fechaFin;
    private String tipoReporte;
    private String descripcion;

    public Reporte(String idReporte, Date fechaIni, Date fechaFin, String tipoReporte) {
        this.idReporte = idReporte;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.tipoReporte = tipoReporte;
        this.descripcion = ""; 
    }

    public void generarReporte() {

        System.out.println("\n=================================================");
        System.out.println("         SISTEMA DE REPORTES - INVENTARIO");
        System.out.println("=================================================");
        System.out.println("ID REPORTE   : " + idReporte);
        System.out.println("TIPO         : " + tipoReporte);
        System.out.println("FECHA INICIO : " + fechaIni);
        System.out.println("FECHA FIN    : " + fechaFin);
        System.out.println("DESCRIPCIÓN  : " + descripcion);
        System.out.println("=================================================\n");
    }

    public String getIdReporte() { return idReporte; }
    public Date getFechaIni() { return fechaIni; }      
    public Date getFechaFin() { return fechaFin; }
    public String getTipoReporte() { return tipoReporte; }
    public String getDescripcion() { return descripcion; }  

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}