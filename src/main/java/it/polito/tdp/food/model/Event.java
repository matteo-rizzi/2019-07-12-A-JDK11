package it.polito.tdp.food.model;

public class Event implements Comparable<Event> {

	public enum EventType {
		INIZIO_PREPARAZIONE, FINE_PREPARAZIONE
	}

	private Double time; // tempo in minuti
	private EventType type;
	private Food cibo;
	private Stazione stazione;

	public Event(Double time, EventType type, Food cibo, Stazione stazione) {
		super();
		this.time = time;
		this.type = type;
		this.cibo = cibo;
		this.stazione = stazione;
	}

	public Double getTime() {
		return time;
	}

	public EventType getType() {
		return type;
	}

	public Food getCibo() {
		return cibo;
	}

	public Stazione getStazione() {
		return stazione;
	}

	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.getTime());
	}

}
