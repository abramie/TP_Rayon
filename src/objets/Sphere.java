/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objets;

import Utils.Rayon;
import Utils.Result;
import Utils.Vec3f;
import static java.lang.Double.min;

/**
 *
 * @author Moi
 */
public class Sphere extends Figures{

    private int R;
    private Vec3f C;
    
    @Override
    public Result intersection(Rayon r) {
        Vec3f A = r.getA();
        Vec3f V = r.getV();
        Vec3f CA = new Vec3f(C,A);
        float vCA = V.dot(CA);
        
        
        float lambda = 0;
        float a = V.length()*V.length(); //a c'est 1
        
        float b = 2*vCA;
        float c = CA.length()*CA.length()-R*R; 
       
        
        float delta = b*b-4*a*c;
        
       
        if (delta>=0){
            
            float x1 = (-b-(float)Math.sqrt(delta))/(2.0f*a);
            float x2 = (-b+(float)Math.sqrt(delta))/(2.0f*a);
    
            
            /*if (x1>0 && x2>0){
                //resultat = r.getV().scale((float)min(x1,x2)).length();
                lambda = (float)min(x1,x2);
                return new Result(lambda, true);
            }
            else */
            if (x1>0.0){
                lambda = x1;
                return new Result(lambda, true);
            }
            else if (x2>0.0){
               // lambda = r.getV().scale(x2).length();
                lambda = x2;
                return new Result(lambda, true);
            }
            else{
                return new Result(5, false);
            }
        }
        
        
        return new Result(0, false);
    }
    
    public Sphere(Vec3f c, int r){
        this.C = c;
        this.R = r;
    }

    @Override
    public Vec3f getNormal(Vec3f intersect) {
        return new Vec3f(C,intersect).normalize();
    }
    
    
}
