package exceptions;

public class NegativeBalanceException extends Exception {
    @Override
    public String toString() {
        return "Для данного счёта расходы превышают доходы";
    }
}
