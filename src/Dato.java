
public class Dato implements Comparable<Dato>{
	private String dato;
	private int numAtributo;
	private int cantOcurrido;
	public Dato(String cadenaDato, int atributo)
	{
		setDato(cadenaDato);
		setNumAtributo(atributo);
		setCantOcurrido(1);
	}
	public void repetido()
	{
		setCantOcurrido(getCantOcurrido()+1);
	}
	public String getDato() {
		return dato;
	}
	public void setDato(String error) {
		this.dato = error;
	}
	public int getNumAtributo() {
		return numAtributo;
	}
	public void setNumAtributo(int atributo) {
		this.numAtributo = atributo;
	}
	public int getCantOcurrido() {
		return cantOcurrido;
	}
	public void setCantOcurrido(int cantOcurrido) {
		this.cantOcurrido = cantOcurrido;
	}
	@Override
	public boolean equals(Object x)
	{
		Dato y = (Dato)x; 
		return y.getDato().equals(getDato())&&y.getNumAtributo()==getNumAtributo();
	}
	public String toString()
	{
		return("El dato " + dato + " se repitio " + cantOcurrido +
				" veces en el atributo "+ numAtributo);
	}
	@Override
	//Compara primero el numero de atributo y si son iguales continua con el valor del atributo
	//Se utiliza para ordenar los datos
	public int compareTo(Dato o) {
		int comparacionNumAtributo = Integer.compare(getNumAtributo(), o.getNumAtributo());
		if(comparacionNumAtributo != 0)
		{
			return comparacionNumAtributo;
		}
		else{
			try{
				double numX , numY;
				numX = Double.parseDouble(getDato());
				numY = Double.parseDouble(o.getDato());
				return Double.compare(numX, numY);
			}
			catch(NumberFormatException ex)
			{
				return getDato().compareTo(o.getDato());
			}
		}
	}
}
