package com.id3.helpers;

import weka.core.Instances;

public class AttributesHelper {

	public AttributesHelper(Instances instances) {
		int totalAttrs = instances.numAttributes();
		int totalInstances = instances.numInstances();
		
		for(int i = 0; i < totalInstances; i++) {
			
			for(int j = 0; j < totalAttrs; j++) {
				instances.instance(i).attribute(j);
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
