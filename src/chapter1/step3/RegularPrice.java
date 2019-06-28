package chapter1.step3;

/**
 * @Author: Lijian
 * @Date: 2019-06-27 16:41
 */
public class RegularPrice extends Price {
    @Override
    int getPriceCode() {
        return Movie.REGULAR;
    }
}
