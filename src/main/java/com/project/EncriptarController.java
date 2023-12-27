package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EncriptarController {
    File archivoEntrada;
    File archivoSalida;

    @FXML
    private TextField id_clave_publica;

    @FXML
    private Button archivo, archivo_salida;

    @FXML
    private void back(ActionEvent event) {
        UtilsViews.setView("layout_principal");
    }

    @FXML
    private void action_encriptar(ActionEvent event) {
    }

    @FXML
    private void select_archivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona el Archivo de Entrada");
        archivoEntrada = fileChooser.showOpenDialog(new Stage());
        if (archivoEntrada != null) {
            archivo.setText(archivoEntrada.getName());
        }

    }

    @FXML
    private void select_destino(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona el Archivo de Entrada");
        archivoSalida = fileChooser.showOpenDialog(new Stage());
        if (archivoSalida != null) {
            archivo_salida.setText(archivoSalida.getName());
        }

    }

}
