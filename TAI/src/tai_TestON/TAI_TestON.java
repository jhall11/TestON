/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tai_TestON;


/**
*
* @author Raghavkashyap (raghavkashyap@paxterra.com)

* TestON is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.

* TestON is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with TestON. If not, see <http://www.gnu.org/licenses/>.

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBuilder;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import javax.swing.SingleSelectionModel;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

/**
 *
 * @author Raghav Kashyap (raghavkashyap@paxterrasolutions.com)
 *
 */
public class TAI_TestON extends Application {

    MenuBar OFA_MenuBar = new MenuBar();
    Scene scene;
    TAILocale label = new TAILocale(new Locale("en", "EN"));
    Stage copyStage;
    double toolBarHeight, menuBarHeight;
    ToolBar toolbar = new ToolBar();
    TitledPane projectExplorer;
    TitledPane logExlorer;
    TitledPane driverExplorer;
    TreeView<String> projectExplorerTreeView, logExplorerTreeView, driverExplorerTreeView;
    TreeItem<String> driverExplorerTree;
    static double OFAContainerWidth, OFAContainerHeight;
    boolean projectExplorerFlag, editorFlag;
    ContextMenu contextMenu;
    TAI_TestON OFAReference;
    TabPane explorerTabPane, editorTabPane;
    FileChooser openParams;
    TAIFileOperations fileOperation = new TAIFileOperations();
    Tab explorerTab;
    Accordion explorer;
    TreeItem<String> projectExplorerTree, logExplorerTree;
    TestONWizard wizard;
    AddParams addParams;
    String paramsFileContent, projectWorkSpacePath;
    TAITestSummary testSummaryPop;
    Button Save, advanceMode;
    MenuItem saveAll;
    MenuItem closeAll;
    TAISyntaxValidator syntaxValidator;
    ArrayList<String> errorAtLineList = new ArrayList<String>();
    HashMap<String, String> syntaxError = new HashMap<String, String>();
    HashMap errorHash;
    boolean switchType = false;
    MenuItem run, runTestMenu, saveFileItem;
    TreeItem<String> testTree = new TreeItem<>();
    Object resultXmlServer;

    public TAI_TestON() {
        OFAReference = this;

    }

    public TAI_TestON(TabPane pane, Scene sc, TabPane pane1) {
        editorTabPane = pane;
        scene = sc;
        explorerTabPane = pane1;
        OFAReference = this;
    }

    @Override
    public void start(Stage primaryStage) {
        copyStage = primaryStage;
        Group root = new Group();

        projectWorkSpacePath = label.hierarchyTestON;
        scene = new Scene(root, 800, 600, Color.DARKGRAY);
        VBox baseBox = new VBox();
        getFileMenu();
        getEditMenu();
        getViewMenu();
        getRunMenu();
        getHelpMenu();
        getToolBar();
        OFA_MenuBar.setPrefWidth(scene.widthProperty().get());
        toolbar.prefWidthProperty().bind(scene.widthProperty());
        toolbar.setMinHeight(scene.heightProperty().get() / 20);
        toolBarHeight = toolbar.getMinHeight();
        OFA_MenuBar.setPrefHeight(scene.heightProperty().get() / 20);
        menuBarHeight = OFA_MenuBar.getPrefHeight();
        explorerTabPane = new TabPane();
        editorTabPane = new TabPane();
        explorer = new Accordion();
        explorerTab = new Tab("Explorer");
        explorerTabPane.setLayoutX(0);
        projectExplorerFlag = true;

        getProjectExplorer();
        getLogExplorer();
        getDriverExplorer();
        explorer.setMinSize(200, scene.getHeight() - 40);
        explorer.getPanes().addAll(projectExplorer, logExlorer, driverExplorer);
        explorerTabPane.prefHeightProperty().bind(scene.heightProperty());
        explorerTabPane.setMinWidth(200);
        explorerTab.setContent(explorer);
        explorerTabPane.getTabs().addAll(explorerTab);
        editorTabPane.setLayoutX(270);
        final Pane basePane = new Pane();
        explorerTabPane.prefHeightProperty().bind(scene.heightProperty().subtract(toolBarHeight + menuBarHeight + 1));
        basePane.getChildren().addAll(explorerTabPane, new Separator(Orientation.VERTICAL), editorTabPane);
        basePane.autosize();
        baseBox.getChildren().addAll(OFA_MenuBar, toolbar, basePane);
        baseBox.prefHeightProperty().bind(primaryStage.heightProperty());
        OFAContainerWidth = scene.getWidth();
        OFAContainerHeight = scene.getHeight();
        root.getChildren().addAll(baseBox);
        primaryStage.setTitle("TestON - Automation is O{pe}N");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public void getFileMenu() {


        //adding Menus 
        Menu fileMenu = new Menu(label.fileMenu);

        //adding File Menu
        Image newImage = new Image("/images/newIcon.jpg", 20.0, 20.0, true, true);
        Image saveAllImage = new Image("/images/saveAll.jpg", 20.0, 20.0, true, true);

        Menu newFileMenu = new Menu("New", new ImageView(newImage));
        MenuItem newTest = new MenuItem("New Test");
        MenuItem componentDriver = new MenuItem(label.newDriver);
        MenuItem newParams = new MenuItem(label.newParams);
        MenuItem newTopology = new MenuItem(label.newTopology);
        MenuItem testScript = new MenuItem(label.newTestScript);
        newFileMenu.getItems().addAll(newTest, newParams, newTopology);


        newTopology.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    wizard = new TestONWizard(projectExplorerTree, 3, projectExplorerTree.getChildren(), projectExplorerTreeView);
                    wizard.setOFA(OFAReference);
                    wizard.start(new Stage());

                } catch (Exception ex) {
                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        newTest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                wizard = new TestONWizard(projectExplorerTree, 1, projectExplorerTree.getChildren(), projectExplorerTreeView);
                wizard.setOFA(OFAReference);
                try {
                    wizard.start(new Stage());
                } catch (Exception ex) {
                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        newParams.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                addParams = new AddParams();
                addParams.setOFA(OFAReference);
                addParams.getNewParams();
            }
        });


        Image openImage = new Image("/images/open.png", 15.0, 15.0, true, true);
        Menu open = new Menu(label.open, new ImageView(openImage));
        MenuItem openParams = new MenuItem("Params");
        Menu openTestScript = new Menu("Test Script");
        MenuItem pyTest = new MenuItem("Python TestScript");
        MenuItem ospkTest = new MenuItem("OpenSpeak TestScript");
        openTestScript.getItems().addAll(pyTest, ospkTest);

        MenuItem openTopology = new MenuItem("Topology");
        MenuItem openDriver = new MenuItem("Driver");

        open.getItems().addAll(openParams, openTopology, openTestScript);

