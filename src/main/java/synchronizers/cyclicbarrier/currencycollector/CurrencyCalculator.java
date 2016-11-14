package synchronizers.cyclicbarrier.currencycollector;

import java.util.Collection;

/**
 * @author Alexey
 */
public class CurrencyCalculator {

    public static Double calcAverage(Collection<Currency> currencies) {
        Double result = 0.0;
        for (Currency currency : currencies) {
            result += currency.getValue();
        }
        return result / currencies.size();

    }
}
