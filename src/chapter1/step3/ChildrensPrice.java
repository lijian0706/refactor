package chapter1.step3;

/**
 * @Author: Lijian
 * @Date: 2019-06-27 16:40
 */
public class ChildrensPrice extends Price {
    @Override
    int getPriceCode() {
        return Movie.CHILDRENS;
    }
}
