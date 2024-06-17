import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MangaGUI extends JFrame {
    private JTextField isbnField, tituloField, autorField, anoInicioField, anoFimField, generoField, revistaField, editoraField, anoEdicaoField, quantidadeVolumesField, quantidadeVolumesAdquiridosField, volumesAdquiridosField;
    private JTextArea outputArea;
    private MangaDatabase db;

    public MangaGUI() {
        db = new MangaDatabase();

        setTitle("Gerenciador de Mangás");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(13, 2));

        panel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        panel.add(isbnField);

        panel.add(new JLabel("Título:"));
        tituloField = new JTextField();
        panel.add(tituloField);

        panel.add(new JLabel("Autor:"));
        autorField = new JTextField();
        panel.add(autorField);

        panel.add(new JLabel("Ano de Início:"));
        anoInicioField = new JTextField();
        panel.add(anoInicioField);

        panel.add(new JLabel("Ano de Fim:"));
        anoFimField = new JTextField();
        panel.add(anoFimField);

        panel.add(new JLabel("Gênero:"));
        generoField = new JTextField();
        panel.add(generoField);

        panel.add(new JLabel("Revista:"));
        revistaField = new JTextField();
        panel.add(revistaField);

        panel.add(new JLabel("Editora:"));
        editoraField = new JTextField();
        panel.add(editoraField);

        panel.add(new JLabel("Ano da Edição:"));
        anoEdicaoField = new JTextField();
        panel.add(anoEdicaoField);

        panel.add(new JLabel("Quantidade de Volumes:"));
        quantidadeVolumesField = new JTextField();
        panel.add(quantidadeVolumesField);

        panel.add(new JLabel("Quantidade de Volumes Adquiridos:"));
        quantidadeVolumesAdquiridosField = new JTextField();
        panel.add(quantidadeVolumesAdquiridosField);

        panel.add(new JLabel("Volumes Adquiridos (separados por espaço):"));
        volumesAdquiridosField = new JTextField();
        panel.add(volumesAdquiridosField);

        JButton addButton = new JButton("Adicionar Mangá");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addManga();
            }
        });
        panel.add(addButton);

        JButton showAllButton = new JButton("Mostrar Todos os Mangás");
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllMangas();
            }
        });
        panel.add(showAllButton);
        add(panel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new GridLayout(2, 1));
        JPanel isbnPanel = new JPanel(new GridLayout(1, 2));
        isbnPanel.add(new JLabel("ISBN:"));
        JTextField searchIsbnField = new JTextField();
        isbnPanel.add(searchIsbnField);
        searchPanel.add(isbnPanel);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
        JButton searchButton = new JButton("Pesquisar Mangá");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = searchIsbnField.getText().trim();
                if (!isbn.isEmpty()) {
                    searchManga(isbn);
                } else {
                    showMessage("ISBN não pode estar vazio.");
                }
            }
        });
        buttonsPanel.add(searchButton);

        JButton updateButton = new JButton("Atualizar Mangá");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = searchIsbnField.getText().trim();
                if (!isbn.isEmpty()) {
                    updateMangaDialog(isbn);
                } else {
                    showMessage("ISBN não pode estar vazio.");
                }
            }
        });
        buttonsPanel.add(updateButton);

        JButton deleteButton = new JButton("Deletar Mangá");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = searchIsbnField.getText().trim();
                if (!isbn.isEmpty()) {
                    deleteManga(isbn);
                } else {
                    showMessage("ISBN não pode estar vazio.");
                }
            }
        });
        buttonsPanel.add(deleteButton);

        searchPanel.add(buttonsPanel);
        add(searchPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);
    }

    private void addManga() {
        try {
            String isbn = isbnField.getText().trim();
            String titulo = tituloField.getText().trim();
            String autor = autorField.getText().trim();
            int anoInicio = Integer.parseInt(anoInicioField.getText().trim());
            int anoFim = Integer.parseInt(anoFimField.getText().trim());
            String genero = generoField.getText().trim();
            String revista = revistaField.getText().trim();
            String editora = editoraField.getText().trim();
            int anoEdicao = Integer.parseInt(anoEdicaoField.getText().trim());
            int quantidadeVolumes = Integer.parseInt(quantidadeVolumesField.getText().trim());
            int quantidadeVolumesAdquiridos = Integer.parseInt(quantidadeVolumesAdquiridosField.getText().trim());
            String[] volumesAdquiridosStr = volumesAdquiridosField.getText().trim().split(" ");
            int[] volumesAdquiridos = new int[volumesAdquiridosStr.length];
            for (int i = 0; i < volumesAdquiridosStr.length; i++) {
                volumesAdquiridos[i] = Integer.parseInt(volumesAdquiridosStr[i].trim());
            }

            Manga manga = new Manga(isbn, titulo, autor, anoInicio, anoFim, genero, revista, editora, anoEdicao, quantidadeVolumes, quantidadeVolumesAdquiridos, volumesAdquiridos);
            db.createManga(manga);

            showMessage("Mangá adicionado com sucesso!");
        } catch (NumberFormatException e) {
            showMessage("Erro de formato numérico: " + e.getMessage());
        } catch (IOException e) {
            showMessage("Erro de I/O: " + e.getMessage());
        }
    }

    private void searchManga(String isbn) {
        try {
            Manga manga = db.readManga(isbn);
            if (manga != null) {
                outputArea.setText("ISBN: " + manga.getIsbn() + "\n"
                        + "Título: " + manga.getTitulo() + "\n"
                        + "Autor: " + manga.getAutor() + "\n"
                        + "Ano de Início: " + manga.getAnoInicio() + "\n"
                        + "Ano de Fim: " + manga.getAnoFim() + "\n"
                        + "Gênero: " + manga.getGenero() + "\n"
                        + "Revista: " + manga.getRevista() + "\n"
                        + "Editora: " + manga.getEditora() + "\n"
                        + "Ano da Edição: " + manga.getAnoEdicao() + "\n"
                        + "Quantidade de Volumes: " + manga.getQuantidadeVolumes() + "\n"
                        + "Quantidade de Volumes Adquiridos: " + manga.getQuantidadeVolumesAdquiridos() + "\n"
                        + "Volumes Adquiridos: " + Arrays.toString(manga.getVolumesAdquiridos()));
            } else {
                showMessage("Mangá não encontrado.");
            }
        } catch (IOException e) {
            showMessage("Erro de I/O: " + e.getMessage());
        }
    }

    private void updateMangaDialog(String isbn) {
        try {
            Manga manga = db.readManga(isbn);
            if (manga != null) {
                JDialog updateDialog = new JDialog(this, "Atualizar Mangá", true);
                updateDialog.setSize(400, 600);
                updateDialog.setLayout(new GridLayout(14, 2));

                JTextField tituloField = new JTextField(manga.getTitulo());
                JTextField autorField = new JTextField(manga.getAutor());
                JTextField anoInicioField = new JTextField(String.valueOf(manga.getAnoInicio()));
                JTextField anoFimField = new JTextField(String.valueOf(manga.getAnoFim()));
                JTextField generoField = new JTextField(manga.getGenero());
                JTextField revistaField = new JTextField(manga.getRevista());
                JTextField editoraField = new JTextField(manga.getEditora());
                JTextField anoEdicaoField = new JTextField(String.valueOf(manga.getAnoEdicao()));
                JTextField quantidadeVolumesField = new JTextField(String.valueOf(manga.getQuantidadeVolumes()));
                JTextField quantidadeVolumesAdquiridosField = new JTextField(String.valueOf(manga.getQuantidadeVolumesAdquiridos()));
                JTextField volumesAdquiridosField = new JTextField(Arrays.toString(manga.getVolumesAdquiridos()).replaceAll("[\\[\\],]", ""));

                updateDialog.add(new JLabel("Título:"));
                updateDialog.add(tituloField);

                updateDialog.add(new JLabel("Autor:"));
                updateDialog.add(autorField);

                updateDialog.add(new JLabel("Ano de Início:"));
                updateDialog.add(anoInicioField);

                updateDialog.add(new JLabel("Ano de Fim:"));
                updateDialog.add(anoFimField);

                updateDialog.add(new JLabel("Gênero:"));
                updateDialog.add(generoField);

                updateDialog.add(new JLabel("Revista:"));
                updateDialog.add(revistaField);

                updateDialog.add(new JLabel("Editora:"));
                updateDialog.add(editoraField);

                updateDialog.add(new JLabel("Ano da Edição:"));
                updateDialog.add(anoEdicaoField);

                updateDialog.add(new JLabel("Quantidade de Volumes:"));
                updateDialog.add(quantidadeVolumesField);

                updateDialog.add(new JLabel("Quantidade de Volumes Adquiridos:"));
                updateDialog.add(quantidadeVolumesAdquiridosField);

                updateDialog.add(new JLabel("Volumes Adquiridos (separados por espaço):"));
                updateDialog.add(volumesAdquiridosField);

                JButton updateButton = new JButton("Atualizar");
                updateButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String newTitulo = tituloField.getText().trim();
                            String newAutor = autorField.getText().trim();
                            int newAnoInicio = Integer.parseInt(anoInicioField.getText().trim());
                            int newAnoFim = Integer.parseInt(anoFimField.getText().trim());
                            String newGenero = generoField.getText().trim();
                            String newRevista = revistaField.getText().trim();
                            String newEditora = editoraField.getText().trim();
                            int newAnoEdicao = Integer.parseInt(anoEdicaoField.getText().trim());
                            int newQuantidadeVolumes = Integer.parseInt(quantidadeVolumesField.getText().trim());
                            int newQuantidadeVolumesAdquiridos = Integer.parseInt(quantidadeVolumesAdquiridosField.getText().trim());
                            String[] newVolumesAdquiridosStr = volumesAdquiridosField.getText().trim().split(" ");
                            int[] newVolumesAdquiridos = new int[newVolumesAdquiridosStr.length];
                            for (int i = 0; i < newVolumesAdquiridosStr.length; i++) {
                                newVolumesAdquiridos[i] = Integer.parseInt(newVolumesAdquiridosStr[i].trim());
                            }

                            Manga updatedManga = new Manga(isbn, newTitulo, newAutor, newAnoInicio, newAnoFim, newGenero, newRevista, newEditora, newAnoEdicao, newQuantidadeVolumes, newQuantidadeVolumesAdquiridos, newVolumesAdquiridos);
                            db.updateManga(isbn, updatedManga);

                            updateDialog.dispose();
                            showMessage("Mangá atualizado com sucesso!");
                        } catch (NumberFormatException ex) {
                            showMessage("Erro de formato numérico: " + ex.getMessage());
                        } catch (IOException ex) {
                            showMessage("Erro de I/O: " + ex.getMessage());
                        }
                    }
                });
                updateDialog.add(updateButton);

                updateDialog.setVisible(true);
            } else {
                showMessage("Mangá não encontrado.");
            }
        } catch (IOException e) {
            showMessage("Erro de I/O: " + e.getMessage());
        }
    }

    private void deleteManga(String isbn) {
        try {
            int option = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o mangá com ISBN: " + isbn + "?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                db.deleteManga(isbn);
                showMessage("Mangá deletado com sucesso!");
            }
        } catch (IOException e) {
            showMessage("Erro de I/O: " + e.getMessage());
        }
    }

    private void showAllMangas() {
        try {
            List<Manga> mangas = db.getAllMangas();
            if (mangas.isEmpty()) {
                showMessage("Não há mangás cadastrados.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Manga manga : mangas) {
                    sb.append("ISBN: ").append(manga.getIsbn()).append("\n")
                            .append("Título: ").append(manga.getTitulo()).append("\n")
                            .append("Autor: ").append(manga.getAutor()).append("\n")
                            .append("Ano de Início: ").append(manga.getAnoInicio()).append("\n")
                            .append("Ano de Fim: ").append(manga.getAnoFim()).append("\n")
                            .append("Gênero: ").append(manga.getGenero()).append("\n")
                            .append("Revista: ").append(manga.getRevista()).append("\n")
                            .append("Editora: ").append(manga.getEditora()).append("\n")
                            .append("Ano da Edição: ").append(manga.getAnoEdicao()).append("\n")
                            .append("Quantidade de Volumes: ").append(manga.getQuantidadeVolumes()).append("\n")
                            .append("Quantidade de Volumes Adquiridos: ").append(manga.getQuantidadeVolumesAdquiridos()).append("\n")
                            .append("Volumes Adquiridos: ").append(Arrays.toString(manga.getVolumesAdquiridos())).append("\n\n");
                }
                outputArea.setText(sb.toString());
            }
        } catch (IOException e) {
            showMessage("Erro de I/O: " + e.getMessage());
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MangaGUI().setVisible(true);
            }
        });
    }
}
