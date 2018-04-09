package word_seg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.FilterModifWord;

public class WordSeg {

	public static void main(String[] args) {
//		testSeg();
		if(args.length < 2){
			System.out.println("need files : input output");
		}
		segWord(new File(args[0]), new File(args[1]));
	}
	
	static String[] filtWords = {"的",",",".","·","-","~","!","。","……","..."};
	
	private static void testSeg(){
		String str = "军师联盟";
		List<Term> words = ToAnalysis.parse(str);
//		new NatureRecognition(words).recognition();
		List<String> filterWords = new ArrayList<String>();
		for(String s : filtWords){
			filterWords.add(s);
		}
		FilterModifWord.insertStopWords(filterWords);
		
		words = FilterModifWord.modifResult(words);
		for(Term w : words){
			System.out.println(w.getName()+" | "+w.getNatureStr()+" | "+w.getOffe()+" | "+w.getRealName());
		}
		System.out.println(words);
	}
	
	private static void segWord(File input, File output){
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(input));
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			
			String tmp;
			int count = 0;
			while((tmp = br.readLine()) != null){
				if(tmp.length() < 3){
					sb.append(tmp+"\n");
					continue;
				}
				List<Term> words = ToAnalysis.parse(tmp);
				
				List<String> filterWords = new ArrayList<String>();
				for(String s : filtWords){
					filterWords.add(s);
				}
				FilterModifWord.insertStopWords(filterWords);
				words = FilterModifWord.modifResult(words);
				
				for(Term w : words) {
					String name = w.getName();
					sb.append(name+" ");
				}
				sb.append("\n");
				count++;
				if(count%100000 == 0){
					bw.write(sb.toString());
					sb = new StringBuilder();
				}
			}
			bw.flush();
			bw.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
