import com.intellij.ide.DataManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class NotesWindow implements ToolWindowFactory, DumbAware {
    JBTable itemTable = new JBTable();
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        AddNoteAction.RegisterNotesWindow(this);

        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = itemTable.rowAtPoint(e.getPoint());
                if (row != -1){
                    var buttonClicked = e.getButton();
                    if (buttonClicked == MouseEvent.BUTTON1) {

                        DataContext context = DataManager.getInstance().getDataContext(itemTable);
                        AnActionEvent anActionEvent = AnActionEvent.createFromDataContext("SomePlace", null, context);
                        (TableModel.Instance().GetListItem(row)).GoToNoteAction.actionPerformed(anActionEvent);

                    }
                    else if (buttonClicked == MouseEvent.BUTTON3) {
                        var menu = ActionManager.getInstance().createActionPopupMenu("My Menu", new ActionGroup() {
                            @Override
                            public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
                                return new AnAction[]{
                                        new DeleteNoteAction(row),
                                        new EditNoteAction(row)
                                };
                            }
                        });

                        menu.getComponent().show(itemTable, e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        Content content = contentManager.getFactory().createContent(new JScrollPane(itemTable), "", false);
        contentManager.addContent(content);

        itemTable.setModel(TableModel.Instance());

        //PropertiesComponent.getInstance(project).unsetValue(TableModel.TABLE_MODEL_KEY);
        var savedSettings = PropertiesComponent.getInstance(project).getValue(TableModel.TABLE_MODEL_KEY);
        if (savedSettings != null) {
            TableModel.Instance().Deserialise(savedSettings);
        }

//        FileEditor editor = com.intellij.openapi.fileEditor.FileEditorManager.getInstance(project).getSelectedEditor();
//
//        CaretModel caretModel = ((TextEditor)editor).getEditor().getCaretModel();
//        Document document = ((TextEditor)editor).getEditor().getDocument();
//        document.addDocumentListener(new DocumentListener() {
//            @Override
//            public void documentChanged(@NotNull DocumentEvent event) {
//                DocumentListener.super.documentChanged(event); // JT: Need to set up a document changed event when deserialising as well as when adding a new event
//            }
//        });
//        caretModel.addCaretListener(new CaretListener() {
//            @Override
//            public void caretPositionChanged(@NotNull CaretEvent event) {
//                var oldPosition = event.getOldPosition();
//                var newPosition = event.getNewPosition();
//                //CaretListener.super.caretPositionChanged(event);
//            }
//        });
    }
}

