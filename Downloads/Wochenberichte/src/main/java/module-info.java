module de.bbq.wochenberichte {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires java.base;

    opens de.bbq.wochenberichte to javafx.fxml, java.xml.bind;
    exports de.bbq.wochenberichte;
}
