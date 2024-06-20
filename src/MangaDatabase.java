import java.io.*;
import java.util.*;

public class MangaDatabase {
    private static final String DATA_FILE = "mangas.dat";
    private static final String INDEX_FILE = "mangas.idx";
    private static final String TITLE_INDEX_FILE = "titles.idx";

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

    public void deleteManga(String isbn) throws IOException {
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
                throw new IOException("Mangá não encontrado.");
            }

        } catch (IOException e) {
            throw new IOException("Erro ao apagar mangá: " + e.getMessage(), e);
        }
    }

    public List<Manga> getAllMangas() throws IOException {
        List<Manga> mangas = new ArrayList<>();
        try (RandomAccessFile dataFile = new RandomAccessFile(DATA_FILE, "r")) {
            while (dataFile.getFilePointer() < dataFile.length()) {
                mangas.add(readManga(dataFile));
            }
        } catch (IOException e) {
            throw new IOException("Erro ao ler todos os mangás: " + e.getMessage(), e);
        }
        return mangas;
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
        file.writeInt(manga.getVolumesAdquiridos().size());
        for (int volume : manga.getVolumesAdquiridos()) {
            file.writeInt(volume);
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
        List<Integer> volumesAdquiridos = new ArrayList<>();
        for (int i = 0; i < quantidadeVolumesAdquiridos; i++) {
            volumesAdquiridos.add(file.readInt());
        }
        return new Manga(isbn, titulo, autor, anoInicio, anoFim, genero, revista, editora, anoEdicao, quantidadeVolumes, volumesAdquiridos.size(), volumesAdquiridos);
    }

    private String padString(String str, int length) {
        return String.format("%-" + length + "s", str);
    }
}