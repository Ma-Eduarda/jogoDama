package com.damas.objetos;

/**
 * Interface com os métodos abstratos das peças
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 */
public interface Peca {
    
    public static final int PEDRA_BRANCA = 0;
    public static final int DAMA_BRANCA = 1;
    public static final int PEDRA_VERMELHA = 2;
    public static final int DAMA_VERMELHA = 3;

    public void mover(Casa destino);
    
    public boolean isMovimentoValido(Casa destino);

    public int getTipo();
    
    public boolean isMesmaCor(Peca outraPeca);

    public boolean isDirecaoValida(int sentidoY);

    public boolean isDama();

    public boolean podeVirarDama(int y);
}