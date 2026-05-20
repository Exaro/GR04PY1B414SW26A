package com.example.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Inventario {

    private Date fechaActualizacion; // Usamos Date para mayor precisión en bodega
    private String descripcion;
    
    public Encargado encargado; 

    // Listas para almacenar todo lo que ocurre en la tienda (Multiplicidades * del UML)
    public List<MovimientoProducto> movimientos;
    public List<Historial> historiales;
    public List<Reporte> reportesGenerados;
    public Producto producto;

    // Constructor
    public Inventario(String descripcion, Encargado encargado) {
        this.producto = null;
        this.fechaActualizacion = new Date(); // Inicia con la fecha actual
        this.descripcion = descripcion;
        this.encargado = encargado;
        
        this.movimientos = new ArrayList<>();
        this.historiales = new ArrayList<>();
        this.reportesGenerados = new ArrayList<>();
    }

    public Producto buscarProducto(String id) {
        return producto.accederProducto(id).buscarProducto(id);
    }

    public void registrarEntrada(String id, int cantidad) {
        int contadorHistorial = historiales.size() + 1;
        Producto p = buscarProducto(id);
        if (p != null && cantidad > 0) {
            int stockViejo = p.getStock();
            p.actualizarStock(stockViejo + cantidad);
            this.fechaActualizacion = new Date(); // Actualizamos la última fecha de movimiento

            // CREACIÓN AUTOMÁTICA DEL HISTORIAL
            String idHist = "HIST-" + (contadorHistorial++);
            String desc = "Ingreso manual de stock para: " + p.getNombre();
            Historial h = new Historial("H-" + id, new Date(), "Ingreso de producto", stockViejo, p.getStock());
            h.registrar();
            
            historiales.add(h);
            h.registrar(); // Lo imprime/guarda en consola
        } else {
            System.out.println("[ERROR] No se pudo registrar la entrada. Producto no encontrado o cantidad inválida.");
        }
    }

    /**
     * Registra una salida simple en bodega, valida que no quede stock negativo
     * y genera automáticamente un registro en el Historial.
     */
    public void registrarSalida(String id, int cantidad) {
        Producto p = buscarProducto(id);
        int contadorHistorial = historiales.size() + 1; 
        if (p != null && cantidad > 0) {
            int stockViejo = p.getStock();
            
            if (stockViejo >= cantidad) {
                p.actualizarStock(stockViejo - cantidad);
                this.fechaActualizacion = new Date();

                // CREACIÓN AUTOMÁTICA DEL HISTORIAL
                String idHist = "HIST-" + (contadorHistorial++);
                String desc = "Salida/Descarga manual de stock para: " + p.getNombre();
                Historial h = new Historial("H-" + id, new Date(), "Salida de producto", stockViejo, p.getStock());
                h.registrar();

                historiales.add(h);
                h.registrar();
            } else {
                System.out.println("[ERROR] Stock insuficiente para '" + p.getNombre() + "'. Disponibles: " + stockViejo);
            }
        } else {
            System.out.println("[ERROR] No se pudo registrar la salida.");
        }
    }

    /**
     * Devuelve la lista de todos los movimientos (entradas/salidas) registrados.
     */
    public List<MovimientoProducto> verMovimientos() {
        return this.movimientos;
    }

    public Reporte generarReporte() {
        int contadorReportes = reportesGenerados.size() + 1;
        String idRep = "REP-BAJO-" + (contadorReportes++);
        Date ahora = new Date();
        
        // 1. Instanciamos el reporte pasándole solo sus datos básicos
        Reporte reporteBajo = new Reporte(idRep, ahora, ahora, "Informe Especial: Alerta de Stock Crítico");
        
        // 2. Llamamos al método que ahora es vacío () ya que no requiere productos
        reporteBajo.generarReporte(); 
        
        // 3. Lo guardamos en el histórico de reportes del inventario
        reportesGenerados.add(reporteBajo);
        
        return reporteBajo;
    }

  
}