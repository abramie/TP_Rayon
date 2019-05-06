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

    private Vec3f normal;
    private float distance;

    @Override
    public Result intersection(Rayon r) {
        Vec3f N = this.normal;
        Float lambda = new Float(-(N.dot(r.getA())))-distance;
        lambda = lambda / N.dot(r.getV());
        float resultat = 0;
        
        if(lambda >0){
            resultat = r.getV().scale(lambda).length();
            return new Result(resultat, true);
        }
        return new Result(resultat, false);
    }
    
    /*@Override
    public Result intersection(Rayon r) {
        float nv = this.normal.dot(r.getV());
        Result result = null;
        float lambda;
        if (nv!=0){
            lambda = -this.normal.dot(r.getA())-distance;
        }
        else{
            lambda = Float.MAX_VALUE;
        }
        
        if(lambda >0.05f){
            return new Result(lambda, true);
        }
        return new Result(lambda, false);
    }*/
    
    
    public Plan(Vec3f normal, float distance){
        this.normal= normal;
        this.distance = distance;
    }

    @Override
    public Vec3f getNormal(Vec3f intersect) {
        return this.normal.normalize();
    }
    
}
