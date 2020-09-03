public class Circle extends Shape{
    public Circle(Coordinates c, Coordinates s, int r, String fill){
        this.c=c;
        this.r=r;
        this.fill=fill;
        this.s=s;
    }
    public double area() {
        float d = Main.PI*this.r;
        return d*d;
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
