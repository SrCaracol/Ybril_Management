/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.User;

/**
 *
 * @author galco
 */
public class RegistrarUsuarioController {

    @FXML
    private TextField nombreUsuario;
    @FXML
    private TextField apellidosUsuario;
    @FXML
    private TextField nickUsuario;
    @FXML
    private Button bVolver;
    @FXML
    private Button bRegistrarse;
    @FXML
    private TextField correoUsuario;
    @FXML
    private Button bEliminarFoto;
    @FXML
    private Button bExplorarFoto;
    @FXML
    private ImageView fotoPerfil;
    @FXML
    private PasswordField confirmarContraseña;
    @FXML
    private PasswordField contraseñaUsuario;
    
    //variables de los LABEL de errores:
   
    @FXML
    private Label errorNombreAp;
    @FXML
    private Label errorNickname;
    @FXML
    private Label errorCorreo;
    @FXML
    private Label errorContra;
    @FXML
    private Label errorConfirmar;
    @FXML
    private TextField contraseñaLabel1;
    @FXML
    private TextField contraseñaLabel;
    @FXML
    private ImageView visibleButton;
    @FXML
    private Button visibleButton1;
    
    

    @FXML
    private void handleBVolver(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();     //Me devuelve el elemento al que hice click
        Stage stageactual = (Stage) source.getScene().getWindow();    //Me devuelve la ventana donde se encuentra el elemento
        stageactual.close();                          //Me cierra la ventana
        
        FXMLLoader loader= new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false); //Inhabilitamos modificación tamaño ventana
        String css = this.getClass().getResource("estils.css").toExternalForm(); //AÑADIR CSS
        scene.getStylesheets().add(css); //AÑADIR CSS
        stage.setTitle("Iniciar Sesión");
        //stage.show();
        stage.show();
    }

  
    
    private void controlDeErrores(){
        if(nombreUsuario.getText().equals("") || apellidosUsuario.getText().equals("")){
            errorNombreAp.setVisible(true);
        }
        if(!User.checkNickName(nickUsuario.getText())){
            errorNickname.setVisible(true);
        }
        if(!User.checkEmail(correoUsuario.getText())){
            errorCorreo.setVisible(true);
        }
        if(!User.checkPassword(contraseñaUsuario.getText())){
            errorContra.setVisible(true);
        }
        if(!contraseñaUsuario.getText().equals(confirmarContraseña.getText())){
            errorConfirmar.setVisible(true);
        }
    }


   @FXML
    private void handleRegistrarUsuario(ActionEvent event) throws AcountDAOException, IOException {
        // Resto del código...

        if (User.checkEmail(correoUsuario.getText()) && User.checkNickName(nickUsuario.getText())
                && User.checkPassword(contraseñaUsuario.getText())
                && contraseñaUsuario.getText().equals(confirmarContraseña.getText())) {
            try {
                boolean registrado = Acount.getInstance().registerUser(nombreUsuario.getText(),
                        apellidosUsuario.getText(), correoUsuario.getText(), nickUsuario.getText(),
                        contraseñaUsuario.getText(), fotoPerfil.getImage(), LocalDate.MAX);
                
                        //AQUI HAY QUE INICIAR SESIÓN Y ENVIAR A LA PESTAÑA DE LA APP
                        
                        boolean inicioSesion = Acount.getInstance().logInUserByCredentials(nickUsuario.getText(), contraseñaUsuario.getText());
        
                        if(inicioSesion){
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
                        
                        
                        
                        
                        
                        
                        
                        
                // Resto del código...
            } catch (AcountDAOException e) {
                // Mostrar alerta de tipo información
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error al registrar usuario");
                alert.setHeaderText(null);
                alert.setContentText("Ya existe un usuario con ese nickname.");
                alert.showAndWait();
            }
        } else {
            controlDeErrores();
        }
    }

    
    
    
    
    
    
    @FXML
    private void handleBExplorarFoto(ActionEvent event) throws MalformedURLException {
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
    private void handleBEliminarFoto(ActionEvent event) throws MalformedURLException {
        File file = new File("src/fotoperfil.png");
        String direccion =  file.toURI().toURL().toString() ;
        Image image = new Image(direccion);
        fotoPerfil.setImage(image);
    }

    @FXML
    private void handleBvisible(MouseEvent event) {
        contraseñaLabel.setText(contraseñaUsuario.getText());
        contraseñaUsuario.setPrefSize(0, 0);
        contraseñaLabel.setPrefSize(325, 26);
        
        contraseñaLabel1.setText(confirmarContraseña.getText());
        confirmarContraseña.setPrefSize(0, 0);
        contraseñaLabel1.setPrefSize(325, 26);
    }

    @FXML
    private void handleBinvisible(MouseEvent event) {
        contraseñaLabel.setText(contraseñaUsuario.getText());
        contraseñaUsuario.setPrefSize(325, 26);
        contraseñaLabel.setPrefSize(0, 0);
        
        contraseñaLabel1.setText(confirmarContraseña.getText());
        confirmarContraseña.setPrefSize(325, 26);
        contraseñaLabel1.setPrefSize(0, 0);
    }

    
}
