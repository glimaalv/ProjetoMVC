package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    
    private static final String URL = "jdbc:sqlite:cadastro_alunos.db";

    // --- Bloco de Inicialização Estático ---
    // Roda uma vez, quando o programa inicia, para garantir que as tabelas existam
    static {
        try (Connection conn = getConnection()) {
            System.out.println("Verificando/Criando tabelas do banco de dados...");
            criarTabelasIniciais(conn);
        } catch (Exception e) {
            System.err.println("FALHA CRÍTICA AO INICIAR BANCO DE DADOS:");
            e.printStackTrace();
            System.exit(1); 
        }
    }
    // --- FIM DO BLOCO ---

    /**
     * Obtém uma conexão com o banco de dados SQLite.
     */
    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC"); 
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite não encontrado. " +
                "Verifique se o .jar foi adicionado ao Build Path.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na conexão com o Banco de Dados: " 
                + e.getMessage(), e);
        }
    }

    /**
     * Este método cria as tabelas (schema) no banco SQLite
     * se elas ainda não existirem.
     */
    private static void criarTabelasIniciais(Connection conn) {
        String sqlAluno = "CREATE TABLE IF NOT EXISTS aluno (" +
                          "  rgm VARCHAR(20) PRIMARY KEY," +
                          "  nome VARCHAR(100) NOT NULL," +
                          "  data_nascimento DATE," +
                          "  cpf VARCHAR(15) UNIQUE," +
                          "  email VARCHAR(100)," +
                          "  endereco VARCHAR(255)," +
                          "  municipio VARCHAR(50)," +
                          "  uf CHAR(2)," +
                          "  celular VARCHAR(20)," +
                          "  curso VARCHAR(100)," +
                          "  campus VARCHAR(50)," +
                          "  periodo VARCHAR(10)" +
                          ");";
        
        String sqlNotas = "CREATE TABLE IF NOT EXISTS notas_faltas (" +
                          "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                          "  semestre VARCHAR(10)," +
                          "  disciplina VARCHAR(100)," +
                          "  nota DECIMAL(4, 2)," +
                          "  faltas INT," +
                          "  rgm_aluno VARCHAR(20)," +
                          "  FOREIGN KEY (rgm_aluno) REFERENCES aluno(rgm) " +
                          "    ON DELETE CASCADE" +
                          ");";
        
        String sqlCursos = "CREATE TABLE IF NOT EXISTS cursos (" +
                           "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                           "  nome VARCHAR(100) NOT NULL UNIQUE" +
                           ");";
        
        String sqlCampus = "CREATE TABLE IF NOT EXISTS campus (" +
                           "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                           "  nome VARCHAR(100) NOT NULL UNIQUE" +
                           ");";
                           
        String sqlDisciplinas = "CREATE TABLE IF NOT EXISTS disciplinas (" +
                                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "  nome VARCHAR(100) NOT NULL," +
                                "  curso_nome VARCHAR(100) NOT NULL," + // <-- Vínculo com Curso
                                "  UNIQUE(nome, curso_nome)" + 
                                ");";

        try (java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(sqlAluno);
            stmt.execute(sqlNotas);
            stmt.execute(sqlCursos);
            stmt.execute(sqlCampus);
            stmt.execute(sqlDisciplinas);
            System.out.println("Tabelas verificadas/criadas com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
    
    /**
     * Método main apenas para testar a conexão
     */
    public static void main(String[] args) {
         try {
            Connection conn = ConnectionFactory.getConnection();
            System.out.println("Conexão testada com SUCESSO!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}