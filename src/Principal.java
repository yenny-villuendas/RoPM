import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * 
 */

/**
 * @author ALIENWARE
 *
 */
public class Principal {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		  RoPM ropm = new RoPM();
			
			System.out.println("RoPM");
			System.out.println("The only one argument is the input file name!");
			
			int cantA=args.length;
			int [] TTr=new int [cantA];
			double [] Tr=new  double [cantA];
			
			for (int k = 0; k < cantA; k++)	{		
			   String ifilename = args[k];
			   System.out.println(ifilename);
		       BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(ifilename)));
	           int filas = Integer.parseInt(in.readLine());
	           int columnas = Integer.parseInt(in.readLine());
	           int[][] matriz = new int[filas][columnas];
	           String linea;
	           for(int i = 0;i < filas;i++) {
		           linea = in.readLine();
		            String[] valores = linea.split(" ");
		            for(int j = 0; j < valores.length;j++)
		             matriz[i][j]  = Integer.parseInt(valores[j]);
		       }
	   
	           in.close();
	         
	           int tt =0;
	           Vector<Integer> P_masc = new Vector<Integer>();
	   
	           Vector<Integer> indices_resp= new Vector<Integer>();;
	           Vector<Integer> n_repeticiones= new Vector<Integer>();;
	   
	           long startTime = System.currentTimeMillis();
	   
	           Object[] listado = new Object[3];
	           listado = ropm.RedCols(matriz);
	           matriz =(int[][])listado[0];
	           indices_resp = (Vector<Integer>)listado[1];
	           n_repeticiones = (Vector<Integer>)listado[2];
	           
	           Object[] mc= new Object[0];
	           int[][] mct = ropm.TrasposeMT(matriz);
	           
	            tt = ropm.Rec_overPosMatriz(mct, matriz,tt,P_masc,indices_resp,n_repeticiones,mc);
	            long endTime = System.currentTimeMillis();
	            TTr[k]=tt;
	            Long ll=endTime-startTime;
	            double Tim=ll.doubleValue();
	            Tr[k]=Tim/1000;
	         }
			 System.out.println("time (s): ");
			 for (int k = 0; k < cantA; k++) {
	            System.out.printf("%.3f", Tr[k]);
	            System.out.print("\t");
			 }
			 
			 System.out.println("\n"+"Testores Tipicos: ");
			 for (int k = 0; k < cantA; k++)
	            System.out.print(TTr[k]+ "\t");   

	}

}
