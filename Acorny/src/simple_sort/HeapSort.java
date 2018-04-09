package simple_sort;

public class HeapSort {

	public static void main(String[] args) {
		int[] arr = {14,16,10,8,7,9,3,2,4,1};
		for(int i=arr.length/2; i>=0; i--) {
			heapAdjust(arr, i, arr.length);
			printArr(arr);
		}
		System.out.println("build heap finish ...");
		for(int i=arr.length-1; i > 0; i--) {
			swap(arr, i, 0);
			heapAdjust(arr, 0, i);
			printArr(arr);
		}
		
	}

	private static void swap(int[] arr, int i, int j) {
		if(arr==null) return;
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
	
	private static void heapAdjust(int[] arr, int i, int length) {
		if(arr==null || length<=1 || 2*i+1>=length) return;
		int father = arr[i];
		int left = 2*i + 1;//left child
		int child = left;
		for(; child < length; i = child) {
			father = arr[i];
			left = 2*i + 1;
			if(left<length-1) {
				child =  arr[left] < arr[left+1] ? left : left+1;
			}
//			System.out.println("i="+i+", left="+left+" child="+child+" arr[child]="+arr[child]+" father="+father+" len="+length);
			if(arr[child] < father) {
				swap(arr, i, child);
			}
			else break;
		}
	}
	
	private static void printArr(int[] arr) {
		if(arr!=null) System.out.print("[");
		for(int i=0; i < arr.length-1; i++) {
			System.out.print(arr[i]+" ");
		}
		if(arr!=null) System.out.print(arr[arr.length-1]+"]\n");
	}
}
