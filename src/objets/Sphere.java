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
        double resultat = 0;
        
        float a = V.normalize().dot(V.normalize());
        
        Vec3f AC = new Vec3f(A.x-C.x, A.y-C.y, A.z-C.z);
        float b = 2*(V.dot(AC));
        float c = AC.normalize().dot(AC.normalize())-R*R;
        
        float delta = b*b-4*a*c;
        double x1 = (-b-delta)/(2*a);
        double x2 = (-b+delta)/(2*a);
        
        if (x1>0 && x2>0){
            resultat = min(x1,x2);
            return new Result(resultat, true);
        }
        else if (x1>0){
            resultat = x1;
            return new Result(resultat, true);
        }
        else if (x2>0){
            resultat = x2;
            return new Result(resultat, true);
        }
        else{
            return new Result(resultat, false);
        }
    }
    
    public Sphere(Vec3f c, int r){
        this.C = c;
        this.R = r;
    }
    
    
}
