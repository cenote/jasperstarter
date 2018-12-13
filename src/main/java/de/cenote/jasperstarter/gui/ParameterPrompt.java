/*
 * Copyright 2013 Cenote GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cenote.jasperstarter.gui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.sf.jasperreports.engine.JRParameter;

/**
 * <p>ParameterPrompt class.</p>
 *
 * @author Volker Voßkämper
 * @version $Revision$
 */
public class ParameterPrompt {

    Component parent;
    JRParameter[] jrParameters;
    Map params;
    JScrollPane scrollPane;
    String reportName;
    AtomicBoolean valid = new AtomicBoolean();

    /**
     * <p>Constructor for ParameterPrompt.</p>
     *
     * @param parent a {@link java.awt.Component} object.
     * @param jrParameters an array of {@link net.sf.jasperreports.engine.JRParameter} objects.
     * @param params a {@link java.util.Map} object.
     * @param reportName a {@link java.lang.String} object.
     * @param isForPromptingOnly a boolean.
     * @param isUserDefinedOnly a boolean.
     * @param emptyOnly a boolean.
     */
    public ParameterPrompt(Component parent, JRParameter[] jrParameters,
            Map<String, Object> params, String reportName, boolean isForPromptingOnly,
            boolean isUserDefinedOnly, boolean emptyOnly) {

        this.valid.set(true);
        this.parent = parent;
        this.jrParameters = jrParameters;
        this.params = params;
        this.reportName = reportName;

        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(600, 250));

        for (JRParameter param : jrParameters) {
            if (!param.isSystemDefined() || !isUserDefinedOnly) {
                if (param.isForPrompting() || !isForPromptingOnly) {
                    if (params.get(param.getName()) == null || !emptyOnly) {
                        panel.add(new ParameterPanel(param, params, this.valid));
                    }
                }
            }
        }
        panel.add(new javax.swing.JSeparator());

        // let the focus scroll the scrollPane
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                addPropertyChangeListener(
                "focusOwner", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!(evt.getNewValue() instanceof JComponent)) {
                    return;
                }
                JComponent focused = (JComponent) evt.getNewValue();
                if (panel.isAncestorOf(focused)) {
                    JComponent myComponent = (JComponent) focused.getParent().getParent();
                    myComponent.scrollRectToVisible(new Rectangle(0, 0, 0, 80));
                }
            }
        });
    }

    /**
     * <p>show.</p>
     *
     * @return a int.
     */
    public int show() {

        final JOptionPane optionPane = new JOptionPane(scrollPane,
                JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        final JDialog dialog;
        JFrame frame = new JFrame();
        if (parent == null) {
            // use a dummy frame to have it on taskbar
            frame.setTitle("JasperStarter - Parameter Prompt: " + reportName);
            frame.setUndecorated(true);  // (invisible)
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            dialog = new JDialog(frame);
        } else if (parent instanceof Window) {
            dialog = new JDialog((Window) parent);
        } else if (parent instanceof Frame) {
            dialog = new JDialog((Frame) parent);
        } else if (parent instanceof Dialog) {
            dialog = new JDialog((Dialog) parent);
        } else {
            dialog = new JDialog();
        }
        dialog.setTitle("JasperStarter - Parameter Prompt: " + reportName);
        dialog.setContentPane(optionPane);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(
                JDialog.DO_NOTHING_ON_CLOSE);
        // set the size to have the dialog properly centered
        dialog.setSize(636, 344);
        dialog.setLocationRelativeTo(parent);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                // trigger a PropertyChangeEvent
                optionPane.setValue(new Integer(
                        JOptionPane.CANCEL_OPTION));
            }
        });

        optionPane.addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();
                if (dialog.isVisible()
                        && (e.getSource() == optionPane)
                        && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                    //If you were going to check something
                    //before closing the window, you'd do
                    //it here.
                    if (valid.get()
                            || ((Integer) optionPane.getValue())
                            .intValue() == JOptionPane.CANCEL_OPTION) {
                        // cancel is possible on invalid options too
                        dialog.setVisible(false);
                    } else {
                        // reset to an unused option so next click on the
                        // same button triggers PropertyChangeEvent again
                        optionPane.setValue(new Integer(JOptionPane.NO_OPTION));
                    }
                }
            }
        });

        dialog.pack();
        dialog.setVisible(true);
        int retval = ((Integer) optionPane.getValue()).intValue();
        dialog.dispose();
        frame.dispose();
        return retval;
    }
}
