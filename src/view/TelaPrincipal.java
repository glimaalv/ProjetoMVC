package view;

// (Todos os imports que já tínhamos...)
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel; 
import javax.swing.JTextField; 
import javax.swing.JFormattedTextField; 
import javax.swing.text.MaskFormatter; 
import javax.swing.SwingUtilities;
import javax.swing.JComboBox; 
import javax.swing.JRadioButton; 
import javax.swing.ButtonGroup; 
import javax.swing.BorderFactory; 
import javax.swing.JMenuBar; 
import javax.swing.JMenu; 
import javax.swing.JMenuItem; 
import javax.swing.KeyStroke; 
import javax.swing.JSpinner; 
import javax.swing.SpinnerNumberModel; 
import javax.swing.border.Border; 
import java.awt.Color; 
import java.awt.BorderLayout;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 
import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent; 
import javax.swing.JOptionPane; 
import java.text.SimpleDateFormat; 
import java.text.ParseException; 
import java.sql.Date; 
import javax.swing.JTable; 
import javax.swing.JScrollPane; 
import javax.swing.table.DefaultTableModel; 
import java.util.List; 
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

// Nossas classes de back-end
import dao.AlunoDAO; 
import dao.NotasFaltasDAO; 
import model.Aluno; 
import model.NotasFaltas; 

import java.awt.event.InputEvent; 

public class TelaPrincipal extends JFrame {

    // (Todos os atributos da classe... permanecem os mesmos)
    // --- Atributos da Tela ---
    private JTabbedPane abas;
    
    private JPanel painelDadosPessoais;
    private JPanel painelCurso;
    private JPanel painelNotasFaltas;
    private JPanel painelBoletim;
    
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
    
    // --- Atributos dos Componentes da Aba 2 ---
    private JComboBox<String> comboCurso;
    private JComboBox<String> comboCampus;
    private JRadioButton radioMatutino;
    private JRadioButton radioVespertino;
    private JRadioButton radioNoturno;
    private ButtonGroup grupoPeriodo; 
    
    // --- Atributos dos Componentes da Aba 3 ---
    private JTextField txtRgmNotas;
    private JLabel lblNomeAlunoNotas;
    private JLabel lblCursoAlunoNotas;
    private JComboBox<String> comboDisciplina;
    private JComboBox<String> comboSemestre;
    private JSpinner spinnerNota;
    private JSpinner spinnerFaltas;
    
    // --- Atributos dos Componentes da Aba 4 (Boletim) ---
    private JLabel lblBoletimRgm;
    private JLabel lblBoletimNome;
    private JLabel lblBoletimCurso;
    private JTable tabelaNotas;
    private DefaultTableModel tableModel; 
    
    // --- Atributos dos Itens de Menu ---
    private JMenuItem itemSalvarAluno;
    private JMenuItem itemAlterarAluno;
    private JMenuItem itemConsultarAluno;
    private JMenuItem itemExcluirAluno;
    private JMenuItem itemSair;
    private JMenuItem itemSalvarNotas;
    private JMenuItem itemAlterarNotas;
    private JMenuItem itemExcluirNotas;
    private JMenuItem itemConsultarNotas;
    private JMenuItem itemSobre;
    
    // --- Variáveis de Estado ---
    private String rgmAlunoAtual; 
    private int idNotaSelecionada = -1;
    

    /**
     * Construtor da tela.
     */
    public TelaPrincipal() {
        // (Este método permanece o mesmo)
        // --- 1. Configurações básicas da janela ---
        setTitle("Projeto Cadastro de Aluno");
        setSize(700, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // --- 2. Adiciona a Barra de Menu ---
        setJMenuBar(criarBarraDeMenu()); 

        // --- 3. Cria o componente de Abas ---
        abas = new JTabbedPane();

        // --- 4. Cria os painéis ---
        painelDadosPessoais = criarPainelDadosPessoais(); 
        painelCurso = criarPainelCurso(); 
        painelNotasFaltas = criarPainelNotasFaltas(); 
        painelBoletim = criarPainelBoletim(); 
        
        // --- 5. Adiciona os painéis às abas ---
        abas.addTab("Dados Pessoais", painelDadosPessoais);
        abas.addTab("Curso", painelCurso);
        abas.addTab("Notas e Faltas", painelNotasFaltas);
        abas.addTab("Boletim", painelBoletim);

        // --- 6. Adiciona as abas na janela ---
        add(abas, BorderLayout.CENTER);
        
        // --- 7. Registra os "ouvintes" de eventos (cliques) ---
        adicionarListeners();
    }
    
    /**
     * Cria e retorna a barra de menu principal.
     */
    private JMenuBar criarBarraDeMenu() {
        // (Este método permanece o mesmo)
        JMenuBar menuBar = new JMenuBar();
        
        // --- Menu Aluno ---
        JMenu menuAluno = new JMenu("Aluno");
        menuBar.add(menuAluno);
        
        itemSalvarAluno = new JMenuItem("Salvar");
        itemSalvarAluno.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        menuAluno.add(itemSalvarAluno);
        
        itemAlterarAluno = new JMenuItem("Alterar");
        menuAluno.add(itemAlterarAluno);
        
        itemConsultarAluno = new JMenuItem("Consultar");
        menuAluno.add(itemConsultarAluno);
        
        itemExcluirAluno = new JMenuItem("Excluir");
        menuAluno.add(itemExcluirAluno);
        
        menuAluno.addSeparator();
        
        itemSair = new JMenuItem("Sair");
        itemSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_DOWN_MASK));
        menuAluno.add(itemSair);

