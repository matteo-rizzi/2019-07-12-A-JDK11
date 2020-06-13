package it.polito.tdp.food.model;

public class Stazione {

	private boolean libera;
	private Food cibo;

	public Stazione(boolean libera, Food cibo) {
		super();
		this.libera = libera;
		this.cibo = cibo;
	}

	public boolean isLibera() {
		return libera;
	}

	public void setLibera(boolean libera) {
		this.libera = libera;
	}

	public Food getCibo() {
		return cibo;
	}

	public void setCibo(Food cibo) {
		this.cibo = cibo;
	}

}
