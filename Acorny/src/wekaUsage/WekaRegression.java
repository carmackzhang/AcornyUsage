package wekaUsage;

import java.io.File;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

public class WekaRegression {

	private ArffLoader loader;
	private Instances trainData;
	private Instances testData;
//	private AttributeSelectedClassifier classifier;
	private Classifier classifier;
	private static String iteration;
	private static String attrRatio;
	private static String attrNum;
	private static String depth;
	private static String model;

	public WekaRegression(File test) {
		loadModel();
		loadTestSet(test);
		evaluateModel();
	}

	public WekaRegression(File train, File test) {
		loadTrainSet(train);
		buildModel();
		outputModel();
		loadTestSet(test);
		evaluateModel();
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
		}else if(args.length==5){
			model = args[4];
			attrNum = args[3];
			depth = args[2];
			iteration = args[1];
			long begin = System.currentTimeMillis();
			new WekaRegression(new File(args[0]));
			long time = System.currentTimeMillis()-begin;
			System.out.println("iteration:"+iteration+"\tdepth:"+depth+"\tattrNum:"+attrNum+"\t"+time/1000+" s");
		}else{
			System.err.println("Usage:(train test iteration depth attrNum model) or (test iteration depth attrNum model)");
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
		int rightNum = 0;
		double rightRatio = 0;
		int totalNum = testData.numInstances();
		double u = 0;
		double v = 0;
		double mean = 0;
		double r2 = 0;
		double relativeSquareMean = 0;
		int displayNum = 300;
		
		try {
			for (int i = 0; i < totalNum; i++) {
				Instance ins = testData.get(i);
				double prediction = classifier.classifyInstance(ins);
				double real = ins.classValue();
				if(Math.abs(prediction-real)<=2){
					rightNum++;
				}
				if(i<displayNum){
//					System.out.println("predictLabel:"+prediction+"\trealLabel:"+real);
				}
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
			System.out.println("rightNum:"+rightNum+",rightRatio:"+rightRatio+",relativeSquareMean:"+relativeSquareMean+",r^2:"+r2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadTestSet(File test) {
		if (loader == null) {
			loader = new ArffLoader();
		}
		try {
			loader.setFile(test);
			testData = loader.getDataSet();
			testData.setClassIndex(testData.numAttributes() - 1);
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
		String[] ops = { "-I", iteration, "-K", attrNum, "-depth", depth };

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
		loader = new ArffLoader();
		try {
			loader.setFile(train);
			trainData = loader.getDataSet();
			trainData.setClassIndex(trainData.numAttributes() - 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Instances getTrainData() {
		return trainData;
	}

	public void setTrainData(Instances trainData) {
		this.trainData = trainData;
	}

	public Instances getTestData() {
		return testData;
	}

	public void setTestData(Instances testData) {
		this.testData = testData;
	}

}
