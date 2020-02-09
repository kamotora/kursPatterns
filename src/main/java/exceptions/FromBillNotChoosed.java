package exceptions;

public class FromBillNotChoosed extends Exception {
    @Override
    public String toString() {
        return "Счёт списания не выбран";
    }
}
