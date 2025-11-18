package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class PainelConsultaAlunos extends JPanel {

    // --- Atributos dos Componentes ---
    private JComboBox<String> comboCursoFiltro;
    private JComboBox<String> comboCampusFiltro;
    private JComboBox<String> comboDisciplinaFiltro;
    private JButton btnBuscar;
    private JTable tabelaAlunos;
    private DefaultTableModel tableModel;
    
    public PainelConsultaAlunos() {
        this.setLayout(new BorderLayout(5, 5));
        
        // --- PAINEL DE FILTROS (TOPO) ---
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        painelFiltros.add(new JLabel("Curso:"));
        comboCursoFiltro = new JComboBox<>();
        painelFiltros.add(comboCursoFiltro);
        
        painelFiltros.add(new JLabel("Campus:"));
        comboCampusFiltro = new JComboBox<>();
        painelFiltros.add(comboCampusFiltro);
        
        painelFiltros.add(new JLabel("Disciplina:"));
        comboDisciplinaFiltro = new JComboBox<>();
        painelFiltros.add(comboDisciplinaFiltro);
        
        btnBuscar = new JButton("Buscar");
        painelFiltros.add(btnBuscar);
        
        this.add(painelFiltros, BorderLayout.NORTH);
        
        // --- TABELA DE RESULTADOS (CENTRO) ---
        String[] colunas = { "RGM", "Nome", "Curso", "Campus", "Período" };
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaAlunos = new JTable(tableModel);
        tabelaAlunos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        this.add(new JScrollPane(tabelaAlunos), BorderLayout.CENTER);
    }
    
    // --- MÉTODOS GETTER ---
    public JComboBox<String> getComboCursoFiltro() { return comboCursoFiltro; }
    public JComboBox<String> getComboCampusFiltro() { return comboCampusFiltro; }
    public JComboBox<String> getComboDisciplinaFiltro() { return comboDisciplinaFiltro; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public DefaultTableModel getTableModel() { return tableModel; }
}