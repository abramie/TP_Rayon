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
        float resultat = 0;
        
     
         float a = V.length()*V.length();
        Vec3f CA = new Vec3f(C.x-A.x, C.y-A.y, C.z-A.z);
       // float b = 2*(V.dot(AC));
        float b = 2*(V.x *(CA.x));
       
        float c = CA.length()*CA.length()-R*R; 
        float delta = b*b-4*a*c;
        
        
        if (delta>0){
            
            float x1 = (-b-(float)Math.sqrt(delta))/(2*a);
            float x2 = (-b+(float)Math.sqrt(delta))/(2*a);
            if (x1>0 && x2>0){
                //resultat = r.getV().scale((float)min(x1,x2)).length();
                resultat = (float)min(x1,x2);
                return new Result(resultat, true);
            }
            else if (x1>0){
                resultat = x1;
                return new Result(resultat, true);
            }
            else if (x2>0){
               // resultat = r.getV().scale(x2).length();
                resultat = x2;
                return new Result(resultat, true);
            }
            else{
                return new Result(resultat, false);
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
        return new Vec3f(C, intersect).normalize();
    }
    
    
}
