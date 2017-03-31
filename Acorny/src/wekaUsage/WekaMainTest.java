package wekaUsage;

import java.io.File;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class WekaMainTest {

	public static void main(String[] args){
		try {
//			J48 c =  (J48) weka.core.SerializationHelper.read("data/weixinJ48.model");
			buildClassifier();
			Instances dataSet = testSet;
			
			int correct = 0;
			int size = dataSet.size();
			for(Instance ins : dataSet){
				double result = classifyInstance(ins);
				if(result == ins.classValue()){
					correct++;
				}
			}
//			weka.core.SerializationHelper.write("data/outModel.model", classifier);
			System.out.println("precision:"+correct+"/"+size+"="+(1.0*correct)/(1.0*size));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	static Instances trainSet;
	static Instances testSet;
	static Evaluation eval;
	public static void loadTrainSet() throws Exception{
		ArffLoader load = new ArffLoader();
		load.setSource(new File("data/tmp1.arff"));
		Instances dataSet = load.getDataSet();
		int numFolds = 10;
		
		double avgPrecision = 0.0;
		for(int i = 0;i < numFolds;i++){
			trainSet = dataSet.trainCV(numFolds, i);
			testSet = dataSet.testCV(numFolds, i);
			trainSet.setClassIndex(13);
			testSet.setClassIndex(13);
			classifier = new J48();
			classifier.buildClassifier(trainSet);
			eval = new Evaluation(trainSet);
			eval.evaluateModel(classifier, testSet);
			avgPrecision += 1-eval.errorRate();
			System.out.println(eval.toSummaryString()+"\n"+eval.toMatrixString());
		}
		
		System.out.println("train size:"+trainSet.size()+",test size:"+testSet.size()+",precision:"+avgPrecision/numFolds);
	}
	
	static Classifier classifier;
	public static void buildClassifier() throws Exception{
//		classifier = new J48();
		classifier = new RandomForest();
		loadTrainSet();
		classifier.buildClassifier(trainSet);
	}
	
	public static double classifyInstance(Instance ins) throws Exception{
		if(ins == null) return -1;
		double res = 0.0;
		res = classifier.classifyInstance(ins);
		return res;
	}
	
	
}
