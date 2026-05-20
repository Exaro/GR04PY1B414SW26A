package com.example.Model;

import java.util.Date;
import java.util.Scanner;

public class Reporte {
    private String idReporte;
    private Date fechaIni;
    private Date fechaFin;
    private String tipoReporte;
    private String descripcion;

    public Reporte(String idReporte, Date fechaIni, Date fechaFin, String tipoReporte) {
        this.idReporte = idReporte;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.tipoReporte = tipoReporte;
        this.descripcion = ""; 
    }

    public void generarReporte() {
        System.out.println("\n=================================================================");
        System.out.println("                 SISTEMA DE REPORTES - TIENDA DE ROPA            ");
        System.out.println("=================================================================");
        System.out.println("CÓDIGO REPORTE : " + idReporte);
        System.out.println("ASUNTO         : " + tipoReporte);
        System.out.println("PERIODO ALUDIDO: Desde " + fechaIni + " hasta " + fechaFin);
        System.out.println("-----------------------------------------------------------------");
        System.out.println("[SISTEMA] Datos de existencias procesados correctamente.");
        System.out.println("-----------------------------------------------------------------");
        
        // CORRECCIÓN: No usamos try-with-resources aquí para no romper el System.in global
        Scanner teclado = new Scanner(System.in);
        System.out.println("\n[ENCARGADO] Escriba a continuación las observaciones o novedades de la tienda:");
        System.out.println("(Presione ENTRAR para finalizar su párrafo)\n");
        System.out.print("> ");
        
        this.descripcion = teclado.nextLine();
        
        System.out.println("\n[SISTEMA] ¡Párrafo del encargado acoplado con éxito al reporte escrito!");
        System.out.println("=================================================================\n");
    }

    public void exportarReporte() {
        System.out.println("\n=================================================================");
        System.out.println("               VISTA FINAL DEL DOCUMENTO EXPORTADO               ");
        System.out.println("=================================================================");
        System.out.println("REPORTE NRO: " + idReporte + " | ASUNTO: " + tipoReporte);
        System.out.println("DESCRIPCIÓN DEL ENCARGADO:\n");
        System.out.println(this.descripcion.trim().isEmpty() ? "  (Vacío)" : "  \"" + this.descripcion + "\"");
        System.out.println("=================================================================\n");
    }
}