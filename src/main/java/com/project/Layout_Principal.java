package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class Layout_Principal {
    private Button button01, button02;

    @FXML
    private void encriptar_boton(ActionEvent event) {
        UtilsViews.setView("layout_principal");
    }

    @FXML
    private void desencriptar_boton(ActionEvent event) {
        UtilsViews.setView("layout_principal");
    }

}