package test;

import java.util.BitSet;

public class BitTest {

	public static void main(String[] args) {
		int[] arr = {1,2,3,4,100,200};
		BitSet bs = new BitSet(3);//default size is 64
		System.out.println(bs.size());
		for(int i : arr) {
			bs.set(i, true);
		}
		System.out.println(bs.size());//size(2^n) >= max element, 256>200 
		System.out.println(bs.get(1)+"\t"+bs.get(5));
	}
}
