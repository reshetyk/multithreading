package cyclicbarrier.currencycollector;

/**
 * @author Alexey
 */
public class Currency {
    private String currency;
    private Double value;

    public Currency(String currency, Double value) {
        this.currency = currency;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "currency='" + currency + '\'' +
                ", value=" + value +
                '}';
    }
}
