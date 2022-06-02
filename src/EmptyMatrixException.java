



public class EmptyMatrixException extends Exception{
    /**
     * Construtor vazio da Exceção;
     */
    public EmptyMatrixException(){
        super("Matriz vazia");
    }


    public EmptyMatrixException(String message){
        super(message);
    }
}

