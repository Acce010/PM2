package kalender;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kalender.interfaces.Datum;
import kalender.interfaces.Monat;
import kalender.interfaces.Tag;
import kalender.interfaces.Termin;
import kalender.interfaces.TerminKalender;
import kalender.interfaces.Woche;

public class TerminKalenderImpl implements TerminKalender {
	
	List<Termin> kalender = new LinkedList<Termin>();

	@Override
	public boolean eintragen(Termin termin) {
		kalender.add(termin);
		return true;
	}

	@Override
	public void verschiebenAuf(Termin termin, Datum datum) {
	Termin vers_term = kalender.stream().filter(ele -> ele.equals(termin)).findFirst().get();
	vers_term.verschiebeAuf(datum);
	}

	@Override
	public boolean terminLoeschen(Termin termin) {
		//TODO
		kalender = kalender.stream().filter(ele -> !(ele.equals(termin))).collect(Collectors.toList());
		return true;
	}

	@Override
	public boolean enthaeltTermin(Termin termin) {
		if(kalender.stream().equals(termin))
			return true;
		return false;
	}

	@Override
	public Map<Datum, List<Termin>> termineFuerTag(Tag tag) {
		Map<Datum, List<Termin>> tabelle = new HashMap<Datum, List<Termin>>();
		List<Termin> return_list = kalender.stream().filter(termin -> termin.getDatum().getTag().equals(tag)).collect(Collectors.toList()); //Verwenden von getStart damit wir zwei Datums Objekte verlgiechen
		tabelle.put(tag.getStart(), return_list);
		return tabelle;
	}

	@Override
	public Map<Datum, List<Termin>> termineFuerWoche(Woche woche) {
		Map<Datum, List<Termin>> tabelle = new HashMap<Datum, List<Termin>>();
		List<Termin> return_list = kalender.stream().filter(termin -> termin.getDatum().getWoche().equals(woche)).collect(Collectors.toList());
		tabelle.put(woche.getStart(), return_list);
		return tabelle;
	}

	@Override
	public Map<Datum, List<Termin>> termineFuerMonat(Monat monat) {
		Map<Datum, List<Termin>> tabelle = new HashMap<Datum, List<Termin>>();
		List<Termin> return_list = kalender.stream().filter(termin -> termin.getDatum().getMonat().equals(monat)).collect(Collectors.toList());
		tabelle.put(monat.getStart(), return_list);
		return tabelle;
	}

}
