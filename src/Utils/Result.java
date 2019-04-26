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
    private double intersection;
    private boolean exists;
    
    public Result(double intersection, boolean exists){
        this.intersection = intersection;
        this.exists = exists;
    }

    public double getIntersection() {
        return intersection;
    }

    public void setIntersection(double intersection) {
        this.intersection = intersection;
    }

    public boolean exists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
    
    
}
