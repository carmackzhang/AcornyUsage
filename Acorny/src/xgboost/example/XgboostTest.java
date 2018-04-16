package xgboost.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoost;
import ml.dmlc.xgboost4j.java.XGBoostError;
import xgboost.example.util.CustomEval;
import xgboost.example.util.DataLoader.CSRSparseData;

public class XgboostTest {

	public static String DATA_PATH = "data/xgboost_demo/";
	public static void main(String[] args) throws XGBoostError,IOException {
		String train = DATA_PATH+"train_demo.txt";
		String test = DATA_PATH+"test_demo.txt";
//		XGBoostRegression(train, test);
//		XGBoostRegression(test);
		XGBoostRegressionOneInstances(test);
	}
	
	public static void XGBoostRegression(String train, String test) throws XGBoostError {
		 DMatrix trainMat = new DMatrix(train);
		 DMatrix testMat = new DMatrix(test);
		 
		 HashMap<String, Object> params = new HashMap<String, Object>();
		 
		 //general parameters
		 params.put("booster", "gbtree");//gbtree or gblinear
		 params.put("silent", 0);
//		 params.put("nthread", 8);
//		 params.put("num_feature", 10);
		 
		 //booster parameters
		 params.put("alpha", 0.0001);
		 params.put("max_depth", 8);//default is 6
		 
		 //learning task parameters: binary && regression
		 params.put("objective", "reg:logistic");//二分类:逻辑回归 (binary:logistic/binary:logitraw; reg:linear/reg:logistic; multi:softmax)
		 params.put("eval_metric", "rmse");
//		 params.put("eval_metric", "auc");
//		 params.put("eval_metric", "error");
		 
		 //learning task parameters: multi class
//		 params.put("objective", "multi:softmax");
//		 params.put("num_class", 2);
//		 params.put("eval_metric", "merror");
		    

		 HashMap<String, DMatrix> watches = new HashMap<String, DMatrix>();
	     watches.put("train", trainMat);
	     watches.put("test", testMat);
	    
	     int round = 10;
	     Booster booster = XGBoost.train(trainMat, params, round, watches, null, null);
	     booster.saveModel(DATA_PATH+"test.model");
	     float[][] predicts = booster.predict(testMat);

	     CustomEval eval = new CustomEval();
	     System.out.println("error=" + eval.eval(predicts, testMat));
	    
	}
	
	public static void XGBoostRegression(String test) throws XGBoostError {
		String model_path = DATA_PATH+"xgb.model";//xgb.model是python训练出来的model
		Booster booster = XGBoost.loadModel(model_path);
		DMatrix testMat = new DMatrix(test);
		float[][] predicts = booster.predict(testMat);
		
		CustomEval eval = new CustomEval();
	    System.out.println("error=" + eval.eval(predicts, testMat));
	    
//		for(int i = 0; i < predicts.length; i++) {
//			for(int j = 0; j < predicts[i].length; j++) {
//				System.out.println(predicts[i][j]);
//			}
//		}
	}
	
	public static void XGBoostRegressionOneInstances(String test) throws XGBoostError, IOException {
		String model_path = DATA_PATH+"xgb.model";//xgb.model是python训练出来的model,训练和预测数据格式需要保持一致
		Booster booster = XGBoost.loadModel(model_path);

//		CSRSparseData spdata = loadSVMFile(test);
		CSRSparseData spdata = genOneInstance();
		DMatrix  testData = new DMatrix(spdata.rowHeaders, //row index of the matrix：特征个数的倍数0,43,86,129...
										spdata.colIndex,  
										spdata.data, 
										DMatrix.SparseType.CSR, 0);  

		testData.setLabel(spdata.labels);
		System.out.println(spdata.data.length+"\t"+spdata.rowHeaders.length+"\t"+spdata.labels.length+"\t"+spdata.colIndex.length);
		
		float[][] predicts = booster.predict(testData);
		
		System.out.println(predicts.length+"\t"+predicts[0].length+"\t"+testData.getLabel().length);
		CustomEval eval = new CustomEval();
//	    System.out.println("error=" + eval.eval(predicts, testData));
	    
		for(int i = 0; i < predicts.length; i++) {
			for(int j = 0; j < predicts[i].length; j++) {
				System.out.println(predicts[i][j]);
			}
		}
		
	}
	
