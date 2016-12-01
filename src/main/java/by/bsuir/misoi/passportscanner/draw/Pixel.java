package by.bsuir.misoi.passportscanner.draw;


public class Pixel {

    public int x;
    public int y;

    public Pixel() {
        this(0, 0);
    }

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean setLeft(int x0) {
        if (x0 <= this.x) {
            this.x = x0;
            return true;
        }
        return false;
    }

    public boolean setRight(int x0) {
        if (x0 >= this.x) {
            this.x = x0;
            return true;
        }
        return false;
    }

    public boolean setBottom(int y0) {
        if (y0 >= this.y) {
            this.y = y0;
            return true;
        }
        return false;
    }

    public boolean setTop(int y0) {
        if (y0 <= this.y) {
            this.y = y0;
            return true;
        }
        return false;
    }
}
