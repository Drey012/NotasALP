public class CalculoNota {
    public double calcularMedia1ALP(double p1, double list, double p2){
        return 0.35*p1 + 0.15*list + 0.5*p2;
    }

    public double calcularMedia2ALP(double p1, double list, double p2, double p3){
        if(p1<p2){
            return 0.35*p3 + 0.15*list + 0.5*p2;
        }
        else {
            return 0.35*p1 + 0.15*list + 0.5*p3;
        }
    }
}

