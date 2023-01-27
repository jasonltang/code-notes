import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class AddNoteAction extends AnAction {
    private static NotesWindow notesWindow = new NotesWindow();
    public static void RegisterNotesWindow(NotesWindow nw) {
        notesWindow = nw;
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();

        // JT: Maybe this? caretModel.moveToOffset

        LogicalPosition caretPosition = caretModel.getLogicalPosition();
        int line = caretPosition.line;
        int column = caretPosition.column;

        VirtualFile currentFile = e.getDataContext().getData(PlatformDataKeys.VIRTUAL_FILE);
        var model = TableModel.Instance();
        var wrapper = new AddNoteDialogWrapper();
        if (wrapper.showAndGet()) {
            model.AddItem(
                    new ClickableListItem(currentFile.getPath(), line, column, wrapper.InputField.getText()),
                    e.getProject()
            );
        }
    }
}
