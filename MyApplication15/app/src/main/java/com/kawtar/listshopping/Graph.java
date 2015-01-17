package com.kawtar.listshopping;

import java.util.List;

public class Graph {

	
	 private final List<ProductToSend> nodes;
	  private final List<Link> links;

	  public Graph(List<ProductToSend> nodes, List<Link> links) {
	    this.nodes = nodes;
	    this.links = links;
	  }

	  public List<ProductToSend> getNodes() {
	    return nodes;
	  }

	  public List<Link> getLinks() {
	    return links;
	  }
}
