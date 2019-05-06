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
        
        //j'ai reduit un peu lambda_minimum taille de lambda_minimum grille
        short width = 1024;
        short height = 1024;
        
        ByteBuffer buffer = ByteBuffer.allocate(width*height*3);
        /**Liste des objets */
        //Les plans veulent vraiment pas marcher
        List<Figures> obj = new ArrayList<Figures>();
        Vec3f normalPlan1 = new Vec3f(0,0,1);
        Plan p1 = new Plan(normalPlan1, 2500);
        p1.cMat = new Color(0f,1f,0f);
        Vec3f normalPlan2 = new Vec3f(0f,1f,0f);
        Plan p2 = new Plan(normalPlan2, 1400);
        p2.cMat = new Color(1f,0f,0f);
        
        Vec3f sphereCoords = new Vec3f(0, 0, -2000);
        Sphere s1 = new Sphere(sphereCoords, 300);
        s1.cMat = new Color(0f,0f,0.5f);
        
        Vec3f sphereCoords2 = new Vec3f(400, 0, -2000);
        Sphere s2 = new Sphere(sphereCoords2, 200);
        s2.cMat = new Color(1f,0f,1f);
        
        /*Rayon testray = new Rayon(new Vec3f(0,0,0), new Vec3f(0,0,-6));
        Result tmp = s1.intersection(testray);
        System.out.println(tmp.exists());
        System.out.println(tmp.getDistance());
        return;*/
        
        obj.add(p1);
        obj.add(s1);
        obj.add(s2);
        //obj.add(p2);
        
        /**Liste des sources de lumieres*/
        List<Vec3f> sources = new ArrayList<Vec3f>();
        sources.add(new Vec3f(500, 0, -1000));
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
                //le pixel est x,y, et lambda_minimum distance de lambda_minimum grille
                Vec3f v = new Vec3f(x-width/2,y-height/2,distance_grille);
                Color color = rayCast(O, v, obj, sources);
                //Sauver les contribs pour obtenir lambda_minimum couleur final du pixel
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
    
    private static Color rayCast(Vec3f origine_rayon, Vec3f direction_rayon, List<Figures> obj,
            List<Vec3f> sources){
        Rayon primaire;
        Color ambiantLight = new Color(0.3f, 0.3f, 0.3f);
        Color lightColor = new Color(1.0f, 1.0f, 1.0f);
        Result r;
        Figures proche = null;
        
        Rayon lumiere;
        Vec3f  Mmin = new Vec3f();
        primaire = new Rayon(origine_rayon,direction_rayon);
        
        //Foreach obj
        /**La distance minimum d'un objet**/
        double lambda_minimum =0.0;
        /**Devient vrai si un objet se trouve dans la diection du rayon*/
        boolean is = false;

        /**res est la distance de l'intersection courrante*/
        double res = 0.0;

        /**Pour chaque objet, on verifie si il est sur le chemin du rayon*/
        for(Figures f : obj){
            
           if( (r = f.intersection(primaire)).exists() ){
               res = r.getDistance();
               //Si c'est pas le premier objet
               if(is ){
                   // ou si il est devant source_actuelle'objet le plus proche precedent
                   if(res < lambda_minimum){
                       //On source_actuelle'enregistre.
                        lambda_minimum = res;
                        proche = f;
                   }  
               }else{
                   //On source_actuelle'enregistre.
                   is = true;
                   lambda_minimum = res;
                   proche = f;

               }
           }
        }
        if (proche==null){
            return new Color(0,0,0);
        }
        //Si y a un objet sur le chemin, sinon sa sert à rien de calculer tous sa
        
        //M = origine_rayon + lambda*direction_rayon

        //v le vecteur de source_actuelle'observateur au pixel
        
        Mmin.setAdd(origine_rayon,direction_rayon.scale((float)lambda_minimum));

        
        Color finalColor = new Color(proche.cMat);
        finalColor.mult(ambiantLight); //On met de source_actuelle'ambiant, du coup à source_actuelle'objet le plus proche de source_actuelle'observateur
        
        
        boolean b_NoIntersection = true;
        //Pour chaque sources lumineuse
        for(Vec3f source_actuelle : sources){
            b_NoIntersection = true;
            //Rayon du point d'intersection à lambda_minimum lumiere
            lumiere = new Rayon(Mmin,new Vec3f(Mmin,source_actuelle));
            //On calcule lambda_minimum distance entre le pixel et lambda_minimum source de lambda_minimum lumiere
            //double distance_pixel_lumiere = Math.sqrt(Math.pow(Mmin.x-source_actuelle.x, 2)+Math.pow(Mmin.y-source_actuelle.y, 2)+Math.pow(Mmin.z-source_actuelle.z, 2));
            //On verifie si un objet oculte lambda_minimum source
            for(Figures fig_intercepteur : obj){
                //Si il y a une intersection, et que celle ci est entre
                //Mmin et fig_intercepteur alors lambda_minimum lumiere n'atteint pas le pixel
                if((r = fig_intercepteur.intersection(lumiere)).exists() && r.getDistance()< 1/*distance_pixel_lumiere*/ && r.getDistance() > 0.05f){
                    b_NoIntersection = false;
                }
            }
            if(b_NoIntersection){
                
                Vec3f aaa = proche.getNormal(Mmin).normalize();
                Vec3f bbb = new Vec3f(Mmin,source_actuelle).normalize();
              
                float weight = aaa.dot(bbb);
                //System.out.println(Mmin);
                weight = weight < 0 ? 0 : weight;
                //if(weight >0)System.out.println(weight);
                //float weight = Math.max(0, proche.getNormal(Mmin).normalize().dot(new Vec3f(Mmin,source_actuelle).normalize())); //l'angle diminue source_actuelle'intensité de lambda_minimum lumiere
                //System.out.println(weight);
                
                Color color = new Color(proche.cMat);
                
                color.mult(lightColor); //On applique lambda_minimum lumiere
                color.mult(weight); //et lambda_minimum reduction d'angle
                finalColor.add(color); //On ajoute à source_actuelle'ambiant
            }
        }
        finalColor.ceil(1); //On plafonne à 1 lambda_minimum lumiere.
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
