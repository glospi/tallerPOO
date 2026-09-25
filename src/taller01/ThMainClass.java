// Lucas Ramirez - 22.325.313-k - ITI

package taller01;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Sistema de Control de Acceso al Grupo de POO.
 * 
 * Esta clase contiene el programa principal y métodos auxiliares procedimentales
 * para gestionar el control de ingreso al grupo del curso, permitiendo la carga
 * de datos de alumnos y solicitudes, filtrado automático, inscripción manual,
 * administración del curso, generación de reportes versionados y análisis estadístico.
 */
public class ThMainClass
{

    public static void main(String[] args) throws IOException
    {
        boolean boton = true;
        boolean filtro = true;
        boolean filtro2 = true;
        String[][] alumnos = null;
        String[][] solicitudes = null;
        String[][][] datos = new String[3][100][100];
        Scanner sc = new Scanner(System.in);

        String rutaAlumnos = "txt/Alumnos.txt";
        String carpetaReportes = "txt/";

        do
        {
            imprimirMenu();
            short seleccion = leerOpcion(sc);

            switch (seleccion)
            {

                case 1:
                    boolean a = true;
                    do
                    {
                        alumnos = cargar(rutaAlumnos);
                        solicitudes = cargarSolicitudes("txt/Solicitudes.txt");

                        if (alumnos == null || solicitudes == null)
                        {
                            System.out.print("¿Desea terminar el programa?\n(S/N) default S: ");
                            String respuesta = sc.nextLine();

                            if (!respuesta.equalsIgnoreCase("N"))
                            {
                                a = false;
                                break;
                            }

                            a = true;
                        }
                        else
                        {
                            for (int i = 0; i < alumnos.length; i++)
                            {
                                for (int c = 0; c < 4; c++)
                                {
                                    datos[2][i][c] = alumnos[i][c];
                                }
                            }

                            a = false;
                            filtro = false;
                            break;
                        }

                    } while (a);
                    break;

                case 2:
                    int posicionador = 0;
                    int posicionadorRechazados = 0;

                    if (filtro)
                    {
                        System.out.println("No se han cargado archivos");
                        break;
                    }

                    for (int i = 0; i < solicitudes.length; i++)
                    {
                        if (solicitudes[i] == null || solicitudes[i][0] == null)
                        {
                            break;
                        }

                        boolean yaAdmitido = false;
                        for (int k = 0; k < posicionador; k++)
                        {
                            if (solicitudes[i][0].equalsIgnoreCase(datos[0][k][0]) && solicitudes[i][1].equalsIgnoreCase(datos[0][k][1]))
                            {
                                yaAdmitido = true;
                                break;
                            }
                        }
                        if (!yaAdmitido)
                        {

                            boolean encontrado = false;

                            for (int j = 0; j < datos[2].length; j++)
                            {
                                if (datos[2][j][0] != null && solicitudes[i][0].equalsIgnoreCase(datos[2][j][0]) && solicitudes[i][1].equalsIgnoreCase(datos[2][j][1]))
                                {
                                    datos[0][posicionador][0] = datos[2][j][0];
                                    datos[0][posicionador][1] = datos[2][j][1];
                                    datos[0][posicionador][2] = datos[2][j][2];
                                    datos[0][posicionador][3] = datos[2][j][3];
                                    System.out.println("[OK] " + datos[0][posicionador][0] + " " + datos[0][posicionador][1] + "-> " + datos[0][posicionador][3]);
                                    posicionador++;
                                    encontrado = true;
                                    break;
                                }
                            }

                            if (!encontrado)
                            {
                                datos[1][posicionadorRechazados][0] = solicitudes[i][0];
                                datos[1][posicionadorRechazados][1] = solicitudes[i][1];
                                System.out.println("[RECHAZO] " + datos[1][posicionadorRechazados][0] + " " + datos[1][posicionadorRechazados][1] + "-> no pertenece a ningun paralelo");
                                posicionadorRechazados++;
                            }
                        }
                    }
                    System.out.printf("Resumen: %d admitidos / %d rechazados.%n", posicionador, posicionadorRechazados);
                    filtro2 = false;
                    break;

                case 3:

                    short key = 1;
                    if (filtro)
                    {
                        System.out.println("No se han cargado los datos. Por favor cargue los archivos con la opcion 1.");
                        break;
                    }

                    do
                    {
                        boolean valido = false;
                        opcionDosMenu();
                        key = leerOpcion(sc);

                        switch (key)
                        {
                            case 1:

                                System.out.print("Ingrese el nombre del estudiante, use espacio 'Nombre Apellido': ");
                                String nombre = sc.nextLine();
                                boolean estaEnCurso = buscarNombreEnlista(nombre, datos);

                                if (!estaEnCurso)
                                {

                                    System.out.println("No se encuentra el alumno, se registro su nomnbre en rechazados");

                                }
                                else
                                {
                                    System.out.println("El alumno ya se registro dentro del grupo");
                                }
                                break;

                            case 2:
                                String rut = "";
                                do
                                {
                                    System.out.print("Ingrese el rut del estudiante sin puntos y con guion '-': ");
                                    rut = sc.nextLine();
                                    valido = rutValidador(rut);
                                    if (!valido)
                                    {

                                        System.out.println("Ingrese un RUT valido");

                                    }


                                } while (!valido);
                                boolean estaEnCurso1 = buscarRutEnLista(rut, datos);
                                if (!estaEnCurso1)
                                {
                                    System.out.println("No se encontro estudiante, su rut se registro en rechazados");
                                }
                                else
                                {
                                    System.out.println("Se encontro alumno en curso y se admitio al grupo");
                                }
                                break;

                            case 3:
                                System.out.println("Volviendo al menu principal...");
                                break;

                            default:
                                System.out.println("Error de ingreso");
                                break;
                        }

                    } while (key != 3);
                    break;

                case 4:
                    if (filtro)
                    {
                        System.out.println("No se han cargado los datos. Por favor cargue los archivos con la opcion 1.");
                        break;
                    }

                    short opcionAdmin = 0;

                    do
                    {
                        administracionCurso();

                        opcionAdmin = leerOpcion(sc);

                        switch (opcionAdmin)
                        {

                            case 1:
                                System.out.print("Ingrese RUT del alumno: ");
                                String rutCambiar = sc.nextLine();
                                boolean encontradoCambiar = false;

                                for (int i = 0; i < datos[2].length; i++)
                                {
                                    if (datos[2][i][2] != null && datos[2][i][2].equalsIgnoreCase(rutCambiar))
                                    {
                                        encontradoCambiar = true;
                                        System.out.println("Alumno: " + datos[2][i][0] + " " + datos[2][i][1]
                                                + " (actualmente en " + datos[2][i][3] + ")");

                                        String nuevoParalelo = "";
                                        do
                                        {
                                            System.out.print("Nuevo paralelo (C1/C2): ");
                                            nuevoParalelo = sc.nextLine();

                                            if (!nuevoParalelo.equalsIgnoreCase("C1")
                                                    && !nuevoParalelo.equalsIgnoreCase("C2"))
                                            {
                                                System.out.println("Paralelo invalido. Ingrese C1 o C2.");
                                            }
                                        } while (!nuevoParalelo.equalsIgnoreCase("C1")
                                                && !nuevoParalelo.equalsIgnoreCase("C2"));

                                        datos[2][i][3] = nuevoParalelo.toUpperCase();

                                        for (int k = 0; k < datos[0].length; k++)
                                        {
                                            if (datos[0][k][2] != null && datos[0][k][2].equalsIgnoreCase(rutCambiar))
                                            {
                                                datos[0][k][3] = datos[2][i][3];
                                                break;
                                            }
                                        }

                                        System.out.println("Paralelo actualizado correctamente.");
                                        guardarAlumnos(rutaAlumnos, datos[2]);
                                        break;
                                    }
                                }

                                if (!encontradoCambiar)
                                {
                                    System.out.println("No se encontro alumno con ese RUT en el curso.");
                                }
                                break;

                            case 2:
                                System.out.print("Ingrese RUT del alumno a eliminar: ");
                                String rutEliminar = sc.nextLine();
                                boolean eliminado = false;

                                for (int i = 0; i < datos[2].length; i++)
                                {
                                    if (datos[2][i][2] != null && datos[2][i][2].equalsIgnoreCase(rutEliminar))
                                    {
                                        System.out.println("Alumno eliminado: "
                                                + datos[2][i][0] + " " + datos[2][i][1]);

                                        for (int j = i; j < datos[2].length - 1; j++)
                                        {
                                            datos[2][j][0] = datos[2][j + 1][0];
                                            datos[2][j][1] = datos[2][j + 1][1];
                                            datos[2][j][2] = datos[2][j + 1][2];
                                            datos[2][j][3] = datos[2][j + 1][3];
                                        }

                                        datos[2][datos[2].length - 1][0] = null;
                                        datos[2][datos[2].length - 1][1] = null;
                                        datos[2][datos[2].length - 1][2] = null;
                                        datos[2][datos[2].length - 1][3] = null;

                                        for (int k = 0; k < datos[0].length; k++)
                                        {
                                            if (datos[0][k][2] != null && datos[0][k][2].equalsIgnoreCase(rutEliminar))
                                            {
                                                datos[0][k][0] = null;
                                                datos[0][k][1] = null;
                                                datos[0][k][2] = null;
                                                datos[0][k][3] = null;
                                                break;
                                            }
                                        }

                                        guardarAlumnos(rutaAlumnos, datos[2]);
                                        eliminado = true;
                                        break;
                                    }
                                }

                                if (!eliminado)
                                {
                                    System.out.println("No se encontro alumno con ese RUT en el curso.");
                                }
                                break;

                            case 3:
                                System.out.print("Ingrese RUT del nuevo alumno (sin puntos, con guion): ");
                                String rutNuevo = sc.nextLine().toUpperCase();

                                if (!rutValidador(rutNuevo))
                                {
                                    System.out.println("RUT invalido.");
                                    break;
                                }

                                boolean duplicado = false;
                                for (int i = 0; i < datos[2].length; i++)
                                {
                                    if (datos[2][i][2] != null && datos[2][i][2].equalsIgnoreCase(rutNuevo))
                                    {
                                        duplicado = true;
                                        break;
                                    }
                                }

                                if (duplicado)
                                {
                                    System.out.println("El RUT ya existe en el curso.");
                                    break;
                                }

                                System.out.print("Ingrese nombre: ");
                                String nombreNuevo = sc.nextLine();

                                System.out.print("Ingrese apellido: ");
                                String apellidoNuevo = sc.nextLine();

                                if (nombreNuevo.equals("") || apellidoNuevo.equals(""))
                                {
                                    System.out.println("Nombre y apellido no pueden estar vacios.");
                                    break;
                                }

                                String paralelo = "";
                                do
                                {
                                    System.out.print("Paralelo (C1/C2): ");
                                    paralelo = sc.nextLine();

                                    if (!paralelo.equalsIgnoreCase("C1")
                                            && !paralelo.equalsIgnoreCase("C2"))
                                    {
                                        System.out.println("Paralelo invalido. Ingrese C1 o C2.");
                                    }
                                } while (!paralelo.equalsIgnoreCase("C1")
                                        && !paralelo.equalsIgnoreCase("C2"));

                                boolean agregado = false;

                                for (int i = 0; i < datos[2].length; i++)
                                {
                                    if (datos[2][i][2] == null)
                                    {
                                        datos[2][i][0] = nombreNuevo;
                                        datos[2][i][1] = apellidoNuevo;
                                        datos[2][i][2] = rutNuevo;
                                        datos[2][i][3] = paralelo.toUpperCase();

                                        agregado = true;
                                        System.out.println("Alumno inscrito correctamente.");
                                        guardarAlumnos(rutaAlumnos, datos[2]);
                                        break;
                                    }
                                }

                                if (!agregado)
                                {
                                    System.out.println("No hay espacio para mas alumnos.");
                                }
                                break;

                            case 4:
                                System.out.println("Volviendo al menu principal...");
                                break;

                            default:
                                System.out.println("Opcion invalida");
                                break;
                        }

                    } while (opcionAdmin != 4);
                    break;

                case 5:
                    if (filtro)
                    {
                        System.out.println("No se han cargado los datos. Por favor cargue los archivos con la opcion 1.");
                        break;
                    }

                    short opcionReporte = 0;

                    do
                    {
                        menuReportes();

                        opcionReporte = leerOpcion(sc);

                        switch (opcionReporte)
                        {
                            case 1:
                                generarReporteParalelo(carpetaReportes, datos, "C1");
                                break;

                            case 2:
                                generarReporteParalelo(carpetaReportes, datos, "C2");
                                break;

                            case 3:
                                generarReporteRechazados(carpetaReportes, datos);
                                break;

                            case 4:
                                System.out.println("Volviendo al menu principal...");
                                break;

                            default:
                                System.out.println("Opcion invalida");
                                break;
                        }

                    } while (opcionReporte != 4);
                    break;

                case 6:
                    if (filtro)
                    {
                        System.out.println("No se han cargado los datos. Por favor cargue los archivos con la opcion 1.");
                        break;
                    }

                    mostrarEstadisticas(datos);
                    break;

                case 7:
                    boton = false;
                    System.out.println("Saliendo del programa...");
                    sc.close();
                    break;

                default:
                    System.out.println("Opcion invalida");
            }
        } while (boton);
        sc.close();
    }

