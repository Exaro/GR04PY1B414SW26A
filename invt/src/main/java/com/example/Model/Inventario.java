package com.example.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Inventario {

    private Date fechaActualizacion;
    private String descripcion;
    
    public List<MovimientoProducto> movimientos;
    public List<Reporte> reportesGenerados;
    public List<Historial> historialTransacciones;
    private ProductoControl controlProductos; 

    public Inventario(String descripcion, ProductoControl controlProductos) {
        this.fechaActualizacion = new Date();
        this.descripcion = descripcion;
        this.controlProductos = controlProductos;
        this.movimientos = new ArrayList<>();
        this.reportesGenerados = new ArrayList<>();
        this.historialTransacciones = new ArrayList<>();
    }

    public void registrarEntrada(String id, int cantidad) {
        Producto p = controlProductos.buscarProducto(id);
        if (p != null && cantidad > 0) {
            MovimientoProducto mov = new MovimientoProducto("MOV-IN-" + (movimientos.size() + 1));
            
            // Constructor limpio sin precio ni marca quemados
            DetalleMovimiento dm = new DetalleMovimiento("DET-" + (mov.detalle.size() + 1), "ENTRADA", cantidad, p);
            mov.agregarDetalle(dm);
            
            mov.registrarMovimientoP();
            movimientos.add(mov);
            
            this.fechaActualizacion = new Date();
        } else {
            System.out.println("[ERROR] No se pudo registrar la entrada. Producto no encontrado.");
        }
    }

    public void registrarSalida(String id, int cantidad) {
        Producto p = controlProductos.buscarProducto(id);
        if (p != null && cantidad > 0) {
            if (p.getStock() >= cantidad) {
                MovimientoProducto mov = new MovimientoProducto("MOV-OUT-" + (movimientos.size() + 1));
                DetalleMovimiento dm = new DetalleMovimiento("DET-" + (mov.detalle.size() + 1), "SALIDA", cantidad, p);
                mov.agregarDetalle(dm);
                
                mov.registrarMovimientoP();
                movimientos.add(mov);
                
                this.fechaActualizacion = new Date();
            } else {
                System.out.println("[ERROR] Stock insuficiente para '" + p.getNombre() + "'. Disponibles: " + p.getStock());
            }
        } else {
            System.out.println("[ERROR] No se pudo registrar la salida.");
        }
    }

    public List<MovimientoProducto> verMovimientos() {
        System.out.println("\n==================================================");
        System.out.println("          SISTEMA DE AUDITORÍA: BODEGA            ");
        System.out.println("==================================================");
        
        if (this.movimientos.isEmpty()) {
            System.out.println("[INFO] No se han registrado movimientos en esta sesión.");
        } else {
            System.out.println("Mostrando un total de (" + movimientos.size() + ") transacciones:\n");
            
            for (MovimientoProducto mp : movimientos) {
                System.out.println("» Transacción: " + mp.getIdMovimiento() + " | Fecha: " + mp.getFecha());
                
                // Recorremos los detalles de este movimiento específico
                for (DetalleMovimiento dm : mp.detalle) {
                    String accion = dm.getTipo().equalsIgnoreCase("ENTRADA") ? "[ENTRADA]" : "[SALIDA]";
                    System.out.println("  • " + accion + " " + dm.getProducto().getNombre() + 
                                       " (" + dm.getProducto().getMarca() + ") x" + dm.getCantidadProductos() + " uds.");
                }
                System.out.println("--------------------------------------------------");
            }
        }
        System.out.println("==================================================\n");
        
        // Seguimos retornando la lista por si el controlador la necesita para una tabla gráfica
        return this.movimientos;
    }
}