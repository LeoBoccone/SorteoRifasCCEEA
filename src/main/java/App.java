import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;


public class App
{
	public static final String PROJECT_NAME = "sorteo - 4ta - solo aleatorio";
	
	public static final String PATH_PROJECT = System.getProperty("user.dir").concat("/");
	//public static final String PATH_PROJECT = "C:\\Users\\Alejandro Brusco\\git\\sorteoRifas\\".concat(PROJECT_NAME).concat("\\");
	public static final String PATH_GENERADOS = PATH_PROJECT.concat("generados/");
	private static final String TOTAL_CSV = PATH_PROJECT.concat("generados/TOTAL.csv");
	//private static final String TOTAL_TXT = PATH_PROJECT.concat("generados/TOTAL.txt");
	private static final String TOTAL_AUN_DISPONIBLES_TXT = PATH_PROJECT.concat("generados/TOTAL_AUN_DISPONIBLES.txt");
	private static  String FILENAME_PEDIDOS_EXTRA = PATH_PROJECT;
	//private static final String FILENAME_PEDIDOS_EXTRA = PATH_PROJECT.concat("PedidosExtra.txt");
	private static String FILENAME_RIFAS_DISPONIBLES = PATH_PROJECT;
	//private static final String FILENAME_RIFAS_DISPONIBLES = PATH_PROJECT.concat("RifasDisponibles.txt");
	
	private static Integer primerRifa = 99999;
	private static Integer ultimaRifa = 0;
	private static Integer completos = 0;
	private static Integer cant_titulares = 0;
	private static Integer cant_rifas_pedidas_total = 0;
	private static Random rnd = new Random();
	private static SortedMap<Integer, Integer> rifasAsignadas = new TreeMap<>();
	private static ArrayList<Integer> rifasDisponibles = new ArrayList<Integer>();
	private static ArrayList<Integrante> integrantes = new ArrayList<Integrante>();
	
	
	// **************
	// INICIALIZACIÓN
	// **************

	private App(){}

	private static void initialize() throws Exception {
		if (!Files.isDirectory(Paths.get(PATH_GENERADOS))){
			Files.createDirectory(Paths.get(PATH_GENERADOS));
		}
		initializePedidosExtra();
		initializeRifas();
		System.out.println("# Rifas pedidas = " + cant_rifas_pedidas_total + " - Rifas disponibles: " + rifasDisponibles.size());
		if (cant_rifas_pedidas_total > rifasDisponibles.size()) {
			throw new Exception("ERROR: Hay más rifas pedidas que las disponibles.");
		}
	}
	
	private static void initializePedidosExtra() throws IOException{
		System.out.println("# INICIO - initializePedidosExtra");
    	
    	try (BufferedReader br = new BufferedReader(new FileReader(FILENAME_PEDIDOS_EXTRA))) {
			String linea, idTitularStr, cantRifasPedidas;
			while ((linea = br.readLine()) != null) {
				idTitularStr = linea.split("=")[0];
				cantRifasPedidas = linea.split("=")[1];
				cant_rifas_pedidas_total += Integer.valueOf(cantRifasPedidas);
				Integrante integrante = new Integrante(Integer.valueOf(idTitularStr), Integer.valueOf(cantRifasPedidas));
				integrantes.add(integrante);
				cant_titulares++;
			}
		} catch (IOException e) {
		   throw e;
		}
    	System.out.println("# FIN - initializePedidosExtra");
    }
	
