package com.example.Controller;

import com.example.Model.*;
import com.example.View.InventarioGlobalView;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.Date;
import java.util.Optional;

public class InventarioController {

    private InventarioGlobalView vista;
    private Inventario inventario; // <-- El modelo de inventario que maneja la lógica de movimientos y reportes
    private ProductoControl modeloControl; // <-- La fuente de la verdad lógica

    public InventarioController(InventarioGlobalView vista, ProductoControl modeloControl) {
        this.vista = vista;
        this.inventario = new Inventario("Inventario Principal", modeloControl);
        this.modeloControl = modeloControl;
        
        cargarProductosPredeterminados();
        refrescarTablaDesdeModelo();
        inicializarEventos();
    }

    private void cargarProductosPredeterminados() {
        // CASO DE PRUEBA 1: Insertar a través del método nativo del modelo
        modeloControl.agregarProducto(new Producto("P001", "Camiseta", "Algodón liviano", "M", "Negro", 15.0, 20, true, "Nike"));
        modeloControl.agregarProducto(new Producto("P002", "Jeans Slim Fit", "Mezclilla elástica", "32", "Azul", 59.99, 3, true, "Levi's"));
    }

    private void refrescarTablaDesdeModelo() {
        // Sincroniza la lista visual de la UI con la lista interna de ProductoControl
        vista.getProductos().setAll(modeloControl.listarProductos());
    }

    /**
     * 
     */
    private void inicializarEventos() {
        
        // ==========================================
        // CASO DE USO 1: AGREGAR PRODUCTO
        // ==========================================
        vista.getBtnAgregarProducto().setOnAction(e -> {
            abrirDialogoAgregarProducto();
        });

        // ==========================================
        // CASO DE USO 1: ELIMINAR PRODUCTO (Por ID)
        // ==========================================
        vista.getBtnEliminarProducto().setOnAction(e -> {
            Producto seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarAlerta("Atención", "Procedimiento Inválido", "Debe seleccionar un producto de la tabla.", Alert.AlertType.WARNING);
                return;
            }
            
            // CASO DE PRUEBA 3: Se ejecuta eliminarProducto(id) en ProductoControl
            modeloControl.eliminarProducto(seleccionado.getIdProducto());
inventario.historiales.add(new Historial("HIST-" + (inventario.historiales.size() + 1),new Date(),  "SE ELIMINÓ -> " + seleccionado.getNombre(), seleccionado.getStock(), 0));            // Se actualiza la interfaz
            refrescarTablaDesdeModelo();
            mostrarAlerta("Éxito", "Producto Eliminado", "Producto eliminado de la lista de ProductoControl.", Alert.AlertType.INFORMATION);
        });

