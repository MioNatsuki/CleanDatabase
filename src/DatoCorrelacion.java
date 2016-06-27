
public class DatoCorrelacion implements Comparable<DatoCorrelacion>{
	private String atributo1;
	private String atributo2;
	private String tipo;
	//Aqui se pondra en caso de ser nominal el grado de liberetad
	//En caso de ser nominal se pondra el chi-cuadrado, en caso contrari el coeficiente de correlacion
	private double datoCorrelacion;
	//Tipo puede ser numerico o nominal
	private String detalles;
	public DatoCorrelacion(String atributo1, String atributo2, String tipo, double datoCorrelacion, String detalles)
	{
		setAtributo1(atributo1);
		setAtributo2(atributo2);
		setTipo(tipo);
		setDatoCorrelacion(datoCorrelacion);
		setDetalles(detalles);
	}
	public DatoCorrelacion(String atributo1, String atributo2, String tipo, double datoCorrelacion)
	{
		setAtributo1(atributo1);
		setAtributo2(atributo2);
		setTipo(tipo);
		setDatoCorrelacion(datoCorrelacion);
		setDetalles(" Coeficiente de correlacion: ");
	}
	public String getAtributo1() {
		return atributo1;
	}
	public void setAtributo1(String atributo1) {
		this.atributo1 = atributo1;
	}
	public String getAtributo2() {
		return atributo2;
	}
	public void setAtributo2(String atributo2) {
		this.atributo2 = atributo2;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public double getDatoCorrelacion() {
		return datoCorrelacion;
	}
	public void setDatoCorrelacion(double datoCorrelacion) {
		this.datoCorrelacion = datoCorrelacion;
	}
	public String getDetalles() {
		return detalles;
	}
	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}
	public String toString() {
		return getAtributo1() + " y  " + getAtributo2() + getDetalles() + getDatoCorrelacion() + "\n";
	}
	@Override
	public boolean equals(Object x)
	{
		DatoCorrelacion otro = (DatoCorrelacion)x;
		return (getAtributo1().equals(otro.getAtributo1()) && getAtributo2().equals(otro.getAtributo2()))
				|| (getAtributo2().equals(otro.getAtributo1()) && getAtributo1().equals(otro.getAtributo2()));
	}
	@Override
	public int compareTo(DatoCorrelacion o) {
		//Como -1 y 1 son muy significativos, se usara el valor absoluto
		//A los valores de chicuadrada no afectara porque no dan valores negativos
		double primerDato = getDatoCorrelacion();
		double segundoDato = o.getDatoCorrelacion();
		return Double.compare(Math.abs(primerDato), Math.abs(segundoDato));
	}
	
} 
