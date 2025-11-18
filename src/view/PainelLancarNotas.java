package view;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.util.Calendar; // <-- IMPORT ADICIONADO
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

public class PainelLancarNotas extends JPanel {

    // --- Atributos dos Componentes ---
    private JTextField txtRgmNotas;
    private JLabel lblNomeAlunoNotas;
    private JLabel lblCursoAlunoNotas;
    private JComboBox<String> comboDisciplina;
    private JComboBox<String> comboSemestre;
    private JSpinner spinnerNota;
    private JSpinner spinnerFaltas;
    private JButton btnSalvarNota;
    
    public PainelLancarNotas() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        Border bordaDisplay = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

        // --- Linha 0: RGM e Nome ---
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.EAST; 
        gbc.weightx = 0.0;
        this.add(new JLabel("RGM:"), gbc);
        
        txtRgmNotas = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.3;
        this.add(txtRgmNotas, gbc);
        
        lblNomeAlunoNotas = new JLabel(" [Digite o RGM e aperte Enter]");
        lblNomeAlunoNotas.setBorder(bordaDisplay);
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.7;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(lblNomeAlunoNotas, gbc);
        
        // --- Linha 1: Curso ---
        gbc.gridwidth = 1; // Reseta
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.EAST;
        this.add(new JLabel("Curso:"), gbc); 
        
        lblCursoAlunoNotas = new JLabel(" [Aguardando aluno...]");
        lblCursoAlunoNotas.setBorder(bordaDisplay);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(lblCursoAlunoNotas, gbc);
        
        // --- Linha 2: Disciplina ---
        gbc.gridwidth = 1; // Reseta
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.EAST; 
        gbc.weightx = 0.0;
        this.add(new JLabel("Disciplina:"), gbc);
        
        comboDisciplina = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(comboDisciplina, gbc);
        
        // --- Linha 3: Semestre, Nota, Faltas (Painel Interno) ---
        gbc.gridwidth = 1; // Reseta
        JPanel painelLinha3 = new JPanel(new GridBagLayout());
        GridBagConstraints gbcLinha3 = new GridBagConstraints();
        gbcLinha3.insets = new Insets(0, 5, 0, 5); 
        
        // Semestre
        gbcLinha3.gridx = 0; gbcLinha3.weightx = 0.0;
        gbcLinha3.fill = GridBagConstraints.NONE; 
        gbcLinha3.anchor = GridBagConstraints.EAST; 
        painelLinha3.add(new JLabel("Semestre:"), gbcLinha3);
        
        // Lógica para gerar o semestre atual e o anterior
        Calendar cal = Calendar.getInstance();
        int ano = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH); // 0-11

        String semestreAtual;
        String semestreAnterior;

        if (mes >= 6) { // Julho a Dezembro (Semestre 2)
            semestreAtual = ano + "-2";
            semestreAnterior = ano + "-1";
        } else { // Janeiro a Junho (Semestre 1)
            semestreAtual = ano + "-1";
            semestreAnterior = (ano - 1) + "-2"; // Semestre 2 do ano anterior
        }
        
        // Cria a lista dinâmica de semestres
        String[] semestres = { semestreAtual, semestreAnterior };
        
        comboSemestre = new JComboBox<>(semestres); // Popula o combo com os valores dinâmicos
        gbcLinha3.gridx = 1; gbcLinha3.weightx = 0.5;
        gbcLinha3.fill = GridBagConstraints.HORIZONTAL;
        painelLinha3.add(comboSemestre, gbcLinha3);
        
        // Nota
        gbcLinha3.gridx = 2; gbcLinha3.weightx = 0.0;
        gbcLinha3.fill = GridBagConstraints.NONE; 
        gbcLinha3.anchor = GridBagConstraints.EAST; 
        painelLinha3.add(new JLabel("Nota:"), gbcLinha3);
        
        SpinnerNumberModel modelNota = new SpinnerNumberModel(5.0, 0.0, 10.0, 0.5);
        spinnerNota = new JSpinner(modelNota);
        gbcLinha3.gridx = 3; gbcLinha3.weightx = 0.2;
        gbcLinha3.fill = GridBagConstraints.HORIZONTAL;
        painelLinha3.add(spinnerNota, gbcLinha3);
        
        // Faltas
        gbcLinha3.gridx = 4; gbcLinha3.weightx = 0.0;
        gbcLinha3.fill = GridBagConstraints.NONE; 
        gbcLinha3.anchor = GridBagConstraints.EAST; 
        painelLinha3.add(new JLabel("Faltas:"), gbcLinha3);
        
        SpinnerNumberModel modelFaltas = new SpinnerNumberModel(0, 0, 100, 1);
        spinnerFaltas = new JSpinner(modelFaltas);
        gbcLinha3.gridx = 5; gbcLinha3.weightx = 0.2;
        gbcLinha3.fill = GridBagConstraints.HORIZONTAL;
        gbcLinha3.insets = new Insets(0, 5, 0, 0);
        painelLinha3.add(spinnerFaltas, gbcLinha3);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        this.add(painelLinha3, gbc);
        
        // --- Linha 4: Painel de Botão Salvar Nota ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSalvarNota = new JButton("Salvar Nota");
        buttonPanel.add(btnSalvarNota);
        
        gbc.gridx = 0;
        gbc.gridy = 4; 
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(buttonPanel, gbc);
        
        // --- "Peso morto" para empurrar tudo para cima ---
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weighty = 1.0; 
        this.add(new JLabel(""), gbc); 
    }

    // --- MÉTODOS GETTER ---
    public JTextField getTxtRgmNotas() { return txtRgmNotas; }
    public JLabel getLblNomeAlunoNotas() { return lblNomeAlunoNotas; }
    public JLabel getLblCursoAlunoNotas() { return lblCursoAlunoNotas; }
    public JComboBox<String> getComboDisciplina() { return comboDisciplina; }
    public JComboBox<String> getComboSemestre() { return comboSemestre; }
    public JSpinner getSpinnerNota() { return spinnerNota; }
    public JSpinner getSpinnerFaltas() { return spinnerFaltas; }
    public JButton getBtnSalvarNota() { return btnSalvarNota; }
}