/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objets;

import Utils.Color;
import Utils.Rayon;
import Utils.Result;
import Utils.Vec3f;

/**
 *
 * @author jerem
 */
public abstract class Figures {
    
    abstract public Result intersection(Rayon r);
    
    public Color cMat;
    
    public abstract Vec3f getNormal(Vec3f intersect);
    
}
