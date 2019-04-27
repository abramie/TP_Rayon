/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objets;

import Utils.Rayon;
import Utils.Result;
import Utils.Vec3f;

/**
 *
 * @author jerem
 */
public class Plan extends Figures{

    @Override
    public Result intersection(Rayon r) {
        Vec3f N = this.getNormal();
        Double lambda = new Double(-(N.dot(r.getA()))-this.d);
        lambda = lambda / N.dot(r.getV());
        double resultat = 0;
        
        if(lambda >0){
            resultat = lambda;
            return new Result(resultat, true);
        }
        return new Result(resultat, false);
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
    
    @Override
    public String toString(){
        return this.getNormal().toString();
    }
}
