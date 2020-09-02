public class Coordinates {
    public int x;
    public int y;
    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){return this.x;}
    public int getY(){return this.y;}
    public void setX(int x){this.x=x;}
    public void setY(int y){this.y=y;}
    public boolean equals(Coordinates other){return other.x==this.x&&other.y==this.y;}
}