    private static boolean buscarRutEnLista(String rut, String[][][] datos)
    {
        for (int i = 0; i < 100; i++)
        {
            if (rut.equalsIgnoreCase(datos[0][i][2]))
            {
                return true;
            }
        }
        for (int j = 0; j < 100; j++)
        {
            if (datos[2][j][2] != null && datos[2][j][2].equalsIgnoreCase(rut))
            {
                for (int k = 0; k < 100; k++)
                {
                    if (datos[0][k][0] == null)
                    {
                        datos[0][k][0] = datos[2][j][0];
                        datos[0][k][1] = datos[2][j][1];
                        datos[0][k][2] = datos[2][j][2];
                        datos[0][k][3] = datos[2][j][3];
                        break;
                    }
                }
                return true;
            }
        }
        for (int i = 0; i < 100; i++)
        {
            if (datos[1][i][0] == null && datos[1][i][2] == null)
            {
                datos[1][i][2] = rut;
                return false;
            }
        }
        return false;
    }

    private static boolean buscarNombreEnlista(String nombre, String[][][] datos)
    {

        if (nombre == null)
        {
            System.out.println("Nombre invalido");
            return false;
        }
        String[] partes = nombre.split(" ");
        if (partes.length < 2)
        {
            System.out.println("Nombre invalido, añada apellido");
            return false;
        }

        if (partes[0] == null || partes[1] == null)
        {
            System.out.println("Nombre invalido, añada apellido");
            return false;
        }

        for (int i = 0; i < 100; i++)
        {
            if (partes[0].equalsIgnoreCase(datos[0][i][0]) && partes[1].equalsIgnoreCase(datos[0][i][1]))
            {
                return true;
            }
        }
        for (int j = 0; j < 100; j++)
        {
            if (datos[2][j][0] != null && partes[0].equalsIgnoreCase(datos[2][j][0]) && partes[1].equalsIgnoreCase(datos[2][j][1]))
            {
                for (int k = 0; k < 100; k++)
                {
                    if (datos[0][k][0] == null)
                    {
                        datos[0][k][0] = datos[2][j][0];
                        datos[0][k][1] = datos[2][j][1];
                        datos[0][k][2] = datos[2][j][2];
                        datos[0][k][3] = datos[2][j][3];
                        break;
                    }
                }
                return true;
            }
        }
        for (int i = 0; i < 100; i++)
        {
            if (datos[1][i][0] == null && datos[1][i][2] == null)
            {
                datos[1][i][0] = partes[0];
                datos[1][i][1] = partes[1];
                return false;
            }
        }

        return false;
    }

