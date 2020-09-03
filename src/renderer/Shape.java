public abstract class Shape {
    public Coordinates c;
    public Coordinates s;
    public int r;
    public String fill;
    public abstract double area();
    public abstract void scale(int s);
    public  Coordinates getCenter(){
        return this.c;
    };
    public void transform(int xp, int yp){
        this.c.x+=xp;
        this.c.y+=yp;
    }
}
