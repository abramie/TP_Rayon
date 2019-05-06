
package Utils;

public class Color {
    
    public float r, g, b;
    
    public Color(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public Color(Color color){
        this.b = color.b;
        this.g = color.g;
        this.r = color.r;
    }
    
    public void mult(Color col){
        this.r *= col.r;
        this.g *= col.g;
        this.b *= col.b;
    }
    
    public void mult(float weight){
        this.r *= weight;
        this.g *= weight;
        this.b *= weight;
    }
    
    public void add(Color col){
        this.r += col.r;
        this.g += col.g;
        this.b += col.b;
    }
    
    public void ceil(float max){
        this.r = Math.min(this.r, max);
        this.g = Math.min(this.g, max);
        this.b = Math.min(this.b, max);
        
    }
    
    
}
