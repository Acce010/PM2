package application;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import kalender.*;
import kalender.TerminMitWiederholungImpl.WiederholungImpl;
import kalender.interfaces.*;

public class TerminKalenderController {

	@FXML
	private ToggleButton togglebutton;

	@FXML
	private TextField zyklus;

	@FXML
	private RadioButton widwoch;

	@FXML
	private TextField anzahl;

	@FXML
	private DatePicker datepick;

	@FXML
	private Button submitbutton;

	@FXML
	private TextField dauer;

	@FXML
	private TextField beschreibung;

	@FXML
	private StackPane stack_pane;

	@FXML
	private TextField beginn;

	@FXML
	private RadioButton widtag;

	@FXML
	private TerminKalenderImpl terminkalender = new TerminKalenderImpl();

	@FXML
	protected void initialize() {

		// Ausschalten der Sachen für Termin
		widtag.setVisible(false);
		widwoch.setVisible(false);
		zyklus.setVisible(false);
		anzahl.setVisible(false);

		// Wenn der Togglebutton gedrückt wird
		togglebutton.setOnAction((ActionEvent e) -> {
			toggle_effekt();
		});

		submitbutton.setOnAction((ActionEvent e) -> {
			if (!beginn.getText().isEmpty() && !beschreibung.getText().isEmpty() && !dauer.getText().isEmpty()) {
				if (!togglebutton.isSelected()) { // Nicht ausgewählt
					termin_erstellen();
					field_clear();
				} else if (!anzahl.getText().isEmpty() && !zyklus.getText().isEmpty()) {
					termin_erstellen_wid();
					field_clear();
				} else {

				}

			}
		});
	}

	@FXML
	public boolean check_field() {
		if (dauer.getText().matches("\\d+")) {
			return true;
		}
		return false;
	}

	@FXML
	public void field_clear() {
		beginn.clear();
		dauer.clear();
		beschreibung.clear();
		anzahl.clear();
		zyklus.clear();
	}

	@FXML
	public void toggle_effekt() {
		if (togglebutton.isSelected()) {
			widtag.setVisible(true);
			widwoch.setVisible(true);
			zyklus.setVisible(true);
			anzahl.setVisible(true);
		} else {
			widtag.setVisible(false);
			widwoch.setVisible(false);
			zyklus.setVisible(false);
			anzahl.setVisible(false);
			widtag.setSelected(true);
		}
		// Damit die Werte beim Switchen nicht erhalten bleiben
		zyklus.clear();
		anzahl.clear();
	}

	@FXML
	public void termin_erstellen() {
		LocalDate locdate = datepick.getValue();
		TagImpl day = new TagImpl(locdate.getYear(), locdate.getMonthValue() - 1, locdate.getDayOfMonth());
		DauerImpl d = new DauerImpl(Integer.valueOf(dauer.getText()));
		String[] string = beginn.getText().split("-");
		UhrzeitImpl time = new UhrzeitImpl(Integer.valueOf(string[0]), Integer.valueOf(string[1]));
		DatumImpl date = new DatumImpl(day, time);

		terminkalender.eintragen(new TerminImpl(beschreibung.getText(), date, d));
	}

	@FXML
	public void termin_erstellen_wid() {
		LocalDate locdate = datepick.getValue();
		TagImpl day = new TagImpl(locdate.getYear(), locdate.getMonthValue() - 1, locdate.getDayOfMonth());
		DauerImpl d = new DauerImpl(Integer.valueOf(dauer.getText()));
		String[] string = beginn.getText().split("-");
		UhrzeitImpl time = new UhrzeitImpl(Integer.valueOf(string[0]), Integer.valueOf(string[1]));
		DatumImpl date = new DatumImpl(day, time);

		String widstring = widtag.isSelected() ? "TAEGLICH" : "WOECHENTLICH";

		terminkalender.eintragen(
				new TerminMitWiederholungImpl(beschreibung.getText(), date, d, WiederholungType.valueOf(widstring),
						Integer.valueOf(anzahl.getText()), Integer.valueOf(zyklus.getText())));
	}
}