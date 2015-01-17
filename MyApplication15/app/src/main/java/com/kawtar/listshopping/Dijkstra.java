package com.kawtar.listshopping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Dijkstra {

	
	
	  private final List<ProductToSend> nodes;
	  private final List<Link> Links;
	  private Set<ProductToSend> settledNodes;
	  private Set<ProductToSend> unSettledNodes;
	  private Map<ProductToSend, ProductToSend> predecessors;
	  private Map<ProductToSend, Integer> distance;

	  public Dijkstra(Graph graph) {
	    // create a copy of the array so that we can operate on this array
	    this.nodes = new ArrayList<ProductToSend>(graph.getNodes());
	    this.Links = new ArrayList<Link>(graph.getLinks());
	  }

	  public void execute(ProductToSend source) {
	    settledNodes = new HashSet<ProductToSend>();
	    unSettledNodes = new HashSet<ProductToSend>();
	    distance = new HashMap<ProductToSend, Integer>();
	    predecessors = new HashMap<ProductToSend, ProductToSend>();
	    distance.put(source, 0);
	    unSettledNodes.add(source);
	    while (unSettledNodes.size() > 0) {
	      ProductToSend node = getMinimum(unSettledNodes);
	      settledNodes.add(node);
	      unSettledNodes.remove(node);
	      findMinimalDistances(node);
	    }
	  }

	  private void findMinimalDistances(ProductToSend node) {
	    List<ProductToSend> adjacentNodes = getNeighbors(node);
	    for (ProductToSend target : adjacentNodes) {
	      if (getShortestDistance(target) > getShortestDistance(node)
	          + getDistance(node, target)) {
	        distance.put(target, (int) (getShortestDistance(node)
	            + getDistance(node, target)));
	        predecessors.put(target, node);
	        unSettledNodes.add(target);
	      }
	    }

	  }

	  private double getDistance(ProductToSend node, ProductToSend target) {
	    for (Link Link : Links) {
	      if (Link.getSource().equals(node)
	          && Link.getDestination().equals(target)) {
	        return Link.getWeight();
	      }
	    }
	    throw new RuntimeException("Should not happen");
	  }

	  private List<ProductToSend> getNeighbors(ProductToSend node) {
	    List<ProductToSend> neighbors = new ArrayList<ProductToSend>();
	    for (Link Link : Links) {
	      if (Link.getSource().equals(node)
	          && !isSettled(Link.getDestination())) {
	        neighbors.add(Link.getDestination());
	      }
	    }
	    return neighbors;
	  }

	  private ProductToSend getMinimum(Set<ProductToSend> Nodees) {
	    ProductToSend minimum = null;
	    for (ProductToSend Node : Nodees) {
	      if (minimum == null) {
	        minimum = Node;
	      } else {
	        if (getShortestDistance(Node) < getShortestDistance(minimum)) {
	          minimum = Node;
	        }
	      }
	    }
	    return minimum;
	  }

	  private boolean isSettled(ProductToSend Node) {
	    return settledNodes.contains(Node);
	  }

	  private int getShortestDistance(ProductToSend destination) {
	    Integer d = distance.get(destination);
	    if (d == null) {
	      return Integer.MAX_VALUE;
	    } else {
	      return d;
	    }
	  }

	  /*
	   * This method returns the path from the source to the selected target and
	   * NULL if no path exists
	   */
	  public LinkedList<ProductToSend> getPath(ProductToSend target) {
	    LinkedList<ProductToSend> path = new LinkedList<ProductToSend>();
	    ProductToSend step = target;
	    // check if a path exists
	    if (predecessors.get(step) == null) {
	      return null;
	    }
	    path.add(step);
	    while (predecessors.get(step) != null) {
	      step = predecessors.get(step);
	      path.add(step);
	    }
	    // Put it into the correct order
	    Collections.reverse(path);
	    return path;
	  }
	
	
}
