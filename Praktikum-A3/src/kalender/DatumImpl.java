package kalender;

import java.util.Calendar;

import kalender.interfaces.Datum;
import kalender.interfaces.Dauer;
import kalender.interfaces.Monat;
import kalender.interfaces.Tag;
import kalender.interfaces.Uhrzeit;
import kalender.interfaces.Woche;

public class DatumImpl implements Datum {


	private Calendar intern;
	
	public DatumImpl(Tag tag){
		this(tag, new UhrzeitImpl());
	}
	public DatumImpl(Tag tag, Uhrzeit uhrzeit ) {
		// da ein Datum aus Tag.Monat.Jahr besteht. und die einfachste Uhrzeit ist Stunde:Minute
		intern = Calendar.getInstance();
		intern.clear();
		intern.set(Calendar.YEAR, tag.getJahr());
		intern.set(Calendar.MONTH, tag.getMonat());
		intern.set(Calendar.HOUR_OF_DAY, uhrzeit.getStunde());
		intern.set(Calendar.MINUTE, uhrzeit.getMinuten());
	}

	public DatumImpl(Datum d) {
		this(d.inBasis());
	}

	private DatumImpl(Calendar intern) {
		this.intern = intern;
	}
	
	
	@Override
	public int compareTo(Datum o) { 
		//Abstand vergleicht wie weit zwei Datums Objekte aus einander liegen.
		//Mit inMinuten bekommen wir den Wert. 
		//compareTo bei Comparable schaut nur ob die Zahl negativ, null oder positiv ist, daher nicht den exakten Wert.
		return this.abstand(o).inMinuten();
	}

	@Override
	public Tag getTag() {
		TagImpl day = new TagImpl(intern.get(Calendar.YEAR), intern.get(Calendar.DAY_OF_YEAR));  //Contructor/2 ist mit Jahr und TagImJahr
		return day;
	}

	@Override
	public Woche getWoche() {
		WocheImpl week = new WocheImpl(intern.get(Calendar.YEAR),intern.get(Calendar.MONTH),intern.get(Calendar.WEEK_OF_MONTH));
		return week;
	}

	@Override
	public Monat getMonat() {
		MonatImpl month = new MonatImpl(intern.get(Calendar.YEAR),intern.get(Calendar.MONTH));
		return month;
	}

	@Override
	public Uhrzeit getUhrzeit() {
		UhrzeitImpl time = new UhrzeitImpl(intern.get(Calendar.HOUR_OF_DAY),intern.get(Calendar.MINUTE)); 
		return time;
	}

	@Override
	public int getJahr() {
		return intern.get(Calendar.YEAR);
	}

	@Override
	public int getTagImMonat() {
		return intern.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public int getTagImJahr() {
		return intern.get(Calendar.DAY_OF_YEAR);
	}

	@Override
	public int getWocheImMonat() {
		return intern.get(Calendar.WEEK_OF_MONTH);
	}

	@Override
	public int getWocheImJahr() {
		return intern.get(Calendar.WEEK_OF_YEAR);
	}

	@Override
	public int getMonatImJahr() {
		return intern.get(Calendar.MONTH);
	}

	@Override
	public Datum add(Dauer dauer) {
		Calendar clone = (Calendar) intern.clone();	//Klonen des Kalenders
		clone.add(Calendar.MINUTE, dauer.inMinuten());	//Hinzufügen der Minuten die dauer übergiebt
		Datum date = new DatumImpl(clone); //Ein neues Datum_Objekt erstellen
		return date;
	}

	@Override
	public Datum sub(Dauer dauer) {
		Calendar clone = (Calendar) intern.clone();	//Klonen des Kalenders
		clone.add(Calendar.MINUTE, -(dauer.inMinuten()));	//Hinzufügen der Minuten als Minus, daher implizite Subtraktion
		Datum date = new DatumImpl(clone); //Ein neues Datum_Objekt erstellen
		return date;
	}

	@Override
	public Dauer abstand(Datum d) {
		//Erstellt ein Dauer-Objekt mit der differenz der Minuten beider Objekte
		DauerImpl dauer = new DauerImpl(this.inMinuten()-d.inMinuten());
		return dauer;
	}

	@Override
	public long differenzInTagen(Datum d) {
		//Abstand berrechnen und in Tage umrechenen und ausgeben.
		return this.abstand(d).inTagen();
	}

	@Override
	public int inMinuten() {
		long mili_sec = intern.getTimeInMillis();
		return (int) (mili_sec/60000);
	}

	@Override
	public Calendar inBasis() {
		return (Calendar) intern.clone();
	}

	@Override
	public String toString(){
		return String.format("%d.%d.%d", getTagImMonat(), getMonatImJahr()+1, getJahr());
	}
	
}
