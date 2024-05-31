/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Category;
import model.Charge;

/**
 * FXML Controller class
 *
 * @author tonim
 */
public class InfoCategoríasController implements Initializable {

    @FXML
    private Label NombreCategoría;
    @FXML
    private Button bAtrás;
    @FXML
    private Label DescripciónCategoría;

    private BorderPane mainBorderPane;
     private Category selectedCategory;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (selectedCategory != null) {
        setCategory(selectedCategory);
        }
    }    

    public void setCategory(Category category) {
        this.selectedCategory = category;
        NombreCategoría.setText(category.getName());  
        DescripciónCategoría.setText(category.getDescription()); 
    }
    @FXML
    private void handleBAtrás(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Categorias.fxml"));
        Parent root = loader.load();
        CategoriasController controllerCategorias = loader.getController();
        controllerCategorias.setMainBorderPane(mainBorderPane); // Pasar la referencia al controller
        mainBorderPane.setCenter(root);
    }
    
    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }
}
