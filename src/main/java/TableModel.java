import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.TableModelListener;
import java.util.ArrayList;

public class TableModel implements javax.swing.table.TableModel {
    public static final String TABLE_MODEL_KEY = "CodeNotes.TableModel";
    public static final char LINE_SEPARATOR = 0x1e;
    public static final char FIELD_SEPARATOR = 0x1f;
    private static final ArrayList<ClickableListItem> listItems = new ArrayList<>();

    private static TableModel instance;
    public static TableModel Instance() {
        if (instance == null) {
            instance = new TableModel();
        }
        return instance;
    }

    @Override
    public int getRowCount() {
        return listItems.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Nls
    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0)
            return "Path";
        if (columnIndex == 1)
            return "Note";
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var action = listItems.get(rowIndex);
        if (columnIndex == 0) {
            return action.getPath();
        }
        if (columnIndex == 1) {
            return action.getNote();
        }
        return listItems.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }

    public void AddItem(ClickableListItem item, Project project) {
        listItems.add(item);
        PropertiesComponent.getInstance(project).setValue(TABLE_MODEL_KEY, Serialise());
    }

    public ClickableListItem GetListItem(int rowIndex) {
        return listItems.get(rowIndex);
    }

    public void RemoveItem(int rowIndex, Project project) {
        listItems.remove(rowIndex);
        PropertiesComponent.getInstance(project).setValue(TABLE_MODEL_KEY, Serialise());
    }

    public void EditItemNote(int rowIndex, String text, Project project) {
        listItems.get(rowIndex).setNote(text);
        PropertiesComponent.getInstance(project).setValue(TABLE_MODEL_KEY, Serialise());
    }

    public String Serialise() {
        StringBuilder sb = new StringBuilder();
        for (ClickableListItem listItem : listItems) {
            sb.append(String.format("%s%c%d%c%d%c%s%c",
                    listItem.FilePath, FIELD_SEPARATOR,
                    listItem.CursorLine, FIELD_SEPARATOR,
                    listItem.CursorColumn, FIELD_SEPARATOR,
                    listItem.Note, LINE_SEPARATOR
            ));
        }
        return sb.toString();
    }

    public void Deserialise(String input) {
        listItems.clear();
        var li = input.split(Character.toString(LINE_SEPARATOR));
        for (String listItem : li) {
            var parts = listItem.split(Character.toString(FIELD_SEPARATOR), -1);
            var clickableListItem = new ClickableListItem(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]);
            listItems.add(clickableListItem);
        }
    }
}
