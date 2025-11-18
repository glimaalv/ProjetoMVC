package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import conexao.ConnectionFactory;


public class DisciplinaDAO {

    /**
     * (CREATE) Salva uma nova disciplina, agora vinculada a um curso
     */
    public void salvar(String nomeDisciplina, String nomeCurso) throws SQLException {
        String sql = "INSERT INTO disciplinas (nome, curso_nome) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeDisciplina);
            stmt.setString(2, nomeCurso);
            stmt.executeUpdate();
        }
    }

    /**
     * (READ) Consulta todas as disciplinas e seus cursos
     */
    public List<DisciplinaDTO> consultarTodos() {
        String sql = "SELECT nome, curso_nome FROM disciplinas ORDER BY curso_nome, nome";
        List<DisciplinaDTO> disciplinas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                DisciplinaDTO dto = new DisciplinaDTO();
                dto.nome = rs.getString("nome");
                dto.cursoNome = rs.getString("curso_nome");
                disciplinas.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disciplinas;
    }
    
    /**
     * (READ) Consulta apenas as disciplinas de UM curso
     */
    public List<String> consultarPorCurso(String nomeCurso) {
        String sql = "SELECT nome FROM disciplinas WHERE curso_nome = ? ORDER BY nome";
        List<String> disciplinas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nomeCurso);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    disciplinas.add(rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disciplinas;
    }

    /**
     * (DELETE) Exclui uma disciplina (pelo nome e pelo curso)
     */
    public void excluir(String nomeDisciplina, String nomeCurso) throws SQLException {
        String sql = "DELETE FROM disciplinas WHERE nome = ? AND curso_nome = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeDisciplina);
            stmt.setString(2, nomeCurso);
            stmt.executeUpdate();
        }
    }
    
    /**
     * (DELETE) Exclui TODAS as disciplinas de um curso específico.
     * Usado na exclusão em cascata de um Curso.
     */
    public void excluirPorCurso(String nomeCurso) throws SQLException {
        String sql = "DELETE FROM disciplinas WHERE curso_nome = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeCurso);
            stmt.executeUpdate();
            System.out.println("Disciplinas do curso " + nomeCurso + " excluídas.");
        }
    }
}