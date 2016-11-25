package kalender;

import java.util.HashMap;
import java.util.Map;

import kalender.interfaces.Datum;
import kalender.interfaces.Dauer;
import kalender.interfaces.Monat;
import kalender.interfaces.Tag;
import kalender.interfaces.Termin;
import kalender.interfaces.Woche;

public class TerminImpl implements Termin {
	
	String beschreibung;
	Datum datum;
	Dauer dauer;

	public TerminImpl(String beschreibung, Datum datum, Dauer dauer) {
		this.beschreibung = beschreibung;
		this.datum = datum;
		this.dauer = dauer;	
	}


	@Override
	public int compareTo(Termin o) {
		int name_compTo = beschreibung.compareTo(o.getBeschreibung());
		int datum_compTo = datum.compareTo(o.getDatum());
		//Wenn beide am selben Datum sind, entscheidet die Terminbeschreibung
		if(datum_compTo == 0){
			return name_compTo;
		}else{
			//Sonst entscheidet das Datum
			return datum_compTo;
		}
	}

	@Override
	public String getBeschreibung() {
		return beschreibung;
	}

	@Override
	public Datum getDatum() {
		return new DatumImpl(datum);
	}

	@Override
	public Dauer getDauer() {
		return new DauerImpl(dauer.inMinuten());
	}

	@Override
	public Termin verschiebeAuf(Datum datum) {
		return new TerminImpl(beschreibung,datum,dauer);
	}

	@Override
	public Map<Datum, Termin> termineIn(Monat monat) {
		Map<Datum, Termin> map = new HashMap<Datum, Termin>();
		if(datum.getMonat().equals(monat))
			map.put(datum, new TerminImpl(beschreibung, datum, dauer));
		return map;
	}

	@Override
	public Map<Datum, Termin> termineIn(Woche woche) {
		Map<Datum, Termin> map = new HashMap<Datum, Termin>();
		if(datum.getWoche().equals(woche))
			map.put(datum, new TerminImpl(beschreibung, datum, dauer));
		return map;
	}

	@Override
	public Map<Datum, Termin> termineAn(Tag tag) {
		Map<Datum, Termin> map = new HashMap<Datum, Termin>();
		if(datum.getTag().equals(tag))
			map.put(datum, new TerminImpl(beschreibung, datum, dauer));
		return map;
	}

	//AUTO-GENERATED
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beschreibung == null) ? 0 : beschreibung.hashCode());
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + ((dauer == null) ? 0 : dauer.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TerminImpl other = (TerminImpl) obj;
		if (beschreibung == null) {
			if (other.beschreibung != null)
				return false;
		} else if (!beschreibung.equals(other.beschreibung))
			return false;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (dauer == null) {
			if (other.dauer != null)
				return false;
		} else if (!dauer.equals(other.dauer))
			return false;
		return true;
	}
	
}
