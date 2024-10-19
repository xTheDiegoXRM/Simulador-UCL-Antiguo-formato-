package models;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EliminacionDirecta {
    private List<Clubes> clubes1raPosicion;
    private List<Clubes> clubes2daPosicion;
    private List<Partidos> octavosIda;
    private List<Partidos> cuartosIda;
    private List<Partidos> semisIda;
    private List<Partidos> finalisima;

    public EliminacionDirecta(List<Grupos> grupos) {
        this.clubes1raPosicion = new ArrayList<>();
        this.clubes2daPosicion = new ArrayList<>();
        this.octavosIda = new ArrayList<>();
        this.cuartosIda = new ArrayList<>();
        this.semisIda = new ArrayList<>();
        this.finalisima = new ArrayList<>();
    
    // separo clubes en 1ra y 2da posicion    
    for (Grupos grupo : grupos) {
        List<Clubes> clubes = grupo.getClubes();
        if(clubes.size() > 2) {       
            clubes1raPosicion.add(clubes.get(0));
            clubes2daPosicion.add(clubes.get(1));
            }
        }
    }
    
    public void armarOctavosDeFinal() {
        Collections.shuffle(clubes2daPosicion);
        Collections.shuffle(clubes1raPosicion);
        
        List<Clubes> rivalesUsados = new ArrayList<>(); // evito repeticiones
        
        for (Clubes club2 : clubes2daPosicion) {
            Clubes rival = encontrarRival(club2, rivalesUsados);
            if (rival != null) {
                rivalesUsados.add(rival);
                Partidos partidoIda = new Partidos(club2, rival);
                octavosIda.add(partidoIda);
            }
        }
    }
    
    private Clubes encontrarRival(Clubes club2, List<Clubes> rivalesUsados) {
        Random random = new Random();
        List<Clubes> posiblesRivales = new ArrayList<>(clubes1raPosicion);      
        String grupoClub2 = club2.getGrupo();
        // filtro rivales q no sean del mismo grupo y tampoco usados
        posiblesRivales.removeIf(rival -> (grupoClub2 != null && grupoClub2.equals(rival.getGrupo())) || rivalesUsados.contains(rival));
        
        if(!posiblesRivales.isEmpty()) {
            return posiblesRivales.get(random.nextInt(posiblesRivales.size()));
        }
        return null;
    }
    
    public void simularOctavosIda() {
        for (Partidos partido : octavosIda) {
            partido.simularPartido();
            partido.setGolesIda(partido.getGolesLocal(), partido.getGolesVisita());
        }
    }
    
    public void mostrarResultadosOctavosIda() {   
        System.out.println();
        System.out.println("----------------------");    
        System.out.println("Octavos de Final - Ida");
        System.out.println("----------------------"); 
        for (Partidos partido : octavosIda) {
            System.out.println(partido.getClubLocal().getNombre() + " vs " + partido.getClubVisita().getNombre());
            System.out.println();
        }
        
        System.out.println("---------------------------------");    
        System.out.println("Resultados Octavos de Final - Ida");
        System.out.println("---------------------------------"); 
        for (Partidos partido : octavosIda) {
            System.out.println(partido.getClubLocal().getNombre() + " " + partido.getGolesLocal() + " - " + partido.getGolesVisita() + " " + partido.getClubVisita().getNombre());
            System.out.println();
        } 
    }
    
    public void simularOctavosVuelta() {
        for (Partidos partido : octavosIda) {
            partido.simularPartido();
        }
    }
    
    public void mostrarResultadosOctavosVuelta() {   
        System.out.println("-------------------------");    
        System.out.println("Octavos de Final - Vuelta");
        System.out.println("-------------------------"); 
        for (Partidos partido : octavosIda) {
            System.out.println(partido.getClubVisita().getNombre() + " vs " + partido.getClubLocal().getNombre());
            System.out.println();
        }
        
        System.out.println("------------------------------------");    
        System.out.println("Resultados Octavos de Final - Vuelta");
        System.out.println("------------------------------------"); 
        for (Partidos partido : octavosIda) {
            int golesLocal = partido.getGolesLocal();
            int golesVisita = partido.getGolesVisita();
            
            // calcular el global
            int globalLocal = golesLocal + partido.getGolesIdaLocal();
            int globalVisita = golesVisita + partido.getGolesIdaVisita();
            
            // mostrar el global
            System.out.println(partido.getClubVisita().getNombre() + " " + golesVisita + " - " + golesLocal + " " + partido.getClubLocal().getNombre());
            System.out.print("Global: " + partido.getClubVisita().getNombre() + " " + globalVisita + " - " + globalLocal + " " + partido.getClubLocal().getNombre());
            System.out.println();
        
        Clubes ganador;
        if (globalLocal > globalVisita) {
            ganador = partido.getClubLocal();
        } else if (globalVisita > globalLocal) {
            ganador = partido.getClubVisita();
        } else {
            ganador = desempatePorPenales(partido);
            System.out.println("(Pasa por penales " + ganador.getNombre() + ")");
            }
        partido.setGanador(ganador);
        System.out.println();    
        }
    }
    
    public List<Clubes> determinarGanadores8vos() {
        List<Clubes> ganadores = new ArrayList<>();
        for (Partidos partido : octavosIda) {
            Clubes ganador = partido.getGanador();
            ganadores.add(ganador);
        }
            
        // mostrar ganadores
        System.out.println("Clasifican a los 4tos de final de la UEFA Champions League:");
        for (Clubes ganador : ganadores) {
            System.out.println(ganador.getNombre());
        }
        return ganadores;
    }
    
    public void armarCuartosDeFinal(List<Clubes> ganadoresOctavos) {
        List <Partidos> cuartos = new ArrayList<>();
        
        cuartos.add(new Partidos(ganadoresOctavos.get(0), ganadoresOctavos.get(1)));
        cuartos.add(new Partidos(ganadoresOctavos.get(2), ganadoresOctavos.get(3)));
        cuartos.add(new Partidos(ganadoresOctavos.get(4), ganadoresOctavos.get(5)));
        cuartos.add(new Partidos(ganadoresOctavos.get(6), ganadoresOctavos.get(7)));
        
        for (Partidos partido : cuartos) {
            Clubes localIda;
            Clubes visitaIda;
            
            if (partido.getClubLocal().getPosicion() > partido.getClubVisita().getPosicion()) {
                localIda = partido.getClubLocal();
                visitaIda = partido.getClubVisita();
            } else if (partido.getClubLocal().getPosicion() < partido.getClubVisita().getPosicion()) {
                localIda = partido.getClubVisita();
                visitaIda = partido.getClubLocal();
            } else {
                if (partido.getClubLocal().getPuntos() < partido.getClubVisita().getPuntos()) {
                localIda = partido.getClubLocal();
                visitaIda = partido.getClubVisita();
            } else if (partido.getClubLocal().getPuntos() > partido.getClubVisita().getPuntos()) {
                localIda = partido.getClubVisita();
                visitaIda = partido.getClubLocal();
            } else {
                if (partido.getClubLocal().getDiferenciaGoles() < partido.getClubVisita().getDiferenciaGoles()) {
                localIda = partido.getClubLocal();
                visitaIda = partido.getClubVisita();
            } else if (partido.getClubLocal().getDiferenciaGoles() > partido.getClubVisita().getDiferenciaGoles()) {
                localIda = partido.getClubVisita();
                visitaIda = partido.getClubLocal();
            } else {
                if (partido.getClubLocal().getGolesAFavor() < partido.getClubVisita().getGolesAFavor()) {
                localIda = partido.getClubLocal();
                visitaIda = partido.getClubVisita();
            } else {
                localIda = partido.getClubVisita();
                visitaIda = partido.getClubLocal();   
                        }
                    }
                }
            }
            partido.setClubLocal(localIda);
            partido.setClubVisita(visitaIda);
        }
        
        this.cuartosIda = cuartos;
    }

    public void simularCuartosIda() {
        for (Partidos partido : cuartosIda) {
            partido.simularPartido();
            partido.setGolesIda(partido.getGolesLocal(), partido.getGolesVisita());
        }
    }

    public void mostrarResultadosCuartosIda() {   
        System.out.println();
        System.out.println("----------------------");    
        System.out.println("Cuartos de Final - Ida");
        System.out.println("----------------------"); 
        for (Partidos partido : cuartosIda) {
            System.out.println(partido.getClubLocal().getNombre() + " vs " + partido.getClubVisita().getNombre());
            System.out.println();
        }
        
        System.out.println("---------------------------------");    
        System.out.println("Resultados Cuartos de Final - Ida");
        System.out.println("---------------------------------"); 
        for (Partidos partido : cuartosIda) {
            System.out.println(partido.getClubLocal().getNombre() + " " + partido.getGolesLocal() + " - " + partido.getGolesVisita() + " " + partido.getClubVisita().getNombre());
            System.out.println();
        } 
    }
    
    public void simularCuartosVuelta() {
        for (Partidos partido : cuartosIda) {
            partido.simularPartido();
        }
    }
    
    public void mostrarResultadosCuartosVuelta() {   
        System.out.println("-------------------------");    
        System.out.println("Cuartos de Final - Vuelta");
        System.out.println("-------------------------"); 
        for (Partidos partido : cuartosIda) {
            System.out.println(partido.getClubVisita().getNombre() + " vs " + partido.getClubLocal().getNombre());
            System.out.println();
        }
        
        System.out.println("------------------------------------");    
        System.out.println("Resultados Cuartos de Final - Vuelta");
        System.out.println("------------------------------------"); 
        for (Partidos partido : cuartosIda) {
            int golesLocal = partido.getGolesLocal();
            int golesVisita = partido.getGolesVisita();
            
            int globalLocal = golesLocal + partido.getGolesIdaLocal();
            int globalVisita = golesVisita + partido.getGolesIdaVisita();
            
            System.out.println(partido.getClubVisita().getNombre() + " " + golesVisita + " - " + golesLocal + " " + partido.getClubLocal().getNombre());
            System.out.print("Global: " + partido.getClubVisita().getNombre() + " " + globalVisita + " - " + globalLocal + " " + partido.getClubLocal().getNombre());
            System.out.println();
        
        Clubes ganador;
        if (globalLocal > globalVisita) {
            ganador = partido.getClubLocal();
        } else if (globalVisita > globalLocal) {
            ganador = partido.getClubVisita();
        } else {
            ganador = desempatePorPenales(partido);
            System.out.println("(Pasa por penales " + ganador.getNombre() + ")");
            }
        partido.setGanador(ganador);
        System.out.println();    
        }
    }
    
    public List<Clubes> determinarGanadores4tos() {
        List<Clubes> ganadores = new ArrayList<>();
        for (Partidos partido : cuartosIda) {
            Clubes ganador = partido.getGanador();
            ganadores.add(ganador);
        }
            
        System.out.println("Son semifinalistas de la UEFA Champions League:");
        for (Clubes ganador : ganadores) {
            System.out.println(ganador.getNombre());
        }
        return ganadores;
    }
    
    public void armarSemifinales(List<Clubes> ganadoresCuartos) {
        List <Partidos> semis = new ArrayList<>();
        
        semis.add(new Partidos(ganadoresCuartos.get(0), ganadoresCuartos.get(1)));
        semis.add(new Partidos(ganadoresCuartos.get(2), ganadoresCuartos.get(3)));
        
        for (Partidos partido : semis) {
            Clubes localIda;
            Clubes visitaIda;
            
            if (partido.getClubLocal().getPosicion() > partido.getClubVisita().getPosicion()) {
                localIda = partido.getClubLocal();
                visitaIda = partido.getClubVisita();
            } else if (partido.getClubLocal().getPosicion() < partido.getClubVisita().getPosicion()) {
                localIda = partido.getClubVisita();
                visitaIda = partido.getClubLocal();
            } else {
                if (partido.getClubLocal().getPuntos() < partido.getClubVisita().getPuntos()) {
                localIda = partido.getClubLocal();
                visitaIda = partido.getClubVisita();
            } else if (partido.getClubLocal().getPuntos() > partido.getClubVisita().getPuntos()) {
                localIda = partido.getClubVisita();
                visitaIda = partido.getClubLocal();
            } else {
                if (partido.getClubLocal().getDiferenciaGoles() < partido.getClubVisita().getDiferenciaGoles()) {
                localIda = partido.getClubLocal();
                visitaIda = partido.getClubVisita();
            } else if (partido.getClubLocal().getDiferenciaGoles() > partido.getClubVisita().getDiferenciaGoles()) {
                localIda = partido.getClubVisita();
                visitaIda = partido.getClubLocal();
            } else {
                if (partido.getClubLocal().getGolesAFavor() < partido.getClubVisita().getGolesAFavor()) {
                localIda = partido.getClubLocal();
                visitaIda = partido.getClubVisita();
            } else {
                localIda = partido.getClubVisita();
                visitaIda = partido.getClubLocal();   
                        }
                    }
                }
            }
            partido.setClubLocal(localIda);
            partido.setClubVisita(visitaIda);
        }
        
        this.semisIda = semis;
    }
    
    public void simularSemifinalesIda() {
        for (Partidos partido : semisIda) {
            partido.simularPartido();
            partido.setGolesIda(partido.getGolesLocal(), partido.getGolesVisita());
        }
    }
    
    public void mostrarResultadosSemifinalesIda() {   
        System.out.println();
        System.out.println("------------------");    
        System.out.println("Semmifinales - Ida");
        System.out.println("------------------"); 
        for (Partidos partido : semisIda) {
            System.out.println(partido.getClubLocal().getNombre() + " vs " + partido.getClubVisita().getNombre());
            System.out.println();
        }
        
        System.out.println("------------------------------");    
        System.out.println("Resultados Semmifinales - Ida");
        System.out.println("------------------------------"); 
        for (Partidos partido : semisIda) {
            System.out.println(partido.getClubLocal().getNombre() + " " + partido.getGolesLocal() + " - " + partido.getGolesVisita() + " " + partido.getClubVisita().getNombre());
            System.out.println();
        } 
    }
    
    public void simularSemifinalesVuelta() {
        for (Partidos partido : semisIda) {
            partido.simularPartido();
        }
    }
    
    public void mostrarResultadosSemifinalesVuelta() {   
        System.out.println("---------------------");    
        System.out.println("Semifinalesl - Vuelta");
        System.out.println("---------------------"); 
        for (Partidos partido : semisIda) {
            System.out.println(partido.getClubVisita().getNombre() + " vs " + partido.getClubLocal().getNombre());
            System.out.println();
        }
        
        System.out.println("-------------------------------");    
        System.out.println("Resultados Semifinales - Vuelta");
        System.out.println("-------------------------------"); 
        for (Partidos partido : semisIda) {
            int golesLocal = partido.getGolesLocal();
            int golesVisita = partido.getGolesVisita();
            
            int globalLocal = golesLocal + partido.getGolesIdaLocal();
            int globalVisita = golesVisita + partido.getGolesIdaVisita();
            
            System.out.println(partido.getClubVisita().getNombre() + " " + golesVisita + " - " + golesLocal + " " + partido.getClubLocal().getNombre());
            System.out.print("Global: " + partido.getClubVisita().getNombre() + " " + globalVisita + " - " + globalLocal + " " + partido.getClubLocal().getNombre());
            System.out.println();
        
        Clubes ganador;
        if (globalLocal > globalVisita) {
            ganador = partido.getClubLocal();
        } else if (globalVisita > globalLocal) {
            ganador = partido.getClubVisita();
        } else {
            ganador = desempatePorPenales(partido);
            System.out.println("(Pasa por penales " + ganador.getNombre() + ")");
            }
        partido.setGanador(ganador);
        System.out.println();    
        }
    }
    
    public List<Clubes> determinarGanadoresSemis() {
        List<Clubes> ganadores = new ArrayList<>();
        for (Partidos partido : semisIda) {
            Clubes ganador = partido.getGanador();
            ganadores.add(ganador);
        }
            
        System.out.println("Son finalistas de la UEFA Champions League:");
        for (Clubes ganador : ganadores) {
            System.out.println(ganador.getNombre());
        }
        return ganadores;
    }
    
    public void armarFinal(List<Clubes> ganadoresSemis) {
        List <Partidos> laFinal = new ArrayList<>();
        
        laFinal.add(new Partidos(ganadoresSemis.get(0), ganadoresSemis.get(1)));
        
        this.finalisima = laFinal;
    }
    
    public void simularFinal() {
        for (Partidos partido : finalisima) {
            partido.simularPartido();
        }
    }
    
    public void mostrarResultadoFinal() {   
        System.out.println();
        System.out.println("---------------------");    
        System.out.println("        Final        ");
        System.out.println("---------------------"); 
        for (Partidos partido : finalisima) {
            int golesLocal = partido.getGolesLocal();
            int golesVisita = partido.getGolesVisita();
            
            System.out.println(partido.getClubLocal().getNombre() + " " + golesLocal + " - " + golesVisita + " " + partido.getClubVisita().getNombre());
            System.out.println();
        
        Clubes ganador;
        if (golesLocal > golesVisita) {
            ganador = partido.getClubLocal();
        } else if (golesVisita > golesLocal) {
            ganador = partido.getClubVisita();
        } else {
            ganador = desempatePorPenales(partido);
            System.out.println("(Pasa por penales " + ganador.getNombre() + ")");
            }
        partido.setGanador(ganador);
        System.out.println();    
        }
    }
    
    public List<Clubes> determinarCampeon() {
        List<Clubes> ganadores = new ArrayList<>();
        for (Partidos partido : finalisima) {
            Clubes ganador = partido.getGanador();
            ganadores.add(ganador);
        }
            
        for (Clubes ganador : ganadores) {
            System.out.println(ganador.getNombre() + " es campeon de la UEFA Champions League");
        }
        return ganadores;
    }
    
    public Clubes desempatePorPenales(Partidos partido) {
        // logica de desempate 
        Random random = new Random();
        return random.nextBoolean() ? partido.getClubLocal() : partido.getClubVisita();        
    }
}