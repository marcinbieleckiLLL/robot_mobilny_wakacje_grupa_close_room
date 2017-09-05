package sample;

/**
 * Created by Marcin on 05.09.2017.
 */
public class XconYcon {
    private int id;
    private int xcon;
    private int ycon;

    public XconYcon(int id, int xcon, int ycon) {
        this.id = id;
        this.xcon = xcon;
        this.ycon = ycon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getXcon() {
        return xcon;
    }

    public void setXcon(int xcon) {
        this.xcon = xcon;
    }

    public int getYcon() {
        return ycon;
    }

    public void setYcon(int ycon) {
        this.ycon = ycon;
    }
}
