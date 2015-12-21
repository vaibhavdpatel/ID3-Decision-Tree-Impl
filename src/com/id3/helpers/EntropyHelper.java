package com.id3.helpers;

import weka.core.Instances;

public class EntropyHelper extends Id3TopClass {

	public static double findEntropy (Instances dataInstances) {

		int pos = 0, neg = 0;
		String classi;
		
		for(int i = 0; i < dataInstances.numInstances(); i++) {
			classi = dataInstances.instance(i).stringValue(dataInstances.classIndex());
			if(classLabel0.equals(classi)) {
				pos ++;
			} else {
				neg ++;
			}
		}
		
		if(pos == 0 && neg == 0) {
			return 0;
		}

		if(pos == 0 || neg == 0) {
			return 0;
		}
		
		//System.out.println(pos + " " + neg);

		double entropy = 0;
		double posByTotal = ((double)(pos)/(double)(pos+neg));
		double negByTotal = ((double)(neg)/(double)(pos+neg));

		double posLog = Math.log(posByTotal) / Math.log(2);
		double negLog = Math.log(negByTotal) / Math.log(2);

		entropy = - posByTotal * posLog - negByTotal * negLog;
		return entropy;
	}
	
}
