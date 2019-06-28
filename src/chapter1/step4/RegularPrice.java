package chapter1.step4;

/**
 * @Author: Lijian
 * @Date: 2019-06-27 16:41
 */
public class RegularPrice extends Price {
    @Override
    int getPriceCode() {
        return Movie.REGULAR;
    }

    public double getCharge(int daysRented){
        double result = 2;
        if(daysRented > 2)
            result += (daysRented - 2) * 1.5;
        return result;
    }
}
