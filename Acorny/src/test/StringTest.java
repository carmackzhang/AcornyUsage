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
//		System.out.println(isAllLetterOrDigit(" 1024 cmk "));
//		System.out.println(getMedPbValue("MedPb:107 URank:43 URAns:40 Nor:608 FF:889 KC:889 END:889 d(2:0)"));
//		String vrid = null;
//		System.out.println("11009801".equals(vrid));
//		String query1 = "（钟祥）（最新）（招聘）（信）";
//		ArrayList<String> names = new ArrayList<String>();
//		names.add("钟祥最新");
//		System.out.println(hasQueryMatch(query1,"",names));
//		int attrNum = 50;
//		printAttr(attrNum);
//		String xml = "<xml>";
//		String tplid = "3007";
//		
//		System.out.println(appendTest(xml, tplid));
		
//		emptyTest();
//		printTest();
//		charTest();
//		System.out.println(isAllDigit("1234 "));
//		String str = "壮阳药哪个好＿有问必答＿快速问医生";
//		System.out.println(str.indexOf("_"));
//		System.out.println(str.indexOf("＿"));
//		StringBuilder sb = new StringBuilder();
//		String tmp = "clustervr=\"0\"";
//		sb.append(tmp+"   aa");
//		int start = sb.indexOf(tmp);
//		sb.toString().replace(tmp, "clustervr=\"2\"");
//		sb.replace(start, start+tmp.length(), "clustervr=\"2\"");
//		System.out.println(sb.toString());
//		int classid = 0;
//		int tplid = 0;
//		String horizon_type = "other";
//		switch(horizon_type){
//		case "japan":classid=30010141;tplid=18094;break;
//		case "korea":classid=30010142;tplid=18094;break;
//		case "english":classid=30010140;tplid=18094;break;
//		default:classid=1;tplid=1;break;
//		}
//		System.out.println(classid + "\t" +tplid);
		
		printFeature(44);
	}

	private static void printFeature(int num) {
		for(int i=1;i < num; i++) {
			System.out.print("f"+i+",");
		}
		System.out.println("label");
	}

	public static String reverseString(String str) {
		String res = "abcde";
		char[] array = str.toCharArray();
		for(int i=0; i < array.length; i++) {
			
		}
		return res;
	}
	
	public static void charTest() {
		char dict[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		StringBuffer buffer = new StringBuffer();
		buffer.reverse();
		buffer.append("13102");
		char x[] = new char[3];
		x[0] = dict[10 >> 2 & 0xf];//2
		x[1] = dict[(60 >>> 2) - (10 ^ 9)];//c
		x[2] = dict[(139 | 193) % 5];//3
		buffer.insert(2, Integer.parseInt(String.valueOf(x), 16));//13707102
		for (int i = buffer.length(); i > 0; i--) {
			System.out.print(buffer.charAt(i - 1));
		}
	}
	
	private static void printTest() {
		StringBuilder sb = new StringBuilder();
		sb.append("abc\n");
		sb.append("cde");
		System.out.println(sb.toString());
	}
	
	private static void emptyTest() {
		String a = null;
		String b = "";
		String c = " ";
//		System.out.println(a.isEmpty()+","+b.isEmpty());
		System.out.println(b.isEmpty()+","+b.length()+"\t"+c.isEmpty()+","+c.length());
	}

	public static String appendTest(String xml, String tplid){
		xml = xml+tplid;
		return xml;
	}
	
	public static void printAttr(int attrNum){
		for(int i=0;i<attrNum;i++){
			System.out.print("attr"+i+",");
		}
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
			if (query.charAt(i) > '9' || query.charAt(i) < '0')
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
		String url = "http://g.pconline.com.cn/dl/64569.htmlj";
		int pos = url.indexOf("/",9);
		System.out.println(url.substring(0,pos+1));
		int pos1 = url.lastIndexOf("/");
		System.out.println(url.substring(0,pos1+1));
		int pos2 = url.lastIndexOf("/", url.length());
		System.out.println(url.substring(0,pos2+1));
	}
	
	private static void matchTest(){
		String s1 = "北京北京到广州的高铁时刻表北京到武汉的高铁";
		String s2 = "<timeorder><![CDATA[07070906]]></timeorder>";
		String s3 = "<timeorder>07070906</timeorder>";
		
		String pattern = "[\u4e00-\u9fa5]*到[\u4e00-\u9fa5]*高铁";
		String pattern2 = "(.*)到(.*)高铁(.*)";
		Pattern p1 = Pattern.compile(pattern2);
		Matcher m1 = p1.matcher(s1);
//		while(m1.find()){
//			System.out.println(m1.group(0));
//		}
		
		String pattern3 = "<timeorder[<!\\[CDATA\\[]{0,}([\\d]{8})[\\]]{0,}[>]{0,}</timeorder>";
		Pattern p3 = Pattern.compile(pattern3);
		Matcher m3 = p3.matcher(s2);
//		while(m3.find()){
//			System.out.println(m3.group(1));
//		}
		
		String title = "广东潮汕麻将v1.0.2";
		String versionP = "v[\\d]\\.[\\d]\\.[\\d]";
	
		Pattern p4 = Pattern.compile(versionP);
		Matcher m4 = p4.matcher(title);
		while(m4.find()){
			System.out.println(m4.group(0));
		}
		
		String frontContent = "苏轼,字子瞻号东坡居士";
		
		Pattern pname = Pattern.compile("(.*?)[字|号](.*)");
		Matcher mname = pname.matcher(frontContent);
		
		while(mname.find()) {
			int start = mname.start();//下标
			int end = mname.end();
			int count = mname.groupCount();
			System.out.println(start+"\t"+end+"\t"+count);
			for(int i=0; i <= count; i++) {
				System.out.println(mname.group(i));
			}
		}
		if(frontContent.contains("(.*)字(.*)") || frontContent.contains("###号###")){
			System.out.println("content is contained");
		}
		
		String health = "health:37;knowledge:6;";
		Pattern hp = Pattern.compile("health:([\\d]+)");
		Matcher hm = hp.matcher(health);
		System.out.println(hm.groupCount());
		int groupCount = hm.groupCount();
		int health_val = 0;
		while(hm.find()) {
			if(groupCount>=1) health_val = Integer.parseInt(hm.group(1));
			System.out.print(health_val);
		}
	}
}
