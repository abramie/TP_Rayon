/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author jerem
 */
public class Rayon {
    
    /**Point d'origine du rayon*/
    private Vec3f a;
    /**Vecteur principal du rayon*/
    private Vec3f v;
    
    
    public Vec3f getA(){
        return a;
    }
    
    public Vec3f getV(){
        return v;
    }
    
    
    public Rayon(Vec3f a, Vec3f v){
        this.a =a;
        this.v = v;
    }
    
}
