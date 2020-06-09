package it.polito.tdp.food.model;

public class Adiacenza implements Comparable<Adiacenza>{

	private Food first;
	private Food second;
	private Double peso;

	public Adiacenza(Food first, Food second, Double peso) {
		super();
		this.first = first;
		this.second = second;
		this.peso = peso;
	}

	public Food getFirst() {
		return first;
	}

	public void setFirst(Food first) {
		this.first = first;
	}

	public Food getSecond() {
		return second;
	}

	public void setSecond(Food second) {
		this.second = second;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Adiacenza other) {
		return other.peso.compareTo(this.peso);
	}

}
