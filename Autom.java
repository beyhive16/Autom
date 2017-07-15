package autom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Autom {

    public static void main(String[] args) throws IOException {
        Scanner entrada = new Scanner(System.in);
        Funcoes obj = new Funcoes();
        TXTAutomatos obj2 = new TXTAutomatos();

        List<String> proibidas = new ArrayList<>();
        List<Character> simbolos = new ArrayList<>();
        List<Character> finala = new ArrayList<>();
        finala = obj2.txtOracao();
        for (int i = 0; i < finala.size(); i++) {
            //System.out.println("leu"+i+": "+finala.get(i));
        }
        addPalavras(proibidas);
        obj.analise(finala, proibidas);

    }

    public static void addPalavras(List<String> proibidas) {
        proibidas.add("programa");
        proibidas.add("se");
        proibidas.add("entao");
        proibidas.add("senao");
        proibidas.add("enquanto");
        proibidas.add("faca");
        proibidas.add("ate");
        proibidas.add("repita");
        proibidas.add("inteiro");
        proibidas.add("real");
        proibidas.add("caractere");
        proibidas.add("caso");
        proibidas.add("escolha");
        proibidas.add("fim");
        proibidas.add("procedimento");
        proibidas.add("funcao");
        proibidas.add("de");
        proibidas.add("para");
        proibidas.add("inicio");
    }
}
