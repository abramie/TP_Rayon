/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objets;

import Utils.Rayon;
import Utils.Result;

/**
 *
 * @author jerem
 */
public abstract class Figures {
    
    abstract public Result intersection(Rayon r);
    
    public float[] cMat = new float[3];
    
}
