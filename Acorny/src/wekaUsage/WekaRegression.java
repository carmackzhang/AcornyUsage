package wekaUsage;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.AbstractFileLoader;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Normalize;

public class WekaRegression {

//	private ArffLoader loader;
	private AbstractFileLoader loader;
//	private CSVLoader loader;
	private static Instances trainData;
	private static Instances testData;
//	private AttributeSelectedClassifier classifier;
	private static Classifier classifier;
	private static String iteration;
	private static String attrRatio;
	private static String attrNum;
	private static String depth;
	private static String model;
	private static int classIndex = 0;

	public WekaRegression(File test, int args_num) {
		if(args_num == 2) {	
			loadModel();
			loadTestSet(test);
			evaluateModel();
	//		evaluateModel_nominal();
		}else if(args_num == 5) {
			loadTestSet(test);
			Random rand = new Random(1);//seed=1
			int folds = 10;
			Instances data = testData;
			data.randomize(rand);
			if(data.classAttribute().isNominal()) {
				data.stratify(folds);
				System.out.println("class attribute is nominal");
			}
			classifier = new RandomForest();
			String[] ops = { "-I", iteration, "-K", attrNum, "-depth", depth, "-S", "1"};//-I numTrees
			try {
				((RandomForest)classifier).setOptions(ops);
				Evaluation eval = new Evaluation(data);
				double rmse = 0;
				for(int i = 0; i < folds; i++) {
					Instances trainSet = data.trainCV(folds, i);
					Instances testSet = data.testCV(folds, i);
					
					classifier.buildClassifier(trainSet);
					eval.evaluateModel(classifier, testSet);
					rmse += eval.rootMeanSquaredError();
//					System.out.println("Evaluation:"+eval.toSummaryString());
				}
				
				System.out.println("rmse:"+new DecimalFormat("0.000").format(rmse/folds));
				System.out.println(eval.toSummaryString("== "+folds+"-folds-cross-validation ==", false));
				System.out.println(eval.numInstances()+" ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public WekaRegression(File train, File test) {
		loadTrainSet(train);
		buildModel();
		outputModel();
		loadTestSet(test);
		evaluateModel();
//		evaluateModel_nominal();
	}

	public static void main(String[] args) {
		
		if (args.length == 6) {
			model = args[5];
//			attrRatio = args[4];
			attrNum = args[4];
			depth = args[3];
			iteration = args[2];
			long begin = System.currentTimeMillis();
			new WekaRegression(new File(args[0]), new File(args[1]));
			long time = System.currentTimeMillis()-begin;
			System.out.println("iteration:"+iteration+"\tdepth:"+depth+"\tattrNum:"+attrNum+"\t"+time/1000+" s");
		}else if(args.length==5) {
			model = args[4];
			attrNum = args[3];
			depth = args[2];
			iteration = args[1];
			long begin = System.currentTimeMillis();
			new WekaRegression(new File(args[0]), args.length);
			long time = System.currentTimeMillis()-begin;
			System.out.println("iteration:"+iteration+"\tdepth:"+depth+"\tattrNum:"+attrNum+"\t"+time/1000+" s");
		}else if(args.length==2){
			model = args[1];
//			attrNum = args[3];
//			depth = args[2];
//			iteration = args[1];
			long begin = System.currentTimeMillis();
			new WekaRegression(new File(args[0]), args.length);
			long time = System.currentTimeMillis()-begin;
			System.out.println("modelInfo:"+classifier+"time:"+time/1000+" s");
		}else{
			System.err.println("Usage:(train test iteration depth attrNum model) or ((train iteration depth attrNum model)) or (test model)");
		}
		
		try {
			attributeSelect(testData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadModel(){
		try {
			classifier = (Classifier) SerializationHelper.read(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void evaluateModel() {
		int right1 = 0;
		int right2 = 0;
		int right3 = 0;
		int right4 = 0;
		double rightRatio1 = 0;
		double rightRatio2 = 0;
		double rightRatio3 = 0;
		double rightRatio4 = 0;
		int totalNum = testData.numInstances();
		double u = 0;
		double v = 0;
		double variance = 0;
		double std = 0;
		double mean = 0;
		double r2 = 0;
		double root_mean_square_error = 0;//rmse
		double mae = 0;//Mean Absolute Error
		double m = 0;
		int displayNum = 100;
		DecimalFormat df = new DecimalFormat("0.000");
		
		try {
			
			for(int i = 0; i < totalNum; i++){
				double real = testData.get(i).classValue();
				mean += real;
			}
			mean = mean/totalNum;//mean
			
			for(int i = 0; i < totalNum; i++){
				double real = testData.get(i).classValue();
				v+=(real-mean)*(real-mean);//variance=(real-mean)^2/(n-1)
			}
			variance = v/(totalNum-1);
			std = Math.sqrt(variance);
			
			for (int i = 0,j=0; i < totalNum; i++) {
				Instance ins = testData.get(i);
				boolean isRight = false;
				double prediction = classifier.classifyInstance(ins);
				double real = ins.classValue();
				if(Math.abs(prediction-real)< mean/5){
					right1++;
					isRight = true;
				}
				if(Math.abs(prediction - real) < mean/2) {
					right2++;
					isRight = true;
				}
				if(Math.abs(prediction - real) < mean) {
					right3++;
				}
				if(Math.abs(prediction - real) < variance) {
					right4++;
					isRight = true;
				}
//				else{
//					if(j<displayNum){
//						System.out.println("predictLabel:"+prediction+"\trealLabel:"+real);
////						System.out.println(ins);
//						j++;
//					}
//				}
				if(!isRight && i < displayNum)
				System.out.println(i+"\tpredictLabel:"+df.format(prediction)+"\trealLabel:"+real);
				
				u += (prediction-real)*(prediction-real);//bias square
				m += Math.abs(prediction - real);
				
			}
			
			root_mean_square_error = Math.sqrt(u/totalNum);
			mae = m/totalNum;
			
			r2 = 1 - u/v; //R square [0-1]大于0.8认为很好
			rightRatio1 = (1.0*right1/(1.0*totalNum));
			rightRatio2 = (1.0*right2/(1.0*totalNum));
			rightRatio3 = (1.0*right3/(1.0*totalNum));
			rightRatio4 = (1.0*right4/(1.0*totalNum));
			
			System.out.println("right1:"+right1+" rightRatio1:"+df.format(rightRatio1)+" ratio2:"+df.format(rightRatio2)+
					" ratio3:"+df.format(rightRatio3)+" ratio4(variance):"+df.format(rightRatio4)+
					" root_mean_square_error(rmse):"+
					df.format(root_mean_square_error)+" r^2(R square):"+df.format(r2)+" mean:"+df.format(mean)+" variance:"+df.format(variance)+
					" bias^2:"+df.format(u)+" MAE:"+df.format(mae));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void evaluateModel_nominal() {
		int rightNum = 0;
		double rightRatio = 0;
		int rightNum0 = 0;
		double rightRatio0 = 0;
		int totalNum = testData.numInstances();
		double u = 0;
		double v = 0;
		double mean = 0;
		double r2 = 0;
		double relativeSquareMean = 0;
		int displayNum = 100;
		
		try {
			for (int i = 0,j=0; i < totalNum; i++) {
				Instance ins = testData.get(i);
				double prediction = classifier.classifyInstance(ins);
				String prePosLabel = testData.classAttribute().value((int)prediction);
				int prePos = -1;
				if(prePosLabel.length() == 4) prePos = Integer.valueOf(prePosLabel.substring(0,3));
				double real = ins.classValue();
				String realPosLabel = testData.classAttribute().value((int)real);
				int realPos = Integer.valueOf(realPosLabel.substring(0,3));
				
				if(Math.abs(prePos-realPos)<=2){
					rightNum++;
				}
				if(Math.abs(prePos-realPos)<=0) rightNum0++;
//				else{
//					if(j<displayNum){
//						System.out.println("predictLabel:"+prediction+"\trealLabel:"+real);
////						System.out.println(ins);
//						j++;
//					}
//				}
				System.out.println("predictLabel:"+Math.round(prePos)+"\trealLabel:"+realPos);
				
				u += (prediction-real)*(prediction-real);
				mean += real;
			}
			mean = mean/totalNum;
			for(int i=0;i<totalNum;i++){
				Instance ins = testData.get(i);
				double real = ins.classValue();
				v+=(real-mean)*(real-mean);
			}
			relativeSquareMean = u/totalNum;
			r2 = 1-u/v;
			rightRatio = (1.0*rightNum/(1.0*totalNum));
			rightRatio0 = (1.0*rightNum0/(1.0*totalNum));
			
			System.out.println("rightNum0:"+rightNum0+",rightRatio0:"+rightRatio0+",rightNum:"+rightNum
					+",rightRatio:"+rightRatio+",relativeSquareMean:"+relativeSquareMean+",r^2:"+r2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadTestSet(File test) {
		try {
			if(test.getName().endsWith("arff")){
				if (loader == null) {
					loader = new ArffLoader();
				}
			}else if(test.getName().endsWith("csv")){
				if (loader == null) loader = new CSVLoader();
			}
			loader.setFile(test);
//			testData = loader.getDataSet();
			setTestData(loader.getDataSet());
			testData.setClassIndex(classIndex);
//			System.out.println("testData:"+testData.get(0));
//			System.out.println(testData.numAttributes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void outputModel() {
		// System.out.println(classifier);
		try {
			SerializationHelper.write(model, classifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buildModel() {
//		classifier = new AttributeSelectedClassifier();

		classifier = new RandomForest();
		String[] ops = { "-I", iteration, "-K", attrNum, "-depth", depth, "-S", "1"};//-I numTrees

//		PrincipalComponents pca = new PrincipalComponents();
//		Ranker rank = new Ranker();

//		String[] options = { "-R", attrRatio, "-A", "1" };
		try {
			((RandomForest) classifier).setOptions(ops);
//			pca.setOptions(options);
//			classifier.setClassifier(base);
//			classifier.setEvaluator(pca);
//			classifier.setSearch(rank);

			classifier.buildClassifier(trainData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadTrainSet(File train) {
//		loader = new ArffLoader();
//		loader = new CSVLoader();
		try {
			if(train.getName().endsWith("arff")){
				if (loader == null) {
					loader = new ArffLoader();
				}
			}else if(train.getName().endsWith("csv")){
				if (loader == null) loader = new CSVLoader();
			}
			loader.setFile(train);
//			trainData = loader.getDataSet();
			setTrainData(loader.getDataSet());
//			trainData.setClassIndex(trainData.numAttributes() - 1);
			classIndex = trainData.numAttributes() - 1;
			trainData.setClassIndex(classIndex);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Instances getTrainData() {
		return trainData;
	}

	public void setTrainData(Instances trainData) {
//		this.trainData = normalizeData(trainData);
		this.trainData = trainData;
	}

	public Instances getTestData() {
		return testData;
	}

	public void setTestData(Instances testData) {
//		this.testData = normalizeData(testData);
		this.testData = testData;
	}

	public Instances normalizeData(Instances data) {
		Normalize norm = new Normalize();
		Instances newInstances = null;
		HashMap<Integer,Double> attr_value = new HashMap<Integer, Double>();
		try {
			norm.setInputFormat(data);
			newInstances = Filter.useFilter(data, norm);
			double max_attr = 0;
			for(int i = 0; i < newInstances.size(); i++) {
				for(int j = 0; j < newInstances.get(i).numAttributes(); j++) {
					max_attr = newInstances.get(i).value(j);
					if(max_attr > 0.9999 || i==22 || i==23) {
						double data_attr = data.get(i).value(j);
//						System.out.print("("+i+","+j+")"+data_attr+" ");
						if(!attr_value.containsKey(j)) attr_value.put(j, data_attr);
					}
				}
//				System.out.println();
			}
			Iterator<Entry<Integer, Double>> iter = attr_value.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<Integer, Double> entry = iter.next();
				System.out.println(entry.getKey()+"\t"+entry.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(newInstances!=null) return newInstances;
		else {
			System.err.println("Normalize fail!");
			return data;
		}
	}
	
	public static void attributeSelectUseFilter(Instances data) throws Exception {
		weka.filters.supervised.attribute.AttributeSelection attr = new weka.filters.supervised.attribute.AttributeSelection();
//		ASEvaluation eval = new GainRatioAttributeEval();
		ASEvaluation eval = new PrincipalComponents();
	
		ASSearch search = new Ranker();
		
		attr.setEvaluator(eval);
		attr.setSearch(search);
		attr.setInputFormat(data);
		
		System.out.println("ori attribute num:"+data.numAttributes()+" instance_num:"+data.numInstances());
		
		Instances pro_data = Filter.useFilter(data, attr);
		
		System.out.println("pro attribute num:"+pro_data.numAttributes()+" instance_num:"+pro_data.numInstances());
		
		
		System.out.println(pro_data.stringFreeStructure());
		
	}
	
	public static void attributeSelect(Instances data) throws Exception {
		weka.attributeSelection.AttributeSelection attr_select = new weka.attributeSelection.AttributeSelection();
	    PrincipalComponents eval = new PrincipalComponents();
//	    GreedyStepwise search = new GreedyStepwise();
	    
//	    eval.setMaximumAttributeNames(5);
//	    eval.setVarianceCovered(0.9);
	    
	    String[] options = {"-R","0.8","-A","1"};
	    eval.setOptions(options);
	    
	    Ranker search = new Ranker();
//	    search.setSearchBackwards(true);
	    attr_select.setEvaluator(eval);
	    attr_select.setSearch(search);
	    
	    attr_select.SelectAttributes(data);
	    System.out.println(attr_select.toResultsString());
	    
	    int[] indices = attr_select.selectedAttributes();
	    for(int i : indices) {
	    	System.out.print(i+" ");
	    }
	    double[][] selectedAttr = attr_select.rankedAttributes();
	    System.out.println("indices.len:"+indices.length+" "+selectedAttr.length);
	    
	    for(int i = 0; i < selectedAttr.length; i++) {
	    	for(int j = 0; j < selectedAttr[i].length; j++) {
	    		System.out.print("["+i+","+j+"]="+selectedAttr[i][j]+" ");
	    	}
	    	System.out.println();
	    }
	}
	
	
}
