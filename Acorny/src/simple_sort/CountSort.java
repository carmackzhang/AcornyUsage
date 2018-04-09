package simple_sort;

public class CountSort {
	public static void main(String[] args) {
		int[] arr = {14,16,10,8,7,9,3,2,4,1};
//		countSort(arr);
		countSort2(arr);
		printArr(arr);
	}

	private static void countSort(int[] arr) {
		int max = getMax(arr);
		int[] brr = new int[max+1];
		for(int i=0; i < arr.length; i++) {
			brr[arr[i]] += 1;
		}
		for(int i=1; i < brr.length; i++) {
			brr[i] += brr[i-1];
		}
		printArr(brr);
		int tmp = arr.length - 1;
		for(int i=brr.length-1; i > 0; i--) {
			
			arr[tmp] = i;
		}
	}

	private static void countSort2(int[] a) {
		int max = getMax(a);
		int[] b = new int[a.length];
		int[] c = new int[max+1];
		for(int i=0;i<=max;i++){
			c[i]=0;
		}
		for(int i=0;i<a.length;i++){
			c[a[i]] += 1;
		}
		for(int i=1;i<=max;i++){
			c[i] += c[i-1];
		}
		for(int i=a.length-1;i>=0;i--){
			b[c[a[i]]-1]=a[i];
			c[a[i]]--;
		}
		printArr(b);
	}
		
	private static int getMax(int[] arr) {
		int max = Integer.MIN_VALUE;
		for(int i=0; i < arr.length; i++) {
			if(max < arr[i]) max = arr[i];
		}
		return max;
	}

	private static void printArr(int[] arr) {
		if(arr!=null) System.out.print("[");
		for(int i=0; i < arr.length-1; i++) {
			System.out.print(arr[i]+" ");
		}
		if(arr!=null) System.out.print(arr[arr.length-1]+"]\n");
	}
}
