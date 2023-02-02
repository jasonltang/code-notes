import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.ScrollingModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GoToNoteAction extends AnAction {
    String FilePath;
    int CursorLine;
    int CursorColumn; // JT: Remove column
    public GoToNoteAction(String path, int cursorLocation, int cursorColumn) {
        FilePath = path;
        CursorLine = cursorLocation;
        CursorColumn = cursorColumn;
    }
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project currentProject = e.getProject();
        Path myPath = Paths.get(FilePath);
        VirtualFile myFile = VirtualFileManager.getInstance().findFileByNioPath(myPath);
        OpenFileDescriptor fd = new OpenFileDescriptor(currentProject, myFile);
        fd.navigate(true);

        CaretModel cm = FileEditorManager.getInstance(currentProject).getSelectedTextEditor().getCaretModel();
        cm.moveToLogicalPosition(new LogicalPosition(CursorLine, CursorColumn));

        ScrollingModel scrollingModel = FileEditorManager.getInstance(currentProject).getSelectedTextEditor().getScrollingModel();
        scrollingModel.scrollToCaret(ScrollType.CENTER_UP);
    }
}
