package main;
import java.util.List;
import models.Clubes;
import models.ClubesDao;
import models.Grupos;
import models.EliminacionDirecta;
import models.Partidos;
import models.Sorteo;

public class Principal {
    public static void main(String[] args) {
        ClubesDao clubesDao = new ClubesDao();
        List<Clubes> clubes = clubesDao.obtenerTodosLosClubes();
        
        Sorteo sorteo = new Sorteo();
        sorteo.realizarSorteo(clubes);
        
        for (Grupos grupo : sorteo.getGrupos()) {
            System.out.println("Grupo " + grupo.getNombre());
            System.out.println("-------");
            grupo.mostrarIntegrantes();
            grupo.generarFixture();
            System.out.println();
            
            System.out.println("Fixture ");
            System.out.println("--------");
            grupo.mostrarFixture();
            System.out.println();
             
            System.out.println("Resultados");
            System.out.println("----------");
            for (Partidos partido : grupo.getPartidos()) {
                System.out.println(partido.getClubLocal().getNombre() + " " + partido.getGolesLocal() + " - " +
                                   partido.getGolesVisita() + " " + partido.getClubVisita().getNombre());
            }     
            
            System.out.println();
            grupo.mostrarTablaDePosiciones();
            System.out.println();
            grupo.clasificarClubesGrupo(clubes);
        }
        
        System.out.println("Clasifican a los 8vos de final de la UEFA Champions League:");
        for (Grupos grupo : sorteo.getGrupos()) {
            for (Clubes club : grupo.getClubes()) {
                if (club.isClasificadosUCL()) {
                    System.out.println(club.getNombre());
                }
            }
        }
        
        System.out.println();
        System.out.println("Acceden a los 16vos de final de la UEFA Europa League:");
        for (Grupos grupo : sorteo.getGrupos()) {
            for (Clubes club : grupo.getClubes()) {
                if (club.isClasificadosUEL()) {
                    System.out.println(club.getNombre());
                }
            }
        }
        
    EliminacionDirecta manoAmano = new EliminacionDirecta(sorteo.getGrupos());
    manoAmano.armarOctavosDeFinal();
    manoAmano.simularOctavosIda();
    manoAmano.mostrarResultadosOctavosIda();
    
    manoAmano.simularOctavosVuelta();
    manoAmano.mostrarResultadosOctavosVuelta();
    
    List<Clubes> ganadores8vos = manoAmano.determinarGanadores8vos();
    
    manoAmano.armarCuartosDeFinal(ganadores8vos);
    manoAmano.simularCuartosIda();
    manoAmano.mostrarResultadosCuartosIda();
    
    manoAmano.simularCuartosVuelta();
    manoAmano.mostrarResultadosCuartosVuelta();
    
    List<Clubes> semifinalistas = manoAmano.determinarGanadores4tos();  
    
    manoAmano.armarSemifinales(semifinalistas);
    manoAmano.simularSemifinalesIda();
    manoAmano.mostrarResultadosSemifinalesIda();
    
    manoAmano.simularSemifinalesVuelta();
    manoAmano.mostrarResultadosSemifinalesVuelta();
    
    List<Clubes> finalistas = manoAmano.determinarGanadoresSemis();  
    
    manoAmano.armarFinal(finalistas);
    manoAmano.simularFinal();
    manoAmano.mostrarResultadoFinal();
    
    List<Clubes> campeon = manoAmano.determinarCampeon();  
    }
}