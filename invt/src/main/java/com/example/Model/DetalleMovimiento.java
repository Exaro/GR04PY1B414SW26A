package com.example.Model;

public class DetalleMovimiento {
    private String idDetalle;
    private String tipo; // ENTRADA o SALIDA
    private int cantidadProductos;
    private String marca; 
    private double precio;

    public Producto producto; 

    public DetalleMovimiento(String idDetalle, String tipo, int cantidadProductos, String marca, double precio, Producto producto) {
        this.idDetalle = idDetalle;
        this.tipo = tipo;
        this.cantidadProductos = cantidadProductos;
        this.marca = marca;
        this.precio = precio;
        this.producto = producto;
    }

   public double calcularTotal() {
    return this.cantidadProductos * producto.getPrecio();
}
    public String getIdDetalle() { return idDetalle; }
    public void setIdDetalle(String idDetalle) { this.idDetalle = idDetalle; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getCantidadProductos() { return cantidadProductos; }
    public void setCantidadProductos(int cantidadProductos) { this.cantidadProductos = cantidadProductos; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }  

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}