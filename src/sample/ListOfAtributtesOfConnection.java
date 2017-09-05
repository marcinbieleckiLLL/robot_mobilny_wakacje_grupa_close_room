package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Marcin on 22.08.2017.
 */
public class ListOfAtributtesOfConnection {
    private List<AtributtesOfConnection> listOfAtributtesOfConnections = new ArrayList<>();

    public void add(AtributtesOfConnection atributtesOfConnection){
        listOfAtributtesOfConnections.add(atributtesOfConnection);
    }

    public AtributtesOfConnection getClientByName(String name){
        List<AtributtesOfConnection> list =  listOfAtributtesOfConnections.stream().filter(c -> c.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        return list.get(0);
    }
}
