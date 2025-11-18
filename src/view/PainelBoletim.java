package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class PainelBoletim extends JPanel {

    // --- Atributos dos Componentes ---
    private JLabel lblBoletimRgm;
    private JLabel lblBoletimNome;
    private JLabel lblBoletimCurso;
    private JTable tabelaNotas;
    private DefaultTableModel tableModel; 
    private JButton btnGerarPDF;
    private JButton btnConsultarBoletim; 
    private JButton btnAlterarNota; 
    private JButton btnExcluirNota; 
    
    public PainelBoletim() {
        this.setLayout(new BorderLayout(5, 5));
        
        // --- Painel de Informações (TOPO) ---
        JPanel painelInfo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        Border bordaDisplay = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0;
        painelInfo.add(new JLabel("RGM:"), gbc);
        lblBoletimRgm = new JLabel();
        lblBoletimRgm.setBorder(bordaDisplay);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.3;
        painelInfo.add(lblBoletimRgm, gbc);
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0;
        painelInfo.add(new JLabel("Nome:"), gbc);
        lblBoletimNome = new JLabel();
        lblBoletimNome.setBorder(bordaDisplay);
        gbc.gridx = 3; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.7;
        painelInfo.add(lblBoletimNome, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0;
        painelInfo.add(new JLabel("Curso:"), gbc);
        lblBoletimCurso = new JLabel();
        lblBoletimCurso.setBorder(bordaDisplay);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        painelInfo.add(lblBoletimCurso, gbc);
        btnGerarPDF = new JButton("Gerar PDF");
        gbc.gridx = 4; gbc.gridy = 0;
        gbc.gridheight = 2; gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 1; gbc.weightx = 0.0;
        painelInfo.add(btnGerarPDF, gbc);
        this.add(painelInfo, BorderLayout.NORTH);

        // --- Tabela (CENTRO) ---
        String[] colunas = { "ID", "Semestre", "Disciplina", "Nota", "Faltas" };
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaNotas = new JTable(tableModel);
        tabelaNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.add(new JScrollPane(tabelaNotas), BorderLayout.CENTER);
        
        // --- Painel de Botões (SUL) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnConsultarBoletim = new JButton("Consultar Boletim");
        btnAlterarNota = new JButton("Alterar Nota Selecionada");
        btnExcluirNota = new JButton("Excluir Nota Selecionada");
        buttonPanel.add(btnConsultarBoletim);
        buttonPanel.add(btnAlterarNota);
        buttonPanel.add(btnExcluirNota);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // --- MÉTODOS GETTER ---
    public JLabel getLblBoletimRgm() { return lblBoletimRgm; }
    public JLabel getLblBoletimNome() { return lblBoletimNome; }
    public JLabel getLblBoletimCurso() { return lblBoletimCurso; }
    public JTable getTabelaNotas() { return tabelaNotas; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JButton getBtnGerarPDF() { return btnGerarPDF; }
    public JButton getBtnConsultarBoletim() { return btnConsultarBoletim; }
    public JButton getBtnAlterarNota() { return btnAlterarNota; }
    public JButton getBtnExcluirNota() { return btnExcluirNota; }
}