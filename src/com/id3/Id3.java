package com.id3;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import com.id3.helpers.Id3TopClass;
import com.id3.helpers.InfoGainHelper;
import com.id3.pojo.Node;
import com.id3.pojo.Result;

public class Id3 extends Id3TopClass {

	@SuppressWarnings("unused")
	private static final String ARFF_FILE_PATH = 
			"/afs/cs.wisc.edu/u/v/a/vaibhav/private/"
					+ "Eclipse/ML1/src/inputFiles/heart_train.arff";

	public Result findBestSplit(Node root) {

		Instances dataInstances = root.getInstances();
		System.out.println("size"                  + dataInstances.numInstances() + "\n");
		double currentEntropy = root.getEntropy(); 

		double maxInfoGain = Double.MIN_VALUE;
		Result bestSplit = null;
		Result infogainResult = null;
		//int attrInd = 11;
		for(int attrInd = 0; attrInd < dataInstances.numAttributes()-1; attrInd++) {
			infogainResult = 
					InfoGainHelper.findInfoGain(currentEntropy,dataInstances,attrInd);

			if(infogainResult.getInfoGain() > maxInfoGain) {
				maxInfoGain = infogainResult.getInfoGain();
				bestSplit = infogainResult;
			} else if(infogainResult.getInfoGain() == maxInfoGain) {
				// Do nothing as attr with small index has to be picked up
				// as candidate attr
			}
		}

		//System.out.println("\n\n");
		if(bestSplit == null) {
			//System.out.println("=========+++++++ below result is wrong. No split found.");
			bestSplit = infogainResult;
		}
		System.out.println("\n\n========= best split ========\n" + bestSplit.toString());
		System.out.println("\n\n");
		return bestSplit;
	}

	public void buildTree(Node root, int m) {

		Instances dataInstances = root.getInstances();
		if(dataInstances.numInstances() < m || 
				dataInstances.numAttributes() == 1 ||
				root.getNumOfClassLabel0() == dataInstances.numInstances() ||
				root.getNumOfClassLabel1() == dataInstances.numInstances()) {

			root.setLeafNode();
			return ;
		}

		
		System.out.println("size1 " + dataInstances.numInstances());
		Result bestSplit = findBestSplit(root);

		if(bestSplit == null || bestSplit.getInfoGain() == Double.MIN_VALUE) {
			//System.out.println("Setting up root");
			root.setLeafNode();
			return ;
		}

		root.setTypeOfNode(text_nonLeaf);
		root.setNumOfChildren(bestSplit.getNumOfChildren());
		root.setBestSplit(bestSplit);

		for(int i = 0; i < bestSplit.getNumOfChildren(); i++) {

			String childName = bestSplit.getAttrName() ;
			if(bestSplit.getTypeOfAttr().equals(text_numeric)) {
				if(i == 0) 
					childName += " <= " + new DecimalFormat("#0.000000").
					format(bestSplit.getSplitForNumericAttr());
				else
					childName += " > " + new DecimalFormat("#0.000000").
					format(bestSplit.getSplitForNumericAttr());
			} else {
				childName += " = " + bestSplit.getNameOfEachChild().get(i); 
			}

			//System.out.println(childName);
			Instances childInstances = bestSplit.getInstancesOfEachChild().get(i);

			Node child = new Node(childInstances, childName); 

			root.getChildTreeList().put(i, child);
			buildTree(child, m);
		}

		return ;
	}

	public String printTree(Node root, int height) {
		if(root == null) {
			return "";
		}

		String printedTree = new String();

		if(!text_leaf.equals(root.getTypeOfNode()) && height == -1) {
			//dont print anything
		} else {
			printedTree += "\n" ;
			for(int i = 0; i < height; i++) {
				printedTree += "|       ";
			}

			printedTree += root.getNodeValue() + " " ;

			printedTree += "[" + root.getNumOfClassLabel0() + " " + root.getNumOfClassLabel1() + "]" ;
			if(text_leaf.equals(root.getTypeOfNode())) {
				printedTree += ": " + root.getPredictedClass();
			}
		}

		for(int i =0; i <root.getNumOfChildren(); i++) {
			printedTree += printTree(root.getChildTreeList().get(i), height + 1);
		}

		return printedTree;
	}

