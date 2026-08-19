import javax.swing.JOptionPane;

public class MenuGrafico {
    public static void main(String[] args) {
        // Opções que aparecerão nos botões
        String[] opcoes = {"Sirley", "Junior", "Sair"};
        CalculoNota aritmetica = new CalculoNota();

        // Exibe a caixa de diálogo com os botões personalizados
        int escolha = JOptionPane.showOptionDialog(
                null,
                "Selecione a ação desejada:",
                "Cálculo de Nota",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes, // Array com os textos dos botões
                opcoes[0] // Botão focado por padrão
        );

        // O retorno é o índice do array selecionado (0, 1 ou 2). Se fechar a janela, retorna -1.
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
                double mediaFinal = aritmetica.calcularMedia1ALP(p1, list, p2);

                // 3. Define o status do aluno (Aprovado / Reprovado)
                String status = (mediaFinal >= 6.0) ? "APROVADO" : "NECESSÁRIO P3";

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

                if(mediaFinal < 6){
                    double p3 = pedirNota("Digite a nota da Prova 3 (P3):");
                    if (p3 == -1) return;

                    double mediaFinal2 = aritmetica.calcularMedia2ALP(p1,list, p2, p3);
                    status = (mediaFinal2 >= 6.0) ? "APROVADO" : (mediaFinal2 < 4) ? "REPROVADO, INELEGÍVEL PARA EXAME FINAL" : "NECESSÁRIO EXAME FINAL";

                     mensagemResultado = String.format(
                            "Média final com a P3: %.2f\n" +
                                    "Situação: %s",
                            mediaFinal2, status
                     );

                    JOptionPane.showMessageDialog(null, mensagemResultado, "Resultado Final", JOptionPane.INFORMATION_MESSAGE);

                    if(mediaFinal2<6 && mediaFinal2>=4){
                        double exame = pedirNota("Digite a nota do Exame Final:");
                        if (exame == -1) return;

                        status = (exame < 6) ? "REPROVADO" : "APROVADO";

                        mensagemResultado = String.format(
                                "Resultado do Exame Final: %.2f\n" +
                                        "Situação: %s",
                                exame, status
                        );

                        JOptionPane.showMessageDialog(null, mensagemResultado, "Resultado Final", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            case 1 -> {
                // 1. Pede as duas notas em sequência
                double p1 = pedirNota("Digite a nota da Prova 1 (P1):");
                if (p1 == -1) return; // Cancela se o usuário fechar a janela

                double p2 = pedirNota("Digite a nota da Prova 2 (P2):");
                if (p2 == -1) return;

                // 2. Calcula a média usando a sua função
                double mediaFinal = aritmetica.calcularMedia1OS(p1, p2);

                //3. Define o status do aluno como (APROVADO/REPROVADO)
                String status = (mediaFinal >= 6.0) ? "APROVADO" : "REPROVADO";

                // 4. Exibe o resultado final formatado com 2 casas decimais
                String mensagemResultado = String.format(
                        "Resumo de Notas:\n" +
                                "- P1: %.2f\n" +
                                "- P2: %.2f\n\n" +
                                "Média Final: %.2f\n" +
                                "Situação: %s",
                        p1, p2, mediaFinal, status
                );

                JOptionPane.showMessageDialog(null, mensagemResultado, "Resultado Final", JOptionPane.INFORMATION_MESSAGE);
            }
            case 2, -1 -> JOptionPane.showMessageDialog(null, "Sistema encerrado.");
        }

    }


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