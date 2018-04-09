package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class XmlTest {

	
	public static void main(String[] args) {
//		readXml("data/dataFile70080300.xml");
		String str = "谢邀　（╭￣３￣）╭恭喜中国队首次获得场地自行车金牌！";
		System.out.println(str.replace("（╭￣３￣）╭", ""));
		System.out.println("finished");
	}
	
	private static void readXml(String fileName){
		File file = new File(fileName);
		
		try {
//			FileReader fr = new FileReader(file);
			InputStreamReader fr = new InputStreamReader(new FileInputStream(file),"utf8");
			BufferedReader br = new BufferedReader(fr);
//			FileWriter fw = new FileWriter(new File("output.xml"));
			OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file),"gbk");
			
			String tmp = "";
			while((tmp=br.readLine())!=null){
				fw.append(tmp+"\n");
			}
			fw.flush();
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