        // --- Menu Notas e Faltas ---
        JMenu menuNotas = new JMenu("Notas e Faltas");
        menuBar.add(menuNotas);
        
        itemSalvarNotas = new JMenuItem("Salvar");
        menuNotas.add(itemSalvarNotas);
        
        itemAlterarNotas = new JMenuItem("Alterar");
        itemAlterarNotas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        menuNotas.add(itemAlterarNotas);

        itemExcluirNotas = new JMenuItem("Excluir");
        menuNotas.add(itemExcluirNotas);
        
        itemConsultarNotas = new JMenuItem("Consultar");
        menuNotas.add(itemConsultarNotas);

        // --- Menu Ajuda ---
        JMenu menuAjuda = new JMenu("Ajuda");
        menuBar.add(menuAjuda);
        
        itemSobre = new JMenuItem("Sobre");
        menuAjuda.add(itemSobre);
        
        return menuBar;
    }

    
    /**
     * Cria o painel com os campos de Dados Pessoais.
     */
    private JPanel criarPainelDadosPessoais() {
        // (Este método permanece o mesmo)
        JPanel painel = new JPanel();
        painel.setLayout(null); 
        
        // --- Campo RGM ---
        JLabel lblRgm = new JLabel("RGM:");
        lblRgm.setBounds(20, 30, 80, 25);
        painel.add(lblRgm);
        
        txtRgm = new JTextField();
        txtRgm.setBounds(100, 30, 120, 25);
        painel.add(txtRgm);
        
        // (Resto dos campos... Nome, Data, CPF, etc...)
        // --- Campo Nome ---
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(240, 30, 80, 25);
        painel.add(lblNome);
        
        txtNome = new JTextField();
        txtNome.setBounds(290, 30, 350, 25);
        painel.add(txtNome);
        
        // --- Campo Data de Nascimento ---
        JLabel lblDataNasc = new JLabel("Data de Nasc.:");
        lblDataNasc.setBounds(20, 70, 80, 25);
        painel.add(lblDataNasc);
        
        // --- Campo CPF ---
        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(240, 70, 80, 25);
        painel.add(lblCpf);
        
        // --- Campo Email ---
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 110, 80, 25);
        painel.add(lblEmail);
        
        txtEmail = new JTextField();
        txtEmail.setBounds(100, 110, 540, 25);
        painel.add(txtEmail);
        
        // --- Campo Endereço ---
        JLabel lblEnd = new JLabel("Endereço:");
        lblEnd.setBounds(20, 150, 80, 25);
        painel.add(lblEnd);
        
        txtEndereco = new JTextField();
        txtEndereco.setBounds(100, 150, 540, 25);
        painel.add(txtEndereco);
        
        // --- Campo Município ---
        JLabel lblMunicipio = new JLabel("Município:");
        lblMunicipio.setBounds(20, 190, 80, 25);
        painel.add(lblMunicipio);
        
        txtMunicipio = new JTextField();
        txtMunicipio.setBounds(100, 190, 200, 25);
        painel.add(txtMunicipio);
        
        // --- Campo UF ---
        JLabel lblUf = new JLabel("UF:");
        lblUf.setBounds(320, 190, 30, 25);
        painel.add(lblUf);
        
        txtUf = new JTextField();
        txtUf.setBounds(350, 190, 50, 25);
        painel.add(txtUf);
        
        // --- Campo Celular ---
        JLabel lblCelular = new JLabel("Celular:");
        lblCelular.setBounds(420, 190, 80, 25);
        painel.add(lblCelular);

        // --- Tratamento das Máscaras ---
        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtDataNasc = new JFormattedTextField(mascaraData);
            txtDataNasc.setBounds(100, 70, 120, 25);
            painel.add(txtDataNasc);
            
            MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
            mascaraCpf.setPlaceholderCharacter('_');
            txtCpf = new JFormattedTextField(mascaraCpf);
            txtCpf.setBounds(290, 70, 150, 25);
            painel.add(txtCpf);
            
            MaskFormatter mascaraCel = new MaskFormatter("(##) #####-####");
            mascaraCel.setPlaceholderCharacter('_');
            txtCelular = new JFormattedTextField(mascaraCel);
            txtCelular.setBounds(480, 190, 160, 25);
            painel.add(txtCelular);
            
        } catch (ParseException e) {
            System.err.println("Erro ao criar máscaras: " + e.getMessage());
        }
        
        return painel;
    }
    
    /**
     * Cria o painel com os campos de Curso.
     * (MÉTODO ATUALIZADO)
     */
    private JPanel criarPainelCurso() {
        JPanel painel = new JPanel();
        painel.setLayout(null); // Layout absoluto

        // --- Campo Curso (JComboBox) ---
        JLabel lblCurso = new JLabel("Curso:");
        lblCurso.setBounds(20, 30, 80, 25);
        painel.add(lblCurso);

        // Opções de exemplo (pode alterar)
        String[] cursos = { 
            "Análise e Desenvolvimento de Sistemas", 
            "Ciência da Computação", 
            "Engenharia de Software", 
            "Sistemas de Informação" 
        };
        comboCurso = new JComboBox<>(cursos);
        comboCurso.setBounds(100, 30, 300, 25);
        painel.add(comboCurso);

        // --- Campo Campus (JComboBox) ---
        JLabel lblCampus = new JLabel("Campus:");
        lblCampus.setBounds(20, 70, 80, 25);
        painel.add(lblCampus);

        // ***** ALTERAÇÃO AQUI *****
        // Opções de exemplo (pode alterar)
        String[] campus = { 
            "Tatuapé", 
            "Pinheiros", 
            "Santo Amaro", 
            "Barra Funda",
            "Guarulhos", // <-- Adicionado
            "São Paulo"  // <-- Adicionado
        };
        comboCampus = new JComboBox<>(campus);
        comboCampus.setBounds(100, 70, 300, 25);
        painel.add(comboCampus);
        // ***** FIM DA ALTERAÇÃO *****

        // --- Campo Período (JRadioButton) ---
        // Criamos um painel interno para agrupar os botões de rádio
        JPanel painelPeriodo = new JPanel();
        painelPeriodo.setBounds(20, 110, 400, 60);
        // Cria uma borda com título
        painelPeriodo.setBorder(BorderFactory.createTitledBorder("Período")); 
        painel.add(painelPeriodo);

        radioMatutino = new JRadioButton("Matutino");
        radioVespertino = new JRadioButton("Vespertino");
        radioNoturno = new JRadioButton("Noturno");
        radioNoturno.setSelected(true); // Deixa "Noturno" pré-selecionado (como na imagem)

        // Isso é o mais importante: agrupa os botões
        // Garante que só um pode ser selecionado por vez
        grupoPeriodo = new ButtonGroup();
        grupoPeriodo.add(radioMatutino);
        grupoPeriodo.add(radioVespertino);
        grupoPeriodo.add(radioNoturno);

        // Adiciona os botões ao painel interno
        painelPeriodo.add(radioMatutino);
        painelPeriodo.add(radioVespertino);
        painelPeriodo.add(radioNoturno);

        return painel;
    }

    
    /**
     * Cria o painel com os campos de Notas e Faltas.
     */
    private JPanel criarPainelNotasFaltas() {
        // (Este método permanece o mesmo)
        JPanel painel = new JPanel();
        painel.setLayout(null); 
        
        // --- Campo RGM ---
        JLabel lblRgmNotas = new JLabel("RGM:");
        lblRgmNotas.setBounds(20, 30, 80, 25);
        painel.add(lblRgmNotas);
        
        txtRgmNotas = new JTextField();
        txtRgmNotas.setBounds(100, 30, 100, 25);
        painel.add(txtRgmNotas);
        
        // --- "Display" Nome do Aluno ---
        Border bordaDisplay = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        
        lblNomeAlunoNotas = new JLabel(" [Digite o RGM e aperte Enter]");
        lblNomeAlunoNotas.setBounds(210, 30, 430, 25);
        lblNomeAlunoNotas.setBorder(bordaDisplay);
        painel.add(lblNomeAlunoNotas);
        
        // --- "Display" Curso do Aluno ---
        lblCursoAlunoNotas = new JLabel(" [Aguardando aluno...]");
        lblCursoAlunoNotas.setBounds(100, 70, 540, 25);
        lblCursoAlunoNotas.setBorder(bordaDisplay);
        painel.add(lblCursoAlunoNotas);
        
        // (Resto do painel... disciplinas, semestre, nota, faltas...)
        // --- Campo Disciplina ---
        JLabel lblDisciplina = new JLabel("Disciplina:");
        lblDisciplina.setBounds(20, 110, 80, 25);
        painel.add(lblDisciplina);
        
        String[] disciplinas = {
            "Programação Orientada a Objetos",
            "Banco de Dados",
            "Engenharia de Software III",
            "Programação Web",
            "Sistemas Operacionais"
        };
        comboDisciplina = new JComboBox<>(disciplinas);
        comboDisciplina.setBounds(100, 110, 300, 25);
        painel.add(comboDisciplina);
        
        // --- Campo Semestre ---
        JLabel lblSemestre = new JLabel("Semestre:");
        lblSemestre.setBounds(20, 150, 80, 25);
        painel.add(lblSemestre);
        
        String[] semestres = { "2020-1", "2020-2", "2021-1", "2021-2", "2022-1" };
        comboSemestre = new JComboBox<>(semestres);
        comboSemestre.setBounds(100, 150, 100, 25);
        painel.add(comboSemestre);
        
        // --- Campo Nota ---
        JLabel lblNota = new JLabel("Nota:");
        lblNota.setBounds(220, 150, 40, 25);
        painel.add(lblNota);
        
        SpinnerNumberModel modelNota = new SpinnerNumberModel(5.0, 0.0, 10.0, 0.5);
        spinnerNota = new JSpinner(modelNota);
        spinnerNota.setBounds(260, 150, 60, 25);
        painel.add(spinnerNota);
        
        // --- Campo Faltas ---
        JLabel lblFaltas = new JLabel("Faltas:");
        lblFaltas.setBounds(340, 150, 50, 25);
        painel.add(lblFaltas);
        
        SpinnerNumberModel modelFaltas = new SpinnerNumberModel(0, 0, 100, 1);
        spinnerFaltas = new JSpinner(modelFaltas);
        spinnerFaltas.setBounds(390, 150, 60, 25);
        painel.add(spinnerFaltas);
        
        return painel;
    }

    /**
     * Cria o painel da aba Boletim
     */
    private JPanel criarPainelBoletim() {
        // (Este método permanece o mesmo)
        JPanel painel = new JPanel();
        painel.setLayout(null); 
        
        Border bordaDisplay = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        
        // (Rótulos e Displays para RGM, Nome, Curso...)
        // --- Rótulos (fixos) ---
        JLabel lblRgmLabel = new JLabel("RGM:");
        lblRgmLabel.setBounds(20, 30, 80, 25);
        painel.add(lblRgmLabel);
        
        JLabel lblNomeLabel = new JLabel("Nome:");
        lblNomeLabel.setBounds(20, 70, 80, 25);
        painel.add(lblNomeLabel);
        
        JLabel lblCursoLabel = new JLabel("Curso:");
        lblCursoLabel.setBounds(20, 110, 80, 25);
        painel.add(lblCursoLabel);

        // --- Displays (onde os dados vão aparecer) ---
        lblBoletimRgm = new JLabel();
        lblBoletimRgm.setBounds(100, 30, 150, 25);
        lblBoletimRgm.setBorder(bordaDisplay);
        painel.add(lblBoletimRgm);
        
        lblBoletimNome = new JLabel();
        lblBoletimNome.setBounds(100, 70, 540, 25);
        lblBoletimNome.setBorder(bordaDisplay);
        painel.add(lblBoletimNome);
        
        lblBoletimCurso = new JLabel();
        lblBoletimCurso.setBounds(100, 110, 540, 25);
        lblBoletimCurso.setBorder(bordaDisplay);
        painel.add(lblBoletimCurso);
        
        // --- Tabela de Notas ---
        
        // 1. Define as colunas
        String[] colunas = { "ID", "Semestre", "Disciplina", "Nota", "Faltas" };
        
        // 2. Cria o modelo da tabela
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // 3. Cria a tabela
        tabelaNotas = new JTable(tableModel);
        
        // 4. Configuração da Tabela
        tabelaNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 5. Coloca a tabela dentro de um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabelaNotas);
        scrollPane.setBounds(20, 150, 620, 250); 
        painel.add(scrollPane);
        
        return painel;
    }
    
    // -----------------------------------------------------------------
    // --- MÉTODOS DE INTEGRAÇÃO ---
    // -----------------------------------------------------------------

    /**
     * Adiciona os listeners (ouvintes de ação) aos componentes
     * (MÉTODO ATUALIZADO)
     */
    private void adicionarListeners() {
        
        // --- Menu Aluno ---
        itemSalvarAluno.addActionListener(e -> salvarAluno());
        itemExcluirAluno.addActionListener(e -> excluirAluno());
        itemConsultarAluno.addActionListener(e -> consultarAluno());
        itemAlterarAluno.addActionListener(e -> alterarAluno());
        
        itemSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pergunta ao usuário se ele realmente quer sair
                int resposta = JOptionPane.showConfirmDialog(
                    TelaPrincipal.this, // "Pai" da janela
                    "Tem certeza que deseja fechar o sistema?",
                    "Confirmar Saída",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                // Se o usuário clicou "YES" (Sim)
                if (resposta == JOptionPane.YES_OPTION) {
                    System.exit(0); // Fecha a aplicação
                }
            }
        });
        
        // --- Aba 3 (Notas e Faltas) ---
        txtRgmNotas.addActionListener(e -> buscarAlunoParaNotas());
        
        // --- Menu Notas e Faltas ---
        itemSalvarNotas.addActionListener(e -> salvarNotaFalta());
        itemConsultarNotas.addActionListener(e -> consultarBoletim());
        itemExcluirNotas.addActionListener(e -> excluirNotaFalta());
        itemAlterarNotas.addActionListener(e -> alterarNotaFalta());

        // --- Menu Ajuda (ALTERADO) ---
        itemSobre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ***** ALTERAÇÃO AQUI *****
                // Mostra uma janela de informação simples
                JOptionPane.showMessageDialog(
                    TelaPrincipal.this, 
                    "Sistema de Cadastro de Aluno\n\n" +
                    "Desenvolvido por Gustavo Alves como projeto de POO na Fatec Guarulhos.\n" +
                    "Tecnologias: Java Swing e MySQL.",
                    "Sobre o Sistema", // Título da Janela
                    JOptionPane.INFORMATION_MESSAGE // Ícone de informação
                );
                // ***** FIM DA ALTERAÇÃO *****
            }
        });
        
        // --- Listener de Seleção da Tabela ---
        tabelaNotas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int linhaSelecionada = tabelaNotas.getSelectedRow();
                    
                    if (linhaSelecionada != -1) {
                        // --- LÓGICA DE ALTERAÇÃO (Início) ---
                        // 1. Pega os dados da linha selecionada
                        idNotaSelecionada = (Integer) tableModel.getValueAt(linhaSelecionada, 0);
                        String semestre = (String) tableModel.getValueAt(linhaSelecionada, 1);
                        String disciplina = (String) tableModel.getValueAt(linhaSelecionada, 2);
                        Double nota = (Double) tableModel.getValueAt(linhaSelecionada, 3);
                        Integer faltas = (Integer) tableModel.getValueAt(linhaSelecionada, 4);
                        
                        // 2. Preenche a aba "Notas e Faltas" com esses dados
                        comboSemestre.setSelectedItem(semestre);
                        comboDisciplina.setSelectedItem(disciplina);
                        spinnerNota.setValue(nota);
                        spinnerFaltas.setValue(faltas);
                        
                        // 3. Preenche também os dados do aluno (RGM, Nome, Curso)
                        //    na aba "Notas e Faltas"
                        txtRgmNotas.setText(lblBoletimRgm.getText());
                        lblNomeAlunoNotas.setText(lblBoletimNome.getText());
                        lblCursoAlunoNotas.setText(lblBoletimCurso.getText());
                        rgmAlunoAtual = lblBoletimRgm.getText();
                        
                        // 4. Muda para a aba de edição
                        abas.setSelectedComponent(painelNotasFaltas);
                        
                    } else {
                        idNotaSelecionada = -1;
                    }
                }
            }
        });
    }
    
    // --- Métodos do CRUD Aluno ---
    
    /**
     * Método para salvar os dados do Aluno
     */
    private void salvarAluno() {
        // (Este método permanece o mesmo)
        if (txtRgm.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo RGM não pode estar vazio.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            abas.setSelectedComponent(painelDadosPessoais);
            txtRgm.requestFocus(); 
            return;
        }

        Aluno aluno = new Aluno();
        try {
            coletarDadosAluno(aluno); 
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            abas.setSelectedComponent(painelDadosPessoais);
            txtDataNasc.requestFocus();
            return;
        }

        try {
            AlunoDAO dao = new AlunoDAO();
            dao.salvar(aluno); 
            JOptionPane.showMessageDialog(this, "Aluno salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar aluno no banco de dados:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); 
        }
    }
    
    /**
     * Método para excluir um Aluno
     */
    private void excluirAluno() {
        // (Este método permanece o mesmo)
        String rgm = JOptionPane.showInputDialog(this, "Digite o RGM do aluno que deseja excluir:", "Excluir Aluno", JOptionPane.QUESTION_MESSAGE);
        
        if (rgm == null || rgm.trim().isEmpty()) {
            return; 
        }
        
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o aluno com RGM " + rgm + "?\n" +
            "ATENÇÃO: Todas as notas e faltas deste aluno também serão excluídas.", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            AlunoDAO dao = new AlunoDAO();
            dao.excluir(rgm.trim());
            JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir aluno:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Método para consultar um Aluno
     */
    private void consultarAluno() {
        // (Este método permanece o mesmo)
        String rgm = JOptionPane.showInputDialog(this, "Digite o RGM do aluno que deseja consultar:", "Consultar Aluno", JOptionPane.QUESTION_MESSAGE);

        if (rgm == null || rgm.trim().isEmpty()) {
            return; 
        }

        try {
            AlunoDAO dao = new AlunoDAO();
            Aluno aluno = dao.consultar(rgm.trim()); 

            if (aluno != null) {
                preencherCampos(aluno);
                JOptionPane.showMessageDialog(this, "Aluno encontrado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                abas.setSelectedComponent(painelDadosPessoais); 
            } else {
                JOptionPane.showMessageDialog(this, "Aluno com RGM " + rgm + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao consultar aluno:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Método para alterar os dados do Aluno
     */
    private void alterarAluno() {
        // (Este método permanece o mesmo)
        if (txtRgm.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "RGM não preenchido. Consulte um aluno antes de alterar.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            abas.setSelectedComponent(painelDadosPessoais);
            return;
        }
        
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja salvar as alterações para o aluno RGM " + txtRgm.getText() + "?", "Confirmação de Alteração", JOptionPane.YES_NO_OPTION);
        
        if (resposta != JOptionPane.YES_OPTION) {
            return; 
        }

        Aluno aluno = new Aluno();
        try {
            coletarDadosAluno(aluno); 
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            abas.setSelectedComponent(painelDadosPessoais);
            txtDataNasc.requestFocus();
            return;
        }
        
        try {
            AlunoDAO dao = new AlunoDAO();
            dao.alterar(aluno); 
            JOptionPane.showMessageDialog(this, "Aluno alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar aluno:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    
    // --- Métodos do CRUD Notas e Faltas ---
    
    /**
     * Busca os dados de um aluno (Nome e Curso) na aba "Notas e Faltas".
     */
    private void buscarAlunoParaNotas() {
        // (Este método permanece o mesmo)
        String rgm = txtRgmNotas.getText().trim();
        if (rgm.isEmpty()) {
            lblNomeAlunoNotas.setText(" [Digite um RGM e aperte Enter]");
            lblCursoAlunoNotas.setText(" [Aguardando aluno...]");
            this.rgmAlunoAtual = null; 
            return;
        }

        try {
            AlunoDAO dao = new AlunoDAO();
            Aluno aluno = dao.consultar(rgm);

            if (aluno != null) {
                lblNomeAlunoNotas.setText(aluno.getNome());
                lblCursoAlunoNotas.setText(aluno.getCurso());
                this.rgmAlunoAtual = aluno.getRgm(); 
                comboDisciplina.requestFocus();
            } else {
                lblNomeAlunoNotas.setText(" [ALUNO NÃO ENCONTRADO]");
                lblCursoAlunoNotas.setText(" [Verifique o RGM]");
                this.rgmAlunoAtual = null; 
                JOptionPane.showMessageDialog(this, "Aluno com RGM " + rgm + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar aluno:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Salva uma nova entrada de nota/falta para o aluno carregado.
     */
    private void salvarNotaFalta() {
        // (Este método permanece o mesmo)
        if (this.rgmAlunoAtual == null) {
            JOptionPane.showMessageDialog(this, "Nenhum aluno carregado. Busque um aluno pelo RGM primeiro.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            abas.setSelectedComponent(painelNotasFaltas);
            txtRgmNotas.requestFocus();
            return;
        }
        
        NotasFaltas nota = new NotasFaltas();
        nota.setSemestre((String) comboSemestre.getSelectedItem());
        nota.setDisciplina((String) comboDisciplina.getSelectedItem());
        nota.setNota((Double) spinnerNota.getValue());
        nota.setFaltas((Integer) spinnerFaltas.getValue());
        nota.setRgmAluno(this.rgmAlunoAtual); 
        
        try {
            NotasFaltasDAO dao = new NotasFaltasDAO();
            dao.salvar(nota); 
            JOptionPane.showMessageDialog(this, "Nota/Falta salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar nota/falta:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Consulta o boletim (pede o RGM).
     */
    private void consultarBoletim() {
        // (Este método permanece o mesmo)
        String rgm = JOptionPane.showInputDialog(this, "Digite o RGM do aluno para gerar o boletim:", "Consultar Boletim", JOptionPane.QUESTION_MESSAGE);

        if (rgm == null || rgm.trim().isEmpty()) {
            return; 
        }
        
        consultarBoletimPeloRgm(rgm.trim()); 
    }
    
    /**
     * Busca os dados e preenche o boletim para um RGM específico.
     */
    private void consultarBoletimPeloRgm(String rgm) {
        // (Este método permanece o mesmo)
        lblBoletimRgm.setText("");
        lblBoletimNome.setText("");
        lblBoletimCurso.setText("");
        tableModel.setRowCount(0); 
        idNotaSelecionada = -1; 

        try {
            AlunoDAO alunoDAO = new AlunoDAO();
            Aluno aluno = alunoDAO.consultar(rgm);

            if (aluno == null) {
                JOptionPane.showMessageDialog(this, "Aluno com RGM " + rgm + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            lblBoletimRgm.setText(aluno.getRgm());
            lblBoletimNome.setText(aluno.getNome());
            lblBoletimCurso.setText(aluno.getCurso());
            
            NotasFaltasDAO notasDAO = new NotasFaltasDAO();
            List<NotasFaltas> listaDeNotas = notasDAO.consultarPorAluno(rgm);
            
            if (!listaDeNotas.isEmpty()) {
                for (NotasFaltas nota : listaDeNotas) {
                    Object[] linha = {
                        nota.getId(),
                        nota.getSemestre(),
                        nota.getDisciplina(),
                        nota.getNota(),
                        nota.getFaltas()
                    };
                    tableModel.addRow(linha);
                }
            }
            
            abas.setSelectedComponent(painelBoletim);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar boletim:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Exclui a nota/falta selecionada na tabela do boletim.
     */
    private void excluirNotaFalta() {
        // (Este método permanece o mesmo)
        if (abas.getSelectedComponent() != painelBoletim) {
            JOptionPane.showMessageDialog(this, "Vá para a aba 'Boletim' para selecionar uma nota.", "Ação Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (idNotaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Nenhuma nota selecionada. Clique em uma linha na tabela.", "Ação Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir a nota selecionada (ID: " + idNotaSelecionada + ")?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (resposta != JOptionPane.YES_OPTION) {
            return; 
        }

        try {
            NotasFaltasDAO dao = new NotasFaltasDAO();
            dao.excluir(idNotaSelecionada); 
            
            JOptionPane.showMessageDialog(this, "Nota/Falta excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            int linhaParaRemover = tabelaNotas.getSelectedRow();
            tableModel.removeRow(linhaParaRemover);
            idNotaSelecionada = -1; 
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir nota/falta:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Altera a nota/falta selecionada com os dados da aba "Notas e Faltas".
     */
    private void alterarNotaFalta() {
        // (Este método permanece o mesmo)
        if (idNotaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma nota na aba 'Boletim' primeiro.", "Ação Inválida", JOptionPane.WARNING_MESSAGE);
            abas.setSelectedComponent(painelBoletim);
            return;
        }
        
        if (abas.getSelectedComponent() != painelNotasFaltas) {
             JOptionPane.showMessageDialog(this, "Clique na nota no Boletim para editá-la na aba 'Notas e Faltas'.", "Ação Inválida", JOptionPane.WARNING_MESSAGE);
             abas.setSelectedComponent(painelBoletim);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja salvar as alterações para a nota ID " + idNotaSelecionada + "?", "Confirmação de Alteração", JOptionPane.YES_NO_OPTION);
        
        if (resposta != JOptionPane.YES_OPTION) {
            return; 
        }

        NotasFaltas nota = new NotasFaltas();
        nota.setId(idNotaSelecionada); 
        
        nota.setSemestre((String) comboSemestre.getSelectedItem());
        nota.setDisciplina((String) comboDisciplina.getSelectedItem());
        nota.setNota((Double) spinnerNota.getValue());
        nota.setFaltas((Integer) spinnerFaltas.getValue());
        
        try {
            NotasFaltasDAO dao = new NotasFaltasDAO();
            dao.alterar(nota); 
            
            JOptionPane.showMessageDialog(this, "Nota alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            String rgm = lblBoletimRgm.getText(); 
            if (rgm != null && !rgm.isEmpty()) {
                consultarBoletimPeloRgm(rgm); 
            }
            abas.setSelectedComponent(painelBoletim); 
            idNotaSelecionada = -1; 
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar nota:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    
    // --- Métodos Auxiliares ---

    /**
     * Coleta os dados das abas 1 e 2 e preenche um objeto Aluno.
     */
    private void coletarDadosAluno(Aluno aluno) throws ParseException {
        // (Este método permanece o mesmo)
        aluno.setRgm(txtRgm.getText().trim()); 
        aluno.setNome(txtNome.getText());
        aluno.setCpf(txtCpf.getText());
        aluno.setEmail(txtEmail.getText());
        aluno.setEndereco(txtEndereco.getText());
        aluno.setMunicipio(txtMunicipio.getText());
        aluno.setUf(txtUf.getText());
        aluno.setCelular(txtCelular.getText());

        String dataString = txtDataNasc.getText();
        if (dataString == null || dataString.contains("_")) {
            aluno.setDataNascimento(null);
        } else {
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dataUtil = formatador.parse(dataString);
            java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());
            aluno.setDataNascimento(dataSql);
        }

        aluno.setCurso((String) comboCurso.getSelectedItem());
        aluno.setCampus((String) comboCampus.getSelectedItem());
        aluno.setPeriodo(getPeriodoSelecionado());
    }

    /**
     * Preenche os campos da tela com dados de um objeto Aluno.
     */
    private void preencherCampos(Aluno aluno) {
        // (Este método permanece o mesmo)
        // Aba 1
        txtRgm.setText(aluno.getRgm());
        txtNome.setText(aluno.getNome());
        txtCpf.setText(aluno.getCpf());
        txtEmail.setText(aluno.getEmail());
        txtEndereco.setText(aluno.getEndereco());
        txtMunicipio.setText(aluno.getMunicipio());
        txtUf.setText(aluno.getUf());
        txtCelular.setText(aluno.getCelular());

        if (aluno.getDataNascimento() != null) {
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
            String dataFormatada = formatador.format(aluno.getDataNascimento());
            txtDataNasc.setText(dataFormatada);
        } else {
            txtDataNasc.setText(""); 
        }

        // Aba 2
        comboCurso.setSelectedItem(aluno.getCurso());
        comboCampus.setSelectedItem(aluno.getCampus());
        setPeriodoSelecionado(aluno.getPeriodo());
        
        // Aba 3
        txtRgmNotas.setText(aluno.getRgm());
        lblNomeAlunoNotas.setText(aluno.getNome());
        lblCursoAlunoNotas.setText(aluno.getCurso());
        this.rgmAlunoAtual = aluno.getRgm();
    }

    /**
     * Seleciona o JRadioButton com base no texto do período.
     */
    private void setPeriodoSelecionado(String periodo) {
        // (Este método permanece o mesmo)
        if (periodo == null) {
            grupoPeriodo.clearSelection(); 
        } else if (periodo.equals("Matutino")) {
            radioMatutino.setSelected(true);
        } else if (periodo.equals("Vespertino")) {
            radioVespertino.setSelected(true);
        } else if (periodo.equals("Noturno")) {
            radioNoturno.setSelected(true);
        } else {
            grupoPeriodo.clearSelection();
        }
    }
    
    /**
     * Método auxiliar para pegar o período (permanece o mesmo)
     */
    private String getPeriodoSelecionado() {
        if (radioMatutino.isSelected()) return "Matutino";
        if (radioVespertino.isSelected()) return "Vespertino";
        if (radioNoturno.isSelected()) return "Noturno";
        return null;
    }
    
    /**
     * Método principal (main)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }
}