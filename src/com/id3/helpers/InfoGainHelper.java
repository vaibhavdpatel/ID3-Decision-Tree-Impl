package com.id3.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import com.id3.pojo.Result;

import weka.core.Instances;

public class InfoGainHelper extends Id3TopClass {

	public static Result findInfoGain(double entropyOfParent, 
			Instances dataInstances, int attrInd) {

		Result result = new Result();

		String typeOfAttr = null;

		//System.out.println("---------------------------\n" + "Num of distinct values: " + dataInstances.numDistinctValues(attrInd));
		int numOfChildren = dataInstances.numDistinctValues(attrInd);

		if(dataInstances.attribute(attrInd).isNumeric() == true) {
			typeOfAttr = text_numeric;
			result.setNumOfChildren(2);
		} else {
			typeOfAttr = text_string;
			result.setNumOfChildren(numOfChildren);
		}

		result.setTypeOfAttr(typeOfAttr);
		result.setAttrName(dataInstances.attribute(attrInd).name());
		result.setAttribute(dataInstances.attribute(attrInd));

		if(dataInstances.numInstances() == 0) {
			result.setInfoGain(Double.MIN_VALUE);
			return result;
		}

		if(numOfChildren == 1) {
			result.setInfoGain(Double.MIN_VALUE);
			return result;
		}

		ArrayList<String> nameOfEachChild = new ArrayList<String> (numOfChildren);
		ArrayList<Double> entropyOfEachChild = new ArrayList<Double> (numOfChildren);
		ArrayList<Instances> instancesOfEachChild = 
				new ArrayList<Instances> (numOfChildren);
		ArrayList<Instances> instancesOfEachChildFromLeft = 
				new ArrayList<Instances> (numOfChildren);
		ArrayList<Instances> instancesOfEachChildFromRight = 
				new ArrayList<Instances> (numOfChildren);

		//For numeric features
		ArrayList<Double> valueOfEachChild = new ArrayList<Double> (numOfChildren);
		int[][] numOfClassVbles = new int[numOfChildren][2] ;
		for(int i = 0; i < numOfChildren; i++) {
			numOfClassVbles[i][0] = 0;
			numOfClassVbles[i][1] = 0;
		}
		double[][] entropyOfEachChildForNumeric = new double [numOfChildren-1][2];

		LinkedHashMap<Double, Integer> valueIndices = 
				new LinkedHashMap <Double, Integer>();

		if(text_numeric.equals(typeOfAttr)) {
			for(int i = 0; i < dataInstances.numInstances(); i++) {

				if(!valueOfEachChild.contains(dataInstances.instance(i).value(attrInd))) {
					valueOfEachChild.add(dataInstances.instance(i).value(attrInd));
				} 
			}
		} else {

			/*String s = dataInstances.attribute(attrInd).toString();
			s = s.substring(s.indexOf("{") + 1, s.lastIndexOf("}"));
			for(int i = 0; i < numOfChildren; i++) {

				String temp = null;

				if(i == numOfChildren-1) {
					temp = s;
				} else {

					temp = s.substring(0, s.indexOf(","));
					s = s.substring(s.indexOf(",") + 1);
				}
				//System.out.println(temp);
				nameOfEachChild.add(temp);
			}*/
			nameOfEachChild = getAttrValues(dataInstances, attrInd);
		}

		if(text_numeric.equals(typeOfAttr)) {
			Collections.sort(valueOfEachChild);

			//logger.info("sorted array: " + valueOfEachChild.toString());

			for(int i = 0; i < valueOfEachChild.size(); i++) {
				valueIndices.put(valueOfEachChild.get(i), i);
			}

			//logger.info("Hashmap:" + valueIndices.toString());

			int ind = 0;
			String classi;
			for(int i = 0; i < dataInstances.numInstances(); i++) {
				ind = valueIndices.get(dataInstances.instance(i).value(attrInd));

				classi = dataInstances.instance(i).stringValue(dataInstances.numAttributes()-1);
				if(classLabel1.equals(classi)) {
					numOfClassVbles[ind][0] ++;
				} else {
					numOfClassVbles[ind][1] ++;
				}
			}
		}

		// adding all instances, later on 'll remove instances 
		// not matching attr value
		for(int i = 0; i < numOfChildren; i++) {
			Instances inst = new Instances(dataInstances);
			instancesOfEachChild.add(i, inst);
		}

		for(int i = 0; i < numOfChildren; i++) {

			for(int j = dataInstances.numInstances()-1; j >=0 ; j--) {

				if(text_numeric.equals(typeOfAttr)) {
					if(valueOfEachChild.get(i) != dataInstances.instance(j).value(attrInd)) {
						instancesOfEachChild.get(i).delete(j);
					}

				} else {
					if(!nameOfEachChild.get(i).equals(dataInstances.instance(j).stringValue(attrInd))) {
						instancesOfEachChild.get(i).delete(j);
					}
				}
			}
		}

		for(int i = 0; i < numOfChildren; i++) {
			Instances inst = new Instances(instancesOfEachChild.get(i));
			instancesOfEachChildFromLeft.add(i,inst);

			inst = new Instances(instancesOfEachChild.get(i));
			instancesOfEachChildFromRight.add(i, inst);
		}

		for(int i = 0; i < numOfChildren; i++) {
			if(!text_numeric.equals(typeOfAttr)) {
				instancesOfEachChild.get(i).deleteAttributeAt(attrInd);
			} 
		}

		if(text_numeric.equals(typeOfAttr)) {

			for(int i = 1; i < numOfChildren; i++) {

				int num = instancesOfEachChildFromLeft.get(i-1).numInstances();
				for(int k = 0; k < num ; k++) {
					instancesOfEachChildFromLeft.get(i).
					add(instancesOfEachChildFromLeft.get(i-1).instance(k));
				}
			}

			for(int i = numOfChildren - 2; i >= 0; i--) {

				int num = instancesOfEachChildFromRight.get(i+1).numInstances();
				for(int k = 0; k < num; k++) {
					instancesOfEachChildFromRight.get(i).
					add(instancesOfEachChildFromRight.get(i+1).instance(k));
				}
			}

			for(int i = 0; i < numOfChildren - 1; i++) {

				if((numOfClassVbles[i][0] > 0 && numOfClassVbles[i+1][1] > 0) ||
						(numOfClassVbles[i][1] > 0 && numOfClassVbles[i+1][0] > 0)) {
					double entropyOfSplitAtIPlus1ForLeft = EntropyHelper.
							findEntropy(instancesOfEachChildFromLeft.get(i));
					double entropyOfSplitAtIPlus1ForRight = EntropyHelper.
							findEntropy(instancesOfEachChildFromRight.get(i+1));
					entropyOfEachChildForNumeric[i][0] = entropyOfSplitAtIPlus1ForLeft;
					entropyOfEachChildForNumeric[i][1] = entropyOfSplitAtIPlus1ForRight;

				} else {
					entropyOfEachChildForNumeric[i][0] = -1;
					entropyOfEachChildForNumeric[i][1] = -1;
				}
			}

			double weightedEntropy = 0;
			double maxInfoGain = Double.MIN_VALUE;
			double splittingPoint = 0;
			int maxInfoGainInd = 0;

			for(int i = 0; i < numOfChildren - 1; i++) {

				if(entropyOfEachChildForNumeric[i][0] != -1) {
					weightedEntropy = instancesOfEachChildFromLeft.get(i).numInstances() * 
							entropyOfEachChildForNumeric[i][0];
					weightedEntropy += instancesOfEachChildFromRight.get(i+1).numInstances() * 
							entropyOfEachChildForNumeric[i][1];

					weightedEntropy /= dataInstances.numInstances();
					//System.out.println(weightedEntropy);

					double infoGain = entropyOfParent - weightedEntropy;
					System.out.println("Entropy here: pt: " + ((valueOfEachChild.get(i) + 
							valueOfEachChild.get(i+1)) / 2) + " , infogain: " + infoGain);
					if(infoGain > maxInfoGain) {
						maxInfoGain = infoGain;
						maxInfoGainInd = i;
					} else if(infoGain == maxInfoGain) {
						// Do nothing as splitting value should be set to min 
						// in this case.
					}
				}
			}
			//System.out.println("# of children " + numOfChildren + ", " + valueOfEachChild.size());
			splittingPoint = (valueOfEachChild.get(maxInfoGainInd) + 
					valueOfEachChild.get(maxInfoGainInd+1)) / 2;
			//System.out.println("Splitting pt: " + splittingPoint);

			result.getInstancesOfEachChild().add(
					instancesOfEachChildFromLeft.get(maxInfoGainInd));
			result.getInstancesOfEachChild().add(
					instancesOfEachChildFromRight.get(maxInfoGainInd + 1));

			result.setInfoGain(maxInfoGain);
			result.setSplitForNumericAttr(splittingPoint);
		} else {

			for(int i = 0; i < numOfChildren; i++) {
				double entropyOfChildAtI = EntropyHelper.findEntropy(instancesOfEachChild.get(i));
				entropyOfEachChild.add(entropyOfChildAtI);

				result.getInstancesOfEachChild().add(instancesOfEachChild.get(i));

				//System.out.println(nameOfEachChild.get(i));
				System.out.println(entropyOfEachChild.get(i)+ " --entropy of each child\n");
			}

			double weightedEntropy = 0;

			for(int i = 0; i < numOfChildren; i++) {
				weightedEntropy += instancesOfEachChild.get(i).numInstances() * entropyOfEachChild.get(i);
			}

			weightedEntropy /= dataInstances.numInstances();
			//System.out.println(weightedEntropy);

			double infoGain = entropyOfParent - weightedEntropy;

			result.setInfoGain(infoGain);
			result.setSplitForNumericAttr(-1);
			result.setNameOfEachChild(nameOfEachChild);
			//return infoGain;

		}

		result.setAttrInd(attrInd);
		System.out.println(result.toString());

		return result;
	}

}
