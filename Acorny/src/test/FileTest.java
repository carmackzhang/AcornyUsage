package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileTest {

	public static void main(String[] args) {
		String filename = "data/wap英文翻译点出子域list2.csv";
		formatContent(filename);
	}
	
	public static void formatContent(String fileInt){
		try {
			FileReader fr = new FileReader(new File(fileInt));
			BufferedReader br = new BufferedReader(fr);
			String tmp;
			while((tmp=br.readLine())!=null){
				System.out.print(tmp+",");
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
