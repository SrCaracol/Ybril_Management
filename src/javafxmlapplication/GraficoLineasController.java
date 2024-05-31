package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import model.Acount;
import model.AcountDAOException;
import model.Charge;

public class GraficoLineasController implements Initializable {

    @FXML
    private LineChart<String, Number> graficoLineas;
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
                Logger.getLogger(GraficoLineasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GraficoLineasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        hastaDP.setOnAction(event -> {
            try {
                actualizarGrafico();
            } catch (AcountDAOException ex) {
                Logger.getLogger(GraficoLineasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GraficoLineasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        try {
            actualizarGrafico();
        } catch (AcountDAOException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(GraficoLineasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleBImprimir(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(graficoLineas.getScene().getWindow())) {
            boolean success = printerJob.printPage(graficoLineas);
            if (success) {
                printerJob.endJob();
            }
        }
    }

    private void actualizarGrafico() throws AcountDAOException, IOException {
    LocalDate desde = desdeDP.getValue();
    LocalDate hasta = hastaDP.getValue();

    List<Charge> chargesList = Acount.getInstance().getUserCharges();
    ArrayList<Charge> charges = new ArrayList<>(chargesList);

    Collections.sort(charges, Comparator.comparing(Charge::getDate));

    Map<LocalDate, Double> gastosPorFecha = charges.stream()
            .filter(charge -> !charge.getDate().isBefore(desde) && !charge.getDate().isAfter(hasta))
            .collect(Collectors.groupingBy(
                    Charge::getDate,
                    Collectors.summingDouble(Charge::getCost)
            ));

    // Generar una lista de todas las fechas en el rango
    List<LocalDate> fechasEnRango = new ArrayList<>();
    LocalDate fechaTemporal = desde;
    while (!fechaTemporal.isAfter(hasta)) {
        fechasEnRango.add(fechaTemporal);
        fechaTemporal = fechaTemporal.plusDays(1); // Avanzar al siguiente día
    }

    // Crear un mapa con todas las fechas en el rango y sus gastos asociados
    Map<LocalDate, Double> gastosPorFechaCompleto = new TreeMap<>();
    for (LocalDate fecha : fechasEnRango) {
        gastosPorFechaCompleto.put(fecha, gastosPorFecha.getOrDefault(fecha, 0.0));
    }

    graficoLineas.getData().clear();

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Gastos por Fecha");

    gastosPorFechaCompleto.forEach((fecha, total) -> {
        series.getData().add(new XYChart.Data<>(fecha.toString(), total));
    });

    graficoLineas.getData().add(series);
    }

}
