package kalender;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kalender.TerminMitWiederholungImpl.WiederholungImpl;
import kalender.interfaces.Datum;
import kalender.interfaces.DatumsGroesse;
import kalender.interfaces.Dauer;
import kalender.interfaces.IntervallIterator;
import kalender.interfaces.Monat;
import kalender.interfaces.Tag;
import kalender.interfaces.Termin;
import kalender.interfaces.TerminMitWiederholung;
import kalender.interfaces.Wiederholung;
import kalender.interfaces.Woche;

public class TerminMitWiederholungImpl extends TerminImpl implements TerminMitWiederholung {

	private Wiederholung wdh;

 // TODO Konstruktorprobleme auflösen
	public TerminMitWiederholungImpl(String beschreibung, Datum start, Dauer dauer, WiederholungType type, int anzahl,
			int zyklus) {
			super(beschreibung, start, dauer);	//TerminImpl konstruktor aufruf
			this.wdh = new WiederholungImpl(type,anzahl, zyklus);
	}

	public TerminMitWiederholungImpl(String beschreibung, Datum start, Dauer dauer, Wiederholung wdh) {
		//super(beschreibung, start, dauer); this.wdh=wdh;
		this(beschreibung, start, dauer, wdh.getType(), wdh.anzahl(),wdh.getZyklus()); //Kaskadischer Aufruf
	}
	
	
	public Wiederholung getWdh() {
		return new WiederholungImpl(wdh);
	}


	@Override
	public Map<Datum, Termin> termineIn(Monat monat) {
		Map<Datum, Termin> termine = termineFuer(monat);
		return (termine != null) ? termine : (new HashMap<Datum, Termin>()); //Falls kein Termin exisitert eine Leere Map zur�ck geben
		//return termineFuer(monat) ? termineFuer(monat) : (new HashMap<Datum, Termin>());
	}

	@Override
	public Map<Datum, Termin> termineIn(Woche woche) {
		Map<Datum, Termin> termine = termineFuer(woche);
		return (termine != null) ? termine : (new HashMap<Datum, Termin>());
	}

	@Override
	public Map<Datum, Termin> termineAn(Tag tag) {
		Map<Datum, Termin> termine = termineFuer(tag);
		return (termine != null) ? termine : (new HashMap<Datum, Termin>());
	}

	
	/**
	 * Beispiel für den naiven Iterator, der alle Wiederholungen explizit aufzaehlt
	 */
	@Override
	public Iterator<Termin> iterator() {
		return new Iterator<Termin>() {
			private TerminMitWiederholung current = null;
			private int howManySeen = 0;

			@Override
			public boolean hasNext() {
				return howManySeen <= wdh.anzahl();
			}

			@Override
			public Termin next() {
				if (current == null) {
					current = TerminMitWiederholungImpl.this;
				} else {
					current = new TerminMitWiederholungImpl(getBeschreibung(), current.getWdh().naechstesDatum(),
							getDauer(), current.getWdh().sub(1));
				}
				howManySeen += 1;
				return current;
			}
		};
	}


	@Override
	public IntervallIterator<Datum> intervallIterator(int von, int bis) {
		return new IntervallIterator<Datum>() {
			private int upperbound = bis;
			private int cursor = von;
			
			@Override
			public boolean hasNext() {
				//Pr�fen ob der Cursor am Ende ist und ob der Cursor nicht das Max Intervall �berschritten hat.
				return (cursor <= upperbound) && (cursor <= TerminMitWiederholungImpl.this.getWdh().maxIntervallIndex()); //Aufruf der obigen Klasse aus unter Klasse
			}

			@Override
			public Datum next() {
				//Ausgabe des n�chsten Datums im TerminMitWiederholungs Objekt, und erh�hen des Zeigers f�r die Postion der Wiederholung.
				return TerminMitWiederholungImpl.this.getWdh().naechstesDatum(cursor++);
			}
		};
	}


	@Override
	public Map<Datum, Termin> termineFuer(DatumsGroesse groesse) {
		
		// TODO Indizes fuer Start und End Intervall berechnen
		int startIndex = this.getWdh().naechstesIntervall(groesse.getStart());
		int endIndex = this.getWdh().naechstesIntervall(groesse.getEnde());
		// TODO Indizes auf Gültigkeit prüfen
		// wenn endIndex > maxIntervallIndex dann setze endIndex auf
		// maxIntervallIndex
		// wenn endIndex < startIndex || endIndex < 0 || startIndex < 0 ||
		// endIndex > maxIntervallIndex
		// gib null zurück
		if(endIndex < startIndex || endIndex < 0 || startIndex < 0 || endIndex > this.getWdh().maxIntervallIndex()) //Indizies Pr�fen
			return null;
		// TODO hier den Intervalliterator nutzen 
		// Map erzeugen und die Wiederholungen einsammeln
		Map<Datum, Termin> termine = new HashMap<Datum, Termin>();
		IntervallIterator<Datum> iter = intervallIterator(startIndex, endIndex);
		while(iter.hasNext()){
			Datum next_date = iter.next();
			termine.put(next_date, TerminMitWiederholungImpl.this);
		}
		return termine;
	}

