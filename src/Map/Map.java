package Map;


import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Map {

    public static int W = 600;
    public static int X_TILES = 10;
    public static int Y_TILES = 10;
    public static int xCon = Map.X_TILES/2;
    public static int yCon = Map.Y_TILES/2;
    public static int TILE_SIZE = W/X_TILES;
    public static Tile[][] grid = new Tile[X_TILES][Y_TILES];
    private Pane root;

    public static int getxTiles() {
        return X_TILES;
    }

    public static int getyTiles() {
        return Y_TILES;
    }

    public static int getxCon() {
        return xCon;
    }

    public static void setxCon(int xCon) {
        Map.xCon = xCon;
    }

    public static int getyCon() {
        return yCon;
    }

    public static void setyCon(int yCon) {
        Map.yCon = yCon;
    }

    public static Tile[][] getGrid() {
        return grid;
    }
    public static void setGrid(Tile[][] grid) {
        Map.grid = grid;
    }

    public Pane createContent() {
        root = new Pane();
        root.setStyle("-fx-background-color: #383838");

        // dodawanie pojedynczych kafli do dwuwymiarowej tablicy
        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = new Tile(x, y);
                grid[x][y] = tile;
                root.getChildren().addAll(tile);
            }
        }
        return root;
    }

    public class Tile extends StackPane {
        private int x, y;
        boolean isNew = true;
        public boolean isWall = false;
        private boolean isOpen = false;
        Rectangle border = new Rectangle(TILE_SIZE , TILE_SIZE );

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
            getChildren().addAll(border);

            //ustawianie lokalizacji kafla odpowienio do jego wspolrzednej
            setTranslateX(x * TILE_SIZE);
            setTranslateY(y * TILE_SIZE);;

        }

        public void open() {

            //ustawianie parametrow kafla
            border.setStroke(Color.web("0x454545"));
            border.setStrokeWidth(2.0);

            //jezeli kafel jest odkryty i nie jest nowy, staje sie stary czyli oznaczony ponizszym kolorem
            for (int y = 0; y < Y_TILES; y++) {
                for (int x = 0; x < X_TILES; x++) {
                    if( !grid[x][y].isNew && grid[x][y].isOpen)
                        grid[x][y].border.setFill(Color.web("0xbebebe"));

                    // w przeciwnym razie kolor jest ustawiany na kolor kafla aktualnego
                    else
                    border.setFill(Color.web("0xbe7e7e"));
                }
            }
            isOpen = true;
            isNew = false;
        }

        //funkcja rysujaca sciane
        public void makeWall(){
            grid[x][y].border.setFill(Color.web("0x454545"));
            grid[x][y].isWall = true;
        }
    }
}
