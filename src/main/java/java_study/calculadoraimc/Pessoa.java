package java_study.calculadoraimc;

public class Pessoa {

    private final int id;
    private final String nome;
    private final double altura; // em metros
    private final double peso;   // em kg
    private final double imc;

    public Pessoa(int id, String nome, double altura, double peso) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome não pode ficar vazio.");
        }
        if (altura < 0.5 || altura > 3.0) {
            throw new IllegalArgumentException("Altura inválida. Informe em metros (ex.: 1.75).");
        }
        if (peso < 1 || peso > 500) {
            throw new IllegalArgumentException("Peso inválido. Informe em kg (ex.: 70.5).");
        }

        this.id = id;
        this.nome = nome.trim().replace(",", " ");
        this.altura = altura;
        this.peso = peso;
        this.imc = calcularImc();
    }

    public double calcularImc() {
        return peso / (altura * altura);
    }

    public String getClassificacao() {
        if (imc < 18.5) {
            return "Abaixo do Peso";
        } else if (imc < 25) {
            return "Peso Normal";
        } else if (imc < 30) {
            return "Sobrepeso";
        } else if (imc < 35) {
            return "Obesidade Grau 1";
        } else if (imc < 40) {
            return "Obesidade Grau 2";
        } else {
            return "Obesidade Grau 3";
        }
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getAltura() { return altura; }
    public double getPeso() { return peso; }
    public double getImc() { return imc; }
}
