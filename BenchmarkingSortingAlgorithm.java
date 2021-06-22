import java.util.Arrays;

/**
 *
 Selection Sort method for an array size of 1000 takes 1.624374218235E9 seconds to sort the array.
 Array.sort() method for an array of 1000 takes 1.624374218237E9 seconds to sort the array.

 Selection Sort method for an array size of 10000 takes 1.624374309456E9 seconds to sort the array.
 Array.sort() method for an array of 10000 takes 1.62437430947E9 seconds to sort the array.

 Selection Sort method for an array size of 100000 takes 1.624374394782E9 seconds to sort the array.
 Array.sort() method for an array of 100000 takes 1.624374394874E9 seconds to sort the array.

 Array.sort() method for an array of 1000000 takes 1.624374919181E9 seconds to sort the array.

 *
 */

//computing benchmarks of two different sorting techniques
public class BenchmarkingSortingAlgorithm {

	// array size 1000
//	int arraySize = 1000; uncomment to check time for 1000 and comment the 100000

	// array size 10000
//	int arraySize = 10000; uncomment to check time for 10000 and comment the 100000

	// array size 100000
	int arraySize = 100000;

	// first array
	int[] sortArray1 = new int[arraySize];
	// second array
	int[] sortArray2 = new int[arraySize];

	public BenchmarkingSortingAlgorithm() {

		// two arrays with the same randomly generated numbers
		for (int i = 0; i < sortArray1.length; i++) {
			sortArray1[i] = (int)(Integer.MAX_VALUE * Math.random());
			sortArray2[i] = sortArray1[i];
		}

		// start computing time for Selection Sort
		long startTimeArray1 = System.currentTimeMillis();
		// sorting the array1 with Selection Sort
		selectionSort(sortArray1);
		// running time of Selection Sort
		long runTimeArray1 = System.currentTimeMillis();


		// start computing time for Arrays.sort()
		long startTimeArray2 = System.currentTimeMillis();
		// sorting Array2 using Arrays.sort()
		Arrays.sort(sortArray2);
		// running time of Arrays.sort()
		long runTimeArray2 = System.currentTimeMillis();

		// printing the results to the console
		System.out.println("Selection Sort time in seconds: " + runTimeArray1/1000.0);
		System.out.println("Arrays.sort() time in seconds: " + runTimeArray2/1000.0);
	}

	// sort array in increasing manner using Selection Sort
	static void selectionSort(int[] A) {

		// one by one move boundary of unsorted array
		for (int lastPlace = A.length -1; lastPlace > 0; lastPlace--) {
			// find the maximum element in unsorted array
			int maxLocation = 0;

			for (int j = 1; j <= lastPlace; j++) {
				if (A[j] > A[maxLocation]) {
					maxLocation = j;
				}
			}

			// swap the found max element with last element, A[lastPlace]
			int temp = A[maxLocation];
			A[lastPlace] = temp;
		}
	}

}
