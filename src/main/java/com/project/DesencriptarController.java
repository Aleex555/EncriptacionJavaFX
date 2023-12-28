package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.openpgp.PGPException;

public class DesencriptarController {
    File archivoEntrada;
    File archivoSalida;
    File claveprivada;

    @FXML
    private Button archivo, archivo_salida, clave_privada;

    @FXML
    private void back(ActionEvent event) {
        UtilsViews.setView("layout_principal");
    }

    @FXML
    private PasswordField id_contra;


    @FXML
    private void action_desencriptar(ActionEvent event) {
    if (archivoEntrada != null && archivoSalida != null && claveprivada != null && id_contra.getText() != null) {
        System.out.println(archivoEntrada.getName());
        System.out.println(archivoSalida.getName());
        System.out.println(claveprivada.getName());
        System.out.println(id_contra.getText());
        try {
            DesencriptadorGPG.desencriptarArchivo(archivoEntrada, archivoSalida, claveprivada, id_contra.getText());
            UtilsViews.setView("layout_resultado");
        } catch (IOException | PGPException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    } else {
        UtilsViews.setView("layout_resultado_error");
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
    private void clave_privada(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona el Archivo de Salida");
        claveprivada = fileChooser.showOpenDialog(new Stage());
        if (clave_privada != null) {
            clave_privada.setText(claveprivada.getName());
        }
    }

}
