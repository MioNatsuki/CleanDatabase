import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class ManejadorArchivoTxt {
	private PrintWriter out;
	private File archivo;
	public ManejadorArchivoTxt(String nombreArchivo)
	{
		archivo=new File(nombreArchivo + ".txt");
		try{
			out=new PrintWriter(new FileWriter(archivo));
		}catch(IOException e){
			System.out.println("no se inicializo PrintWriter");
		}
	}
	//Siempre cierra el archivo
	public void cerrarArchivo()
	{
		out.close();
	}
	//No imprime salto de linea al final
	public void escribir(String cadena)
	{
		out.print(cadena);
	}
	//Imprime salto de linea al final
	public void escribirLn(String cadena)
	{
		out.println(cadena);
	}
}
