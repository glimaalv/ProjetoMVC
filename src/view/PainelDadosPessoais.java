package view;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

// Este painel é "burro". Ele só cria e exibe os componentes.
// A 'TelaPrincipal' (o cérebro) vai pegar os componentes dele usando os "getters".
public class PainelDadosPessoais extends JPanel {

    // --- Atributos dos Componentes da Aba 1 ---
    private JTextField txtRgm;
    private JTextField txtNome;
    private JFormattedTextField txtDataNasc;
    private JFormattedTextField txtCpf;
    private JTextField txtEmail;
    private JTextField txtEndereco;
    private JTextField txtMunicipio;
    private JTextField txtUf;
    private JFormattedTextField txtCelular;
    
    // --- Atributos dos Componentes do Curso ---
    private JComboBox<String> comboCurso;
    private JComboBox<String> comboCampus;
    private JRadioButton radioMatutino;
    private JRadioButton radioVespertino;
    private JRadioButton radioNoturno;
    private ButtonGroup grupoPeriodo; 
    
    // --- Botões do CRUD Aluno ---
    private JButton btnSalvarAluno;
    private JButton btnAlterarAluno;
    private JButton btnConsultarAluno;
    private JButton btnExcluirAluno;
    private JButton btnLimparCampos;
    
    /**
     * Construtor do painel de Dados Pessoais e Curso
     */
    public PainelDadosPessoais() {
        // Usa o código do seu antigo método criarPainelDadosPessoais()
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        
        try { 
            
            // --- Linha 0: RGM e Nome ---
            gbc.gridx = 0; gbc.gridy = 0;
            gbc.fill = GridBagConstraints.NONE; 
            gbc.anchor = GridBagConstraints.EAST; 
            gbc.weightx = 0.0;
            this.add(new JLabel("RGM:"), gbc);
        
            gbc.gridx = 1; gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL; 
            gbc.weightx = 0.5;
            txtRgm = new JTextField();
            this.add(txtRgm, gbc);
        
            gbc.gridx = 2; gbc.gridy = 0;
            gbc.fill = GridBagConstraints.NONE; 
            gbc.anchor = GridBagConstraints.EAST; 
            gbc.weightx = 0.0;
            this.add(new JLabel("Nome:"), gbc);
        
            gbc.gridx = 3; gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL; 
            gbc.weightx = 1.0;
            txtNome = new JTextField();
            this.add(txtNome, gbc);
        
            // --- Linha 1: Data de Nasc. e CPF ---
            gbc.gridx = 0; gbc.gridy = 1;
            gbc.fill = GridBagConstraints.NONE; 
            gbc.anchor = GridBagConstraints.EAST; 
            gbc.weightx = 0.0;
            this.add(new JLabel("Data de Nasc.:"), gbc);

            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtDataNasc = new JFormattedTextField(mascaraData);
            gbc.gridx = 1; gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.5;
            this.add(txtDataNasc, gbc);

            gbc.gridx = 2; gbc.gridy = 1;
            gbc.fill = GridBagConstraints.NONE; 
            gbc.anchor = GridBagConstraints.EAST; 
            gbc.weightx = 0.0;
            this.add(new JLabel("CPF:"), gbc);

            MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
            mascaraCpf.setPlaceholderCharacter('_');
            txtCpf = new JFormattedTextField(mascaraCpf);
            gbc.gridx = 3; gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            this.add(txtCpf, gbc);

            // --- Linha 2: Email ---
            gbc.gridx = 0; gbc.gridy = 2;
            gbc.fill = GridBagConstraints.NONE; 
            gbc.anchor = GridBagConstraints.EAST; 
            gbc.weightx = 0.0;
            this.add(new JLabel("Email:"), gbc);
        
            txtEmail = new JTextField();
            gbc.gridx = 1; gbc.gridy = 2;
            gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            this.add(txtEmail, gbc);
        
            // --- Linha 3: Endereço ---
            gbc.gridx = 0; gbc.gridy = 3;
            gbc.fill = GridBagConstraints.NONE; 
            gbc.anchor = GridBagConstraints.EAST; 
            gbc.weightx = 0.0;
            gbc.gridwidth = 1; // Reseta
            this.add(new JLabel("Endereço:"), gbc);
        
            txtEndereco = new JTextField();
            gbc.gridx = 1; gbc.gridy = 3;
            gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            this.add(txtEndereco, gbc);
        
            // --- Linha 4: Município, UF, Celular ---
            JPanel painelLinha4 = new JPanel(new GridBagLayout());
            GridBagConstraints gbcLinha4 = new GridBagConstraints();
            gbcLinha4.insets = new Insets(0, 0, 0, 5);
        
            gbcLinha4.gridx = 0; gbcLinha4.fill = GridBagConstraints.NONE; 
            gbcLinha4.anchor = GridBagConstraints.EAST; 
            gbcLinha4.weightx = 0.0;
            painelLinha4.add(new JLabel("Município:"), gbcLinha4);
        
            txtMunicipio = new JTextField();
            gbcLinha4.gridx = 1; gbcLinha4.fill = GridBagConstraints.HORIZONTAL; gbcLinha4.weightx = 0.6;
            painelLinha4.add(txtMunicipio, gbcLinha4);
        
            gbcLinha4.gridx = 2; gbcLinha4.fill = GridBagConstraints.NONE; 
            gbcLinha4.anchor = GridBagConstraints.EAST; 
            gbcLinha4.weightx = 0.0;
            painelLinha4.add(new JLabel("UF:"), gbcLinha4);
        
            txtUf = new JTextField();
            gbcLinha4.gridx = 3; gbcLinha4.fill = GridBagConstraints.HORIZONTAL; gbcLinha4.weightx = 0.1;
            painelLinha4.add(txtUf, gbcLinha4);
            
            gbcLinha4.gridx = 4; gbcLinha4.fill = GridBagConstraints.NONE; 
            gbcLinha4.anchor = GridBagConstraints.EAST; 
            gbcLinha4.weightx = 0.0;
            painelLinha4.add(new JLabel("Celular:"), gbcLinha4);
        
            MaskFormatter mascaraCel = new MaskFormatter("(##) #####-####");
            mascaraCel.setPlaceholderCharacter('_');
            txtCelular = new JFormattedTextField(mascaraCel);
            gbcLinha4.gridx = 5; gbcLinha4.fill = GridBagConstraints.HORIZONTAL; gbcLinha4.weightx = 0.3;
            gbcLinha4.insets = new Insets(0, 0, 0, 0); 
            painelLinha4.add(txtCelular, gbcLinha4);
        
            gbc.gridx = 0; gbc.gridy = 4;
            gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            this.add(painelLinha4, gbc);
        
            // --- Linha 5: Painel Interno "Curso" ---
            JPanel painelCursoInterno = new JPanel(new GridBagLayout());
            painelCursoInterno.setBorder(BorderFactory.createTitledBorder("Curso"));
            GridBagConstraints gbcCurso = new GridBagConstraints();
            gbcCurso.insets = new Insets(5, 5, 5, 5);
            gbcCurso.fill = GridBagConstraints.HORIZONTAL;

            gbcCurso.gridx = 0; gbcCurso.gridy = 0;
            gbcCurso.fill = GridBagConstraints.NONE; 
            gbcCurso.anchor = GridBagConstraints.EAST; 
            gbcCurso.weightx = 0.0;
            painelCursoInterno.add(new JLabel("Curso:"), gbcCurso);

            comboCurso = new JComboBox<>();
            gbcCurso.gridx = 1; gbcCurso.gridy = 0;
            gbcCurso.fill = GridBagConstraints.HORIZONTAL; 
            gbcCurso.weightx = 1.0;
            painelCursoInterno.add(comboCurso, gbcCurso);
            
            gbcCurso.gridx = 0; gbcCurso.gridy = 1;
            gbcCurso.fill = GridBagConstraints.NONE; 
            gbcCurso.anchor = GridBagConstraints.EAST; 
            gbcCurso.weightx = 0.0;
            painelCursoInterno.add(new JLabel("Campus:"), gbcCurso);

            comboCampus = new JComboBox<>();
            gbcCurso.gridx = 1; gbcCurso.gridy = 1;
            gbcCurso.fill = GridBagConstraints.HORIZONTAL; 
            gbcCurso.weightx = 1.0;
            painelCursoInterno.add(comboCampus, gbcCurso);
            
            JPanel painelPeriodo = new JPanel(); 
            painelPeriodo.setBorder(BorderFactory.createTitledBorder("Período")); 
            radioMatutino = new JRadioButton("Matutino");
            radioVespertino = new JRadioButton("Vespertino");
            radioNoturno = new JRadioButton("Noturno");
            radioNoturno.setSelected(true);
            grupoPeriodo = new ButtonGroup();
            grupoPeriodo.add(radioMatutino);
            grupoPeriodo.add(radioVespertino);
            grupoPeriodo.add(radioNoturno);
            painelPeriodo.add(radioMatutino);
            painelPeriodo.add(radioVespertino);
            painelPeriodo.add(radioNoturno);
            
            gbcCurso.gridx = 0; gbcCurso.gridy = 2;
            gbcCurso.gridwidth = 2;
            gbcCurso.fill = GridBagConstraints.NONE; 
            gbcCurso.anchor = GridBagConstraints.WEST;
            painelCursoInterno.add(painelPeriodo, gbcCurso);
            
            gbc.gridx = 0; gbc.gridy = 5; 
            gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            this.add(painelCursoInterno, gbc);
            
            // --- Linha 6: Painel de Botões Aluno ---
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnSalvarAluno = new JButton("Salvar");
            btnAlterarAluno = new JButton("Alterar");
            btnConsultarAluno = new JButton("Consultar");
            btnExcluirAluno = new JButton("Excluir");
            btnLimparCampos = new JButton("Limpar"); 
            
            buttonPanel.add(btnSalvarAluno);
            buttonPanel.add(btnAlterarAluno);
            buttonPanel.add(btnConsultarAluno);
            buttonPanel.add(btnExcluirAluno);
            buttonPanel.add(btnLimparCampos); 
            
            gbc.gridx = 0;
            gbc.gridy = 6; 
            gbc.gridwidth = 4;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            this.add(buttonPanel, gbc);
            
            // --- "Peso morto" para empurrar tudo para cima ---
            gbc.gridx = 0;
            gbc.gridy = 7; 
            gbc.weighty = 1.0; 
            gbc.gridwidth = 1;
            this.add(new JLabel(""), gbc); 

        } catch (ParseException e) {
            e.printStackTrace();
            this.add(new JLabel("Erro ao carregar máscaras de formulário!"));
        }
    }

