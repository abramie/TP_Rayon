/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author Moi
 */
public class Result {
    private double distance;
    private boolean exists;
    
    public Result(double intersection, boolean exists){
        this.distance = intersection;
        this.exists = exists;
    }

    public double getDistance() {
        return distance;
    }

   

    public boolean exists() {
        return exists;
    }

   
    
    
}
