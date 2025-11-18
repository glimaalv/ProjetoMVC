package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
// (Todos os seus imports javax.swing, java.awt, etc. devem estar aqui)
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

// Imports do PDFBox
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

// Nossas classes de back-end
import dao.AlunoDAO;
import dao.CampusDAO;
import dao.CursoDAO;
import dao.DisciplinaDAO;
import dao.NotasFaltasDAO;
import model.Aluno;
import model.NotasFaltas;
import dao.DisciplinaDTO;


public class TelaPrincipal extends JFrame {
    
    private static final long serialVersionUID = 1L; // <-- ID de Versão Adicionado

    // --- Atributos da Tela ---
    private JTabbedPane abas;
    
    // --- Nossos 5 Painéis Personalizados ---
    private PainelDadosPessoais painelDadosPessoais;
    private PainelLancarNotas painelLancarNotas;
    private PainelBoletim painelBoletim;
    private PainelCadastroSistema painelCadastroSistema;
    private PainelConsultaAlunos painelConsultaAlunos; 
    
    // --- DAOs (Acesso ao Banco) ---
    private AlunoDAO alunoDAO;
    private NotasFaltasDAO notasFaltasDAO;
    private CursoDAO cursoDAO;
    private CampusDAO campusDAO;
    private DisciplinaDAO disciplinaDAO;

    // --- Atributos dos Itens de Menu (Restantes) ---
    private JMenuItem itemSair;
    private JMenuItem itemSobre;
    
    // --- Variáveis de Estado ---
    private String rgmAlunoAtual; 
    private int idNotaSelecionada = -1;
    

    /**
     * Construtor da tela.
     */
    public TelaPrincipal() {
        // --- 1. Inicializa os DAOs ---
        alunoDAO = new AlunoDAO();
        notasFaltasDAO = new NotasFaltasDAO();
        cursoDAO = new CursoDAO();
        campusDAO = new CampusDAO();
        disciplinaDAO = new DisciplinaDAO();
        
        // --- 2. Configurações básicas da janela ---
        setTitle("Projeto Cadastro de Aluno");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // --- 3. Adiciona a Barra de Menu ---
        setJMenuBar(criarBarraDeMenu()); 

        // --- 4. Cria o componente de Abas ---
        abas = new JTabbedPane();

        // --- 5. Cria os painéis (agora instanciando nossas classes) ---
        painelDadosPessoais = new PainelDadosPessoais(); 
        painelLancarNotas = new PainelLancarNotas(); 
        painelBoletim = new PainelBoletim(); 
        painelCadastroSistema = new PainelCadastroSistema(); 
        painelConsultaAlunos = new PainelConsultaAlunos(); 
        
        // --- 6. Adiciona os painéis às abas ---
        abas.addTab("Dados Pessoais e Curso", painelDadosPessoais); 
        abas.addTab("Lançar Notas/Faltas", painelLancarNotas);
        abas.addTab("Boletim", painelBoletim);
        abas.addTab("Cadastro Sistema", painelCadastroSistema);
        abas.addTab("Consulta Alunos", painelConsultaAlunos); 
        
        // --- 7. Adiciona as abas na janela ---
        add(abas, BorderLayout.CENTER);
        
        // --- 8. Carrega dados dinâmicos ---
        carregarComboBoxesEListas(); 
        
        // --- 9. Registra os "ouvintes" de eventos (cliques) ---
        adicionarListeners();
    }
    
