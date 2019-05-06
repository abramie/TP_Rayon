package tp_rayon;

import Utils.Color;
import Utils.Rayon;
import Utils.Result;
import Utils.Vec3f;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import objets.Figures;
import objets.Plan;
import objets.Sphere;

/**
 *
 * @author Benjamin
 */
public class Jtga {

   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //j'ai reduit un peu la taille de la grille
        short width = 1024;
        short height = 1024;
        
        ByteBuffer buffer = ByteBuffer.allocate(width*height*3);
        /**Liste des objets */
        List<Figures> obj = new ArrayList<Figures>();
        Vec3f normalPlan1 = new Vec3f(0,0,1);
        Plan p1 = new Plan(normalPlan1, -500);
        p1.cMat = new Color(0f,1f,0f);
        Vec3f normalPlan2 = new Vec3f(0f,1f,0f);
        Plan p2 = new Plan(normalPlan2, -200);
        p2.cMat = new Color(1f,0f,0f);
        Vec3f sphereCoords = new Vec3f(0, 0, -1100);
        Sphere s1 = new Sphere(sphereCoords, 300);
        s1.cMat = new Color(0f,0f,0.5f);
        Vec3f sphereCoords2 = new Vec3f(0, 0, -1500);
        Sphere s2 = new Sphere(sphereCoords2, 400);
        s2.cMat = new Color(1f,0f,1f);
        
        
        obj.add(p1);
        obj.add(s1);
        obj.add(s2);
        //obj.add(s1);
        /**Liste des sources de lumieres*/
        List<Vec3f> sources = new ArrayList<Vec3f>();
        sources.add(new Vec3f(0, 500, 0));
       // sources.add(new Vec3f(25, 9, 8));
        int distance_grille = -1000;
        
        
        
        Vec3f O = new Vec3f();
        // For each pixel...
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                // Compute the index of the current pixel in the buffer
                int index = 3*((y*width)+x);
                /*
                // Ensure that the pixel is black
                buffer.put(index, (byte)0); // blue : take care, blue is the first component !!!
                buffer.put(index+1, (byte)0); // green
                buffer.put(index+2, (byte)0); // red (red is the last component !!!)
                
                // Depending on the x position, select a color... 
                if (x<width/3) buffer.put(index, (byte)255);
                else if (x<2*width/3) buffer.put(index+1, (byte)255);
                else buffer.put(index+2, (byte)255);*/
                
                //Construire le rayon entre O et le pixel
                //le pixel est x,y, et la distance de la grille
                Vec3f v = new Vec3f(x-width/2,y-height/2,distance_grille);
                Color color = rayCast(O, v, obj, sources);
                //Sauver les contribs pour obtenir la couleur final du pixel
                buffer.put(index, (byte)(color.r*255)); // blue : take care, blue is the first component !!!
                buffer.put(index+1, (byte)(color.g*255)); // green
                buffer.put(index+2, (byte)(color.b*255)); // red (red is the last component !!!)
                
            }
        }
        
        try {
            
            int ret = saveTGA("test.tga",buffer,width,height);   
            if (ret==0)
            {
                System.out.println("Could not save TGA file ????");
                System.exit(1);
            }
            
        } catch (IOException ex) {
            
            Logger.getLogger(Jtga.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static Color rayCast(Vec3f A, Vec3f v, List<Figures> obj,
            List<Vec3f> sources){
        Rayon primaire;
        Color ambiantLight = new Color(0.1f, 0.1f, 0.1f);
        Color lightColor = new Color(0.9f, 0.9f, 0.9f);
        Result r;
        Figures proche = null;
        
        Rayon lumiere;
        Vec3f  Mmin = new Vec3f();
        primaire = new Rayon(v,new Vec3f(A,v ));
                
                
        //Foreach obj
        /**La distance minimum d'un objet**/
        Double la =0.0;
        /**Devient vrai si un objet se trouve dans la diection du rayon*/
        boolean is = false;

        /**res est la distance de l'intersection courrante*/
        Double res = 0.0;

        /**Pour chaque objet, on verifie si il est sur le chemin du rayon*/
        for(Figures f : obj){
            
           if( (r = f.intersection(primaire)).exists() ){
               res = r.getDistance();
               //Si c'est pas le premier objet
               if(is ){
                   // ou si il est devant l'objet le plus proche precedent
                   if(res < la){
                       //On l'enregistre.
                        la = res;
                        proche = f;
                   }  
               }else{
                   //On l'enregistre.
                   is = true;
                   la = res;
                   proche = f;

               }
           }
        }
        if (proche==null){
            return new Color(0,0,0);
        }
        //M = A + lambda*v

        //v le vecteur de l'observateur au pixel
        Mmin.setAdd(A,v.scale(la.floatValue()));

        //Si y a un objet sur le chemin, sinon sa sert à rien de calculer tous sa
        
        Color finalColor = new Color(proche.cMat);
        finalColor.mult(ambiantLight);
        //Foreach sources
        boolean b = true;
        //Pour chaque lumiere
        for(Vec3f l : sources){
            b = true;
            //Rayon du point d'intersection à la lumiere
            lumiere = new Rayon(Mmin,new Vec3f(Mmin,l));
            //On calcule la distance entre le pixel et la source de la lumiere
            double distance_pixel_lumiere = Math.sqrt(Math.pow(Mmin.x-l.x, 2)+Math.pow(Mmin.y-l.y, 2)+Math.pow(Mmin.z-l.z, 2));
            //On verifie si un objet oculte la source
            for(Figures f : obj){
                //Si il y a une intersection, et que celle ci est entre
                //Mmin et f alors la lumiere n'atteint pas le pixel
                if((r = f.intersection(lumiere)).exists() && r.getDistance()< distance_pixel_lumiere && r.getDistance() > 0.05f){
                    b = false;
                }
            }
            if(b){
                //System.out.println("ce pixel est touché par la lumiere \\o/ (" + x + "," + y +")" + "il appartient à " + proche);
                float weight = Math.max(0, proche.getNormal(Mmin).normalize().dot(new Vec3f(Mmin,l).normalize()));

                Color color = new Color(proche.cMat);
                
                color.mult(lightColor);
                color.mult(weight);
                
                finalColor.add(color);
            }
        }
        finalColor.ceil(1);
        return finalColor;
    }
    
    /* save an image in a TGA file with no compression and in true color (24bits/pixel)
    - filename : name of the TGA file
    - buffer : buffer containing the image. Only 3 bytes per pixel are tolerated.
    Take care : the expected order is (first) Blue, Green and Red for each pixel. */
    private static int saveTGA(String filename, ByteBuffer buffer, short width, short height) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        FileOutputStream fos = new FileOutputStream(new File(filename));
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(fos));

        out.writeByte((byte) 0);
        out.writeByte((byte) 0);
        out.writeByte((byte) 2);
        out.writeShort(fe((short) 0));
        out.writeShort(fe((short) 0));
        out.writeByte((byte) 0);
        out.writeShort(fe((short) 0));
        out.writeShort(fe((short) 0));
        out.writeShort(fe(width));
        out.writeShort(fe(height));
        out.writeByte((byte) 24);
        out.writeByte((byte) 0);

        /* Write the buffer */
        out.write(buffer.array());

        out.close();
        return 1;
    }

    private static short fe(short signedShort)
    {
        int input = signedShort & 0xFFFF;
        return (short) (input << 8 | (input & 0xFF00) >>> 8);
    }
}
