/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;

/**
 * FXML Controller class
 *
 * @author galco
 */
public class CategoriasController implements Initializable {

    @FXML
    private Button bCrearCategoria;
    @FXML
    private Button bBorarCategoria;
    
    private Acount cuentaActual;
    
    @FXML
    private BorderPane borderPane;
    @FXML
    private ListView<Category> listaCategorias;
    
    ObservableList<Category> datos;
    
    private BorderPane mainBorderPane;
    @FXML
    private Pane pane;
    @FXML
    private Button bInfoCategoríes;
    
    private Category selectedCategory;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            List<Category> categorias = Acount.getInstance().getUserCategories();
            datos = FXCollections.observableArrayList(categorias);
            listaCategorias.setItems(datos);
            listaCategorias.setCellFactory(param -> new ListCell<Category>() {
                @Override
                protected void updateItem(Category item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.getName() == null) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            });
            
        } catch (AcountDAOException | IOException ex) {
            Logger.getLogger(CategoriasController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        
    }    
    
    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }


    @FXML
    private void handleBCrearCategoria(ActionEvent event) throws IOException, AcountDAOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearCategoria.fxml"));
        Parent root = loader.load();
        CrearCategoriaController controllerCrearCategoria = loader.getController();
        controllerCrearCategoria.setMainBorderPane(mainBorderPane); // Pasar la referencia al controller
        mainBorderPane.setCenter(root);
        
        if (controllerCrearCategoria.iscrearPressed()) {
            datos.setAll(cuentaActual.getUserCategories());
        }
        
        
       
    }

    
    
    @FXML
    private void handleBBorrarCategoria(ActionEvent event) throws IOException, AcountDAOException {
        Category selectedCategory = listaCategorias.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            cuentaActual = Acount.getInstance();
            boolean sePuedeBorrarCategoria = cuentaActual.removeCategory(selectedCategory);
            if (sePuedeBorrarCategoria) {
                datos.remove(selectedCategory);
                switchCenter("Categorias.fxml");
            }
        }
    }


    private void switchCenter(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();

        // Pasar la referencia del BorderPane principal al controlador cargado
        Initializable controller = loader.getController();
        if (controller instanceof CategoriasController) {
            ((CategoriasController) controller).setMainBorderPane(mainBorderPane);
        } else if (controller instanceof CrearCategoriaController) {
            ((CrearCategoriaController) controller).setMainBorderPane(mainBorderPane);
        }

        mainBorderPane.setCenter(root);
    }

    @FXML
    private void handleBInfoCategoríes(ActionEvent event) throws IOException {
       
        this.selectedCategory = listaCategorias.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                 FXMLLoader loader = new FXMLLoader(getClass().getResource("InfoCategorías.fxml"));
                Parent root = loader.load();
                InfoCategoríasController controllerInfoCategoria = loader.getController();
                controllerInfoCategoria.setMainBorderPane(mainBorderPane); // Pasar la referencia al controller
                controllerInfoCategoria.setCategory(selectedCategory); // Pasar el cargo seleccionado
                mainBorderPane.setCenter(root);
            } catch (IOException ex) {
                Logger.getLogger(GastosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
}