        // ==========================================
        // CASO DE USO 2: MOVIMIENTOS DE STOCK
        // ==========================================
        vista.getBtnNuevaTransaccion().setOnAction(e -> {
            Producto seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarAlerta("Atención", "Producto No Seleccionado", "Seleccione una prenda en la tabla.", Alert.AlertType.WARNING);
                return;
            }

            TextInputDialog dialog = new TextInputDialog("0");
            dialog.setTitle("Registrar Movimiento");
            dialog.setHeaderText("Movimiento para: " + seleccionado.getNombre());
            dialog.setContentText("Ingrese cantidad (Positivo = Entrada, Negativo = Salida):");

            Optional<String> resultado = dialog.showAndWait();
            if (resultado.isPresent()) {
                try {
                    int cantidad = Integer.parseInt(resultado.get());
                    if (cantidad == 0) throw new InventarioException("La cantidad debe ser distinta de 0.");

                    String idProd = seleccionado.getIdProducto();

                    if (cantidad > 0) {
                        // USAMOS TU MODELO TRANSACCIONAL: Esto creará el DetalleMovimiento y lo guardará
                        inventario.registrarEntrada(idProd, cantidad);
                        mostrarAlerta("Éxito", "Entrada Registrada", "Movimiento guardado en la bitácora de Inventario.", Alert.AlertType.INFORMATION);
                    } else {
                        int salida = Math.abs(cantidad);
                        if (seleccionado.getStock() < salida) {
                            throw new StockInsuficienteException(seleccionado.getNombre(), seleccionado.getStock(), salida);
                        }
                        // USAMOS TU MODELO TRANSACCIONAL: Esto validará y guardará en la lista
                        inventario.registrarSalida(idProd, salida);
                        mostrarAlerta("Éxito", "Salida Registrada", "Movimiento guardado en la bitácora de Inventario.", Alert.AlertType.INFORMATION);
                    }

                    // Sincronizamos los cambios del modelo con la pantalla
                    refrescarTablaDesdeModelo();

                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "Formato Inválido", "Ingrese un número entero.", Alert.AlertType.ERROR);
                } catch (InventarioException ex) {
                    mostrarAlerta("Error", "Operación Cancelada", ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });

        // ==========================================
        // TU BOTÓN DE PRUEBA 
        // ==========================================
        vista.getBtnPruebaMov().setOnAction(e -> {
           Producto seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Atención","Producto no seleccionado","Seleccione un producto.", Alert.AlertType.WARNING);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("   HISTORIAL MOVIMIENTOS PRODUCTO\n");
        sb.append("========================================\n\n");
        sb.append("Producto: ").append(seleccionado.getNombre()).append("\n\n");


        for (MovimientoProducto mp : inventario.movimientos) {
            for (DetalleMovimiento dm : mp.detalle) {

            double total = dm.calcularTotal();

            sb.append("ID Detalle: ")
            .append(dm.getIdDetalle())
            .append("\n");

            sb.append("Tipo Movimiento: ")
            .append(dm.getTipo())
            .append("\n");

            sb.append("Producto: ")
            .append(dm.getProducto().getNombre())
            .append("\n");

            sb.append("Marca: ")
            .append(dm.getProducto().getMarca())
            .append("\n");

            sb.append("Cantidad: ")
            .append(dm.getCantidadProductos())
            .append("\n");

            sb.append("Precio Unitario: $")
            .append(dm.getPrecio())
            .append("\n");

            sb.append("Total Movimiento: $")
            .append(total)
            .append("\n");

            sb.append("--------------------------------------\n");
}
        }


            TextArea area = new TextArea(sb.toString());

            area.setEditable(false);
            area.setWrapText(true);

            area.setPrefWidth(600);
            area.setPrefHeight(450);

            area.setStyle(
                "-fx-font-family: 'Courier New';" +
                "-fx-font-size: 13px;"
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Historial Movimientos");

            alert.setHeaderText(
                "Movimientos del producto seleccionado"
            );

            alert.getDialogPane().setContent(area);

            alert.showAndWait();
        });

        // ==========================================
        // CASO DE USO 3: REPORTE DE TIENDA
        // ==========================================
       vista.getBtnGenerarReporteEscrito().setOnAction(e -> {

    Producto seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();

    if (seleccionado == null) {
        mostrarAlerta(
            "Atención",
            "Producto no seleccionado",
            "Seleccione un producto.",
            Alert.AlertType.WARNING
        );
        return;
    }

    Dialog<ButtonType> dialog = new Dialog<>();

    dialog.setTitle("Generar Reporte");

    dialog.getDialogPane().getButtonTypes().addAll(
        ButtonType.OK,
        ButtonType.CANCEL
    );

    GridPane grid = new GridPane();

    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20));

    ComboBox<String> tipoBox = new ComboBox<>();

    tipoBox.getItems().addAll(
        "Producto Dañado",
        "Bajo Stock"
    );

    tipoBox.setValue("Bajo Stock");

    TextArea descripcionArea = new TextArea();

    descripcionArea.setPromptText(
        "Escriba observaciones..."
    );

    DatePicker fechaInicio = new DatePicker();
    DatePicker fechaFin = new DatePicker();

    grid.add(new Label("Tipo Reporte:"), 0, 0);
    grid.add(tipoBox, 1, 0);

    grid.add(new Label("Fecha Inicio:"), 0, 1);
    grid.add(fechaInicio, 1, 1);

    grid.add(new Label("Fecha Fin:"), 0, 2);
    grid.add(fechaFin, 1, 2);

    grid.add(new Label("Descripción:"), 0, 3);
    grid.add(descripcionArea, 1, 3);

    dialog.getDialogPane().setContent(grid);

    Optional<ButtonType> resultado = dialog.showAndWait();

    if (resultado.isPresent() &&
        resultado.get() == ButtonType.OK) {

        Reporte reporte = new Reporte(

            "REP-" + (inventario.reportesGenerados.size() + 1),

            new Date(),

            new Date(),

            tipoBox.getValue()
        );

        reporte.setDescripcion(

            "Producto: "
            + seleccionado.getNombre()
            + " | "
            + descripcionArea.getText()
        );

        inventario.reportesGenerados.add(reporte);

        mostrarAlerta(
            "Reporte generado",
            "Proceso exitoso",
            "Reporte registrado correctamente.",
            Alert.AlertType.INFORMATION
        );
    }
});

