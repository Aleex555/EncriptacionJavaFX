package cat.iesesteveterradas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EncryptionScreen {

    @FXML
    private Button encriptarButton;

    @FXML
    private Button desencriptarButton;

    @FXML
    private TextField archivoTextField;

    @FXML
    private TextField claveTextField;

    @FXML
    private TextField contraseniaTextField;

    @FXML
    private TextField resultadoTextField;

    @FXML
    private Stage primaryStage;

    private File archivoSeleccionado;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void encriptarBoton(ActionEvent event) {
        seleccionarArchivo();
        // Lógica de encriptación aquí
    }

    @FXML
    void desencriptarBoton(ActionEvent event) {
        seleccionarArchivo();
        // Lógica de desencriptación aquí
    }

    private void seleccionarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo");
        archivoSeleccionado = fileChooser.showOpenDialog(primaryStage);

        if (archivoSeleccionado != null) {
            archivoTextField.setText(archivoSeleccionado.getAbsolutePath());
        } else {
            archivoTextField.setText("");
        }
    }

    @FXML
    void volverBoton(ActionEvent event) {
        // Lógica para volver a la pantalla anterior
    }

    @FXML
    void mostrarResultado(MouseEvent event) {
        // Lógica para mostrar el resultado
    }
}

