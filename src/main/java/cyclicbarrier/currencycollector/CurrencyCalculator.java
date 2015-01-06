package cyclicbarrier.currencycollector;

import java.util.Collection;
import java.util.List;

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
