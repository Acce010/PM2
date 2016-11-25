package kalender;

import java.util.Calendar;

import kalender.interfaces.Datum;
import kalender.interfaces.Tag;

public class TagImpl implements Tag {

	private Calendar intern; 
	
	public TagImpl(int jahr, int tagImJahr) {
		intern = Calendar.getInstance();
		intern.clear();
		intern.set(Calendar.YEAR, jahr);
		intern.set(Calendar.DAY_OF_YEAR, tagImJahr);
	}
	
	public TagImpl(int jahr, int monat, int tagImMonat) {
		intern = Calendar.getInstance();
		intern.clear();
		intern.set(Calendar.YEAR, jahr);
		intern.set(Calendar.MONTH, monat);
		intern.set(Calendar.DAY_OF_MONTH, tagImMonat);
	}
	
	public TagImpl(Tag tag) {
		this(tag.getJahr(),tag.getTagImJahr());
	}

	@Override
	public Datum getStart() {
		Calendar copy = (Calendar) intern.clone();
		copy.set(Calendar.HOUR_OF_DAY, 0);
		copy.set(Calendar.MINUTE, 0);
		copy.set(Calendar.SECOND, 0);
		DatumImpl date = new DatumImpl(new TagImpl(copy.get(Calendar.YEAR), copy.get(Calendar.MONTH), copy.get(Calendar.DAY_OF_MONTH)));
		return date;
	}

	@Override
	public Datum getEnde() {
		Calendar copy = (Calendar) intern.clone();
		copy.set(Calendar.HOUR_OF_DAY, 23);	//23 Stunden ist Grenze für den Tag
		copy.set(Calendar.MINUTE, 59);	//bei 60 min ist eine Stunde
		copy.set(Calendar.SECOND, 59);	//Bei 60 Sec ist eine min
		DatumImpl date = new DatumImpl(new TagImpl(copy.get(Calendar.YEAR), copy.get(Calendar.MONTH), copy.get(Calendar.DAY_OF_MONTH)));
		return date;
	}

	@Override
	public int compareTo(Tag o) {	
		//differenzInTagen gibt die Differenz in Tagen als long zurück, daher Casten da die Methode int zurück gibt
		return (int)differenzInTagen(o);	
	}

	@Override
	public int getJahr() {
		return intern.get(Calendar.YEAR);
	}

	@Override
	public int getMonat() {
		return intern.get(Calendar.MONTH);
	}

	@Override
	public int getTagImJahr() {
		return intern.get(Calendar.DAY_OF_YEAR);
	}

	@Override
	public int getTagImMonat() {
		return intern.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public long differenzInTagen(Tag other) {
		return getTagImJahr()-other.getTagImJahr();
	}

	@Override
	public Calendar inBasis() {
		return (Calendar)intern.clone(); //Type-Casting sollte gehen
	}

	@Override
	public String toString(){
		return String.format("Tag %d" + "[" + getStart() + "," + getEnde() +"]", getTagImJahr());
	}
	
}
