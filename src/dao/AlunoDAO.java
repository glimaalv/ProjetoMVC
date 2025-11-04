package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.SQLException;
import java.sql.Date; 

import model.Aluno; 
import conexao.ConnectionFactory; 

public class AlunoDAO {

    /**
     * (CREATE) - Salvar
     */
    public void salvar(Aluno aluno) {
        // (Este método permanece o mesmo)
        String sql = "INSERT INTO aluno (rgm, nome, data_nascimento, cpf, email, " +
                     "endereco, municipio, uf, celular, curso, campus, periodo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, aluno.getRgm());
            stmt.setString(2, aluno.getNome());
            stmt.setDate(3, aluno.getDataNascimento()); 
            stmt.setString(4, aluno.getCpf());
            stmt.setString(5, aluno.getEmail());
            stmt.setString(6, aluno.getEndereco());
            stmt.setString(7, aluno.getMunicipio());
            stmt.setString(8, aluno.getUf());
            stmt.setString(9, aluno.getCelular());
            stmt.setString(10, aluno.getCurso());
            stmt.setString(11, aluno.getCampus());
            stmt.setString(12, aluno.getPeriodo());

            stmt.executeUpdate();
            System.out.println("Aluno salvo com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar aluno: " + e.getMessage(), e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * (DELETE) - Excluir
     */
    public void excluir(String rgm) {
        // (Este método permanece o mesmo)
        String sql = "DELETE FROM aluno WHERE rgm = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, rgm);
            stmt.executeUpdate();
            
            System.out.println("Aluno excluído com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir aluno: " + e.getMessage(), e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    /**
     * ***** MÉTODO ATUALIZADO *****
     * Método para ATUALIZAR os dados de um aluno.
     * (UPDATE)
     */
    public void alterar(Aluno aluno) {
        
        // Comando SQL de UPDATE
        String sql = "UPDATE aluno SET nome = ?, data_nascimento = ?, cpf = ?, email = ?, " +
                     "endereco = ?, municipio = ?, uf = ?, celular = ?, curso = ?, " +
                     "campus = ?, periodo = ? " +
                     "WHERE rgm = ?"; // O RGM é a condição (chave)

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // 1. Conexão
            conn = ConnectionFactory.getConnection();
            
            // 2. Preparar SQL
            stmt = conn.prepareStatement(sql);
            
            // 3. Definir os parâmetros (?)
            // Note que são 11 campos para atualizar + 1 RGM no final
            stmt.setString(1, aluno.getNome());
            stmt.setDate(2, aluno.getDataNascimento());
            stmt.setString(3, aluno.getCpf());
            stmt.setString(4, aluno.getEmail());
            stmt.setString(5, aluno.getEndereco());
            stmt.setString(6, aluno.getMunicipio());
            stmt.setString(7, aluno.getUf());
            stmt.setString(8, aluno.getCelular());
            stmt.setString(9, aluno.getCurso());
            stmt.setString(10, aluno.getCampus());
            stmt.setString(11, aluno.getPeriodo());
            
            // O último ? (número 12) é o RGM
            stmt.setString(12, aluno.getRgm()); 
            
            // 4. Executar
            stmt.executeUpdate();
            
            System.out.println("Aluno alterado com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar aluno: " + e.getMessage(), e);
        } finally {
            // 5. Fechar tudo
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * (READ) - Consultar
     */
    public Aluno consultar(String rgm) {
        // (Este método permanece o mesmo)
        String sql = "SELECT * FROM aluno WHERE rgm = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; 
        
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, rgm);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Aluno aluno = new Aluno();
                
                aluno.setRgm(rs.getString("rgm"));
                aluno.setNome(rs.getString("nome"));
                aluno.setDataNascimento(rs.getDate("data_nascimento"));
                aluno.setCpf(rs.getString("cpf"));
                aluno.setEmail(rs.getString("email"));
                aluno.setEndereco(rs.getString("endereco"));
                aluno.setMunicipio(rs.getString("municipio"));
                aluno.setUf(rs.getString("uf"));
                aluno.setCelular(rs.getString("celular"));
                aluno.setCurso(rs.getString("curso"));
                aluno.setCampus(rs.getString("campus"));
                aluno.setPeriodo(rs.getString("periodo"));
                
                return aluno;
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar aluno: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return null; 
    }
}