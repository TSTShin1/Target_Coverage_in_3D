public class Sensor {
    private double x;
    private double y;
    private double z;
    private double radius;
    public Sensor(double x, double y, double z, double radius) {
        this.x = x;
        this.y = y;
        this.z = z; 
        this.radius = radius;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getRadius() {
        return radius;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
    }
}