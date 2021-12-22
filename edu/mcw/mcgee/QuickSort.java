package edu.mcw.mcgee;

/*************************************************************************
 *  Compilation:  javac QuickSort.java
 *  Execution:    java QuickSort N
 *
 *  Generate N random real numbers between 0 and 1 and quicksort them.
 *
 *  On average, this quicksort algorithm runs in time proportional to
 *  N log N, independent of the input distribution. The algorithm
 *  guards against the worst-case by randomly shuffling the elements
 *  before sorting. In addition, it uses Sedgewick's partitioning
 *  method which stops on equal keys. This protects against cases
 *  that make many textbook implementations, even randomized ones,
 *  go quadratic (e.g., all keys are the same).
 *
 *************************************************************************/

public class QuickSort {
    private static long comparisons = 0;
    private static long exchanges   = 0;

   /***********************************************************************
    *  Quicksort code from Sedgewick 7.1, 7.2.
    ***********************************************************************/
    public static void quicksort(double[] a, int[] b) {
        shuffle(a,b);                        // to guard against worst-case
        quicksort(a, b, 0, a.length - 1);
    }
    public static void quicksort(double[] a, int[] b,int left, int right) {
        if (right <= left) return;
        int i = partition(a, b, left, right);
        quicksort(a, b, left, i-1);
        quicksort(a, b, i+1, right);
    }

    private static int partition(double[] a, int[] b, int left, int right) {
        int i = left - 1;
        int j = right;
        while (true) {
            while (less(a[++i], a[right]))      // find item on left to swap
                ;                               // a[right] acts as sentinel
            while (less(a[right], a[--j]))      // find item on right to swap
                if (j == left) break;           // don't go out-of-bounds
            if (i >= j) break;                  // check if pointers cross
            exch(a, b, i, j);                      // swap two elements into place
        }
        exch(a, b, i, right);                      // swap with partition element
        return i;
    }

    // is x < y ?
    private static boolean less(double x, double y) {
        comparisons++;
        return (x < y);
    }

    // exchange a[i] and a[j]
    private static void exch(double[] a, int[] b, int i, int j) {
        exchanges++;
        double swap_a = a[i];
        a[i] = a[j];
        a[j] = swap_a;
        int swap_b = b[i];
        b[i] = b[j];
        b[j] = swap_b;
    }

    // shuffle the array a
    private static void shuffle(double[] a, int[] b) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int r = i + (int) (Math.random() * (N-i));   // between i and N-1
            exch(a, b, i, r);
        }
    }

    public static int[] randomList(int N){
        //int N = Integer.parseInt(args[0]);
        double[] a = new double[N];
        int [] b = new int[N];
        for (int i = 0; i < N; i++){
            a[i] = Math.random();
            b[i] = i;
        }
        quicksort(a,b);
        return b;
    }

    // test client
    public static void main(String[] args) {
    	for(int j=0; j<100; j++){
    		int[] randomList = randomList(10);
    		for(int i=0; i<randomList.length; i++){
    			System.out.print(randomList[i] + "\t");
    		}
    		System.out.println();
    	}
    }
}
