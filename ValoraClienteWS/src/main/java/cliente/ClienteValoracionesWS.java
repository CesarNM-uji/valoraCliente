package cliente;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class ClienteValoracionesWS {

	// Sustituye esta clase por tu implementación.
	// Deberías copiar y modificar ligeramente la clase cliente que has implementado por ejemplo
	// en la solución con sockets o RMI sin callbacks


    // Sustituye esta clase por tu versión de la clase ValoraLocal de la práctica 1

    /**
     * Muestra el menu de opciones y lee repetidamente de teclado hasta obtener una opción válida
     *
     * @param teclado	stream para leer la opción elegida de teclado
     * @return			opción elegida
     */
    public static int menu(Scanner teclado) {
        int opcion;
        System.out.println("\n\n");
        System.out.println("=====================================================");
        System.out.println("============            MENU        =================");
        System.out.println("=====================================================");
        System.out.println("0. Salir");
        System.out.println("1. Listar los restaurantes");
        System.out.println("2. Consultar valoraciones de un restaurante dado");
        System.out.println("3. Hacer una valoración");
        System.out.println("4. Borrar una valoración");
        System.out.println("5. Modificar una valoración");
        do {
            System.out.print("\nElige una opción (0..5): ");
            opcion = teclado.nextInt();
        } while ( (opcion<0) || (opcion>5) );
        teclado.nextLine(); // Elimina retorno de carro del buffer de entrada
        return opcion;
    }

    /**
     * Devuelve la fecha actual en el formato dd-mm-aaaa
     * @return fecha actual en formato dd-mm-aaaa
     */
    static private String fechaHoy() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate hoy = LocalDate.now();
        return dtf.format(hoy);

    }

    /**
     * Lee un valor de estrellas hasta que se teclea uno valido: (0..5)
     * @param teclado	teclado del que se lee
     * @return	numero de estrellas leído
     */
    private static long leeEstrellas(Scanner teclado) {
        long key = teclado.nextLong();
        while (key<0 || key>5){
            System.out.println("Escribe un número de estrellas válido: ");
            key = new Scanner(System.in).nextLong();
        }
        return key;
    }


    /**
     * Programa principal. Muestra el menú repetidamente y atiende las peticiones del cliente.
     *
     * @param args	no se usan argumentos de entrada al programa principal
     */
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        // Crea un gestor de valoraciones
        AuxiliarValoracionesWS gestor = new AuxiliarValoracionesWS();//LINEA CAMBIADA

        System.out.print("Introduce tu código de cliente: ");
        String codcliente = teclado.nextLine();

        int opcion;
        do {
            opcion = menu(teclado);
            switch (opcion) {
                case 0 -> { // Guardar los datos en el fichero y salir del programa
                    gestor.guardaDatos();
                }
                case 1 -> { // Listar los nombres de los restaurantes
                    //Lista los restaurantes con el jsonArray
                    JSONArray jsonArray = gestor.listaRestaurantes();
                    System.out.println(jsonArray.toJSONString());

                }
                case 2 -> { // Consultar valoraciones de un restaurante dado
                    System.out.println("Escribe el nombre del restaurante: ");
                    Scanner scanner=new Scanner(System.in);
                    //Recogemos el jsonarray con el nombre
                    JSONArray jsonArray=gestor.consultaValoraciones(scanner.next());
                    //Si el tamaño del array es 0 significa que no hay valoraciones
                    if(jsonArray.size()==0){
                        System.out.println("No hay valoraciones para este restaurante");
                    }else{
                        System.out.println(jsonArray.toJSONString());
                    }

                }
                case 3 -> { // Hacer una valoración
                    //Pedimos los datos de la valoracion
                    System.out.println("Escribe el nombre del restaurante: ");
                    String rest=new Scanner(System.in).next();
                    System.out.println("Estrellas de la valoracion: ");
                    long estrellas =leeEstrellas(new Scanner(System.in));
                    System.out.println("Escribe un comentario: ");
                    String comentario=new Scanner(System.in).nextLine();
                    JSONObject jsonObject = gestor.hazValoracion(codcliente,rest,fechaHoy(),estrellas,comentario);
                    //si el tamalo es 0 ya hay una valoracion en esa fecha
                    if(jsonObject.size()==0) {
                        System.out.println("No puedes hacer más de una valoración al restaurante el mismo día");
                    }
                    else{
                        //la crea y te la muesta
                        System.out.println("Valoración creada");
                        System.out.println(jsonObject.toJSONString());
                    }

                }
                case 4 -> { // Borrar una valoración tuya
                    System.out.println("Escribe el nombre del restaurante: ");
                    String rest=new Scanner(System.in).next();
                    System.out.println("Escribe la fecha (formato:dd-MM-yyyy): ");
                    String fecha=new Scanner(System.in).next();
                    JSONObject jsonObject = gestor.borraValoracion(codcliente,rest,fecha);
                    if(jsonObject.size()==0) {
                        System.out.println("No puedes borrar esta valoración");
                    }
                    else{
                        System.out.println("Valoración borrada");
                        System.out.println(jsonObject.toJSONString());
                    }
                }
                case 5 -> { // Modificar una valoración tuya
                    System.out.println("Escribe el nombre del restaurante: ");
                    String rest=new Scanner(System.in).next();
                    System.out.println("Estrellas de la valoracion: ");
                    long estrellas =leeEstrellas(new Scanner(System.in));
                    System.out.println("Escribe la fecha (formato:dd-MM-yyyy): ");
                    String fecha=new Scanner(System.in).next();
                    System.out.println("Escribe un comentario: ");
                    String comentario=new Scanner(System.in).nextLine();
                    JSONObject jsonObject = gestor.modificaValoracion(codcliente,rest,fecha,estrellas,comentario);
                    if(jsonObject.size()==0) {

                        System.out.println("No puedes modificar esta valoración");
                    }
                    else{
                        System.out.println("Valoración modificada");
                        System.out.println(jsonObject.toJSONString()); /*QUIZÁS PODEMOS DEVOLVER SOLO EL COMENTARIO??*/
                    }

                }
            } // fin switch

        } while (opcion != 0);

    } // fin de main

    // Modifícala para que instancie un objeto de la clase AuxiliarClienteValora

    // Modifica todas las llamadas al objeto de la clase GestorValoraciones
    // por llamadas al objeto de la clase AuxiliarClienteValora.
    // Los métodos a llamar tendrán la misma signatura.-

} // fin class
