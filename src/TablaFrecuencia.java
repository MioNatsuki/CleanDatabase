import java.util.ArrayList;

public class TablaFrecuencia {
	private ArrayList<String> valoresClase;
	private ArrayList<String> valoresAtributo;
	private int matrizFrecuencia[][];
	private float tablaVerosimilitud[][];
	public TablaFrecuencia(ArrayList<String> valoresClase, ArrayList<String> valoresAtributo, int matrizFrecuencia[][])
	{
		setMatrizFrecuencia(matrizFrecuencia);
		setValoresAtributo(valoresAtributo);
		setValoresClase(valoresClase);
	}
	public Regla calcularRegla()
	{
		float[] porcentajeError = new float [valoresAtributo.size()];
		float numeradorErrorTotal = 0, denominadorErrorTotal = 0;
		ArrayList<ArrayList <String>> reglas = new ArrayList<>();
		for(int j = 0, cantAtributos = valoresAtributo.size(); j < cantAtributos; j++)
		{
			ArrayList<String> regla = new ArrayList<>();
			regla.add(valoresAtributo.get(j));
			String clasePredicha = valoresClase.get(0);
			int menor = matrizFrecuencia[j][0], cantValores = 0, mayor = matrizFrecuencia[j][0];
			for(int k = 0, cantValoresClase = valoresClase.size(); k < cantValoresClase; k++)
			{
				int temp = matrizFrecuencia[j][k];
				if(temp < menor)
				{
					menor = temp; 
				}
				else if(temp > mayor)
				{
					clasePredicha = valoresClase.get(k);
				}
				cantValores += temp;
			}
			porcentajeError[j] = (float)menor / (float)cantValores;
			numeradorErrorTotal += menor;
			denominadorErrorTotal += cantValores;
			regla.add(clasePredicha);
			reglas.add(regla);
		}
		float menor = porcentajeError[0];
		int numAtributo = 0, i = 0;
		for(float porcentaje: porcentajeError)
		{
			if(porcentaje < menor)
			{
				menor = porcentaje;
				numAtributo = i;
			}
			i++;
		}
		return new Regla(reglas, (numeradorErrorTotal/denominadorErrorTotal), false, numAtributo);
	}
	public void llenarTablaVerosimilitud()
	{
		tablaVerosimilitud = new float[valoresAtributo.size()][valoresClase.size()];
		for(int k = 0, cantValoresClase = valoresClase.size(); k < cantValoresClase; k++)
		{
			int numVuelta = 0, cantidadTotalColum = 0;
			while(numVuelta < 2)
			{
				for(int j = 0, cantAtributos = valoresAtributo.size(); j < cantAtributos; j++)
				{
					if(numVuelta == 0)
					{
						cantidadTotalColum += matrizFrecuencia[j][k];
						//cantidadTotalColum += (matrizFrecuencia[j][k] + 1);
					}
					else
					{
						tablaVerosimilitud[j][k] = (float)matrizFrecuencia[j][k] / (float)cantidadTotalColum;
						//tablaVerosimilitud[j][k] = (float)(matrizFrecuencia[j][k] + 1) / (float)cantidadTotalColum;
					}
				}
				numVuelta++;
			}
		}
	}
	
	public ArrayList<String> getValoresClase() {
		return valoresClase;
	}
	
	public void setValoresClase(ArrayList<String> valoresClase) {
		this.valoresClase = valoresClase;
	}
	
	public ArrayList<String> getValoresAtributo() {
		return valoresAtributo;
	}
	
	public void setValoresAtributo(ArrayList<String> valoresAtributo) {
		this.valoresAtributo = valoresAtributo;
	}
	
	public int[][] getMatrizFrecuencia() {
		return matrizFrecuencia;
	}
	
	public void setMatrizFrecuencia(int[][] matrizFrecuencia) {
		this.matrizFrecuencia = matrizFrecuencia;
	}
	public float[][] getTablaVerosimilitud()
	{
		return tablaVerosimilitud;
	}
	public String toString()
	{
		String cadena = "\t";
		for(String valorClase: valoresClase)
		{
			cadena += valorClase + "\t";
		}
		cadena += "\n";
		for(int j = 0, cantAtributos = valoresAtributo.size(); j < cantAtributos; j++)
		{
			cadena += valoresAtributo.get(j) + "\t";
			for(int k = 0, cantValoresClase = valoresClase.size(); k < cantValoresClase; k++)
			{
				cadena += matrizFrecuencia[j][k] + "\t";
			}
			cadena += "\n";
		}
		return cadena;
	}
}
