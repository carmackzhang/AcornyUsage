package test;
import java.util.HashSet;

public class HashSetTest {

	private static HashSet<String> set = new HashSet<String>();
	private static String vrid = "";
	
	public static void main(String[] args) {
		String q = "五台山";
		String v = "11002501";
		System.out.println(containsTest(q,v));
		System.out.println(vrid+"\t"+getVrid());
	}
	
	private static boolean containsTest(String q,String v){
		String query = "五台山";
		vrid = "11002501";
		set.add(query+"\t"+vrid);
		vrid = "11002301";
		String vv = "11002401";
		vrid = vv;
		return set.contains(q+"\t"+v);
	}
	
	private static String getVrid(){
		return vrid;
	}
}
