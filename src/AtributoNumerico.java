import java.util.ArrayList;
import java.util.List;


public class AtributoNumerico {
	private int numAtributo;
	private float media;
	private float mediana;
	private ArrayList<Float> moda;
	private float desviacionEstandar;
	private float maximo;
	private float minimo;
	public AtributoNumerico(int numAtributo)
	{
		setNumAtributo(numAtributo);
	}
	public void calcularDatosAtributo(List<Dato> listaDatos)
	{
		float min = 0, max = 0, total = 0;
		ArrayList<Float> moda = new ArrayList<>();
		int maxRepeticiones = 0, cantOcurrido, cantTotalValores = 0, tamanioLista = listaDatos.size();
		boolean primerVuelta = true;
		for(Dato dato: listaDatos)
		{
			float valor = Float.parseFloat(dato.getDato());
			//En la primer vuelta se asignan los valores de ese dato para comparar a partir de ahi
			if(primerVuelta){
				max = valor;
				min = valor;
				maxRepeticiones = dato.getCantOcurrido();
				moda.add(valor);
				primerVuelta = false;
			}
			else{
				//Compara si es el valor mas pequeño salido hasta el momento
				if(valor < min)
				{
					min = valor;
				}
				//Compara si es el valor mas grande salido hasta el momento
				else if(valor > max)
				{
					max = valor;
				}
				//Compara si el dato tiene mas repeticiones que la moda actual
				cantOcurrido = dato.getCantOcurrido();
				if(cantOcurrido > maxRepeticiones)
				{
					moda.clear();
					moda.add(valor);
					maxRepeticiones = cantOcurrido;
				}
				else if(cantOcurrido == maxRepeticiones)
				{
					moda.add(valor);
				}
			}
			total += (valor * dato.getCantOcurrido());
			cantTotalValores += dato.getCantOcurrido();
		}
		//Asigno los datos que ya tengo
		setMaximo(max);
		setMinimo(min);
		//Calcular los demas
		asignarModa(moda, tamanioLista);
		setMedia(total/cantTotalValores);
		asignarMediana(listaDatos, tamanioLista, cantTotalValores);
		asignarDesviacionEstandar(listaDatos, cantTotalValores);
	}
	public void asignarDesviacionEstandar(List<Dato> listaDatos, int cantTotalValores)
	{
		float totalCuadrados = 0;
		for(Dato dato: listaDatos)
		{
			float diferencia = (getMedia() - Float.parseFloat(dato.getDato()));
			totalCuadrados += ((diferencia * diferencia) * dato.getCantOcurrido());
		}
		setDesviacionEstandar((float)Math.sqrt(totalCuadrados / cantTotalValores));
	}
	public void asignarModa(ArrayList<Float> moda, int cantValoresDiferentes)
	{
		//Comprobar si hubo elementos repetidos para que la moda no incluya todos los elementos
		if(moda.size() == cantValoresDiferentes)
		{
			//No hay moda si hay el mismo numero de elementos en la lista que en la moda
			moda.clear();
		}
		setModa(moda);
	}
	public void asignarMediana(List<Dato> listaDatos, int cantValoresDiferentes, int cantTotalValores)
	{
		//Calculo de la mediana
		if(cantTotalValores % 2 == 0)
		{
			int indiceRespuesta1 = 0, indiceRespuesta2 = 0;
			for(int valoresPasados = 0, segundoIndiceBuscado = cantTotalValores / 2, primerIndiceBuscado = segundoIndiceBuscado - 1;
					valoresPasados < segundoIndiceBuscado; indiceRespuesta2++)
			{
				if(valoresPasados <= primerIndiceBuscado)
				{
					indiceRespuesta1 = indiceRespuesta2; 
				}
				valoresPasados += listaDatos.get(indiceRespuesta2).getCantOcurrido();
			}
			float numMedio1 = Float.parseFloat(listaDatos.get(indiceRespuesta1).getDato());
			float numMedio2 = Float.parseFloat(listaDatos.get(indiceRespuesta2).getDato());
			setMediana((numMedio1 + numMedio2) / 2);
		}
		else
		{
			int indiceRespuesta = 0;
			for(int valoresPasados = 0, indiceMedioBuscado = cantTotalValores / 2;
					valoresPasados < indiceMedioBuscado; indiceRespuesta++)
			{
				valoresPasados += listaDatos.get(indiceRespuesta).getCantOcurrido();
			}
			setMediana(Float.parseFloat(listaDatos.get(indiceRespuesta).getDato()));
		}
	}
	public int getNumAtributo() {
		return numAtributo;
	}
	public void setNumAtributo(int numAtributo) {
		this.numAtributo = numAtributo;
	}
	public float getMedia() {
		return media;
	}
	public void setMedia(float media) {
		this.media = media;
	}
	public float getMediana() {
		return mediana;
	}
	public void setMediana(float mediana) {
		this.mediana = mediana;
	}
	public ArrayList<Float> getModa() {
		return moda;
	}
	public void setModa(ArrayList<Float> moda) {
		this.moda = moda;
	}
	public float getDesviacionEstandar() {
		return desviacionEstandar;
	}
	public void setDesviacionEstandar(float desviacionEstandar) {
		this.desviacionEstandar = desviacionEstandar;
	}
	public float getMaximo() {
		return maximo;
	}
	public void setMaximo(float maximo) {
		this.maximo = maximo;
	}
	public float getMinimo() {
		return minimo;
	}
	public void setMinimo(float minimo) {
		this.minimo = minimo;
	}
	@Override
	public String toString() {
		String cadena = "";
		cadena += "-----------------------------\n";
		cadena +="Datos estadisticos\n";
		cadena += "-----------------------------\n";
		cadena += "Media : " + getMedia() + "\n";
		cadena += "Mediana : " + getMediana()+ "\n";
		cadena += "Moda : " + getModa()+ "\n";
		cadena += "Maximo : " + getMaximo()+ "\n";
		cadena += "Minimo : " + getMinimo()+ "\n";
		cadena += "Desviacion estandar : " + getDesviacionEstandar()+ "\n";
		return cadena;
	}
	//Devuelve cierto si el numero de atributo de los dos coincide
	public boolean equals(Object obj) { 
		return getNumAtributo() == ((AtributoNumerico)obj).getNumAtributo();
	}
}
