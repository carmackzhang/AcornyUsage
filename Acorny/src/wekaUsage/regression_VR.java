package wekaUsage;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.core.converters.ArffLoader;
import weka.gui.InstancesSummaryPanel;

public class regression_VR {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
//		Classifier m_classifier = new LibSVM();
//        File inputFile = new File("D://code_temp//weka_data//G219.arff");//训练语料文件
//        ArffLoader atf = new ArffLoader(); 
//        atf.setFile(inputFile);
//        Instances instancesTrain = atf.getDataSet(); // 读入训练文件    
//        inputFile = new File("D://code_temp//weka_data//G219.arff");//测试语料文件
//        atf.setFile(inputFile);          
//        Instances instancesTest = atf.getDataSet(); // 读入测试文件
//        instancesTest.setClassIndex(0); //设置分类属性所在行号（第一行为0号），instancesTest.numAttributes()可以取得属性总数
//        double sum = instancesTest.numInstances(),//测试语料实例数
//        right = 0.0f;
//        instancesTrain.setClassIndex(0);
        
        //批量训练代码，这次是认真的呢！不是做实验！
		String trainPath = "/search/odin/uigs/wekaData_700_v1/";
		//String trainPath = "D://code_temp//weka_data";
		File file = new File(trainPath);
		File[] tempList = file.listFiles();
		for ( int i = 0; i < tempList.length; i++ ){
//			System.out.print(tempList[i].getName());
//	        System.out.print(tempList[i].getPath());
			ArffLoader atf = new ArffLoader();
			atf.setFile(tempList[i]);
			Instances instancesTotal = atf.getDataSet();
		
//			ArrayList<Attribute> atts = new ArrayList();
//			Attribute target = new Attribute("target");
//			atts.add(target);
//			for ( int j = 0; j < 600; j++ ){
//				Attribute atr = new Attribute(String.valueOf(j));
//				atts.add(atr);
//			}
//
//			Attribute atr = new Attribute("NUMERIC");
//			Instances ones = new Instances("600",atrList,1);
//			Instance b = ones.get(0);
//			b.setValue(2, 0.1);
			
			//ArrayList<Attribute> atts = new ArrayList<Attribute>(600);
//			Instances dataRaw = new Instances("TestInstances",atts,0);
//			double[] instanceValue = new double[dataRaw.numAttributes()];
//			dataRaw.add(new DenseInstance(1.0, instanceValue));
//			dataRaw.setClassIndex(0);
//			Instance one = dataRaw.get(0);
//			System.out.println(one.numAttributes());
//			for ( int j = 0; j < 600; j++ ){
//				one.setValue(j, 1.1);
//			}
			
	        //数据集长度，用这个来划分
	        int length = instancesTotal.numInstances();
	        String[] options = null;
			if ( length < 10 ){
				continue;
			}
//			if ( tempList[i].getName().equals("100001.arff") || tempList[i].getName().equals("11002401.arff") 
//					|| tempList[i].getName().equals("100002.arff") || tempList[i].getName().equals("11008601.arff") 
//					|| tempList[i].getName().equals("11009501.arff") || tempList[i].getName().equals("11002301.arff") 
//					|| tempList[i].getName().equals("G251.arff") || tempList[i].getName().equals("G253.arff")
//					|| tempList[i].getName().equals("G59.arff") || tempList[i].getName().equals("G39.arff")
//					|| tempList[i].getName().equals("11000203.arff") || tempList[i].getName().equals("70048100.arff")
//					|| tempList[i].getName().equals("100003.arff") || tempList[i].getName().equals("11008801.arff")
//					|| tempList[i].getName().equals("100009.arff") || tempList[i].getName().equals("G143.arff")
//					|| tempList[i].getName().equals("100008.arff") ){
//				options=weka.core.Utils.splitOptions("-I 100 -K 40 -num-slots 32 -depth 20");
//			}
//			else{	
//				options=weka.core.Utils.splitOptions("-I 50 -K 40 -num-slots 32 -depth 10");
//				continue;
//			}
			if ( tempList[i].getName().equals("100009.arff") || tempList[i].getName().equals("100008.arff") 
					|| tempList[i].getName().equals("100001.arff") ){
				options=weka.core.Utils.splitOptions("-I 100 -K 40 -num-slots 32 -depth 20");
			}
			else{	
				options=weka.core.Utils.splitOptions("-I 50 -K 40 -num-slots 32 -depth 10");
			}
			System.out.print(tempList[i].getName());
	        System.out.print(tempList[i].getPath());
			
			
//			if ( length > 1000000 ){
//				options=weka.core.Utils.splitOptions("-I 200 -K 40 -num-slots 32 -depth 20");
//			}
//			if ( length > 100000 ){
//				options=weka.core.Utils.splitOptions("-I 100 -K 40 -num-slots 32 -depth 20");
//			}
//			else{
//				options=weka.core.Utils.splitOptions("-I 80 -K 40 -num-slots 32 -depth 10");
//			}
			
			
			
			SimpleDateFormat time1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println(time1.format(new java.util.Date()));
			
	        //学习比率
	        //double learningRate = 0.7;
	        //分割点下标，（0 -- devidePoint-1）是训练集， （devidePoint-1 -- length-1）是测试集
	        //int devidePoint = (int) (length*learningRate);
	        instancesTotal.setClassIndex(0);
	        
	        //String[] options=weka.core.Utils.splitOptions("-S 4 -T 2 -C 1000");
	        //Classifier m_classifier = new LibSVM();
	        
	        
			Classifier m_classifier = new RandomForest();
	        
			
	        //m_classifier.setOptions(options);
			((RandomForest) m_classifier).setOptions(options);
	        m_classifier.buildClassifier(instancesTotal); //训练
	        
	      
	        
	        SimpleDateFormat time2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println(time2.format(new java.util.Date()));
	        
	        //weka.core.SerializationHelper.write("D://code_temp//models_rf//" + tempList[i].getName(), m_classifier);
	        weka.core.SerializationHelper.write("/search/odin/uigs/models_rf_700_v4/" + tempList[i].getName(), m_classifier);
		}
		
		
//		SimpleDateFormat time1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println(time1.format(new java.util.Date()));
//		
//		//RandomForest rf = new RandomForest();
//		Classifier m_classifier = new RandomForest();
//		
//		
//		
//        //File inputFile = new File("D://code_temp//weka_data//100008.arff");//训练语料文件
//		File inputFile = new File("/search/odin/uigs/wekaData_700_v1/100001.arff");//训练语料文件
//        ArffLoader atf = new ArffLoader(); 
//        atf.setFile(inputFile);
//        Instances instancesTotal = atf.getDataSet();
//        
//        
//        
//        //数据集长度，用这个来划分
//        int length = instancesTotal.numInstances();
//        //学习比率
//        double learningRate = 0.7;
//        //分割点下标，（0 -- devidePoint-1）是训练集， （devidePoint-1 -- length-1）是测试集
//        int devidePoint = (int) (length*learningRate);
//        //迭代次数
//        int folds = 5;
//        
//        
//        int seed = 605;
//        Instances randData = new Instances(instancesTotal);
//        Random rand = new Random(seed);
//        randData.randomize(rand);
//        
//        
//        Instances instancesValidation  = randData.testCV(5, 1);
//        instancesTotal = randData.trainCV(5, 1);
//
//        
//        
//        float scoreTotal = 0.0f;
//        for ( int n = 0; n < folds; n++ ){
//            Instances instancesTest = instancesTotal.testCV(folds, 1);
//            Instances instancesTrain = instancesTotal.trainCV(folds, 1);
//            
//            instancesTest.setClassIndex(0);
//            instancesTrain.setClassIndex(0);
//            
//            
//            //String[] options=weka.core.Utils.splitOptions("-S 4 -T 2 -C 1000");
//            String[] options=weka.core.Utils.splitOptions("-I 80 -K 40 -num-slots 32 -depth 10 -B");
//            ((RandomForest) m_classifier).setOptions(options);
//            System.out.println("before train");
//            m_classifier.buildClassifier(instancesTrain); //训练
//            System.out.println("after train");
//            
//            SimpleDateFormat time2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    		System.out.println(time2.format(new java.util.Date()));
//            
//            
//            List<Double> yTrue    = new ArrayList();
//            List<Double> yPredict = new ArrayList();
//            
//            
//            for(int  i = 0;i < instancesTest.numInstances(); i++ )//测试分类结果
//            {
//            	yTrue.add(instancesTest.instance(i).classValue());
//            	yPredict.add(m_classifier.classifyInstance(instancesTest.instance(i)));	
//            }
//            
//            double yMean = 0.0f;
//            double yTotal = 0.0f;
//            for ( int i = 0; i < instancesTest.numInstances(); i++ )
//            {
//            	yTotal += yTrue.get(i);
//            }
//            yMean = yTotal / instancesTest.numInstances();
//            
//            double uSum = 0.0f;
//            double vSum = 0.0f;
//            for ( int i = 0; i < instancesTest.numInstances(); i++ )
//            {
//            	uSum += (yTrue.get(i) -yPredict.get(i))*(yTrue.get(i) -yPredict.get(i));
//            	vSum += (yTrue.get(i) - yMean)*(yTrue.get(i) - yMean);
//            }
//            double score = 1 - uSum/vSum;
//            System.out.println(score);
//            scoreTotal += score;
//            weka.core.SerializationHelper.write("/search/odin/uigs/testfile/" + "100001", m_classifier);
//        }
//        System.out.println("the avg score is " + String.valueOf(scoreTotal/folds));
        

        
        
        
//        weka.core.SerializationHelper.write("D://svr_model", m_classifier);
//        ObjectInputStream ois = new ObjectInputStream( new FileInputStream ("D://svr_model"));
//        Classifier classifier = (Classifier)ois.readObject();
//        for(int i = 0; i < instancesTest.numInstances(); i++ )
//        {
//        	System.out.println(classifier.classifyInstance(instancesTest.instance(i)));
//        }
        //System.out.println("SVM classification precision:"+(right/sum));
	}

}
