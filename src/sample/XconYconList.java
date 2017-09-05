package sample;

import Map.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Marcin on 05.09.2017.
 */
public class XconYconList {
    private static List<XconYcon> listOfXconYcon = new ArrayList<>();

    public XconYconList() {
        listOfXconYcon.add(new XconYcon(0, Map.xCon, Map.yCon));
    }

    public static List<XconYcon> getListOfXconYcon() {
        return listOfXconYcon;
    }

    public static void setListOfXconYcon(List<XconYcon> listOfXconYcon) {
        XconYconList.listOfXconYcon = listOfXconYcon;
    }

    public void add(int xcon, int ycon){
        listOfXconYcon.add(new XconYcon(listOfXconYcon.get(listOfXconYcon.size() - 1).getId() + 1, xcon, ycon));
    }

    public XconYcon getXconYconById(int id){
        return listOfXconYcon.stream().filter(xy -> xy.getId() == id).collect(Collectors.toList()).get(0);
    }
}
