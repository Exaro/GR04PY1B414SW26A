package com.example.Model;

public class DetalleMovimiento {
    private String idDetalle;
    private String tipo; // "ENTRADA" o "SALIDA"
    private int cantidadProductos;
    private Producto producto; // Asociación directa al objeto original

    // Constructor limpio y óptimo
    public DetalleMovimiento(String idDetalle, String tipo, int cantidadProductos, Producto producto) {
        this.idDetalle = idDetalle;
        this.tipo = tipo;
        this.cantidadProductos = cantidadProductos;
        this.producto = producto;
    }

    // Calcula el total consultando el precio real del producto en ese instante
    public void calcularTotal() {
        double totalLineal = this.cantidadProductos * producto.getPrecio();
        String accion = (tipo.equalsIgnoreCase("ENTRADA")) ? "[ENTRADA]" : "[SALIDA]";
        
        System.out.println(accion + " " + producto.getNombre() + " (" + producto.getMarca() + ") x" + cantidadProductos + " uds. Subtotal: $" + totalLineal);
    }

    // Getters y Setters necesarios
    public String getIdDetalle() { return idDetalle; }
    public void setIdDetalle(String idDetalle) { this.idDetalle = idDetalle; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getCantidadProductos() { return cantidadProductos; }
    public void setCantidadProductos(int cantidadProductos) { this.cantidadProductos = cantidadProductos; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}