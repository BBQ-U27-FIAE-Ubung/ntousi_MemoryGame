package de.bbq.wochenberichte;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class AiFragenController {

    @FXML
    TextArea prompt;

    @FXML
    public void absenden(ActionEvent e) throws Exception {
        String text = prompt.getText();

        URL url = new URL("https://bbq-wochenberichte-ai.gentlent.workers.dev");
        URLConnection con = url.openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "text/plain");
        con.setRequestProperty("Content-Length", text.length() + "");

        try {
            //Daten "schreiben"
            DataOutputStream schreiber = new DataOutputStream(con.getOutputStream());
            schreiber.writeBytes(text);

            //Daten "lesen"
            InputStreamReader leser = new InputStreamReader(con.getInputStream());
            JAXBContext ctx = JAXBContext.newInstance(Wochenbericht.class);
            Unmarshaller unmar = ctx.createUnmarshaller();
            Wochenbericht bericht = (Wochenbericht) unmar.unmarshal(leser);

            bericht.setName(App.primary.name.getText());
            bericht.setDatum(App.primary.montag);
            bericht.setUmschulung(App.primary.umschulung.getText());

            App.primary.berichtLaden(bericht);

        } catch (Exception err) {
        }

        //Fenster schließen
        PrimaryController.aiFenster.close();

    }

}
