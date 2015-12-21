package com.id3.pojo;

import java.util.HashMap;


public class attribute {

	private String attrName;
	private HashMap <String, Integer> values;
	private int colPos;

	public attribute (String name, HashMap<String, Integer> vals, int col) {
		this.attrName = name;
		this.values = vals;
		this.colPos = col;
	}
	
	public int getNumOfValues () {
		return values.size();
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public HashMap<String, Integer> getValues() {
		return values;
	}

	public void setValues(HashMap<String, Integer> values) {
		this.values = values;
	}

	public int getColPos() {
		return colPos;
	}

	public void setColPos(int colPos) {
		this.colPos = colPos;
	}

	public boolean equals(attribute a) {
		if(this.getColPos() != a.getColPos()) return false;
		return true;
	}

}
