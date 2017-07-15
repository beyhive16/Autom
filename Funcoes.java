package autom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Funcoes {

    public void analise(List<Character> entrada, List<String> proibidas) throws IOException {
        String superString = "";
        List<Character> in = entrada;
        List<Character> tokenTEMP = new ArrayList<>();
        List<String> identificadores = new ArrayList<>();
        List<String> digitos = new ArrayList<>();
        List<String> simbolos = new ArrayList<>();
        boolean flag = false;
        int estado = 0;
        int incr = 0;
        
        File arquivo = new File("resposta.txt");
        FileWriter fw = new FileWriter(arquivo, true);
        BufferedWriter bw = new BufferedWriter(fw);
        
        thedo:
            do {
                switch (estado) {
                    case 0:
                        if (Character.isLetter(entrada.get(incr))) {
                            estado = 1;
                        } else if (Character.isDigit(entrada.get(incr))) {
                            estado = 2;
                        } else if (entrada.get(incr) == ';' || entrada.get(incr) == '.'
                                || entrada.get(incr) == '+' || entrada.get(incr) == '('
                                || entrada.get(incr) == ')' || entrada.get(incr) == '<'
                                || entrada.get(incr) == '>' || entrada.get(incr) == ':'
                                || entrada.get(incr) == '=' || entrada.get(incr) == '{'
                                || entrada.get(incr) == '}' || entrada.get(incr) == '*') {
                            estado = 3;
                        } else if (entrada.get(incr) == ',' || entrada.get(incr) == '-') {
                            estado = 4;
                        } else if (entrada.get(incr) == '@' || entrada.get(incr) == '/') {
                            estado = 5;
                        } else if (entrada.get(incr) == ' ' || entrada.get(incr) == '\n' 
                                || entrada.get(incr) == '\t'){
                            incr++;
                        } else{
                            System.out.println("BREAK! Caractere não reconhecido: "+entrada.get(incr));
                            bw.write("BREAK! Caractere não reconhecido: "+entrada.get(incr));
                            bw.newLine();
                            break thedo;
                        }
                        break;
                    case 1:
                        if (Character.isLetter(entrada.get(incr))) {
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            boolean duoarroba = false;
                            //boolean permitidoarroba = true;
                            nome:
                            while (true) {
                                if (Character.isLetter(entrada.get(incr)) || Character.isDigit(entrada.get(incr))) {
                                    tokenTEMP.add(entrada.get(incr));
                                } else if (entrada.get(incr) == '_'){
                                    estado = 4;
                                    //permitidoarroba = false;
                                    break nome;
                                }else if (entrada.get(incr) == '@'){
                                    tokenTEMP.add(entrada.get(incr));
                                    incr++;
                                    if((entrada.get(incr) == '@')){
                                        tokenTEMP.add(entrada.get(incr));
                                        duoarroba= true;
                                    }else{
                                        incr--;
                                    }
                                }else{
                                    estado = 0;
                                    boolean retorno;
                                    if (!verifica(tokenTEMP, proibidas, fw, bw)) {
                                        retorno = adicionar(tokenTEMP, identificadores, fw, bw, duoarroba);
                                        if(!retorno){
                                            System.out.println("BREAK! Identificador inválido");
                                            break thedo;
                                        }
                                    }
                                    break nome;
                                }
                                
                                incr++;
                            }
                        duoarroba=false;
                        //permitidoarroba=true;
                        }
                        
                        break;
                    case 2:
                        if (Character.isDigit(entrada.get(incr))) {
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            nome:
                            while (true) {
                                if (Character.isDigit(entrada.get(incr))) {
                                    tokenTEMP.add(entrada.get(incr));
                                } else if (Character.isLetter(entrada.get(incr)) || entrada.get(incr) == '@'){
                                    estado = 1;
                                    break nome;
                                }else {
                                    validaDigito(tokenTEMP, digitos, fw, bw);
                                    estado = 0;
                                    break nome;
                                }
                                incr++;
                            }
                            
                        }
                        break;
                    case 3:
                        if(entrada.get(incr) == '>' || entrada.get(incr) == ':'){
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            if(entrada.get(incr) == '='){
                                tokenTEMP.add(entrada.get(incr));
                                idOperador(tokenTEMP, simbolos, fw, bw);
                                estado = 0;
                                incr++;
                            }else{
                                idSimbol(tokenTEMP, simbolos, fw, bw);
                                estado = 0;
                                incr++;
                                break;
                            }
                        } else if (entrada.get(incr) == '<'){
                            //System.out.println("anterior: "+entrada.get(incr-1));
                            //System.out.println("achou o <");
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            if(entrada.get(incr) == '=' || entrada.get(incr) == '>'){
                                //System.out.println("achou o "+entrada.get(incr));
                                tokenTEMP.add(entrada.get(incr));
                                idOperador(tokenTEMP, simbolos, fw, bw);
                                estado = 0;
                                incr++;
                            }else{
                                //System.out.println("entrou no else no "+entrada.get(incr));
                                idSimbol(tokenTEMP, simbolos, fw, bw);
                                estado = 0;
                                //incr++;
                                break;
                            }
                        }else if (entrada.get(incr) == '.'){
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            if(entrada.get(incr) == '*'){
                                tokenTEMP.add(entrada.get(incr));
                                idOperador(tokenTEMP, simbolos, fw, bw);
                                estado = 0;
                                incr++;
                            }else{
                                idSimbol(tokenTEMP, simbolos, fw, bw);
                                estado = 0;
                                incr++;
                                break;
                            }
                        }else if (entrada.get(incr) == '(') {
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            if (entrada.get(incr) == '*') {
                                nome:
                                while (true) {
                                    incr++;
                                    //pos = incr + 1;
                                    if (entrada.get(incr) == '*') {
                                        incr++;
                                        if (entrada.get(incr) == ')') {
                                            System.out.println("comentario de muitas linhas");
                                            break nome;
                                        }
                                    }
                                    if(incr==entrada.size()-1){
                                        System.out.println("fim do programa! Final do comentário nao encontrado");
                                         bw.write("fim do programa! Final do comentário nao encontrado");
                                         bw.newLine();
                                         break thedo;
                                    }
                                }
                                tokenTEMP.clear();
                                incr++;
                                estado = 0;
                            }else {
                                idSimbol(tokenTEMP, simbolos, fw, bw);
                                incr++;
                                estado = 0;
                            }
                        }else{
                            tokenTEMP.add(entrada.get(incr));
                            idSimbol(tokenTEMP, simbolos, fw, bw);
                            estado = 0;
                            incr++;
                            break;
                        }
                    case 4:
                        if (entrada.get(incr) == '-') {
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            if (Character.isDigit(entrada.get(incr))) {
                                tokenTEMP.add(entrada.get(incr));
                                incr++;
                                nome:
                                while (true) {
                                    if (Character.isDigit(entrada.get(incr))) {
                                        tokenTEMP.add(entrada.get(incr));
                                    } else if (entrada.get(incr) == ',') {
                                        estado = 4;
                                        break nome;
                                    } else {
                                        estado = 0;
                                        incr++;
                                        validaDigito(tokenTEMP, digitos, fw, bw);
                                        break nome;
                                    }
                                    incr++;
                                }
                            } else if (entrada.get(incr) == '-') {
                                System.out.println("cadeia invalida por repeticao");
                                estado = 0;
                                incr++;
                                break;
                            } else {
                                idSimbol(tokenTEMP, simbolos, fw, bw);
                                incr++;
                                estado = 0;
                            }
                        } else if (entrada.get(incr) == ',') {
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            if (Character.isDigit(entrada.get(incr))) {
                                tokenTEMP.add(entrada.get(incr));
                                incr++;
                                nome:
                                while (true) {
                                    if (Character.isDigit(entrada.get(incr))) {
                                        tokenTEMP.add(entrada.get(incr));
                                    } else if (entrada.get(incr) == ',') {
                                        estado = 4;
                                        break nome;
                                    } else {
                                        estado = 0;
                                        incr++;
                                        validaDigito(tokenTEMP, digitos, fw, bw);
                                        break nome;
                                    }
                                    incr++;
                                }
                            } else if (entrada.get(incr) == ',') {
                                System.out.println("cadeia invalida por repeticao");
                                estado = 0;
                                incr++;
                                break;

                            } else {
                                idSimbol(tokenTEMP, simbolos, fw, bw);
                                incr++;
                                estado = 0;
                            }
                        }else if (entrada.get(incr) == '_') {
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            if (Character.isLetter(entrada.get(incr))) {
                                tokenTEMP.add(entrada.get(incr));
                                incr++;
                                nome:
                                while (true) {
                                    if (Character.isLetter(entrada.get(incr))) {
                                        tokenTEMP.add(entrada.get(incr));
                                    }else if (entrada.get(incr) == '_') {
                                        estado = 4;
                                        break nome;
                                    }else if (entrada.get(incr) == '@') {
                                        System.out.println("BREAK! Identificador invalido: @");
                                        break thedo;
                                    }else {
                                        estado = 0;
                                        boolean retorno;
                                        retorno = adicionar(tokenTEMP, identificadores, fw, bw,false);
                                        if(!retorno){
                                            System.out.println("BREAK! Identificador inválido");
                                            break thedo;
                                        }
                                        incr++;
                                        break nome;
                                    }
                                    incr++;
                                }
                            } else if (entrada.get(incr) == '_') {
                                System.out.println("cadeia invalida por repeticao");
                                estado = 0;
                                incr++;
                                break;

                            } else {
                                idSimbol(tokenTEMP, simbolos, fw, bw);
                                incr++;
                                estado = 0;
                            }
                        }
                        break;
                    case 5:
                        if (entrada.get(incr) == '@') {
                            //System.out.println("achou o 1º @");
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            if (entrada.get(incr) == '@') {
                                //System.out.println("achou o 2º @");
                                incr++;
                                nome:
                                while (entrada.get(incr) != '\n') {
                                    //System.out.println("ta no while no: " + entrada.get(incr));
                                    incr++;
                                }
                                System.out.println("comentario de uma linha");
                                bw.write("comentario de uma linha");
                                bw.newLine();
                                tokenTEMP.clear();
                                incr++;
                                estado = 0;
                            } else if (Character.isLetter(entrada.get(incr))) {
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            nome:
                                while (true) {
                                    if (Character.isLetter(entrada.get(incr))) {
                                        tokenTEMP.add(entrada.get(incr));
                                    }else if (entrada.get(incr) == '_') {
                                        estado = 4;
                                        break nome;
                                    }else if (entrada.get(incr) == '@') {
                                        estado = 5;
                                        break nome;
                                    }else {
                                        estado = 0;
                                        boolean retorno;
                                        retorno = adicionar(tokenTEMP, identificadores, fw, bw,false);
                                        if(!retorno){
                                            System.out.println("BREAK! Identificador inválido");
                                            break thedo;
                                        }
                                        incr++;
                                        break nome;
                                    }
                                    incr++;
                                }

                        }else {
                                idSimbol(tokenTEMP, simbolos, fw, bw);
                                incr++;
                                estado = 0;
                            }
                        } else if (entrada.get(incr) == '/') {
                            tokenTEMP.add(entrada.get(incr));
                            incr++;
                            boolean continua=true;
                            if (entrada.get(incr) == '/') {
                                nome:
                                while (true) {
                                    incr++;
                                    //pos = incr + 1;
                                    if (entrada.get(incr) == '/') {
                                        incr++;
                                        if (entrada.get(incr) == '/') {
                                            System.out.println("comentario de muitas linhas");
                                            bw.write("comentario de muitas linhas");
                                            bw.newLine();
                                            continua=false;
                                            break nome;
                                        }
                                    }
                                    if(incr==entrada.size()-1 && continua==true){
                                        System.out.println("fim do programa! Final do comentário nao encontrado");
                                         bw.write("fim do programa! Final do comentário nao encontrado");
                                         bw.newLine();
                                         break thedo;
                                    }
                                }
                                tokenTEMP.clear();
                                incr++;
                                estado = 0;
                            }else if(entrada.get(incr) == '-'){
                                tokenTEMP.add(entrada.get(incr));
                                idOperador(tokenTEMP, simbolos, fw, bw);
                                estado = 0;
                                incr++;
                            }else {
                                idSimbol(tokenTEMP, simbolos, fw, bw);
                                incr++;
                                estado = 0;
                            }
                        }
                }
            } while (incr < entrada.size());
        
            bw.close();
            fw.close();
    }

    public boolean verifica(List<Character> tokenTEMP, List<String> proibidas, FileWriter fw, BufferedWriter bw) throws IOException {
        String texto = "";
        for (Character c : tokenTEMP) {
            //System.out.println("char: " + c);
            texto += c;
        }
        if (proibidas.contains(texto)) {
            System.out.println(texto + " é palavra reservada");
            bw.write(texto + " é palavra reservada");
            bw.newLine();
            tokenTEMP.clear();
            return true;
        }
        return false;
    }

    public boolean adicionar(List<Character> tokenTEMP, List<String> identificadores, FileWriter fw, BufferedWriter bw, boolean flag) throws IOException {
        String texto = "";
        int cont=0;
        int i=0;
        boolean retorno = true;
        boolean valido = true;
        for (Character palavra : tokenTEMP) {
            texto += palavra;
            if(palavra=='_'){
                if(i!=1){
                    valido = false;
                }
                cont++;
            }
            i++;
        }
        if((Character.isLetter(tokenTEMP.get(0)) && flag == false) && (Character.isLetter(tokenTEMP.get(tokenTEMP.size()-1)) && cont<=1) && (valido==true)){
        identificadores.add(texto);
        System.out.println(texto + " é identificador");
        bw.write(texto + " é identificador");
        bw.newLine();
        }else{
            //System.out.println(texto+" não é identificador");
            retorno = false;
        }
        flag = true;
        valido = true;
        tokenTEMP.clear();
        return retorno;
    }

    public void validaDigito(List<Character> tokenTEMP, List<String> digitos, FileWriter fw, BufferedWriter bw) throws IOException {
        String texto = "";
        for (Character palavra : tokenTEMP) {
            texto += palavra;
        }
        digitos.add(texto);
        System.out.println(texto + " é digito");
        bw.write(texto + " é digito");
        bw.newLine();
        tokenTEMP.clear();
    }

    public void idSimbol(List<Character> tokenTEMP, List<String> simbolos, FileWriter fw, BufferedWriter bw) throws IOException {
        String texto = "";
        for (Character palavra : tokenTEMP) {
            texto += palavra;
        }
        if(!Character.isLetter(tokenTEMP.get(0)) && !Character.isDigit(tokenTEMP.get(0))){
            simbolos.add(texto);
        System.out.println(texto + " é simbolo");
        bw.write(texto + " é simbolo");
        bw.newLine();
        }else{
            //System.out.println(texto+" não é identificador");
        }
        tokenTEMP.clear();
    }
    public void idOperador(List<Character> tokenTEMP, List<String> simbolos, FileWriter fw, BufferedWriter bw) throws IOException {
        String texto = "";
        for (Character palavra : tokenTEMP) {
            texto += palavra;
        }
        simbolos.add(texto);
        System.out.println(texto + " é operador");
        bw.write(texto + " é operador");
        bw.newLine();
        tokenTEMP.clear();
    }
}