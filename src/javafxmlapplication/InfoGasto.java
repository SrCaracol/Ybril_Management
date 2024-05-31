package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Charge;

public class InfoGasto implements Initializable {

    @FXML
    private Label NombreGasto;
    @FXML
    private Label PrecioGasto;
    @FXML
    private Button bAtrás;
    @FXML
    private Label FachaGasto;
    @FXML
    private ImageView TicketGasto;
    @FXML
    private Label CategoríaGasto;
    @FXML
    private Label DescripciónGasto;
    @FXML
    private Label UnidadesGasto;

    private Charge selectedCharge;
    
    private BorderPane mainBorderPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No se necesita inicialización adicional
        if (selectedCharge != null) {
        setCharge(selectedCharge);
        }
    }    
   

    public void setCharge(Charge charge) {
        this.selectedCharge = charge;
        NombreGasto.setText(charge.getName());
        PrecioGasto.setText(String.valueOf(charge.getCost()));
        DescripciónGasto.setText(charge.getDescription());
        FachaGasto.setText(charge.getDate().toString());
        CategoríaGasto.setText(charge.getCategory().getName());
        UnidadesGasto.setText(Integer.toString(charge.getUnits()));
        TicketGasto.setImage(charge.getImageScan());
    }

    @FXML
    private void handleBAtrás(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Gastos.fxml"));
        Parent root = loader.load();
        GastosController controllerGastos = loader.getController();
        controllerGastos.setMainBorderPane(mainBorderPane); // Pasar la referencia al controller
        mainBorderPane.setCenter(root);
    }
    
    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }
}
