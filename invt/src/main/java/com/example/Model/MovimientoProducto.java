package com.example.Model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class MovimientoProducto {
    private String idMovimiento;
    private Date fecha;
    public List<DetalleMovimiento> detalle = new ArrayList<>();
    
    public MovimientoProducto(String idMovimiento) {
        this.idMovimiento = idMovimiento;
        this.fecha = new Date(); 
    }

    public String getIdMovimiento() { return idMovimiento; }
    public Date getFecha() { return fecha; }


    public void agregarDetalle(DetalleMovimiento dm) {
        if (dm != null) {
            this.detalle.add(dm);
        }
    }

    public void registrarMovimientoP() {
        System.out.println("\n========== PROCESANDO TRANSACCIÓN DE BODEGA ==========");
        System.out.println("ID Transacción: " + idMovimiento + " | Fecha: " + fecha);
        System.out.println("--------------------------------------------------");
        double totalMonetario = 0;
        for (DetalleMovimiento dm : detalle) {
            dm.calcularTotal(); 
            Producto prod = dm.getProducto();
            int cantidad = dm.getCantidadProductos();
            
            if (dm.getTipo().equalsIgnoreCase("ENTRADA")) {
                prod.setStock(prod.getStock() + cantidad);
            } else if (dm.getTipo().equalsIgnoreCase("SALIDA")) {
                if (prod.getStock() >= cantidad) {
                    prod.setStock(prod.getStock() - cantidad);
                } else {
                    System.out.println("[ERROR] Stock insuficiente para descargar '" + prod.getNombre() + "'");
                }
            }
            
            totalMonetario += (cantidad * prod.getPrecio());
        }

        System.out.println("--------------------------------------------------");
        System.out.println("Monto total procesado en la transacción: $" + totalMonetario);
        System.out.println("==================================================\n");
    }

}