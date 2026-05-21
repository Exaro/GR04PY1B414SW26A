package com.example.View;

import com.example.Model.Producto;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class InventarioGlobalView {

    private TableView<Producto> tabla;
    private TextField filtroInput;
    private Label totalPrendasLabel;
    private Label valorBodegaLabel;
    private Label alertaStockLabel;

    private Button btnAgregarProducto;
    private Button btnNuevaTransaccion;
    private Button btnGenerarReporteEscrito;
    private Button btnEliminarProducto;
    private Button btnPrueba;
    private Button btnPruebaMov;
    private Button btnHistorialProductos;
    private Button btnHistorialReportes;

    private ObservableList<Producto> productos;
    private FilteredList<Producto> productosFiltrados;

    public InventarioGlobalView(Stage stage) {
        tabla = new TableView<>();
        productos = FXCollections.observableArrayList(); // La lista inicia vacía; la llenará el controlador
        productosFiltrados = new FilteredList<>(productos, p -> true);
        tabla.setItems(productosFiltrados);

        // COLUMNAS
        TableColumn<Producto, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdProducto()));

        TableColumn<Producto, String> colNombre = new TableColumn<>("Prenda");
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));

        TableColumn<Producto, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMarca()));

        TableColumn<Producto, String> colTalla = new TableColumn<>("Talla");
        colTalla.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTalla()));

        TableColumn<Producto, String> colColor = new TableColumn<>("Color");
        colColor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getColor()));

        TableColumn<Producto, Number> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrecio()));

        TableColumn<Producto, Number> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStock()));

        tabla.getColumns().addAll(colId, colNombre, colMarca, colTalla, colColor, colPrecio, colStock);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // COMPONENTES
        filtroInput = new TextField();
        filtroInput.setPromptText("Buscar por nombre de prenda o marca...");

        btnAgregarProducto = new Button("Agregar Producto");
        btnNuevaTransaccion = new Button("Movimiento Producto");
        btnGenerarReporteEscrito = new Button("Generar Reporte");
        btnEliminarProducto = new Button("Eliminar Producto");
        btnPrueba = new Button("Prueba");
        btnPruebaMov = new Button("Ver Movimientos");
        btnHistorialProductos = new Button("Historial de Productos");
        btnHistorialReportes = new Button("Historial de Reportes");

        btnAgregarProducto.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnNuevaTransaccion.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold;");
        btnGenerarReporteEscrito.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        btnEliminarProducto.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
        btnPrueba.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");
        btnPruebaMov.setStyle("-fx-background-color: #16a085; -fx-text-fill: white;");
        btnHistorialProductos.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");

        totalPrendasLabel = new Label();
        valorBodegaLabel = new Label();
        alertaStockLabel = new Label();
        alertaStockLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        // LAYOUTS
        HBox topBar = new HBox(15, new Label("Filtrar:"), filtroInput);
        topBar.setPadding(new Insets(5, 0, 5, 0));
        HBox barraBotones = new HBox(10, btnAgregarProducto,btnEliminarProducto,btnHistorialProductos, btnNuevaTransaccion,btnPruebaMov, btnGenerarReporteEscrito,  btnHistorialReportes, btnPrueba);
        VBox infoPanel = new VBox(8, totalPrendasLabel, valorBodegaLabel, alertaStockLabel);
        infoPanel.setPadding(new Insets(10));
        infoPanel.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
        btnHistorialReportes.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-weight: bold;");
        
        VBox root = new VBox(15, topBar, tabla, barraBotones, infoPanel);
        root.setPadding(new Insets(15));

        // BINDINGS Y FILTROS
        filtroInput.textProperty().addListener((obs, old, nuevo) -> {
            productosFiltrados.setPredicate(p -> {
                if (nuevo == null || nuevo.isEmpty()) return true;
                String lower = nuevo.toLowerCase();
                return p.getIdProducto().toLowerCase().contains(lower) || p.getMarca().toLowerCase().contains(lower);
            });
        });

        totalPrendasLabel.textProperty().bind(Bindings.size(productos).asString("Variantes de prendas en catálogo: %d"));
        
        valorBodegaLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            double total = productos.stream().mapToDouble(p -> p.getPrecio() * p.getStock()).sum();
            return String.format("Capital total retenido en Bodega: $%.2f", total);
        }, productos));

        alertaStockLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            long criticos = productos.stream().filter(p -> p.getStock() <= 5).count();
            return criticos > 0 ? "¡Alerta! Hay " + criticos + " prenda(s) con stock crítico (5 o menos unidades)." : "✅ Niveles de stock estables en bodega.";
        }, productos));

        Scene scene = new Scene(root, 900, 650);
        stage.setTitle("Sistema de Gestión de Inventario - Tienda de Ropa");
        stage.setScene(scene);
        stage.show();
    }

    // Getters convencionales
    public TableView<Producto> getTabla() { return tabla; }
    public ObservableList<Producto> getProductos() { return productos; }
    public Button getBtnAgregarProducto() { return btnAgregarProducto; }
    public Button getBtnNuevaTransaccion() { return btnNuevaTransaccion; }
    public Button getBtnGenerarReporteEscrito() { return btnGenerarReporteEscrito; }
    public Button getBtnEliminarProducto() { return btnEliminarProducto; }
    public Button getBtnPrueba() { return btnPrueba; }
    public Button getBtnPruebaMov() { return btnPruebaMov; }
    public Button getBtnHistorialProductos() { return btnHistorialProductos; }  
    public Button getBtnHistorialReportes() { return btnHistorialReportes; }
}