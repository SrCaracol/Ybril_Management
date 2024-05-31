package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;

public class GastosController implements Initializable {

    private Charge selectedCharge;

    @FXML
    private DatePicker desdeDP;
    @FXML
    private DatePicker hastaDP;
    @FXML
    private Button bImprimir;
    @FXML
    private Button bModificarGasto;
    @FXML
    private Button bBorrarGasto;
    @FXML
    private TableView<Charge> gastosTable;
    @FXML
    private TableColumn<Charge, String> categoriaColumn;
    @FXML
    private TableColumn<Charge, String> gastosColumn;
    @FXML
    private TableColumn<Charge, Double> costeColumn;

    private ObservableList<Charge> gastosList = FXCollections.observableArrayList();

    private static GastosController instance;
    @FXML
    private Button bInfo;
    @FXML
    private Menu menuFiltrar;

    ObservableList<Category> datos;

    List<Category> categorias;

    private BorderPane mainBorderPane;

    public GastosController() {
        instance = this;
    }

    public static GastosController getInstance() {
        return instance;
    }

    public Charge getCharge() {
        return selectedCharge;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar las columnas de la tabla
        categoriaColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Charge, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Charge, String> param) {
                return new SimpleStringProperty(param.getValue().getCategory().getName());
            }
        });
        gastosColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        costeColumn.setCellValueFactory(cellData -> {
            double costeMultiplicado = cellData.getValue().getCost() * cellData.getValue().getUnits();
            return new SimpleDoubleProperty(costeMultiplicado).asObject();
        });

        gastosTable.setItems(gastosList);

        // Establecer los valores iniciales de los DatePicker
        LocalDate today = LocalDate.now();
        desdeDP.setValue(today.minusMonths(1));
        hastaDP.setValue(today);

        // Configurar los DatePicker para deshabilitar fechas inválidas
        desdeDP.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) > 0);
            }
        });

        hastaDP.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) > 0);
            }
        });

        // Agregar listeners a los DatePicker para actualizar los gastos
        desdeDP.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                actualizarGastos();
            } catch (AcountDAOException | IOException ex) {
                Logger.getLogger(GastosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        hastaDP.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                actualizarGastos();
            } catch (AcountDAOException | IOException ex) {
                Logger.getLogger(GastosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        try {
            cargarGastos();
        } catch (AcountDAOException | IOException ex) {
            Logger.getLogger(GastosController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Inicializar el menú de filtrado
        try {
            categorias = Acount.getInstance().getUserCategories();
        } catch (AcountDAOException | IOException ex) {
            Logger.getLogger(GastosController.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Category categoria : categorias) {
            CheckMenuItem checkMenuItem = new CheckMenuItem(categoria.getName());
            checkMenuItem.setSelected(true);
            checkMenuItem.setOnAction(e -> updateFilteredCategories());
            menuFiltrar.getItems().add(checkMenuItem);
        }
    }

    private void cargarGastos() throws AcountDAOException, IOException {
        List<Charge> charges = Acount.getInstance().getUserCharges();
        if (charges != null) {
            gastosList.addAll(charges);
            actualizarGastos(); // Filtrar los gastos iniciales por la fecha actual
        }
    }

    public void addCharge(Charge charge) throws AcountDAOException, IOException {
        gastosList.add(charge);
        actualizarGastos(); // Actualizar la lista después de agregar un nuevo gasto
    }

    private void actualizarGastos() throws AcountDAOException, IOException {
        LocalDate desde = desdeDP.getValue();
        LocalDate hasta = hastaDP.getValue();
        if (desde != null && hasta != null) {
            gastosList.setAll(Acount.getInstance().getUserCharges().stream()
                .filter(charge -> !charge.getDate().isBefore(desde) && !charge.getDate().isAfter(hasta))
                .toList());
        }
    }

    @FXML
    private void handleBImprimir(ActionEvent event) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean proceed = job.showPrintDialog(gastosTable.getScene().getWindow());
            if (proceed) {
                Node printableNode = gastosTable;

                double scaleX = job.getJobSettings().getPageLayout().getPrintableWidth() / printableNode.getBoundsInParent().getWidth();
                double scaleY = job.getJobSettings().getPageLayout().getPrintableHeight() / printableNode.getBoundsInParent().getHeight();
                double scale = Math.min(scaleX, scaleY);

                Scale newScale = new Scale(scale, scale);
                printableNode.getTransforms().add(newScale);

                boolean success = job.printPage(printableNode);
                if (success) {
                    job.endJob();
                }

                printableNode.getTransforms().remove(newScale);
            }
        }
    }

    @FXML
    private void handleBModificarGasto(ActionEvent event) {
        this.selectedCharge = gastosTable.getSelectionModel().getSelectedItem();
        if (selectedCharge != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditarDespesa.fxml"));
                Parent root = loader.load();
                EditarDespesaController controllerEditarDespesa = loader.getController();
                controllerEditarDespesa.setMainBorderPane(mainBorderPane); // Pasar la referencia al controller
                controllerEditarDespesa.setCharge(selectedCharge); // Pasar el cargo seleccionado
                mainBorderPane.setCenter(root);

                actualizarGastos(); // Actualizar la tabla después de modificar un gasto
            } catch (IOException | AcountDAOException ex) {
                Logger.getLogger(GastosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void handleBBorrarGasto(ActionEvent event) throws AcountDAOException, IOException {
        Charge selectedCharge = gastosTable.getSelectionModel().getSelectedItem();
        if (selectedCharge != null) {
            boolean removed = Acount.getInstance().removeCharge(selectedCharge);
            if (removed) {
                gastosList.remove(selectedCharge);
            } else {
                System.out.println("Error: No se pudo eliminar el gasto.");
            }
        } else {
            System.out.println("Error: No hay ningún gasto seleccionado.");
        }
    }

    @FXML
    private void handleBInfo(ActionEvent event) {
        this.selectedCharge = gastosTable.getSelectionModel().getSelectedItem();
        if (selectedCharge != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("InfoGasto.fxml"));
                Parent root = loader.load();
                InfoGasto controllerInfoGasto = loader.getController();
                controllerInfoGasto.setMainBorderPane(mainBorderPane); // Pasar la referencia al controller
                controllerInfoGasto.setCharge(selectedCharge); // Pasar el cargo seleccionado
                mainBorderPane.setCenter(root);
            } catch (IOException ex) {
                Logger.getLogger(GastosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void updateFilteredCategories() {
        ObservableList<Charge> filteredData = FXCollections.observableArrayList();
        for (MenuItem item : menuFiltrar.getItems()) {
            if (item instanceof CheckMenuItem && ((CheckMenuItem) item).isSelected()) {
                String categoriaName = item.getText();
                for (Charge gasto : gastosList) {
                    if (gasto.getCategory().getName().equals(categoriaName)) {
                        filteredData.add(gasto);
                    }
                }
            }
        }
        gastosTable.setItems(filteredData);
    }

    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }
    
    
    private void switchCenter(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();

        // Pasar la referencia del BorderPane principal al controlador cargado
        Initializable controller = loader.getController();
        if (controller instanceof EditarDespesaController) {
            ((EditarDespesaController) controller).setMainBorderPane(mainBorderPane);
        } else if (controller instanceof InfoGasto) {
            ((InfoGasto) controller).setMainBorderPane(mainBorderPane);
        }

        mainBorderPane.setCenter(root);
    }
}
