package kalender;

import java.util.Calendar;

import kalender.interfaces.Datum;
import kalender.interfaces.Dauer;
import kalender.interfaces.Monat;
import kalender.interfaces.Tag;
import kalender.interfaces.Uhrzeit;
import kalender.interfaces.Woche;

public class DauerImpl implements Dauer {

	private int minuten;
	
	public DauerImpl(Datum d1, Datum d2) {
		this(d1.abstand(d2).inMinuten()); //Berechnet die Dauer von d1 zu d2 gibt aber eine dauer zurück, daher umwandlung in Minuten
	}

	public DauerImpl(int minuten) {
		this(0,minuten);
	}
	
	public DauerImpl(int stunden, int minuten) {
		this(0, stunden, minuten);
	}

	public DauerImpl(int tage, int stunden, int minuten) {
		this.minuten = minuten + (stunden*60) + (tage*1440);
	}

	@Override
	public int compareTo(Dauer o) {
		if(minuten>o.inMinuten()){
			return 1;
		}else if(minuten<o.inMinuten()){
			return -1;
		}else{
			return 0;
		}
	}


	@Override
	public int inMinuten() {
		return minuten;
	}

	@Override
	public int inStunden() {
		return minuten/60;
	}

	@Override
	public int inTagen() {
		return minuten/1440;
	}

	@Override
	public int inWochen() {
		return minuten/10080;
	}

	@Override
	public int anteilMinuten() {
		int left_min = ((minuten%10080)%1440)%60;
		return left_min;
	}

	@Override
	public int anteilStunden() {
		int left_min = (minuten%10080)%1440;
		return left_min/60;
	}

	@Override
	public int anteilTage() {
		int left_min = minuten%10080;
		return left_min/1440;
	}

	@Override
	public int anteilWochen() {
		return minuten/10080;
	}
	

	@Override
	public String toString() { //String ausgabe als Minuten
		return String.valueOf(minuten);
	}
	
	

}
