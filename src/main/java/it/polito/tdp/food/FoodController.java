/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Creazione grafo...\n\n");
    	
    	try {
    		int numeroPorzioni = Integer.parseInt(this.txtPorzioni.getText());
    		this.model.creaGrafo(numeroPorzioni);
    		this.txtResult.appendText(String.format("GRAFO CREATO! Sono presenti %d VERTICI e %d ARCHI.\n", this.model.nVertici(), this.model.nArchi()));
    		
    		this.boxFood.getItems().clear();
    		this.boxFood.getItems().addAll(this.model.vertici());
    	} catch(NumberFormatException e) {
    		this.txtResult.appendText("Errore! Nella casella di testo 'Porzioni' va inserito un valore numerico intero!\n");
    		return;
    	}
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Analisi calorie...\n\n");
    	
    	Food selezionato = this.boxFood.getValue();
    	if(selezionato == null) {
    		this.txtResult.appendText("Errore! Non hai selezionato alcun cibo!\n");
    		return;
    	}
    	
    	Map<Food, Double> max = this.model.calorieCongiunteMassime(selezionato);
    	int i = 0;
    	for(Food f : max.keySet()) {
    		if(i < 5)
    			this.txtResult.appendText(String.format("%s %.2f\n", f.toString(), max.get(f)));
    		else
    			return;
    		i++;
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Simulazione...\n\n");
    	
    	Food selezionato = this.boxFood.getValue();
    	if(selezionato == null) {
    		this.txtResult.appendText("Errore! Non hai selezionato alcun cibo!\n");
    		return;
    	}
    	
    	int K;
    	try {
    		K = Integer.parseInt(this.txtK.getText());
    		if(K < 1 || K > 10) {
    			this.txtResult.appendText("Errore! K deve essere un numero compreso tra 1 e 10!\n");
    			return;
    		}
    	} catch(NumberFormatException e) {
    		this.txtResult.appendText("Errore! K deve essere un numero\n");
    		return;
    	}
    	
    	String messaggio = model.simula(selezionato, K);
    	this.txtResult.appendText(messaggio);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
