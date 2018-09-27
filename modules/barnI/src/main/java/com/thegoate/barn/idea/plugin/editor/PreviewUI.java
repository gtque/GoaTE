package com.thegoate.barn.idea.plugin.editor;

import com.intellij.ide.DeleteProvider;
import com.intellij.ide.PsiActionSupportFactory;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.ui.ScrollPaneFactory;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Eric Angeli on 9/26/2018.
 */
public class PreviewUI extends JPanel {
    BarnPreview editor;
    private final DeleteProvider deleteProvider;
    private final JPanel contentPanel;
    JTextArea preview;

    PreviewUI(BarnPreview editor) {
        this.editor = editor;
        final PsiActionSupportFactory factory = PsiActionSupportFactory.getInstance();

        deleteProvider = factory.createPsiBasedDeleteProvider();

        // Create layout
        preview = new JTextArea(editor.contents());

        JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(preview);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Construct UI
        setLayout(new BorderLayout());

        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(scrollPane, "preview");

        add(contentPanel, BorderLayout.CENTER);

    }

    private void updateInfo() {
    }

    JComponent getContentComponent() {
        return contentPanel;
    }

    void dispose() {
        removeAll();
    }

    private class DocumentChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            String value = editor.contents();
            preview.setText(value);
            CardLayout layout = (CardLayout) contentPanel.getLayout();
            layout.show(contentPanel, "preview");

            updateInfo();

            revalidate();
            repaint();
        }
    }


    @Nullable
    public Object getData(String dataId) {

        /**
        if (PlatformDataKeys.PROJECT.is(dataId)) {
            return editor.getProject();
        } else if (PlatformDataKeys.VIRTUAL_FILE.is(dataId)) {
            return editor.getFile();
        } else if (PlatformDataKeys.VIRTUAL_FILE_ARRAY.is(dataId)) {
            return new VirtualFile[]{editor.getFile()};
        } else if (LangDataKeys.PSI_FILE.is(dataId)) {
            return getData(LangDataKeys.PSI_ELEMENT.getName());
        } else if (LangDataKeys.PSI_ELEMENT.is(dataId)) {
            VirtualFile file = editor.getFile();
            return file != null && file.isValid() ? PsiManager.getInstance(editor.getProject()).findFile(file) : null;
        } else if (LangDataKeys.PSI_ELEMENT_ARRAY.is(dataId)) {
            return new PsiElement[]{(PsiElement) getData(LangDataKeys.PSI_ELEMENT.getName())};
        } else if (PlatformDataKeys.COPY_PROVIDER.is(dataId)) {
            return copyPasteSupport.getCopyProvider();
        } else if (PlatformDataKeys.CUT_PROVIDER.is(dataId)) {
            return copyPasteSupport.getCutProvider();
        } else if (PlatformDataKeys.DELETE_ELEMENT_PROVIDER.is(dataId)) {
            return deleteProvider;
        } else if (ImageComponentDecorator.DATA_KEY.is(dataId)) {
            return editor;
        }
        /**/
        return null;
    }
}
