import java.util.logging.Level;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
class MatrizEsparsaNova <T>{

    public final int linhas; // numero de linhas


    public final int colunas;// numero de colunas

    private Celula head;

    private long numero_de_celulas;//numero de celulas nao nulas

    private class Celula{
        public Celula abaixo, direita;
        public int linha, coluna;
        public T item;
        public Celula(T item, int linha, int coluna){
            this.item = item;
            this.linha = linha;
            this.coluna = coluna;
        }
        public Celula(T item){
            this.item = item;
        }
        public Celula(int linha, int coluna){
            this.linha = linha;
            this.coluna = coluna;
        }
    }


    public MatrizEsparsaNova(int linhas, int colunas){// construtor da matriz esparsa que recebe numero de linhas e colunas
        this.linhas = linhas;
        this.colunas = colunas;
        this.head = new Celula(-1,-1);
        this.head.direita = head;
        this.head.abaixo = head;
        this.numero_de_celulas = 0;
        this.cabeca();
    }



    private Celula getCabeca(int linha, int coluna){ //retorna a linha e a coluna
        Celula aux = this.head, aux2;
        do{
            aux = aux.direita;
            if(aux.coluna==coluna)
                break;

        }while(aux!=head);
        aux2=aux;
        do{
            aux2 = aux2.abaixo;
            if(aux2.linha==linha)
                break;

        }while(aux!=aux2);
        return aux2;
    }


    private Celula getCelulaAt(int linha, int coluna){//retorna a celula que esta localizada a linha e a coluna
        Celula aux = head, a = aux;
        do{
            do{
                if(a.linha == linha && a.coluna == coluna){
                    return a;
                }
                a = a.direita;
            }while(a!=aux);
            aux = aux.abaixo;
            a = aux;
        }while(aux!=head);
        return null;
    }

    private void cabeca(){//utiliza os numero de linha e coluna para comecar a cabecq
        Celula aux = this.head;
        for(int i = 0; i<this.linhas; i++){
            Celula nova = new Celula(null, i, -1);
            nova.abaixo = aux.abaixo;
            nova.direita = nova;
            aux.abaixo = nova;
            aux = aux.abaixo;
        }
        aux = this.head;
        for(int i = 0; i<this.colunas; i++){
            Celula nova = new Celula(null, -1, i);
            nova.direita = aux.direita;
            nova.abaixo = nova;
            aux.direita = nova;
            aux = aux.direita;
        }
    }



    public void insere(T item, int linha, int coluna) throws NullPointerException, ArrayIndexOutOfBoundsException{//adiciona da posição linha e coluna
        if(item==null){
            throw new NullPointerException();
        }else{
            if(linha>=this.linhas && linha<0 || coluna>=this.colunas && coluna<0){
                throw new ArrayIndexOutOfBoundsException();
            }else{
                Celula ch = this.getCabeca(linha,-1), cv = this.getCabeca(-1, coluna), nova = new Celula(item, linha, coluna);
                if(ch.direita!=ch || cv.abaixo!=cv){
                    Celula a = ch, b = cv;
                    do{
                        if(a.coluna<coluna && a.direita.coluna>coluna){
                            break;
                        }
                        a = a.direita;
                    }while(a!=ch);
                    do{
                        if(b.linha<linha && b.abaixo.linha>linha){
                            break;
                        }
                        b = b.abaixo;
                    }while(b!=cv);

                    nova.direita = a.direita;
                    nova.abaixo = b.abaixo;
                    a.direita = nova;
                    b.abaixo = nova;
                }else{
                    if(ch.direita==ch){
                        nova.direita = ch.direita;
                        ch.direita = nova;
                    }
                    if(cv.abaixo==cv){
                        nova.abaixo = cv.abaixo;
                        cv.abaixo = nova;
                    }
                }
                this.numero_de_celulas++;
            }
        }
    }



    public long celulas_ocupadas(){// numero de celulas nao nulas

        return this.numero_de_celulas;
    }


