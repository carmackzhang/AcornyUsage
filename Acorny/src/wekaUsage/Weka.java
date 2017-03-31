package wekaUsage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class Weka {
	
	private static Instances totalSet;
	private static Instances trainSet;
	private static Instances testSet;
	private static RandomForest classifier;
	private static String inDataPath = "data/tmp1.arff";
	private static String outModelPath = "data/test1.model";

	public static Instances loadDataSet(String filePath){
		try {
			totalSet = new Instances(new FileReader(new File(filePath)));
			totalSet.setClassIndex(13);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return totalSet;
	}
	
	public static void buildModel(Instances data){
		try {
			classifier = new RandomForest();
			String[] options = {"-K","15","-I","100","-depth","0"};
			classifier.setOptions(options);
			classifier.buildClassifier(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Evaluation crossValidation(Classifier classifier, Instances data, int numFolds){
		Evaluation eval = null;
		try {
			eval = new Evaluation(data);
			classifier.buildClassifier(data);
			eval.crossValidateModel(classifier, data, numFolds, new Random(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		printEvaluationInfo(eval);
		return eval;
	}
	
	public static void outputModel(String outFilePath){
		try {
			weka.core.SerializationHelper.write(outFilePath,classifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printEvaluationInfo(Evaluation eval){
		try {
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toMatrixString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testModel(){
		try {
			J48 rf = (J48) weka.core.SerializationHelper.read(outModelPath);
//			RandomForest rf = new RandomForest();
			rf.buildClassifier(totalSet.resample(new Random(System.currentTimeMillis())));
			double correct = 0;
			for(int i=0; i<10; i++){
				trainSet = totalSet.trainCV(10, i);
				testSet = totalSet.testCV(10, i);
				Evaluation eval = new Evaluation(trainSet);
				eval.evaluateModel(rf, testSet);
				correct += eval.correct();
				System.out.println(1-eval.errorRate()+" "+eval.correct());
			}
			System.out.println("correct="+correct/totalSet.size());
			
			int rightNum = 0;
			for(int i=0; i<totalSet.size(); i++){
				double predict = rf.classifyInstance(totalSet.get(i));
				double real = totalSet.get(i).classValue();
				if( predict == real ) rightNum++;
			}
			System.out.println(rightNum*1.0/totalSet.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testModelByInstance(Instance ins){
		try {
			RandomForest rf = (RandomForest) weka.core.SerializationHelper.read(outModelPath);
			ins.insertAttributeAt(0);
			rf.classifyInstance(ins);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Instance makeInstance(float a1,float a2,Instances data){
		Instance ins = new DenseInstance(4);//36 attr
		
		//numeric
		Attribute oldpos_page = new Attribute("oldpos_page");
		Attribute articlesPos = new Attribute("articlesPos");
		Attribute pageShowType = new Attribute("pageShowType");
		
		//nominal
		Attribute isOfficial = new Attribute("isOfficial");
		
//		Instances instances = new Instances("", attributeList, 10);
		ins.setValue(oldpos_page, 1);
		ins.setValue(articlesPos, 2);
		ins.setValue(pageShowType, 0);
		ins.setValue(isOfficial, "true");
		
		return ins;
	}
	
	public static void main(String[] args){
		loadDataSet(inDataPath);
		
//		crossValidation(classifier,totalSet,10);
//		
//		outputModel(classifier, outModelPath);
		
//		testModel();
		
		testModelByInstance(new DenseInstance(36));
	}
	
}

