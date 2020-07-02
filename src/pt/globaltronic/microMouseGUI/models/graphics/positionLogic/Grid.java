package pt.globaltronic.microMouseGUI.models.graphics.positionLogic;

import com.sun.javafx.geom.Vec3f;
import pt.globaltronic.microMouseGUI.OpenGLEngine;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.HorizontalWallPosition;
import pt.globaltronic.microMouseGUI.models.graphics.positionLogic.walls.VerticalWallPosition;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.HorizontalWalls;
import pt.globaltronic.microMouseGUI.models.graphics.viewObjects.VerticalWalls;
import pt.globaltronic.microMouseGUI.openGL.OBJLoader;
import pt.globaltronic.microMouseGUI.openGL.entity.Entity;
import pt.globaltronic.microMouseGUI.openGL.models.RawModel;
import pt.globaltronic.microMouseGUI.openGL.models.TexturedModel;
import pt.globaltronic.microMouseGUI.openGL.terrain.Terrain;
import pt.globaltronic.microMouseGUI.openGL.textures.ModelTexture;

import java.util.HashSet;

public class Grid {

    private int cols;
    private int rows;
    private int cellSize; //size of cells in pixels
    private Position[][] positions;
    private HorizontalWallPosition[][] hWallPositions;
    private VerticalWallPosition[][] vWallPositions;
    private OpenGLEngine engine3d;

    public Grid(int cols, int rows, int cellSize) {
        this.cols = cols;
        this.rows = rows;
        this.cellSize = cellSize;
        init();
    }

    public Grid(int cols, int rows, int cellSize, OpenGLEngine engine3d) {
        this.cols = cols;
        this.rows = rows;
        this.cellSize = cellSize;
        this.engine3d = engine3d;
        init();
    }

    public void init() {
        createPosition();
        createHWallPositions();
        createVWallPositions();
    }

    public void createPosition() {
        positions = new Position[cols][rows];
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                positions[i][j] = new Position(i, j);
                if (engine3d != null) {
                    if (cols == rows && cols == 16) {
                        if (((i == 7) || (i == 8)) && ((j == 7) || (j == 8))) {
                            engine3d.getTerrains().add(new Terrain(i, j, engine3d.getLoader(), new ModelTexture(engine3d.getLoader().loadTexture("green"))));
                            continue;
                        }
                    }
                    engine3d.getTerrains().add(new Terrain(i, j, engine3d.getLoader(), new ModelTexture(engine3d.getLoader().loadTexture("black"))));
                }
            }
        }
    }

    public void createHWallPositions() {
        hWallPositions = new HorizontalWallPosition[cols][rows + 1];
        TexturedModel texturedWall = null;
        if (engine3d != null) {
            RawModel wallRaw = OBJLoader.loadObjectModel("wall", engine3d.getLoader());
            ModelTexture wallTexture = new ModelTexture(engine3d.getLoader().loadTexture("myTexture"));
            texturedWall = new TexturedModel(wallRaw, wallTexture);
            texturedWall.getModelTexture().setReflectivity(1);
            texturedWall.getModelTexture().setShineDamper(10);
        }
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows + 1; j++) {
                HorizontalWallPosition hWallPosition = new HorizontalWallPosition(i, j);
                hWallPositions[i][j] = hWallPosition;
                if (engine3d != null) {
                    engine3d.gethWalls()[i][j] = new HorizontalWalls(hWallPosition);
                    int x = cols - 1 - i;
                    int z = j;

                    Entity hWall = new Entity(texturedWall, new Vec3f(x * cellSize, 0, z * cellSize - 1), 0, 0, 0, 1);
                    engine3d.gethWalls()[i][j].setEntity(hWall);
                    engine3d.gethWalls()[i][j].setVisible(false);

                    //always showing side walls.
                    if (j == 0 || j == rows) {
                        engine3d.gethWalls()[i][j].setVisible(true);
                    }
                }
            }
        }
    }

    public void createVWallPositions() {
        vWallPositions = new VerticalWallPosition[cols + 1][rows];
        TexturedModel texturedWall = null;
        if (engine3d != null) {
            RawModel wallRaw = OBJLoader.loadObjectModel("wall", engine3d.getLoader());
            ModelTexture wallTexture = new ModelTexture(engine3d.getLoader().loadTexture("myTexture"));
            texturedWall = new TexturedModel(wallRaw, wallTexture);
            texturedWall.getModelTexture().setReflectivity(1);
            texturedWall.getModelTexture().setShineDamper(10);
        }

        for (int i = 0; i < cols + 1; i++) {
            for (int j = 0; j < rows; j++) {
                VerticalWallPosition vWallPosition = new VerticalWallPosition(i, j);
                vWallPositions[i][j] = vWallPosition;
                if (engine3d != null) {
                    engine3d.getvWalls()[i][j] = new VerticalWalls(vWallPosition);
                    int x = cols - i;
                    int z = j + 1;

                    Entity vWall = new Entity(texturedWall, new Vec3f(x * cellSize - 1, 0, z * cellSize), 0, 90, 0, 1);
                    engine3d.getvWalls()[i][j].setEntity(vWall);
                    engine3d.getvWalls()[i][j].setVisible(false);

                    //always showing side walls.
                    if (i == 0 || i == cols) {
                        engine3d.getvWalls()[i][j].setVisible(true);
                    }
                    //always showing starting wall
                    if (i == 1 && j == 0) {
                        engine3d.getvWalls()[i][j].setVisible(true);
                    }
                }
            }
        }
    }

    //convert a position col to X pixels
    public int colToX(int col) {
        return col * cellSize;
    }

    //convert a position row to Y pixels
    public int rowToY(int row) {
        return row * cellSize;
    }

    public Position getPosition(int col, int row) {
        return positions[col][row];
    }

    public HorizontalWallPosition getHWallPosition(int col, int row) {
        return hWallPositions[col][row];
    }

    public VerticalWallPosition getVWallPosition(int col, int row) {
        return vWallPositions[col][row];
    }
}
