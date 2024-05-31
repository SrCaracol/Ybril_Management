/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;

/**
 * FXML Controller class
 *
 * @author carlosalbeldo
 */
public class CrearDespesaController implements Initializable {

    @FXML
    private TextField nombreGasto;
    @FXML
    private TextField precioGasto;
    @FXML
    private TextArea descripcionGasto;
    @FXML
    private DatePicker fechaGasto;
    @FXML
    private ImageView fotoPerfil;
    @FXML
    private ChoiceBox<String> categoriasGasto;
    @FXML
    private TextField unidades;
    @FXML
    private Button bCancelar;
    @FXML
    private Button bCrear;
    @FXML
    private Button bEliminarTicket;
    @FXML
    private Button bExplorarTicket;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            cargarCategoriasChoiceBox();
        } catch (IOException ex) {
            Logger.getLogger(CrearDespesaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AcountDAOException ex) {
            Logger.getLogger(CrearDespesaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
       // ************************************************
       //PRUEBA DE FUNCIONAMIENTO
        try {
            List<Category> categorias = Acount.getInstance().getUserCategories();
            System.out.println(categorias.toString());
        } catch (AcountDAOException ex) {
            Logger.getLogger(CrearDespesaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CrearDespesaController.class.getName()).log(Level.SEVERE, null, ex);
        }
      //*******************************************************  
        
        
    }

    
    private void cargarCategoriasChoiceBox() throws IOException, AcountDAOException {
        // Obtener la lista de categorías del usuario y actualizar el ChoiceBox
        List<Category> categorias = Acount.getInstance().getUserCategories();
        
        for(int i = 0; i < categorias.size() ;i++){
            categoriasGasto.getItems().add(categorias.get(i).getName());
        }
        
        
    }
    @FXML
    private void handleBCancelar(ActionEvent event) throws IOException {
        
        Node source = (Node) event.getSource();     //Me devuelve el elemento al que hice click
            Stage stageactual = (Stage) source.getScene().getWindow();    //Me devuelve la ventana donde se encuentra el elemento
            stageactual.close();                          //Me cierra la ventana
            
            //Ir a la pestaña principal:
            FXMLLoader loader= new FXMLLoader(getClass().getResource("AppPrincipal.fxml"));
            Parent root = loader.load();
            AppPrincipalController controllerAppPrincipal = loader.getController();


            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ybril Management");
            String css = this.getClass().getResource("estils.css").toExternalForm(); //AÑADIR CSS
            scene.getStylesheets().add(css); //AÑADIR CSS
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            //stage.showAndWait();
        
    }

    @FXML
    private void handleBCrear(ActionEvent event) throws IOException, AcountDAOException {
        //Comprovem que estiga tots els TextFields obligatoris plens
        if (nombreGasto.getText() == "" || precioGasto.getText() == "" || fechaGasto.getValue() == null || categoriasGasto.getSelectionModel().getSelectedIndex() < 0){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Campos vacíos");
            alert.setContentText("Hay campos obligatorios por rellenar");
            alert.showAndWait();
        } else {
            //Comprove el format de precioGasto
            try {
                Double.parseDouble(precioGasto.getText());

            }catch (Exception e){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Error de formato");
                alert.setContentText("Debe poner números en la casilla de precio");
                alert.showAndWait();
            }
            //Comprava el format de Unidades
            if (unidades.getText()!=""){
                try {
                    Integer.parseInt(unidades.getText());
                }catch (Exception e){
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error de formato");
                    alert.setContentText("Debe poner números en la casilla de unidades");
                    alert.showAndWait();
                }
            }
            double precioGastoDouble = Double.parseDouble(precioGasto.getText());
            int unidadesInt = 1;
            if (unidades.getText()!=""){
                unidadesInt = Integer.parseInt(unidades.getText());
            }

            LocalDate fechaDate = fechaGasto.getValue();

            boolean registrado = Acount.getInstance().registerCharge(nombreGasto.getText(), descripcionGasto.getText(), precioGastoDouble, unidadesInt, fotoPerfil.getImage(), fechaDate, Acount.getInstance().getUserCategories().get(categoriasGasto.getSelectionModel().getSelectedIndex()));
            if(registrado){


                Node source = (Node) event.getSource();     //Me devuelve el elemento al que hice click
                Stage stageactual = (Stage) source.getScene().getWindow();    //Me devuelve la ventana donde se encuentra el elemento
                stageactual.close();                          //Me cierra la ventana

                //Ir a la pestaña principal:
                FXMLLoader loader= new FXMLLoader(getClass().getResource("AppPrincipal.fxml"));
                Parent root = loader.load();
                AppPrincipalController controllerAppPrincipal = loader.getController();


                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Ybril Management");
                String css = this.getClass().getResource("estils.css").toExternalForm(); //AÑADIR CSS
                scene.getStylesheets().add(css); //AÑADIR CSS
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                //stage.showAndWait();
            }
        }
        
    }

    @FXML
    private void handleBEliminarTicket(ActionEvent event) throws MalformedURLException {
        File file = new File("src/ticket.png");
        String direccion =  file.toURI().toURL().toString() ;
        Image image = new Image(direccion);
        fotoPerfil.setImage(image);
    }

    @FXML
    private void handleBExplorarTicket(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Obrir fitxer");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imatges", "*.png", "*.jpg", "*.gif","*.jpeg"),
            new FileChooser.ExtensionFilter("Tots", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());

        String imageUrl = selectedFile.toURI().toURL().toString();
        Image image = new Image(imageUrl);
        fotoPerfil.setImage(image);
    }



   
    
}
