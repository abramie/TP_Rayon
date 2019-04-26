/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objets;

import Utils.Rayon;
import Utils.Vec3f;

/**
 *
 * @author jerem
 */
public class Plan extends Figures{

    @Override
    public boolean intersection(Rayon r, Double resultat) {
        Vec3f N = this.getNormal();
        double lambda = -(N.dot(r.getA()))-this.d;
        lambda = lambda / N.dot(r.getV());
        
        if(lambda >0){
            resultat = lambda;
            return true;
        }
        return false;
    }
    
    private int a,b,c,d;
    
    public Plan(int  a, int b, int c, int d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    public Vec3f getNormal(){
        return new Vec3f(a,b,c);
    }
    
}
