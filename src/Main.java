import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static final Map<Integer, String> arquivoTxt;

    static {
        arquivoTxt = new HashMap<>();
        arquivoTxt.put(1, "src/casos1(1)/c1a.txt");
        arquivoTxt.put(2, "src/casos1(1)/c1b.txt");
        arquivoTxt.put(3, "src/casos1(1)/c1c.txt");
        arquivoTxt.put(4, "src/casos1(1)/c1d.txt");
        arquivoTxt.put(5, "src/casos1(1)/c1e.txt");
        arquivoTxt.put(6, "src/casos1(1)/c1f.txt");
        arquivoTxt.put(7, "src/casos1(1)/c1h.txt");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o número do arquivo que deseja ler (de 1 a 7): ");
        int numeroArquivo = scanner.nextInt();
        String caminhoRelativo = arquivoTxt.get(numeroArquivo);
        if (caminhoRelativo == null) {
            System.out.println("Número inválido.");
        }
        System.out.println("Arquivo selecionado: " + caminhoRelativo);
        String caminhoAbsoluto = new File(caminhoRelativo).getAbsolutePath();
        File arquivo = new File(caminhoAbsoluto);
        if (!arquivo.exists()) {
            System.out.println("Arquivo não encontrado: " + caminhoAbsoluto);
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(arquivo));
            List<String> linhas = new ArrayList<>();
            br.readLine();
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);

            }

            int menorCaminho = menorCaminhoAZ(linhas);
            if (menorCaminho != -1) {
                System.out.println("Menor caminho: " + menorCaminho);
            } else {
                System.out.println("Nenhum caminho encontrado.");
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static int menorCaminhoAZ(List<String> linhas) {
        int numLinhas = linhas.size();
        int numColunas = linhas.get(0).length();
        int[][] direcoes = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        Map<Character, List<int[]>> posicoes = new HashMap<>();

        // Mapear posições de cada letra
        for (int i = 0; i < numLinhas; i++) {
            for (int j = 0; j < numColunas; j++) {
                char c = linhas.get(i).charAt(j);
                posicoes.computeIfAbsent(c, k -> new ArrayList<>()).add(new int[]{i, j});
            }
        }

        // Verificar se temos letras de a até z
        for (char l = 'a'; l <= 'z'; l++) {
            if (!posicoes.containsKey(l)) return -1;
        }

        int passosTotais = 0;

        // Caminhar de 'a' até 'z'
        for (char letra = 'a'; letra < 'z'; letra++) {
            List<int[]> inicio = posicoes.get(letra);
            List<int[]> destino = posicoes.get((char) (letra + 1));
            Set<String> destinos = destino.stream()
                    .map(p -> p[0] + "," + p[1])
                    .collect(Collectors.toSet());

            Queue<int[]> fila = new LinkedList<>();
            Set<String> visitado = new HashSet<>();

            for (int[] p : inicio) {
                fila.add(new int[]{p[0], p[1], 0});
                visitado.add(p[0] + "," + p[1]);
            }

            boolean encontrou = false;

            while (!fila.isEmpty() && !encontrou) {
                int[] atual = fila.poll();
                int x = atual[0], y = atual[1], dist = atual[2];

                if (destinos.contains(x + "," + y)) {
                    System.out.println(letra + " → " + (char) (letra + 1) + " = " + dist + " passos");
                    System.out.println(letra + " → " + (char) (letra + 1) + ": (" + x + ", " + y + ")");
                    passosTotais += dist;
                    encontrou = true;
                    break;
                }

                for (int[] dir : direcoes) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    if (nx >= 0 && nx < numLinhas && ny >= 0 && ny < numColunas) {
                        char c = linhas.get(nx).charAt(ny);
                        if ((c == letra || c == letra + 1) && visitado.add(nx + "," + ny)) {
                            fila.add(new int[]{nx, ny, dist + 1});
                        }
                    }
                }
            }

            if (!encontrou) return -1;
        }

        return passosTotais;
    }




}