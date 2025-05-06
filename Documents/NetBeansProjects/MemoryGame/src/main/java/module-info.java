module de.bbq.memorygame {
   requires javafx.controls;
   requires javafx.fxml;
   requires java.xml.bind;
   requires com.sun.xml.bind;
   requires java.base;
   
   opens de.bbq.memorygame to javafx.fxml, java.xml.bind;
   exports de.bbq.memorygame;
}