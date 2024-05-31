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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.User;

/**
 * FXML Controller class
 *
 * @author carlosalbeldo
 */
public class EditarPerfilController implements Initializable {

    @FXML
    private TextField nombreUsuario;
    @FXML
    private TextField apellidosUsuario;
    private TextField nickUsuario;
    @FXML
    private Button bAceptarEditar;
    @FXML
    private TextField correoUsuario;
    @FXML
    private ImageView fotoPerfil;
    @FXML
    private Button bEliminarFoto;
    @FXML
    private Button bExplorarFoto;
    @FXML
    private PasswordField contraseñaUsuario;
    @FXML
    private PasswordField confirmarContraseña;
    @FXML
    private Label campoVacio;
    @FXML
    private Label formatoContraseña;
    @FXML
    private Label formatCorreo;
    @FXML
    private Label contraseñasCoinciden;
    @FXML
    private Button bCancelar;
    @FXML
    private TextField contraseñaLabel;
    @FXML
    private Button visibleButton1;
    @FXML
    private ImageView visibleButton2;
    @FXML
    private Label errorConfirmar;
    @FXML
    private TextField contraseñaLabel1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
           
            nombreUsuario.setPromptText(Acount.getInstance().getLoggedUser().getName());
            correoUsuario.setPromptText(Acount.getInstance().getLoggedUser().getEmail());
            apellidosUsuario.setPromptText(Acount.getInstance().getLoggedUser().getSurname());
            fotoPerfil.setImage(Acount.getInstance().getLoggedUser().getImage());
            
        } catch (AcountDAOException ex) {
            Logger.getLogger(EditarPerfilController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EditarPerfilController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    


    @FXML
    private void handleBAceptarEditar(ActionEvent event) throws AcountDAOException, IOException {
        
        
        formatoContraseña.setVisible(false);

        contraseñasCoinciden.setVisible(false); //esto es para que los comentarios de error se
                                                //vuelvan invisibles si no lo estan
        formatCorreo.setVisible(false);

        campoVacio.setVisible(false);
        
        campoVacio.setVisible(false);
        
        
        
        
        if(User.checkPassword(contraseñaUsuario.getText()) && confirmarContraseña.getText().equals(contraseñaUsuario.getText()) &&
           User.checkEmail(correoUsuario.getText()) &&
           !apellidosUsuario.getText().equals("") && 
           !apellidosUsuario.getText().equals("")) //SI TODO LO QUE SE HA INTRODUCIDO ES CORRECTO SE CAMBIAN LOS DATOS
        {
            Acount.getInstance().getLoggedUser().setName(nombreUsuario.getText());
            
            Acount.getInstance().getLoggedUser().setPassword(contraseñaUsuario.getText());

            Acount.getInstance().getLoggedUser().setEmail(correoUsuario.getText());

            Acount.getInstance().getLoggedUser().setSurname(apellidosUsuario.getText());

            Acount.getInstance().getLoggedUser().setImage(fotoPerfil.getImage());
            
            //Volver a la pestaña anterior:
            FXMLLoader loader= new FXMLLoader(getClass().getResource("AppPrincipal.fxml"));
            Parent root = loader.load();
            AppPrincipalController controllerAppPrincipal = loader.getController();


            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            String css = this.getClass().getResource("estils.css").toExternalForm(); //AÑADIR CSS
            scene.getStylesheets().add(css); //AÑADIR CSS
            stage.setTitle("Ybril Management");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            //stage.showAndWait();

            Node source = (Node) event.getSource();     //Me devuelve el elemento al que hice click
            stage = (Stage) source.getScene().getWindow();    //Me devuelve la ventana donde se encuentra el elemento
            stage.close();                          //Me cierra la ventana                         //Me cierra la ventana                          //Me cierra la ventana
            
            
        }else{errores();} //SI LOS DATOS SON INCORRECTOS SE SALTA AL CONTROL DE ERRORES
        
        
        
        
        
        
    }
    

    @FXML
    private void handleBExplorarFotoOnAction(ActionEvent event) throws MalformedURLException {
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
    
    @FXML
    private void handleBEliminarFotoOnAction(ActionEvent event) throws MalformedURLException {
        File file = new File("src/fotoperfil.png");
        String direccion =  file.toURI().toURL().toString() ;
        Image image = new Image(direccion);
        fotoPerfil.setImage(image);
    }

    private void errores() { //hace visibles los comentarios en base a el error
        if(!User.checkPassword(contraseñaUsuario.getText())){
            formatoContraseña.setVisible(true);
        }
        if(!confirmarContraseña.getText().equals(contraseñaUsuario.getText())){
            contraseñasCoinciden.setVisible(true);
        }
        if(!User.checkEmail(correoUsuario.getText())){
            formatCorreo.setVisible(true);
        }
        if(apellidosUsuario.getText().equals("")){
            campoVacio.setVisible(true);
        }
        if(apellidosUsuario.getText().equals("")){
            campoVacio.setVisible(true);
        }
    }

    @FXML
    private void handleBCancelarEditar(ActionEvent event) throws IOException {
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
    private void handleBvisible(MouseEvent event) {
        contraseñaLabel.setText(contraseñaUsuario.getText());
        contraseñaUsuario.setPrefSize(0, 0);
        contraseñaLabel.setPrefSize(389.6, 25.6);
        
        contraseñaLabel1.setText(confirmarContraseña.getText());
        confirmarContraseña.setPrefSize(0, 0);
        contraseñaLabel1.setPrefSize(389.6, 25.6);
    }

    @FXML
    private void handleBinvisible(MouseEvent event) {
        contraseñaLabel.setText(contraseñaUsuario.getText());
        contraseñaUsuario.setPrefSize(389.6, 25.6);
        contraseñaLabel.setPrefSize(0, 0);
        
        contraseñaLabel1.setText(confirmarContraseña.getText());
        confirmarContraseña.setPrefSize(389.6, 25.6);
        contraseñaLabel1.setPrefSize(0, 0);
    }


    

    

    
    
}
