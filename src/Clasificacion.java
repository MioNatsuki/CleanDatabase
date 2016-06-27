import java.util.Scanner;

public class Clasificacion {
	
	Scanner entrada = new Scanner(System.in);
	int instancias;
	String clase, clima, temperatura, humedad, viento;
	String posible = "Yes";
	String posible2 = "No";
	String clima1 = "Sunny";
	String clima2 = "Overcast";
	String clima3 = "Rainy";
	String temperatura1 = "Hot";
	String temperatura2 = "Mild";
	String temperatura3 = "Cool";
	String humedad1 = "High";
	String humedad2 = "Normal";
	String viento1 = "True";
	String viento2 = "False";
	
	public void ZeroR(){
		
		int cont1=0, cont2=0;
		
		
		System.out.println("Ingrese el numero de instancias: ");
		instancias = entrada.nextInt();
		
		for(int i=0; i<instancias; i++)
		{
			System.out.println("Ingrese la clase para la intancia " +i + " : ");
			clase = entrada.next();
			if(clase.equals(posible))
				cont1++;
			
			else if(clase.equals(posible2))
				cont2++;
		}
		
		if(cont1 > cont2)
			System.out.println("La clase con mayor frecuencia es: " +posible);
		else if(cont2 > cont1)
			System.out.println("La clase con mayor frecuencia es: " +posible2);
		else
			System.out.println("Ambas clases tienen el mismo valor de frecuencia");
	}
	
