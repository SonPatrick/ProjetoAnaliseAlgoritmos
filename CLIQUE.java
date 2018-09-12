//UNIVERSIDADE FEDERAL DO OESTE DO PARÁ
//INSTITUTO DE ENGENHARIA E GEOCIÊNCIAS 
//CIÊNCIA DA COMPUTAÇÃO
//ACADÊMICO: PATRICK J. MACÊDO
//MATRÍCULA: 201100896
//DISCIPLINA: TEORIA DOS GRAFOS E COMPLEXIDADE DOS ALGORITMOS
//PROFESSOR: EFREN L. SOUZA
import java.util.Scanner;

public class Clique {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in); //recebe as entradas do usuário
		int T = sc.nextInt(); 
		
		for(int i = 1 ; i <= T ; i++){
                    int nos = sc.nextInt();
                    int arestas = sc.nextInt();
                    System.out.println("Tamanho do clique = "+Clique.tamCliqueMinimo(nos, arestas));
		}
	}
	//efetua o calculo do tamanho do clique a partir do número de nós e arestas presentes 
	public static int tamCliqueMinimo(int numNos, int arestas){
		int tamanhoClique = 0; //tamanho do clique
		int limiteInferior = 1; //limite inferior
		int limiteSuperior = numNos ; //o limite superior é igual o
		int desc = (limiteInferior + limiteSuperior)/2;
	
		do{
                    desc = (limiteInferior + limiteSuperior)/2;
                    int numArestas = getLimitClique(numNos, desc);

                    if(numArestas == arestas){
                            return desc;
                    }
                    if(limiteSuperior - limiteInferior == 1){
                            numArestas = getLimitClique(numNos, limiteInferior);
                            if(numArestas >= arestas){
                                    return limiteInferior;
                            }else{
                                    return limiteSuperior;
                            }
                    }
                    if(numArestas > arestas ){
                            limiteSuperior = desc;
                    }else{
                            limiteInferior = desc ;
                    }
		}while(limiteSuperior != limiteInferior);
	
		System.out.println("ERRO, tente novamente com outros valores");
		System.exit(-1);
		
		return tamanhoClique;
	}
	//método responsável por calcular o limite do clique
	public static int getLimitClique(double numNos, double tamCliqueMaxima){
		double numArestas = 0;
		double num_1 = ((1 - (double)1/((double)tamCliqueMaxima)));
		double num_2 = Math.pow(numNos, 2) / 2;
		numArestas = num_1 * num_2;
		
		return (int)numArestas;
	}
}