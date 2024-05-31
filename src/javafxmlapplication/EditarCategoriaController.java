/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Category;

/**
 * FXML Controller class
 *
 * @author carlosalbeldo
 */
public class EditarCategoriaController implements Initializable {

    @FXML
    private TextField nombreCategoria;
    @FXML
    private TextField DescripcionCategoria;
    @FXML
    private Button bCancelar;
    @FXML
    private Button bCrear;
    
    private boolean crearPressed = false;
    
    private Category categoria;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleBCancelar(ActionEvent event) {
         nombreCategoria.getScene().getWindow().hide();
    }

    @FXML
    private void handleBCrear(ActionEvent event) {
        
        String nombre = nombreCategoria.getText();
        String descripcion = DescripcionCategoria.getText();
        if(!nombre.equals("") && !descripcion.equals("")){
            crearPressed = true;
            nombreCategoria.getScene().getWindow().hide();
        }
    }
    
    public boolean iscrearPressed(){
        return crearPressed;
    }
    
    public Category getCategory(){
        return categoria;
    }
    
    public void initCategoria(Category c){
        categoria = c;
        nombreCategoria.setText(c.getName());
        DescripcionCategoria.setText(c.getDescription());
    }
    
}
