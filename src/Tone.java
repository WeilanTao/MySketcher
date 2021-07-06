import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;

public class Tone {
    private static int average;
    private static int counter;
    private static int sum;
    private static int width;
    private static int height;
    private static BufferedImage toBeTone;
    private static BufferedImage result;


    public Tone(BufferedImage input) {
        width = input.getWidth();
        height = input.getHeight();
        toBeTone = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        result = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        average = 0;
        counter = width * height;
        sum = 0;

        //make a copy of the original picture
        Graphics g = toBeTone.getGraphics();
        g.drawImage(input, 0, 0, null);
        g.dispose();

        //adjustContract
        adjustContract();
    }

    private static void adjustContract() {
        findAverage();

        int mContrast = average * (2);
        int d = (100 + mContrast) / 100;

        short contractTable[] = makeLookUpTable(d);

        LookupTable lookupTable1 = new ShortLookupTable(0, contractTable);
        LookupOp lookupOp = new LookupOp(lookupTable1, null);
        lookupOp.filter(toBeTone, result);

    }

    private static void findAverage() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int red = ((toBeTone.getRGB(i, j)) & 0x00FF0000) >> 16;

                sum += red;

            }
        }
        average = sum / counter;
    }

    private static short[] makeLookUpTable(int d) {
        short[] cT = new short[256];
        for (short i = 0; i < 256; i++) {
            cT[i] = (short) ((i - average) * d + average + 0.5 + 128);
            if (cT[i] > 255) {
                cT[i] = 255;
            }
            if (cT[i] < 0) {
                cT[i] = 0;
            }

        }

        return cT;
    }

    public BufferedImage getResult() {
        return this.result;
    }

}
