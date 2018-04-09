package weixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class PageShowType {

	public static void main(String []args){
		if(args.length < 3){
			System.out.println("Usage ERROR need parameters : FixSogouWeixinCluster query_good query_bad");
			return;
		}
		
		genWeixinData(args[0], args[1], args[2]);
		
	}
	
	public static void genWeixinData(String filename, String good, String bad){
		try {
			BufferedReader inputfile = new BufferedReader(new FileReader(new File(filename)));
			BufferedReader query_good = new BufferedReader(new FileReader(new File(good)));
			BufferedReader query_bad = new BufferedReader(new FileReader(new File(bad)));
			FileWriter page1 = new FileWriter("./pageShowType1");
			FileWriter page0 = new FileWriter("./pageShowType0");
			
			HashSet<String> query_good_set = new HashSet<String>();
			HashSet<String> query_bad_set = new HashSet<String>();
			
			String tmp = "";
			while((tmp=query_good.readLine())!=null){
				query_good_set.add(tmp);
			}
			String tmp1 = "";
			while((tmp1=query_bad.readLine())!=null){
				query_bad_set.add(tmp1);
			}
			
			System.out.println("good size:"+query_good_set.size()+", bad size:"+query_bad_set.size());
			String tmp2 = "";
			int pNum1 = 0;
			int pNum0 = 0;
			while((tmp2=inputfile.readLine())!=null){
				int queryStart = tmp2.indexOf("query=");
				int queryEnd = tmp2.indexOf(",", queryStart);
				
				if(queryEnd>queryStart && queryEnd<queryStart+30){
					String query = tmp2.substring(queryStart+6, queryEnd);
					boolean t0 = tmp2.contains("pageShowType=0");
					boolean t1 = tmp2.contains("pageShowType=1");
					
					if(t0 && query_bad_set.contains(query)){
						page0.write(tmp2+"\n");
						pNum0++;
						System.out.println("query0:"+query);
					}
					
					if(t1 && query_good_set.contains(query)){
						page1.write(tmp2+"\n");
						pNum1++;
						System.out.println("query1:"+query);
					}
				}
			}
			System.out.println("page1 size:"+pNum1+", page0 size:"+pNum0);
			inputfile.close();
			query_good.close();
			query_bad.close();
			page0.flush();
			page1.flush();
			page0.close();
			page1.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
