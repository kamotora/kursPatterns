package exceptions;

public class DublicateCategory extends Throwable {
    @Override
    public String toString() {
        return "Было найдено несколько категорий с такими данными. Такого быть не должно";
    }
}
