package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import model.Acount;
import model.AcountDAOException;
import model.Charge;

public class GraficoBarrasController implements Initializable {

    @FXML
    private BarChart<String, Number> graficoBarras;
    @FXML
    private NumberAxis yCantidad;
    @FXML
    private CategoryAxis xCategoria;
    @FXML
    private DatePicker desdeDP;
    @FXML
    private DatePicker hastaDP;
    @FXML
    private Button bImprimir;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDate today = LocalDate.now();
        desdeDP.setValue(today.minusMonths(1));  
        hastaDP.setValue(today);

        desdeDP.setOnAction(event -> {
            try {
                actualizarGrafico();
            } catch (AcountDAOException ex) {
                Logger.getLogger(GraficoBarrasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GraficoBarrasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        hastaDP.setOnAction(event -> {
            try {
                actualizarGrafico();
            } catch (AcountDAOException ex) {
                Logger.getLogger(GraficoBarrasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GraficoBarrasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        try {
            actualizarGrafico();
        } catch (AcountDAOException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(GraficoBarrasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleBImprimir(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(graficoBarras.getScene().getWindow())) {
            boolean success = printerJob.printPage(graficoBarras);
            if (success) {
                printerJob.endJob();
            }
        }
    }

    private void actualizarGrafico() throws AcountDAOException, IOException {
        LocalDate desde = desdeDP.getValue();
        LocalDate hasta = hastaDP.getValue();

        List<Charge> charges = Acount.getInstance().getUserCharges();
        Map<String, Double> gastosPorCategoria = charges.stream()
                .filter(charge -> !charge.getDate().isBefore(desde) && !charge.getDate().isAfter(hasta))
                .collect(Collectors.groupingBy(
                        charge -> charge.getCategory().getName(),
                        Collectors.summingDouble(Charge::getCost)
                ));

        graficoBarras.getData().clear();

        ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
        gastosPorCategoria.forEach((categoria, total) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(categoria);
            series.getData().add(new XYChart.Data<>(categoria, total));
            barChartData.add(series);
        });

        graficoBarras.setData(barChartData);
    }
}
