
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Sistema {

    private static Scanner scan;

    private static MatrizEsparsaNova<Integer> imagem;

    private static Scanner ler;

    private static Integer maxVal;

    private static String nome_imagem;

    public static void main(String args[]) {
        menu();
    }


    public static void menu(){
        System.out.println("Digite: ");
        System.out.println("1 - Para realizar o processamento com a imagem de exemplo ('exemplo.pgm');");
        // System.out.println("2 - Para ler uma imagem que esteja no diretório raiz do projeto; ");
        System.out.println("2 - Para sair.");

        scan = new Scanner(System.in);
        int i = scan.nextInt();
        switch(i){
            //aqui adiciona a imagem
            //infelimente nao consegui fazer para o usuario adicionar bonitinho
            case 1: ler_imagem("C:\\Users\\andre\\Downloads\\Telegram Desktop\\Trabalho1_bandeira.pgm");
            menu_imagem("C:\\Users\\andre\\Downloads\\Telegram Desktop\\Trabalho1_bandeira.pgm");
            break;
            default: System.exit(0);
        }
    }






    public static void imprime_imagem(){
        System.out.println(imagem);
        menu_imagem(nome_imagem);
    }




    public static void rotaciona(){
        imagem = imagem.rotacionar_sentidoHorario();
        System.out.println(imagem);escreverArquivo(geraNome("IMG_Rotacionada"), imagem);
        menu_imagem(nome_imagem);}



    public static void inverte_cores(){
        try{
            imagem.inverterCores(255);
            System.out.println(imagem);
            escreverArquivo(geraNome("IMG_CorInvert"), imagem);
            menu_imagem(nome_imagem);
        } catch (NullPointerException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyMatrixException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public static void insere_borda(){
        imagem.insereBordas(3, 255);
        System.out.println(imagem);
        escreverArquivo(geraNome("IMG_Borda"), imagem);
        menu_imagem(nome_imagem);
    }


    public static void inverte_horizontalmente(){
        imagem = imagem.inverterHorizontalmente();
        System.out.println(imagem);
        escreverArquivo(geraNome("IMG_InvertidaHoriz"), imagem);
        menu_imagem(nome_imagem);
    }


    public static void inverte_verticalmente(){
        imagem = imagem.inverterVerticalmente();
        System.out.println(imagem);
        escreverArquivo(geraNome("IMG_InvertidaVert"), imagem);
        menu_imagem(nome_imagem);
    }


    public static void transposta(){
        imagem = imagem.transposta();
        System.out.println(imagem);
        escreverArquivo(geraNome("IMG_Transposta"), imagem);
        menu_imagem(nome_imagem);
    }



    public static void menu_imagem(String nome){

        System.out.println("digite a opção ");
        System.out.println("-----------------------------------------------");
        System.out.println("1-imprimir imagem ");
        System.out.println("2-rotacionar imagem ");
        System.out.println("3-inverter cores da imagem");
        System.out.println("4-iserir bordas (3 px)");
        System.out.println("5-inverter imagem horizontalmente");
        System.out.println("6-inverter a imagem verticalmente ");
        System.out.println("7-transposta da imagem ");
        System.out.println("8-voltar ao primeiro menu ");
        System.out.println("0-finalizar programa");
        System.out.println("------------------------------------------------");

        scan = new Scanner(System.in);
        int i = scan.nextInt();
        switch(i){
            case 1: imprime_imagem();
                break;
            case 2: rotaciona();break;
            case 3:inverte_cores() ;break;
            case 4: insere_borda();break;
            case 5: inverte_horizontalmente();break;
            case 6: inverte_verticalmente();break;
            case 7: transposta();break;
            case 8: menu();break;
            default: System.exit(0);break;
        }
    }



    public static void ler_imagem(String arquivo){
        File a = new File(arquivo);
        try {
            ler = new Scanner(a);
        } catch (FileNotFoundException ex) {
            System.err.println("O arquivo não foi encontrado!");

        }
        String tipo;
        int linhas, colunas;
        if(a.exists()){
            tipo = ler.next();
            if(!tipo.equals("P2")){
                System.err.println("Imagem não suportada. ");
                System.exit(1);
            }
            while(!ler.hasNextInt()){
                ler.next();
            }
            colunas = ler.nextInt();
            linhas = ler.nextInt();
            maxVal = ler.nextInt();
            imagem = new MatrizEsparsaNova<>(linhas, colunas);

            for(int i = 0; i<linhas; i++){
                for(int j = 0; j<colunas; j++){
                    int b = ler.nextShort();
                    if(b!=0)
                        imagem.insere(b, i, j);
                }
            }
        }

    }


    public static void escreverArquivo(String nome, MatrizEsparsaNova<Integer> imagem){
        BufferedWriter arquivo;
        try {
            arquivo = new BufferedWriter(new FileWriter(nome));
            String img = imagem.gerarStringPGM(maxVal);
            arquivo.write(img);
            arquivo.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public static String geraNome(String t){
        Date data = new Date();
        Calendar hoje = Calendar.getInstance();
        hoje.setTime(data);
        return new StringBuilder().append(t).append("_").append(hoje.get(Calendar.DAY_OF_MONTH)).append("-").
                append(hoje.get(Calendar.MONTH)).append("-").append(hoje.get(Calendar.YEAR)).append("_").append(hoje.get(Calendar.HOUR)).
                append("h").append(hoje.get(Calendar.MINUTE)).append("m").append(hoje.get(Calendar.SECOND)).
                append("s").append(".pgm").toString();
    }
}


