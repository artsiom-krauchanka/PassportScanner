package by.bsuir.misoi.passportscanner.draw;


public class Content {

    public int x;
    public int y;

    public int width;
    public int height;

    public Content() {
        this(0, 0, 0, 0);
    }

    public Content(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
