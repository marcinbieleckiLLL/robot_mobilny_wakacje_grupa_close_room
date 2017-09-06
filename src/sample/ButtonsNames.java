package sample;

/**
 * Created by Marcin on 03.09.2017.
 */
public class ButtonsNames {
    private String nameOfButton;
    private String radioButton;

    public ButtonsNames(String nameOfButton, String radioButton) {
        this.nameOfButton = nameOfButton;
        this.radioButton = radioButton;
    }

    public void setRadioButton(String radioButton) {
        this.radioButton = radioButton;
    }

    public String getNameOfButton() {
        return nameOfButton;
    }

    public String getRadioButton() {
        return radioButton;
    }
}
