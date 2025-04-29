package de.bbq.wochenberichte;

import java.io.File;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class PrimaryController {
    @FXML
    public AnchorPane blatt;
    
    @FXML
    public TextField    wocheVom,
                        wocheBis,
                        jahr,
                        datumAuszubildender,
                        datumAusbildender,
                        stunden1,
                        stunden2,
                        stunden3,
                        stunden4,
                        stunden5,
                        gesamtStunden,
                        name,
                        umschulung;
    
    @FXML
    public MenuButton menuButton;
    
    @FXML
    public Label tag1, tag2, tag3, tag4, tag5;
    
    public LocalDate montag;
    
    @FXML
    public MenuItem menuSpeichernUndDrucken, 
                    menuSpeichern, 
                    menuDrucken, 
                    menuLaden;
    
    @FXML
    public void speichernUndDrucken(ActionEvent e) throws Exception {
        speichern(e);
        //  und  //
        drucken(e);
    }
    
    @FXML
    public void drucken(ActionEvent e) {
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob printerJob = PrinterJob.createPrinterJob(printer);
        
        // Fixt unsere Abstände / Verschiebung
        PageLayout pageLayout = printer.createPageLayout(
                Paper.A4, PageOrientation.PORTRAIT, 0, 0, 0, 0
        );
        printerJob.getJobSettings().setPageLayout(pageLayout);
        
        // Menü-Button Ausblendung
        menuButton.setVisible(false);
        
        // Eingabe-Focus entfernen
        App.scene.getRoot().requestFocus();
        
        // Druckauftrag mit Skalierungs-Fix
        double scaleX = pageLayout.getPrintableWidth() / blatt.snapshot(null, null).getWidth();
        double scaleY = pageLayout.getPrintableHeight() / blatt.snapshot(null, null).getHeight();
        double scale = Math.min(scaleX, scaleY);
        blatt.getTransforms().add(new Scale(scale, scale));
        
        printerJob.printPage(blatt);
        printerJob.endJob();
        blatt.getTransforms().remove(blatt.getTransforms().size() - 1);
        menuButton.setVisible(true);
        
    }
    
    @FXML
    public void speichern(ActionEvent e) throws Exception {
        // Objekterstellung
        Wochenbericht bericht = new Wochenbericht();
        bericht.setName(name.getText());
        bericht.setUmschulung(umschulung.getText());
        bericht.setDatum(montag);
        
        String[] stunden = {
            stunden1.getText(), stunden2.getText(),
            stunden3.getText(), stunden4.getText(),
            stunden5.getText()
        };
        bericht.setStundenFelder(stunden);
        
        ArrayList<String> eingaben = new ArrayList<>();
        for (Node element : blatt.getChildren()) {
            if (element instanceof TextField) {
                if (element.getStyleClass().contains("eintrag")) {
                    TextField text = (TextField) element;
                    eingaben.add(text.getText());
                }
            }
        }
        bericht.setEingabeFelder(eingaben.toArray(new String[0]));
                
        // Dateiwahl
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wochenbericht speichern");
        fileChooser.getExtensionFilters().add(
            new ExtensionFilter("IHK Wochenbericht", "*.ihk")
        );
        File file = fileChooser.showSaveDialog(App.scene.getWindow());
        
        // Speicherung
        JAXBContext ctx = JAXBContext.newInstance(Wochenbericht.class);
        Marshaller mar = ctx.createMarshaller();
        mar.marshal(bericht, file);
    }
    
    @FXML
    public void laden(ActionEvent e) throws Exception {
        // Dateiwahl
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wochenbericht laden");
        fileChooser.getExtensionFilters().add(
            new ExtensionFilter("IHK Wochenbericht", "*.ihk")
        );
        File file = fileChooser.showOpenDialog(App.scene.getWindow());
        // Unmarshalling
        JAXBContext ctx = JAXBContext.newInstance(Wochenbericht.class);
        Unmarshaller unmar = ctx.createUnmarshaller();
        Wochenbericht bericht = (Wochenbericht) unmar.unmarshal( file);
        
        berichtLaden(bericht);
    }
    
    public void berichtLaden (Wochenbericht bericht){
        //Objekterstellung
        name.setText(bericht.getName());
        umschulung.setText(bericht.getUmschulung());
        montag = bericht.getDatum();
        wocheVom.setText(montag.format(
                 DateTimeFormatter.ofPattern("dd.MM")
        ));
        jahr.setText(montag.format(
            DateTimeFormatter.ofPattern("yyyy")
        ));
        stunden1.setText(bericht.getStundenFelder()[0]);
        stunden2.setText(bericht.getStundenFelder()[1]);
        stunden3.setText(bericht.getStundenFelder()[2]);
        stunden4.setText(bericht.getStundenFelder()[3]);
        stunden5.setText(bericht.getStundenFelder()[4]);

        String [] eingaben = bericht.getEingabeFelder();
        int i = 0;
        for (Node element : blatt.getChildren()) {
            if (element instanceof TextField) {
                if (element.getStyleClass().contains("eintrag")) {
                    TextField text = (TextField) element;
                    text.setText(eingaben[i]);
                    i++;
                }
            }
        }
        updateDatum(null);
        updateStunden(null);
    }
    
    @FXML
    public void updateStunden(KeyEvent e) {
        TextField[] stunden = {stunden1, stunden2, stunden3, stunden4, stunden5};
        double summe = 0;
        
        for (TextField stunde : stunden) {
            try {
                // Versucht den Double-Wert zu parsen
                summe += Double.parseDouble(stunde.getText());
            } catch (Exception err) {
                // Wenn fehlschlägt, dann setzen wir 0
                stunde.setText("0");
            }
        }
        
        DecimalFormat df = new DecimalFormat("0.#");
        gesamtStunden.setText(df.format(summe));
    }
    
    public void updateWochentage() {
        Label[] tage = {tag1, tag2, tag3, tag4, tag5};
        
        for (int i = 0; i < tage.length; i++) {
            tage[i].setText(
                    montag.plusDays(i).format(
                            DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    )
            );
        }
    }
    
    @FXML
    public void updateDatum(KeyEvent e) {
        try {
            LocalDate vonDatum = LocalDate.parse(
                wocheVom.getText() + jahr.getText(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy")
            );

            montag = vonDatum.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate freitag = montag.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
            wocheVom.setText(montag.format(DateTimeFormatter.ofPattern("dd.MM.")));
            wocheBis.setText(freitag.format(DateTimeFormatter.ofPattern("dd.MM.")));
            
            updateWochentage();
        } catch (Exception err) {}
    }
    
    public static Stage aiFenster;
    
    @FXML
    public void aiFragen(ActionEvent e ) throws Exception {
        aiFenster = new Stage();
        Scene scene = new Scene(App.loadFXML("aiFragen"),400, 400);
        
        aiFenster.setScene(scene);
        aiFenster.setTitle("Mit AI generieren...");
        aiFenster.show();
    }
    
    public void initialize() {
        // Tastenkombinationen
        menuSpeichern.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        menuDrucken.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        menuLaden.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        menuSpeichernUndDrucken.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        
        // Eigentliche Inhalte
        LocalDate heute = LocalDate.now();
        montag = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate freitag = montag.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        wocheVom.setText(montag.format(DateTimeFormatter.ofPattern("dd.MM.")));
        wocheBis.setText(freitag.format(DateTimeFormatter.ofPattern("dd.MM.")));
        jahr.setText(montag.format(DateTimeFormatter.ofPattern("yyyy")));
        
        String datum = heute.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        datumAusbildender.setText(datum);
        datumAuszubildender.setText(datum);
        
        updateWochentage();
        
        // Skalierung
        double hoeheScene = App.hoeheScene;
        double hoeheBlatt = blatt.getPrefHeight();
        double skalierung = hoeheScene / hoeheBlatt;
       
        Scale scale = Transform.scale(skalierung, skalierung);
        scale.setPivotX(0);
        scale.setPivotY(0);
        
        blatt.getTransforms().add(scale);
    }
    
}