        openParams.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                openFile("params", "");
            }
        });

        ospkTest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                openFile("ospk", "");
            }
        });

        pyTest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                openFile("py", "");
            }
        });

        openTopology.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                openFile("topo", "");
            }
        });
        
        Image saveImage = new Image("/images/Save_24x24.png", 15.0, 15.0, true, true);
        MenuItem saveFileItem = new MenuItem(label.save, new ImageView(saveImage));

        MenuItem saveAs = new MenuItem(label.saveAs);
        saveAll = new MenuItem(label.saveAll, new ImageView(saveAllImage));

        Image exitImage = new Image("/images/exit.gif", 15.0, 15.0, true, true);
        MenuItem exit = new MenuItem(label.exit, new ImageView(exitImage));
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                copyStage.close();
            }
        });
        Image closeImage = new Image("/images/close_icon2.jpg", 15.0, 15.0, true, true);
        MenuItem close = new MenuItem(label.close, new ImageView(closeImage));
        closeAll = new MenuItem(label.closeAll);
        fileMenu.getItems().addAll(newFileMenu, open, saveFileItem, saveAll, saveAs, new SeparatorMenuItem(), close, closeAll, new SeparatorMenuItem(), exit);

        OFA_MenuBar.getMenus().addAll(fileMenu);
    }

    public void getProjectExplorer() {

        projectWorkSpacePath = label.hierarchyTestON;
        File[] file = File.listRoots();
        Path name = new File(projectWorkSpacePath).toPath();
        TAILoadTree treeNode = new TAILoadTree(name);

        treeNode.setExpanded(true);
        int loop = 0;
        while (loop < 3) {
            for (int i = 0; i < treeNode.getChildren().size(); i++) {
                if (treeNode.getChildren().get(i).getValue().equals("__init__")) {
                    treeNode.getChildren().remove(treeNode.getChildren().get(i));

                } else if (treeNode.getChildren().get(i).getValue().equals("bin")) {
                    treeNode.getChildren().remove(treeNode.getChildren().get(i));
                } else if (treeNode.getChildren().get(i).getValue().equals("config")) {
                    treeNode.getChildren().remove(treeNode.getChildren().get(i));
                } else if (treeNode.getChildren().get(i).getValue().equals("core")) {
                    treeNode.getChildren().remove(treeNode.getChildren().get(i));
                } else if (treeNode.getChildren().get(i).getValue().equals("Documentation")) {
                    treeNode.getChildren().remove(treeNode.getChildren().get(i));
                } else if (treeNode.getChildren().get(i).getValue().equals("drivers")) {
                    treeNode.getChildren().remove(treeNode.getChildren().get(i));
                } else if (treeNode.getChildren().get(i).getValue().equals("lib")) {
                    treeNode.getChildren().remove(treeNode.getChildren().get(i));
                } else if (treeNode.getChildren().get(i).getValue().equals("logs")) {
                    treeNode.getChildren().remove(treeNode.getChildren().get(i));
                }

            }
            loop++;
        }


        projectExplorerTree = treeNode;
        projectExplorerTreeView = new TreeView<String>(treeNode);
        projectExplorerTreeView.setShowRoot(false);
        VBox projectExpl = new VBox();
        Button newTest = new Button("New Test");
        projectExplorerTreeView.setMinHeight(scene.getHeight() - (40 + toolBarHeight + menuBarHeight));
        projectExpl.getChildren().addAll(newTest, projectExplorerTreeView);
        projectExplorer = new TitledPane("Project Explorer", projectExpl);
        projectExplorer.setContent(projectExpl);

        newTest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                wizard = new TestONWizard(projectExplorerTree, 1, projectExplorerTree.getChildren(), projectExplorerTreeView);
                wizard.setOFA(OFAReference);
                try {
                    wizard.start(new Stage());
                } catch (Exception ex) {
                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        projectExplorerTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {

                if (arg0.getButton() == MouseButton.SECONDARY) {
                    contextMenu();
                    contextMenu.show(projectExplorerTreeView, arg0.getScreenX(), arg0.getScreenY());

                } else if (arg0.getClickCount() == 2) {
                    String path = "";
                    TreeItem<String> selectedTreeItem = projectExplorerTreeView.getSelectionModel().getSelectedItem();
                    advanceMode.setDisable(false);
                    try {
                       
                        if (selectedTreeItem.getParent().getParent().getValue().equals("tests")) {
                            path = label.hierarchyTestON + "/tests/" + selectedTreeItem.getParent().getValue().toString() + "/" + selectedTreeItem.getValue();
                        } else if (selectedTreeItem.getParent().getParent().getValue().equals("examples")) {
                            path = label.hierarchyTestON + "examples/" + selectedTreeItem.getParent().getValue().toString() + "/" + selectedTreeItem.getValue();
                        }
                        
                         if (selectedTreeItem.isLeaf()) {
                            if (selectedTreeItem.getGraphic().getId().equals(".params")) {
                                path = path + ".params";
                            } else if (selectedTreeItem.getGraphic().getId().equals(".topo")) {
                                path = path + ".topo";
                            } else if (selectedTreeItem.getGraphic().getId().equals(".py")) {
                                path = path + ".py";
                            } else if (selectedTreeItem.getGraphic().getId().equals(".ospk")) {
                                path = path + ".ospk";
                            }
                          
                            String fileContent = fileOperation.getContents(new File(path));
                            editorFlag = true;
                            checkEditor();
                            OFATabEditor(new CodeEditor(fileContent), new CodeEditorParams(fileContent), new File(path).getAbsolutePath(), "");
                        } else if (!selectedTreeItem.isLeaf()) {
                        }



                    } catch (Exception e) {
                    }
                }
            }
        });

        projectExplorerTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValus, Object newValue) {
                ObservableList<TreeItem<String>> tata = projectExplorerTreeView.getSelectionModel().getSelectedItems();

                TreeItem treeItem = (TreeItem) newValue;
            }
        });


        explorerTab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event closeEvent) {
                explorerTabPane.setVisible(false);
                projectExplorerFlag = false;
                checkEditor();

            }
        });


    }

    public void loadLogExlporer() {
        String projectWorkSpacePaths = label.hierarchyTestON + "/logs/";
        File[] file = File.listRoots();
        Path name = new File(projectWorkSpacePaths).toPath();
        TAILoadTree treeNode = new TAILoadTree(name);
        treeNode.setExpanded(true);
        logExplorerTreeView = new TreeView<String>(treeNode);
    }

    public void getLogExplorer() {

        loadLogExlporer();
        VBox logExpl = new VBox();
        logExplorerTreeView.setMinHeight(scene.getHeight() - (toolBarHeight + menuBarHeight + 20));
        logExpl.getChildren().addAll(logExplorerTreeView);
        logExlorer = new TitledPane("Log Explorer", logExpl);

        VBox logExplore = new VBox();
        Button refresh = new Button("refresh");
        logExplorerTreeView.setMinHeight(scene.getHeight() - (40 + toolBarHeight + menuBarHeight));
        logExplore.getChildren().addAll(refresh, logExplorerTreeView);
        logExlorer.setContent(logExplore);

        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                String projectWorkSpacePaths = label.hierarchyTestON + "/logs/";
                File[] file = File.listRoots();
                Path name = new File(projectWorkSpacePaths).toPath();
                TAILoadTree treeNode = new TAILoadTree(name);
                treeNode.setExpanded(true);

                logExplorerTreeView.setRoot(treeNode);
            }
        });


        logExplorerTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2 && click.getButton() == MouseButton.PRIMARY) {
                    TreeItem<String> selectedItem = logExplorerTreeView.getSelectionModel().getSelectedItem();
                    String fileExtentsion = selectedItem.getGraphic().getId();
                    String selectedFilePath = label.hierarchyTestON + "/logs/" + selectedItem.getParent().getValue() + "/" + selectedItem.getValue() + fileExtentsion;
                    String fileContent = fileOperation.getContents(new File(selectedFilePath));
                    editorFlag = true;

                    checkEditor();
                    OFATabEditor(new CodeEditor(fileContent), new CodeEditorParams(""), new File(selectedFilePath).getAbsolutePath(), "");
                }
            }
        });
    }

    public void getDriverExplorer() {

        projectWorkSpacePath = label.hierarchyTestON + "/drivers/common";

        final Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/project.jpeg"), 16, 16, true, true));
        driverExplorerTree = new TreeItem<String>("Drivers");
        File[] file = File.listRoots();

        Path name = new File(projectWorkSpacePath).toPath();
        TAILoadTree treeNode = new TAILoadTree(name);

        driverExplorerTree.setExpanded(true);
        //create the tree view
        driverExplorerTreeView = new TreeView<String>(treeNode);
        driverExplorer = new TitledPane("Driver Explorer", driverExplorerTreeView);

        driverExplorer.prefHeight(scene.heightProperty().get());

        VBox driverExpl = new VBox();
        driverExpl.getChildren().addAll(new Button("New Driver"), driverExplorerTreeView);
        driverExplorerTreeView.setShowRoot(false);
        driverExplorerTreeView.setMinHeight(scene.getHeight() - (toolBarHeight + menuBarHeight + 50));

        driverExplorer.setContent(driverExpl);
        driverExplorer.prefHeight(scene.heightProperty().get());

        ObservableList<TreeItem<String>> children = driverExplorerTree.getChildren();
        Iterator<TreeItem<String>> tree = children.iterator();
        while (tree.hasNext()) {
            TreeItem<String> value = tree.next();
            if (value.getValue().equals("common")) {
                driverExplorerTreeView.setShowRoot(false);
            }
        }

        driverExplorerTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                    //final Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/project.jpeg")));
                    TreeItem<String> driverSelectedItem = driverExplorerTreeView.getSelectionModel().getSelectedItem();

                    try {
                        String[] splitSelectedDriver = driverSelectedItem.getValue().split("\\.");

                        String path = null;
                        TreeItem selectedTreeItem = driverSelectedItem.getParent();
                        TreeItem selectedParentItem = selectedTreeItem.getParent();

                        path = projectWorkSpacePath + "/" + driverSelectedItem.getParent().getParent().getValue() + "/" + driverSelectedItem.getParent().getValue()
                                + "/" + driverSelectedItem.getValue() + ".py";

                        if (driverSelectedItem.getParent().getValue().equals("emulator")) {
                            path = projectWorkSpacePath + "/" + driverSelectedItem.getParent().getParent().getValue() + "/" + driverSelectedItem.getParent().getValue()
                                    + "/" + driverSelectedItem.getValue() + ".py";
                        } else if (driverSelectedItem.getParent().getValue().equals("api")) {
                            path = projectWorkSpacePath + "/" + driverSelectedItem.getParent().getValue() + "/" + driverSelectedItem.getValue() + ".py";
                        } else if (driverSelectedItem.getParent().getValue().equals("tool")) {
                            path = projectWorkSpacePath + "/" + driverSelectedItem.getParent().getParent().getValue() + "/" + driverSelectedItem.getParent().getValue()
                                    + "/" + driverSelectedItem.getValue() + ".py";
                        } else if (driverSelectedItem.getParent().getValue().equals("cli")) {
                            path = projectWorkSpacePath + "/" + driverSelectedItem.getParent().getValue() + "/" + driverSelectedItem.getValue() + ".py";
                        }
                        String fileContent = fileOperation.getContents(new File(path));
                        projectExplorerFlag = true;
                        checkEditor();
                        OFATabEditor(new CodeEditor(fileContent), new CodeEditorParams(""), path, driverSelectedItem.getValue());


                    } catch (Exception e) {
                    }

                }
            }
        });



        driverExplorerTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValus, Object newValue) {
                ObservableList<TreeItem<String>> tata = projectExplorerTreeView.getSelectionModel().getSelectedItems();

                TreeItem treeItem = (TreeItem) newValue;
            }
        });

        driverExplorerTree.setExpanded(true);

    }

    
    public void contextMenu() {
        contextMenu = new ContextMenu();
        Image copyImage = new Image("/images/Copy.png", 20.0, 20.0, true, true);
        Image cutImage = new Image("/images/Cut.png", 20.0, 20.0, true, true);
        Image pasteImage = new Image("/images/Paste.jpg", 20.0, 20.0, true, true);
        Image deleteImage = new Image("/images/delete.png", 20.0, 20.0, true, true);

        Menu newContextMenu = new Menu(label.contextNew);
        MenuItem scriptContextMenu = new MenuItem(label.contextTest);
        newContextMenu.getItems().add(scriptContextMenu);
        MenuItem openContextMenu = new MenuItem(label.contextOpen);
        MenuItem cutContextMenu = new MenuItem(label.contextCut, new ImageView(cutImage));
        MenuItem copyContextMenu = new MenuItem(label.contextCopy, new ImageView(copyImage));
        MenuItem pasteContextMenu = new MenuItem(label.contextPaste, new ImageView(pasteImage));
        MenuItem refreshContextMenu = new MenuItem(label.contextRefresh);
        MenuItem deleteContextMenu = new MenuItem(label.contextDelete, new ImageView(deleteImage));

        refreshContextMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                refreshLogExplorer();
            }
        });

        openContextMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent args0) {
                TreeItem<String> selectedTreeItem = projectExplorerTreeView.getSelectionModel().getSelectedItem();

            }
        });

        contextMenu.getItems().addAll(newContextMenu, openContextMenu, cutContextMenu, copyContextMenu, pasteContextMenu, refreshContextMenu, deleteContextMenu);
        projectExplorerTreeView.contextMenuProperty().setValue(contextMenu);
        scriptContextMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
            }
        });
    }

    public void getEditMenu() {
        //adding Edit Menu
        Image copyImage = new Image("/images/Copy.png", 20.0, 20.0, true, true);
        Image cutImage = new Image("/images/Cut.png", 20.0, 20.0, true, true);
        Image pasteImage = new Image("/images/Paste.jpg", 20.0, 20.0, true, true);


        Menu editMenu = new Menu(label.editMenu);
        MenuItem cut = new MenuItem(label.cut, new ImageView(cutImage));
        MenuItem copy = new MenuItem(label.copy, new ImageView(copyImage));
        MenuItem paste = new MenuItem(label.paste, new ImageView(pasteImage));
        MenuItem select = new MenuItem(label.select);
        MenuItem selectAll = new MenuItem(label.selectAll);

        editMenu.getItems().addAll(cut, copy, paste, select, selectAll);
        OFA_MenuBar.getMenus().addAll(editMenu);


    }

    public void getViewMenu() {
        //add View Menu
        Menu viewMenu = new Menu(label.viewMenu);
        MenuItem Explorer = new MenuItem("Explorer");
        MenuItem commandOptions = new MenuItem("CommandLine Explorer");


        viewMenu.getItems().addAll(Explorer);
        OFA_MenuBar.getMenus().addAll(viewMenu);

        Explorer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                explorerTab = new Tab("Explorer");
                explorerTabPane.setLayoutX(0);
                projectExplorerFlag = true;

                explorerTab.setContent(explorer);

                explorerTabPane.getTabs().addAll(explorerTab);
                editorTabPane.setLayoutX(270);
                explorerTabPane.setVisible(true);
                projectExplorerFlag = true;


                checkEditor();


            }
        });


    }

    public void getToolBar() {
        Image newImage = new Image("/images/newIcon.jpg", 20.0, 20.0, true, true);
        MenuButton New = new MenuButton("", new ImageView(newImage));
        New.setTooltip(new Tooltip("New"));
        MenuItem newTest = new MenuItem("New Test");
        MenuItem componentDriver = new MenuItem(label.newDriver);
        MenuItem newParams = new MenuItem(label.newParams);
        MenuItem newTopology = new MenuItem(label.newTopology);
        MenuItem testScript = new MenuItem(label.newTestScript);

        New.getItems().addAll(newTest, newParams, newTopology);
        New.setMinWidth(2);

        newTest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                wizard = new TestONWizard(projectExplorerTree, 1, projectExplorerTree.getChildren(), projectExplorerTreeView);
                wizard.setOFA(OFAReference);
                try {
                    wizard.start(new Stage());
                } catch (Exception ex) {
                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        newTopology.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                try {
                    wizard = new TestONWizard(projectExplorerTree, 3, projectExplorerTree.getChildren(), projectExplorerTreeView);
                    wizard.setOFA(OFAReference);
                    wizard.start(new Stage());

                } catch (Exception ex) {
                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        newParams.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                addParams = new AddParams();
                addParams.setOFA(OFAReference);
                addParams.getNewParams();

            }
        });

        //Open Icon on Tool Bar
        Image openImage = new Image("/images/open.png", 20.0, 20.0, true, true);
        MenuButton Open = new MenuButton("", new ImageView(openImage));
        Open.setTooltip(new Tooltip("Open"));
        MenuItem openComponentDriver = new MenuItem(label.newDriver);
        MenuItem openParams = new MenuItem(label.newParams);
        MenuItem openTopology = new MenuItem(label.newTopology);
        MenuItem openTestScript = new MenuItem(label.newTestScript);

        Open.getItems().addAll(openParams, openTopology, openTestScript);
        Image saveImage = new Image("/images/Save_24x24.png", 20.0, 20.0, true, true);
        Save = new Button("", new ImageView(saveImage));
        Save.setTooltip(new Tooltip("Save"));

        openParams.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                 openFile("params", "");
            }
        });
        
        openTopology.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                 openFile("topo", "");
            }
        });
        
        openTestScript.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                 openFile("ospk", "py");
            }
        });
        
        Image playImage = new Image("/images/Play.png", 20.0, 20.0, true, true);
        MenuButton play = new MenuButton("", new ImageView(playImage));
        play.setTooltip(new Tooltip("Run"));

        run = new MenuItem("Run");
        MenuItem runWithOptionButton = new MenuItem("Run With ...");
        MenuItem runWithExamples = new MenuItem("Run Examples");
        
         
        play.getItems().addAll(run, runWithOptionButton,runWithExamples);
        Image pauseImage = new Image("/images/Pause.png", 20.0, 20.0, true, true);
        Button pause = new Button("", new ImageView(pauseImage));
        pause.setTooltip(new Tooltip("Pause"));

        String advanceModeImgPath = "/images/switch.png";
        advanceMode = new Button();
        Image advanceModeImage = new Image(getClass().getResourceAsStream(advanceModeImgPath), 24.0, 24.0, true, true);
        final ImageView imageAdvanceMode = new ImageView(advanceModeImage);
        advanceMode.setGraphic(imageAdvanceMode);
        advanceMode.setMaxWidth(1);

        run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                testSummaryPop = new TAITestSummary(OFAReference, copyStage);
                testSummaryPop.start(new Stage());
                Runnable firstRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Runnable secondRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ExecuteTest testExecute = new ExecuteTest(testSummaryPop.getTable(), testSummaryPop.getData(), testSummaryPop.getChart(),
                                    testSummaryPop.getFinalSummaryTable(), testSummaryPop.getFinalSummaryData(),
                                    testSummaryPop.getpieChartData(),
                                    testSummaryPop.getPassData(), testSummaryPop.getFailData(), testSummaryPop.getAbortData(),
                                    testSummaryPop.getNoResultData(), editorTabPane.getSelectionModel().getSelectedItem().getText().substring(0, editorTabPane.getSelectionModel().getSelectedItem().getText().lastIndexOf('.')), testSummaryPop.getTextArea("log"), testSummaryPop.getStepTable(), testSummaryPop.getStepData(), testSummaryPop.getTextArea("pox"), testSummaryPop.getTextArea("mininet"),
                                    testSummaryPop.getTextArea("flowvisor"), testSummaryPop);

                            testExecute.runTest();
                        } catch (Exception iex) {
                        }
                    }
                };

                Platform.runLater(firstRunnable);
                Platform.runLater(secondRunnable);
            }
        });


        runWithOptionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                TAITestParameters testParameter = new TAITestParameters(OFAReference,"tests");
                testParameter.setProjectView(projectExplorerTreeView);
                for (int k = 0; k < projectExplorerTree.getChildren().size(); k++) {
                    if (projectExplorerTree.getChildren().get(k).getValue().equals("tests")) {
                        testTree = projectExplorerTree.getChildren().get(k);
                    }
                }
                testParameter.setProjectList(testTree.getChildren());
                testParameter.start(new Stage());
            }
        });

        runWithExamples.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                TAITestParameters testParameter = new TAITestParameters(OFAReference,"examples");
                testParameter.setProjectView(projectExplorerTreeView);
                for (int k = 0; k < projectExplorerTree.getChildren().size(); k++) {
                    if (projectExplorerTree.getChildren().get(k).getValue().equals("examples")) {
                        testTree = projectExplorerTree.getChildren().get(k);
                    }
                }
                testParameter.setProjectList(testTree.getChildren());
                testParameter.start(new Stage());
            }
        });
        
        Image stopImage = new Image("/images/Stop.png", 20.0, 20.0, true, true);
        Button stop = new Button("", new ImageView(stopImage));
        stop.setTooltip(new Tooltip("Stop"));

        Image resumeImage = new Image("/images/Resume_1.png", 20.0, 20.0, true, true);
        Button resume = new Button("", new ImageView(resumeImage));
        resume.setTooltip(new Tooltip("Resume"));

        Image copyImage = new Image("/images/Copy.png", 20.0, 20.0, true, true);
        Button copy = new Button("", new ImageView(copyImage));
        copy.setTooltip(new Tooltip("Copy"));

        Image cutImage = new Image("/images/Cut.png", 20.0, 20.0, true, true);
        Button cut = new Button("", new ImageView(cutImage));
        cut.setTooltip(new Tooltip("Cut"));

        Image pasteImage = new Image("/images/Paste.jpg", 20.0, 20.0, true, true);
        Button paste = new Button("", new ImageView(pasteImage));
        paste.setTooltip(new Tooltip("Paste"));

        Image exitImage = new Image("/images/exit.gif", 20.0, 20.0, true, true);
        Button exit = new Button("", new ImageView(exitImage));
        exit.setTooltip(new Tooltip("Exit"));

        exit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                copyStage.close();
            }
        });
        Image toolImage = new Image("/images/Tools.jpg", 20.0, 20.0, true, true);
        Button tools = new Button("", new ImageView(toolImage));
        tools.setTooltip(new Tooltip("ToolSelectionWindow"));


        tools.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ToolSelection toolSelection = new ToolSelection();
                try {
                    toolSelection.start(new Stage());
                } catch (Exception ex) {
                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        toolbar.getItems().addAll(New, Open, Save, new Separator(Orientation.VERTICAL), cut, copy, paste, new Separator(Orientation.VERTICAL),play, new Separator(Orientation.VERTICAL), advanceMode, new Separator(Orientation.VERTICAL), tools, exit);

    }

    public void getRunMenu() {
        //adding Run Menu
        Menu runMenu = new Menu(label.runMenu);
        Image pauseImage = new Image("/images/Pause.png", 20.0, 20.0, true, true);
        Image playImage = new Image("/images/Play.png", 20.0, 20.0, true, true);
        Image stopImage = new Image("/images/Stop.png", 20.0, 20.0, true, true);
        Image configImage = new Image("/images/Settings.jpg", 20.0, 20.0, true, true);
        Image resumeImage = new Image("/images/Resume_1.png", 20.0, 20.0, true, true);


        runTestMenu = new MenuItem(label.runMenu, new ImageView(playImage));
        MenuItem pause = new MenuItem(label.pauseTest, new ImageView(pauseImage));
        MenuItem resume = new MenuItem(label.resumeTest, new ImageView(resumeImage));
        MenuItem stop = new MenuItem(label.stopTest, new ImageView(stopImage));
        MenuItem config = new MenuItem("Settings", new ImageView(configImage));

        config.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ConfigSettings configSet = new ConfigSettings();
                configSet.start(new Stage());
            }
        });

        runMenu.getItems().addAll(runTestMenu, pause, resume, stop, new SeparatorMenuItem(), config);
        OFA_MenuBar.getMenus().addAll(runMenu);
    }

    public void getHelpMenu() {
        //adding Help Menu
        Menu helpMenu = new Menu(label.helpMenu);
        MenuItem about = new MenuItem(label.aboutHelp);
        MenuItem help = new MenuItem(label.helpMenu);

        helpMenu.getItems().addAll(about, help);
        OFA_MenuBar.getMenus().addAll(helpMenu);

    }

    public void OFATabEditor(final CodeEditor Content, final CodeEditorParams ContentParams, final String title, String str) {
        //saveFileItem.setDisable(false);
        ContentParams.setOFA(OFAReference);

        final Tab editorTab = new Tab();
        final File fileName;
        ObservableList<Tab> allTab = editorTabPane.getTabs();
        if (allTab.isEmpty()) {
            String fname = "";
            String ext = "";
            int mid = title.lastIndexOf(".");
            fname = title.substring(0, mid);
            ext = title.substring(mid + 1, title.length());

            if ("params".equals(ext) || "topo".equals(ext)) {

                ContentParams.prefWidthProperty().bind(scene.widthProperty().subtract(270));

                fileName = new File(title);
                editorTab.setText(fileName.getName());
                editorTab.setContent(ContentParams);
                editorTab.setId(title);
                String currentTab1 = editorTab.getId();

                editorTabPane.getTabs().add(editorTab);

                javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                selectionModel.select(editorTab);
                ContentParams.setOnKeyReleased(new EventHandler<InputEvent>() {
                    @Override
                    public void handle(InputEvent arg0) {

                        ArrayList<String> textContent = new ArrayList<String>();
                        String textAreaCon = ContentParams.getCodeAndSnapshot();
                        String[] textAreaContent = textAreaCon.split("\n");
                        for (String string : textAreaContent) {
                            textContent.add(string);
                        }


                        String selectedTab = editorTabPane.getSelectionModel().getSelectedItem().getText();
                        String[] selectedTabExtintion = selectedTab.split("\\.");

                        if (selectedTabExtintion[1].equals("params")) {
                            syntaxValidator = new TAISyntaxValidator(ContentParams);
                            syntaxValidator.validatorParams(textContent);

                        }
                        if (selectedTabExtintion[1].equals("topo")) {

                            syntaxValidator = new TAISyntaxValidator(ContentParams);
                            syntaxValidator.validatorParams(textContent);
                        }


                        Set set = syntaxValidator.paramFileErrorHash.entrySet();
                        Iterator errorHashIterator = set.iterator();
                        String line;
                        String error;
                        for (String clearGutterLine : errorAtLineList) {
                            ContentParams.clearMarker(clearGutterLine);
                        }
                        while (errorHashIterator.hasNext()) {
                            Map.Entry errorInfo = (Map.Entry) errorHashIterator.next();
                            line = errorInfo.getKey().toString();
                            try {
                                error = errorInfo.getValue().toString();
                                syntaxError.put(line, error);
                                ContentParams.SetError(line, error);
                            } catch (Exception e) {
                            }

                            errorAtLineList.add(line);
                        }

                        Set paramFileWarnSet = syntaxValidator.paramFileWarnHash.entrySet();
                        Iterator paramWarnIterastor = paramFileWarnSet.iterator();
                        while (paramWarnIterastor.hasNext()) {
                            Map.Entry errorInfo = (Map.Entry) paramWarnIterastor.next();
                            line = errorInfo.getKey().toString();
                            error = errorInfo.getValue().toString();
                            syntaxError.put(line, error);
                            ContentParams.SetWarning(line, error);
                            errorAtLineList.add(line);
                        }

                        Set paramFileInfoSet = syntaxValidator.paramFileInfoHash.entrySet();
                        Iterator paramInfoIterastor = paramFileInfoSet.iterator();
                        while (paramInfoIterastor.hasNext()) {
                            Map.Entry errorInfo = (Map.Entry) paramInfoIterastor.next();
                            line = errorInfo.getKey().toString();
                            error = errorInfo.getValue().toString();
                            syntaxError.put(line, error);
                            ContentParams.SetInfo(line, error);
                            errorAtLineList.add(line);
                        }


                        javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                        String currentTab1 = selectionModel.getSelectedItem().getId();
                        String currentTabTitle = selectionModel.getSelectedItem().getText();
                        if (fileName.getName().equals(currentTabTitle)) {
                            selectionModel.getSelectedItem().setText("* " + currentTabTitle);
                            Save.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent arg0) {

                                    javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                                    String currentTab1 = selectionModel.getSelectedItem().getId();
                                    selectionModel.getSelectedItem().setText(fileName.getName());
                                    String currentFileContent = ContentParams.getCodeAndSnapshot();

                                    try {

                                        ArrayList<String> textContent = new ArrayList<String>();
                                        String textAreaCon = ContentParams.getCodeAndSnapshot();

                                        String[] textAreaContent = textAreaCon.split("\n");

                                        for (String string : textAreaContent) {
                                            textContent.add(string);
                                        }

                                        HashMap errorHash = new HashMap();
                                        fileOperation.saveFile(fileName, currentFileContent);
                                    } catch (FileNotFoundException ex) {
                                        Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                }
                            });
                        } else {
                        }
                    }
                });

            } else {
                fileName = new File(title);

                editorTab.setText(fileName.getName());
                editorTab.setId(title);
                Content.prefWidthProperty().bind(scene.widthProperty());
                editorTab.setContent(Content);
                String currentTab = editorTab.getId();

                editorTabPane.getTabs().addAll(editorTab);

                javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                selectionModel.select(editorTab);
                Content.setOnKeyReleased(new EventHandler<InputEvent>() {
                    @Override
                    public void handle(InputEvent arg0) {
                        javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                        String currentTab = selectionModel.getSelectedItem().getId();
                        File file = new File(currentTab);

                        String currentTabTitle = selectionModel.getSelectedItem().getText();

                        ArrayList<String> textContent = new ArrayList<String>();
                        String textAreaCon = Content.getCodeAndSnapshot();
                        String[] textAreaContent = textAreaCon.split("\n");
                        for (String string : textAreaContent) {
                            textContent.add(string);
                        }

                        if (currentTabTitle.split("\\.")[1].equals("ospk")) {
                            syntaxValidator = new TAISyntaxValidator(Content);
                            syntaxValidator.ospkValidator(textContent);
                        }

                        for (String clearGutterLine : errorAtLineList) {
                            Content.clearMarker(clearGutterLine);
                        }
                        try {
                            Set ospkErrorSet =
                                    syntaxValidator.ospkErrorHash.entrySet();
                            Iterator ospkErrorIterator = ospkErrorSet.iterator();
                            while (ospkErrorIterator.hasNext()) {
                                Map.Entry ospkErrorInfo = (Map.Entry) ospkErrorIterator.next();
                                Content.SetError(ospkErrorInfo.getKey().toString(),
                                        ospkErrorInfo.getValue().toString());
                                errorAtLineList.add(ospkErrorInfo.getKey().toString());
                            }

                            Set ospkWarnSet =
                                    syntaxValidator.ospkWarnHash.entrySet();
                            Iterator ospkWarnIterator = ospkWarnSet.iterator();
                            while (ospkWarnIterator.hasNext()) {
                                Map.Entry ospkWarn =
                                        (Map.Entry) ospkWarnIterator.next();
                                Content.SetWarning(ospkWarn.getKey().toString(),
                                        ospkWarn.getValue().toString());
                                errorAtLineList.add(ospkWarn.getKey().toString());
                            }

                            Set ospkInfoSet =
                                    syntaxValidator.ospkInfoHash.entrySet();
                            Iterator ospkInfoIterator = ospkInfoSet.iterator();
                            while (ospkInfoIterator.hasNext()) {
                                Map.Entry ospkInfo =
                                        (Map.Entry) ospkInfoIterator.next();
                                Content.SetInfo(ospkInfo.getKey().toString(),
                                        ospkInfo.getValue().toString());
                                errorAtLineList.add(ospkInfo.getKey().toString());
                            }


                        } catch (NullPointerException e) {
                        }




                        if (file.getName().equals(currentTabTitle)) {
                            selectionModel.getSelectedItem().setText("* " + currentTabTitle);
                            Save.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent arg0) {
                                    javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                                    String currentTab = selectionModel.getSelectedItem().getId();
                                    File file = new File(currentTab);
                                    selectionModel.getSelectedItem().setText(file.getName());
                                    File fileName = new File(currentTab);
                                    String currentFileContent = Content.getCodeAndSnapshot();
                                    try {
                                        fileOperation.saveFile(fileName, currentFileContent);
                                    } catch (FileNotFoundException ex) {
                                        Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                }
                            });
                        } else {
                        }

                    }
                });


                advanceMode.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                        final String currentTab = selectionModel.getSelectedItem().getId();
                        File file = new File(currentTab);
                        selectionModel.getSelectedItem().setText(file.getName());
                        
                        final File fileName = new File(currentTab);


                        int dotPos = currentTab.lastIndexOf(".");
                        String extension = currentTab.substring(dotPos);

                        if (".ospk".equals(extension)) {
                            String str = fileName.getName();

                            String filePathwithoutname = fileName.getParent();

                            String fileWithoutPath = str.substring(0, str.lastIndexOf('.'));

                            String pyfilePath = fileName.getParent() + "/" + fileWithoutPath + ".py";
                            try {
                                new File(filePathwithoutname + "/" + fileWithoutPath + ".py").createNewFile();
                                Process child = Runtime.getRuntime().exec("chmod 777 " + fileWithoutPath + "/*");
                            } catch (IOException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            String currentFileContent = Content.getCodeAndSnapshot();

                            XmlRpcClient server = null;
                            Vector params = new Vector();
                            params.add(new String(currentTab));
                            try {
                                server = new XmlRpcClient("http://localhost:9000");
                                try {
                                    resultXmlServer = server.execute("doCompile", params);
                                } catch (XmlRpcException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }


                            try {
                                Process child = Runtime.getRuntime().exec("chmod 777 " + filePathwithoutname + "/" + fileWithoutPath + ".py");
                            } catch (IOException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }


                            File pyfile = new File(pyfilePath);
                            selectionModel.getSelectedItem().setText(pyfile.getName());
                            selectionModel.getSelectedItem().setId(pyfilePath);;
                            String formattedData = fileOperation.getContents(pyfile);
                            Content.setCode(formattedData);

                        } else if (".py".equals(extension)) {

                            String str = fileName.getName();
                            String filePathwithoutname = fileName.getParent();
                            String fileWithoutPath = str.substring(0, str.lastIndexOf('.'));
                            String ospkfilePath = fileName.getParent() + "/" + fileWithoutPath + ".ospk";
                            String currentFileContent = Content.getCodeAndSnapshot();
                            XmlRpcClient server = null;
                            Vector params = new Vector();
                            params.add(new String(currentTab));
                            try {
                                server = new XmlRpcClient("http://localhost:9000");
                                try {
                                    resultXmlServer = server.execute("reverseCompile", params);
                                } catch (XmlRpcException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        
//                            String message = clientForReverse.reverse().toString();
                            File ospkfile = new File(ospkfilePath);
                            try {
                                Process child = Runtime.getRuntime().exec("chmod 777 " + ospkfilePath);
                            } catch (IOException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            selectionModel.getSelectedItem().setText(ospkfile.getName());
                            selectionModel.getSelectedItem().setId(ospkfilePath);

                            Content.setCode(resultXmlServer.toString());

                        } else if (".topo".equals(extension)) {
                         
                            
                            //   selectionModel.getSelectedItem().setText("TPLEDITOR");
                            selectionModel.getSelectedItem().setId(selectionModel.getSelectedItem().getId());
                            // selectionModel.getSelectedItem().setContent();
                            if (switchType == false) {
                                switchType = true;
                                TopologyNode node = new TopologyNode(OFAReference);
                                selectionModel.getSelectedItem().setContent(node.getNode(selectionModel.getSelectedItem().getId()));
                                node.getPropertyDetail();
                            } else {

                                //throw new UnsupportedOperationException("Not supported yet.");

                                selectionModel.getSelectedItem().getId();
                                File fileParams = new File(selectionModel.getSelectedItem().getId());
                                String fileContent = fileOperation.getContents(fileParams);
                                final CodeEditorParams paramFileContent = new CodeEditorParams(fileContent);
                                selectionModel.getSelectedItem().setContent(paramFileContent);
                                //     paramFileContent.setOnKeyPressed(new EventHandler<KeyEvent>() {

                                // @Override
                                //   public void handle(KeyEvent arg0) {
                                //throw new UnsupportedOperationException("Not supported yet.");
                                //          selectionModel.getSelectedItem().setText("* "+ selectionModel.getSelectedItem().getText() );
                                //   }
                                // });
                                paramFileContent.setOnKeyReleased(new EventHandler<InputEvent>() {
                                    @Override
                                    public void handle(InputEvent arg0) {
                                        // throw new UnsupportedOperationException("Not supported yet.");


                                        javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                                        String currentTab1 = selectionModel.getSelectedItem().getId();
                                        //      File file = new File(currentTab1);
                                        // ContentParams.coordinate();


                                        String currentTabTitle = selectionModel.getSelectedItem().getText();

                                        if (fileName.getName().equals(currentTabTitle)) {
                                            selectionModel.getSelectedItem().setText("* " + currentTabTitle);
                                            Save.setOnAction(new EventHandler<ActionEvent>() {
                                                @Override
                                                public void handle(ActionEvent arg0) {

                                                    javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                                                    String currentTab1 = selectionModel.getSelectedItem().getId();
                                                    selectionModel.getSelectedItem().setText(fileName.getName());
                                                    CodeEditorParams content = (CodeEditorParams) selectionModel.getSelectedItem().getContent();
                                                    String currentFileContent = content.getCodeAndSnapshot();


                                                    try {

                                                        // -----------------------------------------Valdation Part starts ------------------------------------                              
                                                        ArrayList<String> textContent = new ArrayList<String>();

                                                        String textAreaCon = paramFileContent.getCodeAndSnapshot();

                                                        String[] textAreaContent = textAreaCon.split("\n");

                                                        for (String string : textAreaContent) {
                                                            textContent.add(string);
                                                        }

                                                        HashMap errorHash = new HashMap();
                                                        TAISyntaxValidator syntaxValidator = new TAISyntaxValidator();
                                                        String selectedTab = editorTabPane.getSelectionModel().getSelectedItem().getText();
                                                        String[] selectedTabExtintion = selectedTab.split("\\.");
                                                        if (selectedTabExtintion[1].equals("ospk")) {
//                                                                errorHash = syntaxValidator.ospkValidator(textContent);
                                                        } else if (selectedTabExtintion[1].equals("params")) {
                                                            //                                                              errorHash = syntaxValidator.validatorParams(textContent);
                                                        } else {
                                                            //                                                             errorHash = syntaxValidator.bdrValidator(textContent);
                                                        }


                                                        Set set = errorHash.entrySet();
                                                        // Get an iterator
                                                        Iterator i = set.iterator();
                                                        // Display elements
                                                        String line = null;
                                                        String error = null;

                                                        if (i.hasNext()) {
                                                            Map.Entry me = (Map.Entry) i.next();
                                                            line = me.getKey().toString();
                                                            error = me.getValue().toString();


                                                            //   paramFileContent.Set(me.getKey().toString(), me.getValue().toString());
                                                        } else {
                                                            //  paramFileContent.Set("", "");
                                                        }
                                                        /// ContentParams.Set(null,null);

// -----------------------------------------Valdation Part ends ------------------------------------
                                                        fileOperation.saveFile(fileName, currentFileContent);
                                                    } catch (FileNotFoundException ex) {
                                                        Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                                    }

                                                }
                                            });
                                        } else {
                                        }

                                    }
                                });

                                switchType = false;
                            }

                        }

                    }
                });

                File path = new File(OFAReference.editorTabPane.getSelectionModel().getSelectedItem().getId());
                String[] currentFileExtintion = path.getName().split("\\.");
                if (currentFileExtintion[1].equals("ospk")) {
                    TAIContentHelp contentHelp = new TAIContentHelp(Content, OFAReference);
                    contentHelp.ospkHelp();
                }
            }
        } else {
            boolean tabexistsFlag = false;
           
            for (Tab tab : allTab) {
                if (tab.getId().equals(title)) {
                    javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                    selectionModel.select(tab);
                    tabexistsFlag = true;
                    break;
                } else {
                }
            }
            if (tabexistsFlag == false) {

                String fname = "";
                String ext = "";
                int mid = title.lastIndexOf(".");
                fname = title.substring(0, mid);
                ext = title.substring(mid + 1, title.length());
          









                if ("params".equals(ext) || "topo".equals(ext)) {

                    ContentParams.prefWidthProperty().bind(scene.widthProperty().subtract(500));

                    fileName = new File(title);
                    editorTab.setText(fileName.getName());
                    editorTab.setContent(ContentParams);
                    editorTab.setId(title);
                    String currentTab1 = editorTab.getId();
                    editorTabPane.getTabs().add(editorTab);

                    javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                    selectionModel.select(editorTab);

                    ContentParams.setOnKeyReleased(new EventHandler<InputEvent>() {
                        @Override
                        public void handle(InputEvent arg0) {
                            javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                            String currentTab1 = selectionModel.getSelectedItem().getId();

                            String currentTabTitle = selectionModel.getSelectedItem().getText();

                            if (fileName.getName().equals(currentTabTitle)) {

                                ArrayList<String> textContent = new ArrayList<String>();
                                String textAreaCon = ContentParams.getCodeAndSnapshot();
                                String[] textAreaContent = textAreaCon.split("\n");
                                for (String string : textAreaContent) {
                                    textContent.add(string);
                                }


                                String selectedTab = editorTabPane.getSelectionModel().getSelectedItem().getText();
                                String[] selectedTabExtintion = selectedTab.split("\\.");
                                if (selectedTabExtintion[1].equals("params")) {
                                    syntaxValidator = new TAISyntaxValidator(ContentParams);
                                    syntaxValidator.validatorParams(textContent);

                                } else if (selectedTabExtintion[1].equals("topo")) {
                                    syntaxValidator = new TAISyntaxValidator(ContentParams);
                                    syntaxValidator.validatorParams(textContent);
                                }



                                Set set = syntaxValidator.paramFileErrorHash.entrySet();
                                Iterator errorHashIterator = set.iterator();
                                String line;
                                String error;
                                for (String clearGutterLine : errorAtLineList) {
                                    ContentParams.clearMarker(clearGutterLine);
                                }
                                while (errorHashIterator.hasNext()) {
                                    Map.Entry errorInfo = (Map.Entry) errorHashIterator.next();
                                    line = errorInfo.getKey().toString();
                                    try {
                                        error = errorInfo.getValue().toString();
                                        syntaxError.put(line, error);
                                        ContentParams.SetError(line, error);
                                    } catch (Exception e) {
                                    }

                                    errorAtLineList.add(line);
                                }

                                Set paramFileWarnSet = syntaxValidator.paramFileWarnHash.entrySet();
                                Iterator paramWarnIterastor = paramFileWarnSet.iterator();
                                while (paramWarnIterastor.hasNext()) {
                                    Map.Entry errorInfo = (Map.Entry) paramWarnIterastor.next();
                                    line = errorInfo.getKey().toString();
                                    error = errorInfo.getValue().toString();
                                    syntaxError.put(line, error);
                                    ContentParams.SetWarning(line, error);
                                    errorAtLineList.add(line);
                                }

                                Set paramFileInfoSet = syntaxValidator.paramFileInfoHash.entrySet();
                                Iterator paramInfoIterastor = paramFileInfoSet.iterator();
                                while (paramInfoIterastor.hasNext()) {
                                    Map.Entry errorInfo = (Map.Entry) paramInfoIterastor.next();
                                    line = errorInfo.getKey().toString();
                                    error = errorInfo.getValue().toString();
                                    syntaxError.put(line, error);
                                    ContentParams.SetInfo(line, error);
                                    errorAtLineList.add(line);
                                }






                                selectionModel.getSelectedItem().setText("* " + currentTabTitle);
                                Save.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {

                                        javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                                        String currentTab1 = selectionModel.getSelectedItem().getId();

                                        selectionModel.getSelectedItem().setText(fileName.getName());
                                        String currentFileContent = ContentParams.getCodeAndSnapshot();
                                        try {
                                            fileOperation.saveFile(fileName, currentFileContent);
                                        } catch (FileNotFoundException ex) {
                                            Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    }
                                });
                            } else {
                            }
                        }
                    });




                } else {
                    fileName = new File(title);

                    editorTab.setText(fileName.getName());
                    editorTab.setId(title);
                    Content.prefWidthProperty().bind(scene.widthProperty());
                    editorTab.setContent(Content);
                    String currentTab = editorTab.getId();

                    editorTabPane.getTabs().addAll(editorTab);
                    javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                    selectionModel.select(editorTab);

                    Content.setOnKeyReleased(new EventHandler<InputEvent>() {
                        @Override
                        public void handle(InputEvent arg0) {
                            javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                            String currentTab = selectionModel.getSelectedItem().getId();
                            File file = new File(currentTab);

                            String currentTabTitle = selectionModel.getSelectedItem().getText();


                            if (file.getName().equals(currentTabTitle)) {
                                selectionModel.getSelectedItem().setText("* " + currentTabTitle);
                                Save.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {
                                        javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                                        String currentTab = selectionModel.getSelectedItem().getId();
                                        File file = new File(currentTab);
                                        selectionModel.getSelectedItem().setText(file.getName());
                                        File fileName = new File(currentTab);
                                        String currentFileContent = Content.getCodeAndSnapshot();
                                        try {
                                            fileOperation.saveFile(fileName, currentFileContent);
                                        } catch (FileNotFoundException ex) {
                                            Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    }
                                });
                            } else {
                            }

                        }
                    });

                    File path = new File(OFAReference.editorTabPane.getSelectionModel().getSelectedItem().getId());
                    String[] currentFileExtintion = path.getName().split("\\.");
                    if (currentFileExtintion[1].equals("ospk")) {
                        TAIContentHelp contentHelp = new TAIContentHelp(Content, OFAReference);
                        contentHelp.ospkHelp();
                    }
                }


                advanceMode.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                        final String currentTab = selectionModel.getSelectedItem().getId();
                        File file = new File(currentTab);
                        selectionModel.getSelectedItem().setText(file.getName());

                        final File fileName = new File(currentTab);


                        int dotPos = currentTab.lastIndexOf(".");
                        String extension = currentTab.substring(dotPos);

                        if (".ospk".equals(extension)) {

                            String str = fileName.getName();

                            String filePathwithoutname = fileName.getParent();

                            String fileWithoutPath = str.substring(0, str.lastIndexOf('.'));

                            String pyfilePath = fileName.getParent() + "/" + fileWithoutPath + ".py";


                            String currentFileContent = Content.getCodeAndSnapshot();

                            XmlRpcClient server = null;
                            Vector params = new Vector();
                            params.add(new String(currentTab));
                         
                            try {
                                server = new XmlRpcClient("http://localhost:9000");
                                try {
                                    final Object result = server.execute("doCompile", params);
                                } catch (XmlRpcException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }


                            File pyfile = new File(pyfilePath);
                            selectionModel.getSelectedItem().setText(pyfile.getName());
                            selectionModel.getSelectedItem().setId(pyfilePath);
                            String formattedData = fileOperation.getContents(pyfile);
                            Content.setCode(formattedData);

                        } else if (".py".equals(extension)) {

                         
                            String str = fileName.getName();
                            String filePathwithoutname = fileName.getParent();
                            String fileWithoutPath = str.substring(0, str.lastIndexOf('.'));
                            String ospkfilePath = fileName.getParent() + "/" + fileWithoutPath + ".ospk";
                            String currentFileContent = Content.getCodeAndSnapshot();
                            XmlRpcClient server = null;
                            Vector params = new Vector();
                            params.add(new String(currentTab));
                            try {
                                server = new XmlRpcClient("http://localhost:9000");
                                try {
                                    resultXmlServer = server.execute("reverseCompile", params);
                                } catch (XmlRpcException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        

                            File ospkfile = new File(ospkfilePath);
                            try {
                                Process child = Runtime.getRuntime().exec("chmod 777 " + ospkfilePath);
                            } catch (IOException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            selectionModel.getSelectedItem().setText(ospkfile.getName());
                            selectionModel.getSelectedItem().setId(ospkfilePath);

                            Content.setCode(resultXmlServer.toString());

                        } else if (".topo".equals(extension)) {

                            //   selectionModel.getSelectedItem().setText("TPLEDITOR");
                            selectionModel.getSelectedItem().setId(selectionModel.getSelectedItem().getId());
                          
                            // selectionModel.getSelectedItem().setContent();
                            if (switchType == false) {
                                switchType = true;
                                TopologyNode node = new TopologyNode(OFAReference);
                                selectionModel.getSelectedItem().setContent(node.getNode(selectionModel.getSelectedItem().getId()));
                                node.getPropertyDetail();
                            } else {

                                //throw new UnsupportedOperationException("Not supported yet.");

                                selectionModel.getSelectedItem().getId();
                                File fileParams = new File(selectionModel.getSelectedItem().getId());
                                String fileContent = fileOperation.getContents(fileParams);
                                final CodeEditorParams paramFileContent = new CodeEditorParams(fileContent);
                                selectionModel.getSelectedItem().setContent(paramFileContent);

                                paramFileContent.setOnKeyReleased(new EventHandler<InputEvent>() {
                                    @Override
                                    public void handle(InputEvent arg0) {
                                        // throw new UnsupportedOperationException("Not supported yet.");


                                        javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                                        String currentTab1 = selectionModel.getSelectedItem().getId();
                                        //      File file = new File(currentTab1);
                                        // ContentParams.coordinate();


                                        String currentTabTitle = selectionModel.getSelectedItem().getText();

                                        if (fileName.getName().equals(currentTabTitle)) {
                                            selectionModel.getSelectedItem().setText("* " + currentTabTitle);
                                            Save.setOnAction(new EventHandler<ActionEvent>() {
                                                @Override
                                                public void handle(ActionEvent arg0) {

                                                    javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                                                    String currentTab1 = selectionModel.getSelectedItem().getId();
                                                    selectionModel.getSelectedItem().setText(fileName.getName());
                                                    CodeEditorParams content = (CodeEditorParams) selectionModel.getSelectedItem().getContent();
                                                    String currentFileContent = content.getCodeAndSnapshot();


                                                    try {

                                                        // -----------------------------------------Valdation Part starts ------------------------------------                              
                                                        ArrayList<String> textContent = new ArrayList<String>();

                                                        String textAreaCon = paramFileContent.getCodeAndSnapshot();

                                                        String[] textAreaContent = textAreaCon.split("\n");

                                                        for (String string : textAreaContent) {
                                                            textContent.add(string);
                                                        }

                                                        HashMap errorHash = new HashMap();
                                                        TAISyntaxValidator syntaxValidator = new TAISyntaxValidator();
                                                        String selectedTab = editorTabPane.getSelectionModel().getSelectedItem().getText();
                                                        String[] selectedTabExtintion = selectedTab.split("\\.");
                                                        if (selectedTabExtintion[1].equals("ospk")) {
//                                                                errorHash = syntaxValidator.ospkValidator(textContent);
                                                        } else if (selectedTabExtintion[1].equals("params")) {
                                                            //                                                              errorHash = syntaxValidator.validatorParams(textContent);
                                                        } else {
                                                            //                                                             errorHash = syntaxValidator.bdrValidator(textContent);
                                                        }


                                                        Set set = errorHash.entrySet();
                                                        // Get an iterator
                                                        Iterator i = set.iterator();
                                                        // Display elements
                                                        String line = null;
                                                        String error = null;

                                                        if (i.hasNext()) {
                                                            Map.Entry me = (Map.Entry) i.next();
                                                            line = me.getKey().toString();
                                                            error = me.getValue().toString();


                                                            //   paramFileContent.Set(me.getKey().toString(), me.getValue().toString());
                                                        } else {
                                                            //  paramFileContent.Set("", "");
                                                        }
                                                        /// ContentParams.Set(null,null);

// -----------------------------------------Valdation Part ends ------------------------------------
                                                        fileOperation.saveFile(fileName, currentFileContent);
                                                    } catch (FileNotFoundException ex) {
                                                        Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                                    }

                                                }
                                            });
                                        } else {
                                        }

                                    }
                                });

                                switchType = false;
                            }

                        }

                    }
                });









            }
        }

        editorTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event arg0) {

                String currentTab = editorTab.getId();
                String fname = "";
                String ext = "";
                int mid = currentTab.lastIndexOf(".");
                fname = currentTab.substring(0, mid);
                try {
                } catch (ArrayIndexOutOfBoundsException e) {
                }
//******************************************************************************
                final File fileName = new File(currentTab);
                ContentParams.setOnKeyReleased(new EventHandler<InputEvent>() {
                        @Override
                        public void handle(InputEvent arg0) {
                            javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                            String currentTab1 = selectionModel.getSelectedItem().getId();

                            String currentTabTitle = selectionModel.getSelectedItem().getText();
                        
                            if (fileName.getName().equals(currentTabTitle)) {

                                ArrayList<String> textContent = new ArrayList<String>();
                                String textAreaCon = ContentParams.getCodeAndSnapshot();
                                String[] textAreaContent = textAreaCon.split("\n");
                                for (String string : textAreaContent) {
                                    textContent.add(string);
                                }


                                String selectedTab = editorTabPane.getSelectionModel().getSelectedItem().getText();
                                String[] selectedTabExtintion = selectedTab.split("\\.");
                                if (selectedTabExtintion[1].equals("params")) {
                                    syntaxValidator = new TAISyntaxValidator(ContentParams);
                                    syntaxValidator.validatorParams(textContent);

                                } else if (selectedTabExtintion[1].equals("topo")) {
                                    syntaxValidator = new TAISyntaxValidator(ContentParams);
                                    syntaxValidator.validatorParams(textContent);
                                }



                                Set set = syntaxValidator.paramFileErrorHash.entrySet();
                                Iterator errorHashIterator = set.iterator();
                                String line;
                                String error;
                                for (String clearGutterLine : errorAtLineList) {
                                    ContentParams.clearMarker(clearGutterLine);
                                }
                                while (errorHashIterator.hasNext()) {
                                    Map.Entry errorInfo = (Map.Entry) errorHashIterator.next();
                                    line = errorInfo.getKey().toString();
                                    try {
                                        error = errorInfo.getValue().toString();
                                        syntaxError.put(line, error);
                                        ContentParams.SetError(line, error);
                                    } catch (Exception e) {
                                    }

                                    errorAtLineList.add(line);
                                }

                                Set paramFileWarnSet = syntaxValidator.paramFileWarnHash.entrySet();
                                Iterator paramWarnIterastor = paramFileWarnSet.iterator();
                                while (paramWarnIterastor.hasNext()) {
                                    Map.Entry errorInfo = (Map.Entry) paramWarnIterastor.next();
                                    line = errorInfo.getKey().toString();
                                    error = errorInfo.getValue().toString();
                                    syntaxError.put(line, error);
                                    ContentParams.SetWarning(line, error);
                                    errorAtLineList.add(line);
                                }

                                Set paramFileInfoSet = syntaxValidator.paramFileInfoHash.entrySet();
                                Iterator paramInfoIterastor = paramFileInfoSet.iterator();
                                while (paramInfoIterastor.hasNext()) {
                                    Map.Entry errorInfo = (Map.Entry) paramInfoIterastor.next();
                                    line = errorInfo.getKey().toString();
                                    error = errorInfo.getValue().toString();
                                    syntaxError.put(line, error);
                                    ContentParams.SetInfo(line, error);
                                    errorAtLineList.add(line);
                                }






                                selectionModel.getSelectedItem().setText("* " + currentTabTitle);
                                Save.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent arg0) {

                                        javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                                        String currentTab1 = selectionModel.getSelectedItem().getId();

                                        selectionModel.getSelectedItem().setText(fileName.getName());
                                        String currentFileContent = ContentParams.getCodeAndSnapshot();
                                        try {
                                            fileOperation.saveFile(fileName, currentFileContent);
                                        } catch (FileNotFoundException ex) {
                                            Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    }
                                });
                            } else {
                            }
                        }
                    });
                
                
                
                
                
                

                advanceMode.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                        final String currentTab = selectionModel.getSelectedItem().getId();
                        File file = new File(currentTab);
                        selectionModel.getSelectedItem().setText(file.getName());

                        final File fileName = new File(currentTab);


                        int dotPos = currentTab.lastIndexOf(".");
                        String extension = currentTab.substring(dotPos);

                        if (".ospk".equals(extension)) {

                            String str = fileName.getName();

                            String filePathwithoutname = fileName.getParent();

                            String fileWithoutPath = str.substring(0, str.lastIndexOf('.'));

                            String pyfilePath = fileName.getParent() + "/" + fileWithoutPath + ".py";


                            String currentFileContent = Content.getCodeAndSnapshot();
                            XmlRpcClient server = null;
                            Vector params = new Vector();
                            params.add(new String(currentTab));
                         
                            try {
                                server = new XmlRpcClient("http://localhost:9000");
                                try {
                                    final Object result = server.execute("doCompile", params);
                                } catch (XmlRpcException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }


                            File pyfile = new File(pyfilePath);
                            selectionModel.getSelectedItem().setText(pyfile.getName());
                            selectionModel.getSelectedItem().setId(pyfilePath);
                            String formattedData = fileOperation.getContents(pyfile);
                            Content.setCode(formattedData);

                        } else if (".py".equals(extension)) {

                         
                            String str = fileName.getName();
                            String filePathwithoutname = fileName.getParent();
                            String fileWithoutPath = str.substring(0, str.lastIndexOf('.'));
                            String ospkfilePath = fileName.getParent() + "/" + fileWithoutPath + ".ospk";
                            String currentFileContent = Content.getCodeAndSnapshot();
                            XmlRpcClient server = null;
                            Vector params = new Vector();
                            params.add(new String(currentTab));
                            try {
                                server = new XmlRpcClient("http://localhost:9000");
                                try {
                                    resultXmlServer = server.execute("reverseCompile", params);
                                } catch (XmlRpcException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }

                         
                            File ospkfile = new File(ospkfilePath);
                            try {
                                Process child = Runtime.getRuntime().exec("chmod 777 " + ospkfilePath);
                            } catch (IOException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            selectionModel.getSelectedItem().setText(ospkfile.getName());
                            selectionModel.getSelectedItem().setId(ospkfilePath);

                            Content.setCode(resultXmlServer.toString());

                        } else if (".topo".equals(extension)) {

                            selectionModel.getSelectedItem().setId(selectionModel.getSelectedItem().getId());
                           
                            if (switchType == false) {
                                switchType = true;
                                TopologyNode node = new TopologyNode(OFAReference);
                                selectionModel.getSelectedItem().setContent(node.getNode(selectionModel.getSelectedItem().getId()));
                                node.getPropertyDetail();
                            } else {

                                //throw new UnsupportedOperationException("Not supported yet.");

                                selectionModel.getSelectedItem().getId();
                                File fileParams = new File(selectionModel.getSelectedItem().getId());
                                String fileContent = fileOperation.getContents(fileParams);
                                final CodeEditorParams paramFileContent = new CodeEditorParams(fileContent);
                                selectionModel.getSelectedItem().setContent(paramFileContent);

                                paramFileContent.setOnKeyReleased(new EventHandler<InputEvent>() {
                                    @Override
                                    public void handle(InputEvent arg0) {
                                        // throw new UnsupportedOperationException("Not supported yet.");


                                        javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                                        String currentTab1 = selectionModel.getSelectedItem().getId();
                                      


                                        String currentTabTitle = selectionModel.getSelectedItem().getText();

                                        if (fileName.getName().equals(currentTabTitle)) {
                                            selectionModel.getSelectedItem().setText("* " + currentTabTitle);
                                            Save.setOnAction(new EventHandler<ActionEvent>() {
                                                @Override
                                                public void handle(ActionEvent arg0) {

                                                    javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                                                    String currentTab1 = selectionModel.getSelectedItem().getId();
                                                    selectionModel.getSelectedItem().setText(fileName.getName());
                                                    CodeEditorParams content = (CodeEditorParams) selectionModel.getSelectedItem().getContent();
                                                    String currentFileContent = content.getCodeAndSnapshot();


                                                    try {

                                                        // -----------------------------------------Valdation Part starts ------------------------------------                              
                                                        ArrayList<String> textContent = new ArrayList<String>();

                                                        String textAreaCon = paramFileContent.getCodeAndSnapshot();

                                                        String[] textAreaContent = textAreaCon.split("\n");

                                                        for (String string : textAreaContent) {
                                                            textContent.add(string);
                                                        }

                                                        HashMap errorHash = new HashMap();
                                                        TAISyntaxValidator syntaxValidator = new TAISyntaxValidator();
                                                        String selectedTab = editorTabPane.getSelectionModel().getSelectedItem().getText();
                                                        String[] selectedTabExtintion = selectedTab.split("\\.");
                                                        if (selectedTabExtintion[1].equals("ospk")) {
//                                                                errorHash = syntaxValidator.ospkValidator(textContent);
                                                        } else if (selectedTabExtintion[1].equals("params")) {
                                                            //                                                              errorHash = syntaxValidator.validatorParams(textContent);
                                                        } else {
                                                            //                                                             errorHash = syntaxValidator.bdrValidator(textContent);
                                                        }


                                                        Set set = errorHash.entrySet();
                                                        // Get an iterator
                                                        Iterator i = set.iterator();
                                                        // Display elements
                                                        String line = null;
                                                        String error = null;

                                                        if (i.hasNext()) {
                                                            Map.Entry me = (Map.Entry) i.next();
                                                            line = me.getKey().toString();
                                                            error = me.getValue().toString();


                                                         
                                                        } else {
                                                           
                                                        }
                                                       

// -----------------------------------------Valdation Part ends ------------------------------------
                                                        fileOperation.saveFile(fileName, currentFileContent);
                                                    } catch (FileNotFoundException ex) {
                                                        Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                                                    }

                                                }
                                            });
                                        } else {
                                        }

                                    }
                                });

                                switchType = false;
                            }

                        }

                    }
                });










