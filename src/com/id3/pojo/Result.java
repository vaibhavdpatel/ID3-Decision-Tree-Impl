package com.id3.pojo;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.Instances;

import com.id3.helpers.Id3TopClass;

public class Result extends Id3TopClass{

	//numeric/String
	String typeOfAttr = null;
	
	// Attr Name: weather/age
	String attrName = null;
	Attribute attribute = null;
	
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	int attrInd = 0;
	
	// sunny/rainy
	ArrayList<String> nameOfEachChild = null;
	
	// array of child instances for each children
	ArrayList<Instances> instancesOfEachChild = null;
	
	// for numeric attr what is split value
	double splitForNumericAttr = 0;
	
	double infoGain = 0;
	int numOfChildren = 0;
	
	public String getTypeOfAttr() {
		return typeOfAttr;
	}
	public void setTypeOfAttr(String typeOfAttr) {
		this.typeOfAttr = typeOfAttr;
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public ArrayList<String> getNameOfEachChild() {
		return nameOfEachChild;
	}
	public void setNameOfEachChild(ArrayList<String> nameOfEachChild) {
		this.nameOfEachChild = nameOfEachChild;
	}
	public ArrayList<Instances> getInstancesOfEachChild() {
		return instancesOfEachChild;
	}
	public void setInstancesOfEachChild(ArrayList<Instances> instancesOfEachChild) {
		this.instancesOfEachChild = instancesOfEachChild;
	}
	public double getSplitForNumericAttr() {
		return splitForNumericAttr;
	}
	public void setSplitForNumericAttr(double splitForNumericAttr) {
		this.splitForNumericAttr = splitForNumericAttr;
	}
	public double getInfoGain() {
		return infoGain;
	}
	public void setInfoGain(double infoGain) {
		this.infoGain = infoGain;
	}
	public int getNumOfChildren() {
		return numOfChildren;
	}
	public void setNumOfChildren(int numOfChildren) {
		this.instancesOfEachChild = new ArrayList<Instances> (numOfChildren);
		this.nameOfEachChild = new ArrayList<String> (numOfChildren); 
		this.numOfChildren = numOfChildren;
	}
	
	public int getAttrInd() {
		return attrInd;
	}
	public void setAttrInd(int attrInd) {
		this.attrInd = attrInd;
	}
	
	public String toString() {
		String ans = null;
		
		ans = "Type Of attr: " + this.typeOfAttr + "\n";
		ans += "Attr Name: " + this.attrName + "\n";
		ans += "# of children: " + this.numOfChildren + "\n";
		ans += "Name of each child: " + this.nameOfEachChild.toString() + "\n";
		ans += "Attr index: " + this.attrInd + "\n";
		
		ans += "instances size of each child: ";
		for(int i = 0; i < this.numOfChildren; i++) {
			ans += this.instancesOfEachChild.get(i).numInstances() + " ";
		}
		
		ans += "\nInformation Gain: " + this.infoGain + "\n";
		ans += "Split value if it is a Numeric Feature: " + this.splitForNumericAttr;
		
		return ans;
	}
	
}
