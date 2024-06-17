import java.io.Serializable;
import java.util.Arrays;

public class Manga implements Serializable {
    private String isbn; // 13 caracteres
    private String titulo; // 50 caracteres
    private String autor; // 50 caracteres
    private int anoInicio;
    private int anoFim;
    private String genero; // 20 caracteres
    private String revista; // 30 caracteres
    private String editora; // 30 caracteres
    private int anoEdicao;
    private int quantidadeVolumes;
    private int quantidadeVolumesAdquiridos;
    private int[] volumesAdquiridos; // 100 volumes no máximo

    public Manga(String isbn, String titulo, String autor, int anoInicio, int anoFim, String genero, String revista, String editora, int anoEdicao, int quantidadeVolumes, int quantidadeVolumesAdquiridos, int[] volumesAdquiridos) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anoInicio = anoInicio;
        this.anoFim = anoFim;
        this.genero = genero;
        this.revista = revista;
        this.editora = editora;
        this.anoEdicao = anoEdicao;
        this.quantidadeVolumes = quantidadeVolumes;
        this.quantidadeVolumesAdquiridos = quantidadeVolumesAdquiridos;
        this.volumesAdquiridos = Arrays.copyOf(volumesAdquiridos, 100);
    }

    // Getters and Setters here...

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnoInicio() {
        return anoInicio;
    }

    public int getAnoFim() {
        return anoFim;
    }

    public String getGenero() {
        return genero;
    }

    public String getRevista() {
        return revista;
    }

    public String getEditora() {
        return editora;
    }

    public int getAnoEdicao() {
        return anoEdicao;
    }

    public int getQuantidadeVolumes() {
        return quantidadeVolumes;
    }

    public int getQuantidadeVolumesAdquiridos() {
        return quantidadeVolumesAdquiridos;
    }

    public int[] getVolumesAdquiridos() {
        return volumesAdquiridos;
    }
}