    @Override
    public String toString(){ //retorna uma string da matriz com vazio no lugar das celulas
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<this.linhas; i++){
            for(int j = 0; j<this.colunas; j++){
                Celula aux = getCelulaAt(i, j);
                if(aux==null){
                    sb.append("");
                }else if(aux.item!=null){
                    sb.append(aux.item);
                }
                sb.append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }



    public T excluir(int i, int j) throws NullPointerException, EmptyMatrixException, ArrayIndexOutOfBoundsException{
        if(i>=this.linhas || j>=this.colunas)
            throw new ArrayIndexOutOfBoundsException();
        else if(this.numero_de_celulas==0)
            throw new NullPointerException() ; //EmptyMatrixException();
        else if(this.getCelulaAt(i, j)==null)
            throw new NullPointerException();
        else {
            Celula ch = this.getCabeca(i,-1), cv = this.getCabeca(-1, j), a = ch, b = cv, ap = null, bp = null;
            if(ch.direita!=ch && cv.abaixo !=cv){
                do{
                    ap = a;
                    a = a.direita;
                    if(a.coluna == j){
                        break;
                    }
                }while(a!=ch);
                do{
                    bp = b;
                    b = b.abaixo;
                    if(b.linha==i)
                        break;
                }while(b!=cv);
                ap.direita = a.direita;
                bp.abaixo = b.abaixo;
                this.numero_de_celulas--;
                return a.item;
            }
        }
        return null;
    }



    public void insereBordas(int largura, T valor){ //adiciona as bordas
        bordas(valor, largura, (this.linhas-1), 0, 0, (this.colunas-1));
    }


    public MatrizEsparsaNova<T> transposta(){  //retorna uma trasposta
        MatrizEsparsaNova<T> transposta = new MatrizEsparsaNova<>(this.colunas, this.linhas);
        for(int i = 0; i< transposta.linhas; i++){
            for(int j = 0; j<this.linhas; j++){
                T item = this.itemAt(j, i);
                if(item!=null)
                    transposta.insere(item, i, j);
            }
        }
        return transposta;
    }


    public MatrizEsparsaNova<T> rotacionar_sentidoHorario(){
        MatrizEsparsaNova<T> rotacionada = new MatrizEsparsaNova<>(this.colunas,this.linhas);
        int k;
        for(int i = 0; i< rotacionada.linhas; i++){
            k = rotacionada.colunas ;
            for(int j = 0; j<rotacionada.colunas; j++){
                k--;
                T item = this.itemAt(k, i);
                if(item!=null)
                    rotacionada.insere(item, i, j);
            }
        }
        return rotacionada;
    }


    public MatrizEsparsaNova<T> inverterVerticalmente(){
        return this.rotacionar_sentidoHorario().transposta();
    }



    public MatrizEsparsaNova<T> inverterHorizontalmente(){
        return this.transposta().rotacionar_sentidoHorario();
    }



    public void inverterCores(T maxVal) throws NullPointerException, EmptyMatrixException{
        for(int i = 0; i< this.linhas; i++){
            for(int j = 0; j <this.colunas; j++){
                T item = itemAt(i, j);
                if(item==null){
                    this.insere(maxVal, i, j);
                }else if(item.equals(maxVal)){
                    try {
                        this.excluir(i, j);
                    } catch (NullPointerException ex) {
                        Logger.getLogger(MatrizEsparsaNova.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (EmptyMatrixException ex) {
                        Logger.getLogger(MatrizEsparsaNova.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        Logger.getLogger(MatrizEsparsaNova.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    int a = (int) maxVal;
                    int b = (int) item;
                    b = a - b;
                    if((int)b==0){
                        this.excluir(i, j);
                    }else{
                        T t = (T)(Object)b;
                        getCelulaAt(i,j).item = t;
                    }
                }
            }
        }
    }


    public String gerarStringPGM(T maxVal){
        StringBuilder sb = new StringBuilder();
        sb.delete(0, sb.length());
        sb.append("P2\n");
        sb.append(this.colunas).append(" ").append(this.linhas).append("\n");
        sb.append(maxVal).append("\n");
        for(int i = 0; i<this.linhas; i++){
            for(int j = 0; j<this.colunas; j++){
                Celula aux = getCelulaAt(i, j);
                if(aux==null){
                    sb.append("0");
                }else if(aux.item!=null){
                    sb.append(aux.item);
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void bordas(T valor, int largura,int b, int t, int l, int r){
        if(t<largura){
            for(int i = l; i<=r; i++){
                this.insere(valor,t,i);
                this.insere(valor,b,i);
            }
            for(int i = (t+1); i<b; i++){
                this.insere(valor,i,l);
                this.insere(valor,i,r);
            }
            this.bordas(valor, largura, (b-1), (t+1), (l+1), (r-1));
        }
    }


    public T itemAt(int linha, int coluna){
        if(this.getCelulaAt(linha, coluna)==null){
            return null;
        }else
            return getCelulaAt(linha, coluna).item;
    }
}

