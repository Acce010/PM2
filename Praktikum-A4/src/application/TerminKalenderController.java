package application;

import java.time.LocalDate;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import kalender.*;
import kalender.interfaces.*;


public class TerminKalenderController {

    @FXML
    private DatePicker datepick;

    @FXML
    private ToggleButton togglebutton;

    @FXML
    private TextField zyklus;

    @FXML
    private TextField anzahl;

    @FXML
    private Button submitbutton;

    @FXML
    private TextField dauer;

    @FXML
    private TextField beschreibung;

    @FXML
    private TextField widtyp;

    @FXML
    private StackPane stack_pane;
    
    @FXML
    private TextField beginn;

    @FXML
    private TerminKalenderImpl terminkalender;

    @FXML
    protected void initialize(){
    	terminkalender = new TerminKalenderImpl();
    	
    	//Ausschalten der Sachen für Termin
    	widtyp.setVisible(false);
		zyklus.setVisible(false);
		anzahl.setVisible(false);
    	
    	
    	//Wenn der Togglebutton gedrückt wird
    	togglebutton.setOnAction((ActionEvent e) -> {
    		toggle_effekt();
    	});
    	
    	submitbutton.setOnAction((ActionEvent e) -> {
    		termin_erstellen();
    	});
    }
    
    @FXML
    public void toggle_effekt(){
    	if(togglebutton.isSelected()){
    		widtyp.setVisible(true);
    		zyklus.setVisible(true);
    		anzahl.setVisible(true);
    	}else{
    		widtyp.setVisible(false);
    		zyklus.setVisible(false);
    		anzahl.setVisible(false);
    	}
    	//Damit die Werte beim Switchen nicht erhalten bleiben
    	widtyp.clear();
    	zyklus.clear();
    	anzahl.clear();
    }
    
    
    @FXML
    public void termin_erstellen(){
    	LocalDate locdate = datepick.getValue();
		TagImpl day = new TagImpl(locdate.getYear(), locdate.getMonthValue()-1, locdate.getDayOfMonth());
		DauerImpl d = new DauerImpl(Integer.valueOf(dauer.getText()));
		String[] string = beginn.getText().split("-");
		UhrzeitImpl time = new UhrzeitImpl(Integer.valueOf(string[0]),Integer.valueOf(string[1]));
		DatumImpl date = new DatumImpl(day,time);
		
    	if(togglebutton.isSelected()){
    		//Termin mit Wid Erstellen
    	}else{
    		terminkalender.eintragen(new TerminImpl(beschreibung.getText(),date,d));
    	}
    }
}