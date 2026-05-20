package com.example.Model;

import java.util.ArrayList;
import java.util.List;

public class CategoriaProducto {
    private String tipo;
    private String descripcion;
    private String idCategoria;

    public CategoriaProducto(String idCategoria, String tipo, String descripcion) {
        this.idCategoria = idCategoria;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public List<Producto> obtenerProducto(ProductoControl control) {
        List<Producto> productosDeEstaCategoria = new ArrayList<>();
        
        for (Producto p : control.listarProductos()) {
            if (p.obtenerCategoria() != null && p.obtenerCategoria().getIdCategoria().equals(this.idCategoria)) {
                productosDeEstaCategoria.add(p);
            }
        }
        return productosDeEstaCategoria;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }
}