    /**
     * Cria e retorna a barra de menu principal.
     */
    private JMenuBar criarBarraDeMenu() {
        // (Este método permanece o mesmo)
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuSair = new JMenu("Sair"); 
        menuBar.add(menuSair);
        
        itemSair = new JMenuItem("Sair do Sistema"); 
        itemSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_DOWN_MASK));
        menuSair.add(itemSair); 

        JMenu menuAjuda = new JMenu("Ajuda");
        menuBar.add(menuAjuda);
        
        itemSobre = new JMenuItem("Sobre");
        menuAjuda.add(itemSobre);
        
        return menuBar;
    }

    
    // -----------------------------------------------------------------
    // --- MÉTODOS DE INTEGRAÇÃO ---
    // -----------------------------------------------------------------

    /**
     * Adiciona os listeners (ouvintes de ação) aos componentes
     */
    private void adicionarListeners() {
        
        // --- Aba 1 (Botões Aluno) ---
        painelDadosPessoais.getBtnSalvarAluno().addActionListener(e -> salvarAluno());
        painelDadosPessoais.getBtnExcluirAluno().addActionListener(e -> excluirAluno());
        painelDadosPessoais.getBtnConsultarAluno().addActionListener(e -> consultarAluno());
        painelDadosPessoais.getBtnAlterarAluno().addActionListener(e -> alterarAluno());
        painelDadosPessoais.getBtnLimparCampos().addActionListener(e -> limparCamposAluno());
        
        // --- Menu Sair ---
        itemSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int resposta = JOptionPane.showConfirmDialog(
                    TelaPrincipal.this, 
                    "Tem certeza que deseja fechar o sistema?",
                    "Confirmar Saída",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (resposta == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        
        // --- Botão PDF ---
        painelBoletim.getBtnGerarPDF().addActionListener(e -> gerarBoletimPDF());
        
        // --- Aba 2 (Lançar Notas) ---
        painelLancarNotas.getTxtRgmNotas().addActionListener(e -> buscarAlunoParaNotas());
        painelLancarNotas.getBtnSalvarNota().addActionListener(e -> salvarNotaFalta());
        
        // --- Aba 3 (Boletim) ---
        painelBoletim.getBtnConsultarBoletim().addActionListener(e -> consultarBoletim());
        painelBoletim.getBtnExcluirNota().addActionListener(e -> excluirNotaFalta());
        painelBoletim.getBtnAlterarNota().addActionListener(e -> alterarNotaFalta());

        // --- Menu Ajuda ---
        itemSobre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    TelaPrincipal.this, 
                    "Sistema de Cadastro de Aluno\n\n" +
                    "Desenvolvido por Gustavo Alves como projeto de POO na Fatec Guarulhos.\n" +
                    "Tecnologias: Java Swing e SQLite.", // Atualizei para SQLite
                    "Sobre o Sistema", 
                    JOptionPane.INFORMATION_MESSAGE 
                );
            }
        });
        
        // --- Listener de Seleção da Tabela (Aba Boletim) ---
        painelBoletim.getTabelaNotas().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                // (O erro de digitação foi corrigido aqui)
                if (!event.getValueIsAdjusting()) { 
                    int linhaSelecionada = painelBoletim.getTabelaNotas().getSelectedRow();
                    
                    if (linhaSelecionada != -1) {
                        DefaultTableModel tableModel = painelBoletim.getTableModel();
                        
                        idNotaSelecionada = (Integer) tableModel.getValueAt(linhaSelecionada, 0);
                        String semestre = (String) tableModel.getValueAt(linhaSelecionada, 1);
                        String disciplina = (String) tableModel.getValueAt(linhaSelecionada, 2);
                        Double nota = (Double) tableModel.getValueAt(linhaSelecionada, 3);
                        Integer faltas = (Integer) tableModel.getValueAt(linhaSelecionada, 4);
                        
                        // Preenche a aba "Lançar Notas"
                        painelLancarNotas.getComboSemestre().setSelectedItem(semestre);
                        
                        painelLancarNotas.getSpinnerNota().setValue(nota);
                        painelLancarNotas.getSpinnerFaltas().setValue(faltas);
                        
                        // Chama o buscarAluno para carregar o aluno e
                        // (principalmente) carregar as disciplinas corretas no combo
                        painelLancarNotas.getTxtRgmNotas().setText(painelBoletim.getLblBoletimRgm().getText());
                        buscarAlunoParaNotas(); // Isso vai carregar o combo de disciplinas
                        
                        // Agora que o combo está carregado, nós selecionamos a disciplina
                        painelLancarNotas.getComboDisciplina().setSelectedItem(disciplina);
                        
                        abas.setSelectedComponent(painelLancarNotas);
                        
                    } else {
                        idNotaSelecionada = -1;
                    }
                }
            }
        });
        
        // --- Listeners da Aba "Cadastro Sistema" ---
        painelCadastroSistema.getBtnSalvarCurso().addActionListener(e -> salvarNovoItem("curso"));
        painelCadastroSistema.getBtnExcluirCurso().addActionListener(e -> excluirItemSelecionado("curso"));
        painelCadastroSistema.getBtnSalvarCampus().addActionListener(e -> salvarNovoItem("campus"));
        painelCadastroSistema.getBtnExcluirCampus().addActionListener(e -> excluirItemSelecionado("campus"));
        painelCadastroSistema.getBtnSalvarDisciplina().addActionListener(e -> salvarNovoItem("disciplina"));
        painelCadastroSistema.getBtnExcluirDisciplina().addActionListener(e -> excluirItemSelecionado("disciplina"));
        
        // --- Listener da Aba "Consulta Alunos" ---
        painelConsultaAlunos.getBtnBuscar().addActionListener(e -> consultarAlunosPorFiltro());
    }
    
    // --- Métodos do CRUD Aluno ---
    
    private void salvarAluno() {
        if (painelDadosPessoais.getTxtRgm().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo RGM não pode estar vazio.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            abas.setSelectedComponent(painelDadosPessoais);
            painelDadosPessoais.getTxtRgm().requestFocus(); 
            return;
        }

        Aluno aluno = new Aluno();
        try {
            coletarDadosAluno(aluno); 
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            abas.setSelectedComponent(painelDadosPessoais);
            painelDadosPessoais.getTxtDataNasc().requestFocus();
            return;
        }

        try {
            alunoDAO.salvar(aluno); 
            JOptionPane.showMessageDialog(this, "Aluno salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCamposAluno();
            
        } catch (Exception e) {
            String mensagemErro = e.getMessage();
            
            if (mensagemErro != null && mensagemErro.contains("SQLITE_CONSTRAINT_PRIMARYKEY")) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao Salvar: O RGM '" + aluno.getRgm() + "' já está cadastrado no sistema.", 
                    "RGM Duplicado", 
                    JOptionPane.ERROR_MESSAGE);
                
            } else if (mensagemErro != null && mensagemErro.contains("SQLITE_CONSTRAINT_UNIQUE")) {
                 JOptionPane.showMessageDialog(this, 
                    "Erro ao Salvar: O CPF '" + aluno.getCpf() + "' já está cadastrado para outro aluno.", 
                    "CPF Duplicado", 
                    JOptionPane.ERROR_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao salvar aluno no banco de dados:\n" + mensagemErro, 
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace(); 
        }
    }
    
    private void excluirAluno() {
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
            alunoDAO.excluir(rgm.trim());
            JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            if (painelDadosPessoais.getTxtRgm().getText().equals(rgm.trim())) {
                limparCamposAluno();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir aluno:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void consultarAluno() {
        String rgm = JOptionPane.showInputDialog(this, "Digite o RGM do aluno que deseja consultar:", "Consultar Aluno", JOptionPane.QUESTION_MESSAGE);

        if (rgm == null || rgm.trim().isEmpty()) {
            return; 
        }

        try {
            Aluno aluno = alunoDAO.consultar(rgm.trim()); 

            if (aluno != null) {
                preencherCampos(aluno);
                JOptionPane.showMessageDialog(this, "Aluno encontrado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                abas.setSelectedComponent(painelDadosPessoais); 
            } else {
                JOptionPane.showMessageDialog(this, "Aluno com RGM " + rgm + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                limparCamposAluno(); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao consultar aluno:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void alterarAluno() {
        if (painelDadosPessoais.getTxtRgm().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "RGM não preenchido. Consulte um aluno antes de alterar.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            abas.setSelectedComponent(painelDadosPessoais);
            return;
        }
        
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja salvar as alterações para o aluno RGM " + painelDadosPessoais.getTxtRgm().getText() + "?", "Confirmação de Alteração", JOptionPane.YES_NO_OPTION);
        
        if (resposta != JOptionPane.YES_OPTION) {
            return; 
        }

        Aluno aluno = new Aluno();
        try {
            coletarDadosAluno(aluno); 
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            abas.setSelectedComponent(painelDadosPessoais);
            painelDadosPessoais.getTxtDataNasc().requestFocus();
            return;
        }
        
        try {
            alunoDAO.alterar(aluno); 
            JOptionPane.showMessageDialog(this, "Aluno alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            String mensagemErro = e.getMessage();
            if (mensagemErro != null && mensagemErro.contains("SQLITE_CONSTRAINT_UNIQUE")) {
                 JOptionPane.showMessageDialog(this, 
                    "Erro ao Alterar: O CPF '" + aluno.getCpf() + "' já está cadastrado para outro aluno.", 
                    "CPF Duplicado", 
                    JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar aluno:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }
    
    
    // --- Métodos do CRUD Notas e Faltas ---
    
    /**
     * (MÉTODO ATUALIZADO)
     * Busca os dados de um aluno E FILTRA as disciplinas dele.
     */
    private void buscarAlunoParaNotas() {
        String rgm = painelLancarNotas.getTxtRgmNotas().getText().trim();
        if (rgm.isEmpty()) {
            painelLancarNotas.getLblNomeAlunoNotas().setText(" [Digite um RGM e aperte Enter]");
            painelLancarNotas.getLblCursoAlunoNotas().setText(" [Aguardando aluno...]");
            this.rgmAlunoAtual = null; 
            painelLancarNotas.getComboDisciplina().removeAllItems(); // Limpa o combo
            return;
        }

        try {
            Aluno aluno = alunoDAO.consultar(rgm);

            if (aluno != null) {
                // 1. Preenche os dados do aluno
                painelLancarNotas.getLblNomeAlunoNotas().setText(aluno.getNome());
                painelLancarNotas.getLblCursoAlunoNotas().setText(aluno.getCurso());
                this.rgmAlunoAtual = aluno.getRgm(); 
                
                // --- 2. (NOVO) Carrega as disciplinas SÓ DESSE CURSO ---
                JComboBox<String> comboDisciplina = painelLancarNotas.getComboDisciplina();
                comboDisciplina.removeAllItems(); // Limpa as disciplinas antigas
                
                if (aluno.getCurso() != null) {
                    List<String> disciplinasDoCurso = disciplinaDAO.consultarPorCurso(aluno.getCurso());
                    if (disciplinasDoCurso.isEmpty()) {
                        comboDisciplina.addItem("NENHUMA DISCIPLINA CADASTRADA PARA ESTE CURSO");
                    } else {
                        for (String d : disciplinasDoCurso) {
                            comboDisciplina.addItem(d);
                        }
                    }
                } else {
                    comboDisciplina.addItem("ALUNO SEM CURSO DEFINIDO");
                }
                // --- FIM DA NOVA LÓGICA ---
                
                painelLancarNotas.getComboDisciplina().requestFocus();
                
            } else {
                painelLancarNotas.getLblNomeAlunoNotas().setText(" [ALUNO NÃO ENCONTRADO]");
                painelLancarNotas.getLblCursoAlunoNotas().setText(" [Verifique o RGM]");
                this.rgmAlunoAtual = null; 
                painelLancarNotas.getComboDisciplina().removeAllItems(); // Limpa o combo
                JOptionPane.showMessageDialog(this, "Aluno com RGM " + rgm + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar aluno:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void salvarNotaFalta() {
        if (this.rgmAlunoAtual == null) {
            JOptionPane.showMessageDialog(this, "Nenhum aluno carregado. Busque um aluno pelo RGM primeiro.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            abas.setSelectedComponent(painelLancarNotas);
            painelLancarNotas.getTxtRgmNotas().requestFocus();
            return;
        }
        
        // Validação da disciplina
        if (painelLancarNotas.getComboDisciplina().getSelectedItem() == null || 
            painelLancarNotas.getComboDisciplina().getItemCount() == 0 ||
            ((String)painelLancarNotas.getComboDisciplina().getSelectedItem()).contains("NENHUMA") ||
            ((String)painelLancarNotas.getComboDisciplina().getSelectedItem()).contains("ALUNO SEM CURSO")) {
            
            JOptionPane.showMessageDialog(this, "Nenhuma disciplina válida selecionada.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        NotasFaltas nota = new NotasFaltas();
        nota.setSemestre((String) painelLancarNotas.getComboSemestre().getSelectedItem());
        nota.setDisciplina((String) painelLancarNotas.getComboDisciplina().getSelectedItem());
        nota.setNota((Double) painelLancarNotas.getSpinnerNota().getValue());
        nota.setFaltas((Integer) painelLancarNotas.getSpinnerFaltas().getValue());
        nota.setRgmAluno(this.rgmAlunoAtual); 
        
        try {
            notasFaltasDAO.salvar(nota); 
            JOptionPane.showMessageDialog(this, "Nota/Falta salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            painelLancarNotas.getComboSemestre().setSelectedIndex(0);
            painelLancarNotas.getComboDisciplina().setSelectedIndex(0);
            painelLancarNotas.getSpinnerNota().setValue(5.0);
            painelLancarNotas.getSpinnerFaltas().setValue(0);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar nota/falta:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void consultarBoletim() {
        String rgm = JOptionPane.showInputDialog(this, "Digite o RGM do aluno para gerar o boletim:", "Consultar Boletim", JOptionPane.QUESTION_MESSAGE);

        if (rgm == null || rgm.trim().isEmpty()) {
            return; 
        }
        
        consultarBoletimPeloRgm(rgm.trim()); 
    }
    
    private void consultarBoletimPeloRgm(String rgm) {
        painelBoletim.getLblBoletimRgm().setText("");
        painelBoletim.getLblBoletimNome().setText("");
        painelBoletim.getLblBoletimCurso().setText("");
        painelBoletim.getTableModel().setRowCount(0); 
        idNotaSelecionada = -1; 

        try {
            Aluno aluno = alunoDAO.consultar(rgm);

            if (aluno == null) {
                JOptionPane.showMessageDialog(this, "Aluno com RGM " + rgm + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            painelBoletim.getLblBoletimRgm().setText(aluno.getRgm());
            painelBoletim.getLblBoletimNome().setText(aluno.getNome());
            painelBoletim.getLblBoletimCurso().setText(aluno.getCurso());
            
            List<NotasFaltas> listaDeNotas = notasFaltasDAO.consultarPorAluno(rgm);
            
            if (!listaDeNotas.isEmpty()) {
                for (NotasFaltas nota : listaDeNotas) {
                    Object[] linha = {
                        nota.getId(),
                        nota.getSemestre(),
                        nota.getDisciplina(),
                        nota.getNota(),
                        nota.getFaltas()
                    };
                    painelBoletim.getTableModel().addRow(linha);
                }
            }
            
            abas.setSelectedComponent(painelBoletim);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar boletim:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void excluirNotaFalta() {
        if (abas.getSelectedComponent() != painelBoletim) {
            JOptionPane.showMessageDialog(this, "Consulte o Boletim e selecione uma nota.", "Ação Inválida", JOptionPane.WARNING_MESSAGE);
            abas.setSelectedComponent(painelBoletim);
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
            notasFaltasDAO.excluir(idNotaSelecionada); 
            
            JOptionPane.showMessageDialog(this, "Nota/Falta excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            int linhaParaRemover = painelBoletim.getTabelaNotas().getSelectedRow();
            painelBoletim.getTableModel().removeRow(linhaParaRemover);
            idNotaSelecionada = -1; 
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir nota/falta:\n" + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void alterarNotaFalta() {
        if (idNotaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma nota na aba 'Boletim' primeiro.", "Ação Inválida", JOptionPane.WARNING_MESSAGE);
            abas.setSelectedComponent(painelBoletim);
            return;
        }
        
        if (abas.getSelectedComponent() != painelLancarNotas) {
             JOptionPane.showMessageDialog(this, "Os dados da nota selecionada estão nesta aba, prontos para editar. Clique 'Alterar' novamente para confirmar.", "Ação Inválida", JOptionPane.WARNING_MESSAGE);
             abas.setSelectedComponent(painelLancarNotas);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja salvar as alterações para a nota ID " + idNotaSelecionada + "?", "Confirmação de Alteração", JOptionPane.YES_NO_OPTION);
        
        if (resposta != JOptionPane.YES_OPTION) {
            return; 
        }

        NotasFaltas nota = new NotasFaltas();
        nota.setId(idNotaSelecionada); 
        
        nota.setSemestre((String) painelLancarNotas.getComboSemestre().getSelectedItem());
        nota.setDisciplina((String) painelLancarNotas.getComboDisciplina().getSelectedItem());
        nota.setNota((Double) painelLancarNotas.getSpinnerNota().getValue());
        nota.setFaltas((Integer) painelLancarNotas.getSpinnerFaltas().getValue());
        
        try {
            notasFaltasDAO.alterar(nota); 
            
            JOptionPane.showMessageDialog(this, "Nota alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            String rgm = painelBoletim.getLblBoletimRgm().getText(); 
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
    
    private void gerarBoletimPDF() {
        String rgm = painelBoletim.getLblBoletimRgm().getText();
        if (rgm == null || rgm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Consulte um boletim primeiro.",
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar PDF como...");
        fileChooser.setSelectedFile(new File("Boletim_" + rgm + ".pdf"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".pdf")) {
                fileToSave = new File(filePath + ".pdf");
            }
            
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);
                
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    
                    PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                    PDType1Font fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                    
                    float y = page.getMediaBox().getUpperRightY() - 50; 
                    float x = 50; 
                    
                    contentStream.beginText();
                    contentStream.setFont(fontBold, 18);
                    contentStream.newLineAtOffset(x, y);
                    contentStream.showText("Boletim Escolar");
                    contentStream.endText();
                    y -= 30; 

                    contentStream.beginText();
                    contentStream.setFont(fontRegular, 12);
                    contentStream.newLineAtOffset(x, y);
                    contentStream.showText("RGM: " + painelBoletim.getLblBoletimRgm().getText());
                    y -= 15; 
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Nome: " + painelBoletim.getLblBoletimNome().getText());
                    y -= 15;
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Curso: " + painelBoletim.getLblBoletimCurso().getText());
                    contentStream.endText();
                    y -= 30;

                    contentStream.beginText();
                    contentStream.setFont(fontBold, 12);
                    contentStream.newLineAtOffset(x, y);
                    
                    float colSemestre = 120;
                    float colDisciplina = 250;
                    float colNota = 80;
                    
                    contentStream.showText("Semestre");
                    contentStream.newLineAtOffset(colSemestre, 0);
                    contentStream.showText("Disciplina");
                    contentStream.newLineAtOffset(colDisciplina, 0);
                    contentStream.showText("Nota");
                    contentStream.newLineAtOffset(colNota, 0);
                    contentStream.showText("Faltas");
                    contentStream.endText();
                    y -= 20;

                    contentStream.setFont(fontRegular, 10);
                    
                    DefaultTableModel tableModel = painelBoletim.getTableModel();
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        String semestre = (String) tableModel.getValueAt(i, 1);
                        String disciplina = (String) tableModel.getValueAt(i, 2);
                        String nota = tableModel.getValueAt(i, 3).toString();
                        String faltas = tableModel.getValueAt(i, 4).toString();

                        contentStream.beginText();
                        contentStream.newLineAtOffset(x, y);
                        contentStream.showText(semestre);
                        contentStream.newLineAtOffset(colSemestre, 0);
                        contentStream.showText(disciplina);
                        contentStream.newLineAtOffset(colDisciplina, 0);
                        contentStream.showText(nota);
                        contentStream.newLineAtOffset(colNota, 0);
                        contentStream.showText(faltas);
                        contentStream.endText();
                        
                        y -= 15;
                    }
                } 
                
                document.save(fileToSave);
                
                JOptionPane.showMessageDialog(this,
                    "PDF salvo com sucesso em:\n" + fileToSave.getAbsolutePath(),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Erro ao salvar PDF: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    // --- Métodos da Aba "Cadastro Sistema" ---

    /**
     * (MÉTODO ATUALIZADO)
     */
    private void carregarComboBoxesEListas() {
        // Pega os componentes dos painéis
        JComboBox<String> comboCurso = painelDadosPessoais.getComboCurso();
        JComboBox<String> comboCampus = painelDadosPessoais.getComboCampus();
        JComboBox<String> comboDisciplina = painelLancarNotas.getComboDisciplina();
        DefaultListModel<String> modeloCursos = painelCadastroSistema.getModeloListaCursos();
        DefaultListModel<String> modeloCampus = painelCadastroSistema.getModeloListaCampus();
        DefaultListModel<String> modeloDisciplinas = painelCadastroSistema.getModeloListaDisciplinas();
        JComboBox<String> comboCursoFiltro = painelConsultaAlunos.getComboCursoFiltro();
        JComboBox<String> comboCampusFiltro = painelConsultaAlunos.getComboCampusFiltro();
        JComboBox<String> comboDisciplinaFiltro = painelConsultaAlunos.getComboDisciplinaFiltro();
        JComboBox<String> comboCursoDisciplina = painelCadastroSistema.getComboCursoDisciplina();
        
        // Limpa tudo primeiro
        comboCurso.removeAllItems();
        comboCampus.removeAllItems();
        comboDisciplina.removeAllItems();
        modeloCursos.removeAllElements();
        modeloCampus.removeAllElements();
        modeloDisciplinas.removeAllElements();
        comboCursoFiltro.removeAllItems();
        comboCampusFiltro.removeAllItems();
        comboDisciplinaFiltro.removeAllItems();
        comboCursoDisciplina.removeAllItems();
        
        // Adiciona a opção "TODOS" nos filtros
        comboCursoFiltro.addItem("TODOS");
        comboCampusFiltro.addItem("TODOS");
        comboDisciplinaFiltro.addItem("TODOS");
        
        // Carrega Cursos
        List<String> cursos = cursoDAO.consultarTodos();
        for (String curso : cursos) {
            comboCurso.addItem(curso);
            modeloCursos.addElement(curso);
            comboCursoFiltro.addItem(curso);
            comboCursoDisciplina.addItem(curso); // <-- Adiciona cursos no novo combo
        }
        
        // Carrega Campus
        List<String> campusList = campusDAO.consultarTodos();
        for (String campus : campusList) {
            comboCampus.addItem(campus);
            modeloCampus.addElement(campus);
            comboCampusFiltro.addItem(campus);
        }
        
        // Carrega Disciplinas
        List<DisciplinaDTO> disciplinas = disciplinaDAO.consultarTodos();
        modeloDisciplinas.removeAllElements();
        comboDisciplinaFiltro.removeAllItems(); // Limpa e readiciona
        comboDisciplinaFiltro.addItem("TODOS");
        
        for (DisciplinaDTO dto : disciplinas) {
            modeloDisciplinas.addElement(dto.nome + "  (" + dto.cursoNome + ")");
            
            // Evita duplicatas no filtro de consulta
            if (((DefaultComboBoxModel<String>)comboDisciplinaFiltro.getModel()).getIndexOf(dto.nome) == -1) {
                comboDisciplinaFiltro.addItem(dto.nome);
            }
        }
        
        // O comboDisciplina da Aba 2 (Lançar Notas) NÃO é carregado aqui.
        // Ele é carregado dinamicamente no método 'buscarAlunoParaNotas()'
    }

    /**
     * (MÉTODO ATUALIZADO)
     */
    private void salvarNovoItem(String tipo) {
        String nome = "";
        try {
            if (tipo.equals("curso")) {
                nome = painelCadastroSistema.getTxtNovoCurso().getText().trim();
                if (nome.isEmpty()) return;
                cursoDAO.salvar(nome);
                painelCadastroSistema.getTxtNovoCurso().setText("");
            } else if (tipo.equals("campus")) {
                nome = painelCadastroSistema.getTxtNovoCampus().getText().trim();
                if (nome.isEmpty()) return;
                campusDAO.salvar(nome);
                painelCadastroSistema.getTxtNovoCampus().setText("");
            } else if (tipo.equals("disciplina")) {
                String nomeCurso = (String) painelCadastroSistema.getComboCursoDisciplina().getSelectedItem();
                nome = painelCadastroSistema.getTxtNovaDisciplina().getText().trim();
                
                if (nome.isEmpty() || nomeCurso == null) {
                    JOptionPane.showMessageDialog(this, "Selecione um curso e digite um nome para a disciplina.", "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                disciplinaDAO.salvar(nome, nomeCurso);
                painelCadastroSistema.getTxtNovaDisciplina().setText("");
            }
            
            carregarComboBoxesEListas();
            
        } catch (SQLException e) {
            if (e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                JOptionPane.showMessageDialog(this, "Erro: O item '" + nome + "' já existe.", "Item Duplicado", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro de BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * (MÉTODO ATUALIZADO)
     */
    /**
     * (MÉTODO ATUALIZADO)
     * Método genérico para excluir um item selecionado
     * (COM EXCLUSÃO EM CASCATA DE CURSO -> DISCIPLINAS)
     */
    private void excluirItemSelecionado(String tipo) {
        String itemSelecionado = null;
        try {
            if (tipo.equals("curso")) {
                itemSelecionado = painelCadastroSistema.getListaCursos().getSelectedValue();
                if (itemSelecionado == null) return;
                
                // --- VERIFICAÇÃO 1: Alunos no Curso ---
                if (alunoDAO.temAlunosParaCurso(itemSelecionado)) {
                    JOptionPane.showMessageDialog(this,
                        "Não é possível excluir o curso '" + itemSelecionado + "'.\n" +
                        "Existem alunos matriculados nele. Altere os alunos primeiro.",
                        "Erro de Integridade", JOptionPane.ERROR_MESSAGE);
                    return; 
                }
                
                // --- VERIFICAÇÃO 2: Notas nas Disciplinas (Sua nova regra) ---
                List<String> disciplinasDoCurso = disciplinaDAO.consultarPorCurso(itemSelecionado);
                for (String disc : disciplinasDoCurso) {
                    if (notasFaltasDAO.temNotasParaDisciplina(disc)) {
                        JOptionPane.showMessageDialog(this,
                            "Não é possível excluir o curso '" + itemSelecionado + "'.\n" +
                            "A disciplina '" + disc + "' (deste curso) possui notas lançadas.",
                            "Erro de Integridade", JOptionPane.ERROR_MESSAGE);
                        return; // Para a exclusão
                    }
                }
                
                // --- CONFIRMAÇÃO ---
                int resposta = JOptionPane.showConfirmDialog(this, 
                    "Tem certeza que deseja excluir o curso '" + itemSelecionado + "'?\n" +
                    "ATENÇÃO: Todas as " + disciplinasDoCurso.size() + " disciplinas vinculadas a ele também serão excluídas.",
                    "Confirmar Exclusão em Cascata",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (resposta != JOptionPane.YES_OPTION) {
                    return;
                }

                // --- EXECUÇÃO (Exclui disciplinas PRIMEIRO, depois o curso) ---
                disciplinaDAO.excluirPorCurso(itemSelecionado); // <-- Exclusão em cascata
                cursoDAO.excluir(itemSelecionado); // <-- Exclusão do curso
                
            } else if (tipo.equals("campus")) {
                itemSelecionado = painelCadastroSistema.getListaCampus().getSelectedValue();
                if (itemSelecionado == null) return;
                
                 if (alunoDAO.temAlunosParaCampus(itemSelecionado)) {
                    JOptionPane.showMessageDialog(this,
                        "Não é possível excluir o campus '" + itemSelecionado + "'.\n" +
                        "Existem alunos matriculados nele. Altere os alunos primeiro.",
                        "Erro de Integridade", JOptionPane.ERROR_MESSAGE);
                    return; 
                }
                
                campusDAO.excluir(itemSelecionado);
                
            } else if (tipo.equals("disciplina")) {
                itemSelecionado = painelCadastroSistema.getListaDisciplinas().getSelectedValue();
                if (itemSelecionado == null) return;
                
                String nomeCurso = itemSelecionado.replaceAll(".*\\((.*)\\)$", "$1");
                String nomeDisciplina = itemSelecionado.replaceAll("  \\(.*\\)$", "");

                if (notasFaltasDAO.temNotasParaDisciplina(nomeDisciplina)) {
                     JOptionPane.showMessageDialog(this,
                        "Não é possível excluir a disciplina '" + nomeDisciplina + "'.\n" +
                        "Existem notas lançadas para ela.",
                        "Erro de Integridade", JOptionPane.ERROR_MESSAGE);
                    return; 
                }
                
                // Confirmação para excluir disciplina individual
                int respDisc = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir a disciplina '" + nomeDisciplina + "'?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                
                if (respDisc == JOptionPane.YES_OPTION) {
                    disciplinaDAO.excluir(nomeDisciplina, nomeCurso);
                }
            }
            
            // Atualiza todas as listas e combos
            carregarComboBoxesEListas();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro de BD", JOptionPane.ERROR_MESSAGE);
        }
    }    
    /**
     * Consulta alunos com base nos filtros e preenche a tabela
     */
    private void consultarAlunosPorFiltro() {
        String curso = (String) painelConsultaAlunos.getComboCursoFiltro().getSelectedItem();
        String campus = (String) painelConsultaAlunos.getComboCampusFiltro().getSelectedItem();
        String disciplina = (String) painelConsultaAlunos.getComboDisciplinaFiltro().getSelectedItem();
        
        if (curso != null && curso.equals("TODOS")) {
            curso = null;
        }
        if (campus != null && campus.equals("TODOS")) {
            campus = null;
        }
        if (disciplina != null && disciplina.equals("TODOS")) {
            disciplina = null;
        }
        
        DefaultTableModel model = painelConsultaAlunos.getTableModel();
        model.setRowCount(0);

        try {
            List<Aluno> alunos = alunoDAO.consultarPorFiltros(curso, campus, disciplina);
            
            for (Aluno aluno : alunos) {
                model.addRow(new Object[]{
                    aluno.getRgm(),
                    aluno.getNome(),
                    aluno.getCurso(),
                    aluno.getCampus(),
                    aluno.getPeriodo()
                });
            }
            
            if (alunos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum aluno encontrado com esses filtros.", "Busca Vazia", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar alunos: " + e.getMessage(), "Erro de BD", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    // --- Métodos Auxiliares ---

    private void coletarDadosAluno(Aluno aluno) throws ParseException {
        aluno.setRgm(painelDadosPessoais.getTxtRgm().getText().trim()); 
        aluno.setNome(painelDadosPessoais.getTxtNome().getText());
        aluno.setCpf(painelDadosPessoais.getTxtCpf().getText());
        aluno.setEmail(painelDadosPessoais.getTxtEmail().getText());
        aluno.setEndereco(painelDadosPessoais.getTxtEndereco().getText());
        aluno.setMunicipio(painelDadosPessoais.getTxtMunicipio().getText());
        aluno.setUf(painelDadosPessoais.getTxtUf().getText());
        aluno.setCelular(painelDadosPessoais.getTxtCelular().getText());

        String dataString = painelDadosPessoais.getTxtDataNasc().getText();
        if (dataString == null || dataString.contains("_")) {
            aluno.setDataNascimento(null);
        } else {
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dataUtil = formatador.parse(dataString);
            java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());
            aluno.setDataNascimento(dataSql);
        }
        
        Object cursoItem = painelDadosPessoais.getComboCurso().getSelectedItem();
        aluno.setCurso(cursoItem != null ? cursoItem.toString() : null);

        Object campusItem = painelDadosPessoais.getComboCampus().getSelectedItem();
        aluno.setCampus(campusItem != null ? campusItem.toString() : null);

        aluno.setPeriodo(getPeriodoSelecionado());
    }

    private void preencherCampos(Aluno aluno) {
        // Aba 1
        painelDadosPessoais.getTxtRgm().setText(aluno.getRgm());
        painelDadosPessoais.getTxtNome().setText(aluno.getNome());
        painelDadosPessoais.getTxtCpf().setText(aluno.getCpf());
        painelDadosPessoais.getTxtEmail().setText(aluno.getEmail());
        painelDadosPessoais.getTxtEndereco().setText(aluno.getEndereco());
        painelDadosPessoais.getTxtMunicipio().setText(aluno.getMunicipio());
        painelDadosPessoais.getTxtUf().setText(aluno.getUf());
        painelDadosPessoais.getTxtCelular().setText(aluno.getCelular());

        if (aluno.getDataNascimento() != null) {
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
            String dataFormatada = formatador.format(aluno.getDataNascimento());
            painelDadosPessoais.getTxtDataNasc().setText(dataFormatada);
        } else {
            painelDadosPessoais.getTxtDataNasc().setText(""); 
        }

        // Aba 1 (Curso)
        painelDadosPessoais.getComboCurso().setSelectedItem(aluno.getCurso());
        painelDadosPessoais.getComboCampus().setSelectedItem(aluno.getCampus());
        setPeriodoSelecionado(aluno.getPeriodo());
        
        // Aba 2 (Lançar Notas)
        painelLancarNotas.getTxtRgmNotas().setText(aluno.getRgm());
        painelLancarNotas.getLblNomeAlunoNotas().setText(aluno.getNome());
        painelLancarNotas.getLblCursoAlunoNotas().setText(aluno.getCurso());
        this.rgmAlunoAtual = aluno.getRgm();
    }
    
    private void limparCamposAluno() {
        // Dados Pessoais
        painelDadosPessoais.getTxtRgm().setText("");
        painelDadosPessoais.getTxtNome().setText("");
        painelDadosPessoais.getTxtDataNasc().setText(""); 
        painelDadosPessoais.getTxtCpf().setText("");
        painelDadosPessoais.getTxtEmail().setText("");
        painelDadosPessoais.getTxtEndereco().setText("");
        painelDadosPessoais.getTxtMunicipio().setText("");
        painelDadosPessoais.getTxtUf().setText("");
        painelDadosPessoais.getTxtCelular().setText("");
        
        // Curso
        painelDadosPessoais.getComboCurso().setSelectedIndex(-1); 
        painelDadosPessoais.getComboCampus().setSelectedIndex(-1); 
        painelDadosPessoais.getRadioNoturno().setSelected(true); 
        
        painelDadosPessoais.getTxtRgm().requestFocus();
    }

    private void setPeriodoSelecionado(String periodo) {
        ButtonGroup grupoPeriodo = painelDadosPessoais.getGrupoPeriodo();
        if (periodo == null) {
            grupoPeriodo.clearSelection(); 
        } else if (periodo.equals("Matutino")) {
            painelDadosPessoais.getRadioMatutino().setSelected(true);
        } else if (periodo.equals("Vespertino")) {
            painelDadosPessoais.getRadioVespertino().setSelected(true);
        } else if (periodo.equals("Noturno")) {
            painelDadosPessoais.getRadioNoturno().setSelected(true);
        } else {
            grupoPeriodo.clearSelection();
        }
    }
    
    private String getPeriodoSelecionado() {
        if (painelDadosPessoais.getRadioMatutino().isSelected()) return "Matutino";
        if (painelDadosPessoais.getRadioVespertino().isSelected()) return "Vespertino";
        if (painelDadosPessoais.getRadioNoturno().isSelected()) return "Noturno";
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