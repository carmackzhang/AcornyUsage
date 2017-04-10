package wekaUsage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;  
  
public class WekaTestInstance   
{  
    Instances m_Data = null;  
    Classifier m_Classifier = null;  
    public WekaTestInstance() throws FileNotFoundException, Exception  
    {  
        m_Classifier = (RandomForest)SerializationHelper.read(new FileInputStream("data/test2_4attr.model"));    
          
        String nameOfDataset = "messDataset";  
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();  
        attributes.add(new Attribute("aa"));  
        attributes.add(new Attribute("bb"));  
          
        ArrayList<String> fvNominalVal = new ArrayList<String>();  
        fvNominalVal.add("blue");  
        fvNominalVal.add("gray");  
        fvNominalVal.add("black");                 
        attributes.add(new Attribute("Nominal", fvNominalVal));  
          
        ArrayList<String> classValues = new ArrayList<String>();  
        classValues.add("pos");  
        classValues.add("neg");  
        attributes.add(new Attribute("Class", classValues));  
        m_Data = new Instances(nameOfDataset, attributes, 10);  
        m_Data.setClassIndex(m_Data.numAttributes()-1);  
    }  
    
    public void classifyMessage(double aa,double bb,String nominal) throws Exception   
    {  
        Instances testset = m_Data.stringFreeStructure();  
        Instance instance = makeInstance(aa,bb,nominal,testset);  
        System.out.println(m_Data.numAttributes());  
        System.out.println(instance);  
        double predicted = m_Classifier.classifyInstance(instance);  
        System.out.println("predicted:"+predicted);  
        System.out.println("Message classified as : " +  
                m_Data.classAttribute().value((int)predicted));  
    }  
    
    private Instance makeInstance(double aa,double bb,String nominal,Instances data)   
    {  
        Instance instance = new DenseInstance(4);  
        instance.setDataset(data);  
        Attribute aaAtt = data.attribute("aa");  
        Attribute bbAtt = data.attribute("bb");  
        Attribute nominalAtt = data.attribute("Nominal");  
          
        instance.setValue(aaAtt, aa);  
        instance.setValue(bbAtt, bb);  
        instance.setValue(nominalAtt, nominal);  
          
        return instance;  
    }
    
    public static void main(String[] args) throws Exception   
    {  
        WekaTestInstance wTestInstance = new WekaTestInstance();  
        wTestInstance.classifyMessage(5.6,9.9,"gray");  
    }  
  
}  