    // --- MÉTODOS GETTER ---
    // (Para a TelaPrincipal poder acessar os componentes)
    
    public JTextField getTxtRgm() { return txtRgm; }
    public JTextField getTxtNome() { return txtNome; }
    public JFormattedTextField getTxtDataNasc() { return txtDataNasc; }
    public JFormattedTextField getTxtCpf() { return txtCpf; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JTextField getTxtEndereco() { return txtEndereco; }
    public JTextField getTxtMunicipio() { return txtMunicipio; }
    public JTextField getTxtUf() { return txtUf; }
    public JFormattedTextField getTxtCelular() { return txtCelular; }
    public JComboBox<String> getComboCurso() { return comboCurso; }
    public JComboBox<String> getComboCampus() { return comboCampus; }
    public JRadioButton getRadioMatutino() { return radioMatutino; }
    public JRadioButton getRadioVespertino() { return radioVespertino; }
    public JRadioButton getRadioNoturno() { return radioNoturno; }
    public ButtonGroup getGrupoPeriodo() { return grupoPeriodo; }
    public JButton getBtnSalvarAluno() { return btnSalvarAluno; }
    public JButton getBtnAlterarAluno() { return btnAlterarAluno; }
    public JButton getBtnConsultarAluno() { return btnConsultarAluno; }
    public JButton getBtnExcluirAluno() { return btnExcluirAluno; }
    public JButton getBtnLimparCampos() { return btnLimparCampos; }
}