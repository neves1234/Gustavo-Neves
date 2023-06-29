
import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {

    private static final int TAMANHO_TABULEIRO = 10;
    private static final char VAZIA = '-';
    private static final char BARCO = 'B';
    private static final char ACERTO = 'X';
    private static final char ERRO = '~';

    private char[][] tabuleiroJogador1;
    private char[][] tabuleiroJogador2;
    private int barcosRestantesJogador1;
    private int barcosRestantesJogador2;
    private boolean jogador1VsComputador;

    public BatalhaNaval(boolean jogador1VsComputador) {
        this.jogador1VsComputador = jogador1VsComputador;
        this.tabuleiroJogador1 = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        this.tabuleiroJogador2 = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        this.barcosRestantesJogador1 = 10;
        this.barcosRestantesJogador2 = 10;
        inicializarTabuleiros();
    }

    private void inicializarTabuleiros() {
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                tabuleiroJogador1[i][j] = VAZIA;
                tabuleiroJogador2[i][j] = VAZIA;
            }
        }
    }

    private void exibirTabuleiro(char[][] tabuleiro) {
        System.out.print("  ");
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                char celula = tabuleiro[i][j];
                if (celula == BARCO && jogador1VsComputador) {
                    celula = VAZIA;
                }
                System.out.print(celula + " ");
            }
            System.out.println();
        }
    }

    private void colocarBarco(char[][] tabuleiro, int tamanho, boolean jogador1) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Informe a linha inicial: ");
        int linhaInicial = scanner.nextInt();
        System.out.print("Informe a coluna inicial: ");
        int colunaInicial = scanner.nextInt();
        System.out.print("Informe a direcao (0 - horizontal, 1 - vertical): ");
        int direcao = scanner.nextInt();

        boolean posicaoValida = verificarPosicaoBarco(tabuleiro, linhaInicial, colunaInicial, tamanho, direcao);

        if (posicaoValida) {
            if (direcao == 0) {
                for (int j = colunaInicial; j < colunaInicial + tamanho; j++) {
                    tabuleiro[linhaInicial][j] = BARCO;
                }
            } else {
                for (int j = linhaInicial; j < linhaInicial + tamanho; j++) {
                    tabuleiro[j][colunaInicial] = BARCO;
                }
            }

            if (jogador1) {
                barcosRestantesJogador1--;
            } else {
                barcosRestantesJogador2--;
            }
        } else {
            System.out.println("Posicao invalida! Tente novamente.");
            colocarBarco(tabuleiro, tamanho, jogador1);
        }
    }

    private boolean verificarPosicaoBarco(char[][] tabuleiro, int linhaInicial, int colunaInicial, int tamanho,
            int direcao) {
        if (direcao == 0) {
            if (colunaInicial + tamanho > TAMANHO_TABULEIRO) {
                return false;
            }
            for (int j = colunaInicial; j < colunaInicial + tamanho; j++) {
                if (tabuleiro[linhaInicial][j] == BARCO) {
                    return false;
                }
            }
        } else {
            if (linhaInicial + tamanho > TAMANHO_TABULEIRO) {
                return false;
            }
            for (int j = linhaInicial; j < linhaInicial + tamanho; j++) {
                if (tabuleiro[j][colunaInicial] == BARCO) {
                    return false;
                }
            }
        }
        return true;
    }

    private void alocarBarcosAutomaticamente(char[][] tabuleiro) {
        Random random = new Random();

        for (int tamanho = 4; tamanho >= 1; tamanho--) {
            int quantidade = 5 - tamanho;
            for (int i = 0; i < quantidade; i++) {
                boolean barcoColocado = false;
                while (!barcoColocado) {
                    int linha = random.nextInt(TAMANHO_TABULEIRO);
                    int coluna = random.nextInt(TAMANHO_TABULEIRO);
                    int direcao = random.nextInt(2);

                    boolean posicaoValida = verificarPosicaoBarco(tabuleiro, linha, coluna, tamanho, direcao);

                    if (posicaoValida) {
                        if (direcao == 0) {
                            for (int j = coluna; j < coluna + tamanho; j++) {
                                tabuleiro[linha][j] = BARCO;
                            }
                        } else {
                            for (int j = linha; j < linha + tamanho; j++) {
                                tabuleiro[j][coluna] = BARCO;
                            }
                        }

                        barcoColocado = true;
                    }
                }
            }
        }
    }

    private void realizarJogada(char[][] tabuleiroAtaque, char[][] tabuleiroDefesa, int jogador) {
        Scanner scanner = new Scanner(System.in);

        exibirTabuleiro(tabuleiroAtaque);

        System.out.print("Informe a linha do ataque: ");
        int linha = scanner.nextInt();
        System.out.print("Informe a coluna do ataque: ");
        int coluna = scanner.nextInt();

        if (linha < 0 || linha >= TAMANHO_TABULEIRO || coluna < 0 || coluna >= TAMANHO_TABULEIRO) {
            System.out.println("Posicao invalida! Tente novamente.");
            realizarJogada(tabuleiroAtaque, tabuleiroDefesa, jogador);
            return;
        }

        if (tabuleiroAtaque[linha][coluna] == ACERTO || tabuleiroAtaque[linha][coluna] == ERRO) {
            System.out.println("Voce ja atacou essa posicao! Tente novamente.");
            realizarJogada(tabuleiroAtaque, tabuleiroDefesa, jogador);
            return;
        }

        if (tabuleiroDefesa[linha][coluna] == BARCO) {
            tabuleiroAtaque[linha][coluna] = ACERTO;
            tabuleiroDefesa[linha][coluna] = ACERTO;
            System.out.println("Voce acertou um barco!");

            if (jogador == 1) {
                barcosRestantesJogador2--;
            } else {
                barcosRestantesJogador1--;
            }
        } else {
            tabuleiroAtaque[linha][coluna] = ERRO;
            tabuleiroDefesa[linha][coluna] = ERRO;
            System.out.println("Voce errou o ataque!");
        }
    }

    private void realizarAtaqueComputador(char[][] tabuleiroAtaque, char[][] tabuleiroDefesa) {
        Random random = new Random();
        int linha;
        int coluna;

        do {
            linha = random.nextInt(TAMANHO_TABULEIRO);
            coluna = random.nextInt(TAMANHO_TABULEIRO);
        } while (tabuleiroAtaque[linha][coluna] == ACERTO || tabuleiroAtaque[linha][coluna] == ERRO);

        if (tabuleiroDefesa[linha][coluna] == BARCO) {
            tabuleiroAtaque[linha][coluna] = ACERTO;
            tabuleiroDefesa[linha][coluna] = ACERTO;
            System.out.println("O computador acertou um barco!");

            barcosRestantesJogador1--;
        } else {
            tabuleiroAtaque[linha][coluna] = ERRO;
            tabuleiroDefesa[linha][coluna] = ERRO;
            System.out.println("O computador errou o ataque!");
        }
    }

    public void jogar() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Jogador 1 - Posicionamento dos barcos:");
        System.out.println("Escolha a opcao:");
        System.out.println("1. Posicionar manualmente");
        System.out.println("2. Posicionar automaticamente");
        System.out.print("Opcao: ");
        int opcao = scanner.nextInt();

        if (opcao == 1) {
            for (int i = 0; i < 1; i++) {
                exibirTabuleiro(tabuleiroJogador1);
                System.out.println("Coloque um barco de tamanho 4.");
                colocarBarco(tabuleiroJogador1, 4, true);
            }

            for (int i = 0; i < 2; i++) {
                exibirTabuleiro(tabuleiroJogador1);
                System.out.println("Coloque um barco de tamanho 3.");
                colocarBarco(tabuleiroJogador1, 3, true);
            }

            for (int i = 0; i < 3; i++) {
                exibirTabuleiro(tabuleiroJogador1);
                System.out.println("Coloque um barco de tamanho 2.");
                colocarBarco(tabuleiroJogador1, 2, true);
            }

            for (int i = 0; i < 4; i++) {
                exibirTabuleiro(tabuleiroJogador1);
                System.out.println("Coloque um barco de tamanho 1.");
                colocarBarco(tabuleiroJogador1, 1, true);
            }
        } else {
            alocarBarcosAutomaticamente(tabuleiroJogador1);
        }

        alocarBarcosAutomaticamente(tabuleiroJogador2);

        boolean jogador1Vez = true;

        while (barcosRestantesJogador1 > 0 && barcosRestantesJogador2 > 0) {
            System.out.println("----- Jogador 1 -----");
            realizarJogada(tabuleiroJogador1, tabuleiroJogador2, 1);

            if (barcosRestantesJogador2 == 0) {
                break;
            }

            if (jogador1VsComputador) {
                System.out.println("----- Jogador 2 (Computador) -----");
                realizarAtaqueComputador(tabuleiroJogador2, tabuleiroJogador1);
            } else {
                System.out.println("----- Jogador 2 -----");
                realizarJogada(tabuleiroJogador2, tabuleiroJogador1, 2);
            }

            if (barcosRestantesJogador1 == 0) {
                break;
            }
        }

        if (barcosRestantesJogador1 == 0) {
            System.out.println("Parabens, Jogador 1! Voce venceu o jogo!");
        } else {
            System.out.println("Parabens, Jogador 2! Voce venceu o jogo!");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha o modo de jogo:");
        System.out.println("1. Jogador vs. Computador");
        System.out.println("2. Jogador vs. Jogador");
        System.out.print("Opcao: ");
        int modoJogo = scanner.nextInt();

        BatalhaNaval jogo;

        if (modoJogo == 1) {
            jogo = new BatalhaNaval(true);
        } else {
            jogo = new BatalhaNaval(false);
        }

        jogo.jogar();
    }
}
