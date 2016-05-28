package test;

import java.util.ArrayList;
import java.util.HashMap;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class WekaTest {
	private static final int m_FeatureNum = 572;
	private static Instance m_predictInstance;
	private static final int m_FeatureCount=1000;
	
	public static void main(String[] args){
//		createInstance();
		for(int i=1;i<688;i++){
			System.out.print(i+",");
		}
	}
	
	public static void createInstance(){
		ArrayList<Attribute> atts = new ArrayList();
		Attribute target = new Attribute("class");
		atts.add(target);
		for ( int j = 1; j < m_FeatureNum; j++ ){
			Attribute atr = new Attribute(String.valueOf(j));
			atts.add(atr);
		}
		
		Instances dataRaw = new Instances("Instances",atts,0);	
		double[] instanceValue = new double[dataRaw.numAttributes()];
		dataRaw.add(new DenseInstance(1.0, instanceValue));
		dataRaw.setClassIndex(0);
		m_predictInstance = dataRaw.get(0);
		m_predictInstance.setClassValue(1.234);
		HashMap<Integer,Double> m_FeatureMap = new HashMap<Integer,Double>();
		
		for(int i=0;i<m_FeatureCount;i++){
			if(i==0){
				m_FeatureMap.put(i,1.11);
			}else{
				m_FeatureMap.put(i, i*1.0);
			}
		}
		
		int total = 0;
		
		for (int i = 0,j=1; i < m_FeatureCount && j<m_FeatureNum; i ++) {
			
			boolean flag1=((i>=0 && i<473)||(i>481 && i<540)||(i>599 && i<608));
	        boolean flag2=((i>619 && i<628)||(i>639 && i<648)||(i>659 && i<668)||(i>679 && i<688));
	        
//	        System.out.println(flag1+"\t"+flag2);
	        
	        if(i>=0 && i<473){
	        	j=i+1;
	        }else if(i>481 && i<540){
	        	j=i-8;
	        }else if(i>599 && i<608){
	        	j=i-68;
	        }else if(i>619 && i<628){
	        	j=i-80;
	        }else if(i>639 && i<648){
	        	j=i-92;
	        }else if(i>659 && i<668){
	        	j=i-104;
	        }else if(i>679 && i<688){
	        	j=i-116;
	        }
	        
			if ( m_FeatureMap.get(i) != null &&(flag1 || flag2)){
				m_predictInstance.setValue(j,(double) m_FeatureMap.get(i));
				total++;
			}
//			System.out.println(j+"\t"+m_predictInstance.index(i));
		}
		System.out.println(m_predictInstance);
		System.out.println(m_predictInstance.classValue()+"\t"+m_predictInstance.numAttributes());
		System.out.println(total);
	}
}
