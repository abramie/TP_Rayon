/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objets;

import Utils.Rayon;

/**
 *
 * @author jerem
 */
public abstract class Figures {
    
    abstract public boolean intersection(Rayon r, Double resultat);
    
    public float[] cMat = new float[3];
    
}
