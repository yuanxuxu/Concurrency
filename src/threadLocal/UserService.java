package threadLocal;

import java.text.SimpleDateFormat;

/**
 * ThreadLocal in Java, Defog Tech
 */

public class UserService {

    public static void main(String[] args) {

    }

    public String birthDate() {
//        Date birthDate = birthDateFromDB(userId);
        final SimpleDateFormat df = ThreadSafeFormatter.df.get();
        return df.format("1991.07.19"); // birthDate
    }

}

// Java 8
class ThreadSafeFormatter {
    public static ThreadLocal<SimpleDateFormat> df =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
}

class ThreadSafeFormatter2 {
    public static ThreadLocal<SimpleDateFormat> dateFormatter =
            new ThreadLocal<>(){
        // Called once for each thread
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("yyyy-MM-dd");
                }

                // 1st call = initialValue()
                // subsequent calls will return same initialed value
                @Override
                public SimpleDateFormat get() {
                    return super.get();
                }
            };
}






















