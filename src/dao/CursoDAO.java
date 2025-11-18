package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import conexao.ConnectionFactory;

public class CursoDAO {

    // (CREATE) Salva um novo curso
    public void salvar(String nomeCurso) throws SQLException {
        String sql = "INSERT INTO cursos (nome) VALUES (?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeCurso);
            stmt.executeUpdate();
        }
    }

    // (READ) Consulta todos os cursos
    public List<String> consultarTodos() {
        String sql = "SELECT nome FROM cursos ORDER BY nome";
        List<String> cursos = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                cursos.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursos;
    }

    // (DELETE) Exclui um curso pelo nome
    public void excluir(String nomeCurso) throws SQLException {
        String sql = "DELETE FROM cursos WHERE nome = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeCurso);
            stmt.executeUpdate();
        }
    }
}