	public class WiederholungImpl implements Wiederholung {

		private WiederholungType wdhType;
		private int anzahl;
		private int cycle;

		public WiederholungImpl(WiederholungType wdhType, int anzahl, int cyclus) {
			this.wdhType = wdhType;
			this.anzahl = anzahl;
			this.cycle = cyclus;
		}

		public WiederholungImpl(Wiederholung wdh) {
			this(wdh.getType(), wdh.anzahl(), wdh.getZyklus());
		}
		@Override
		public WiederholungType getType() {
			return wdhType;
		}
		@Override
		public int getZyklus() {
			return cycle;
		}
		@Override
		public int anzahl() {
			return anzahl;
		}
		@Override
		public int maxIntervallIndex() {
			return anzahl;
		}
		@Override
		public int intervallLaenge() {
			return cycle * wdhType.inTagen();
		}
		/*
		 * @see kalender.interfaces.Wiederholung#naechstesIntervall(kalender.interfaces.Datum)
		 * 
		 * Methode liefert den Intervallindex für das einem Datum nachfolgendem
		 * Intervall. Es werden auch Intervalle berechnet, die außerhalb des
		 * gültigen Bereichs maxIntervallIndex liegen. Nutzer der Methode müssen
		 * sicher stellen, dass die Gültigkeit des Index geprüft wird.
		 */
		@Override
		public int naechstesIntervall(Datum dat) {
			long diff = dat.differenzInTagen(getDatum());
			long div = diff / intervallLaenge();
			long mod = diff % intervallLaenge();

			/*
			 * div <= 0 und mod < 0: tag liegt vor dem ersten Termin der
			 * Wiederholung (Intervall 0) div > 0 && mod > 0: tag liegt vor dem
			 * Termin im Intervall div+1 div >= 0 && mod == 0: tag ist ein
			 * Termin der Wiederholung im Intervall div
			 */
			int intervallIndex = -1;
			if (div <= 0 && mod < 0)
				intervallIndex = 0;
			if (diff > 0 && mod > 0)
				intervallIndex = (int) div + 1;
			if (diff >= 0 && mod == 0)
				intervallIndex = (int) div;
			return intervallIndex;
		}

		/*
		 * @see kalender.interfaces.Wiederholung#vorherigesIntervall(kalender.
		 * interfaces.Datum)
		 * 
		 * Methode liefert den Intervallindex für das einem Datum vorausgehenden
		 * Intervall. Es werden auch Intervalle berechnet, die außerhalb des
		 * gültigen Bereichs maxIntervallIndex liegen. Nutzer der Methode müssen
		 * sicher stellen, dass die Gültigkeit des Index geprüft wird.
		 */
		@Override
		public int vorherigesIntervall(Datum dat) {
			long diff = dat.differenzInTagen(getDatum());
			long div = diff / intervallLaenge();
			long mod = diff % intervallLaenge();

			/*
			 * diff < 0: dann liegt das Datum vor dem ersten Termin Fehler div
			 * >= 0 && mod = 0: dann interval = div sonst intervall =
			 * (naechstesIntervall(dat) -1)
			 */

			if (diff < 0)
				return -1;
			if (div >= 0 && mod == 0)
				return (int) div;
			return naechstesIntervall(dat) - 1;
		}

		/*
		 * @see kalender.interfaces.Wiederholung#naechstesDatum()
		 */
		@Override
		public Datum naechstesDatum() {
			return naechstesDatum(1);
		}
		/*
		 * @see kalender.interfaces.Wiederholung#naechstesDatum(int)
		 */
		@Override
		public Datum naechstesDatum(int faktor) {
			int anzahlTage = faktor * intervallLaenge();
			return new DatumImpl(getDatum()).add(new DauerImpl(anzahlTage, 0, 0));
		}
		/*
		 * @see kalender.interfaces.Wiederholung#sub(int)
		 */
		@Override
		public Wiederholung sub(int wdhCount) {
			return new WiederholungImpl(wdhType, anzahl - wdhCount, cycle);
		}
		/*
		 * @see kalender.interfaces.Wiederholung#add(int)
		 */
		@Override
		public Wiederholung add(int wdhCount) {
			return new WiederholungImpl(wdhType, anzahl + wdhCount, cycle);
		}


	}

}
