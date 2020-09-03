public class Square extends Shape{
    public Square(Coordinates c, Coordinates s, int r, String fill){
        this.c=c;
        this.r=r;
        this.fill=fill;
        this.s=s;
    }
    public double area() {
        return this.r*2;
    }
    public void scale(int s){
        this.r+=s;
    }
    public void transform(int xp, int yp){
        this.c.x+=xp;
        this.c.y+=yp;
        this.s.x+=xp;
        this.s.y+=yp;
    }
}
