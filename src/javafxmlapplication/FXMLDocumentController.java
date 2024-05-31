/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmlapplication;

import java.io.File;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model.Acount;
import model.AcountDAOException;
import model.User;


public class FXMLDocumentController implements Initializable {

    //Objetos Iniciar Sesión
    @FXML
    private TextField nickUsuario;
    @FXML
    private PasswordField contraseñaUsuario;
    @FXML
    private Button bSalir;
    
    //Objetos Registrar Usuario
    private Button bVolver;
    private ImageView fotoPerfil;
    @FXML
    private Hyperlink linkRegistrarse;
    @FXML
    private Button bIniciarSesion;
    @FXML
    private Label errorUsuCont;
    @FXML
    private Text TítuloInicioSesión;
    @FXML
    private GridPane Fondo;
    @FXML
    private Pane FondoTítulo;
    @FXML
    private Button visibleButton;
    @FXML
    private TextField contraseñaLabel;
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
     
        contraseñaLabel.setPrefSize(0, 0);
  
    }  
    
    //Iniciar Sesión Métodos
    @FXML
    private void handleLinkRegistro(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();     //Me devuelve el elemento al que hice click
        Stage stageactual = (Stage) source.getScene().getWindow();    //Me devuelve la ventana donde se encuentra el elemento
        stageactual.close();                          //Me cierra la ventana
        
        FXMLLoader loader= new FXMLLoader(getClass().getResource("RegistrarUsuario.fxml"));
        Parent root = loader.load();
        RegistrarUsuarioController controllerRegistrar = loader.getController();
        
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        
        stage.setResizable(false); //Inhabilitamos modificación tamaño ventana
        stage.setTitle("Registrar");
        String css = this.getClass().getResource("estils.css").toExternalForm(); //AÑADIR CSS
        scene.getStylesheets().add(css); //AÑADIR CSS
        stage.initModality(Modality.APPLICATION_MODAL);
        //stage.show();
        stage.show();
        
    }

    @FXML
    private void handleBSalir(ActionEvent event) {
        System.exit(0);
    }
      
    @FXML
    private void handlebIniciarSesion(ActionEvent event) throws IOException, AcountDAOException {
        
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

                                   
        }else{avisoDeError();}
    }

    private void avisoDeError() {
        //introducir codigo para que salga el error cuando alguien pone un usuario mal
        errorUsuCont.setVisible(true);
    }

    

    @FXML
    private void handleBvisible(MouseEvent event) {
        contraseñaLabel.setText(contraseñaUsuario.getText());
        contraseñaUsuario.setPrefSize(0, 0);
        contraseñaLabel.setPrefSize(227, 26);
    }

    @FXML
    private void handleBinvisible(MouseEvent event) {
        contraseñaLabel.setText(contraseñaUsuario.getText());
        contraseñaUsuario.setPrefSize(227, 26);
        contraseñaLabel.setPrefSize(0, 0);
    }
    
    

}
