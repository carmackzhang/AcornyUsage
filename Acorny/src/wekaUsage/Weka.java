package wekaUsage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class Weka {
	
	private static Instances totalSet;
	private static Instances trainSet;
	private static Instances testSet;
	private static RandomForest classifier;
	private static String inDataPath = "data/data_4attr_1w.arff";
	private static String outModelPath = "data/test1_6attr.model";
	
	public static Instances loadDataSet(String filePath){
		try {
			totalSet = new Instances(new FileReader(new File(filePath)));
			totalSet.setClassIndex(0);
			System.out.println("total num:"+totalSet.numInstances()+" attribute num:"+totalSet.numAttributes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return totalSet;
	}
	
	public static void buildModel(Instances data){
		try {
			loadDataSet(inDataPath);
			classifier = new RandomForest();
			String[] options = {"-K","15","-I","50","-depth","20"};
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
	
	public static void testModelByInstance(double pMaxTr, double pMaxMr, String isOnlyOneTerm, String isArticleOld, double maxRNum){
		try {
			RandomForest rf = (RandomForest) weka.core.SerializationHelper.read(outModelPath);
			Instances dataset = makeInstances();
			Instance ins = makeInstance(pMaxTr, pMaxMr, isOnlyOneTerm, isArticleOld, maxRNum, dataset);
			double predict = rf.classifyInstance(ins);
			System.out.println("classify result:"+predict);
			System.out.println("classify result type:" + dataset.classAttribute().value((int)predict));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Instances makeInstances(){
		
		Instances dataset;
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		
		ArrayList<String> pageShowNominalVal = new ArrayList<String>();
		pageShowNominalVal.add("class0");
		pageShowNominalVal.add("class1");
		attributes.add(new Attribute("pageShowType",pageShowNominalVal));
		
		attributes.add(new Attribute("pMaxTr"));
		attributes.add(new Attribute("pMaxMr"));
		
		ArrayList<String> onlyOneTermNominalVal = new ArrayList<String>();
		onlyOneTermNominalVal.add("FALSE");
		onlyOneTermNominalVal.add("TRUE");
		attributes.add(new Attribute("isOnlyOneTerm",onlyOneTermNominalVal));
		
		attributes.add(new Attribute("isArticleOld",onlyOneTermNominalVal));
		attributes.add(new Attribute("maxRNum"));
		
		dataset = new Instances("dataName", attributes, 0);
		dataset.setClass(dataset.attribute("pageShowType"));
		
		return dataset;
	}
	
	public static Instance makeInstance(double pMaxTr, double pMaxMr, String isOnlyOneTerm, String isArticleOld, double maxRNum, Instances dataset){
		
//		double pMaxTr = 5.1;
//		double pMaxMr = 50;
//		String isOnlyOneTerm = "FALSE";
//		String isArticleOld = "FALSE";
//		double maxRNum = 36472.0;
		
		Instance ins = new DenseInstance(6);//6 attr
		ins.setDataset(dataset.stringFreeStructure());
		System.out.println("total attr num:"+dataset.numAttributes());
		ins.setValue(dataset.attribute("pMaxTr"), pMaxTr);
		ins.setValue(dataset.attribute("pMaxMr"), pMaxMr);
		ins.setValue(dataset.attribute("isOnlyOneTerm"), isOnlyOneTerm);
		ins.setValue(dataset.attribute("isArticleOld"), isArticleOld);
		ins.setValue(dataset.attribute("maxRNum"), maxRNum);
		
		return ins;
	}
	
	public static void attributeSelect(){
		System.out.println(getMethodName(2));
		Ranker rank = new Ranker();
//		InfoGainAttributeEval eval = new InfoGainAttributeEval();
		GainRatioAttributeEval eval = new GainRatioAttributeEval();
		try {
			eval.buildEvaluator(totalSet);
			int[] evalRes = rank.search(eval, totalSet);
			for(int i : evalRes){
				System.out.println(i+" "+totalSet.attribute(i)+" -- "+eval.evaluateAttribute(i));
			}
			double[][] rankedAttrs = rank.rankedAttributes();
			printMatrix(rankedAttrs);
			attributeSelectByPCA();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printMatrix(double[][] matrix){
		for(int i = 0; i < matrix.length; i++){
			for(int j=0; j < matrix[i].length; j++){
				System.out.print(matrix[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	public static void attributeSelectByPCA(){
		System.out.println(getMethodName(2));
		AttributeSelection select = new AttributeSelection();
		Ranker rank = new Ranker();
		PrincipalComponents pca = new PrincipalComponents();
		String[] options = {"-R","0.99","-A","1"};
		try {
			pca.setMaximumAttributeNames(totalSet.numAttributes());
			pca.setOptions(options);
			select.setEvaluator(pca);
			select.setSearch(rank);
			select.SelectAttributes(totalSet);
//			select.setRanking(true);
			System.out.println(select.toResultsString());//unknown:(pMaxMr前的0.611不知道表示什么)
														//Ranked attributes:
														//0.6046114483115955    1 0.611 pMaxMr
			double[][] rankedAttrs = select.rankedAttributes();
			int attrNum = rank.getCalculatedNumToSelect();
			int f_p = 4;
			int w_p = 2;
			for(int i=0; i<attrNum; i++){//特征评价分，index和name
				System.out.println(Utils.doubleToString(rankedAttrs[i][1], f_p + w_p + 1, f_p)
						+Utils.doubleToString((rankedAttrs[i][0]+1), 4, 0)
						+" "
						+totalSet.attribute((int) (rankedAttrs[i][0]+1)).name()
						+"\n");
			}
			
//			testWekeMethod();
//			pca.buildEvaluator(totalSet);
//			System.out.println(rank.search(pca, totalSet));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getMethodName(int depth){
		StackTraceElement[] eles = Thread.currentThread().getStackTrace();
//		System.out.println("get method name by depth "+depth+" stack trace len "+eles.length);
//		for(StackTraceElement ele : eles){
//			System.out.println(ele.getMethodName());
//		}
		return eles[depth].getMethodName();
	}
	
	public static void testWekeMethod(){
		System.out.println(getMethodName(2));
		System.out.println(Utils.doubleToString(3.1415926, 3));
	}
	
	public static void main(String[] args){
//		loadDataSet(inDataPath);
		
//		attributeSelect();
		
//		crossValidation(classifier,totalSet,10);
//		
//		outputModel(outModelPath);
		
//		testModel();
		
//		testModelByInstance(5.1, 50, "FALSE", "FALSE", 36472.0);
//		testModelByInstance(5.6, 50, "FALSE", "FALSE", 61492.0);
//		testModelByInstance(5.4, 50, "TRUE", "FALSE", 100000.0);
		
	}
	
}

