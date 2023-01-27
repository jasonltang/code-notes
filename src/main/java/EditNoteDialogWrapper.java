import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

public class EditNoteDialogWrapper extends DialogWrapper {
    public JBTextField InputField = new JBTextField(1);
    public EditNoteDialogWrapper() {
        super(true); // use current window as parent
        setTitle("Enter Note");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JBPanel dialogPanel = new JBPanel(new BorderLayout());
        dialogPanel.add(InputField);
        return dialogPanel;
    }

    @Override
    public @org.jetbrains.annotations.Nullable JComponent getPreferredFocusedComponent() {
        return InputField;
    }
}
