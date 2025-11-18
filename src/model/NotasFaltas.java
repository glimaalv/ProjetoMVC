package model;

public class NotasFaltas {

    // --- Atributos ---
    private int id; // Chave primária da tabela
    private String semestre;
    private String disciplina;
    private double nota; 
    private int faltas;
    
    // Chave estrangeira
    private String rgmAluno;

    
    // --- Getters e Setters ---
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public String getRgmAluno() {
        return rgmAluno;
    }

    public void setRgmAluno(String rgmAluno) {
        this.rgmAluno = rgmAluno;
    }
}