//***************************************************************************
                
                if ("params".equals(ext) || "topo".equals(ext)) {


                    Save.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent arg0) {
                            javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                            String currentTab = selectionModel.getSelectedItem().getId();
                            File file = new File(currentTab);
                            selectionModel.getSelectedItem().setText(file.getName());
                            File fileName = new File(currentTab);
                            String currentFileContent = ContentParams.getCodeAndSnapshot();
                            try {
                                fileOperation.saveFile(fileName, currentFileContent);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    });

                } else {

                    Save.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent arg0) {

                            javafx.scene.control.SingleSelectionModel<Tab> selectionModel = editorTabPane.getSelectionModel();
                            String currentTab = selectionModel.getSelectedItem().getId();
                            File file = new File(currentTab);
                            selectionModel.getSelectedItem().setText(file.getName());
                            File fileName = new File(currentTab);
                            String currentFileContent = Content.getCodeAndSnapshot();
                            try {
                                fileOperation.saveFile(fileName, currentFileContent);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    });
                }
            }
        });













        //SaveAll Option in file menu click event

        saveAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {

                ObservableList<Tab> allTab = editorTabPane.getTabs();
                javafx.scene.control.SingleSelectionModel<Tab> selectedItemToGetBack = editorTabPane.getSelectionModel();
                Tab selectedTabBeforeSaveAll = selectedItemToGetBack.getSelectedItem();
                for (Tab tab : allTab) {
                    javafx.scene.control.SingleSelectionModel<Tab> selectedItem = editorTabPane.getSelectionModel();

                    selectedItem.select(tab);
                    String currentTab = tab.getId();
                    String fname = "";
                    String ext = "";
                    int mid = currentTab.lastIndexOf(".");
                    fname = currentTab.substring(0, mid);
                    ext = currentTab.substring(mid + 1, currentTab.length());
                    final File fileName;
                    String checkModifier = selectedItem.getSelectedItem().getText();
                    File exactPath = new File(currentTab);
                    String properName = exactPath.getName();
                    if (!checkModifier.equals(properName)) {

                        if ("params".equals(ext) || "topo".equals(ext)) {
                            javafx.scene.control.SingleSelectionModel<Tab> selectedItem1 = editorTabPane.getSelectionModel();
                            String currentTab1 = tab.getId();
                            File file = new File(currentTab1);

                            File fileName1 = new File(currentTab1);
                            selectedItem1.getSelectedItem().setText(fileName1.getName());
                            String currentFileContent = ((CodeEditorParams) tab.getContent()).getCodeAndSnapshot();
                            try {
                                fileOperation.saveFile(fileName1, currentFileContent);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }


                        } else {
                            javafx.scene.control.SingleSelectionModel<Tab> selectedItem1 = editorTabPane.getSelectionModel();
                            String currentTab2 = tab.getId();

                            File fileName2 = new File(currentTab2);
                            selectedItem1.getSelectedItem().setText(fileName2.getName());
                            String currentFileContent = ((CodeEditor) tab.getContent()).getCodeAndSnapshot();
                            try {
                                fileOperation.saveFile(fileName2, currentFileContent);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }

                    }
                }
                selectedItemToGetBack.select(selectedTabBeforeSaveAll);
            }
        });

        editorTab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event arg0) {
                javafx.scene.control.SingleSelectionModel<Tab> selectedItem1 = editorTabPane.getSelectionModel();
                String fileName = editorTab.getId();
                String tabName = editorTab.getText();
                File tabIdentity = new File(fileName);
                String properName = tabIdentity.getName();
                if (!properName.equals(tabName)) {
                    int optionPressed = JOptionPane.showConfirmDialog(null, "Would you like to save the content of this tab\n");
                    if (optionPressed == 0) {
                        String extension = fileOperation.getExtension(editorTab.getText());
                        if ("params".equals(extension) || "topo".equals(extension)) {
                            try {
                                String tabContent = ((CodeEditorParams) editorTab.getContent()).getCodeAndSnapshot();
                                fileOperation.saveFile(tabIdentity, tabContent);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            try {
                                String tabContent1 = ((CodeEditor) editorTab.getContent()).getCodeAndSnapshot();
                                fileOperation.saveFile(tabIdentity, tabContent1);
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(TAI_TestON.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }



                    }
                }

                if (editorTabPane.getTabs().size() == 0) {
                    run.setDisable(true);
                    runTestMenu.setDisable(true);


                    saveAll.setDisable(true);
                    advanceMode.setDisable(true);
                }
                if (editorTabPane.getTabs().size() < 2) {
                    saveAll.setDisable(true);
                }

            }
        });


        closeAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.NONE);
                dialogStage.setScene(new Scene(VBoxBuilder.create().
                        children(new Text("Yes"), new Button("No")).
                        alignment(Pos.CENTER).padding(new Insets(5)).build()));
                dialogStage.show();


            }
        });

    }

    public void openFile(String extension, String extension2) {
        File selected;
        String fileContent = "";
        openParams = new FileChooser();
        if ("".equals(extension2)) {
            openParams.getExtensionFilters().add(new ExtensionFilter("Display  only (*." + extension + ") files", "*." + extension));
        } else {
            openParams.getExtensionFilters().add(new ExtensionFilter("Display  only (*." + extension + ")" + "files", "*." + extension));
            openParams.getExtensionFilters().add(new ExtensionFilter("Display  only (*." + extension2 + ") files", "*." + extension2));
        }
        selected = openParams.showOpenDialog(contextMenu);
        try {
            fileContent = fileOperation.getContents(selected);
            checkEditor();
            String filename = selected.getName();
            checkEditor();
            OFATabEditor(new CodeEditor(fileContent), new CodeEditorParams(fileContent), selected.getAbsolutePath(), "");
        } catch (Exception e) {
        }

        editorTabPane.prefWidthProperty().bind(scene.widthProperty().subtract(200));
    }

    public void checkEditor() {
        if (projectExplorerFlag == false && editorFlag == false) {
            editorFlag = true;
            editorTabPane.prefHeightProperty().bind(scene.heightProperty().subtract(40));
            editorTabPane.setLayoutX(explorerTabPane.getLayoutX());
            editorTabPane.setMaxWidth(OFAContainerWidth);
        } else if (projectExplorerFlag == true && editorFlag == false) {
            editorFlag = true;
            editorTabPane.setLayoutX(explorerTabPane.getLayoutX() + explorerTabPane.getWidth());
            editorTabPane.prefHeightProperty().bind(scene.heightProperty().subtract(65));
            editorTabPane.prefWidthProperty().bind(scene.widthProperty().subtract(250));

        } else if (editorFlag == true && projectExplorerFlag == false) {
            editorTabPane.setLayoutX(explorerTabPane.getLayoutX());
            editorTabPane.prefHeightProperty().bind(scene.heightProperty().subtract(65));
            editorTabPane.prefWidthProperty().bind(scene.widthProperty());
        } else if (editorFlag == true && projectExplorerFlag == true) {
            editorTabPane.prefHeightProperty().bind(scene.heightProperty().subtract(65));
            editorTabPane.prefWidthProperty().bind(scene.widthProperty().subtract(explorerTabPane.getWidth()));
            editorTabPane.setLayoutX(explorerTabPane.getLayoutX() + explorerTabPane.getWidth());
        }

    }

    public void refreshLogExplorer() {


        String projectWorkSpacePaths = label.hierarchyTestON + "/logs/";
        File[] file = File.listRoots();
        Path name = new File(projectWorkSpacePaths).toPath();
        TAILoadTree treeNode = new TAILoadTree(name);
        treeNode.setExpanded(true);


    }

    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
