import java.io.*;
import java.util.*;

public class MangaDatabase {
    private static final String DATA_FILE = "mangas.dat";
    private static final String INDEX_FILE = "mangas.idx";
    private static final String TITLE_INDEX_FILE = "titles.idx";
    private static final int RECORD_SIZE = 413; // Tamanho fixo do registro

    public static void main(String[] args) {
        MangaDatabase db = new MangaDatabase();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("1. Adicionar Mangá");
            System.out.println("2. Ler Mangá");
            System.out.println("3. Atualizar Mangá");
            System.out.println("4. Apagar Mangá");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consome a nova linha

            try {
                switch (choice) {
                    case 1:
                        db.createManga(getMangaDetails(scanner));
                        break;
                    case 2:
                        System.out.print("Digite o ISBN do mangá: ");
                        String isbn = scanner.nextLine();
                        Manga manga = db.readManga(isbn);
                        if (manga != null) {
                            printMangaDetails(manga);
                        } else {
                            System.out.println("Mangá não encontrado.");
                        }
                        break;
                    case 3:
                        System.out.print("Digite o ISBN do mangá a ser atualizado: ");
                        isbn = scanner.nextLine();
                        Manga updatedManga = getMangaDetails(scanner);
                        db.updateManga(isbn, updatedManga);
                        break;
                    case 4:
                        System.out.print("Digite o ISBN do mangá a ser apagado: ");
                        isbn = scanner.nextLine();
                        db.deleteManga(isbn, scanner);
                        break;
                    case 5:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                e.printStackTrace();
            }
        } while (choice != 5);

        scanner.close();
    }

    public void createManga(Manga manga) throws IOException {
        try (RandomAccessFile dataFile = new RandomAccessFile(DATA_FILE, "rw");
             RandomAccessFile indexFile = new RandomAccessFile(INDEX_FILE, "rw");
             RandomAccessFile titleIndexFile = new RandomAccessFile(TITLE_INDEX_FILE, "rw")) {

            long fileLength = dataFile.length();
            dataFile.seek(fileLength);
            writeManga(dataFile, manga);

            // Escreve o índice primário
            indexFile.seek(indexFile.length());
            indexFile.writeUTF(manga.getIsbn());
            indexFile.writeLong(fileLength);

            // Escreve o índice secundário
            titleIndexFile.seek(titleIndexFile.length());
            titleIndexFile.writeUTF(manga.getTitulo());
            titleIndexFile.writeUTF(manga.getIsbn());

        } catch (IOException e) {
            throw new IOException("Erro ao criar mangá: " + e.getMessage(), e);
        }
    }

    public Manga readManga(String isbn) throws IOException {
        try (RandomAccessFile dataFile = new RandomAccessFile(DATA_FILE, "r");
             RandomAccessFile indexFile = new RandomAccessFile(INDEX_FILE, "r")) {

            // Busca pelo índice primário
            while (indexFile.getFilePointer() < indexFile.length()) {
                String indexIsbn = indexFile.readUTF();
                long pointer = indexFile.readLong();
                if (indexIsbn.equals(isbn)) {
                    dataFile.seek(pointer);
                    return readManga(dataFile);
                }
            }

        } catch (IOException e) {
            throw new IOException("Erro ao ler mangá: " + e.getMessage(), e);
        }
        return null;
    }

    public void updateManga(String isbn, Manga newManga) throws IOException {
        try (RandomAccessFile dataFile = new RandomAccessFile(DATA_FILE, "rw");
             RandomAccessFile indexFile = new RandomAccessFile(INDEX_FILE, "rw")) {

            while (indexFile.getFilePointer() < indexFile.length()) {
                String indexIsbn = indexFile.readUTF();
                long pointer = indexFile.readLong();
                if (indexIsbn.equals(isbn)) {
                    dataFile.seek(pointer);
                    writeManga(dataFile, newManga);
                    return;
                }
            }
        } catch (IOException e) {
            throw new IOException("Erro ao atualizar mangá: " + e.getMessage(), e);
        }
    }

    public void deleteManga(String isbn, Scanner scanner) throws IOException {
        System.out.print("Tem certeza que deseja apagar o mangá? (s/n): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("s")) {
            try (RandomAccessFile dataFile = new RandomAccessFile(DATA_FILE, "rw");
                 RandomAccessFile indexFile = new RandomAccessFile(INDEX_FILE, "rw");
                 RandomAccessFile tempIndexFile = new RandomAccessFile("temp.idx", "rw")) {

                boolean found = false;
                while (indexFile.getFilePointer() < indexFile.length()) {
                    String indexIsbn = indexFile.readUTF();
                    long pointer = indexFile.readLong();
                    if (indexIsbn.equals(isbn)) {
                        found = true;
                    } else {
                        tempIndexFile.writeUTF(indexIsbn);
                        tempIndexFile.writeLong(pointer);
                    }
                }

                if (found) {
                    indexFile.setLength(0);
                    tempIndexFile.seek(0);
                    while (tempIndexFile.getFilePointer() < tempIndexFile.length()) {
                        indexFile.writeUTF(tempIndexFile.readUTF());
                        indexFile.writeLong(tempIndexFile.readLong());
                    }
                } else {
                    System.out.println("Mangá não encontrado.");
                }

            } catch (IOException e) {
                throw new IOException("Erro ao apagar mangá: " + e.getMessage(), e);
            }
        }
    }