    private static boolean rutValidador(String rut)
    {
        if (rut == null)
        {
            return false;
        }

        // Un RUT valido tiene la forma numero-digitoVerificador
        String[] partes = rut.split("-");
        if (partes.length != 2)
        {
            return false;
        }

        int numero;
        try
        {
            numero = Integer.valueOf(partes[0]);
        }
        catch (Exception e)
        {
            return false;
        }

        // 7 u 8 digitos, sin signo ni ceros a la izquierda
        if (numero < 1000000 || numero > 99999999)
        {
            return false;
        }
        if (!(numero + "").equals(partes[0]))
        {
            return false;
        }

        String[] verificadores = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "K"};

        for (int i = 0; i < verificadores.length; i++)
        {
            if (partes[1].equalsIgnoreCase(verificadores[i]))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Lee una opcion de menu desde el teclado.
     *
     * @param sc Scanner conectado al teclado.
     * @return la opcion ingresada, o -1 si no es un numero valido.
     */
    private static short leerOpcion(Scanner sc)
    {
        try
        {
            return Short.parseShort(sc.nextLine());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    private static String[][] cargarSolicitudes(String name) throws FileNotFoundException
    {

        try
        {
            File file = new File(name);
            Scanner lector = new Scanner(file);
            String[][] array = new String[100][2];
            int i = 0;

            while (lector.hasNextLine())
            {
                String linea = lector.nextLine();
                String[] partes = linea.split("-");
                if (partes.length >= 2)
                {
                    array[i][0] = partes[0];
                    array[i][1] = partes[1];
                    i++;
                }
            }

            lector.close();
            System.out.print("\nSe cargaron esta cantidad de solicitudes: " + i + "\n");
            return array;

        }
        catch (Exception e)
        {
            System.out.println("\nNo se encontro archivo " + name);
            return null;
        }
    }

    private static String[][] cargar(String name)
    {

        try
        {
            File file = new File(name);
            Scanner lector = new Scanner(file);
            String[][] array = new String[100][4];
            int i = 0;

            while (lector.hasNextLine())
            {
                String linea = lector.nextLine();
                String[] partes = linea.split(";");
                array[i][0] = partes[0];
                array[i][1] = partes[1];
                array[i][2] = partes[2];
                array[i][3] = partes[3];
                i++;
            }

            lector.close();
            System.out.print("Se cargaron esta cantidad de alumnos: " + i);
            return array;

        }
        catch (Exception e)
        {
            System.out.println("\nNo se encontro archivo " + name);
            return null;
        }
    }

    private static void guardarAlumnos(String ruta, String[][] lista)
    {
        try
        {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(ruta));

            for (int i = 0; i < lista.length; i++)
            {
                if (lista[i][2] != null)
                {
                    escritor.write(lista[i][0] + ";" + lista[i][1] + ";" + lista[i][2] + ";" + lista[i][3]);
                    escritor.newLine();
                }
            }

            escritor.close();
            System.out.println("Cambios guardados en Alumnos.txt");
        }
        catch (IOException e)
        {
            System.out.println("No se pudo guardar Alumnos.txt");
        }
    }

    private static boolean existeArchivo(String ruta)
    {
        try
        {
            Scanner lector = new Scanner(new File(ruta));
            lector.close();
            return true;
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
    }

    private static void generarReporteParalelo(String carpetaReportes, String[][][] datos, String paralelo)
    {
        String base = "Reporte" + paralelo;
        int version = 1;

        while (existeArchivo(carpetaReportes + base + "-V" + version + ".txt"))
        {
            version++;
        }

        File archivo = new File(carpetaReportes + base + "-V" + version + ".txt");

        try
        {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo));

            escritor.write("=== Miembros del grupo - Paralelo " + paralelo + " ===");
            escritor.newLine();

            for (int i = 0; i < datos[0].length; i++)
            {
                if (datos[0][i][0] != null && paralelo.equals(datos[0][i][3]))
                {
                    escritor.write(datos[0][i][0] + " " + datos[0][i][1]
                            + " - " + datos[0][i][2]);
                    escritor.newLine();
                }
            }

            escritor.close();
            System.out.println("Reporte generado: " + base + "-V" + version + ".txt");
        }
        catch (IOException e)
        {
            System.out.println("No se pudo generar el reporte " + paralelo);
        }
    }

    private static void generarReporteRechazados(String carpetaReportes, String[][][] datos)
    {
        String base = "Rechazados";
        int version = 1;

        while (existeArchivo(carpetaReportes + base + "-V" + version + ".txt"))
        {
            version++;
        }

        File archivo = new File(carpetaReportes + base + "-V" + version + ".txt");

        try
        {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo));

            escritor.write("=== Solicitudes rechazadas ===");
            escritor.newLine();

            for (int i = 0; i < datos[1].length; i++)
            {
                if (datos[1][i][0] != null)
                {
                    escritor.write(datos[1][i][0] + " " + datos[1][i][1]
                            + " - No pertenece a ningun paralelo del curso");
                    escritor.newLine();
                }
                else if (datos[1][i][2] != null)
                {
                    escritor.write("Sin nombre registrado, RUT: " + datos[1][i][2]);
                    escritor.newLine();
                }
            }

            escritor.close();
            System.out.println("Reporte generado: " + base + "-V" + version + ".txt");
        }
        catch (IOException e)
        {
            System.out.println("No se pudo generar el reporte de rechazados");
        }
    }

    private static void mostrarEstadisticas(String[][][] datos)
    {
        int totalIntentos = 0;
        int totalRechazados = 0;
        int admitidosC1 = 0;
        int admitidosC2 = 0;
        int rechazadosSinNombre = 0;

        for (int i = 0; i < datos[0].length; i++)
        {
            if (datos[0][i][0] != null)
            {
                totalIntentos++;

                if ("C1".equals(datos[0][i][3]))
                {
                    admitidosC1++;
                }
                else if ("C2".equals(datos[0][i][3]))
                {
                    admitidosC2++;
                }
            }
        }

        for (int i = 0; i < datos[1].length; i++)
        {
            if (datos[1][i][0] != null)
            {
                totalIntentos++;
                totalRechazados++;
            }
            else if (datos[1][i][2] != null)
            {
                totalIntentos++;
                totalRechazados++;
                rechazadosSinNombre++;
            }
        }

        double porcentajeRechazo = 0;
        double tasaAdmision = 0;

        if (totalIntentos > 0)
        {
            porcentajeRechazo = (totalRechazados * 100.0) / totalIntentos;
            tasaAdmision = ((totalIntentos - totalRechazados) * 100.0) / totalIntentos;
        }

        System.out.println("--- Analisis estadistico ---");
        System.out.println("Total de intentos de ingreso: " + totalIntentos);
        System.out.println("Rechazados: " + totalRechazados + " (" + porcentajeRechazo + "%)");
        System.out.println("Admitidos por paralelo -> C1: " + admitidosC1 + " | C2: " + admitidosC2);
        System.out.println("Tasa de admision: " + tasaAdmision + "%");
        System.out.println("Rechazados sin nombre registrado (solo RUT): " + rechazadosSinNombre);
    }

    public static void menuReportes()
    {
        System.out.println("--- Generar reportes ---");
        System.out.println("1) Reporte C1");
        System.out.println("2) Reporte C2");
        System.out.println("3) Reporte rechazados");
        System.out.println("4) Volver");
        System.out.print("Ingrese opcion: ");
    }

    public static void opcionDosMenu()
    {
        System.out.println("Como desea inscribir a la persona?");
        System.out.println("1) Por nombre completo");
        System.out.println("2) Por RUT");
        System.out.println("3) Volver");
        System.out.print("Ingrese opcion: ");

    }

    public static void administracionCurso()
    {
        System.out.println("--- Administracion del curso ---");
        System.out.println("1) Cambiar paralelo de un alumno");
        System.out.println("2) Eliminar alumno del curso");
        System.out.println("3) Inscribir alumno nuevo");
        System.out.println("4) Volver");
        System.out.print("Ingrese opcion: ");
    }

    public static void imprimirMenu()
    {
        System.out.println("===== Sistema de Control del Grupo POO =====");
        System.out.println("1) Cargar archivos (Alumnos y Solicitudes)");
        System.out.println("2) Procesar solicitudes (Filtrado automatico)");
        System.out.println("3) Inscripcion manual al grupo");
        System.out.println("4) Administracion del curso");
        System.out.println("5) Generar reportes");
        System.out.println("6) Analisis estadistico");
        System.out.println("7) Salir");
    }
}