	private static void initializeRifas() throws IOException{
		System.out.println("# INICIO - initializePrimerasRifas");
		
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME_RIFAS_DISPONIBLES))) {
			String rifaStr;
			Integer rifa;
			while ((rifaStr = br.readLine()) != null) {
				rifa = Integer.valueOf(rifaStr);
				rifasDisponibles.add(rifa);
				if (rifa < primerRifa) {
					primerRifa = rifa;
				}
				if (rifa > ultimaRifa) {
					ultimaRifa = rifa;
				}
			}
		} catch (IOException e) {
		   throw e;
		}
		
		System.out.println("# FIN - initializePrimerasRifas");
	}
	
	// **********
	// ASIGNACIÓN
	// **********
	
	private static void asignarRifa(Integer rifa, Integrante integrante) {
		integrante.addRifa(rifa);
		rifasAsignadas.put(rifa, integrante.getId());
		rifasDisponibles.remove(rifa);
		if (integrante.getSizeRifas().equals(integrante.getCantRifasASortear())) {
			completos++;
		}
	}
	
	// ***********
	// IMPRESIONES
	// ***********
	
	private static void printRifasXIntegrantes2Files() throws IOException {
		for (Integrante integrante : integrantes) {
			integrante.printRifas2File();
			//integrante.printRifas2PDF();
		}
		System.out.println( "# FIN - Creación archivos Integrantes con sus asignaciones");
	}
	
	private static void printTotalRifas2File() throws IOException {
		try{
			Integer idIntegrante;
			List<String> lines = new ArrayList<String>();
			lines.add("RIFA - ID INTEGRANTE");
			for (Integer rifa : rifasAsignadas.keySet()) {
				idIntegrante = rifasAsignadas.get(rifa);
				lines.add(rifa + "-" + idIntegrante);
			}
			/*
			Path file = Paths.get(TOTAL_TXT);
			Files.write(file, lines, Charset.forName("UTF-8"));
			System.out.println( "# FIN - Creación archivo mapa con todas las asignaciones en " + TOTAL_TXT);
			*/
			
			List<String> lines2 = new ArrayList<String>();
			lines2.add("RIFA;ID INTEGRANTE");
			for (Integer rifa : rifasAsignadas.keySet()) {
				idIntegrante = rifasAsignadas.get(rifa);
				lines2.add(rifa + ";" + idIntegrante);
			}
			Path file2 = Paths.get(TOTAL_CSV);
			Files.write(file2, lines2, Charset.forName("UTF-8"));
			System.out.println( "# FIN - Creación archivo mapa CSV con todas las asignaciones en " + TOTAL_CSV);
			

			// TOTAL AUN DISPONIBLES
			List<String> lines3 = new ArrayList<String>();
			for (Integer rifa : rifasDisponibles) {
				lines3.add(rifa + "-" + "null");
			}
			Path file3 = Paths.get(TOTAL_AUN_DISPONIBLES_TXT);
			Files.write(file3, lines3, Charset.forName("UTF-8"));
			System.out.println( "# FIN - Creación archivo mapa con todas las rifas aun disponibles en " + TOTAL_AUN_DISPONIBLES_TXT);
		} catch (IOException e) {
		   throw e;
		}
	}
	
	
	private static void saveOutput() throws IOException {
		printTotalRifas2File();
    	//printRifasXIntegrantes2Files();
	}

	
	// **********
	// AUXILIARES
	// **********
	
	private static Integer nextIntegrante(Integer posIntegrante, Integer tope) {
		if (posIntegrante >= tope - 1){
			posIntegrante = 0;
		} else {
			posIntegrante++;
		}
		return posIntegrante;
	}
	
	private static Integer maxCantRifas() {
		Integer max = 0;
		for (Integrante integrante : integrantes) {
			if (integrante.getSizeRifas() > max)
				max = integrante.getSizeRifas();
		}
		return max;
	}
	
	private static Integer minCantRifas() {
		Integer min = 99999;
		for (Integrante integrante : integrantes) {
			if (integrante.getSizeRifas() < min)
				min = integrante.getSizeRifas();
		}
		return min;
	}
	

	// ******
	// SORTEO
	// ******
	
	private static void executeSorteo() throws IOException {
    	Integer rifa, posRifa, posIntegrante = 0;
    	
    	while (rifasDisponibles.size() > 0 && completos < cant_titulares){
			posRifa = (int) (rnd.nextDouble() * rifasDisponibles.size());
			rifa = rifasDisponibles.get(posRifa);
			Integrante integrante = integrantes.get(posIntegrante);
			while ((integrante.getSizeRifas() >= integrante.getCantRifasASortear()) && completos != cant_titulares) {
				posIntegrante = nextIntegrante(posIntegrante, cant_titulares);
				integrante = integrantes.get(posIntegrante);
			}
    		asignarRifa(rifa, integrante);
    		posIntegrante = nextIntegrante(posIntegrante, cant_titulares);
    	}
    	System.out.println("# Rifas disponibles post-sorteo: " + rifasDisponibles.size());
    	saveOutput();
	}
	
	private static void printResume() {
		System.out.println();
		System.out.println("***************************************");
		System.out.println("Total titulares: " + cant_titulares);
		System.out.println("Rifas sorteadas: " + rifasAsignadas.size());
		System.out.println("Max cantidad rifas integrante: " + maxCantRifas());
		System.out.println("Min cantidad rifas integrante: " + minCantRifas());
		System.out.println("***************************************");
		System.out.println();
	}
	
	
	// ******************
	// PROGRAMA PRINCIPAL
	// ******************
	
    public static void sortear( String[] args ) throws Exception
    {
    	FILENAME_PEDIDOS_EXTRA = FILENAME_PEDIDOS_EXTRA.concat(args[0]);
    	FILENAME_RIFAS_DISPONIBLES = FILENAME_RIFAS_DISPONIBLES.concat(args[1]);
    	System.out.println( "# PATH_PROJECT: " + PATH_PROJECT );
    	PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
    	System.setOut(out);
    	System.out.println();
    	System.out.println( "# INICIO PROGRAMA" );
		Date fecha = new Date();
		System.out.println("# FECHA: " + fecha);
    	initialize();
    	executeSorteo();
    	printResume();
    	System.out.println( "# FIN PROGRAMA" );
    }

}
