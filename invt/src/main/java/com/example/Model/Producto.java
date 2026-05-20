package com.example.Model;

public class Producto {

    private String idProducto;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private boolean estado;
    private String talla;
    private String color;
    private String marca;

    public CategoriaProducto categoria;
    public ProductoControl control;

    public Producto(String idProducto, String nombre, String descripcionProducto, String talla, String color,
                    double precio, int stock, boolean estado, String marca) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcionProducto;
        this.talla = talla;
        this.color = color;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
        this.marca = marca;
    }
    public String getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcionProducto) {
        this.descripcion = descripcionProducto;
    }
    public String getTalla() {
        return talla;
    }
    public void setTalla(String talla) {
        this.talla = talla;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public boolean isEstado() {
        return estado;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getMarca() {
        return marca;
    }

    public void actualizarPrecio(double nuevoPrecio){
        if(nuevoPrecio >= 0){
            this.precio = nuevoPrecio;
            System.out.println("Precio actualizado a: " + nuevoPrecio + " con exito");
        }else{
            System.out.println("Error: El nuevo precio debe ser mayor a cero.");
        }
    }

    public void actualizarStock(int nuevoStock){
        if (nuevoStock >= 0){
            this.stock = nuevoStock;
            System.out.println("Stock actualizado a: " + nuevoStock + " con exito");
        }else{
            System.out.println("Error: insuficiente stock para realizar esta operación.");
        }
    }
    public void verificarDisponibilidad(){
        if(this.stock > 0){
            System.out.println("El producto " + this.nombre + " está disponible.");
        }else{
            System.out.println("El producto " + this.nombre + " no está disponible.");
        }
    }
    public CategoriaProducto obtenerCategoria() {
        return this.categoria;
    }

    public void alertaStock() {
        if (this.stock <= 5) {
            System.out.println("Alerta: El producto " + this.nombre + " tiene un stock bajo (" + this.stock + " unidades).");
        }
    }

    public ProductoControl accederProducto(String id) {
        if (this.idProducto.equals(id)) {
            return this.control;
        }
        System.out.println("El ID no coincide con este producto.");
        return null;
    }

}
