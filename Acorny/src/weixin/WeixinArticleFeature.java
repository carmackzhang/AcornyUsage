package weixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

public class WeixinArticleFeature {

	private static List<Entry<String,String>> features;
	
	
	//从文件FixSogouWeixinCluster_step2中提取特征
	public static boolean extractFeature(String FixSogouWeixinCluster_step2){
		File file = new File(FixSogouWeixinCluster_step2);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String tmp = "";
			while((tmp = br.readLine()) != null){
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
