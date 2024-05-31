package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;

public class EditarDespesaController implements Initializable {

    @FXML
    private TextField nombreGasto;
    @FXML
    private TextField precioGasto;
    @FXML
    private TextArea descripcionGasto;
    @FXML
    private Button bCancelar;
    @FXML
    private Button bModificarGasto;
    @FXML
    private DatePicker fechaGasto;
    @FXML
    private ImageView fotoPerfil;
    @FXML
    private Button bEliminarTicket;
    @FXML
    private Button bExplorarTicket;
    @FXML
    private ChoiceBox<String> categoriasGasto;

    private Charge selectedCharge;
    
    private List<Category> categories;
    @FXML
    private TextField unidades;
    
    private BorderPane mainBorderPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            categories = Acount.getInstance().getUserCategories();
        } catch (AcountDAOException | IOException ex) {
            Logger.getLogger(EditarDespesaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        categoriasGasto.setItems(FXCollections.observableArrayList(
            categories.stream().map(Category::getName).toList()
        ));
        
        if (selectedCharge != null) {
        setCharge(selectedCharge);
        }
        
        
    }

    public void setCharge(Charge charge) {
        this.selectedCharge = charge;
        nombreGasto.setText(charge.getName());
        precioGasto.setText(String.valueOf(charge.getCost()));
        descripcionGasto.setText(charge.getDescription());
        fechaGasto.setValue(charge.getDate());
        categoriasGasto.setValue(charge.getCategory().getName());
        unidades.setText(Integer.toString(charge.getUnits()));
        fotoPerfil.setImage(charge.getImageScan());
    }

    @FXML
    private void handleBCancelar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Gastos.fxml"));
        Parent root = loader.load();
        GastosController controllerGastos = loader.getController();
        controllerGastos.setMainBorderPane(mainBorderPane); // Pasar la referencia al controller
        mainBorderPane.setCenter(root);
    }

    @FXML
    private void handleBModificarGasto(ActionEvent event) throws IOException {
        try {
            boolean removed = Acount.getInstance().removeCharge(selectedCharge);

            if (removed) {
                String selectedCategoryName = categoriasGasto.getValue();
                Category selectedCategory = categories.stream()
                    .filter(cat -> cat.getName().equals(selectedCategoryName))
                    .findFirst()
                    .orElse(null);

                if (selectedCategory != null) {
                    boolean registered = Acount.getInstance().registerCharge(
                        nombreGasto.getText(),
                        descripcionGasto.getText(),
                        Double.parseDouble(precioGasto.getText()),
                        parseInt(unidades.getText()),
                        fotoPerfil.getImage(),
                        fechaGasto.getValue(),
                        selectedCategory
                    );

                    if (registered) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Gastos.fxml"));
                        Parent root = loader.load();
                        GastosController controllerGastos = loader.getController();
                        controllerGastos.setMainBorderPane(mainBorderPane); // Pasar la referencia al controller
                        mainBorderPane.setCenter(root);
                    } else {
                        Logger.getLogger(EditarDespesaController.class.getName()).log(Level.SEVERE, "Error: No se pudo registrar el nuevo gasto.");
                    }
                } else {
                    Logger.getLogger(EditarDespesaController.class.getName()).log(Level.SEVERE, "Error: Categoría no encontrada.");
                }
            } else {
                Logger.getLogger(EditarDespesaController.class.getName()).log(Level.SEVERE, "Error: No se pudo eliminar el gasto anterior.");
            }
        } catch (AcountDAOException ex) {
            Logger.getLogger(EditarDespesaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleBEliminarTicket(ActionEvent event) throws MalformedURLException {
        File file = new File("src/ticket.png");
        String direccion =  file.toURI().toURL().toString();
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
    
    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }
}
