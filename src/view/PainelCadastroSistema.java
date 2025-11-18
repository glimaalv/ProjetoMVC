package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox; 
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class PainelCadastroSistema extends JPanel {

    // --- Atributos dos Componentes ---
    private JTextField txtNovoCurso;
    private JButton btnSalvarCurso;
    private JButton btnExcluirCurso;
    private JList<String> listaCursos;
    private DefaultListModel<String> modeloListaCursos;

    private JTextField txtNovoCampus;
    private JButton btnSalvarCampus;
    private JButton btnExcluirCampus;
    private JList<String> listaCampus;
    private DefaultListModel<String> modeloListaCampus;
    
    // --- (MUDANÇA) Componentes de Disciplina ---
    private JComboBox<String> comboCursoDisciplina; // <-- NOVO
    private JTextField txtNovaDisciplina;
    private JButton btnSalvarDisciplina;
    private JButton btnExcluirDisciplina;
    private JList<String> listaDisciplinas;
    private DefaultListModel<String> modeloListaDisciplinas;
    
    public PainelCadastroSistema() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH; // Faz preencher

        // --- PAINEL DE CURSOS ---
        JPanel painelCursos = new JPanel(new BorderLayout(5, 5));
        painelCursos.setBorder(BorderFactory.createTitledBorder("Cursos"));
        modeloListaCursos = new DefaultListModel<>();
        listaCursos = new JList<>(modeloListaCursos);
        painelCursos.add(new JScrollPane(listaCursos), BorderLayout.CENTER);
        JPanel painelBotoesCurso = new JPanel(new FlowLayout(FlowLayout.CENTER));
        txtNovoCurso = new JTextField(20);
        btnSalvarCurso = new JButton("Adicionar");
        btnExcluirCurso = new JButton("Excluir Selecionado");
        painelBotoesCurso.add(new JLabel("Novo Curso:"));
        painelBotoesCurso.add(txtNovoCurso);
        painelBotoesCurso.add(btnSalvarCurso);
        painelBotoesCurso.add(btnExcluirCurso);
        painelCursos.add(painelBotoesCurso, BorderLayout.SOUTH);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 0.33; 
        this.add(painelCursos, gbc);

        // --- PAINEL DE CAMPUS ---
        JPanel painelCampus = new JPanel(new BorderLayout(5, 5));
        painelCampus.setBorder(BorderFactory.createTitledBorder("Campus"));
        modeloListaCampus = new DefaultListModel<>();
        listaCampus = new JList<>(modeloListaCampus);
        painelCampus.add(new JScrollPane(listaCampus), BorderLayout.CENTER);
        JPanel painelBotoesCampus = new JPanel(new FlowLayout(FlowLayout.CENTER));
        txtNovoCampus = new JTextField(20);
        btnSalvarCampus = new JButton("Adicionar");
        btnExcluirCampus = new JButton("Excluir Selecionado");
        painelBotoesCampus.add(new JLabel("Novo Campus:"));
        painelBotoesCampus.add(txtNovoCampus);
        painelBotoesCampus.add(btnSalvarCampus);
        painelBotoesCampus.add(btnExcluirCampus);
        painelCampus.add(painelBotoesCampus, BorderLayout.SOUTH);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weighty = 0.33; 
        this.add(painelCampus, gbc);

        // --- PAINEL DE DISCIPLINAS (MUDANÇA) ---
        JPanel painelDisciplinas = new JPanel(new BorderLayout(5, 5));
        painelDisciplinas.setBorder(BorderFactory.createTitledBorder("Disciplinas (por Curso)"));
        
        modeloListaDisciplinas = new DefaultListModel<>();
        listaDisciplinas = new JList<>(modeloListaDisciplinas);
        painelDisciplinas.add(new JScrollPane(listaDisciplinas), BorderLayout.CENTER);
        
        JPanel painelBotoesDisciplina = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        painelBotoesDisciplina.add(new JLabel("Vincular ao Curso:"));
        comboCursoDisciplina = new JComboBox<>(); // <-- NOVO COMBO
        painelBotoesDisciplina.add(comboCursoDisciplina);
        
        painelBotoesDisciplina.add(new JLabel("Nova Disciplina:"));
        txtNovaDisciplina = new JTextField(20);
        painelBotoesDisciplina.add(txtNovaDisciplina);
        
        btnSalvarDisciplina = new JButton("Adicionar");
        btnExcluirDisciplina = new JButton("Excluir Selecionado");
        painelBotoesDisciplina.add(btnSalvarDisciplina);
        painelBotoesDisciplina.add(btnExcluirDisciplina);
        
        painelDisciplinas.add(painelBotoesDisciplina, BorderLayout.SOUTH);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weighty = 0.33;
        this.add(painelDisciplinas, gbc);
    }
    
    // --- MÉTODOS GETTER (COM MUDANÇAS) ---
    public JTextField getTxtNovoCurso() { return txtNovoCurso; }
    public JButton getBtnSalvarCurso() { return btnSalvarCurso; }
    public JButton getBtnExcluirCurso() { return btnExcluirCurso; }
    public JList<String> getListaCursos() { return listaCursos; }
    public DefaultListModel<String> getModeloListaCursos() { return modeloListaCursos; }

    public JTextField getTxtNovoCampus() { return txtNovoCampus; }
    public JButton getBtnSalvarCampus() { return btnSalvarCampus; }
    public JButton getBtnExcluirCampus() { return btnExcluirCampus; }
    public JList<String> getListaCampus() { return listaCampus; }
    public DefaultListModel<String> getModeloListaCampus() { return modeloListaCampus; }

    public JComboBox<String> getComboCursoDisciplina() { return comboCursoDisciplina; }
    public JTextField getTxtNovaDisciplina() { return txtNovaDisciplina; }
    public JButton getBtnSalvarDisciplina() { return btnSalvarDisciplina; }
    public JButton getBtnExcluirDisciplina() { return btnExcluirDisciplina; }
    public JList<String> getListaDisciplinas() { return listaDisciplinas; }
    public DefaultListModel<String> getModeloListaDisciplinas() { return modeloListaDisciplinas; }
}