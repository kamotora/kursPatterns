package exceptions;

public class EqualsBills extends Throwable {
    @Override
    public String toString() {
        return "Счета списания и назначения одинаковые";
    }
}