        vista.getBtnPrueba().setOnAction(e -> {
            modeloControl.listarProductos();
        });
        vista.getBtnHistorialProductos().setOnAction(e -> {

    StringBuilder sb = new StringBuilder();

    sb.append("====================================\n");
    sb.append("    HISTORIAL DE PRODUCTOS\n");
    sb.append("====================================\n\n");

    if (inventario.historiales.isEmpty()) {

        sb.append("No existen registros.");

    } else {

        for (Historial h : inventario.historiales) {

            sb.append("ID: ")
              .append(h.getIdHistorial())
              .append("\n");

            sb.append("Fecha: ")
              .append(h.getFecha())
              .append("\n");

            sb.append("Descripción: ")
              .append(h.getDescripcion())
              .append("\n");

            sb.append("Stock Viejo: ")
              .append(h.getStockViejo())
              .append("\n");

            sb.append("Stock Nuevo: ")
              .append(h.getStockNuevo())
              .append("\n");

            sb.append("----------------------------------\n\n");
        }
    }

    TextArea area = new TextArea(sb.toString());

    area.setEditable(false);

    area.setWrapText(true);

    area.setPrefWidth(600);

    area.setPrefHeight(400);

    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    alert.setTitle("Historial Productos");

    alert.setHeaderText(
        "Altas y bajas de productos"
    );

    alert.getDialogPane().setContent(area);

    alert.showAndWait();

});
    
