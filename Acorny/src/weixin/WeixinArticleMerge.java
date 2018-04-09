package weixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class WeixinArticleMerge {

	private static HashMap<String,Double[]> queryPVCTR;
//	private static String DATA_PATH = "/search/odin/wujunjie/weixin/weixin_filt/";
	private static String DATA_PATH = "data/";
	private static String outputFile = DATA_PATH+"weixinQueryResult.txt";
	private static String outputFile2 = DATA_PATH+"weixinQueryResult2.txt";
	private static String letterResultFile = DATA_PATH+"weixinLetterResult.txt";
	private static String digitResultFile = DATA_PATH+"weixinDigitResult.txt";
	private static String keywordResultFile = DATA_PATH+"weixinKeywordResult.txt";
	
	private static BigDecimal threshold = new BigDecimal(0.2);
	
    static final char SBC_CHAR_START = 65281; // 全角！  全角对应于ASCII表的可见字符从！开始，偏移值为65281 
  
    static final char SBC_CHAR_END = 65374; // 全角～ 全角对应于ASCII表的可见字符到～结束，偏移值为65374 
     
    static final int CONVERT_STEP = 65248; // 全角半角转换间隔   ASCII表中除空格外的可见字符与对应的全角字符的相对偏移 
  
    static final char SBC_SPACE = 12288; // 全角空格 12288  全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理
  
    static final char DBC_SPACE = ' '; // 半角空格  半角空格的值，在ASCII中为32(Decimal) 
    
	
	public static void main(String[] args) {
//		String letter = "oppo";
//		String digit = "1234560";
//		System.out.println(isAllLetter(letter)+"\t"+isAllDigit(digit));
		
		if(args.length < 2) {
			System.err.println("Need charset, such as: utf8 gb2312; And pv>threshold, such as threshold=20");
			return;
		}
		
		mergeData(args[0], args[1]);
		displayResult();
//		outputResult();
//		filterByPvCTR();
//		countQuery();
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
	public static void mergeData(String charSet, String pv_threshold){
		File f1 = new File(DATA_PATH);
		queryPVCTR = new HashMap<String,Double[]>();
		int total = 0;
//		String charSet = "utf8";
		String testWord = "109路公交车路线";
		
		if(f1.isDirectory()){
			File[] files = f1.listFiles();
			for(File f : files){
				if(f.getName().startsWith("part-r-")){
//					System.out.println("file name:"+f.getName());
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),charSet));
						String tmp = "";
						String key = "";
						
						while((tmp=br.readLine())!=null){
							String[] strs = tmp.split("\t");
							total++;
							Double[] values = {new Double(0),new Double(0),new Double(0)};
							double pv = 0;
							double cl = 0;
							double ctr = 0;
							
							if(strs.length==3){
								key = strs[0];
								
								if(key.trim().isEmpty()) continue;
//								if(isAllDigit(key) || isAllLetter(key)) continue;
								
								pv = Double.parseDouble(strs[1]);
								cl = Double.parseDouble(strs[2]);
								ctr = cl/pv;
								
							}else if(strs.length == 4){
								key = strs[0];
								if(key.trim().isEmpty()) continue;
//								if(isAllDigit(key) || isAllLetter(key)) continue;
								
								pv = Double.parseDouble(strs[1]);
								cl = Double.parseDouble(strs[2]);
								ctr = Double.parseDouble(strs[3]);
							}
							
							values[0] = pv;
							values[1] = cl;
							values[2] = ctr;
							
							if(queryPVCTR.containsKey(key)){
								values[0] += queryPVCTR.get(key)[0];
								values[1] += queryPVCTR.get(key)[1];
								values[2] += queryPVCTR.get(key)[2];
							}
//							if(key.equals(testWord))
//								System.out.println(total+"\t"+key+"\t"+values[0]+"\t"+values[1]+"\t"+values[2]);
							if(isAllDigit(pv_threshold) && pv>Double.parseDouble(pv_threshold))
								queryPVCTR.put(full2HalfChange(key).toLowerCase(), values);
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
//			String key = "";
//			Double[] values = {new Double(0),new Double(0),new Double(0)};
			
			while(iter.hasNext()){
				Entry<String, Double[]> entry = (Entry<String, Double[]>) iter.next();
//				key = entry.getKey();
//				values = entry.getValue();
//				System.out.print(entry.getKey()+"\t"+entry.getValue()[0]+"\t"+entry.getValue()[1]+"\t"+entry.getValue()[2]+"\n");
				fw.write(entry.getKey()+"\t"+entry.getValue()[0]+"\t"+entry.getValue()[1]+"\t"+entry.getValue()[2]+"\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static String full2HalfChange(String src) {  
        if (src == null) {  
            return src;  
        }  
        StringBuilder buf = new StringBuilder(src.length());  
        char[] ca = src.toCharArray();  
        for (int i = 0; i < src.length(); i++) {  
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内  
                buf.append((char) (ca[i] - CONVERT_STEP));  
            } else if (ca[i] == SBC_SPACE) { // 如果是全角空格  
                buf.append(DBC_SPACE);  
            } else { // 不处理全角空格，全角！到全角～区间外的字符  
                buf.append(ca[i]);  
            }  
        }  
        return buf.toString();  
    }  
	
}
