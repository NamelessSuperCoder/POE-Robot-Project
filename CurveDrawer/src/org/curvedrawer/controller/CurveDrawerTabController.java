package org.curvedrawer.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.curvedrawer.Main;
import org.curvedrawer.misc.CirclePoint;
import org.curvedrawer.misc.PathTable;
import org.curvedrawer.path.Path;
import org.curvedrawer.util.Converter;
import org.curvedrawer.util.Point;
import org.curvedrawer.util.Pose;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CurveDrawerTabController implements Initializable {
    private final ObservableMap<Path, Pose[]> pathPoints = FXCollections.observableHashMap();
    private final Map<Integer, Path> pathHashMap = new HashMap<>(10);
    private final Map<Path, HashMap<Pose, CirclePoint>> posePointHashMap = new HashMap<>(10);
    @FXML
    private SplitPane splitPane;
    @FXML
    private Pane drawingPane;
    @FXML
    private Accordion pathsViewer;
    @FXML
    private Button sendButton;
    private SimpleIntegerProperty selectedPath;

    public final void sendCurveToSmartDashboard() {
        Path path = getSelectedPaths();

        Pose[] poses = pathPoints.get(path);

        String converted = Converter.posesToString(poses);

        System.out.println(converted);

        String nameOfPath = pathsViewer.getPanes().get(selectedPath.get()).getText();

        System.out.println('\'' + nameOfPath + '\'');

        Main.networkTable.putString(nameOfPath, converted);
    }

    private void createPath() {
        Entry<String, Path> path = askForPath();

        if (path != null) {
            addPath(path.getKey(), path.getValue());
        }
    }

    public final void createPoint(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            if (selectedPath.get() == -1) {
                createPath();
            } else {
                Path path = pathHashMap.get(selectedPath.get());

                Point point = new Point(mouseEvent.getX(), mouseEvent.getY());

                path.addPoints(point);
            }
        }
    }

    private Entry<String, Path> askForPath() {
        return PathSelectorController.getPathChoice(getUsedPathNames());
    }

    private String[] getUsedPathNames() {
        return pathsViewer.getPanes().stream().map(TitledPane::getText).toArray(String[]::new);
    }

    private void addPath(String pathName, Path path) {
        if ((path != null) || (pathName != null)) {
            assert path != null;
            TitledPane titledPane = new TitledPane(pathName, new PathTable(path));

            pathsViewer.getPanes().add(titledPane);
            pathsViewer.setExpandedPane(titledPane);

            int selection = pathsViewer.getPanes().size() - 1;

            pathHashMap.put(selection, path);
            selectedPath.set(selection);

            sendButton.setDisable(false);

            pathPoints.put(path, path.createPathPoses());
            posePointHashMap.put(path, new HashMap<>(Main.NUMBER_OF_STEPS));

            path.getPoints().addListener((ListChangeListener<Point>) c -> {
                if (c.next()) {
                    if (c.wasRemoved()) {
                        drawingPane.getChildren().removeAll(c.getRemoved().stream().map(CirclePoint::new).collect(Collectors.toList()));
                    } else if (c.wasAdded()) {
                        drawingPane.getChildren().addAll(c.getAddedSubList().stream().map(CirclePoint::new).collect(Collectors.toList()));
                    }

                    pathPoints.put(path, path.createPathPoses());
                }
            });
        }
    }

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        selectedPath = new SimpleIntegerProperty(-1);

        splitPane.setOnKeyPressed(event -> {
            if (event.isControlDown() && (event.getCode() == KeyCode.N)) {
                createPath();
            }
        });

        pathPoints.addListener((MapChangeListener<Path, Pose[]>) c -> {
            if (c.wasAdded()) {
                CirclePoint[] circlePoint = Arrays.stream(c.getValueAdded()).map(pose -> new CirclePoint(pose.getX(), pose.getY())).toArray(CirclePoint[]::new);

                if (posePointHashMap.containsKey(c.getKey())) {
                    Map<Pose, CirclePoint> circlePointHashMap = posePointHashMap.get(c.getKey());

                    Pose[] pose = c.getValueAdded();

                    for (int i = 0; i < pose.length; i++) {
                        circlePointHashMap.put(pose[i], circlePoint[i]);
                    }
                }

                drawingPane.getChildren().addAll(circlePoint);
            }
            if (c.wasRemoved()) {
                if (posePointHashMap.containsKey(c.getKey())) {
                    Map<Pose, CirclePoint> circlePointHashMap = posePointHashMap.get(c.getKey());

                    Pose[] poses = c.getValueRemoved();

                    CirclePoint[] circlePoints = new CirclePoint[poses.length];

                    int i = 0;

                    for (Pose pose : poses) {
                        circlePoints[i] = circlePointHashMap.get(pose);
                        i++;
                    }

                    drawingPane.getChildren().removeAll(circlePoints);
                }
            }

        });
    }

    private Path getSelectedPaths() //TODO make it so that you can select many paths
    {
        return pathHashMap.get(selectedPath.get());
    }
}
