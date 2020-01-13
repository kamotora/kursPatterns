package exceptions;

public class ToBillNotChoosed extends Exception {
    @Override
    public String toString() {
        return "Счёт назначения не выбран";
    }
}
