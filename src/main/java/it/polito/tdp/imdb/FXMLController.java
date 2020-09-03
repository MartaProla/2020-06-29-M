/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import it.polito.tdp.imdb.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Integer anno=this.boxAnno.getValue();
    	//eccezione se non stai selezionando l'anno
    	
    	this.model.creaGrafo(anno);
    	txtResult.appendText(String.format("#Vertici %d \n", this.model.vertexNumber()));
    	txtResult.appendText(String.format("#Archi %d", this.model.edgeNumber()));
    	
    	this.boxRegista.getItems().clear();
    	this.boxRegista.getItems().addAll(this.model.getDirettoriGrafo());
    	
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	Director d=this.boxRegista.getValue();
    	List<Vicino>adiacenti=new LinkedList<Vicino>(this.model.getVicini(d));
    	txtResult.appendText("\n Gli adiacenti al direttore: "+d+" sono: \n");
    	for(Vicino v: adiacenti) {
    		txtResult.appendText(v.getD().toString()+"-"+" # attori condivisi: "+v.getPeso()+"\n");
    	}

    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	txtResult.clear();
    	Director d=this.boxRegista.getValue();
    	if(d==null) {
    		txtResult.appendText("Inserisci il registra");
    		return;
    	}
    	
    	Integer attori_condivisi= null;
    	try {
    		attori_condivisi= Integer.parseInt(this.txtAttoriCondivisi.getText());
    	} catch (NumberFormatException e) {
    		txtResult.appendText("Inserire un numero minimo di attori nel formato corretto");
    		return;
    	}
    	txtResult.appendText("I registi affini a "+d+" sono:\n ");
    	
    	List<Director>ricorsivi=this.model.getAttoriAffini(d, attori_condivisi);
    	for(Director dir: ricorsivi) {
    		if(!dir.equals(d)) {
    			txtResult.appendText(dir.toString()+"\n");
    		}
    	}

    	
    	
    	
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	this.boxAnno.getItems().clear();
    	this.boxAnno.getItems().addAll(this.model.getAnni());
    	
    }
    
}
