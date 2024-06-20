import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MangaGUI extends JFrame {
    private JTextField isbnField, tituloField, autorField, anoInicioField, anoFimField, generoField, revistaField, editoraField, anoEdicaoField, quantidadeVolumesField;
    private JTextArea outputArea;
    private MangaDatabase db;
    private DefaultListModel<Integer> volumesAdquiridosModel;
    private JList<Integer> volumesAdquiridosList;

    public MangaGUI() {
        db = new MangaDatabase();

        setTitle("Gerenciador de Mangás");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campos de entrada para dados do mangá
        inputPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx++;
        isbnField = new JTextField(20);
        inputPanel.add(isbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Título:"), gbc);
        gbc.gridx++;
        tituloField = new JTextField(30);
        inputPanel.add(tituloField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Autor:"), gbc);
        gbc.gridx++;
        autorField = new JTextField(30);
        inputPanel.add(autorField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Ano de Início:"), gbc);
        gbc.gridx++;
        anoInicioField = new JTextField(10);
        inputPanel.add(anoInicioField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Ano de Fim:"), gbc);
        gbc.gridx++;
        anoFimField = new JTextField(10);
        inputPanel.add(anoFimField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Gênero:"), gbc);
        gbc.gridx++;
        generoField = new JTextField(20);
        inputPanel.add(generoField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Revista:"), gbc);
        gbc.gridx++;
        revistaField = new JTextField(20);
        inputPanel.add(revistaField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Editora:"), gbc);
        gbc.gridx++;
        editoraField = new JTextField(20);
        inputPanel.add(editoraField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Ano da Edição:"), gbc);
        gbc.gridx++;
        anoEdicaoField = new JTextField(10);
        inputPanel.add(anoEdicaoField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Quantidade de Volumes:"), gbc);
        gbc.gridx++;
        quantidadeVolumesField = new JTextField(10);
        inputPanel.add(quantidadeVolumesField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Volumes Adquiridos:"), gbc);
        gbc.gridx++;
        volumesAdquiridosModel = new DefaultListModel<>();
        volumesAdquiridosList = new JList<>(volumesAdquiridosModel);
        inputPanel.add(new JScrollPane(volumesAdquiridosList), gbc);

        JPanel volumesAdquiridosPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField volumeField = new JTextField(10);
        volumesAdquiridosPanel.add(volumeField, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        JButton addVolumeButton = new JButton("Adicionar Volume");
        addVolumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int volume = Integer.parseInt(volumeField.getText().trim());
                    volumesAdquiridosModel.addElement(volume);
                    volumeField.setText("");
                } catch (NumberFormatException ex) {
                    showMessage("Volume deve ser um número.");
                }
            }
        });
        volumesAdquiridosPanel.add(addVolumeButton, gbc);

        gbc.gridx++;
        JButton removeVolumeButton = new JButton("Remover Volume");
        removeVolumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = volumesAdquiridosList.getSelectedIndex();
                if (selectedIndex != -1) {
                    volumesAdquiridosModel.remove(selectedIndex);
                } else {
                    showMessage("Selecione um volume para remover.");
                }
            }
        });
        volumesAdquiridosPanel.add(removeVolumeButton, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(volumesAdquiridosPanel, gbc);

        // Botões de ação
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addButton = new JButton("Adicionar Mangá");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addManga();
            }
        });
        buttonPanel.add(addButton);

        JButton showAllButton = new JButton("Mostrar Todos os Mangás");
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllMangas();
            }
        });
        buttonPanel.add(showAllButton);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.add(new JLabel("ISBN:"));
        JTextField searchIsbnField = new JTextField(20);
        searchPanel.add(searchIsbnField);

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
        searchPanel.add(searchButton);

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
        searchPanel.add(updateButton);

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
        searchPanel.add(deleteButton);

        buttonPanel.add(searchPanel);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        outputArea = new JTextArea(10, 80);
        outputArea.setEditable(false);
        mainPanel.add(new JScrollPane(outputArea), BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);
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
            List<Integer> volumesAdquiridos = new ArrayList<>();
            for (int i = 0; i < volumesAdquiridosModel.size(); i++) {
                volumesAdquiridos.add(volumesAdquiridosModel.get(i));
            }

            Manga manga = new Manga(isbn, titulo, autor, anoInicio, anoFim, genero, revista, editora, anoEdicao, quantidadeVolumes, volumesAdquiridos.size(), volumesAdquiridos);
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
                        + "Volumes Adquiridos: " + manga.getVolumesAdquiridos().toString());
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
                updateDialog.setLayout(new GridLayout(12, 2));

                JTextField tituloField = new JTextField(manga.getTitulo());
                JTextField autorField = new JTextField(manga.getAutor());
                JTextField anoInicioField = new JTextField(String.valueOf(manga.getAnoInicio()));
                JTextField anoFimField = new JTextField(String.valueOf(manga.getAnoFim()));
                JTextField generoField = new JTextField(manga.getGenero());
                JTextField revistaField = new JTextField(manga.getRevista());
                JTextField editoraField = new JTextField(manga.getEditora());
                JTextField anoEdicaoField = new JTextField(String.valueOf(manga.getAnoEdicao()));
                JTextField quantidadeVolumesField = new JTextField(String.valueOf(manga.getQuantidadeVolumes()));
                DefaultListModel<Integer> updateVolumesAdquiridosModel = new DefaultListModel<>();
                for (int volume : manga.getVolumesAdquiridos()) {
                    updateVolumesAdquiridosModel.addElement(volume);
                }
                JList<Integer> updateVolumesAdquiridosList = new JList<>(updateVolumesAdquiridosModel);

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

                updateDialog.add(new JLabel("Volumes Adquiridos:"));
                updateDialog.add(new JScrollPane(updateVolumesAdquiridosList));

                JPanel updateVolumesAdquiridosPanel = new JPanel(new GridLayout(2, 2));
                JTextField updateVolumeField = new JTextField();
                updateVolumesAdquiridosPanel.add(updateVolumeField);
                JButton updateAddVolumeButton = new JButton("Adicionar Volume");
                updateAddVolumeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int volume = Integer.parseInt(updateVolumeField.getText().trim());
                            updateVolumesAdquiridosModel.addElement(volume);
                            updateVolumeField.setText("");
                        } catch (NumberFormatException ex) {
                            showMessage("Volume deve ser um número.");
                        }
                    }
                });
                updateVolumesAdquiridosPanel.add(updateAddVolumeButton);

                JButton updateRemoveVolumeButton = new JButton("Remover Volume");
                updateRemoveVolumeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selectedIndex = updateVolumesAdquiridosList.getSelectedIndex();
                        if (selectedIndex != -1) {
                            updateVolumesAdquiridosModel.remove(selectedIndex);
                        } else {
                            showMessage("Selecione um volume para remover.");
                        }
                    }
                });
                updateVolumesAdquiridosPanel.add(updateRemoveVolumeButton);

                updateDialog.add(updateVolumesAdquiridosPanel);

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
                            List<Integer> newVolumesAdquiridos = new ArrayList<>();
                            for (int i = 0; i < updateVolumesAdquiridosModel.size(); i++) {
                                newVolumesAdquiridos.add(updateVolumesAdquiridosModel.get(i));
                            }

                            Manga updatedManga = new Manga(isbn, newTitulo, newAutor, newAnoInicio, newAnoFim, newGenero, newRevista, newEditora, newAnoEdicao, newQuantidadeVolumes, newVolumesAdquiridos.size(), newVolumesAdquiridos);
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
                            .append("Volumes Adquiridos: ").append(manga.getVolumesAdquiridos().toString()).append("\n\n");
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