public class Coordinates {
    private long x; //Максимальное значение поля: 104
    private int y;

    Coordinates(long x, int y) {
        setX(x);
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(long x) {
        if (x > 104) {
            throw new IllegalArgumentException("Значение поля x должно быть меньше или равно 104");
        }
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}