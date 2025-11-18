package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; // Para a lista de consulta
import java.util.List; // Para a lista de consulta

import conexao.ConnectionFactory;
import model.NotasFaltas; // Importa o modelo de NotasFaltas

public class NotasFaltasDAO {

    /**
     * Método para INSERIR uma nova nota/falta no banco.
     * (CREATE)
     */
    public void salvar(NotasFaltas nota) {
        
        String sql = "INSERT INTO notas_faltas (semestre, disciplina, nota, faltas, rgm_aluno) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            
            // Define os 5 parâmetros
            stmt.setString(1, nota.getSemestre());
            stmt.setString(2, nota.getDisciplina());
            stmt.setDouble(3, nota.getNota());
            stmt.setInt(4, nota.getFaltas());
            stmt.setString(5, nota.getRgmAluno()); // A Chave Estrangeira
            
            stmt.executeUpdate();
            System.out.println("Nota/Falta salva com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar nota/falta: " + e.getMessage(), e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * Método para CONSULTAR todas as notas/faltas de um aluno.
     * (READ)
     */
    public List<NotasFaltas> consultarPorAluno(String rgm) {
        
        String sql = "SELECT * FROM notas_faltas WHERE rgm_aluno = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        // Cria a lista que armazenará os resultados
        List<NotasFaltas> listaDeNotas = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, rgm);
            
            rs = stmt.executeQuery();
            
            // Itera sobre todos os resultados (pode haver várias notas)
            while (rs.next()) {
                NotasFaltas nota = new NotasFaltas();
                
                nota.setId(rs.getInt("id")); 
                nota.setSemestre(rs.getString("semestre"));
                nota.setDisciplina(rs.getString("disciplina"));
                nota.setNota(rs.getDouble("nota"));
                
                nota.setFaltas(rs.getInt("faltas")); 
                
                nota.setRgmAluno(rs.getString("rgm_aluno"));
                
                listaDeNotas.add(nota);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar notas: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        
        // Retorna a lista (pode estar vazia, se o aluno não tiver notas)
        return listaDeNotas;
    }

    /**
     * Método para ALTERAR uma nota/falta específica.
     * (UPDATE)
     * Precisamos do ID único da nota para saber qual alterar.
     */
    public void alterar(NotasFaltas nota) {
        
        String sql = "UPDATE notas_faltas SET semestre = ?, disciplina = ?, " +
                     "nota = ?, faltas = ? " +
                     "WHERE id = ?"; // Altera PELA CHAVE PRIMÁRIA (id)

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            
            // Define os 4 campos a serem alterados
            stmt.setString(1, nota.getSemestre());
            stmt.setString(2, nota.getDisciplina());
            stmt.setDouble(3, nota.getNota());
            stmt.setInt(4, nota.getFaltas());
            
            // O 5º parâmetro (WHERE)
            stmt.setInt(5, nota.getId()); 
            
            stmt.executeUpdate();
            System.out.println("Nota/Falta alterada com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar nota/falta: " + e.getMessage(), e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    /**
     * Método para EXCLUIR uma nota/falta específica.
     * (DELETE)
     * Precisamos do ID único da nota para saber qual excluir.
     */
    public void excluir(int id) {
        
        String sql = "DELETE FROM notas_faltas WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id); // Define o ID a ser excluído
            
            stmt.executeUpdate();
            System.out.println("Nota/Falta excluída com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir nota/falta: " + e.getMessage(), e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
 // ... (depois do seu método excluir()) ...

    /**
     * Verifica se alguma nota está registrada para uma disciplina específica.
     */
    public boolean temNotasParaDisciplina(String nomeDisciplina) {
        String sql = "SELECT 1 FROM notas_faltas WHERE disciplina = ? LIMIT 1";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeDisciplina);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retorna true se encontrar pelo menos 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Por segurança, não deixa excluir se der erro
        }
    }
}