	String predictClass(Instances instances, int index, Node root) {

		Instance instance = instances.instance(index);

		if(root.getTypeOfNode().equals(text_leaf)) {
			return root.getPredictedClass();
		} else {
			int attrIndex = root.getBestSplit().getAttrInd();

			if(root.getBestSplit().getTypeOfAttr().equals(text_numeric)) {
				double cond = root.getBestSplit().getSplitForNumericAttr();
				//System.out.println("\nComparing: " + instance.value(attrIndex) + ", to: "+ cond);
				if(instance.value(attrIndex) <= cond) {
					return predictClass(instances, index, root.getChildTreeList().get(0));
				} else {
					return predictClass(instances, index, root.getChildTreeList().get(1));
				}
			} else {
				//System.out.println("\n"+ root.getBestSplit().toString());

				for(int i = 0; i < root.getBestSplit().getNumOfChildren(); i++) {
					String attrValue = root.getBestSplit().getNameOfEachChild().get(i);
					//System.out.println("\nComparing: " + instance.stringValue(attrIndex) + ", to: " + attrValue);
					//System.out.println("attr: " + attrIndex);
					//System.out.println("Instance:" + instance.toString());
					if(instance.stringValue(attrIndex).equals(attrValue)) {
						instances.deleteAttributeAt(attrIndex);
						return predictClass(instances, index, root.getChildTreeList().get(i));
					}
				}
			}
		}
		return null;
	}

	public double predictTestSet(Instances testInstances, Node root) {
		System.out.println("<Predictions for the Test Set Instances>");
		int correctPred = 0;
		Id3 id3 = new Id3();

		@SuppressWarnings("unused")
		int precision = 0;
		int temp = testInstances.numInstances();

		while (temp != 0) {
			temp /= 10;
			precision ++;
		}

		//int i = 2;
		for(int i = 0; i < testInstances.numInstances(); i++) {
			Instances newInstances = new Instances(testInstances);
			String actual = testInstances.instance(i).stringValue(classIndex);
			String predictedValue = id3.predictClass(newInstances, i, root);
			System.out.printf("%3d" + ": Actual: " + actual
					+ "  Predicted: " + predictedValue, (i+1));
			System.out.println();

			if(actual.equals(predictedValue)
					&& predictedValue != null) {
				correctPred ++;
			}
		}

		System.out.println("Number of correctly classified: " + correctPred +
				"  Total number of test instances: " + testInstances.numInstances());

		return testInstances.numAttributes() == 0 ? 0 : (double)correctPred/(double)testInstances.numInstances();
	}

	public int[] giveRandomIndices(int min, int max, double percentageOfNumbers) {
		int size = (int) ((max-min) * percentageOfNumbers /100);
		int[] a = new int[size];

		Random r = new Random();

		for(int i = 0; i < size; i++) {
			a[i] = r.nextInt(max - min + 1) + min;
		}

		return a;
	}

	public void getAccuracy(double percentageOfNumbers, Instances dataInstances, Instances testInstances, 
			int howManyTimes, int m) {

		double minAccuracy = Double.MAX_VALUE, maxAccuracy = Double.MIN_VALUE, avgAccuracy = 0;

		for(int j = 0; j < howManyTimes; j++) {
			int max = dataInstances.numInstances();
			int min = 0;
			int size = (int) ((max-min) * percentageOfNumbers /100);
			int ind ;
			Random r = new Random();

			for(int i = 0; i < max-size; i++) {
				ind = (r.nextInt(max - min + 1) + min) % max;
				dataInstances.delete(ind);
				max = dataInstances.numInstances();
			}
			System.out.println(dataInstances.numInstances());
			setClass(dataInstances);
			Node root = new Node(dataInstances, "");
			Id3 id3 = new Id3();
			id3.buildTree(root, m);

			//String s = new String();
			//s =  id3.printTree(root, -1);
			//System.out.println(s);

			double accuracy = id3.predictTestSet(testInstances, root);
			if(accuracy < minAccuracy) minAccuracy = accuracy;
			if(accuracy > maxAccuracy) maxAccuracy = accuracy;
			avgAccuracy = (avgAccuracy*j+accuracy)/(j+1);
		}
		System.out.println("\n\n" + minAccuracy + " \n" + avgAccuracy + " \n" + maxAccuracy);
	}

	public static void main(String[] args) throws Exception  {

		if(args.length != 3) {
			System.out.println("very few args");
			return;
		}

		ArffLoader arffLoader = new ArffLoader();

		//File datasetFile = new File(ARFF_FILE_PATH);

		File datasetFile = new File(args[0]);
		arffLoader.setFile(datasetFile);

		Instances dataInstances = arffLoader.getDataSet();

		File testsetFile = new File(args[1]);
		arffLoader.setFile(testsetFile);
		Instances testInstances = arffLoader.getDataSet();

		int m ;
		m = Integer.parseInt(args[2]);

		//Id3 id3Test = new Id3();
		//id3Test.getAccuracy(100, dataInstances, testInstances, 1, 2);

		setClass(dataInstances);
		Node root = new Node(dataInstances, "");
		Id3 id3 = new Id3();
		id3.buildTree(root, m);

		//String s = new String();
		//s =  id3.printTree(root, -1);
		//System.out.println(s);

		//id3.predictTestSet(testInstances, root);
	}
}
