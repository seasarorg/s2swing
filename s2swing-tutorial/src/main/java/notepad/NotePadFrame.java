/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package notepad;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;
import org.seasar.swing.annotation.S2Action;
import org.seasar.swing.beans.ObservableBeans;
import org.seasar.swing.binding.Binder;
import org.seasar.swing.binding.BindingStateEvent;
import org.seasar.swing.binding.BindingStateListener;
import org.seasar.swing.builder.ComponentBuilder;
import org.seasar.swing.builder.MenuBuilder;
import org.seasar.swing.component.S2Frame;

/**
 * @author kaiseh
 */

public class NotePadFrame extends S2Frame {
    private static final long serialVersionUID = 1L;

    private JMenu fileMenu = new JMenu();
    private JMenu editMenu = new JMenu();
    private JMenu viewMenu = new JMenu();
    private JMenu helpMenu = new JMenu();
    private JToolBar toolBar = new JToolBar();
    private JTextArea textArea = new JTextArea();
    private JFileChooser fileChooser = new JFileChooser();

    private File currentFile;
    private boolean modified;

    private Document document;

    public void initialize() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        setUpMenu();
        setUpComponents();
        setUpBinding();
    }

    private void setUpMenu() {
        JMenuBar menuBar = new JMenuBar();
        MenuBuilder b = new MenuBuilder(getActionMap());
        b.build(menuBar,
                b.menu(fileMenu, 
                        "newFile", "openFile", b.separator(),
                        "saveFile", "saveFileAs", b.separator(),
                        "exit"),
                b.menu(editMenu, "cut", "copy", "paste"),
                b.menu(viewMenu, b.checkBox("toggleToolBar")),
                b.menu(helpMenu, "about"));
        setJMenuBar(menuBar);
    }

    private void setUpComponents() {
        setPreferredSize(new Dimension(800, 600));
        textArea.setLineWrap(true);
        ComponentBuilder b = new ComponentBuilder(getActionMap());
        b.build(getContentPane(),
                b.component(toolBar, b.north(),
                        b.toolButton("newFile"),
                        b.toolButton("openFile"),
                        b.toolButton("saveFile"),
                        b.toolSeparator(),
                        b.toolButton("cut"),
                        b.toolButton("copy"),
                        b.toolButton("paste")),
                b.scrollPane(b.center(), textArea));
    }

    private void setUpBinding() {
        document = ObservableBeans.create(Document.class);
        Binder binder = new Binder();
        binder.add(document, "text", textArea, "text");
        binder.addBindingStateListener(new BindingStateListener() {
            public void bindingStateChanged(BindingStateEvent e) {
                setModified(true);
            }
        });
        binder.bind();
        setModified(false);
    }

    public Document getDocument() {
        return document;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
        updateTitle();
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
        updateTitle();
    }

    public String getSelectedText() {
        return textArea.getSelectedText();
    }

    public boolean isToolBarVisible() {
        return toolBar.isVisible();
    }

    public void updateTitle() {
        StringBuilder buf = new StringBuilder();
        buf.append("NotePad");
        if (currentFile != null) {
            buf.append(" - ");
            buf.append(currentFile.getName());
        }
        if (modified) {
            buf.append(" *");
        }
        setTitle(buf.toString());
    }

    public boolean confirmSave() {
        if (isModified()) {
            String message = getResourceMap().getString("confirmSave.message");
            int result = JOptionPane.showConfirmDialog(this, message,
                    "NotePad", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (result) {
            case JOptionPane.YES_OPTION:
                return trySaveFile();
            case JOptionPane.NO_OPTION:
                return true;
            case JOptionPane.CANCEL_OPTION:
                return false;
            }
        }
        return true;
    }

    @S2Action
    public void newFile() {
        if (!confirmSave()) {
            return;
        }
        document.setText("");
        setModified(false);
    }

    @S2Action(block = BlockingScope.APPLICATION)
    public Task<?, ?> openFile() {
        if (!confirmSave()) {
            return null;
        }
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return new OpenFileTask(this, fileChooser.getSelectedFile());
    }

    @S2Action(enabled = "currentFile == null || modified")
    public void saveFile() {
        trySaveFile();
    }

    private boolean trySaveFile() {
        if (currentFile == null) {
            return trySaveFileAs();
        }
        doSaveFile(currentFile);
        return true;
    }

    @S2Action
    public void saveFileAs() {
        trySaveFileAs();
    }

    private boolean trySaveFileAs() {
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return false;
        }
        doSaveFile(fileChooser.getSelectedFile());
        return true;
    }

    private void doSaveFile(File file) {
        // 実際のアプリケーションでは、この位置で document.getText() を参照して保存処理を記述する
        setCurrentFile(file);
        setModified(false);
    }

    @S2Action
    public void exit() {
        if (!confirmSave()) {
            return;
        }
        Application.getInstance().exit();
    }

    @S2Action(enabled = "selectedText != null")
    public void cut() {
        textArea.cut();
    }

    @S2Action(enabled = "selectedText != null")
    public void copy() {
        textArea.copy();
    }

    @S2Action
    public void paste() {
        textArea.paste();
    }

    @S2Action(selected = "toolBarVisible")
    public void toggleToolBar() {
        toolBar.setVisible(!toolBar.isVisible());
    }

    @S2Action
    public void about() {
        JOptionPane.showMessageDialog(this, getResourceMap().getString(
                "about.message"));
    }
}
