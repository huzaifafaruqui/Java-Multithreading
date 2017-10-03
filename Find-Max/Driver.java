
import java.util.Scanner;

/**
 *
 * @author huzaifafaruqui
 */
class Max {

    public int ans = 0;
}

class FindMax implements Runnable {

    private int[] arr;
    private int lo, hi;
    public Max m;

    public FindMax(int[] arr, int lo, int hi, Max m) {
        this.arr = arr;
        this.lo = lo;
        this.hi = hi;
        this.m = m;
    }

    @Override
    public void run() {
        for (int i = lo; i < hi; ++i) {
            m.ans = Math.max(m.ans, arr[i]);
        }
    }

}

public class Driver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int[] arr = new int[12];
        Scanner scan = new Scanner(System.in);

        for (int i = 0; i < arr.length; i++) {
            arr[i] = scan.nextInt();
        }

        Max m = new Max();
        Thread[] T = new Thread[4];

        for (int i = 0; i < 4; ++i) {
            T[i] = new Thread(new FindMax(arr, arr.length / 4 * i, arr.length / 4 * (i + 1), m));
            T[i].start();
        }
        try {
            for (int i = 0; i < 4; i++) {
                T[i].join();
            }
            System.out.println("ans:" + m.ans);
        } catch (InterruptedException ie) {
        }

    }

}
