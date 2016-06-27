import java.util.ArrayList;

public class Regla {
	private int numAtributo;
	private boolean esNumerica;
	private float porcentajeError;
	private ArrayList<ArrayList<String>> reglas;
	public Regla(ArrayList<ArrayList<String>> reglas, float porcentajeError, boolean esNumerica, int numAtributo)
	{
		setEsNumerica(esNumerica);
		setNumAtributo(numAtributo);
		setPorcentajeError(porcentajeError);
		setReglas(reglas);
	}
	
	public int getNumAtributo() {
		return numAtributo;
	}

	public void setNumAtributo(int numAtributo) {
		this.numAtributo = numAtributo;
	}

	public boolean isEsNumerica() {
		return esNumerica;
	}

	public void setEsNumerica(boolean esNumerica) {
		this.esNumerica = esNumerica;
	}

	public float getPorcentajeError() {
		return porcentajeError;
	}

	public void setPorcentajeError(float porcentajeError) {
		this.porcentajeError = porcentajeError;
	}

	public ArrayList<ArrayList<String>> getReglas() {
		return reglas;
	}

	public void setReglas(ArrayList<ArrayList<String>> reglas) {
		this.reglas = reglas;
	}

	public String toString()
	{
		String cadena = "";
		for(ArrayList<String> regla: reglas)
		{
			cadena += regla;
		}
		return cadena;
	}
}
