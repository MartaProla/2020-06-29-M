package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private List<Integer>listaAnni;
	private ImdbDAO dao;
	private Graph<Director, DefaultWeightedEdge>grafo;
	private Map<Integer, Director>idMapDirettori;
	private List<Director>listaDirettori;
	private List<Director>listaDirettoriAnno;
	private List<Vicino>adiacenti;
	//ricorsione
	
	private List<Director>listaAttoriMigliori;
	
	
	public Model() {
		listaAnni=new LinkedList<Integer>();
		dao=new ImdbDAO();
		idMapDirettori=new HashMap<Integer, Director>();
		listaDirettori=new LinkedList<Director>(this.dao.listAllDirectors());
		for(Director d: listaDirettori) {
			if(!idMapDirettori.containsKey(d.id)) {
				this.idMapDirettori.put(d.id, d);
			}
		}
	}
	
	
	public List<Integer>getAnni(){
		listaAnni.add(2004);
		listaAnni.add(2005);
		listaAnni.add(2006);
		return listaAnni;
	}
	
	public void creaGrafo(Integer anno) {
		this.grafo=new SimpleWeightedGraph<Director, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.listaDirettoriAnno=new LinkedList<Director>(this.dao.getDirettoriAnno( anno, this.idMapDirettori));
		Graphs.addAllVertices(this.grafo, listaDirettoriAnno);
		List<Adiacenza>adiacenze=new LinkedList<Adiacenza>(this.dao.getAdiacenza(anno, idMapDirettori));
		for(Adiacenza a: adiacenze) {
			if(this.grafo.getEdge(a.getD1(),a.getD2())==null) {
				Graphs.addEdgeWithVertices(this.grafo, a.getD1(),a.getD2(), a.getPeso());
			}
		}
		System.out.println(String.format("Vertici: %d", this.vertexNumber()));
		System.out.println(String.format("Archi: %d", this.edgeNumber()));
	}
	
	public int vertexNumber() {
		return this.grafo.vertexSet().size();
	}
	public int edgeNumber() {
		return this.grafo.edgeSet().size();
	}
	public List<Director>getDirettoriGrafo(){
		List<Director>direttoriGrafo=new LinkedList<Director>(this.grafo.vertexSet());
		return direttoriGrafo;
	}
	
	public List<Vicino>getVicini(Director d){
		adiacenti=new LinkedList<Vicino>();
		
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(d)) {
			Vicino v=new Vicino(this.grafo.getEdgeSource(e), (int) this.grafo.getEdgeWeight(e));
			adiacenti.add(v);
			
		}
		Collections.sort(adiacenti);
		return adiacenti;
	
	}
	//listaAttoriMigliori --> percorso migliore
	
	public List<Director>getAttoriAffini(Director inserito, Integer peso){
		this.listaAttoriMigliori=new ArrayList<Director>();
		List<Director>parziale=new ArrayList<Director>();
		parziale.add(inserito);
		ricorsione(parziale,peso);
		return listaAttoriMigliori;
	}


	private void ricorsione(List<Director> parziale, Integer peso) {
		//caso terminale 
		if(calcolaPeso(parziale)>peso) {
			return;
		}
		if(parziale.size()>listaAttoriMigliori.size()) {
			listaAttoriMigliori=new ArrayList<Director>(parziale);
		}
		//caso generale
		Director ultimo_inserito=parziale.get(parziale.size()-1);
		for(Director d:Graphs.neighborListOf(this.grafo, ultimo_inserito)) {
			if(!parziale.contains(d)) {
				parziale.add(d);
				ricorsione(parziale, peso);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	public Integer calcolaPeso(List<Director>parziale) {
		int peso=0;
		for(int i=0;i<parziale.size()-1;i++) {
			Director d1=parziale.get(i);
			Director d2=parziale.get(i+1);
			peso=peso+(int)this.grafo.getEdgeWeight(this.grafo.getEdge(d1, d2));
		}
		return peso;
	}
	
	
}
