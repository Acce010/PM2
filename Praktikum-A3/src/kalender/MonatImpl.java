package kalender;

import java.util.Calendar;

import kalender.interfaces.Datum;
import kalender.interfaces.Dauer;
import kalender.interfaces.Monat;
import kalender.interfaces.Tag;
import kalender.interfaces.Uhrzeit;
import kalender.interfaces.Woche;

public class MonatImpl implements Monat {

	private Calendar intern;

	public MonatImpl(int jahr, int monat) {
		intern = Calendar.getInstance();
		intern.clear();
		intern.set(Calendar.YEAR, jahr); //Setzt die Klassenkonstante YEAR in intern auf den Wert von jahr
		intern.set(Calendar.MONTH, monat); //Setzt den Monat
	}

	@Override
	public Datum getStart() {
		Calendar copy = (Calendar) intern.clone();
		copy.set(Calendar.MONTH, Calendar.JANUARY);
		//Oder copy.set(Calendar.DAY_OF_MONTH, 1)
		DatumImpl date = new DatumImpl(new TagImpl(copy.get(Calendar.YEAR),copy.get(Calendar.MONTH),copy.get(Calendar.DAY_OF_MONTH)));
		return date;
	}

	@Override
	public Datum getEnde() {
		Calendar copy = (Calendar) intern.clone();
		copy.set(Calendar.MONTH, Calendar.DECEMBER);
		//Oder copy.set(Calendar.DAY_OF_MONTH, copy.getActualMaximum(Calendar.DAY_OF_MONTH));
		DatumImpl date = new DatumImpl(new TagImpl(copy.get(Calendar.YEAR),copy.get(Calendar.MONTH),copy.get(Calendar.DAY_OF_MONTH)));
		return date;
	}

	@Override
	public int getMonat() {
		return intern.get(Calendar.MONTH);
	}

	@Override
	public int getJahr() {
		return intern.get(Calendar.YEAR);
	}

	@Override
	public String toString(){
		return String.format("Monate %d "+"["+getStart()+","+getEnde()+"]", getMonat() + 1);
	}
	
}
