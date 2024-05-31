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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import model.Acount;
import model.AcountDAOException;
import model.Charge;

public class GraficoQuesosController implements Initializable {

    @FXML
    private DatePicker desdeDP;
    @FXML
    private DatePicker hastaDP;
    @FXML
    private Button bImprimir;
    @FXML
    private PieChart tablaQuesos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDate today = LocalDate.now();
        desdeDP.setValue(today.minusMonths(1));  
        hastaDP.setValue(today);

        desdeDP.setOnAction(event -> {
            try {
                actualizarTablaQuesos();
            } catch (AcountDAOException ex) {
                Logger.getLogger(GraficoQuesosController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GraficoQuesosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        hastaDP.setOnAction(event -> {
            try {
                actualizarTablaQuesos();
            } catch (AcountDAOException ex) {
                Logger.getLogger(GraficoQuesosController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GraficoQuesosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        try {
            actualizarTablaQuesos();
        } catch (AcountDAOException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(GraficoQuesosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleBImprimir(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(tablaQuesos.getScene().getWindow())) {
            boolean success = printerJob.printPage(tablaQuesos);
            if (success) {
                printerJob.endJob();
            }
        }
    }

    private void actualizarTablaQuesos() throws AcountDAOException, IOException {
        LocalDate desde = desdeDP.getValue();
        LocalDate hasta = hastaDP.getValue();

        List<Charge> charges = Acount.getInstance().getUserCharges();
        Map<String, Double> gastosPorCategoria = charges.stream()
                .filter(charge -> !charge.getDate().isBefore(desde) && !charge.getDate().isAfter(hasta))
                .collect(Collectors.groupingBy(
                        charge -> charge.getCategory().getName(),
                        Collectors.summingDouble(Charge::getCost)
                ));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        gastosPorCategoria.forEach((categoria, total) -> {
            pieChartData.add(new PieChart.Data(categoria, total));
        });

        tablaQuesos.setData(pieChartData);
    }
}
