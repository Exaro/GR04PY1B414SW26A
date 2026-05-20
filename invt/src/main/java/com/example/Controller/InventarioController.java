package com.example.Controller;

import com.example.Model.Producto;
import com.example.Model.ProductoControl;
import com.example.Model.Reporte;
import com.example.View.InventarioGlobalView;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.Date;
import java.util.Optional;

public class InventarioController {

    private InventarioGlobalView vista;
    private ProductoControl modeloControl; // <-- La fuente de la verdad lógica

    public InventarioController(InventarioGlobalView vista, ProductoControl modeloControl) {
        this.vista = vista;
        this.modeloControl = modeloControl;
        
        cargarProductosPredeterminados();
        refrescarTablaDesdeModelo();
        inicializarEventos();
    }

    private void cargarProductosPredeterminados() {
        // CASO DE PRUEBA 1: Insertar a través del método nativo del modelo
        modeloControl.agregarProducto(new Producto("P001", "Camiseta", "Algodón liviano", "M", "Negro", 15.0, 20, true, "Nike"));
        modeloControl.agregarProducto(new Producto("P002", "Jeans Slim Fit", "Mezclilla elástica", "32", "Azul", 59.99, 3, true, "Levi's"));
        modeloControl.listarProductos();

    }

    private void refrescarTablaDesdeModelo() {
        // Sincroniza la lista visual de la UI con la lista interna de ProductoControl
        vista.getProductos().setAll(modeloControl.listarProductos());
    }

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
            
            // Se actualiza la interfaz
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

            // CASO DE PRUEBA 2: Buscar producto para asegurar trazabilidad antes de operar
            Producto prodModelo = modeloControl.buscarProducto(seleccionado.getIdProducto());

            TextInputDialog dialog = new TextInputDialog("0");
            dialog.setTitle("Registrar Movimiento");
            dialog.setHeaderText("Movimiento para: " + prodModelo.getNombre());
            dialog.setContentText("Ingrese cantidad (Positivo = Entrada, Negativo = Salida):");

            Optional<String> resultado = dialog.showAndWait();
            if (resultado.isPresent()) {
                try {
                    int cantidad = Integer.parseInt(resultado.get());
                    if (cantidad == 0) throw new InventarioException("La cantidad debe ser distinta de 0.");

                    if (cantidad > 0) {
                        prodModelo.setStock(prodModelo.getStock() + cantidad);
                        mostrarAlerta("Éxito", "Entrada Registrada", "Stock aumentado en el Modelo.", Alert.AlertType.INFORMATION);
                    } else {
                        int salida = Math.abs(cantidad);
                        if (prodModelo.getStock() < salida) {
                            throw new StockInsuficienteException(prodModelo.getNombre(), prodModelo.getStock(), salida);
                        }
                        prodModelo.setStock(prodModelo.getStock() - salida);
                        mostrarAlerta("Éxito", "Salida Registrada", "Stock disminuido en el Modelo.", Alert.AlertType.INFORMATION);
                    }

                    refrescarTablaDesdeModelo();

                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "Formato Inválido", "Ingrese un número entero.", Alert.AlertType.ERROR);
                } catch (InventarioException ex) {
                    mostrarAlerta("Error", "Operación Cancelada", ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });

        // ==========================================
        // CASO DE USO 3: REPORTE DE TIENDA
        // ==========================================
        vista.getBtnGenerarReporteEscrito().setOnAction(e -> {
            Date fechaActual = new Date();
            Reporte miReporte = new Reporte("REP-2026", fechaActual, fechaActual, "Alerta de Stock Crítico");

            TextInputDialog dialogoCritica = new TextInputDialog();
            dialogoCritica.setTitle("Redactar Reporte");
            dialogoCritica.setHeaderText("Ingrese observaciones de la tienda:");
            Optional<String> observacion = dialogoCritica.showAndWait();
            
            if (observacion.isPresent()) {
                miReporte.generarReporte(); // Ejecuta cabecera nativa
                System.out.println("[CRÍTICA INCORPORADA]: " + observacion.get());
                miReporte.exportarReporte();
                mostrarAlerta("Reporte", "Completado", "Consulte los logs de la consola.", Alert.AlertType.INFORMATION);
            }
        });

        vista.getBtnPrueba().setOnAction(e -> {
            modeloControl.listarProductos();
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