       vista.getBtnHistorialReportes().setOnAction(e -> {

    Producto seleccionado = vista.getTabla().getSelectionModel().getSelectedItem();

    if (seleccionado == null) {
        mostrarAlerta(
            "Atención",
            "Producto no seleccionado",
            "Seleccione un producto de la tabla.",
            Alert.AlertType.WARNING
        );
        return;
    }

    StringBuilder sb = new StringBuilder();

    sb.append("====================================\n");
    sb.append("     HISTORIAL GENERAL REPORTES\n");
    sb.append("====================================\n\n");

    sb.append("Producto Seleccionado: ")
      .append(seleccionado.getNombre())
      .append("\n\n");

    boolean encontrado = false;

    for (Reporte r : inventario.reportesGenerados) {

        // Como Reporte NO tiene Producto,
        // filtramos usando la descripción

        if (r.getDescripcion() != null &&
            r.getDescripcion().contains(seleccionado.getNombre())) {

            encontrado = true;

            sb.append("ID Reporte: ")
              .append(r.getIdReporte())
              .append("\n");

            sb.append("Tipo Reporte: ")
              .append(r.getTipoReporte())
              .append("\n");

            sb.append("Fecha Inicio: ")
              .append(r.getFechaIni())
              .append("\n");

            sb.append("Fecha Fin: ")
              .append(r.getFechaFin())
              .append("\n");

            sb.append("Descripción:\n")
              .append(r.getDescripcion())
              .append("\n");

            sb.append("------------------------------------\n");
        }
    }

    if (!encontrado) {
        sb.append("No existen reportes asociados a este producto.");
    }

    TextArea area = new TextArea(sb.toString());

    area.setEditable(false);
    area.setWrapText(true);

    area.setPrefWidth(600);
    area.setPrefHeight(450);

    area.setStyle(
        "-fx-font-family: 'Courier New';" +
        "-fx-font-size: 13px;"
    );

    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    alert.setTitle("Historial de Reportes");

    alert.setHeaderText(
        "Reportes encontrados del producto"
    );

    alert.getDialogPane().setContent(area);

    alert.showAndWait();
});
    }


    private void abrirDialogoAgregarProducto() {
        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Registrar Nueva Prenda");
        dialog.setHeaderText("Asignar los atributos del nuevo Producto:");

        ButtonType btnGuardarTipo = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardarTipo, ButtonType.CANCEL);

        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 50, 10, 10));

        TextField txtId = new TextField(); txtId.setPromptText("Ej: P003");
        TextField txtNombre = new TextField(); txtNombre.setPromptText("Ej: Camiseta");
        TextField txtDescripcion = new TextField(); txtDescripcion.setPromptText("Ej: Algodón");
        TextField txtTalla = new TextField(); txtTalla.setPromptText("Ej: M");
        TextField txtColor = new TextField(); txtColor.setPromptText("Ej: Negro");
        TextField txtPrecio = new TextField(); txtPrecio.setPromptText("Ej: 15");
        TextField txtStock = new TextField(); txtStock.setPromptText("Ej: 20");
        TextField txtMarca = new TextField(); txtMarca.setPromptText("Ej: Nike");

        grid.add(new Label("ID Producto:"), 0, 0);   grid.add(txtId, 1, 0);
        grid.add(new Label("Prenda:"), 0, 1);         grid.add(txtNombre, 1, 1);
        grid.add(new Label("Descripción:"), 0, 2);   grid.add(txtDescripcion, 1, 2);
        grid.add(new Label("Talla:"), 0, 3);          grid.add(txtTalla, 1, 3);
        grid.add(new Label("Color:"), 0, 4);         grid.add(txtColor, 1, 4);
        grid.add(new Label("Precio ($):"), 0, 5);    grid.add(txtPrecio, 1, 5);
        grid.add(new Label("Stock Inicial:"), 0, 6);  grid.add(txtStock, 1, 6);
        grid.add(new Label("Marca:"), 0, 7);          grid.add(txtMarca, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardarTipo) {
                try {
                    String idIngresado = txtId.getText().trim();

                    // CONDICIÓN CASO 1: No existe otro producto con el mismo ID en el modelo
                    if (modeloControl.buscarProducto(idIngresado) != null) {
                        throw new IdDuplicadoException(idIngresado);
                    }

                    return new Producto(
                        idIngresado, txtNombre.getText(), txtDescripcion.getText(),
                        txtTalla.getText(), txtColor.getText(), Double.parseDouble(txtPrecio.getText()),
                        Integer.parseInt(txtStock.getText()), true, txtMarca.getText()
                    );
                } catch (Exception ex) {
                    mostrarAlerta("Error de Validación", "No se pudo guardar", ex.getMessage(), Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        Optional<Producto> resultado = dialog.showAndWait();
        resultado.ifPresent(nuevoProducto -> {
            // PROCEDIMIENTO CASO 1: Se llama al método agregarProducto(p) de ProductoControl
            modeloControl.agregarProducto(nuevoProducto);
          inventario.historiales.add(new Historial( "HIST-" + (inventario.historiales.size() + 1), new Date(),"SE AGREGÓ -> " + nuevoProducto.getNombre(),0,nuevoProducto.getStock()));
            // El sistema actualiza la pantalla
            refrescarTablaDesdeModelo();
        });
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo); alerta.setHeaderText(cabecera); alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    // EXCEPCIONES
    public static class InventarioException extends Exception {
        public InventarioException(String mensaje) { super(mensaje); }
    }
    public static class IdDuplicadoException extends InventarioException {
        public IdDuplicadoException(String id) { super("Error: Ya existe un producto con el ID: " + id); }
    }
    public static class StockInsuficienteException extends InventarioException {
        public StockInsuficienteException(String p, int d, int s) {
            super("Stock insuficiente para '" + p + "'. Disponible: " + d + " u. Solicitado: " + s);
        }
    }
}