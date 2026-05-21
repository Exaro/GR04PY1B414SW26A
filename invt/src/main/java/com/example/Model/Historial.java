package com.example.Model;

import java.util.Date;
// Se eliminó el Scanner que no se usaba aquí

public class Historial {
    private String idHistorial;
    private Date fecha;
    private String descripcion;
    private int stockViejo;
    private int stockNuevo;

    public Historial(String idHistorial, Date fecha, String descripcion, int stockViejo, int stockNuevo) {
        this.idHistorial = idHistorial;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.stockViejo = stockViejo;
        this.stockNuevo = stockNuevo;
    }

    public void registrar() {
        System.out.println("ID Historial: " + idHistorial); 
        System.out.println("Fecha: " + fecha);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Stock anterior: " + stockViejo);
        System.out.println("Stock nuevo: " + stockNuevo);
        System.out.println("-----------------------------------");
    }

    public String getIdHistorial() { return idHistorial; }
    public Date getFecha() { return fecha; }                        
    public String getDescripcion() { return descripcion; }
    public int getStockViejo() { return stockViejo; }
    public int getStockNuevo() { return stockNuevo; }
}