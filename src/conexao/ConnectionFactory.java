package conexao; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    
    // --- Configure com seus dados do MySQL ---
    private static final String DATABASE = "db_cadastro_aluno";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE;
    private static final String USER = "root";
    
    // --- Senha MySQL
    private static final String PASSWORD = "sua_senha_aqui";
    

    /**
     * Obtém uma conexão com o banco de dados.
     */
    public static Connection getConnection() {
        try {
            // 1. Carrega o driver JDBC do MySQL na memória
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            // 2. Tenta estabelecer a conexão
            return DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL não encontrado. " +
                "Verifique se o Connector/J foi adicionado ao Build Path.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na conexão com o Banco de Dados: " 
                + e.getMessage(), e);
        }
    }

    /**
     * Método principal apenas para testar a conexão.
     */
    public static void main(String[] args) {
        System.out.println("Tentando conectar ao banco de dados...");
        try {
            Connection conn = ConnectionFactory.getConnection();
            System.out.println("---------------------------------------------");
            System.out.println("Conexão com o banco de dados estabelecida com SUCESSO!");
            System.out.println("---------------------------------------------");
            conn.close();
            System.out.println("Conexão fechada.");
        } catch (Exception e) {
            System.err.println("*********************************");
            System.err.println("FALHA na conexão:");
            System.err.println("*********************************");
            e.printStackTrace();
        }
    }
}