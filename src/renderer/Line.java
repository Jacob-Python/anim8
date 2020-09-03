public class Line extends Shape{
    public Coordinates b;
    public Coordinates e;
    public Line(Coordinates b, Coordinates e, Coordinates s, int r, String fill){
        this.b=b;
        this.e=e;
        this.r=r;
        this.fill=fill;
        this.s=s;
    }
    @Override
    public double area() {
        return r*2;
    }
    public void scale(int s){
        this.r+=s;
        if (this.b.x > this.e.x && this.b.y > this.e.y){// DOwn and right
            this.b.x+=s;
            this.b.y+=s;
        }
        if (this.b.x < this.e.x && this.b.y < this.e.y){// Up and left
            this.b.x-=s;
            this.b.y-=s;
        }
        if (this.b.x < this.e.x && this.b.y > this.e.y){// Up and right
            this.b.x-=s;
            this.b.y+=s;
        }
        if (this.b.x > this.e.x && this.b.y < this.e.y){//Down and left
            this.b.x+=s;
            this.b.y-=s;
        }
    }
    public void transform(int xp, int yp){
        this.b.x+=xp;
        this.b.y+=yp;
        this.e.x+=xp;
        this.e.y+=yp;
    }
}
