package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.openpgp.PGPException;

public class EncriptarController {
    File archivoEntrada;
    File archivoSalida;
    File clave_publica;

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
        if (archivoEntrada != null && archivoSalida != null && clave_publica != null) {
            try {
                EncriptadorGPG.encriptarArchivo(archivoEntrada, archivoSalida, clave_publica);
            } catch (IOException | PGPException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
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
        fileChooser.setTitle("Selecciona el Archivo de Salida");
        File archivoSalida = fileChooser.showSaveDialog(new Stage());
        if (archivoSalida != null) {
            // Actualizar el texto en algún control, como un TextField
            archivo_salida.setText(archivoSalida.getName());
        }
    }

}
