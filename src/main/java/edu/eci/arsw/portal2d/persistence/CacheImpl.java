package edu.eci.arsw.portal2d.persistence;

import edu.eci.arsw.portal2d.dto.HistorialDto;
import edu.eci.arsw.portal2d.dto.PlayerDto;
import edu.eci.arsw.portal2d.model.Partida;
import edu.eci.arsw.portal2d.model.Player;
import edu.eci.arsw.portal2d.model.Sala;
import edu.eci.arsw.portal2d.model.User;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class CacheImpl implements Cache{

    private User user = null;
    private Sala sala = null;

    private final HashMap<String, HashMap<String, Player>> salas = new HashMap<>();
    private final HashMap<String, Partida> salasPartida = new HashMap<>();
    //private final HashMap<String, Partida> partida = new HashMap<>();

    public CacheImpl() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    @Override
    public void crearJugadores(String idSala, List<String> numeroJugadores) {
        HashMap<String, Player> players = new HashMap<>();
        for (String s: numeroJugadores){
            players.put(s, new Player(s, idSala));
        }
        this.salas.put(idSala, players);
    }

    public void bonus(PlayerDto player, Partida partida){
        partida.setPodio();
        player.setPodio(partida.getPodio());
        int exp = (int)Math.ceil(partida.getExperiencia()/player.getPodio());
        int oro = (int)Math.ceil(partida.getOro()/player.getPodio());
        player.setExpe(exp);
        player.setOro(oro);
    }

    @Override
    public boolean finPartida(String idSala, String namePlayer){
        Partida partida = salasPartida.get(idSala);
        PlayerDto player = partida.getPlayers().get(namePlayer);
        System.out.println("x: "+player.getX() + " " + partida.getMapa().getPuntoFin().getX()+ "y: "+player.getY()+" "+ partida.getMapa().getPuntoFin().getY());
        if (player.getX() >= partida.getMapa().getPuntoFin().getX() && player.getY() >= partida.getMapa().getPuntoFin().getY()){
            if (player.getPodio() == 0) {
                bonus(player, partida);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<PlayerDto> iniciarPartida(String idSala) {
        Partida partida = salasPartida.get(idSala);
        for (PlayerDto p: partida.getPlayers().values()){
            p.setX(partida.getMapa().getPuntoInicio().getX());
            p.setY(partida.getMapa().getPuntoInicio().getY());
        }
        return new LinkedList<>(partida.getPlayers().values());
    }

    @Override
    public HistorialDto infoPartida(String idSala, String idUser) {
        //System.out.println(partida.toString());
        //Partida partidaUser = partida.get(idSala);
        //HistorialDto historialDto = new HistorialDto(idUser, partidaUser.getPodioPlayers(idUser),0, 0);
        //System.out.println(partidaUser.getPodioPlayers(idUser));
        //System.out.println(historialDto);
        return null;
    }

    @Override
    public void almacenarPartida(String idSala, String idUser){
        salasPartida.put(idSala, new Partida());
        //partida.put(idSala, new Partida());
    }


    public void crearJugador(String idSala, String namePlayer){
        //almacenarPartida(idSala, namePlayer);
        try {
            Partida partida = salasPartida.get(idSala);
            partida.setPlayers(namePlayer, new PlayerDto(namePlayer));
        }catch (Exception e){
            System.out.println("partida no esta");
        }
        //players.put(idSala, players.put(idUser, new Player(idUser)));
    }

    @Override
    public LinkedList<PlayerDto> moverPlayer(int x, int y, String name, String idSala) {
        Partida partida = salasPartida.get(idSala);
        System.out.println(partida);
        PlayerDto player = partida.getPlayers().get(name);
        System.out.println(player);
        player.setY(y);
        player.setX(x);
        return new LinkedList<>(partida.getPlayers().values());
    }

    @Override
    public List<PlayerDto> infoPodioPlayers(String idSala) {
        Partida partida = salasPartida.get(idSala);
        return new LinkedList<>(partida.getPlayers().values());
    }

    @Override
    public List<PlayerDto> players(String idSala){
        Partida partida = salasPartida.get(idSala);
        HashMap<String, PlayerDto> listaDePlayers = partida.getPlayers();
        return new LinkedList<>(listaDePlayers.values());
    }



    @Override
    public void updatePlayer(String idSala, Player player) {
        HashMap<String, Player> playerInfo = salas.get(idSala);
        playerInfo.replace(player.getId(), player);
    }
}
