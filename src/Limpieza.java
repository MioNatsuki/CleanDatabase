import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Limpieza {
	static final int OPCION_NO_MODIFICAR_VALORES = -1;
	static final String NOMBRE_ARCHIVO_BASE_DATOS = "IncomeUsar";
	public static BufferedReader in;
	static{
		try{
			in = new BufferedReader(new FileReader(new File("Expresiones regularesUsar.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	List<Tupla> tuplas;
	List<ReporteError> listaErrores;
	//En datosDiferentes solo se guardan datos validos
	ArrayList<Dato> listaDatosDiferentes;
	List<Dato> listaErroresDiferentes;
	List<String> listaEncabezados;
	int cantAtributos;
	ArrayList<String> listaExpresionesRegulares;
	ArrayList<AtributoNumerico> listaAtributosNumericos;
	ArrayList<DatoCorrelacion> listaDeCorrelacionesNum;
	ArrayList<DatoCorrelacion> listaDeCorrelacionesNom;
	ArrayList<Integer> listaIndicesPrimerosPorAtributo;
	ArrayList<ArrayList<DistanciaLevenshtein>> listaDistanciasLeven;

	//llenarDatosCorrelaciones

	public Limpieza()
	{
		leerExcel(NOMBRE_ARCHIVO_BASE_DATOS + ".xls");
	}
	public boolean hacerCalculosTardados()
	{
		buscarErrores();
		ordenarListas();
		buscaIndicesPrimeroDeAtrib();
		calcularCorrelacion();
		return true;
	}
	public void buscarErrores()
	{
		listaErrores = new ArrayList<ReporteError>();
		listaDatosDiferentes = new ArrayList<Dato>();
		listaErroresDiferentes = new ArrayList<Dato>();
		int cantTuplas = tuplas.size();
		for(int i = 0; i < cantTuplas; i++)
		{
			Tupla tup = tuplas.get(i);
			for(int j = 0; j < cantAtributos; j++)
			{
				String valorAtributo = tup.getAtributo(j);
				if(estaVacio(valorAtributo))
				{
					listaErrores.add(new ReporteError(i,j, valorAtributo));
					comprobarDatoRepetidoEnLista(listaErroresDiferentes,valorAtributo, j);
				}
				else
				{
					//Comprueba si no es cadena valida
					if(!cumpleExpresionRegular(listaExpresionesRegulares.get(j), valorAtributo))
					{
						/*Si no es cadena valida agrega i=numero de tupla, j=numero de atributo,
						 * y valorAtributo=cadena que no fue valida*/ 
						listaErrores.add(new ReporteError(i, j, valorAtributo));
						comprobarDatoRepetidoEnLista(listaErroresDiferentes,valorAtributo, j);
					}
					else
					{
						comprobarDatoRepetidoEnLista(listaDatosDiferentes,valorAtributo, j);
					}
				}
				/*Se comprueba si es un dato ya repetido*/
			}
		}

	}
	public boolean estaVacio(String valorAtributo)
	{
		//Comprobaciones sobre las cadenas que se consideran como vacias
		return (valorAtributo.equals("") || valorAtributo.equals("?") ||
				valorAtributo.equals(" ?"));
	}
	public void comprobarDatoRepetidoEnLista(List<Dato> lista,String cadenaError, int numAtributoError)
	{
		Dato temp = new Dato(cadenaError,numAtributoError);
		if(lista.contains(temp))
		{
			int indiceDeErrorRepetido = lista.indexOf(temp);
			lista.get(indiceDeErrorRepetido).repetido();
		}
		else
		{
			lista.add(temp);
		}
	}
	public void guardarDatosYErrores()
	{
		ManejadorArchivoTxt archivo = new ManejadorArchivoTxt("Datos");
		int cantDatosDiferentes = listaDatosDiferentes.size();
		int cantErroresDiferentes = listaErroresDiferentes.size();
		int indiceAtributoNum;
		archivo.escribirLn("Lista de datos");
		//Para este for los datos ya se reciben ordenados
		for(int numAtributo = 0, indiceDatos = 0 , indiceErrores = 0;
				numAtributo < cantAtributos; numAtributo++)
		{
			archivo.escribirLn("**********************************");
			archivo.escribirLn("Atributo " + (numAtributo+1) + ": " + listaEncabezados.get(numAtributo));
			archivo.escribirLn("-----------------------------");
			archivo.escribirLn("Frecuencia \tDato");
			archivo.escribirLn("-----------------------------");
			for(; indiceDatos < cantDatosDiferentes; indiceDatos++)
			{
				Dato dato = listaDatosDiferentes.get(indiceDatos);
				if(dato.getNumAtributo()== numAtributo)
				{
					archivo.escribirLn(dato.getCantOcurrido()+ "\t\t" + dato.getDato());
				}
				else
				{
					break;
				}
			}
			//Devuelve -1 si el atributo no es numerico, ya que no se encontro dentro de la lista de atributos numericos
			if((indiceAtributoNum = listaAtributosNumericos.indexOf
					(new AtributoNumerico(numAtributo)))!= -1)
			{
				AtributoNumerico atributoNum = listaAtributosNumericos.get(indiceAtributoNum);
				archivo.escribirLn("-----------------------------");
				archivo.escribirLn("Datos estadisticos");
				archivo.escribirLn("-----------------------------");
				archivo.escribirLn("Media : " + atributoNum.getMedia());
				archivo.escribirLn("Mediana : " + atributoNum.getMediana());
				archivo.escribirLn("Moda : " + atributoNum.getModa());
				archivo.escribirLn("Maximo : " + atributoNum.getMaximo());
				archivo.escribirLn("Minimo : " + atributoNum.getMinimo());
				archivo.escribirLn("Desviacion estandar : " + atributoNum.getDesviacionEstandar());
			}
			archivo.escribirLn("-----------------------------");
			archivo.escribirLn("Cantidad \tError");
			archivo.escribirLn("-----------------------------");
			for(; indiceErrores < cantErroresDiferentes; indiceErrores++)
			{
				Dato error = listaErroresDiferentes.get(indiceErrores);
				if(error.getNumAtributo()== numAtributo)
				{
					archivo.escribirLn(error.getCantOcurrido()+ "\t\t" + error.getDato());
				}
				else
				{
					break;
				}
			}
			archivo.escribirLn("");
		}
		archivo.cerrarArchivo();
	}
	public void leerExcel(String nombreArchivo)
	{
		tuplas = new ArrayList<Tupla>();
		listaAtributosNumericos = new ArrayList<>();
		FileInputStream archivo = null;
		try{
			listaEncabezados = new ArrayList<String>();
			archivo = new FileInputStream(nombreArchivo);
			HSSFWorkbook workbook = new HSSFWorkbook(archivo);
			//Obtienes la primer hoja
			HSSFSheet hoja = workbook.getSheetAt(0);
			//Iterar por las filas
			Iterator<Row> filas = hoja.rowIterator();
			//Para guardar los encabezados aparte
			HSSFRow filaEncabezado = (HSSFRow) filas.next();
			short ultimaCelda = filaEncabezado.getLastCellNum();
			for(int i = 0; i < ultimaCelda ;i++){
				HSSFCell celdaEncabezado = (HSSFCell) filaEncabezado.getCell(i);
				listaEncabezados.add(celdaEncabezado.getStringCellValue());
			}
			leerExpresionesRegulares();
			//Se obtiene el numero de columnas que hay del numero de nombres de atributos
			cantAtributos = listaExpresionesRegulares.size();
			while(filas.hasNext()){
				//Esto es una fila
				HSSFRow fila = (HSSFRow) filas.next();
				/*En "datos" se guardaran todos los Strings de las celdas para al final
				 agregarlos todos a una sola tupla y esta misma agregarla a la lista de tuplas*/
				ArrayList<String> datos = new ArrayList<String>();
				boolean hayAtributoClase = true;
				for(int i = 0; i < cantAtributos ;i++){
					HSSFCell celda = (HSSFCell) fila.getCell(i);
					int tipoCelda;
					//Checa si la celda tiene algun dato
					if(celda != null)
					{
						//Obtiene el tipo de celda (el valor es un int)
						tipoCelda = celda.getCellType();
					}
					else
					{
						//Si la celda es nula le asigna el tipo de "Blank"
						tipoCelda = Cell.CELL_TYPE_BLANK;
					}
					//Si la celda es de tipo numerico
					if(tipoCelda == Cell.CELL_TYPE_NUMERIC)
					{
						/*celda.getNumericCellValue() devuelve float, por eso primero se castea a int
						 * despues se convierte a cadena y se agrega
						 */
						float valorCelda = (float) celda.getNumericCellValue();
						datos.add(String.valueOf((int) valorCelda));
						AtributoNumerico temp = new AtributoNumerico(i);
						if(!listaAtributosNumericos.contains(temp))
						{
							listaAtributosNumericos.add(temp);
						}
					}
					else if (tipoCelda == Cell.CELL_TYPE_STRING)
					{
						datos.add(celda.getStringCellValue().trim());
					}
					else if (tipoCelda == Cell.CELL_TYPE_BLANK)
					{
						//¿Que hacemos en caso de que sea nulo?
						datos.add("");
						if(i == cantAtributos - 1)
						{
							hayAtributoClase = false;
						}
					}
				}
				if(hayAtributoClase)
				{
					tuplas.add(new Tupla(datos));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (archivo != null)
			{
				try {
					archivo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public boolean cumpleExpresionRegular(String cadenaValida, String aComprobar)
	{
		//Expresion regular a comprobar
		Pattern pat = Pattern.compile(cadenaValida);
		Matcher match = pat.matcher(aComprobar);
		return match.matches();

	}
	@SuppressWarnings("deprecation")
	public void guardarExcel()
	{
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet hoja = workbook.createSheet();
		HSSFRow filaEncabezado = hoja.createRow(0);
		for(short i = 0; i<cantAtributos; i++)
		{
			HSSFCell celda = filaEncabezado.createCell(i, Cell.CELL_TYPE_STRING);
			celda.setCellValue(listaEncabezados.get(i));
		}
		//Valor 1 porque el valor 0 sera llenado con los encabezados
		int numFila = 1;
		for(Tupla tupla: tuplas){
			HSSFRow fila = hoja.createRow(numFila);
			for(short i = 0; i<cantAtributos; i++)
			{
				String valorCelda = tupla.getAtributo(i);
				//Expresion regular para numeros enteros y decimales
				if(cumpleExpresionRegular("([0-9]{1,})([.]{0,1})([0-9]{0,})", valorCelda))
				{
					HSSFCell celda = fila.createCell(i, Cell.CELL_TYPE_NUMERIC);
					celda.setCellValue(Float.parseFloat(valorCelda)); 
				}
				else if(valorCelda.equals(""))
				{
					//La celda no tiene nada por lo que no se agrega nada
				}
				else
				{
					HSSFCell celda = fila.createCell(i, Cell.CELL_TYPE_STRING);
					celda.setCellValue(valorCelda);
				}

			}
			numFila++;
		}
		try{
			FileOutputStream archivo = new FileOutputStream("Nuevo excel.xls");
			workbook.write(archivo);
			archivo.close();
			System.out.println("Nuevo excel con datos corregidos se guardo");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void guardarNuevosDatosTxt()
	{
		ManejadorArchivoTxt archivo = new ManejadorArchivoTxt("Nuevos datos");
		for(int i = 0, cantEncabezados = listaEncabezados.size(); i < cantEncabezados-1; i++)
		{
			archivo.escribir(listaEncabezados.get(i) + ",");
		}
		archivo.escribirLn(listaEncabezados.get(listaEncabezados.size()-1));
		for(int i = 0, cantTuplas = tuplas.size(); i < cantTuplas-1; i++)
		{
			for(int j = 0; j < cantAtributos-1; j++)
			{
				archivo.escribir(tuplas.get(i).getAtributo(j) + ",");
			}
			archivo.escribirLn(tuplas.get(i).getAtributo(cantAtributos-1));
		}
		archivo.cerrarArchivo();
	}
	public void preguntarPorErrores()
	{
		List<Integer> atribConErrores = new ArrayList<>();
		String respuesta = "";
		int valorIntRespuesta = 0;
		for(Dato error:listaErroresDiferentes)
		{
			int numAtrib = error.getNumAtributo();
			if(!atribConErrores.contains(numAtrib))
			{
				atribConErrores.add(numAtrib);
			}
		}
		Collections.sort(atribConErrores);
		System.out.println("Lista de atributos con errores");
		for(Integer numAtributo: atribConErrores)
		{
			System.out.println((numAtributo + 1) + ".- " + listaEncabezados.get(numAtributo));
		}
		System.out.println("0.- Cambiar en todos los atributos con errores");
		System.out.println("Si no se desea cambiar valores poner cualquier valor no valido");
		System.out.println("¿Que atributo desea cambiar los valores de los errores?");
		try { 
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			respuesta = bufferRead.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try{
			valorIntRespuesta = Integer.valueOf(respuesta) - 1;
		}
		catch(NumberFormatException ex)
		{
			if(listaEncabezados.contains(respuesta))
			{
				valorIntRespuesta = listaEncabezados.indexOf(respuesta);
			}
			else
			{
				valorIntRespuesta = -2;
			}
		}
		if(respuesta.equalsIgnoreCase("") || 
				(!atribConErrores.contains(valorIntRespuesta) && valorIntRespuesta != OPCION_NO_MODIFICAR_VALORES)){
			System.out.println("No se cambiaran valores");
		}
		else
		{
			for(Dato error:listaErroresDiferentes)
			{
				if(valorIntRespuesta == -1 || valorIntRespuesta == error.getNumAtributo()){
					System.out.println("Por que nuevo dato desea reemplazar el error " + error.getDato() 
							+ " de los atributos "+ (error.getNumAtributo() + 1) +"? ");
					/*Si descomentas esto se imprimiran los valores posibles que pueden tomar los atributos para usar de guia
					 * System.out.println("Valores validos");
			System.out.println(ARREGLO_VALORES[error.getNumAtributo()].replaceAll("[|]", ", "));]*/
					try { 
						BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
						String nuevoDato = bufferRead.readLine();
						reemplazarErrorPorDato(error.getDato(), nuevoDato, error.getNumAtributo());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	public void reemplazarErrorPorDato(String cadenaError, String nuevoDato, int numAtributo)
	{
		for(ReporteError error: listaErrores)
		{
			if(error.getNumAtributo() == numAtributo && error.getError().equals(cadenaError))
			{
				tuplas.get(error.getNumTupla()).setAtributo(nuevoDato, error.getNumAtributo());
			}
		}
	}
	public void leerExpresionesRegulares()
	{
		String linea, nombreAtributo, expresion;
		int indiceCaracterSeparador, agregarEnIndice;
		listaExpresionesRegulares = new ArrayList<>();
		for(int i = 0, j = listaEncabezados.size(); i < j; i++)
		{
			//Se crea un arreglo con nada para poder insertar en las posiciones que se desea
			listaExpresionesRegulares.add("");
		}
		try {
			while((linea = in.readLine()) != null)
			{
				//El caracter : es el que diferencia del nombre del atributo a la expresion regular, se obtiene el indice en el string de la primer ocurrencia de esto
				indiceCaracterSeparador = linea.indexOf(":");
				nombreAtributo = linea.substring(0, indiceCaracterSeparador);
				expresion = linea.substring((indiceCaracterSeparador + 1));
				agregarEnIndice = listaEncabezados.indexOf(nombreAtributo);
				listaExpresionesRegulares.add(agregarEnIndice, expresion);
				listaExpresionesRegulares.remove(agregarEnIndice + 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void calcularDatosAtribNum()
	{
		for(int i = 0, indiceAtribNum; i < cantAtributos; i++)
		{
			if((indiceAtribNum =listaAtributosNumericos.indexOf(new AtributoNumerico(i))) != -1)
			{
				AtributoNumerico temp = listaAtributosNumericos.get(indiceAtribNum);
				temp.calcularDatosAtributo(sublistaValoresDifValores(i));
			}
		}
	}
	public List<Dato> sublistaValoresDifValores(int numAtributo)
	{
		if(numAtributo < (cantAtributos - 1))
		{
			return listaDatosDiferentes.subList(listaIndicesPrimerosPorAtributo.get(numAtributo),
					listaIndicesPrimerosPorAtributo.get(numAtributo +1));
		}
		else
		{
			return listaDatosDiferentes.subList(listaIndicesPrimerosPorAtributo.get(numAtributo),
					listaDatosDiferentes.size());
		}
	}
	public void calcularCorrelacion()
	{
		listaDeCorrelacionesNum = new ArrayList<>();
		listaDeCorrelacionesNom = new ArrayList<>();
		//Se buscan los indices para evitar repasar gran parte de la lista de datos en los calculos
		buscaIndicesPrimeroDeAtrib();
		for(int i = 0; i < cantAtributos - 1; i++)
		{
			for(int j = i + 1; j < cantAtributos; j++)
			{
				//Comprueba si los dos son numericos
				if(esAtributoNumerico(i) && esAtributoNumerico(j))
				{
					listaDeCorrelacionesNum.add(correlacionNumerico(i, j));
				}
				//Al no ser los dos numericos se tratan como nominales
				else
				{
					listaDeCorrelacionesNom.add(correlacionNominal(i, j));
				}
			}
		}
		Collections.sort(listaDeCorrelacionesNom);
		Collections.sort(listaDeCorrelacionesNum);
		Collections.reverse(listaDeCorrelacionesNom);
		Collections.reverse(listaDeCorrelacionesNum);
	}
	//Devuelve la cantidad de tuplas en las que cualquiera de los dos atributos tiene error
	public int cantErroresEnDosAtrib(int numAtributo1, int numAtributo2)
	{
		int cantErrores = 0;
		for(int indiceTupla = 0, cantTuplas = tuplas.size(); indiceTupla < cantTuplas; indiceTupla++)
		{
			if(hayErrorEnAtributo(indiceTupla, numAtributo1) || hayErrorEnAtributo(indiceTupla, numAtributo2))
			{
				cantErrores++;
			}
		}
		return cantErrores;
	}
	public boolean hayErrorEnAtributo(int indiceTupla, int numAtributo1)
	{
		return listaErrores.contains(new ReporteError(indiceTupla, numAtributo1, ""));
	}
	public int cantErroresEnUnAtrib(int numAtributo)
	{
		int cantErrores = 0;
		for(int indiceTupla = 0, cantTuplas = tuplas.size(); indiceTupla < cantTuplas; indiceTupla++)
		{
			if(hayErrorEnAtributo(indiceTupla, numAtributo))
			{
				cantErrores++;
			}
		}
		return cantErrores;
	}
	public DatoCorrelacion correlacionNumerico(int numAtributo1, int numAtributo2)
	{
		float coeficienteCorrelacion = 0;
		//Contador del indice de las tuplas
		int i = 0, cantTuplasSinError = tuplas.size() - cantErroresEnDosAtrib(numAtributo1, numAtributo2);
		for(Tupla tupla: tuplas)
		{
			if(listaErrores.contains(new ReporteError(i, numAtributo1, "")) || 
					listaErrores.contains(new ReporteError(i, numAtributo2, "")))
			{
			}
			else
			{
				AtributoNumerico atributoNum1 = listaAtributosNumericos.get(listaAtributosNumericos.indexOf(new AtributoNumerico(numAtributo1)));
				AtributoNumerico atributoNum2 = listaAtributosNumericos.get(listaAtributosNumericos.indexOf(new AtributoNumerico(numAtributo2)));
				coeficienteCorrelacion += ((Float.parseFloat(tupla.getAtributo(numAtributo1)) - atributoNum1.getMedia())*
						(Float.parseFloat(tupla.getAtributo(numAtributo2)) - atributoNum2.getMedia()))/
						(cantTuplasSinError * atributoNum1.getDesviacionEstandar() * atributoNum2.getDesviacionEstandar());
			}
			i++;
		}
		return new DatoCorrelacion(listaEncabezados.get(numAtributo1), listaEncabezados.get(numAtributo2), "Numerico", coeficienteCorrelacion);
	}
	public DatoCorrelacion correlacionNominal(int numAtributo1, int numAtributo2)
	{
		//Primero buscamos de que tamanio se crera la tabla de contingencia
		List<Dato> listaValoresDiferentesAtrib1 = sublistaValoresDifValores(numAtributo1);
		List<Dato> listaValoresDiferentesAtrib2 = sublistaValoresDifValores(numAtributo2);
		int cantValoresDistintosAtrib1 = listaValoresDiferentesAtrib1.size();
		int cantValoresDistintosAtrib2 = listaValoresDiferentesAtrib2.size();
		int gradoLibertad;
		//int cantErroresEnAtributos = cantErroresEnDosAtrib(numAtributo1, numAtributo2);
		int tablaContingencia[][]= tablaContingencia(listaValoresDiferentesAtrib1, listaValoresDiferentesAtrib2,
				cantValoresDistintosAtrib1, cantValoresDistintosAtrib2, numAtributo1, numAtributo2);
		float chiCuadrada = 0;
		for(int i = 0; i < cantValoresDistintosAtrib1; i++)
		{
			for(int j = 0; j < cantValoresDistintosAtrib2; j++)
			{
				float frecuenciaEsperada = frecuenciaEsperada(cantValoresDistintosAtrib1, cantValoresDistintosAtrib2, 
						tablaContingencia, i, j, numAtributo1, numAtributo2);
				chiCuadrada += ((Math.pow((tablaContingencia[i][j] - frecuenciaEsperada), 2)) / ((float)frecuenciaEsperada));
			}
		}
		gradoLibertad = ((cantValoresDistintosAtrib1 - 1) * (cantValoresDistintosAtrib2 - 1));
		return new DatoCorrelacion(listaEncabezados.get(numAtributo1), listaEncabezados.get(numAtributo2), "Nominal", chiCuadrada, (" Grado de libertad " + gradoLibertad + " Chi-cuadrada "));
	}
	public int[][] tablaContingencia(List<Dato> listaValoresDiferentesAtrib1, List<Dato> listaValoresDiferentesAtrib2,
			int cantValoresDistintosAtrib1, int cantValoresDistintosAtrib2, int numDato1, int numDato2)
	{
		int tablaContingencia[][]= new int[cantValoresDistintosAtrib1][cantValoresDistintosAtrib2];
		int indiceTupla = 0;
		for(Tupla tupla: tuplas)
		{
			if(hayErrorEnAtributo(indiceTupla, numDato1) || hayErrorEnAtributo(indiceTupla, numDato2))
			{
				//No se hace nada porque causaria errores
			}
			else
			{
				int indiceEnTablaAtrib1 = listaValoresDiferentesAtrib1.indexOf(new Dato(tupla.getAtributo(numDato1), numDato1));
				int indiceEnTablaAtrib2 = listaValoresDiferentesAtrib2.indexOf(new Dato(tupla.getAtributo(numDato2), numDato2));
				tablaContingencia[indiceEnTablaAtrib1][indiceEnTablaAtrib2]++;
			}
			indiceTupla++;
		}
		return tablaContingencia;
	}
	public float frecuenciaEsperada(int cantValoresDistintosAtrib1, int cantValoresDistintosAtrib2, int tablaContingencia[][],
			int numDato1, int numDato2, int numAtributo1, int numAtributo2)
	{
		int totalAtrib1 = 0, totalAtrib2 = 0;
		for(int i=0; i < cantValoresDistintosAtrib1; i++)
		{
			totalAtrib2 += tablaContingencia[i][numDato2];
		}
		for(int i=0; i < cantValoresDistintosAtrib2; i++)
		{
			totalAtrib1 += tablaContingencia[numDato1][i];
		}
		return ((float)(totalAtrib1 * totalAtrib2))/(tuplas.size() - cantErroresEnDosAtrib(numAtributo1, numAtributo2));
	}
	public void buscaIndicesPrimeroDeAtrib()
	{
		listaIndicesPrimerosPorAtributo = new ArrayList<>();
		for(int numAtributo = 0, indiceDatos = 0, cantDatos = listaDatosDiferentes.size();
				numAtributo < cantAtributos; numAtributo++)
		{
			listaIndicesPrimerosPorAtributo.add(indiceDatos);
			for(; indiceDatos < cantDatos; indiceDatos++)
			{
				if(listaDatosDiferentes.get(indiceDatos).getNumAtributo() != numAtributo)
				{
					break;
				}
			}
		}
	}
	public ArrayList<Dato> listaErroresDeAtributo(int numAtributo)
	{
		ArrayList<Dato> listaErrores = new ArrayList<>();
		for(Dato dato: listaErroresDiferentes)
		{
			int numAtributoError = dato.getNumAtributo();
			if(numAtributoError == numAtributo)
			{
				listaErrores.add(dato);
			}
			else if(numAtributoError > numAtributo)
			{
				break;
			}
		}
		return listaErrores;
	}
	public void ordenarListas()
	{
		Collections.sort(listaDatosDiferentes);
		Collections.sort(listaErroresDiferentes);
	}
	public void guardarCorrelaciones()
	{
		ManejadorArchivoTxt archivo = new ManejadorArchivoTxt("Correlaciones");
		archivo.escribirLn("Datos de correlaciones nominales");
		for(DatoCorrelacion dato: listaDeCorrelacionesNom)
		{
			archivo.escribirLn(dato.toString());
		}
		archivo.escribirLn("Datos de correlaciones numericos");
		for(DatoCorrelacion dato: listaDeCorrelacionesNum)
		{
			archivo.escribirLn(dato.toString());
		}
		archivo.cerrarArchivo();
	}
	public void guardarExpresionesRegulares()
	{
		ManejadorArchivoTxt archivo = new ManejadorArchivoTxt("Nuevas expresiones regulares");
		for(int i = 0; i < cantAtributos; i++)
		{
			archivo.escribirLn(listaEncabezados.get(i) + ":" + listaExpresionesRegulares.get(i));
		}
		archivo.cerrarArchivo();
	}
	public ArrayList<String> listaValoresAtributo(int numAtributo)
	{
		ArrayList<String> valores = new ArrayList<>();
		for(Tupla tupla: tuplas)
		{
			valores.add(tupla.getAtributo(numAtributo));
		}
		return valores;
	}
	public void cambiarValoresAtributo(int numAtributo, ArrayList<String> nuevosValores)
	{
		for(int i = 0, cantTuplas = tuplas.size(); i < cantTuplas; i++)
		{
			tuplas.get(i).setAtributo(nuevosValores.get(i), numAtributo);
		}
	}


	//Metodos para las ventanas
	public void llenarConAtributosConError(JTextArea txt)
	{
		ArrayList<Integer> numAtributosPasados = new ArrayList<>();
		for(Dato error:listaErroresDiferentes)
		{
			int indice = error.getNumAtributo();
			if(!numAtributosPasados.contains(indice))
			{
				txt.append(listaEncabezados.get(indice) + "\n");
				numAtributosPasados.add(indice);
			}
		}
	}
	public boolean esAtributoNumerico(int numAtributo)
	{
		return listaAtributosNumericos.contains(new AtributoNumerico(numAtributo));
	}
	public void llenarErroresAtributoTextArea(int numAtributo, JTextArea txt)
	{
		txt.append("Total de errores: " + cantErroresEnUnAtrib(numAtributo) + "\n");
		for(Dato error: listaErroresDeAtributo(numAtributo))
		{
			txt.append(error.getDato()+ " - " + error.getCantOcurrido() + "\n");
		}
	}
	public void llenarErroresAtributoComboBox(String nombreAtributo, JComboBox<String> comboBox)
	{
		for(Dato error: listaErroresDeAtributo(listaEncabezados.indexOf(nombreAtributo)))
		{

			comboBox.addItem(error.getDato());
		}
	}
	public void llenarDatosAtributo(int numAtributo, JTextArea txt)
	{
		txt.append("Total de datos validos: " + (tuplas.size() - cantErroresEnUnAtrib(numAtributo)) + "\n");
		for(Dato dato: sublistaValoresDifValores(numAtributo))
		{
			txt.append(dato.getDato()+ " - " + dato.getCantOcurrido() + "\n");
		}
	}
	public void llenarDistanciasLeven(int numAtributo, JTextArea txt, JComboBox<String> comboBox, String error)
	{
		int indiceEnListDis = -1;
		for(int i = 0; i <= numAtributo; i++)
		{
			if(esAtributoNumerico(i) || cantErroresEnUnAtrib(i) == 0)
			{
				//No se hace anda porque indiceEnListDis es para atributos nominales
			}
			else
			{
				indiceEnListDis++;
			}
		}
		//Se recorre la lista de distancias para todos los errores del atributo
		for(DistanciaLevenshtein distancia: listaDistanciasLeven.get(indiceEnListDis))
		{
			//Se elige solo las distancias que sean de dicho error
			if(error.equals(distancia.getCadena2()))
			{
				txt.append(distancia.toString());
				comboBox.addItem(distancia.getCadena1());
			}
		}

	}
	public void llenarEstadisticas(JTextArea txt, int numAtributo)
	{
		txt.append(listaAtributosNumericos.get(listaAtributosNumericos.indexOf(
				new AtributoNumerico(numAtributo))).toString());
	}
	public void llenarDatosCorrelacionesNominales(JTextArea txt)
	{
		txt.append("--------CORRELACIONES NOMINALES---------\n");
		for(DatoCorrelacion correlacion: listaDeCorrelacionesNom)
		{
			txt.append(correlacion.toString());
		}

	}
	public void llenarDatosCorrelacionesNumericas(JTextArea txt)
	{
		txt.append("--------CORRELACIONES NUMERICAS---------\n");
		for(DatoCorrelacion correlacion: listaDeCorrelacionesNum)
		{
			txt.append(correlacion.toString());
		}
	}
	public void llenarDatosCorrelacionAtribDados(String atributo1, String atributo2, int numAtributo1, int numAtributo2, JTextArea text)
	{
		if(esAtributoNumerico(numAtributo1) && esAtributoNumerico(numAtributo2))
		{
			text.setText(listaDeCorrelacionesNum.get(listaDeCorrelacionesNum.indexOf(
					new DatoCorrelacion(atributo1, atributo2, "", 0))).toString());
		}
		else
		{
			text.setText(listaDeCorrelacionesNom.get(listaDeCorrelacionesNom.indexOf(
					new DatoCorrelacion(atributo1, atributo2, "", 0))).toString());
		}
	}
	public void llenarConEncabezados(JComboBox<String> comboBox)
	{
		for(String nombreAtributo: listaEncabezados)
		{
			comboBox.addItem(nombreAtributo);
		}
	}

	//Metodos para los calculos de normalizacion y distancia de levenshtein
	public int dameDistanciaDeLevenshtein(String palabra1, String palabra2)
	{
		int tamanioPalabra1 = palabra1.length(), tamanioPalabra2 = palabra2.length();
		//Si la palabra 1 es cadena vacio regresa el tamanio de la otra palabra
		if(tamanioPalabra1 == 0)
		{
			return tamanioPalabra2;
		}
		//En caso de palabra 2 es cadena vacia regresa tamanio palabra 1
		else if(tamanioPalabra2 == 0)
		{
			return tamanioPalabra1;
		}
		int matriz[][] = prepararMatrizLevenshtein(tamanioPalabra1, tamanioPalabra2);
		char arregloPalabra1[] = palabra1.toCharArray();
		char arregloPalabra2[] = palabra2.toCharArray();
		for(int i = 1; i <= tamanioPalabra1; i++)
		{
			int menor, comp;
			int valores[] = new int[3];
			for(int j = 1; j <= tamanioPalabra2; j++)
			{
				valores[0] = matriz[i - 1][j] + 1;
				valores[1] = matriz[i][j - 1] + 1;
				if(arregloPalabra1[(i - 1)] == arregloPalabra2[(j - 1)])
				{
					comp = 0;
				}
				else
				{
					comp = 1;
				}
				valores[2] = matriz[i -1][j - 1] + comp;
				//Se busca el menor de los tres valoress
				menor = valores[0];
				if(valores[1] < menor)
				{
					menor = valores[1];
				}
				if(valores[2] < menor)
				{
					menor = valores[2];
				}
				matriz[i][j] = menor;
			}
		}
		return matriz[tamanioPalabra1][tamanioPalabra2];
	}
	public int[][] prepararMatrizLevenshtein(int tamanioPalabra1, int tamanioPalabra2)
	{
		int matriz[][] = new int [(tamanioPalabra1 + 1)][(tamanioPalabra2 + 1)];
		for(int j = 0; j <= tamanioPalabra2; j++)
		{
			matriz[0][j] = j;
		}
		//Empezamos desde i = 1 porque el valor con i = 0 ya se asigno en el for anterior
		for(int i = 1; i <= tamanioPalabra1; i++)
		{
			matriz[i][0] = i;
		}
		return matriz;
	}
	public void calcularSugerenciasErroresNominales()
	{
		listaDistanciasLeven = new ArrayList<>();
		//Buscara en todos los atributos (adelante se seleccionan solo los nominales)
		for(int numAtributo = 0; numAtributo < cantAtributos; numAtributo++)
		{
			//Si se cumple el if significa que es un atributo nominal
			if(!esAtributoNumerico(numAtributo))
			{
				ArrayList<DistanciaLevenshtein> temp = new ArrayList<>();
				for(Dato datoError: listaErroresDeAtributo(numAtributo))
				{
					for(Dato datoValido: sublistaValoresDifValores(numAtributo))
					{
						String error = datoError.getDato();
						String valido = datoValido.getDato(); 
						int distancia = dameDistanciaDeLevenshtein(valido, error);
						temp.add(new DistanciaLevenshtein(numAtributo, distancia, valido, error));
					}
				}
				if(temp.size() > 0)
				{
					listaDistanciasLeven.add(temp);
				}
			}
		}
		for(ArrayList<DistanciaLevenshtein> lista: listaDistanciasLeven)
		{
			Collections.sort(lista);
		}
	}
	public ArrayList<String> normalizacionEscalamiento(AtributoNumerico atributo)
	{
		int numAtributo = atributo.getNumAtributo();
		ArrayList<String> valores = listaValoresAtributo(numAtributo);
		float numMaximo = atributo.getMaximo();
		int divisor = 1;
		while((numMaximo / divisor) > 1)
		{
			divisor *= 10;
		}
		for(int i = 0, cantTuplas = tuplas.size(); i < cantTuplas; i++)
		{
			if(listaErrores.contains(new ReporteError(i, numAtributo, "")))
			{

			}
			else
			{
				float nuevoDato = Float.parseFloat(valores.get(i)) / divisor;
				valores.set(i, String.valueOf(nuevoDato));
			}
		}
		return valores;
	}
	public ArrayList<String> normalizacionMinMax(AtributoNumerico atributo, int nuevoMinimo, int nuevoMaximo)
	{
		int numAtributo = atributo.getNumAtributo();
		float minimo = atributo.getMinimo(), maximo = atributo.getMaximo();
		ArrayList<String> valores = listaValoresAtributo(numAtributo);
		for(int i = 0, cantTuplas = tuplas.size(); i < cantTuplas; i++)
		{
			if(listaErrores.contains(new ReporteError(i, numAtributo, "")))
			{

			}
			else
			{
				float valor = Float.parseFloat(valores.get(i));
				float nuevoValor = ((valor - minimo)/(maximo - minimo))*
						(nuevoMaximo - nuevoMinimo) + nuevoMinimo;
				valores.set(i, String.valueOf(nuevoValor));
			}
		}
		return valores;
	}
	public ArrayList<String> normalizacionZScoreDesEst(AtributoNumerico atributo)
	{
		int numAtributo = atributo.getNumAtributo();
		ArrayList<String> valores = listaValoresAtributo(numAtributo);
		float desviacionEstandar = atributo.getDesviacionEstandar();
		float media = atributo.getMedia();
		for(int i = 0, cantTuplas = tuplas.size(); i < cantTuplas; i++)
		{
			if(listaErrores.contains(new ReporteError(i, numAtributo, "")))
			{
				//Si tiene un error en dicho atributo se hace nada
			}
			else
			{
				float valor = Float.parseFloat(valores.get(i));
				float nuevoValor = (valor - media)/desviacionEstandar;
				valores.set(i, String.valueOf(nuevoValor));
			}
		}
		return valores;
	}
	public ArrayList<String> normalizacionZScoreDesMedAbs(AtributoNumerico atributo)
	{
		int numAtributo = atributo.getNumAtributo();
		ArrayList<String> valores = listaValoresAtributo(numAtributo);
		float media = atributo.getMedia();
		for(int i = 0, cantTuplas = tuplas.size(); i < cantTuplas; i++)
		{
			if(listaErrores.contains(new ReporteError(i, numAtributo, "")))
			{
				//Si tiene un error en dicho atributo se hace nada
			}
			else
			{
				float valor = Float.parseFloat(valores.get(i));
				float nuevoValor = (valor - media)/desviacionMediaAbsoluta(atributo);
				valores.set(i, String.valueOf(nuevoValor));
			}
		}
		return valores;
	}
	public float desviacionMediaAbsoluta(AtributoNumerico atributo)
	{
		int numAtributo = atributo.getNumAtributo();
		float media = atributo.getMedia();
		float sumatoria = 0;
		for(Dato dato: sublistaValoresDifValores(numAtributo))
		{
			sumatoria += (dato.getCantOcurrido() * (Math.abs(Float.parseFloat(dato.getDato())- media)));
		}
		return (sumatoria / (tuplas.size() - cantErroresEnUnAtrib(numAtributo)));
	}


	public void eliminarAtributo(int numAtributo)
	{
		int indiceAtribNumerico = listaAtributosNumericos.indexOf(new AtributoNumerico(numAtributo));
		if(indiceAtribNumerico != -1)
		{
			listaAtributosNumericos.remove(indiceAtribNumerico);
		}
		for(AtributoNumerico atributo: listaAtributosNumericos)
		{
			int nuevoNumAtributo = atributo.getNumAtributo();
			if(nuevoNumAtributo > numAtributo)
			{
				atributo.setNumAtributo(nuevoNumAtributo - 1);
			}
		}
		listaEncabezados.remove(numAtributo);
		listaExpresionesRegulares.remove(numAtributo);
		for(int i = 0, cantTuplas = tuplas.size(); i < cantTuplas; i++)
		{
			int indiceReporteError = listaErrores.indexOf(new ReporteError(i, numAtributo, ""));
			tuplas.get(i).eliminarAtributo(numAtributo);
			if(indiceReporteError != -1)
			{
				listaErrores.remove(indiceReporteError);
			}
		}	
		cantAtributos--;
	}

	//Algoritmos
	public String zeroR()
	{
		String claseEsperada = "";
		List<Dato> clases = sublistaValoresDifValores(listaEncabezados.size() - 1);
		int mayor = 0;
		for(Dato clase: clases)
		{
			int cantOcurrido = clase.getCantOcurrido(); //Aquí se supone van las clases y las veces que ocurrió
			if(cantOcurrido > mayor)
			{
				mayor = cantOcurrido;
				claseEsperada = clase.getDato();
			}
		}
		System.out.println("Resultado de ZeroR: " +claseEsperada);
		return claseEsperada;
	}
	public String OneR()
	{
		ArrayList<String> valoresClase = new ArrayList<>();
		int indiceClase = listaEncabezados.size() - 1;
		ArrayList<TablaFrecuencia> listaTablasFrecuncia = new ArrayList<>();
		//Se obtienen los valores diferentes que tiene la clase
		for (Dato valor: sublistaValoresDifValores(indiceClase))
		{
			valoresClase.add(valor.getDato());
		}
		for(int i = 0, cantAtributosSinClase = listaEncabezados.size() - 1;i < cantAtributosSinClase; i++)
		{
			if(esAtributoNumerico(i))
			{
				ArrayList<ArrayList<Object>> valoresTuplas = new ArrayList<>();
				for(Tupla tupla: tuplas)
				{
					ArrayList<Object> temp = new ArrayList<>();
					temp.add(tupla.getAtributo(i));
					temp.add(tupla.getAtributo(indiceClase));
					valoresTuplas.add(temp);
				}
				//Metodo para ordenar la lista
				Collections.sort(valoresTuplas, new Comparator<ArrayList<Object>>() {
					@Override
					public int compare(ArrayList<Object> arg0,
							ArrayList<Object> arg1) {
						return Float.compare(Float.parseFloat((String) ((ArrayList<Object>) arg0).get(0)),
								(Float.parseFloat((String) ((ArrayList<Object>) arg1).get(0))));
					}
				}); 
				//Variables necesarias para particiones
				ArrayList<Integer> indicesParticiones = new ArrayList<>();
				ArrayList<String> particionesClases = new ArrayList<>();
				int[] cantidadSucedidaClase = new int[valoresClase.size()];
				int cantidadVentaneo = 3;
				for(int m = 0, n = valoresTuplas.size(); m < n; m++)	
				{
					String clase = (String)valoresTuplas.get(m).get(1);
					int indiceClaseEnArreglo = valoresClase.indexOf(clase);
					cantidadSucedidaClase[indiceClaseEnArreglo]++;
					if(cantidadSucedidaClase[indiceClaseEnArreglo] == cantidadVentaneo)
					{
						m++;
						for(; m < n && clase.equals((String)valoresTuplas.get(m).get(1)); m++)
						{
							
						}
						cantidadSucedidaClase = new int[valoresClase.size()];
						indicesParticiones.add(m);
						particionesClases.add(clase);
						m--;
					}
				}
				//Aqui se trata cuando quedaron una particion sin completarse por falta de coincidencias para el ventaneo
				ArrayList<Integer> indicesMayorCantidades = new ArrayList<>();
				int mayor = 0;
				for(int x = 0, y = valoresClase.size(); x < y; x++)
				{
					int cantidadActual = cantidadSucedidaClase[x]; 
					if(mayor == cantidadActual)
					{
						indicesMayorCantidades.add(x);
					}
					else if(mayor < cantidadActual)
					{
						indicesMayorCantidades = new ArrayList<>();
						indicesMayorCantidades.add(x);
						mayor = cantidadActual;
					}
				}
				//Se comprueba que hayan quedado elementos sin ventanear
				if(mayor != 0)
				{
					if(indicesMayorCantidades.size() == 1)
					{
						indicesParticiones.add(valoresTuplas.size());
						particionesClases.add(valoresClase.get(indicesMayorCantidades.get(0)));
					}
					else
					{
						String claseAnterior = particionesClases.get(particionesClases.size() - 1);
						String claseAAgregar = "";
						for(Integer mayorIndice: indicesMayorCantidades)
						{
							String claseTemp = valoresClase.get(mayorIndice);
							if(!claseAnterior.equals(claseTemp))
							{
								claseAAgregar = claseTemp;
								break;
							}
						}
						indicesParticiones.add(valoresTuplas.size());
						particionesClases.add(claseAAgregar);
					}
					//Quitar donde hay clases seguidas continuas
					for(int k = 0, l = particionesClases.size() - 1; k < l; k++)
					{
						if(particionesClases.get(k).equals(particionesClases.get(k + 1)))
						{
							particionesClases.remove(k);
							indicesParticiones.remove(k);
							k--;
							l = particionesClases.size() - 1;
						}
					}
					//Ahora a agregar los promedios para comparar
					//ESTO FALTA
				}
				ArrayList<Float> limites = new ArrayList<>();
				for(int k = 0, l = indicesParticiones.size() - 1; k < l; k++)
				{
					float primerValor = Float.parseFloat((String) valoresTuplas.get(indicesParticiones.get(k) - 1).get(0));
					float segundoValor = Float.parseFloat((String) valoresTuplas.get(indicesParticiones.get(k)).get(0));
					limites.add((primerValor + segundoValor) / 2);
				}
				System.out.println(limites);
				System.out.println(indicesParticiones);
				System.out.println(particionesClases);
				float numero = (float) 182.6;
				String clase = "";
				for(int k = 0, l = limites.size(); k < l; k++)
				{
					if(numero <= limites.get(k))
					{
						clase = particionesClases.get(k);
						break;
					}
				}
				if(clase.equals(""))
				{
					clase = particionesClases.get(indicesParticiones.size() - 1);
				}
				System.out.println("Clase " + clase);
			}
			else
			{
				listaTablasFrecuncia.add(dameTablaDeFrecuencias(i));
			}
		}
		return "";
	}
	public void naiveBayes(Tupla tupla)
	{
		ArrayList<TablaFrecuencia> listaTablasFrecuencia = new ArrayList<>();
		List<Dato> datosDeClase = sublistaValoresDifValores(listaEncabezados.size() - 1);
		float[] probabilidadClase = new float [datosDeClase.size()];
		int totalDeValoresClase = 0;
		ArrayList<Float> probabilidades = new ArrayList<>();
		ArrayList<List<Dato>> subListasValoresAtributos = new ArrayList<>();
		//Se obtienen las tablas de verosimilitud de los atributos excepto la clase
		for(int i = 0, j = listaEncabezados.size() - 1; i < j; i++)
		{
			listaTablasFrecuencia.add(dameTablaDeFrecuencias(i));
			subListasValoresAtributos.add(sublistaValoresDifValores(i));
			listaTablasFrecuencia.get(listaTablasFrecuencia.size() - 1).llenarTablaVerosimilitud();
		}
		//Aqui se llena la tabla de verosimilitud de la clase ya que esta es diferente a las otras
		for(int i = 0, j = datosDeClase.size(); i < j; i++)
		{
			int cantOcurridoDato = datosDeClase.get(i).getCantOcurrido();
			probabilidadClase[i] = cantOcurridoDato;
			totalDeValoresClase += cantOcurridoDato;
		}
		for(int i = 0, j = datosDeClase.size(); i < j; i++)
		{
			probabilidadClase[i] /= totalDeValoresClase;
		}
		for(int i = 0, j = datosDeClase.size(); i < j; i++)
		{
			float probabilidad = 1;
			for(int k = 0, l = listaEncabezados.size() - 1; k < l; k++)
			{
				if(cumpleExpresionRegular(listaExpresionesRegulares.get(k), tupla.getAtributo(k)))
				{
					if(esAtributoNumerico(k)){
						//FALTA HACER LO DE LOS ATRIBUTOS NUMERICOS
					}
					else{
						List<Dato> datosAtributo = subListasValoresAtributos.get(k);
						probabilidad *= listaTablasFrecuencia.get(k).getTablaVerosimilitud()
								[datosAtributo.indexOf(new Dato(tupla.getAtributo(k), k))][i];
					}
				}
			}
			probabilidad *= probabilidadClase[i];
			probabilidades.add(probabilidad);
		}
		//Normalizar las probabilidades
		float mayor = 0, menor = 1;
		for(Float probabilidad: probabilidades)
		{
			if(mayor < probabilidad)
			{
				mayor = probabilidad;
			}
			if(menor > probabilidad)
			{
				menor = probabilidad;
			}
		}
		for(int i = 0, j = probabilidades.size(); i < j; i++)
		{
			probabilidades.add(i, probabilidades.get(i) / (mayor + menor));
			probabilidades.remove(i + 1);
		}
		for(Float probabilidad: probabilidades)
		{
			System.out.println(probabilidad);
		}
	}
	public TablaFrecuencia dameTablaDeFrecuencias(int numAtributo)
	{
		ArrayList<String> valoresClase = new ArrayList<>();
		int indiceClase = listaEncabezados.size() - 1;
		//Se obtienen los valores diferentes que tiene la clase
		for (Dato valor: sublistaValoresDifValores(indiceClase))
		{
			valoresClase.add(valor.getDato());
		}
		ArrayList<String> valoresAtributo = new ArrayList<>();
		//Se obtiene los valores diferentes que tiene el atributo analizado
		for (Dato valor: sublistaValoresDifValores(numAtributo))
		{
			valoresAtributo.add(valor.getDato());
		}
		int matrizFrecuencia[][] = new int [valoresAtributo.size()][valoresClase.size()];
		for(Tupla tupla: tuplas)
		{
			matrizFrecuencia[valoresAtributo.indexOf(tupla.getAtributo(numAtributo))][valoresClase.indexOf(tupla.getAtributo(indiceClase))]++;
		}
		return new TablaFrecuencia(valoresClase, valoresAtributo, matrizFrecuencia);
	}
}


