//UNIVERSIDADE FEDERAL DO OESTE DO PARÁ
//INSTITUTO DE ENGENHARIA E GEOCIÊNCIAS 
//CIÊNCIA DA COMPUTAÇÃO
//ACADÊMICO: PATRICK J. MACÊDO
//MATRÍCULA: 201100896
//DISCIPLINA: TEORIA DOS GRAFOS E COMPLEXIDADE DOS ALGORITMOS
//PROFESSOR: EFREN L. SOUZA

//ALGORITMO EXEMPLO PARA O PROBLEMA 3SAT
package tressatverificador;

import java.util.*;

public class TresSatVerificador{
        //3SAT SIGNIFICA QUE SÃO 3 LITERAIS EM CADA CLÁUSULA
	private static int NUMERO_LITERAIS_CLAUSULA = 3;
        //FORMA NORMAL CONJUNTIVA
	private String EntradaFnc = "";
        //CLÁUSULAS ONDE ESTARÃO OS LITERAIS
	private String[] clausulas; 
        //LINHAS PARA CADA CLÁUSULA VERDADEIRA SIGNIFICA QUE UM FALSO PRECEDEU O LITERAL NA COLUNA, ENTÃO SERÁ NEGADO MAIS TARDE
	private ArrayList<ArrayList<Boolean>> negacao = new ArrayList<ArrayList<Boolean>>();
        // LINHAS PARA CADA CLÁUSULA, COLUNAS ARMAZENAM O SUBSCRITO DESSE LITERAL (POR EXEMPLO, X1 OU X2 OU X4 CRIA A LINHA 2, 4)
	private ArrayList<ArrayList<Integer>> Xi = new ArrayList<ArrayList<Integer>>();	
        // VALORES Xi PARA OS LITERAIS. ATUALIZADO A PARTIR DA VERIFICAÇÃO/CERTIFICAÇÃO.
	private HashMap<Integer, Boolean> X = new HashMap<Integer, Boolean>();	
        
        //MÉTODO PRINCIPAL
        public static void main(String[] args) {
		String cnfFormula = "(NOT x1 OR X2 OR x3) AND (x1 OR NOT x2 OR x3) AND (x1 OR x2 OR x4) AND (NOT x1 OR NOT x3 OR NOT x4)";
		TresSatVerificador certifier = new TresSatVerificador(cnfFormula);
		String[] certicicacaos={"(x1=1, x2=1, x3=0, x4=1)",  "(x1=1, x2=1, x3=1, x4=1)",  "(x1=0, x2=0, x3=0, x4=1)",  "(x1=0, x2=1, x3=0, x4=1)"};
		System.out.println("DADA A FORMULA DA EQUAÇÃO NORMAL CONJUNTVA  "+cnfFormula+"\n");
		for(int i=0; i<certicicacaos.length; i++){
			System.out.println("A VERIFICAÇÃO  "+certicicacaos[i]+" É SATISFEITA? " +certifier.verificaVerdadeiro(certicicacaos[i]));
		}
	}
        
	public TresSatVerificador(String EntradaFnc){
		this.EntradaFnc=EntradaFnc.toUpperCase();
		formaNormalConjuntiva();
	}

