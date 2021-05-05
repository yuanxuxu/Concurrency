package miscellaneous;

/**
 *
 * Java Memory Model is a specification which guarantees
 * visibility of fields(aka happens before) amidst
 * reordering of instructions.
 *
 * */

public class HappensBefore {

    public static void main(String[] args) {

    }

    static class VolatileFieldVisibility {
        int a = 0; int x = 0;

        public void writeThread() {
            a = 1;
            synchronized (this) {
                x = 1;
            }
        }

        // if rx = 1, ra = a must happen after a = 1
        public void readThread() {
            synchronized (this) {
                int rx = x;
            }
            int ra = a;
        }
    }
}
