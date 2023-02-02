import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NotNull;

public class ClickableListItem {
    String FilePath;
    int CursorLine;
    int CursorColumn;
    String Note;
    GoToNoteAction GoToNoteAction;

    public ClickableListItem(String path, int cursorLocation, int cursorColumn, String note) {
        FilePath = path;
        CursorLine = cursorLocation;
        CursorColumn = cursorColumn;
        Note = note;
        GoToNoteAction = new GoToNoteAction(path, cursorLocation, cursorColumn);

        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        FileEditor fileEditor = com.intellij.openapi.fileEditor.FileEditorManager.getInstance(project).getSelectedEditor();
        Editor editor = ((TextEditor)fileEditor).getEditor();
        Document document = editor.getDocument();

        document.addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) { // JT: Why this block works in debug mode but not run mode
                var oldOffset = event.getOffset();
                var oldLogicalPosition = editor.offsetToLogicalPosition(oldOffset);
                if (oldLogicalPosition.line > CursorLine) {
                    return;
                }
                var oldFragment = event.getOldFragment();
                var newFragment = event.getNewFragment();
                var oldNewLines = oldFragment.chars().filter(c -> c == '\n').count();
                var newNewLines = newFragment.chars().filter(c -> c == '\n').count();
                var newLinesAdded = newNewLines - oldNewLines;
                CursorLine += newLinesAdded; // JT: Add check if cursor is above bookmark
                GoToNoteAction.CursorLine += newLinesAdded;
                System.out.println("Cursor line changed by " + newLinesAdded);
            }
        });
    }

    public String getPath() {
        return String.format("%s, Line %d, Column %d", FilePath, CursorLine+1, CursorColumn+1);
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String text) {
        Note = text;
    }
}
