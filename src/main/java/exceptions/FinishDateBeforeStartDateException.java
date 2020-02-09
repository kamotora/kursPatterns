package exceptions;

public class FinishDateBeforeStartDateException extends Exception {
    @Override
    public String toString() {
        return "Дата окончания не может быть раньше даты начала";
    }
}
