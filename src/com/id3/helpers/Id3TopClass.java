package com.id3.helpers;

import java.util.ArrayList;
import java.util.logging.Logger;

import weka.core.Instances;

public class Id3TopClass {

	public static Logger logger = Logger.getLogger(Id3TopClass.class.getName());
	public static String classLabel0 = null;
	public static String classLabel1 = null;
	
	public static int classIndex = 0;
	
	public static int getClassIndex() {
		return classIndex;
	}

	public static void setClassIndex(int classIndex) {
		Id3TopClass.classIndex = classIndex;
	}

	public static String text_leaf = "leaf";
	public static String text_nonLeaf = "nonLeaf";

	public static String text_numeric = "numeric";
	public static String text_string = "string";	
	
	public static boolean checkIfNumberOrNot (String s) throws Exception {

		logger.info("into checkIfNumberOrString method");
		if(s == null || s.isEmpty() || s.length() == 0 || s.equals("") ) {
			throw new Exception("#value of passed String is null.");
		}

		if(s.toLowerCase().equals(s.toUpperCase())) {
			return true;
		} else
			return false;

	}
	
	public static String getClassLabel0() {
		return classLabel0;
	}

	public static void setClassLabel0(String classLabel0) {
		Id3TopClass.classLabel0 = classLabel0;
	}

	public static String getClassLabel1() {
		return classLabel1;
	}

	public static void setClassLabel1(String classLabel1) {
		Id3TopClass.classLabel1 = classLabel1;
	}

	public static ArrayList<String> getAttrValues(Instances dataInstances, int attrInd) {
		
		int numOfVals = dataInstances.numDistinctValues(attrInd);
		ArrayList<String> attrValues = new ArrayList <String> (numOfVals);
		
		String s = dataInstances.attribute(attrInd).toString();
		s = s.substring(s.indexOf("{") + 1, s.lastIndexOf("}"));
		for(int i = 0; i < numOfVals; i++) {

			String temp = null;

			if(i == numOfVals-1) {
				temp = s;
			} else {

				temp = s.substring(0, s.indexOf(","));
				s = s.substring(s.indexOf(",") + 1);
			}
			//System.out.println(temp);
			attrValues.add(temp);
		}
		
		return attrValues;
	}
	
	public static void setClass(Instances dataInstances) {
		dataInstances.setClassIndex(dataInstances.numAttributes()-1);
		classIndex = dataInstances.numAttributes()-1;
		ArrayList<String> classValues = 
				getAttrValues(dataInstances, dataInstances.numAttributes()-1);
		setClassLabel0(classValues.get(0));
		setClassLabel1(classValues.get(1));
		//dataInstances.
	}

}
