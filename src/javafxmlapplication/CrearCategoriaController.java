/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.User;
import model.Acount;
import model.AcountDAOException;
import model.Category;

/**
 * FXML Controller class
 *
 * @author carlosalbeldo
 */
public class CrearCategoriaController implements Initializable {

    @FXML
    private TextField nombreCategoria;
    @FXML
    private TextField DescripcionCategoria;
    @FXML
    private Button bCancelar;
    @FXML
    private Button bCrear;
    
    private boolean crearPressed = false;
    
    private Acount cuentaActual;
    
    private List<Category> categoriasUsuario;
    
    private BorderPane mainBorderPane;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            
    }    

    @FXML
    private void handleBCancelar(ActionEvent event) throws IOException {
       switchToCategorias();
    }

    @FXML
    private void handleBCrear(ActionEvent event) throws IOException, AcountDAOException {
        crearPressed = true;
        cuentaActual = Acount.getInstance();
        String nombre = nombreCategoria.getText();
        String descripcion = DescripcionCategoria.getText();

        if (!nombre.isEmpty() && !descripcion.isEmpty()) {
            try {
                boolean sePuedeCrearCategoria = cuentaActual.registerCategory(nombre, descripcion);

                if (sePuedeCrearCategoria) {
                    categoriasUsuario = cuentaActual.getUserCategories();
                    switchToCategorias();
                }
            } catch (AcountDAOException e) {
                // Mostrar alerta en caso de excepción
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error al crear categoría");
                alert.setHeaderText(null);
                alert.setContentText("Ya existe una categoría con ese nombre.");
                alert.showAndWait();
            }
        } else {
            // Mostrar alerta de tipo advertencia si hay campos vacíos
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Campos vacíos");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, completa todos los campos.");
            alert.showAndWait();
        }
    }
    
    private void switchToCategorias() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Categorias.fxml"));
        Parent root = loader.load();
        CategoriasController categoriasController = loader.getController();
        categoriasController.setMainBorderPane(mainBorderPane); // Pasar la referencia al controller
        mainBorderPane.setCenter(root);
    }
    
    public boolean iscrearPressed(){
        return crearPressed;
    }
    
    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }
    public List<Category> getCategorias(){
        return categoriasUsuario;
    }
    
    
    
}
