package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import conexao.ConnectionFactory;

public class CampusDAO {

    // (CREATE) Salva um novo campus
    public void salvar(String nomeCampus) throws SQLException {
        String sql = "INSERT INTO campus (nome) VALUES (?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeCampus);
            stmt.executeUpdate();
        }
    }

    // (READ) Consulta todos os campus
    public List<String> consultarTodos() {
        String sql = "SELECT nome FROM campus ORDER BY nome";
        List<String> campusList = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                campusList.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return campusList;
    }

    // (DELETE) Exclui um campus pelo nome
    public void excluir(String nomeCampus) throws SQLException {
        String sql = "DELETE FROM campus WHERE nome = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeCampus);
            stmt.executeUpdate();
        }
    }
}