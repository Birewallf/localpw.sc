package bwg;

import bwg.netcore.InvalidHttpsURLConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class Controller {
    @FXML
    private ToggleButton exit;
    //-
    @FXML
    private TextField zbpViewTextField;
    @FXML
    private Label zbpViewLabel;
    @FXML
    private TextField ppmViewTextField;
    @FXML
    private Label ppmViewLabel;
    @FXML
    private TextField smViewTextField;
    @FXML
    private Label smViewLabel;

    @FXML
    private Label ppmCalcOutLabel;
    @FXML
    private Label zbpCalcOutLabel;
    @FXML
    private Label smCalcOutLabel;
    @FXML
    private Label profitViewLabel;

    //-

    //prices
    private int ppmPrice = 0;
    private int zbpPrice = 0;
    private int smPrice = 0;

    @FXML
    public void initialize(){
        ppmViewTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                ppmViewTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            ppmPrice = (ppmViewTextField.getText().equals("")) ? 0 : Integer.parseInt(ppmViewTextField.getText());
            ppmViewLabel.setText("5 * " + numNormalView(ppmPrice));

            calcAll();
        });
        zbpViewTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                zbpViewTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            zbpPrice = (zbpViewTextField.getText().equals("")) ? 0 : Integer.parseInt(zbpViewTextField.getText());
            zbpViewLabel.setText("-648 000 + 1 296 * " + numNormalView(zbpPrice));

            calcAll();
        });
        smViewTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                smViewTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            smPrice = (smViewTextField.getText().equals("")) ? 0 : Integer.parseInt(smViewTextField.getText());
            smViewLabel.setText("-108 000 + 108 * " + numNormalView(smPrice));

            calcAll();
        });
    }

    private void calcAll() {
        int ppmCalcAll = 5 * ppmPrice;
        int zbpCalcAll = -648000 + 1296 * zbpPrice;
        int smCalcAll = -108000 + 108 * smPrice;

        int smProfit = smCalcAll - ppmCalcAll;
        int zbpProfit = zbpCalcAll - ppmCalcAll;


        ppmCalcOutLabel.setText(""+ numNormalView(ppmCalcAll));
        zbpCalcOutLabel.setText(""+ numNormalView(zbpCalcAll));
        smCalcOutLabel.setText(""+ numNormalView(smCalcAll));

        profitViewLabel.setText(
                (smProfit > zbpProfit)
                        ? numNormalView(smProfit) + new String((" (Серебряная монена)").getBytes(), StandardCharsets.UTF_8)
                        : numNormalView(zbpProfit) + new String((" (Знак Боевой песни)").getBytes(), StandardCharsets.UTF_8)
                );
    }

    /**
     * Replace numeric
     * @param num int numeric
     * @return normal string
     */
    private String numNormalView(int num) {
        StringBuilder string = new StringBuilder();
        int iter = 0;
        while(num > 0) {
            if (iter > 2) {
                string.insert(0, " ");
                iter = 0;
            }
            string.insert(0, num % 10);
            num /= 10;
            iter++;
        }
        return string.toString();
    }



    public void onExit() {
        System.exit(0);
    }
    public void onMin() {
        Stage stage = (Stage)exit.getScene().getWindow();
        // is stage minimizable into task bar. (true | false)
        stage.setIconified(true);
    }


    /**
     * Get Price
     */
    @FXML
    private Button actionPWCATSButton;
    public void actionPWCATSGetInfo(){
        new Thread(() -> {
            actionPWCATSButton.setDisable(true);

            Platform.runLater(()->{
                ppmViewTextField.setText(getRequest("Пояс повелителей миража", false));
                zbpViewTextField.setText(getRequest("Знак Боевой песни", true));
                smViewTextField.setText(getRequest("Серебряная монета", true));
            });
            actionPWCATSButton.setDisable(false);
        }).start();
    }
    private String getRequest(String thingName, boolean sbFlag) {
        String baseAddress = "https://pwcats.info/sargas/search?item=";
        String address = baseAddress + thingName.replaceAll(" ", "+");
        //address = "https://pwcats.info";
        thingName = "0";
        try {
            URL url = new URL(address);

            InvalidHttpsURLConnection invalidHttpsURLConnection = new InvalidHttpsURLConnection();
            HttpsURLConnection connection = invalidHttpsURLConnection.getConnection(url);

            InputStream is = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            char[] buffer = new char[256];
            int rc;

            StringBuilder sb = new StringBuilder();

            while ((rc = reader.read(buffer)) != -1)
                sb.append(buffer, 0, rc);

            reader.close();
            //System.out.print(sb);

            thingName = (sbFlag) ? getBuy(sb.toString()) : getSell(sb.toString());
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return thingName;
    }
    private String getSell(String text) {
        text = text.substring(text.indexOf("sell\">")+6);
        text = text.substring(0, text.indexOf("<"));
        return (!text.contains("Europe/Moscow")) ? text : "N/D";
    }
    private String getBuy(String text) {
        text = text.substring(text.indexOf("buy\">")+5);
        text = text.substring(0, text.indexOf("<"));
        return (!text.contains("Europe/Moscow")) ? text : "N/D";
    }
}
