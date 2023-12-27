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
    File clave_publica1;

    @FXML
    private Button archivo, archivo_salida, id_clave_publica;

    @FXML
    private void back(ActionEvent event) {
        UtilsViews.setView("layout_principal");
    }

    @FXML
    private void action_encriptar(ActionEvent event) {
        System.out.println(archivoEntrada.getName());
        System.out.println(archivoSalida.getName());
        System.out.println(clave_publica1.getName());
        if (archivoEntrada != null && archivoSalida != null && clave_publica1 != null) {
            try {
                EncriptadorGPG.encriptarArchivo(archivoEntrada, archivoSalida, clave_publica1);
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
        archivoSalida = fileChooser.showSaveDialog(new Stage());
        if (archivoSalida != null) {
            archivo_salida.setText(archivoSalida.getName());
        }
    }

    @FXML
    private void clave_publica(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona el Archivo de Salida");
        clave_publica1 = fileChooser.showOpenDialog(new Stage());
        if (clave_publica1 != null) {
            id_clave_publica.setText(clave_publica1.getName());
        }
    }

}
