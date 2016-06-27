
public class ReporteError {
	private int numTupla;
	private int numAtributo;
	private String error;
	public ReporteError(int numTupla, int numAtributo, String error)
	{
		setNumTupla(numTupla);
		setNumAtributo(numAtributo);
		setError(error);
	}
	public int getNumTupla() {
		return numTupla;
	}
	public void setNumTupla(int numTupla) {
		this.numTupla = numTupla;
	}
	public int getNumAtributo() {
		return numAtributo;
	}
	public void setNumAtributo(int numAtributo) {
		this.numAtributo = numAtributo;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String toString()
	{
		return ("Tupla: " + getNumTupla() + "\tAtributo: " + getNumAtributo() +
				" Error: " + getError());
	}
	public boolean equals(Object otro)
	{
		return getNumAtributo() == ((ReporteError)otro).getNumAtributo() &&
				getNumTupla() == ((ReporteError)otro).getNumTupla();
	}
}
