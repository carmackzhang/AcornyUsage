package weixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class QueryPvCtr {

	private static HashMap<String,Double[]> queryPVCTR;
	private static String outputFile = "conf/weixinQueryResult.txt";
	private static String outputFile2 = "conf/weixinQueryResult2.txt";
	private static String letterResultFile = "conf/weixinLetterResult.txt";
	private static String digitResultFile = "conf/weixinDigitResult.txt";
	private static String keywordResultFile = "conf/weixinKeywordResult.txt";
	
	private static BigDecimal threshold = new BigDecimal(0.2);
	
	public static void main(String[] args) {
//		mergeData();
//		displayResult();
//		outputResult();
//		filterByPvCTR();
		countQuery();
	}
	
	/**
	 * 统计出公众号的query中：纯字母和纯数字的query有多少
	 */
	public static void countQuery(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(outputFile));
			FileWriter fw = new FileWriter(digitResultFile);
			FileWriter fw1 = new FileWriter(letterResultFile);
			FileWriter fw2 = new FileWriter(keywordResultFile);
			
			String tmp = "";
			int letterTotal = 0;
			int total1 = 0;
			int digitTotal = 0;
			
			while((tmp=br.readLine())!=null){
				String[] strs = tmp.split("\t");
				StringBuilder query = new StringBuilder();
				if(strs.length==4){
					query.append(strs[0]);
				}else if(strs.length>4){
					for(int i=0;i<strs.length-3;i++){
						query.append(strs[i]);
					}
				}else{
					continue;
				}
				String word = query.toString();
				if(isAllLetter(word)){
					letterTotal++;
					fw1.write(word+"\n");
				}else if(isAllDigit(word)){
					digitTotal++;
					fw.write(word+"\n");
				}else if(word.endsWith("网") || word.contains("网站") || word.contains("官网")){
					total1++;
					fw2.write(word+"\n");
				}
			}
			fw2.flush();
			fw2.close();
			fw1.flush();
			fw1.close();
			fw.flush();
			fw.close();
			System.out.println("total:"+letterTotal+"\tdigitTotal:"+digitTotal+"\ttotal1:"+total1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 纯字母返回true，否则返回false
	 * @param query
	 * @return
	 */
	public static boolean isAllLetter(String query){
    	String temp = query.toLowerCase();
    	for(int i = 0; i < temp.length(); i++)
    		if (temp.charAt(i) == ' ' || temp.charAt(i) == '\t' || (temp.charAt(i) >= 'a' && temp.charAt(i) <= 'z')){
    			continue;
    		} else{
    			return false;
    		}
    	return true;
    }
	
	public static boolean isAllDigit(String query){
    	String temp = query.toLowerCase();
    	for(int i = 0; i < temp.length(); i++)
    		if (temp.charAt(i) == ' ' || temp.charAt(i) == '\t' || (temp.charAt(i) >= '0' && temp.charAt(i) <= '9')){
    			continue;
    		} else{
    			return false;
    		}
    	return true;
    }
	
	/**
	 * 通过设置pv和ctr来过滤query，输出到outputFile2
	 */
	public static void filterByPvCTR(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(outputFile));
			FileWriter fw = new FileWriter(outputFile2);
			String tmp = "";
			BigDecimal bd;
			while((tmp=br.readLine())!=null){
				String[] strs = tmp.split("\t");
				String query = strs[0];
				double pv = Double.parseDouble(strs[1]);
				double cl = Double.parseDouble(strs[2]);
				double ctr = cl/pv;
				bd = new BigDecimal(ctr);
				bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
				if(pv>20 && bd.compareTo(threshold)==-1){
					fw.write(query+"\t"+pv+"\t"+cl+"\t"+bd+"\n");
				}
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 把多个文件的数据合并，保存为query pv cl ctr
	 */
	public static void mergeData(){
		File f1 = new File("conf/");
		queryPVCTR = new HashMap<String,Double[]>();
		int total = 0;
		
		if(f1.isDirectory()){
			File[] files = f1.listFiles();
			for(File f : files){
				if(f.getName().startsWith("weixinQueryCtr0")){
					try {
						BufferedReader br = new BufferedReader(new FileReader(f));
						String tmp = "";
						while((tmp=br.readLine())!=null){
							String[] strs = tmp.split("\t");
							total++;
							if(strs.length>=4){
								String key = strs[0];
						         
								key = URLDecoder.decode(key, "gb2312");
								key = URLDecoder.decode(key, "gb2312");
								
								Double[] values = {new Double(0),new Double(0),new Double(0)};
								double pv = Double.parseDouble(strs[1]);
								double cl = Double.parseDouble(strs[2]);
								double ctr = Double.parseDouble(strs[3]);
								
								values[0] = pv;
								values[1] = cl;
								values[2] = ctr;
								
								if(queryPVCTR.containsKey(key)){
									values[0] += queryPVCTR.get(key)[0];
									values[1] += queryPVCTR.get(key)[1];
									values[2] += queryPVCTR.get(key)[2];
									System.out.println(total+"\t"+key+"\t"+values[0]+"\t"+values[1]+"\t"+values[2]);
								}
								queryPVCTR.put(key, values);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * system.out输出结果
	 */
	public static void displayResult(){
		if(queryPVCTR==null) return;
		Iterator<Entry<String, Double[]>> iter = queryPVCTR.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, Double[]> entry = (Entry<String, Double[]>) iter.next();
			System.out.print(entry.getKey()+"\t"+entry.getValue()[0]+"\t"+entry.getValue()[1]+"\t"+entry.getValue()[2]+"\n");
		}
	}
	
	/**
	 * 输出结果到文件
	 */
	public static void outputResult(){
		if(queryPVCTR==null) return;
		Iterator<Entry<String, Double[]>> iter = queryPVCTR.entrySet().iterator();
		try {
			FileWriter fw = new FileWriter(new File(outputFile));
			while(iter.hasNext()){
				Entry<String, Double[]> entry = (Entry<String, Double[]>) iter.next();
//				System.out.print(entry.getKey()+"\t"+entry.getValue()[0]+"\t"+entry.getValue()[1]+"\t"+entry.getValue()[2]+"\n");
				fw.write(entry.getKey()+"\t"+entry.getValue()[0]+"\t"+entry.getValue()[1]+"\t"+entry.getValue()[2]+"\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
