import javax.swing.JOptionPane;

public class SistemaNotas {
    public static void main(String[] args) {
        String[] opcoes = {"Calcular Média", "Sair"};

        int escolha = JOptionPane.showOptionDialog(
                null, "Selecione a ação desejada:", "Menu de Notas",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opcoes, opcoes[0]
        );

        switch (escolha) {
            case 0 -> {
                // 1. Pede as três notas em sequência
                double p1 = pedirNota("Digite a nota da Prova 1 (P1):");
                if (p1 == -1) return; // Cancela se o usuário fechar a janela

                double list = pedirNota("Digite a nota das Listas (List):");
                if (list == -1) return;

                double p2 = pedirNota("Digite a nota da Prova 2 (P2):");
                if (p2 == -1) return;

                // 2. Calcula a média usando a sua função
                double mediaFinal = calcularMedia1(p1, list, p2);

                // 3. Define o status do aluno (Aprovado / Reprovado)
                String status = (mediaFinal >= 6.0) ? "APROVADO" : "REPROVADO";

                // 4. Exibe o resultado final formatado com 2 casas decimais
                String mensagemResultado = String.format(
                        "Resumo de Notas:\n" +
                                "- P1: %.2f\n" +
                                "- Listas: %.2f\n" +
                                "- P2: %.2f\n\n" +
                                "Média Final: %.2f\n" +
                                "Situação: %s",
                        p1, list, p2, mediaFinal, status
                );

                JOptionPane.showMessageDialog(null, mensagemResultado, "Resultado Final", JOptionPane.INFORMATION_MESSAGE);
            }
            case 1, -1 -> System.exit(0);
        }
    }

    // A sua função de cálculo inserida no programa
    public static double calcularMedia1(double p1, double list, double p2) {
        return 0.35 * p1 + 0.15 * list + 0.5 * p2;
    }

    // Função auxiliar para pedir a nota, converter e tratar erros de digitação
    private static double pedirNota(String mensagem) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, mensagem);

            if (input == null) {
                return -1; // Usuário clicou em cancelar ou fechou a janela
            }

            try {
                // Transforma vírgula em ponto para evitar quebras de código
                return Double.parseDouble(input.replace(",", "."));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "[Erro] Digite um número válido. Ex: 7.5", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
