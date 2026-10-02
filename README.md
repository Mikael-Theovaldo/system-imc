<h1 align="center">📊 Calculadora de IMC</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/JavaFX-0A74DA?style=for-the-badge&logoColor=white" alt="JavaFX"/>
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven"/>
  <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white" alt="IntelliJ IDEA"/>
  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white" alt="Git"/>
  <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
</p>

<p align="center">
  Aplicação desktop em <b>JavaFX</b> para cadastrar pessoas, calcular o <b>Índice de Massa Corporal (IMC)</b> e salvar os dados em arquivo CSV.
</p>

---

## Sobre o projeto

Exercício de laboratório de **Programação Orientada a Objetos**. O usuário informa **nome**, **altura** (m) e **peso** (kg), e a aplicação calcula o IMC, mostra a classificação e lista todos os cadastros em uma tabela. Os dados podem ser salvos e carregados de um arquivo de texto.

**Fórmula:** `IMC = peso / (altura × altura)`

## Funcionalidades

- ✅ Cálculo do IMC com classificação automática
- ✅ Cadastro de pessoas exibido em uma `TableView`
- ✅ Persistência dos dados em arquivo CSV (`dados_pessoas.txt`)
- ✅ Carregamento automático dos dados ao abrir o programa
- ✅ Validação de entrada (aceita `1,75` ou `1.75`) e tratamento de exceções

## Classificação do IMC

| IMC | Classificação |
|---|---|
| menor que 18,5 | Abaixo do Peso |
| 18,5 a 24,99 | Peso Normal |
| 25 a 29,99 | Sobrepeso |
| 30 a 34,99 | Obesidade Grau 1 |
| 35 a 39,99 | Obesidade Grau 2 |
| 40 ou mais | Obesidade Grau 3 |

## Estrutura do projeto

```
calculadoraimc/
├── pom.xml
├── dados_pessoas.txt
└── src/main/java/
    ├── module-info.java
    └── java_study/calculadoraimc/
        ├── HelloApplication.java   # Interface JavaFX e lógica dos botões
        ├── Pessoa.java             # Modelo de dados e cálculo do IMC
        └── ArquivoUtil.java        # Leitura e gravação do arquivo CSV
```

## Formato do arquivo

Cada linha do `dados_pessoas.txt` segue o formato `id,nome,altura,peso,imc`:

```
1,Ana Souza,1.65,58.00,21.30
2,Carlos Lima,1.80,95.00,29.32
```

## Como executar

**Requisitos:** JDK 17 ou superior e Maven (já incluso no IntelliJ).

```bash
# 1. Clone o repositório
git clone https://github.com/Mikael-Theovaldo/calculadoraimc.git

# 2. Entre na pasta
cd calculadoraimc

# 3. Execute com o Maven
mvn javafx:run
```

Ou, pela IDE, abra o `pom.xml` como projeto e execute a classe `HelloApplication`.

> ⚠️ O arquivo `dados_pessoas.txt` deve ficar na **raiz do projeto**, ao lado do `pom.xml`.

## 👨‍💻 Autor

Desenvolvido por **Mikael Theovaldo** · [GitHub](https://github.com/Mikael-Theovaldo)
