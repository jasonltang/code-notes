import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class EditNoteAction extends AnAction {
    public int Index;
    public EditNoteAction(int index) {
        super("Edit Note...");
        Index = index;
    }
    public void actionPerformed(@NotNull AnActionEvent e) {
        var wrapper = new EditNoteDialogWrapper();
        if (wrapper.showAndGet()) {
            TableModel.Instance().EditItemNote(Index, wrapper.InputField.getText(), e.getProject());
        }
    }
}
