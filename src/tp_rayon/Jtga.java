package tp_rayon;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Benjamin
 */
public class Jtga {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        short width = 1024;
        short height = 1024;
        
        ByteBuffer buffer = ByteBuffer.allocate(width*height*3);
        
        // For each pixel...
        for(int y = 0; y < 1024; y++){
            for(int x = 0; x < 1024; x++){
                
                // Compute the index of the current pixel in the buffer
                int index = 3*((y*width)+x);
                
                // Ensure that the pixel is black
                buffer.put(index, (byte)0); // blue : take care, blue is the first component !!!
                buffer.put(index+1, (byte)0); // green
                buffer.put(index+2, (byte)0); // red (red is the last component !!!)
                
                // Depending on the x position, select a color... 
                if (x<width/3) buffer.put(index, (byte)255);
                else if (x<2*width/3) buffer.put(index+1, (byte)255);
                else buffer.put(index+2, (byte)255);
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
