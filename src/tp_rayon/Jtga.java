package tp_rayon;

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
        short width = 60;
        short height = 60;
        
        ByteBuffer buffer = ByteBuffer.allocate(width*height*3);
        /**Liste des objets */
        List<Figures> obj = new ArrayList<Figures>();
        Plan p1 = new Plan(2, 2, 1, 8);
        p1.cMat = new float[]{0,255,0};
        Plan p2 = new Plan(2, 1, 1, 8);
        p2.cMat = new float[]{255,0,0};
        
        Sphere s = new Sphere(new Vec3f(15,20,100), 5);
        s.cMat = new float[]{0,0,255};
        //obj.add(p1);
        //obj.add(p2);
        obj.add(s);
        
        /**Liste des sources de lumieres*/
        List<Vec3f> sources = new ArrayList<Vec3f>();
        sources.add(new Vec3f(12, 3, 2));
       // sources.add(new Vec3f(25, 9, 8));
        float distance_grille = 3;
       
        //Tas de variables pour des trucs
        Vec3f O = new Vec3f();
        Rayon primaire;
        Rayon lumiere;
        Figures proche = null;
        Vec3f  Mmin = new Vec3f();
        Result r;
        // For each pixel...
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                byte blue =0,red=0,green=0;
                
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
                Vec3f v = new Vec3f(x,y,distance_grille);
                primaire = new Rayon(O,new Vec3f(O,v ));
                
                
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
                       //Si c'est le premier objet, ou si il est devant l'objet le plus proche precedent
                       //On l'enregistre.
                       if(is && res < la){
                           la = res;
                           proche = f;
                           
                           
                       }else{
                           is = true;
                           la = res;
                           proche = f;
                           
                       }
                   }
                }
                //M = A + lambda*v
                
                Mmin.setAdd(O,v.scale(la.floatValue()));
                
                //Si y a un objet sur le chemin, sinon sa sert à rien de calculer tous sa
                if( is ) {
                    
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
                            if((r = f.intersection(lumiere)).exists() && r.getDistance()< distance_pixel_lumiere){
                                b = false;
                            }
                        }
                        if(b){
                            //System.out.println("ce pixel est touché par la lumiere \\o/ (" + x + "," + y +")" + "il appartient à " + proche);
                            //Calculer la contrib de Sj
                            //Quoi que sa veuille dire
                            
                            //J'ai mis des valeurs au pif ici, y a surement un calcul avec de la lumiere et des couleurs.
                            red += (byte)proche.cMat[0];
                            // proche.couleur(); // un truc comme sa aussi d'ailleurs
                            blue += (byte)proche.cMat[1];
                            green += (byte)proche.cMat[2];
                        }
                    }

                    
                 
                }
                //Sauver les contribs pour obtenir la couleur final du pixel
               buffer.put(index, blue); // blue : take care, blue is the first component !!!
               buffer.put(index+1, green); // green
               buffer.put(index+2, red); // red (red is the last component !!!)
                
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
