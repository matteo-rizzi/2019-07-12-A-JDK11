package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Map<Integer, Food> idMap;
	private SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo;
	private List<Adiacenza> adiacenze;
	
	public Model() {
		this.dao = new FoodDao();
		idMap = new HashMap<Integer, Food>();
		this.dao.listAllFoods(idMap);
		this.adiacenze = new ArrayList<Adiacenza>();
	}
	
	public void creaGrafo(int numeroPorzioni) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		List<Food> daInserire = new ArrayList<>(this.dao.getFoodByPortions(numeroPorzioni, idMap));
		Collections.sort(daInserire);
		Graphs.addAllVertices(this.grafo, daInserire);
		
		// aggiungo gli archi
		for(Adiacenza a : this.dao.getAdiacenze(this.idMap)) {
			if(this.grafo.vertexSet().contains(a.getFirst()) && this.grafo.vertexSet().contains(a.getSecond()) && a.getPeso() != 0) {
				Graphs.addEdge(this.grafo, a.getFirst(), a.getSecond(), a.getPeso());
				this.adiacenze.add(a);
			}
		}
	}
	
	public Map<Food, Double> calorieCongiunteMassime(Food selezionato) {
		List<Adiacenza> adiacentiSelezionato = new ArrayList<>();
		Map<Food, Double> max = new LinkedHashMap<>();
		
		// Riempiamo adiacentiSelezionato con tutte le adiacenze in cui è presente selezionato, che sarà presente come "first".
		// In "second", invece, ci sono tutte le adiacenze di selezionato.
		for(Adiacenza a : adiacenze) {
			if(selezionato.equals(a.getFirst()))
				adiacentiSelezionato.add(new Adiacenza(a.getFirst(), a.getSecond(), a.getPeso()));
			else if(selezionato.equals(a.getSecond()))
				adiacentiSelezionato.add(new Adiacenza(a.getSecond(), a.getFirst(), a.getPeso()));
		}
		
		Collections.sort(adiacentiSelezionato);
		for(Adiacenza a : adiacentiSelezionato) {
			max.put(a.getSecond(), a.getPeso());
		}
		
		return max;
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Set<Food> vertici() {
		return this.grafo.vertexSet();
	}
	
	public String simula(Food cibo, int K) {
		Simulator sim = new Simulator(this.grafo, this);
		sim.setK(K);
		sim.init(cibo);
		sim.run();
		String messaggio = String.format("Preparati %d cibi in %f minuti\n", sim.getCibiPreparati(), sim.getTempoTotalePreparazione());
		return messaggio;
	}

}
