package Packete1;

import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Funciones {
    //    private ListaSimple listaVentanillas = new ListaSimple();
    public String leerArchivo() {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        String contenido = "";

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            Scanner Entradaruta = new Scanner(System.in);
            System.out.println("Carga Masiva de Clientes");
            System.out.print("Ingresar ruta del archivo:  ");
            String ruta = Entradaruta.nextLine();

            archivo = new File(ruta);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido += linea;
            }
            System.out.println(contenido);
            ListaSimple colaRecepcion = crearClientes(contenido);
            ListaSimple listaVentanillas = crearVentanillas();
            ListaSimple colaImpresoras = new ListaSimple();
            menu(colaRecepcion, listaVentanillas, colaImpresoras, 0, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();

            }
        }
        return contenido;
    }

    public ListaSimple crearClientes(String contenido) {
        ListaSimple colaRecepcion = new ListaSimple();

        try {
            Gson gson = new Gson();
            //Convirtiendo el contenido a diccionario, esto devuelve
            //Cliente1 que es otro json
            Map diccionarioClienteN = gson.fromJson(contenido, Map.class);
            Set<String> keys = diccionarioClienteN.keySet();
            for (String key : keys) {
                System.out.println(key);
                //Covertimos el diccionario a string para poder generar
                //otro diccionario pasando el string
                String json = gson.toJson(diccionarioClienteN.get(key));
                //Creamos el diccionario con los atributos de los clientes
                Map diccionarioValores = gson.fromJson(json, Map.class);
//            System.out.println(diccionarioValores);
//            Set<String> keysA = diccionarioValores.keySet();
                int id_cliente = Integer.parseInt(diccionarioValores.get("id_cliente").toString());
                int img_bw = Integer.parseInt(diccionarioValores.get("img_bw").toString());
                int img_color = Integer.parseInt(diccionarioValores.get("img_color").toString());
                String nombre_cliente = diccionarioValores.get("nombre_cliente").toString();

                Cliente nuevoCliente = new Cliente(id_cliente, nombre_cliente, img_color, img_bw, img_color, img_bw);
                colaRecepcion.insertarFinal(nuevoCliente);
//            System.out.println(nuevoCliente.mostrarDatos());
                //Usar la siguiente sintaxis si no se saben las claves
//            for(String keyA:keysA){
//                System.out.print(keyA+" ");
//                String valor = diccionarioValores.get(keyA).toString();
//                System.out.print(valor+" ");
//            }
//            System.out.println();
            }

            System.out.println("Clientes Cargados exitosamente");
            verColaRecepcion(colaRecepcion);

        } catch (Exception e) {
            System.out.println("Error al cargar clientes");
//            e.printStackTrace();

        }
        return colaRecepcion;
    }

    public void crearGraphviz(ListaSimple lista) {
        try {
            String ruta = "graph.dot";
            String contenido = "Contenido de ejemplo";
            StringBuilder nodos = new StringBuilder();
            StringBuilder conectarNodos = new StringBuilder();
            nodos.append("digraph ejemplo {\n");
            Nodo aux = lista.getPrimero();
            int id = 0;
            int idAnterior = 0;
            while (aux != null) {
                Cliente cliente = (Cliente) aux.getDato();
                nodos.append(String.format("%d [label=\"%s\"]", id, cliente.getNombre_cliente()));
                nodos.append("\n");
                aux = aux.getSiguiente();
                if (id > 0) {
                    conectarNodos.append(idAnterior + "->" + id + "\n");
                }
                idAnterior = id;
                id++;


            }
            nodos.append(conectarNodos);
            nodos.append("rankdir=TB\n}");
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(nodos.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String parametros[] = new String[5];
        parametros[0] = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
        parametros[1] = "-Tpng";
        parametros[2] = "C:\\Users\\diego\\Desktop\\USAC\\EstructurasDeDatos\\EDD_UDRAWING_FASE_201903909\\Fase1\\graph.dot";
        parametros[3] = "-o";
        parametros[4] = "C:\\Users\\diego\\Desktop\\USAC\\EstructurasDeDatos\\EDD_UDRAWING_FASE_201903909\\Fase1\\g.png";

        try {
            Process proceso = Runtime.getRuntime().exec(parametros, null);
            proceso.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("errror");
            e.printStackTrace();
        }

    }

    public ListaSimple crearVentanillas() {
        System.out.println("Cantidad de ventanillas");
        Scanner EntradaNumeroVentanillas = new Scanner(System.in);
        System.out.print("Ingresar la cantidad de ventanillas:  ");
        int numeroVentanillas = EntradaNumeroVentanillas.nextInt();
        ListaSimple listaVentanillas = new ListaSimple();

        try {
            for (int i = 1; i <= numeroVentanillas; i++) {
                Ventanilla nuevaVentanilla = new Ventanilla("Ventanilla" + i);
                listaVentanillas.insertarFinalAbajo(nuevaVentanilla);
            }
            System.out.println("Ventanillas creadas exitosamente");
            verListaVentanillas(listaVentanillas);
//            ejecutarPaso(listaVentanillas);
//            menu(null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaVentanillas;
    }

    public void menu(ListaSimple colaRecepcion, ListaSimple listaVentanillas, ListaSimple colaImpresoras, int numeroPaso,
                     boolean colorDisponible, boolean BNDisponible) {
        Scanner entradaEscaner = new Scanner(System.in);
        System.out.println("*****MENU*****");
        System.out.println("1. Parametros Iniciales");
        System.out.println("2. Ejecutar Paso");
        System.out.println("3. Estado en memoria de las estructuras");
        System.out.println("4. Reportes");
        System.out.println("5. Datos del estudiante");
        System.out.println("6. Salir");
        System.out.print("Seleccionar Opcion:  ");
        String opcion = entradaEscaner.nextLine();
        switch (opcion) {
            case "1":
                leerArchivo();
                break;
            case "2":
                ejecutarPaso(colaRecepcion, listaVentanillas, colaImpresoras, numeroPaso, colorDisponible, BNDisponible);
                break;
            case "6":
                break;
            default:
                System.out.println("Opcion no disponible");
                break;

        }
        System.out.println();
    }

    public void verColaRecepcion(ListaSimple colaRecepcion) {
        try {
            String ruta = "colaRecepcion.dot";
            StringBuilder nodos = new StringBuilder();
            StringBuilder conectarNodos = new StringBuilder();
            nodos.append("digraph ejemplo {\nnode[shape=box]\nedge[arrowhead=none]\n");
            Nodo aux = colaRecepcion.getPrimero();
            int id = 0;
            int idAnterior = 0;
            while (aux != null) {
                Cliente cliente = (Cliente) aux.getDato();
                nodos.append(String.format("%d [label=\"%s\\nIMG C %d\\nIMG BN %d\"]", id,
                        cliente.getNombre_cliente(), cliente.getImg_color(), cliente.getImg_bw()
                ));
                nodos.append("\n");
                aux = aux.getSiguiente();
                if (id > 0) {
                    conectarNodos.append(idAnterior + "->" + id + "\n");
                }
                idAnterior = id;
                id++;


            }
            nodos.append(conectarNodos);
            nodos.append("rankdir=LR\n}");
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(nodos.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String parametros[] = new String[5];
        parametros[0] = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
        parametros[1] = "-Tpng";
        parametros[2] = "C:\\Users\\diego\\Desktop\\USAC\\EstructurasDeDatos\\EDD_UDRAWING_FASE_201903909\\Fase1\\colaRecepcion.dot";
        parametros[3] = "-o";
        parametros[4] = "C:\\Users\\diego\\Desktop\\USAC\\EstructurasDeDatos\\EDD_UDRAWING_FASE_201903909\\Fase1\\colaRecepcion.png";

        try {
            Process proceso = Runtime.getRuntime().exec(parametros, null);
            proceso.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("errror");
            e.printStackTrace();
        }

    }

    public void verListaVentanillas(ListaSimple listaVentanillas) {
        try {
            String ruta = "listaVentanillas.dot";
            StringBuilder nodos = new StringBuilder();
            StringBuilder conectarNodos = new StringBuilder();
            StringBuilder rank = new StringBuilder();

            nodos.append("digraph listaVentanillas {\nnode[shape=box]\nedge[arrowhead=none]\n");
            Nodo aux = listaVentanillas.getPrimeroAbajo();
            int id = 0;
            int idAnterior = 0;
            boolean esPrimeraVentanilla = true;
            while (aux != null) {
                Ventanilla ventanilla = (Ventanilla) aux.getDato();
                ventanilla.setIdGrafo(id);
                nodos.append(String.format("%d [label=%s]", id, ventanilla.getNumeroVentanilla()));
                nodos.append("\n");

                if (esPrimeraVentanilla==false) {
                    conectarNodos.append(idAnterior + "->" + id + "\n");
                }

                idAnterior = id;
                id++;
                if (ventanilla.getCliente()!=null){
                    nodos.append(String.format("%d [label=\"%s\\nIMG C %d\\nIMG BN %d\"]\n", id,
                            ventanilla.getCliente().getNombre_cliente(), ventanilla.getCliente().getImgColorConstante(),
                            ventanilla.getCliente().getImgBNConstante()
                    ));
                    conectarNodos.append(id + "->" + idAnterior + "\n");
                    rank.append(String.format("{rank=same; %d; %d}\n", id, idAnterior));
                    id++;
                    if (ventanilla.getListaImagenes()!=null){
                        int idAnteriorImg = idAnterior;
                        Nodo auxImg = ventanilla.getListaImagenes().getPrimero();
                        while (auxImg!=null){
                            Imagen imagen = (Imagen) auxImg.getDato();
                            nodos.append(String.format("%d[label=%s]\n", id, imagen.getTipo()));
                            conectarNodos.append(idAnteriorImg+"->"+id+"\n");
                            rank.append(String.format("{rank=same; %d; %d}\n", idAnteriorImg, id));
                            idAnteriorImg = id;
                            id++;
                            auxImg = auxImg.getSiguiente();

                        }
                    }

                }
                esPrimeraVentanilla=false;
                aux = aux.getSiguienteAbajo();

            }
//            aux = listaVentanillas.getPrimero();
//            Nodo aux2 = listaVentanillas.getPrimeroAbajo();
////            System.out.println(aux.getDato());
//            while (aux != null && aux2 != null) {
//                Cliente cliente = (Cliente) aux.getDato();
//                Ventanilla ventanilla = (Ventanilla) aux2.getDato();
//                nodos.append(String.format("%d [label=\"%s\\nIMG C %d\\nIMG BN %d\"]\n", id,
//                        cliente.getNombre_cliente(), cliente.getImg_color(), cliente.getImg_bw()
//                ));
//
//                conectarNodos.append(id + "->" + ventanilla.getIdGrafo() + "\n");
//                rank.append(String.format("{rank=same; %d; %d}\n", id, ventanilla.getIdGrafo()));
//                id++;
//                if (ventanilla.getListaImagenes() != null) {
//                    Nodo auxImg = ventanilla.getListaImagenes().getPrimero();
//                    Imagen img;
//                    if (auxImg != null) {
//                        img = (Imagen) auxImg.getDato();
//                        nodos.append(String.format("%d [label=%s]\n", id, img.getTipo()
//                        ));
//                        conectarNodos.append(ventanilla.getIdGrafo() + "->" + id + "\n");
//                        rank.append(String.format("{rank=same; %d; %d}\n", id, ventanilla.getIdGrafo()));
//                        idAnterior = id;
//                        id++;
//                        auxImg = auxImg.getSiguiente();
//                    }
//                    while (auxImg != null) {
//                        img = (Imagen) auxImg.getDato();
//                        nodos.append(String.format("%d [label=%s]\n", id, img.getTipo()
//                        ));
//                        conectarNodos.append(idAnterior + "->" + id + "\n");
//                        rank.append(String.format("{rank=same; %d; %d}\n", idAnterior, id));
//                        idAnterior = id;
//                        id++;
//                        auxImg = auxImg.getSiguiente();
//                    }
//                }
//
//
//                aux = aux.getSiguiente();
//                aux2 = aux2.getSiguienteAbajo();
//
//
//            }
//            aux = listaVentanillas.getPrimeroAbajo();
//            aux2 = listaVentanillas.getPrimero();
//            while (aux != null && aux2 != null) {
//                Ventanilla ventanilla = (Ventanilla) aux.getDato();
//                System.out.println(ventanilla.getNumeroVentanilla() + " contiene el cliente: ");
//                Cliente cliente = (Cliente) aux2.getDato();
//
//                System.out.println(cliente.getNombre_cliente());
//                System.out.println("Con las imagenes:");
//                if (ventanilla.getListaImagenes() != null) {
//
//
//                    ListaSimple pilaImg = ventanilla.getListaImagenes();
//                    Nodo auxImg = pilaImg.getPrimero();
//                    while (auxImg != null) {
//                        Imagen imagen = (Imagen) auxImg.getDato();
//                        System.out.println(imagen.getTipo());
//                        auxImg = auxImg.getSiguiente();
//                    }
//                }
//
//                aux = aux.getSiguiente();
//                aux2 = aux2.getSiguiente();
//
//            }
            nodos.append(conectarNodos);
            nodos.append(rank);
            nodos.append("rankdir=TB\n}");
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(nodos.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String parametros[] = new String[5];
        parametros[0] = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
        parametros[1] = "-Tpng";
        parametros[2] = "C:\\Users\\diego\\Desktop\\USAC\\EstructurasDeDatos\\EDD_UDRAWING_FASE_201903909\\Fase1\\listaVentanillas.dot";
        parametros[3] = "-o";
        parametros[4] = "C:\\Users\\diego\\Desktop\\USAC\\EstructurasDeDatos\\EDD_UDRAWING_FASE_201903909\\Fase1\\listaVentanillas.png";

        try {
            Process proceso = Runtime.getRuntime().exec(parametros, null);
            proceso.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("errror");
            e.printStackTrace();
        }
    }

    public void ejecutarPaso(ListaSimple colaRecepcion, ListaSimple listaVentanillas, ListaSimple colaImpresoras,
                             int numeroPaso, boolean colorDisponible, boolean BNDisponible) {
        numeroPaso++;
        //El error esta en este metodo
//            Cliente clienteAInsertar = (Cliente) colaRecepcion.getPrimero().getDato();
        //Eliminamos el cliente de la cola de recepcion y se inserta en la lista de ventanillas
//            colaRecepcion.elimiinarInicio();
        Nodo aux = listaVentanillas.getPrimeroAbajo();
//            Nodo aux2 = listaVentanillas.getPrimero();
        /*Lo primero es recorrer las ventanillas y ver cual esta vacia
         */
        if (colaImpresoras.getPrimero()==null){
            ListaSimple colaColor = new ListaSimple();
            colaImpresoras.insertarFinal(colaColor);
            ListaSimple colaBN = new ListaSimple();
            colaImpresoras.insertarFinal(colaBN);
        }
        boolean unSoloPaso = true;
        boolean unSoloCliente = true;
        System.out.println("-".repeat(10)+"PASO "+numeroPaso+"-".repeat(10));
        while (aux != null) {
            Ventanilla ventanilla = (Ventanilla) aux.getDato();
            if (ventanilla.isEstaDisponible() && colaRecepcion.getPrimero() != null && unSoloCliente) {
                unSoloCliente=false;
                Cliente clienteAInsertar = (Cliente) colaRecepcion.getPrimero().getDato();
                System.out.println(clienteAInsertar.getNombre_cliente()+" Ingresa a la "+ventanilla.getNumeroVentanilla());
//                System.out.println(ventanilla.getNumeroVentanilla() + " disponible");
//                System.out.println("Cliente " + clienteAInsertar.getNombre_cliente() + " insertado");
                colaRecepcion.elimiinarInicio();
//                    listaVentanillas.insertarFinal(clienteAInsertar);
                ventanilla.setCliente(clienteAInsertar);
                ventanilla.setEstaDisponible(false);
                aux = aux.getSiguienteAbajo();
                continue;
            }

//                System.out.println(ventanilla.getNumeroVentanilla());
//                System.out.println("Tiene al cliente " + ventanilla.getCliente().getNombre_cliente());
//                System.out.println("Con " + ventanilla.getCliente().getImg_color() + " " + ventanilla.getCliente().getImg_bw());
                //Aqui recorro los clientes en las ventanillas
                if (ventanilla.getCliente()==null){
                    aux = aux.getSiguienteAbajo();
                    continue;
                }
                Cliente cliente = (Cliente) ventanilla.getCliente();
                ListaSimple pilaImagenes;
                if (ventanilla.getListaImagenes() != null) {
                    pilaImagenes = ventanilla.getListaImagenes();

                } else {
//                    System.out.println("Lista imagenes de " + ventanilla.getNumeroVentanilla() + " es null");
                    pilaImagenes = new ListaSimple();
                }
                //Recibiendo imgs en ventanilla

                if (cliente.getImg_color() > 0) {
                    System.out.println("La "+ventanilla.getNumeroVentanilla()+" recibio una imagen");
//                    System.out.println("Cliente " + cliente.getNombre_cliente() + " Insertando imagen a color");
                    Imagen nuevaImg = new Imagen("Color", cliente);
                    nuevaImg.setPasos(2);
                    pilaImagenes.insertarInicio(nuevaImg);
                    ventanilla.setListaImagenes(pilaImagenes);
                    cliente.setImg_color(cliente.getImg_color() - 1);

                } else if (cliente.getImg_bw() > 0) {
                    System.out.println("La "+ventanilla.getNumeroVentanilla()+" recibio una imagen");
//                    System.out.println(cliente.getNombre_cliente() + " Insertando imagen BN");
                    Imagen nuevaImg = new Imagen("BN", cliente);
                    nuevaImg.setPasos(1);
                    pilaImagenes.insertarInicio(nuevaImg);
                    ventanilla.setListaImagenes(pilaImagenes);
                    cliente.setImg_bw(cliente.getImg_bw() - 1);

                }
                if(cliente.getImg_color()==0 && cliente.getImg_bw()==0){
                    System.out.println();

                    Nodo auxImg = ventanilla.getListaImagenes().getPrimero();
                    ListaSimple colaColor = (ListaSimple) colaImpresoras.getPrimero().getDato();
                    ListaSimple colaBN = (ListaSimple) colaImpresoras.getPrimero().getSiguiente().getDato();
                    while (auxImg!=null){
                        Imagen imagen = (Imagen) auxImg.getDato();
                        if(imagen.getTipo()=="Color"){
                            colaColor.insertarFinal(imagen);
                        }else if(imagen.getTipo()=="BN"){
                            colaBN.insertarFinal(imagen);
                        }
                        auxImg = auxImg.getSiguiente();
                    }
                    colaImpresoras.insertarFinal(colaColor);
                    colaImpresoras.insertarFinal(colaBN);
//                    System.out.println(ventanilla.getNumeroVentanilla() + " disponible");
                    ventanilla.setEstaDisponible(true);
                    ventanilla.setCliente(null);
//                    System.out.println("Se elimino el cliente nombre" + cliente.getNombre_cliente());
                    ventanilla.setListaImagenes(null);
                    System.out.println("El cliente"+cliente.getNombre_cliente()+" sale de la "+ventanilla.getNumeroVentanilla()+
                            "sus imagenes se envian a la cola de impresoras");

                    colaImpresoras = pasoImpresora(colaImpresoras, colorDisponible, BNDisponible);
                    verColaImpresoras(colaImpresoras);

                    continue;
                }

            aux = aux.getSiguienteAbajo();


        }
//            verListaVentanillas(listaVentanillas);
        verListaVentanillas(listaVentanillas);
        menu(colaRecepcion, listaVentanillas, colaImpresoras, numeroPaso, colorDisponible, BNDisponible);
    }

    public void verColaImpresoras(ListaSimple colaImpresoras){
        ListaSimple colaColor = (ListaSimple) colaImpresoras.getPrimero().getDato();
        ListaSimple colaBN = (ListaSimple) colaImpresoras.getPrimero().getSiguiente().getDato();
        Nodo auxColor = colaColor.getPrimero();
        System.out.println("Impresora Color: "+colaColor.getSize());
        System.out.println("Impresora BN: "+colaBN.getSize());
    }

    public ListaSimple pasoImpresora(ListaSimple colaImpresoras, boolean colorDisponible, boolean BNDisponible){
        ListaSimple colaColor = (ListaSimple) colaImpresoras.getPrimero().getDato();
        ListaSimple colaBN = (ListaSimple) colaImpresoras.getPrimero().getSiguiente().getDato();

        if(colaColor.getSize()>0){
            Imagen imagen = (Imagen) colaColor.getPrimero().getDato();

            if(imagen.getPasos()==2){
                imagen.setPasos(1);
            }else{
                System.out.println("Se imprimio una imagen a color del cliente "+imagen.getCliente().getNombre_cliente());
                colaColor.elimiinarInicio();
                colaImpresoras = new ListaSimple();
                colaImpresoras.insertarFinal(colaColor);
                colaImpresoras.insertarFinal(colaBN);
                if(colaColor.getSize()>0){
                    Imagen imagen1 = (Imagen) colaColor.getPrimero().getDato();
                    if(imagen1.getCliente() != imagen.getCliente()){
                        imagen.getCliente().setTieneTodasSusImgs(true);
                        System.out.println("Se terminaron de imprimir las imagenes del cliente "+imagen.getCliente().getNombre_cliente());
                    }
                }else {
                    System.out.println("Se terminaron de imprimir las imagenes del cliente "+imagen.getCliente().getNombre_cliente());
                }
            }
        }
        if(colaBN.getSize()>0){
            Imagen imagen = (Imagen) colaBN.getPrimero().getDato();
            colaBN.elimiinarInicio();
            colaImpresoras = new ListaSimple();
            colaImpresoras.insertarFinal(colaColor);
            colaImpresoras.insertarFinal(colaBN);
            System.out.println("Se imprimio una imagen BN del cliente "+imagen.getCliente().getNombre_cliente());
            if(colaBN.getSize()>0){
                Imagen imagen1 = (Imagen) colaBN.getPrimero().getDato();
                if(imagen1.getCliente().getNombre_cliente() != imagen.getCliente().getNombre_cliente()){
                    imagen.getCliente().setTieneTodasSusImgs(true);
                    System.out.println("Se terminaron de imprimir las imagenes del cliente "+imagen.getCliente().getNombre_cliente());

                }

            }else {
                System.out.println("Se terminaron de imprimir las imagenes del cliente "+imagen.getCliente().getNombre_cliente());
            }
        }





        return colaImpresoras;

    }



    //Cambiando disponibilidad de las ventanillas
//        Nodo aux = listaVentanillas.getPrimeroAbajo();
//        while (aux!=null){
//            Ventanilla ventanilla = (Ventanilla) aux.getDato();
//            System.out.println(ventanilla.isEstaDisponible());
//            ventanilla.setEstaDisponible(false);
//            aux = aux.getSiguienteAbajo();
//        }


    public void listas() {
        ListaSimple lista = new ListaSimple();
        lista.insertarFinal("jajaj");
        lista.insertarFinal("ssx");
        lista.insertarFinal("xsx");
        lista.insertarFinal("ddd");
//        System.out.println(lista.primero.getDato());
//        lista.recorrer();

    }
}