    private void writeManga(RandomAccessFile file, Manga manga) throws IOException {
        file.writeUTF(manga.getIsbn());
        file.writeUTF(padString(manga.getTitulo(), 50));
        file.writeUTF(padString(manga.getAutor(), 50));
        file.writeInt(manga.getAnoInicio());
        file.writeInt(manga.getAnoFim());
        file.writeUTF(padString(manga.getGenero(), 20));
        file.writeUTF(padString(manga.getRevista(), 30));
        file.writeUTF(padString(manga.getEditora(), 30));
        file.writeInt(manga.getAnoEdicao());
        file.writeInt(manga.getQuantidadeVolumes());
        file.writeInt(manga.getQuantidadeVolumesAdquiridos());
        for (int i = 0; i < 100; i++) {
            file.writeInt(i < manga.getVolumesAdquiridos().length ? manga.getVolumesAdquiridos()[i] : 0);
        }
    }

    private Manga readManga(RandomAccessFile file) throws IOException {
        String isbn = file.readUTF();
        String titulo = file.readUTF().trim();
        String autor = file.readUTF().trim();
        int anoInicio = file.readInt();
        int anoFim = file.readInt();
        String genero = file.readUTF().trim();
        String revista = file.readUTF().trim();
        String editora = file.readUTF().trim();
        int anoEdicao = file.readInt();
        int quantidadeVolumes = file.readInt();
        int quantidadeVolumesAdquiridos = file.readInt();
        int[] volumesAdquiridos = new int[100];
        for (int i = 0; i < 100; i++) {
            volumesAdquiridos[i] = file.readInt();
        }
        return new Manga(isbn, titulo, autor, anoInicio, anoFim, genero, revista, editora, anoEdicao, quantidadeVolumes, quantidadeVolumesAdquiridos, volumesAdquiridos);
    }

    private String padString(String str, int length) {
        return String.format("%-" + length + "s", str);
    }

    private static Manga getMangaDetails(Scanner scanner) {
        String isbn;
        boolean isbnExists;

        do {
            System.out.print("ISBN: ");
            isbn = scanner.nextLine();
            try {
                isbnExists = checkDuplicateISBN(isbn);
                if (isbnExists) {
                    System.out.println("ISBN já existe. Por favor, digite um ISBN diferente.");
                }
            } catch (IOException e) {
                System.out.println("Erro ao verificar ISBN: " + e.getMessage());
                isbnExists = true; // Tratar como se o ISBN já existisse para forçar nova entrada
            }
        } while (isbnExists);

        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Ano de Início: ");
        int anoInicio = scanner.nextInt();
        System.out.print("Ano de Fim: ");
        int anoFim = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
        System.out.print("Gênero: ");
        String genero = scanner.nextLine();
        System.out.print("Revista: ");
        String revista = scanner.nextLine();
        System.out.print("Editora: ");
        String editora = scanner.nextLine();
        System.out.print("Ano da Edição: ");
        int anoEdicao = scanner.nextInt();
        System.out.print("Quantidade de Volumes: ");
        int quantidadeVolumes = scanner.nextInt();
        System.out.print("Quantidade de Volumes Adquiridos: ");
        int quantidadeVolumesAdquiridos = scanner.nextInt();
        int[] volumesAdquiridos = new int[100];
        System.out.print("Volumes Adquiridos (separados por espaço, termine com -1): ");
        for (int i = 0; i < quantidadeVolumesAdquiridos; i++) {
            volumesAdquiridos[i] = scanner.nextInt();
        }
        scanner.nextLine(); // Consume the newline
        return new Manga(isbn, titulo, autor, anoInicio, anoFim, genero, revista, editora, anoEdicao, quantidadeVolumes, quantidadeVolumesAdquiridos, volumesAdquiridos);
    }

    private static boolean checkDuplicateISBN(String isbn) throws IOException {
        try (RandomAccessFile indexFile = new RandomAccessFile(INDEX_FILE, "r")) {
            while (indexFile.getFilePointer() < indexFile.length()) {
                String indexIsbn = indexFile.readUTF();
                indexFile.readLong(); // avança o ponteiro para o próximo registro
                if (indexIsbn.equals(isbn)) {
                    return true; // ISBN já existe
                }
            }
        }
        return false; // ISBN não encontrado
    }

    private static void printMangaDetails(Manga manga) {
        System.out.println("ISBN: " + manga.getIsbn());
        System.out.println("Título: " + manga.getTitulo());
        System.out.println("Autor: " + manga.getAutor());
        System.out.println("Ano de Início: " + manga.getAnoInicio());
        System.out.println("Ano de Fim: " + manga.getAnoFim());
        System.out.println("Gênero: " + manga.getGenero());
        System.out.println("Revista: " + manga.getRevista());
        System.out.println("Editora: " + manga.getEditora());
        System.out.println("Ano da Edição: " + manga.getAnoEdicao());
        System.out.println("Quantidade de Volumes: " + manga.getQuantidadeVolumes());
        System.out.println("Quantidade de Volumes Adquiridos: " + manga.getQuantidadeVolumesAdquiridos());
        System.out.print("Volumes Adquiridos: ");
        for (int i = 0; i < manga.getQuantidadeVolumesAdquiridos(); i++) {
            System.out.print(manga.getVolumesAdquiridos()[i] + " ");
        }
        System.out.println();
    }
}