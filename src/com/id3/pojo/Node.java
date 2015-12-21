package com.id3.pojo;

import java.util.LinkedHashMap;

import weka.core.Instances;

import com.id3.helpers.EntropyHelper;
import com.id3.helpers.Id3TopClass;

public class Node extends Id3TopClass {
	private String nodeValue = null;
	private String predictedClass = null;
	private int numOfChildren = 0;
	private Instances instances = null;
	private int numOfClassLabel0 = 0;
	private int numOfClassLabel1 = 0;
	private String typeOfNode = null;
	private double entropy = 0;
	private Result bestSplit;
	
	public Result getBestSplit() {
		return bestSplit;
	}

	public void setBestSplit(Result bestSplit) {
		this.bestSplit = bestSplit;
	}

	private LinkedHashMap<Integer,Node> childTreeList = null;

	public double getEntropy() {
		return entropy;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public Node(Instances dataInstances, String nodeValue) {
		this.instances = dataInstances;

		// Finding number of pos/neg instances
		numOfClassLabel0 = 0;
		numOfClassLabel1 = 0;
		String classi;
		for(int i = 0; i < dataInstances.numInstances(); i++) {
			classi = dataInstances.instance(i).stringValue(dataInstances.classIndex());
			if(classLabel0.equals(classi)) {
				numOfClassLabel0 ++;
			} else {
				numOfClassLabel1 ++;
			}
		}
		
		numOfChildren = 0;
		this.nodeValue = nodeValue;
		typeOfNode = null;
		childTreeList = new LinkedHashMap<Integer,Node>();
		predictedClass = null;
		entropy = EntropyHelper.findEntropy(dataInstances);
		bestSplit = null;
	}
	
	public void setLeafNode() {

		if(this.getInstances().numInstances() == 0) {
			this.setPredictedClass(getClassLabel0());
		} else {

			if(this.getNumOfClassLabel0() >= this.getNumOfClassLabel1())
				this.setPredictedClass(getClassLabel0());
			else
				this.setPredictedClass(getClassLabel1());
		}
		//System.out.println("Leaf: predicted label -- " + this.getPredictedClass());
		this.numOfChildren = 0;
		this.setTypeOfNode(text_leaf);
		this.setChildTreeList(null);
		this.bestSplit = null;
	}

	public String getPredictedClass() {
		return predictedClass;
	}

	public void setPredictedClass(String predictedClass) {
		this.predictedClass = predictedClass;
	}
	
	public String getNodeValue() {
		return nodeValue;
	}

	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}

	public int getNumOfChildren() {
		return numOfChildren;
	}

	public void setNumOfChildren(int numOfChildren) {
		this.numOfChildren = numOfChildren;
	}

	public Instances getInstances() {
		return instances;
	}

	public void setInstances(Instances instances) {
		this.instances = instances;
	}

	public int getNumOfClassLabel0() {
		return numOfClassLabel0;
	}

	public void setNumOfClassLabel0(int numOfClassLabel0) {
		this.numOfClassLabel0 = numOfClassLabel0;
	}

	public int getNumOfClassLabel1() {
		return numOfClassLabel1;
	}

	public void setNumOfClassLabel1(int numOfClassLabel1) {
		this.numOfClassLabel1 = numOfClassLabel1;
	}

	public String getTypeOfNode() {
		return typeOfNode;
	}

	public void setTypeOfNode(String typeOfNode) {
		this.typeOfNode = typeOfNode;
	}

	public LinkedHashMap<Integer, Node> getChildTreeList() {
		return childTreeList;
	}

	public void setChildTreeList(LinkedHashMap<Integer, Node> childTreeList) {
		this.childTreeList = childTreeList;
	}




}
