package com.example.Model;

import java.util.ArrayList;
import java.util.List;

public class ProductoControl {
    private List<Producto> productos = new ArrayList<>(); // Atributo: productos: List<productos>

    public void agregarProducto(Producto p) {
        if (p != null) {
            productos.add(p);
        }
    }

    public void eliminarProducto(String id) {
        if (id == null) {
            return;
        }
        productos.removeIf(producto -> id.equals(producto.getIdProducto()));
    }

    public Producto buscarProducto(String id) {
        if (id == null) {
            return null;
        }
        for (Producto producto : productos) {
            if (id.equals(producto.getIdProducto())) {
                return producto;
            }
        }

        System.out.println("Producto con ID " + id + " no encontrado.");
        return null;
    }

    public List<Producto> listarProductos() {
        if (productos.isEmpty()) {
            System.out.println("El inventario de productos está vacío.");
        } else {
            for (Producto producto : productos) {
                System.out.println("ID: " + producto.getIdProducto() + 
                                   ", Nombre: " + producto.getNombre() + 
                                   ", Stock: " + producto.getStock());
            }
        }
        
        return this.productos;
    }
}
