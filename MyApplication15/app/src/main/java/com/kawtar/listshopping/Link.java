package com.kawtar.listshopping;


public class Link {
	
	 private final String id; 
	  private final ProductToSend source;
	  private final ProductToSend destination;
	  private final double weight; 
	  
	  public Link(String id, ProductToSend source, ProductToSend destination) {
	    this.id = id;
	    this.source = source;
	    this.destination = destination;
	    this.weight = Math.sqrt(Math.pow(destination.getPositionx()-source.getPositiony(),2)+Math.pow(destination.getPositionx()-source.getPositiony(),2));
	  }
	  
	  public String getId() {
	    return id;
	  }
	  public ProductToSend getDestination() {
	    return destination;
	  }

	  public ProductToSend getSource() {
	    return source;
	  }
	  public double getWeight() {
	    return weight;
	  }
	  
	  @Override
	  public String toString() {
	    return source + " " + destination;
	  }

}
