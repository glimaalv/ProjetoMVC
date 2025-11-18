# Sistema de Gestão Acadêmica (Projeto POO)

Sistema desktop completo para gerenciamento de alunos, notas e estrutura acadêmica, desenvolvido como projeto da disciplina de Programação Orientada a Objetos (POO) na **Fatec Guarulhos** por **Gustavo Alves**.

O projeto utiliza o padrão de arquitetura **MVC** (Model-View-Controller) e se destaca por ser uma aplicação autônoma (portable), utilizando banco de dados embarcado e geração de relatórios em PDF.

---

## 🚀 Funcionalidades Principais

### 1. Gestão de Alunos
* **CRUD Completo:** Cadastro, Consulta, Alteração e Exclusão de alunos.
* **Validações:** Impede cadastro de RGMs ou CPFs duplicados.
* **Busca Dinâmica:** Interface de busca avançada filtrando por Curso, Campus ou Disciplina.

### 2. Gestão Acadêmica (Notas e Faltas)
* **Lançamento:** Interface para atribuir notas e faltas a um aluno em uma disciplina específica.
* **Regras de Negócio:** O sistema só permite lançar notas em disciplinas vinculadas ao curso do aluno.
* **Cálculo Automático:** Seleção dinâmica de semestre (atual e anterior).

### 3. Administração do Sistema (Dinâmico)
* **Cadastro de Metadados:** O usuário pode cadastrar e excluir **Cursos**, **Campus** e **Disciplinas** livremente.
* **Integridade Referencial:** O sistema impede a exclusão de um Curso se houver alunos matriculados nele, ou a exclusão de uma Disciplina se houver notas lançadas para ela.

### 4. Relatórios
* **Boletim Visual:** Visualização em tabela das notas do aluno.
* **Exportação PDF:** Geração automática de Boletim em formato `.pdf` utilizando a biblioteca **Apache PDFBox**.

---

## 🛠️ Tecnologias e Bibliotecas

* **Linguagem:** Java (JDK 17+ recomendado)
* **Interface Gráfica:** Java Swing (com Layout Managers responsivos `GridBagLayout` e `BorderLayout`).
* **Banco de Dados:** **SQLite** (Banco de dados relacional embutido, não requer instalação de servidor).
* **Bibliotecas Externas (.jar):**
    * `sqlite-jdbc`: Driver de conexão JDBC para SQLite.
    * `pdfbox-app`: API da Apache para criação e manipulação de arquivos PDF.

---

## 📂 Estrutura do Projeto (Arquitetura)

O código foi refatorado para seguir o padrão de responsabilidade única, evitando "God Classes".

### 📦 Pacote `view` (A Interface / Frontend)
* **`TelaPrincipal.java`:** O "Controlador". Gerencia a janela principal, contém os métodos de lógica de negócio, gerencia os eventos de clique (Listeners) e coordena os painéis.
* **`PainelDadosPessoais.java`:** Formulário para cadastro de dados do aluno.
* **`PainelLancarNotas.java`:** Formulário para inserção de notas e faltas.
* **`PainelBoletim.java`:** Tabela de visualização de notas e botão de exportação PDF.
* **`PainelCadastroSistema.java`:** Área administrativa para criar Cursos, Campus e Disciplinas.
* **`PainelConsultaAlunos.java`:** Tabela de busca com filtros dinâmicos.

### 📦 Pacote `dao` (Acesso a Dados / Backend)
Responsáveis por executar o SQL no banco de dados.
* **`AlunoDAO.java`:** Gerencia a tabela `aluno` e verificações de existência.
* **`NotasFaltasDAO.java`:** Gerencia a tabela `notas_faltas`.
* **`CursoDAO.java`, `CampusDAO.java`, `DisciplinaDAO.java`:** Gerenciam as tabelas auxiliares.
* **`DisciplinaDTO.java`:** Objeto de Transferência de Dados auxiliar para carregar disciplinas junto com seus cursos.

### 📦 Pacote `model` (Modelos)
Classes POJO que representam as entidades do sistema.
* `Aluno.java`
* `NotasFaltas.java`

### 📦 Pacote `conexao`
* **`ConnectionFactory.java`:** Gerencia a conexão com o arquivo `cadastro_alunos.db`. Possui um bloco estático que **cria automaticamente as tabelas** do banco caso elas não existam na primeira execução.

---

## 🗄️ Banco de Dados

O sistema cria automaticamente um arquivo chamado `cadastro_alunos.db` na raiz do projeto.

**Esquema das Tabelas:**
1.  **`aluno`**: `rgm` (PK), `nome`, `cpf` (Unique), `email`, `curso`, `campus`, etc.
2.  **`notas_faltas`**: `id` (PK), `semestre`, `disciplina`, `nota`, `faltas`, `rgm_aluno` (FK).
3.  **`cursos`**: `id`, `nome` (Unique).
4.  **`campus`**: `id`, `nome` (Unique).
5.  **`disciplinas`**: `id`, `nome`, `curso_nome` (Vínculo com curso).

---

## ⚙️ Como Executar

### Pré-requisitos
* Java instalado na máquina.
* Eclipse IDE (ou outra de sua preferência).

### Passo a Passo
1.  **Clone este repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/nome-do-repo.git](https://github.com/seu-usuario/nome-do-repo.git)
    ```
2.  **Importe no Eclipse:**
    * `File` > `Import` > `General` > `Existing Projects into Workspace`.
3.  **Configure o Build Path:**
    * Certifique-se de que os arquivos `.jar` (SQLite e PDFBox) na pasta do projeto estejam adicionados ao **Classpath** do Eclipse (`Build Path` > `Configure Build Path` > `Libraries`).
4.  **Execute:**
    * Abra `src/view/TelaPrincipal.java`.
    * Clique com o botão direito > `Run As` > `Java Application`.

---

## 👤 Autor

**Gustavo Alves**
* Projeto de Faculdade (Fatec Guarulhos)
* Disciplina: Programação Orientada a Objetos (POO)