	public void formaNormalConjuntiva(){
            //DIVIDE A STRING EM CLÁUSULAS
		clausulas = EntradaFnc.split("AND");

		//CRIA UM LOOP PARA PREENCHER AS CLÁUSULAS DE NEGAÇÃO E X
		for(int i=0; i<clausulas.length; i++){
                        //REMOVE OS PARENTESES
			clausulas[i]=clausulas[i].replace("(", "");
			clausulas[i]=clausulas[i].replace(")", "");
                        //ELIMINA OS ESPAÇOS EM BRANCO
			clausulas[i]=clausulas[i].trim();

                        //ADICIONA MAIS LINHAS NA MESMA CLÁUSULA
			negacao.add(new ArrayList<Boolean>());
			Xi.add(new ArrayList<Integer>());

                        //DIVIDE EM MÚLTIPLOS ESPAÇOS
			String[] words = clausulas[i].split(" +");

                        //AINDA NÃO LEU NENHUMA PALAVRA, ENTÃO A ÚLTIMA COISA QUE LEU NÃO PODE SER UMA NEGAÇÃO
			boolean verificaNegacao = false;
			for(int j=0; j<words.length; j++){
                            //ATUALIZA A NEGAÇÃO PARA VERDADEIRA PARA QUE O VALOR SEJA INVERTIDO MAIS TARDE NA AVALIAÇÃO
				if(words[j].equals("NOT")){
					negacao.get(i).add(true);
                                        //ENCONTROU UMA NEGAÇÃO, ENTÃO O ESTADO É ATUALIZADO
					verificaNegacao = true;	
				}
				//ARMAZENA FALSO EM NEGAÇÃO SE A PALAVRA FOR UM Xi. TAMBÉM NÃO DESEJA ENTRADAS DUPLICADA SE
                                //A PALAVRA ANTERIOR NA LINHA ESTIVER COM UMA NEGAÇÃO, ISSO ADICIONA O NÚMERO CORRETO DE COLUNAS
                                //EM UM LITERAL NEGATIVO PARA CORRESPONDER AO NÚMERO DE LITERAIS
				else if(!words[j].equals("OR") && !verificaNegacao){		
					negacao.get(i).add(false);
				}
				if(!words[j].equals("NOT")){
					verificaNegacao = false;
				}
				if(Character.toString(words[j].charAt(0)).equals("X")){
					int subscript = Integer.parseInt(words[j].replace("X", ""));
					Xi.get(i).add(subscript);
				}
			}
		}
	}

	public boolean verificaVerdadeiro(String certicicacao){
		certicicacao=certicicacao.toUpperCase();
                //REMOVE OS PARÊNTESE
		certicicacao=certicicacao.replace("(", "");
		certicicacao=certicicacao.replace(")", "");

		//VERIFICA A CERTIFICAÇÃO
                //ADICIONA UMA VÍRGULA À DIREITA PARA QUE CADA TAREFA POSSA SER DIVIDIDA NAS VÍRGULAS
		certicicacao+=",";
		String[] literalAssignmentPairs=certicicacao.split(",");
		for(int i=0; i<literalAssignmentPairs.length; i++){
                        //REMOVE OS ESPAÇOS EM BRANCO INICIAIS E QUALQUER ESPAÇO EM POTENCIAL
			literalAssignmentPairs[i]=literalAssignmentPairs[i].trim();

                        //Xi=k EM [Xi, k]
			String[] literalAndItsAssignment = literalAssignmentPairs[i].split("=");
                        //CAPTURA O VALOR DE i REMOVENDO O "X"
			int subscript = Integer.parseInt( literalAndItsAssignment[0].replace("X", "") );

                        //ASSUME QUE SE TRATA DE UM VALOR VERDADEIRO
			boolean valueOfLiteral = true;	
                        //SE Xi=0, ENTÃO O VALOR SERÁ FALSO
			if(literalAndItsAssignment[1].equals("0")){
				valueOfLiteral = false;
			}
                        //ATUALIZA O MAPA DE VALORES DE Xi E OS LITERAIS PRESENTES
			X.put(subscript, valueOfLiteral);
		}

		//AGORA SERÁ EFETUADA A VERIFICAÇÃO
                //INICIA A VERIFICAÇÃO ASSUMINDO QUE SE TRATA DE UM VALOR VÁLIDO, E PARA CASO UMA CLÁUSULA SEJA FALSA
		boolean verificaVerdadeiro= true;
		for(int clause=0; clause<clausulas.length; clause++){
                    //INICIA ASSUMINDO QUE A CLÁUSULA É INVÁLIDA E PARA QUANDO UM VALOR VERDADEIRO FOR ENCONTRADO
			boolean isClauseValid = false;	
			for(int j=0; j<NUMERO_LITERAIS_CLAUSULA; j++){
                            //ISSO SIGNIFICA QUE UMA VALOR FALSO ESTÁ PRESENTE ANTES DE UM LITERAL
				if(negacao.get(clause).get(j)){	
                                    isClauseValid = isClauseValid  ||  ! X.get(Xi.get(clause).get(j));
				}
				else{	
                                    isClauseValid = isClauseValid  ||  X.get(Xi.get(clause).get(j));
				}
				if(isClauseValid){
					break;
				}
			}
			verificaVerdadeiro = verificaVerdadeiro && isClauseValid;
			if(!verificaVerdadeiro){
                            break;
			}
		}
		return verificaVerdadeiro;
	}
}