	public void OneR(){
		
		System.out.println("Ingrese el numero de instancias: ");
		instancias = entrada.nextInt();
		int sy=0, sn=0, oy=0, on=0, ry=0, rn=0, hy=0, hn=0, my=0, mn=0, cy=0, cn=0, hiy=0, hin=0, ny=0, nn=0, ty=0, tn=0, fy=0, fn=0;
		float total1=0, total2=0, total3=0, total4=0;
		int errores[] = new int[4];
		
		for (int i=0; i<instancias; i++)
		{
			System.out.println("Ingrese el clima para la intancia " +i + " : ");
			clima = entrada.next();
			
			System.out.println("Ingrese la temperatura para la instancia " +i + " :");
			temperatura = entrada.next();
			
			System.out.println("Ingrese la humendad para la instancia " +i +  " :");
			humedad = entrada.next();
			
			System.out.println("Ingrese True o False si habra viento para la instancia " +i +  " :");
			viento = entrada.next();
			
			System.out.println("Ingrese la clase para la intancia " +i + " : ");
			clase = entrada.next();
			
			if((clima.equals(clima1))&& clase.equals(posible))
				sy++;
			else if((clima.equals(clima1))&& clase.equals(posible2))
				sn++;
			else if((clima.equals(clima2))&& clase.equals(posible))
				oy++;
			else if((clima.equals(clima2))&& clase.equals(posible2))
				on++;
			else if((clima.equals(clima3))&& clase.equals(posible))
				ry++;
			else if((clima.equals(clima3))&& clase.equals(posible2))
				rn++;
			
			if((temperatura.equals(temperatura1))&& clase.equals(posible))
				hy++;
			else if((temperatura.equals(temperatura1))&& clase.equals(posible2))
				hn++;
			else if((temperatura.equals(temperatura2))&& clase.equals(posible))
				my++;
			else if((temperatura.equals(temperatura2))&& clase.equals(posible2))
				mn++;
			else if((temperatura.equals(temperatura3))&& clase.equals(posible))
				cy++;
			else if((temperatura.equals(temperatura3))&& clase.equals(posible2))
				cn++;
			
			if((humedad.equals(humedad1))&& clase.equals(posible))
				hiy++;
			else if((humedad.equals(humedad1))&& clase.equals(posible2))
				hin++;
			else if((humedad.equals(humedad2))&& clase.equals(posible))
				ny++;
			else if((humedad.equals(humedad2))&& clase.equals(posible2))
				nn++;
			
			if((viento.equals(viento1))&& clase.equals(posible))
				ty++;
			else if((viento.equals(viento1))&& clase.equals(posible2))
				tn++;
			else if((viento.equals(viento2))&& clase.equals(posible))
				fy++;
			else if((viento.equals(viento2))&& clase.equals(posible2))
				fn++;
		}
		
		System.out.println("-------------------------------------");
		System.out.println("         TABLA DE FRECUENCIA");
		System.out.println("Clima   |Yes|No");
		System.out.println("Sunny   |"+sy+"  |"+sn);
		System.out.println("Rainy   |"+ry+"  |"+rn);
		System.out.println("Overcast|"+oy+"  |"+on);
		System.out.println("-------------------------------------");
		System.out.println("Temperatura|Yes|No");
		System.out.println("Hot        |"+hy+"  |"+hn);
		System.out.println("Mild       |"+my+"  |"+mn);
		System.out.println("Cool       |"+cy+"  |"+cn);
		System.out.println("-------------------------------------");
		System.out.println("Humedad|Yes|No");
		System.out.println("High   |"+hiy+"  |"+hin);
		System.out.println("Normal |"+ny+"  |"+nn);
		System.out.println("-------------------------------------");
		System.out.println("Viento|Yes|No");
		System.out.println("True  |"+ty+"  |"+tn);
		System.out.println("False |"+fy+"  |"+fn);
		System.out.println("-------------------------------------\n");
		
		
		
		System.out.println("Reglas Obtenidas:");
		if(sy>sn)
			{System.out.println("Sunny Yes->: " +sy);
			errores[0]+=sn;
			 }
		else 
			{System.out.println("Sunny No->: " +sn);
			errores[0]+=sy;
			 }
		
		
		if(oy>on)
			{System.out.println("Overcast Yes->: " +oy);
			errores[0]+=on;
			}
		else
			{System.out.println("Overcast No->: " +on);
			errores[0]+=oy;
			}
		
		
		if(ry>rn)
			{System.out.println("Rainy Yes->: " +ry);
			errores[0]+=rn;
			}
		else
			{System.out.println("Rainy No->: " +rn);
			errores[0]+=ry;
			}
		
		if(hy>hn)
			{System.out.println("Hot Yes->: " +hy);
			errores[1]+=hn;
			}
		else 
			{System.out.println("Hot No->: " +hn);
			errores[1]+=hy;
			}
		
		if(my>mn)
			{System.out.println("Mild Yes->: " +my);
			errores[1]+=mn;
			}
		else
			{System.out.println("Mild No->: " +mn);
			errores[1]+=my;
			}
		
		if(cy>cn)
		{	System.out.println("Cool Yes->: " +cy);
			errores[1]+=cn;
		}
		else
		{	System.out.println("Cool No->: " +cn);
			errores[1]+=cy;
		}
		
		if(hiy>hin)
		{	System.out.println("High Yes->: " +hiy);
			errores[2]+=hin;
		}
		else
		{	System.out.println("High No->: " +hin);
			errores[2]+=hiy;
		}
		
		
		if(ny>nn)
		{	System.out.println("Normal Yes->: " +ny);
			errores[2]+=nn;
		}
		else 
		{	System.out.println("Normal No->: " +nn);
			errores[2]+=ny;
		}
		
		
		if(ty>tn)
		{	System.out.println("True Yes->: " +ty);
			errores[3]+=tn;
		}
		else
		{	System.out.println("True No->: " +tn);
			errores[3]+=ty;
		}
		
		if(fy>fn)
		{	System.out.println("False Yes->: " +fy);
			errores[3]+=fn;
		}
		else
		{	System.out.println("False No->: " +fn);
			errores[3]+=fy;
		}
		
		System.out.println("Total de errores para clima: "+errores[0]+"/"+instancias);
		System.out.println("Total de errores para temperatura: "+errores[1]+"/"+instancias);
		System.out.println("Total de errores para humedad: "+errores[2]+"/"+instancias);
		System.out.println("Total de errores para viento: "+errores[3]+"/"+instancias);
		
		int aux=99999, pos=-1;
		
		for(int i=0;i<4;i++)
		{
			if(errores[i]<aux)
			{
				aux = errores[i];
				pos = i;
			}
		}
		
		switch(pos)
		{
		case 0:
			System.out.println("El atributo con menos errores es: Clima");
			break;
		case 1:
			System.out.println("El atributo con menos errores es: Humedad");
			break;
		case 2:
			System.out.println("El atributo con menos errores es: Temperatura");
			break;
		case 3:
			System.out.println("El atributo con menos errores es: Viento");
			break;
		default:
			System.out.println("IMPOSIBLE DETERMINAR MENOR");
			break;
		}
		
		if(total1>total2 && total1>total3 && total1>total4)
			System.out.println("Atributo con el error mas pequenio: Clima");
		
		else if(total2>total1 && total2>total3 && total2>total4)
			System.out.println("Atributo con el error mas pequenio: Temperatura");
		
		else if(total3>total1 && total3>total2 && total3>total4)
			System.out.println("Atributo con el error mas pequenio: Humedad");
		
		else if(total4>total1 && total4>total2 && total4>total3)
			System.out.println("Atributo con el error mas pequenio: Viento");
		
			
	}
	
}

