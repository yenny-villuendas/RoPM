import java.util.Vector;


/**
 * 
 */

/**
 * @author ALIENWARE
 *
 */
public class RoPM {

	/**
	 * 
	 */
	
	 
	 
	public RoPM() {
		
	}
	
/////////////////////////////////////////////////////////
	public int Rec_overPosMatriz(int [][] mct,int [][] mt, int tt, Vector<Integer> P_masc, Vector<Integer> i_rep, Vector<Integer> n_rep, Object[] mc)
	{   
		int f = mt.length;
		int c = mt[0].length;
		int[] resp = FuncAnd(mt);
		Vector<Integer> indresp = VectorIndices(resp);	
		
		int su = indresp.size();
		boolean flag = false;
		if(su>0)
		{
			for (int i = 0; i < su; i++) {
				CompletaColumnaconValor(indresp.get(i), 0, mt);			
			}
			flag = FilaTodos0(mt, f, c);
			
			if(!P_masc.isEmpty()){
				int suma = 0;
				for (int i = su-1; i >=0 ; i--) {
					int ind=indresp.get(i);
					int[] Vr = mct[ind];
					boolean ff= SCEr(mc, Vr);
					if(ff) 
						suma++;
					else
						indresp.remove(i);
				}
				if(suma > 0){
					if(i_rep.isEmpty())
						tt+= suma;
					else
						tt+= CheqColRep(suma, i_rep, n_rep, P_masc, indresp);
				}
			}
			else       
				tt+= su;	
		}
		if(!flag)
		{
			Vector<Object> resultados_ordMat = OrdMat(mt);
			mt = (int[][])resultados_ordMat.get(1);
			Vector<Integer> ind_m = (Vector<Integer>)resultados_ordMat.get(0);
		
			for (int i = 0; i < ind_m.size(); i++) {
				Vector<Integer> A_masc = (Vector<Integer>)P_masc.clone();
				int ia=ind_m.get(i);
				Object[] nmc= Cloneobj(mc);
				int [] Via = mct[ia];
				boolean ff = SCEm1(nmc, Via);
				if(ff) {
					int [] ncol= SCEm2(mct, P_masc, ia);
					nmc= AddVect_aObj(nmc, ncol);
					A_masc.add(ia);
					int[][] nMB = DevMatColInd(mt, ia);
					if(i > 0)
						for (int j = 0; j < i; j++) 
							nMB = CompletaColumnaconValor(ind_m.get(j), 0, nMB);						
											
					tt= Rec_overPosMatriz(mct, nMB,tt,A_masc, i_rep, n_rep, nmc);
				}
			}	
		}
		return tt;
	}
////////////////////////////////////////////////////////////
	public int[] FuncAnd(int [][] arg_matriz)
	{
		int[] resultado = new int[arg_matriz[0].length];
		boolean salida;
		
		for (int i = 0; i < resultado.length; i++) {
			resultado[i] = arg_matriz[0][i];
		}
		
		
		for (int i = 1; i < arg_matriz.length; i++) {
			salida = true;
			for (int j = 0; j < resultado.length; j++) {
				
				if(resultado[j] == 1 && arg_matriz[i][j] == 1) {
					resultado[j] = 1;
					salida = false;					
				}					
				else
					resultado[j] = 0;				
			}
			if(salida)
				return resultado;
		}		
		return resultado;
	}
//////////////////////////////////////////////////////////////////////	
	public int CheqColRep(int l_r, Vector<Integer> ind_rep, Vector<Integer> num_rep, Vector<Integer> p_mas, Vector<Integer> resp)
	{
		int[] v_mult = new int[l_r];
		v_mult = this.InicializaenParametro(v_mult, 1);
		Vector<Integer> cind_rep = (Vector<Integer>)ind_rep.clone();
		Vector<Integer> cnum_rep = (Vector<Integer>)num_rep.clone();
		Vector<Integer> cresp = (Vector<Integer>)resp.clone();
		
		for (int i = 0; i < p_mas.size(); i++) {
			for (int j = 0; j < cind_rep.size(); j++) {
				if(p_mas.get(i) == cind_rep.get(j)) {
					v_mult = this.MultiplicaxEntero(v_mult, num_rep.get(j));
					cind_rep.removeElementAt(j);
					cnum_rep.removeElementAt(j);
					break;
				}
					
			}
		}
		
		for (int i = 0; i < l_r; i++) {
			for (int j = 0; j < cind_rep.size(); j++) {
				if(cresp.get(i) == cind_rep.get(j))
				{
					v_mult[i] *= cnum_rep.get(j); 
					cind_rep.removeElementAt(j);
					cnum_rep.removeElementAt(j);
					break;
				}
					
			}
			
		}
			int suma = 0;
		for (int i = 0; i < v_mult.length; i++) {
			suma+= v_mult[i];
		}
		return suma;
	}
//////////////////////////////////////////////////////////////////////	
	public int[] MultiplicaxEntero(int[] arg_vector, int entero)
	{
		for (int i = 0; i < arg_vector.length; i++) {
			arg_vector[i]*= entero;
		}
		
		return arg_vector;
	}
//////////////////////////////////////////////////////////////////////
	public Vector<Object> OrdMat(int[][] arg_matriz) 
	{
		Vector<Integer> vector_menores = new Vector<Integer>();
	    Vector<Object> resultado = new Vector<Object>(2);
		int suma1 =0;
		int suma_primero = 0;
		for (int i = 0; i < arg_matriz.length; i++) { 
				
					suma1 = Cant1FilaI(i,arg_matriz);
				if(!vector_menores.isEmpty())
				{
					if(suma1 < suma_primero)
					{
						vector_menores.clear();
						vector_menores.addElement(i);
						suma_primero = suma1;
						
					}
					else if(suma1 == suma_primero)
					{
						vector_menores.addElement(i);
					}
				}
				else
				{
					vector_menores.addElement(i);
					suma_primero = suma1;
				}			
		}
		
		Vector<Integer> sumasxposiciones = new Vector<Integer>(vector_menores.size()) ;   
		Vector<Integer> sumasxposiciones_final = new Vector<Integer>(vector_menores.size()) ;  
		
		Vector<Integer> ind_sumasxposiciones = new Vector<Integer>(vector_menores.size()) ;   
		Vector<Integer> ind_sumasxposiciones_final = new Vector<Integer>(vector_menores.size()) ;   
		int cant_cols = arg_matriz[0].length;
		int sumatoria = 0; 
		
		
		if(vector_menores.size() == 1)
		{
			int[] temp = arg_matriz[0];
			arg_matriz[0] = arg_matriz[vector_menores.get(0)];
			arg_matriz[vector_menores.get(0)] = temp;	
			
			for (int j = 0; j < cant_cols; j++) {
				if(arg_matriz[0][j]== 1)
				{
					int unoscoli = Cant1ColI(j, arg_matriz);
					sumatoria+= unoscoli;
					sumasxposiciones.add(unoscoli);
					ind_sumasxposiciones.add(j);
				}
			}
			sumasxposiciones_final  = sumasxposiciones;	
			ind_sumasxposiciones_final = ind_sumasxposiciones;
		}
		else
		{
			
			int mayor_suma =0;
			int pos_filaMenor = vector_menores.get(0);
			
			
			for (int i = 0; i < vector_menores.size(); i++) {
				
				int [] momento = arg_matriz[vector_menores.get(i)];
				sumasxposiciones.clear();
				ind_sumasxposiciones.clear();
				for (int j = 0; j < cant_cols; j++) {					
					if(momento[j]== 1)
					{
						int unoscoli = Cant1ColI(j, arg_matriz);
						sumatoria+= unoscoli;
						sumasxposiciones.add(unoscoli);
						ind_sumasxposiciones.add(j);
					}
				}
				if( sumatoria > mayor_suma ) {
					mayor_suma = sumatoria;
					pos_filaMenor = i;
					
					
					sumasxposiciones_final = (Vector<Integer>)sumasxposiciones.clone();
					ind_sumasxposiciones_final = (Vector<Integer>)ind_sumasxposiciones.clone();
				}
				sumatoria = 0;
				
			}
			
			
			
			int[] temp = arg_matriz[0];
			arg_matriz[0] = arg_matriz[vector_menores.get(pos_filaMenor)];
			arg_matriz[vector_menores.get(pos_filaMenor)] = temp;		
		}
		
		
		for (int i = 0; i < ind_sumasxposiciones_final.size(); i++) {
			for (int j = i+1; j < ind_sumasxposiciones_final.size(); j++) {
				if(sumasxposiciones_final.get(j) > sumasxposiciones_final.get(i))
				{
					
					int temp1 = sumasxposiciones_final.get(j);
					sumasxposiciones_final.set(j, sumasxposiciones_final.get(i));
					sumasxposiciones_final.set(i, temp1);
					
					int temp2 = ind_sumasxposiciones_final.get(j);
					ind_sumasxposiciones_final.set(j, ind_sumasxposiciones_final.get(i));
					ind_sumasxposiciones_final.set(i, temp2);					
				}
			}
		}
		resultado.add(ind_sumasxposiciones_final);
		resultado.add(arg_matriz);
		return resultado;		
	}
//////////////////////////////////////////////////////////////////////	
	public Object[] RedCols(int [][] arg_matriz)
	{
		Object[] resultado = new Object[3];
		Vector<Integer> indices_rep = new Vector<Integer>();
		Vector<Integer> cant_rep = new Vector<Integer>();
		
		int[] comprueba_rep = new int[arg_matriz[0].length];
		int suma_repeticiones = 0;
		
		for (int i = 0; i < arg_matriz[0].length; i++) {
			for (int j = 0; j < arg_matriz.length; j++) {
				if(arg_matriz[j][i]!= 0)
					break;
				if(j == arg_matriz.length-1)
					comprueba_rep[i] = 1;

			}
		}
		
		for (int i = 0; i < arg_matriz[0].length-1; i++) {
			if(comprueba_rep[i]!= 1)
			{
				suma_repeticiones = 0;
				for (int j = i+1; j < arg_matriz[0].length; j++) {
					if(comprueba_rep[j]!= 1) {
						for (int j2 = 0; j2 < arg_matriz.length; j2++) {
							if(arg_matriz[j2][i] != arg_matriz[j2][j])
								break;
							if(j2 == arg_matriz.length-1)
							{
								comprueba_rep[j] = 1;
								suma_repeticiones++;

							}
						}
					}

				}
				
				if(suma_repeticiones > 0)
				{			
					int sumatemp = 0;
					for (int k = 0; k < i; k++) {
						sumatemp+= comprueba_rep[k];
							
					}
					indices_rep.add(i-sumatemp);
					cant_rep.add(++suma_repeticiones);
				}
			}	
		}
		int cant1 = 0;
		for (int i = 0; i < comprueba_rep.length; i++) 
			if(comprueba_rep[i]== 1)
				cant1++;
		
		int nuevascols = arg_matriz[0].length - cant1;
		int filas = arg_matriz.length;
		int[][] salida = new int[filas][nuevascols];
		int cont = 0;
		for (int i = 0; i < comprueba_rep.length; i++) {
			if(comprueba_rep[i]== 0)
			{
				for (int j = 0; j < filas; j++) 
					salida[j][cont] = arg_matriz[j][i];
				cont++;
			}
		}
		resultado[0] = salida;
		resultado[1] = indices_rep;
		resultado[2] = cant_rep;
		return resultado;
	}
//////////////////////////////////////////////////////////////////////
	public boolean FilaTodos0(int[][] mat, int arg_cant_filas, int arg_cant_cols)
	{
		boolean salida = false;
		int cant_filas = arg_cant_filas;
		int cant_cols =  arg_cant_cols;
		
		for (int i = 0; i < cant_filas; i++) { 
			if(salida)
				break;
			for (int j = 0; j < cant_cols; j++) {
				if(mat[i][j] == 1)
					break;
				else if(j == cant_cols-1)
					salida = true;
			}
		}
		return salida;
	}
//////////////////////////////////////////////////////////////////////	
	public int Cant1FilaI(int arg_indice, int[][] arg_matriz) { 
		int sumatoria = 0;
		for(int i = 0; i< arg_matriz[0].length;i++) 
			sumatoria+= arg_matriz[arg_indice][i];		
		return sumatoria;		
	}
//////////////////////////////////////////////////////////////////////	
	public int Cant1ColI(int arg_indice, int[][] arg_matriz)
	{ 
		int sumatoria = 0;
		for(int i = 0; i< arg_matriz.length;i++) 
			sumatoria+= arg_matriz[i][arg_indice];		
		return sumatoria;		
	}	
//////////////////////////////////////////////////////////////////////
	public Vector<Integer> VectorIndices(int[] arg_vector)
	{
		Vector<Integer> indices = new Vector<Integer>();
		for (int i = 0; i < arg_vector.length; i++)
			if(arg_vector[i] == 1) {
				indices.add(i);
			}
		return indices;
	}
//////////////////////////////////////////////////////////////////////
	public int[] InicializaenParametro(int[] arg_vector, int valor)
	{
		for (int i = 0; i < arg_vector.length; i++) 
			arg_vector[i] = valor;
		
		return arg_vector;
	}
//////////////////////////////////////////////////////////////////////	
	public int[][] CopyAtoB(int[][] A, int [][] B)
	{
		for (int i = 0; i < A.length; i++) 
			for (int j = 0; j < A[0].length; j++) 
				B[i][j] = A[i][j];
		return B;
	}	
//////////////////////////////////////////////////////////////////////	
	public int[][] DevuelveColsxIndices(Vector<Integer> arg_vector, int[][] arg_matriz) 
	{
		
		int[][] matb = new int[arg_matriz.length][arg_vector.size()];
		
		for (int i = 0; i < arg_vector.size(); i++) 
			for (int j = 0; j < matb.length; j++) 
				matb[j][i] = arg_matriz[j][arg_vector.get(i)];		 
		return matb;
	}
//////////////////////////////////////////////////////////////////////	
	public int[][] CompletaColumnaconValor(int arg_indice, int valor, int[][] arg_matriz)
	{
		for (int i = 0; i < arg_matriz.length; i++) 
			arg_matriz[i][arg_indice] = valor;
		return arg_matriz;
	}
//////////////////////////////////////////////////////////////////////	
	public int Cant1Vector(int[] arg_vect)
	{
		int suma=0;
		for (int i = 0; i < arg_vect.length; i++) 
			suma+= arg_vect[i];
		
		return suma;	
	}
//////////////////////////////////////////////////////////////////////
	public int[][] DevMatColInd(int[][] arg_matriz, int indice)
	{
		Vector<Integer> vector_indicenm = new Vector<Integer>();
				
		for (int j = 0; j < arg_matriz.length; j++) {
					if(arg_matriz[j][indice] == 0)
						vector_indicenm.add(j);
				}					
		
		int filas = vector_indicenm.size();
		int columnas = arg_matriz[0].length;
		int[][] matriz_salida = new int[filas][columnas];
		for (int i = 0; i < filas; i++) {
			int[] filai = DevuelveFilaxIndice(arg_matriz, vector_indicenm.get(i));
			matriz_salida[i] = filai.clone();			
		}
		return matriz_salida;
	}
//////////////////////////////////////////////////////////////////////
	public int[] DevuelveFilaxIndice(int[][] arg_matriz, int arg_indice)
	{
		int[] resultado = new int[arg_matriz[0].length];
		for (int i = 0; i < arg_matriz[0].length; i++) 
			resultado[i] = arg_matriz[arg_indice][i];
		return resultado;
	}	
//////////////////////////////////////////////////////////////////////
	public boolean ChequeaNMT(int indice1, int indice_fila, int [][] arg_matriz, int cant_cols) 
	{
		boolean flag = true;
		for (int i = 0; i < cant_cols; i++) {
			if(arg_matriz[indice_fila][i] == 1 && i!=indice1)
				return false;
		}
		return flag;
	}
//////////////////////////////////////////////////////////////////////	
    public boolean SCEr(Object[] AO, int [] Av)
    {
    	int no=AO.length;
    	int nc=Av.length;
		boolean f=true;
		for (int i = 0; i < no; i++) {
			int [] vect = (int []) AO[i];
			for (int j = 0; j < nc; j++) {
				if(vect[j]==1 && Av[j]==0)
					break;
				if(j==nc-1)
					return false;
			}
		}
	  return f;		
    }
///////////////////////////////////////////////////////////////////////////////////////    
    public boolean SCEm1(Object[] AO, int [] Via)
	{
    	boolean f=true;
    	int cf=Via.length;
		for (int i = 0; i < AO.length; i++) {
			int [] vect = (int [])AO[i];
			for (int j = cf-1; j >= 0; j--) {
				if(Via[j]==1 && vect[j]==1)
			    	 vect[j]=0;
			}
			if(IfVectT0(vect))
			return false;	
		}
		return f;
	}
/////////////////////////////////////////////////////////////////////////////////////    
    public int [] SCEm2(int [][] Am, Vector<Integer> masc, int ind)
	{   
		int [] ve = Am[ind];
	    int [] vec = ve.clone();
		if(!masc.isEmpty()) {
			int nc=masc.size();
			for (int i = vec.length-1; i >=0; i--) {
				if(vec[i]==1) {
				   for (int j = 0; j < nc; j++) {
					    int [] vm = Am[masc.get(j)];
					    if(vm[i]==1) {
					       vec[i]=0;
						   break;
					    }
				   }
				}
			}
		}
		return vec;
	}
///////////////////////////////////////////////////////////////////////////////   
    public Object[] AddVect_aObj(Object[] ArgO, int [] ArgV)
	{   
		int l=ArgO.length;
		Object[] R = new Object[l+1];
		for (int i = 0; i < l ; i++) 
			   R[i]=ArgO[i];
		R[l]=ArgV;   
		return R;   
	}
 //////////////////////////////////////////////////////////////////////////////////   
    public boolean IfVectT0(int[] arg_vect)
	{
    	boolean f=true;
		for (int i = 0; i < arg_vect.length; i++) 
			if(arg_vect[i]==1)
		       return false;
		return f;
	}
//////////////////////////////////////////////////////////////////////
    public Object[] Cloneobj(Object[] AO)
	{   
		int l=AO.length;
		Object[] RO = new Object[l];
		for (int i = 0; i < l ; i++) {
			   int[] temp = (int[]) AO[i];
			   RO[i]=temp.clone();
		}
		return RO;   
	}
 /////////////////////////////////////////////////////////////////////////////////////   
    public int [][] TrasposeMT(int [][] Am)
    {
    	int nf=Am.length;
    	int nc=Am[0].length;
    	int[][] Rm=new int[nc][nf];
    	for (int i = 0; i < nf; i++) {
    		for (int j = 0; j < nc; j++) {
    			Rm[j][i]=Am[i][j];
    		}
    	}
    	return Rm;
    }
  ///////////////////////////////////////////////////////////////////////////////////////
 ///////////////////////////////////////////////////////////////////////////////////////    
}
