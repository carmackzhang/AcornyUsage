package test;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTest {

	public static void main(String[] args) {
//		indexTest();
//		matchTest();
//		mingyiTest("创伤性枢椎前滑脱");
//		System.out.println(isAllLetterOrDigit(" baixue bing "));
//		System.out.println(getMedPbValue("MedPb:107 URank:43 URAns:40 Nor:608 FF:889 KC:889 END:889 d(2:0)"));
//		String vrid = null;
//		System.out.println("11009801".equals(vrid));
		String query1 = "（钟祥）（最新）（招聘）（信）";
		ArrayList<String> names = new ArrayList<String>();
		names.add("钟祥最新");
		System.out.println(hasQueryMatch(query1,"",names));
	}
	
	
	public static int hasQueryMatch(String query1,String qcString, ArrayList<String> names){
    	if(query1==null || query1.isEmpty() || names==null || names.isEmpty()) return 0;
    	ArrayList<String> terms = new ArrayList<String>();
    	int start = 0;
    	int end = 0;
    	int termMatch = 0;
    	for(int i=0;i<query1.length();i++){
    		start = query1.indexOf("（",end);
    		if(start==-1) continue;
    		end = query1.indexOf("）",start);
    		if(end==-1) break;
			terms.add(query1.substring(start+1, end));
			i += end-start;
		}
    	for(String str : terms){
    		for(String name : names){
    			if(name.equals(str)){
    				termMatch+=2;
    			}else if(name.contains(str)){
    				termMatch++;
    			}
    		}
    	}
    	return termMatch;
    }
	
	
	public static int getMedPbValue(String rerankInfo){
		if(rerankInfo == null || rerankInfo.trim().isEmpty() || !rerankInfo.contains("MedPb")) return 0;
		String[] strs = rerankInfo.split(" ");
		int res = 0;
		for(String str : strs){
			String[] tmps = str.split(":");
			if(tmps.length == 2 && tmps[0].contains("MedPb")){
				System.out.println(tmps[0]+"\t"+tmps[1]);
				if(isAllDigit2(tmps[1])){
					res = Integer.parseInt(tmps[1]);
					System.out.println(res);
					break;
				}
			}
		}
		return res;
	}
	
	public static boolean isAllDigit2(String query){
		if(query==null) return false;
    	for(int i = 0; i < query.length(); i++)
    		if (query.charAt(i) > 57 || query.charAt(i) < 48){//ascii码比较
    			return false;
    		}
    	return true;
    }
	
	public static boolean isAllDigit(String query) {
		for (int i = 0; i < query.length(); i++){
			System.out.println(i+"\t"+query.charAt(i)+"\tlen="+query.length());
			if (query.charAt(i) > 9 || query.charAt(i) < 0)
				return false;
		}
		return true;
	}
	
	public static boolean isAllLetterOrDigit(String query) {
		String temp = query.toLowerCase();
		for (int i = 0; i < temp.length(); i++)
			if (temp.charAt(i) == ' ' || temp.charAt(i) == '\t' || (temp.charAt(i) >= 'a' && temp.charAt(i) <= 'z')
					|| (temp.charAt(i) >= '0' && temp.charAt(i) <= '9')) {
				continue;
			} else {
				return false;
			}
		return true;
	}
	
	private static void mingyiTest(String query){
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/DiseaseNameData.txt"));
			String tmp = "";
			while((tmp=br.readLine())!=null){
				if(query.toLowerCase().equals(tmp)){
					System.out.println("exact");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void weixinTest(){
		String urls = "\"http://mp.weixin.qq.com/count/oIWsFt9Y-3fFDb8V1-kjknHLfjuQ/2651167528/3\",\"http://mp.weixin.qq.com/count/oIWsFt6oGF0VHbQEbcMWSB5em9Gw/2651193544/1\"";
		String[] url = urls.split("\"");
		for(String u :url){
			if(u.contains("/")){
				System.out.println(u);
				int end = u.indexOf("/",u.indexOf("count/")+6);
				System.out.println(u.substring(0,end));
			}
		}
	}
	private static void indexTest(){
		String tmp = "abcdefghijklmnopqrstuvwxyz";
		int start = tmp.indexOf("d");
		int end = tmp.indexOf("z",start);
		System.out.println(start+"\t"+end);
	}
	
	private static void matchTest(){
		String s1 = "北京北京到广州的高铁时刻表北京到武汉的高铁";
		String s2 = "<timeorder><![CDATA[07070906]]></timeorder>";
		String s3 = "<timeorder>07070906</timeorder>";
		
		String pattern = "[\u4e00-\u9fa5]*到[\u4e00-\u9fa5]*高铁";
		String pattern2 = "(.*)到(.*)高铁(.*)";
		Pattern p1 = Pattern.compile(pattern2);
		Matcher m1 = p1.matcher(s1);
		while(m1.find()){
			System.out.println(m1.group(0));
		}
		
		String pattern3 = "<timeorder[<!\\[CDATA\\[]{0,}([\\d]{8})[\\]]{0,}[>]{0,}</timeorder>";
		Pattern p3 = Pattern.compile(pattern3);
		Matcher m3 = p3.matcher(s2);
		while(m3.find()){
			System.out.println(m3.group(1));
		}
	}
}
