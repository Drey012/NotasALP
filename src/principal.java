import java.util.Scanner;

public class principal {
    static void main() {
        Scanner notas = new Scanner(System.in);
        CalculoNota aritmetica = new CalculoNota();

        System.out.println("Qual a nota da p1?");

        double p1 = notas.nextDouble();

        System.out.println("Qual a nota das listas?");

        double list = notas.nextDouble();

        System.out.println("Qual a nota da p2?");

        double p2 = notas.nextDouble();

        double media1 = aritmetica.calcularMedia1ALP(p1, list, p2);

        if (media1 < 6){
            System.out.println("Nota insuficiente (" + media1 +"), qual a nota da p3?");
            double p3 = notas.nextDouble();
            double media2 = aritmetica.calcularMedia2ALP(p1, list, p2, p3);
            System.out.println("A média com a p3 foi: " + media2);

            if (media2 < 6 && media2 >= 4){
                System.out.println("Que foi insuficiente. \n Qual a nota do exame final?");
                double exame = notas.nextDouble();
                if (exame < 6){
                    System.out.println("Aluno reprovado!");
                } else if (exame >= 6) {
                    System.out.println("Aluno aprovado via exame final:" + exame);
                }
            } else if (media2 < 4) {
                System.out.println("Nota inelegível para recuperação via exame final. Aluno Reprovado.");
            }
            else {
                System.out.println("Aluno aprovado! Nota com a p3: " + media2);
            }
        }
        else{
            System.out.println("A média foi:" + media1);
        }
        notas.close();
    }
}
