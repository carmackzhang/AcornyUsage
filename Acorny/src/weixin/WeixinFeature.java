package weixin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class WeixinFeature {

	private static HashMap<Integer,Integer> featureNumPair;
	private static String vrFeature = "./vrFeature";
	private static String vrFeatureFinal = "./vrFeatureFinal";
	private static String query = "./query_rankPos_final";
	private static String rankPosData = "data/rankPosData";
	private static String rankPosDataFinal = "data/rankPosDataFinal";
	
	public static void main(String[] args){
//		analysisFeatureNum();
//		displayResult();
//		extractVRFeature();
//		vrFeatureMerge();
		rankPosData();
//		printAttr(124);
	}
	
	public static void printAttr(int num){
		System.out.print("class,");
		for(int i=1;i<(num-1);i++){
			System.out.print("attr"+i+",");
		}
		System.out.print("attr"+(num-1));
	}
	public static void rankPosData(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(rankPosData));
			BufferedWriter bw = new BufferedWriter(new FileWriter(rankPosDataFinal));
			
			String tmp;
			LinkedHashSet<String> attrs = new LinkedHashSet<String>();
			LinkedHashMap<String,String> kvmap = new LinkedHashMap<String,String>();
			int total = 0;
			
			while((tmp=br.readLine())!=null){
				total++;
				String[] strs = tmp.split(",");
				for(String str : strs){
					String[] kvs = str.split("=");
					if(kvs.length == 2){
						String key = kvs[0];
						attrs.add(key);
						kvmap.put(key, "0");
					}
				}
			}
			
//			System.out.println(attrs.size()+"\t"+kvmap.size());
			br = new BufferedReader(new FileReader(rankPosData));
			
			while((tmp=br.readLine())!=null){
				String[] strs = tmp.split(",");
//				System.out.println("strs len:"+strs.length);
				for(String str : strs){
					String[] kvs = str.split("=");
					if(kvs.length == 2){
						String key = kvs[0];
						String val = kvs[1];
						kvmap.put(key, val);
//						System.out.println("len 2:"+key+"="+val);
					}else{
//						System.out.println("len 1:"+kvs[0]+"="+kvs[1]);
					}
				}
				printMap(kvmap,bw);
			}
			
			bw.flush();
			bw.close();
			
			Iterator iter = attrs.iterator();
			while(iter.hasNext()){
				System.out.print(iter.next()+",");
			}
//			System.out.println("\n"+attrs.size()+"\t"+total);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void printMap(Map<String, String> kvmap, BufferedWriter bw) {
		Iterator<Entry<String, String>> iter = kvmap.entrySet().iterator();
//		System.out.println("size:"+kvmap.size());
		if(kvmap.size() != 127) return;
		StringBuilder sb = new StringBuilder();
		try {
			while(iter.hasNext()){
				Entry<String,String> entry =(Entry<String, String>) iter.next();
				String val = entry.getValue();
				sb.append(val+",");
			}
			sb.append("\n");
//			System.out.print(sb.toString());
			bw.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void vrFeatureMerge(){
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(vrFeatureFinal));
			BufferedReader br1 = new BufferedReader(new FileReader(query));
			HashMap<String,String> map = new HashMap<String,String>();
			String tmp = "";
			String tmp1 = "";
			while((tmp1=br1.readLine())!=null){
				String[] strs1 = tmp1.split("<###>");
//				System.out.println("strsLen:"+strs1.length);
				if(strs1.length<2){
					continue;
				}
				map.put(strs1[0],strs1[1]);
			}
			while((tmp=br.readLine())!=null){
				String[] strs = tmp.split("<###>");
				if(strs.length<2){
					continue;
				}
				String query = strs[0];
//				System.out.println("query:"+query);
				if(map.containsKey(query)){
					System.out.println(strs[1]+","+map.get(query));
				}
			}
//			System.out.println(map.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 提取query对应的特征值
	 */
	public static void extractVRFeature(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(vrFeature));
			FileWriter fw = new FileWriter(vrFeatureFinal);
			HashMap<String,String> map = new HashMap<String,String>();
			HashSet<Integer> set = new HashSet<Integer>();
			int[] nums = {77,115,148,150,162,187,224,309,412,429,430,470,482,484,485,490,536};
			for(int i=0;i<nums.length;i++){
				set.add(nums[i]);
			}
			
//			System.out.println(set.size());
			
			String tmp = "";
			while((tmp=br.readLine())!=null){
				String[] tmps = tmp.split(" ");
//				System.out.println(tmps.length);
				String[] strs = tmps[0].split(":");
				if(strs.length<2){
					continue;
				}
				String query = strs[1];
//				System.out.print(query+"<###>");
				fw.write(query+"<###>");
				
				for(int i=1;i<688;i++){
					
					boolean flag1 = (i>=1&&i<=425)&&((i>=1&&i<=60)||(i>=360&&i<=374)||(i>=400&&i<=408)||(i>=420&&i<=425));
					boolean flag2 = (i>=440&&i<=523)&&((i>=440&&i<=445)||(i>=460&&i<=465)||(i>=501&&i<=513)||(i>=521&&i<=523));
					boolean flag3 = (i>=600&&i<=687)&&((i>=600&&i<=607)||(i>=620&&i<=627)||(i>=640&&i<=647)||(i>=660&&i<=667)||(i>=680&&i<=687));
					
					if(flag1 || flag2 || flag3 || set.contains(i)){
						map.put(i+"", 0+"");
					}
				}
				
				for(int i=1;i<tmps.length;i++){
					strs = tmps[i].split(":");
					if(strs.length<2){
						continue;
					}
					String index = strs[0];
					String value = strs[1];
					
					if(map.containsKey(index)){
						map.put(index, value);
					}
				}
				Iterator iter = map.entrySet().iterator();
				while(iter.hasNext()){
					Entry entry = (Entry)iter.next();
					if(Integer.parseInt((String)entry.getKey())<687){
//						System.out.print(entry.getValue()+",");
						fw.write(entry.getValue()+",");
					}
				}
//				System.out.println(map.get("687"));
				fw.write(map.get("687")+"\n");
				
//				System.out.println("size:"+map.size()+"\t"+map.get("1"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 统计各个文件特征的总数
	 */
	public static void analysisFeatureNum(){
		File f1 = new File("conf/");
		featureNumPair = new HashMap<Integer,Integer>();
		
		if(f1.isDirectory()){
			File[] files = f1.listFiles();
			for(File f : files){
				if(f.getName().startsWith("vrFeatureNum")){
					try {
						BufferedReader br = new BufferedReader(new FileReader(f));
						String tmp = "";
						while((tmp=br.readLine())!=null){
							String[] strs = tmp.split("\t");
							if(strs.length==2){
								Integer key = Integer.valueOf(strs[0]);
								Integer value = Integer.valueOf(strs[1]);
								if(featureNumPair.containsKey(key)){
									value += featureNumPair.get(key);
								}
								featureNumPair.put(key, value);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void displayResult(){
		if(featureNumPair==null) return;
		Iterator<Entry<Integer, Integer>> iter = featureNumPair.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Integer, Integer> entry = (Entry<Integer, Integer>) iter.next();
			System.out.print(entry.getKey()+"\t"+entry.getValue()+"\n");
		}
	}
	
}
