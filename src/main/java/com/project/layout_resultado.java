package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class layout_resultado {

    @FXML
    private Label resultado;

    @FXML
    private void back(ActionEvent event) {
        UtilsViews.setView("layout_principal");
    }

}
