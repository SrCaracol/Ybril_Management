/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.User;
import model.Acount;
import model.AcountDAOException;

/**
 * FXML Controller class
 *
 * @author carlosalbeldo
 */
public class AppPrincipalController implements Initializable {

    @FXML
    private Button bEditarPerfil;
    @FXML
    private Button bGastos;
    @FXML
    private Button bCrearGasto;
    @FXML
    private Button bCategorias;
    @FXML
    private Button bTablaBarras;
    @FXML
    private Button bTablaQueso;
    @FXML
    private Button tablaLineas;
    @FXML
    private Button bSalir;
    @FXML
    private ImageView imagenUsuario;
    @FXML
    private Label niknameUsuario;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ToolBar BarraaHerramientas;
    

    
    public void initialize(URL url, ResourceBundle rb) {
       try {
            imagenUsuario.setImage(Acount.getInstance().getLoggedUser().getImage());
            niknameUsuario.setText(Acount.getInstance().getLoggedUser().getNickName());
            switchCenter("Gastos.fxml");
        } catch (AcountDAOException | IOException ex) {
            Logger.getLogger(AppPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }    

    @FXML
    private void handleBEditarPerfil(ActionEvent event) throws IOException {
        switchCenter("EditarPerfil.fxml");
      
    }

    @FXML
    private void handleBGastos(ActionEvent event) throws IOException {
        switchCenter("Gastos.fxml");
        
    }

    @FXML
    private void handleBCrearGasto(ActionEvent event) throws IOException {
        switchCenter("CrearDespesa.fxml");
    }

    @FXML
    private void handleBCategorias(ActionEvent event) throws IOException {
        switchCenter("Categorias.fxml");
    }

    @FXML
    private void handleBTablasBarras(ActionEvent event) throws IOException {
        switchCenter("GraficoBarras.fxml");
    }

    @FXML
    private void handleBTableQueso(ActionEvent event) throws IOException {
        switchCenter("GraficoQuesos.fxml");
    }

    @FXML
    private void handleBTablaLineas(ActionEvent event) throws IOException {
        switchCenter("GraficoLineas.fxml");
    }

    @FXML
    private void handleBSalir(ActionEvent event) throws IOException, AcountDAOException {
        boolean salirUsuario = Acount.getInstance().logOutUser();
        if(salirUsuario){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        String css = this.getClass().getResource("estils.css").toExternalForm(); //AÑADIR CSS
        scene.getStylesheets().add(css); //AÑADIR CSS
        stage.setTitle("Ybril Management");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        Stage currentStage = (Stage) mainBorderPane.getScene().getWindow();
        currentStage.close();}
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
        } else if (controller instanceof GastosController) {
            ((GastosController) controller).setMainBorderPane(mainBorderPane);
        } else if (controller instanceof EditarDespesaController) {
            ((EditarDespesaController) controller).setMainBorderPane(mainBorderPane);
        } else if (controller instanceof InfoGasto) {
            ((InfoGasto) controller).setMainBorderPane(mainBorderPane);
        }

        mainBorderPane.setCenter(root);
    }
    
    
}
