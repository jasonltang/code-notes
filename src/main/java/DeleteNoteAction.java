import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class DeleteNoteAction extends AnAction {
    public int Index;
    public DeleteNoteAction(int index) {
        super("Delete Note");
        Index = index;
    }
    public void actionPerformed(@NotNull AnActionEvent e) {
        TableModel.Instance().RemoveItem(Index, e.getProject());
    }
}
