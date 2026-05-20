package com.example.MainApp;

import com.example.Model.ProductoControl; // <-- Importado
import com.example.View.InventarioGlobalView;
import com.example.Controller.InventarioController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Instanciamos el Modelo (Donde se almacena la lista real de tus casos de prueba)
        ProductoControl modeloControl = new ProductoControl();
        
        // 2. Levantamos la interfaz gráfica (Vista)
        InventarioGlobalView vista = new InventarioGlobalView(primaryStage);
        
        // 3. El Controlador asocia ambos componentes garantizando la trazabilidad pura
        new InventarioController(vista, modeloControl);
    }

    public static void main(String[] args) {
        launch(args);
    }
}