package chapter1.origin;

/**
 * @Author: Lijian
 * @Date: 2019-06-27 15:52
 */
public class Rental {
    private Movie _movie;
    private int _daysRented;

    public Rental(Movie movie, int daysRented){
        _movie = movie;
        _daysRented = daysRented;
    }

    public int getDaysRented(){
        return _daysRented;
    }

    public Movie getMovie(){
        return _movie;
    }
}
