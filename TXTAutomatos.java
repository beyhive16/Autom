package autom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TXTAutomatos {

    public List<Character> txtOracao() throws FileNotFoundException, IOException {
        InputStream e = new FileInputStream("/home/kel/Documentos/Códigos em Java/Eclipse/src/autom/input.txt");
        InputStreamReader er = new InputStreamReader(e);
        List<Character> partes = new ArrayList<>();
        try {
            BufferedReader ebr = new BufferedReader(er);

            while (ebr.ready()) {
                int texto = ebr.read();
               
                    partes.add((char)texto);
               
            }
            ebr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return partes;
    }
    
}
