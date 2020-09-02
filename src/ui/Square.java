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
    public void scaleUp(int s){
        this.r+=s;
    }
    public void scaleDown(int s){
        this.r-=s;
    }
    public void transformPlus(int xp, int yp){
        this.c.x+=xp;
        this.c.y+=yp;
        this.s.x+=xp;
        this.s.y+=yp;
    }
    public void transformMinus(int xp, int yp) {
        this.c.x -= xp;
        this.c.y -= yp;
        this.s.x -= xp;
        this.s.y -= yp;
    }
}
