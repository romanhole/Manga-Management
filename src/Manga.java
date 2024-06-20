import java.io.Serializable;
import java.util.List;

public class Manga implements Serializable {
    private String isbn;
    private String titulo;
    private String autor;
    private int anoInicio;
    private int anoFim;
    private String genero;
    private String revista;
    private String editora;
    private int anoEdicao;
    private int quantidadeVolumes;
    private int quantidadeVolumesAdquiridos;
    private List<Integer> volumesAdquiridos;

    public Manga(String isbn, String titulo, String autor, int anoInicio, int anoFim, String genero, String revista, String editora, int anoEdicao, int quantidadeVolumes, int quantidadeVolumesAdquiridos, List<Integer> volumesAdquiridos) {
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
        this.volumesAdquiridos = volumesAdquiridos;
    }

    // Getters and Setters

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnoInicio() { return anoInicio; }
    public int getAnoFim() { return anoFim; }
    public String getGenero() { return genero; }
    public String getRevista() { return revista; }
    public String getEditora() { return editora; }
    public int getAnoEdicao() { return anoEdicao; }
    public int getQuantidadeVolumes() { return quantidadeVolumes; }
    public int getQuantidadeVolumesAdquiridos() { return quantidadeVolumesAdquiridos; }
    public List<Integer> getVolumesAdquiridos() { return volumesAdquiridos; }

    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setAnoInicio(int anoInicio) { this.anoInicio = anoInicio; }
    public void setAnoFim(int anoFim) { this.anoFim = anoFim; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setRevista(String revista) { this.revista = revista; }
    public void setEditora(String editora) { this.editora = editora; }
    public void setAnoEdicao(int anoEdicao) { this.anoEdicao = anoEdicao; }
    public void setQuantidadeVolumes(int quantidadeVolumes) { this.quantidadeVolumes = quantidadeVolumes; }
    public void setQuantidadeVolumesAdquiridos(int quantidadeVolumesAdquiridos) { this.quantidadeVolumesAdquiridos = quantidadeVolumesAdquiridos; }
    public void setVolumesAdquiridos(List<Integer> volumesAdquiridos) { this.volumesAdquiridos = volumesAdquiridos; }
}