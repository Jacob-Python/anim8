public abstract class Shape {
    public Coordinates c;
    public Coordinates s;
    public int r;
    public String fill;
    public abstract double area();
    public abstract void scaleUp(int s);
    public abstract void scaleDown(int s);
    public  Coordinates getCenter(){
        return this.c;
    };
    public void transformPlus(int xp, int yp){
        this.c.x+=xp;
        this.c.y+=yp;
    }
    public void transformMinus(int xp, int yp) {
        this.c.x -= xp;
        this.c.y -= yp;
    }
}
