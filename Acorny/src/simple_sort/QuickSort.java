package simple_sort;

public class QuickSort {

	public static void main(String[] args) {
		int[] arr = {14,16,10,8,7,9,3,2,4,1};
		quickSort(arr, 0, arr.length-1);
		printArr(arr);
	}

	private static void quickSort(int[] arr, int low, int high) {
		if(arr==null || arr.length<2) return;
		if(low < high) {
			int mid = getMiddle(arr, low, high);
			quickSort(arr, low, mid-1);
			quickSort(arr, mid+1, high);
		}
	}

	private static int getMiddle(int[] arr, int low, int high) {
		int tmp = arr[low];
		while(low < high) {
			while(low<high && arr[high]>=tmp) high--;
			arr[low] = arr[high];
			while(low<high && arr[low]<=tmp) low++;
			arr[high] = arr[low];
		}
		arr[low] = tmp;
		return low;
	}
	
	private static void printArr(int[] arr) {
		if(arr!=null) System.out.print("[");
		for(int i=0; i < arr.length-1; i++) {
			System.out.print(arr[i]+" ");
		}
		if(arr!=null) System.out.print(arr[arr.length-1]+"]\n");
	}
	
}
