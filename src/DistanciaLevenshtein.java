
public class DistanciaLevenshtein implements Comparable<DistanciaLevenshtein>{
	private int numAtributo;
	private String cadena1;
	private String cadena2;
	private int distancia;
	public DistanciaLevenshtein(int numAtributo, int distancia, String cadena1, String cadena2)
	{
		setDistancia(distancia);
		setCadena2(cadena2);
		setCadena1(cadena1);
	}
	public int getNumAtributo()
	{
		return numAtributo;
	}
	public void setNumAtributo(int numAtributo)
	{
		this.numAtributo = numAtributo;
	}
	public String getCadena1() {
		return cadena1;
	}
	public void setCadena1(String cadena1) {
		this.cadena1 = cadena1;
	}
	public int getDistancia() {
		return distancia;
	}
	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	public String getCadena2() {
		return cadena2;
	}
	public void setCadena2(String cadena2) {
		this.cadena2 = cadena2;
	}
	@Override
	public String toString() {
		return "Distancia entre palabra " + getCadena1() + " y palabra " 
	+ getCadena2() + " es de " + getDistancia() + "\n";
	}
	@Override
	public int compareTo(DistanciaLevenshtein o) {
		return Integer.compare(getDistancia(), o.getDistancia());
	}
}
