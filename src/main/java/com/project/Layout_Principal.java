package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Layout_Principal {

    @FXML
    private void encriptar_boton(ActionEvent event) {
        UtilsViews.setView("layout_encriptar");
    }

    @FXML
    private void desencriptar_boton(ActionEvent event) {
        UtilsViews.setView("layout_principal");
    }

}