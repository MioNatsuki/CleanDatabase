import java.util.ArrayList;


public class Tupla {
	private ArrayList<String> atributos;
	public Tupla(ArrayList<String> datos)
	{
		atributos = datos;
	}
	public void eliminarAtributo(int numAtributo)
	{
		atributos.remove(numAtributo);
	}
	public String getAtributo(int indiceAtributo)
	{
		return atributos.get(indiceAtributo);
	}
	public void setAtributo(String valor, int numAtributo)
	{
		atributos.set(numAtributo, valor);
	}
	public int cantAtributos()
	{
		return atributos.size();
	}
}
