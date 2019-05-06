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

   @Override //Code jérmy
    public Result intersection(Rayon r) {
        Float NA = this.normal.dot(r.getA());
        Float NV = this.normal.dot(r.getV());
        Float resultat = 0.0f;
        Float lambda = Float.MAX_VALUE;;
        if(NV !=0){
            lambda = ((-NA)-distance) / NV;
            resultat = lambda;
            
        }
        
        if(lambda > 0.05f){
            return new Result(resultat, true);
        }
        return new Result(resultat, false);
    }
    
    /*@Override //Code benjamin
    public Result intersection(Rayon r) {
        float nv = this.normal.dot(r.getV());
        float lambda;
        
        if (nv!=0){
            lambda = -(this.normal.dot(r.getA()))-distance;
            lambda = lambda /nv;
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
