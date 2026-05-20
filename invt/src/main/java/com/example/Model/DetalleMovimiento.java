package com.example.Model;

public class DetalleMovimiento {
    private String idDetalle;
    private String tipo; 
    private int cantidadProductos;
    private double precio;
    private String marca;
    private Producto producto;

    public DetalleMovimiento(String idDetalle, String tipo, int cantidadProductos, double precio, String marca, Producto producto) {
        this.idDetalle = idDetalle;
        this.tipo = tipo;
        this.cantidadProductos = cantidadProductos;
        this.precio = precio;
        this.marca = marca;
        this.producto = producto;
    }

    public void calcularTotal() {
        double totalLineal = this.cantidadProductos * this.precio;
        String accion = (tipo.equals("ENTRADA")) ? "[ENTRADA]" : "[SALIDA]";
        System.out.println(accion + " " + producto.getNombre() + " x" + cantidadProductos + " uds. Subtotal: $" + totalLineal);
    }

    public String getIdDetalle() {
        return idDetalle;
    }
    public void setIdDetalle(String idDetalle) {
        this.idDetalle = idDetalle;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getCantidadProductos() {
        return cantidadProductos;
    }
    public void setCantidadProductos(int cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

}
