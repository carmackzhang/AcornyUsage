package test;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ValueTest {

	public static void main(String[] args) {
		int a = 1;
//		intTest(new Integer(1));
//		System.out.println(a);
		
		int[] aa = {1,2};
//		intsTest(aa);
//		System.out.println(aa[0]+"\t"+aa[1]);
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
//		listTest(list);
//		System.out.println(list.get(0)+"\t"+list.get(1));
		
//		long l1 = 0;
//		System.out.println(l1>0);
//		double mingyi_ctr = 1.0E-4;
//		System.out.println(mingyi_ctr<=0.0001);
		
		double a1 = 1;
		System.out.println(Math.round(a1*100)/3/100.0);
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println(df.format(a1/3));
		
		int avgr = 100;
		int fan = 200;
		float rrate = (float) (avgr*100.0/fan);
		System.out.println(rrate);
		
		double simrank = 0.6339813352908076;
		float simrank_f = (float) simrank;
		System.out.println(simrank+"\t"+simrank_f);
		
		int auth_weight = 60;
		System.out.println(0.333*auth_weight);
		
		System.out.println(Math.cos(Math.toRadians(45)));
	}
	
	private static void intTest(int a){
		a = 3;
	}
	
	private static void intTest(Integer a){
		a = new Integer(3);
	}
	
	private static void intsTest(int[] a){
		a[0] = 3;
		a[1] = 4;
	}
	
	private static void listTest(List<Integer> a){
		a.set(0, 3);
		a.set(1, 4);
	}
}
