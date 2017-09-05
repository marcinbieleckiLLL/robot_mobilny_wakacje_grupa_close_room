package sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcin on 03.09.2017.
 */
public class MapAction {
    private static List<ButtonsNames> listOfSelectedButtons;

    public MapAction() {
        listOfSelectedButtons = new ArrayList<>();
        listOfSelectedButtons.add(new ButtonsNames("a", "a"));
    }

    public static List<ButtonsNames> getListOfSelectedButtons() {
        return listOfSelectedButtons;
    }

    public static void addOneAction(String nameOfSelectedButton, String selectedRadioButton){
        listOfSelectedButtons.add(new ButtonsNames(nameOfSelectedButton, selectedRadioButton));
    }
}
