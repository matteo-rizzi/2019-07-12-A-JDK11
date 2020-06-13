package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;

public class Simulator {

	// CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;

	// PARAMETRI DI SIMULAZIONE
	private int K = 5; // numero di stazioni di lavoro

	// MODELLO DEL MONDO
	private List<Stazione> stazioni;
	private List<Food> cibi;

	private Graph<Food, DefaultWeightedEdge> grafo;
	private Model model;

	// VALORI DA CALCOLARE
	private Double tempoPreparazione;
	private int cibiPreparati;

	public Simulator(Graph<Food, DefaultWeightedEdge> grafo, Model model) {
		this.grafo = grafo;
		this.model = model;
	}

	public Double getTempoTotalePreparazione() {
		return tempoPreparazione;
	}

	public int getCibiPreparati() {
		return this.cibiPreparati;
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public void init(Food partenza) {
		this.cibi = new ArrayList<>(this.grafo.vertexSet());
		for (Food f : this.cibi)
			f.setPreparazione(StatoPreparazione.DA_PREPARARE);
		this.stazioni = new ArrayList<>();
		for (int i = 0; i < K; i++) {
			this.stazioni.add(new Stazione(true, null));
		}
		this.tempoPreparazione = 0.0;
		this.cibiPreparati = 0;

		this.queue = new PriorityQueue<>();
		Map<Food, Double> vicini = model.calorieCongiunteMassime(partenza);
		int i = 0;
		for (Food vicino : vicini.keySet()) {
			if (i < this.K && i < vicini.size()) {
				this.stazioni.get(i).setLibera(false);
				this.stazioni.get(i).setCibo(vicino);
				Event e = new Event(vicini.get(vicino), EventType.FINE_PREPARAZIONE, vicino, stazioni.get(i));
				queue.add(e);
			}
			i++;
		}
	}

	public void run() {

		while (!queue.isEmpty()) {
			Event e = queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch (e.getType()) {
		case INIZIO_PREPARAZIONE:
			Map<Food, Double> vicini = model.calorieCongiunteMassime(e.getCibo());
			Food prossimo = null;
			Double tempo = null;
			for (Food vicino : vicini.keySet()) {
				if (vicino.getPreparazione() == StatoPreparazione.DA_PREPARARE) {
					prossimo = vicino;
					tempo = vicini.get(vicino);
					break; // non proseguire nel ciclo
				}
			}

			if (prossimo != null) {
				prossimo.setPreparazione(StatoPreparazione.IN_CORSO);
				e.getStazione().setLibera(false);
				e.getStazione().setCibo(prossimo);

				Event e2 = new Event(e.getTime() + tempo, EventType.FINE_PREPARAZIONE, prossimo, e.getStazione());
				this.queue.add(e2);
			}
			break;
		case FINE_PREPARAZIONE:
			this.cibiPreparati++;
			this.tempoPreparazione = e.getTime();
			e.getStazione().setLibera(true);
			e.getCibo().setPreparazione(StatoPreparazione.PREPARATO);
			Event e2 = new Event(e.getTime(), EventType.INIZIO_PREPARAZIONE, e.getCibo(), e.getStazione());
			this.queue.add(e2);
			break;
		}
	}
}