	public static CSRSparseData genOneInstance() {
		CSRSparseData spData = new CSRSparseData();
//		double[] ori_data = {9,0,0,0,0.999818,1095,1095,0,0,0,0,0,2.78,1.08,0.07,4.1,20.0,7,33.0,56.0,7346,111690,0,0,2,354,1409,0,148,1,0,0,1,24,144,97,0,0,0,0.429,0.667,1,112};
		double[] ori_data = {4,0,1,1,6.27E-4,1095,1095,0,0,0,0,0,4.80,0.00,3.00,5.7,50.0,15,52.0,82.0,3,0,0,0,3,35,240,2,27,1,0,0,0,1,60,83,0,0,0,1.000,1.000,1,116};
		
		List<Float> tlabels = new ArrayList<>();
	    List<Float> tdata = new ArrayList<>();
	    List<Long> theaders = new ArrayList<>();
	    List<Integer> tindex = new ArrayList<>();
	    
	    int index = 1;
	    theaders.add(0L);
	    for(double od : ori_data) {
	    	tdata.add((float)od);
	    	tindex.add(index++);
	    }
	    System.out.println(ori_data.length);
	    theaders.add(43L);
		
	    spData.labels = ArrayUtils.toPrimitive(tlabels.toArray(new Float[tlabels.size()]));
	    spData.data = ArrayUtils.toPrimitive(tdata.toArray(new Float[tdata.size()]));
	    spData.colIndex = ArrayUtils.toPrimitive(tindex.toArray(new Integer[tindex.size()]));
	    spData.rowHeaders = ArrayUtils.toPrimitive(theaders.toArray(new Long[theaders.size()]));
	    
		return spData;
	}
	
	public static CSRSparseData loadSVMFile(String filePath) throws IOException {
	    CSRSparseData spData = new CSRSparseData();

	    List<Float> tlabels = new ArrayList<>();
	    List<Float> tdata = new ArrayList<>();
	    List<Long> theaders = new ArrayList<>();
	    List<Integer> tindex = new ArrayList<>();

	    File f = new File(filePath);
	    FileInputStream in = new FileInputStream(f);
//	    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	    BufferedReader reader = new BufferedReader(new FileReader(f));

	    String line;
	    long rowheader = 0;
	    theaders.add(rowheader);
	    int feature_num = 0;
	    while ((line = reader.readLine()) != null) {
	      String[] items = line.trim().split(" ");
	      if (items.length == 0) {
	        continue;
	      }
//	      System.out.print(rowheader+" ");
	      rowheader += items.length - 1;
	      
	      theaders.add(rowheader);
	      tlabels.add(Float.valueOf(items[0]));

	      for (int i = 1; i < items.length; i++) {
	        String[] tup = items[i].split(":");
//	        assert tup.length == 2;
	        if(tup.length == 2) {
	        	tdata.add(Float.valueOf(tup[1]));
	        	tindex.add(Integer.valueOf(tup[0]));
	        }
	      }
	    }

	    spData.labels = ArrayUtils.toPrimitive(tlabels.toArray(new Float[tlabels.size()]));
	    spData.data = ArrayUtils.toPrimitive(tdata.toArray(new Float[tdata.size()]));
	    spData.colIndex = ArrayUtils.toPrimitive(tindex.toArray(new Integer[tindex.size()]));
	    spData.rowHeaders = ArrayUtils.toPrimitive(theaders.toArray(new Long[0]));
	    
	    
	    return spData;
	  }
	
}
