package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date; 
import java.util.ArrayList;
import java.util.List; 
import model.Aluno; 
import conexao.ConnectionFactory; 

public class AlunoDAO {
    
    /**
     * (CREATE) - Salvar
     */
    public void salvar(Aluno aluno) throws SQLException {
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
            System.err.println("Erro ao salvar aluno: " + e.getMessage());
            throw e; // Lança a exceção para a TelaPrincipal tratar
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
    public void excluir(String rgm) throws SQLException {
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
             System.err.println("Erro ao excluir aluno: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    /**
     * (UPDATE) - Alterar
     */
    public void alterar(Aluno aluno) throws SQLException {
        String sql = "UPDATE aluno SET nome = ?, data_nascimento = ?, cpf = ?, email = ?, " +
                     "endereco = ?, municipio = ?, uf = ?, celular = ?, curso = ?, " +
                     "campus = ?, periodo = ? " +
                     "WHERE rgm = ?"; 

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            
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
            stmt.setString(12, aluno.getRgm()); 
            
            stmt.executeUpdate();
            System.out.println("Aluno alterado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao alterar aluno: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * (READ) - Consultar por RGM
     */
    public Aluno consultar(String rgm) {
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
    
    /**
     * (READ) - Consulta alunos por filtros (curso, campus, disciplina)
     */
    public List<Aluno> consultarPorFiltros(String curso, String campus, String disciplina) {
        List<Aluno> alunos = new ArrayList<>();
        
        String sql = "SELECT * FROM aluno a WHERE 1=1 ";
        
        if (curso != null) {
            sql += " AND a.curso = ?";
        }
        if (campus != null) {
            sql += " AND a.campus = ?";
        }
        if (disciplina != null) {
            sql += " AND EXISTS (SELECT 1 FROM notas_faltas nf " +
                   "              WHERE nf.rgm_aluno = a.rgm AND nf.disciplina = ?)";
        }
        sql += " ORDER BY a.nome";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; 
        
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            
            int paramIndex = 1;
            if (curso != null) {
                stmt.setString(paramIndex++, curso);
            }
            if (campus != null) {
                stmt.setString(paramIndex++, campus);
            }
            if (disciplina != null) {
                stmt.setString(paramIndex++, disciplina);
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setRgm(rs.getString("rgm"));
                aluno.setNome(rs.getString("nome"));
                aluno.setCurso(rs.getString("curso"));
                aluno.setCampus(rs.getString("campus"));
                aluno.setPeriodo(rs.getString("periodo"));
                
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar por filtros: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return alunos;
    }
    
    /**
     * Verifica se algum aluno está matriculado em um curso específico.
     */
    public boolean temAlunosParaCurso(String nomeCurso) {
        String sql = "SELECT 1 FROM aluno WHERE curso = ? LIMIT 1";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeCurso);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retorna true se encontrar pelo menos 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Por segurança, não deixa excluir se der erro
        }
    }

    /**
     * Verifica se algum aluno está matriculado em um campus específico.
     */
    public boolean temAlunosParaCampus(String nomeCampus) {
        String sql = "SELECT 1 FROM aluno WHERE campus = ? LIMIT 1";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeCampus);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retorna true se encontrar pelo menos 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Por segurança, não deixa excluir se der erro
        }
    }
}