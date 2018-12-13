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

import com.toedter.calendar.JCalendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * <p>ParameterPanel class.</p>
 *
 * @author Volker Voßkämper
 * @version $Revision$
 */
public class ParameterPanel extends JPanel {

    private final JTextField paramValue = new JTextField();
    private AtomicBoolean valid;
    private int textFieldHeight;
    UIDefaults uiDefaults = javax.swing.UIManager.getDefaults();
    private static PrintStream configSink = System.err;
    private static PrintStream debugSink = System.err;

    /**
     * <p>Constructor for ParameterPanel.</p>
     *
     * @param jrParameter a {@link net.sf.jasperreports.engine.JRParameter} object.
     * @param params a {@link java.util.Map} object.
     * @param valid a {@link java.util.concurrent.atomic.AtomicBoolean} object.
     */
    public ParameterPanel(final JRParameter jrParameter, final Map<String, Object> params,
            AtomicBoolean valid) {

        this.valid = valid;
        this.textFieldHeight = 25;
        if ("gnome".equals(System.getProperty("sun.desktop"))) {
            // the default font in Gnome needs more space
            this.textFieldHeight = 30;
        }
        this.setLayout(new BorderLayout(10, 5));
        this.setMaximumSize(new Dimension(800, 60));
        this.add(BorderLayout.NORTH, new javax.swing.JSeparator());
        boolean hasStringConstructor;
        try {
            Constructor<?> c = jrParameter.getValueClass()
                    .getConstructor(String.class);
            hasStringConstructor = true;
        } catch (NoSuchMethodException ex) {
            hasStringConstructor = false;
        } catch (SecurityException ex) {
            hasStringConstructor = false;
            Logger.getLogger(ParameterPanel.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        JLabel paramName = new JLabel(jrParameter.getName() + " :");
        paramName.setPreferredSize(new Dimension(180, 25));
        paramName.setMaximumSize(new Dimension(180, 25));
        JLabel paramType = new JLabel(jrParameter.getValueClassName());
        //paramType.setPreferredSize(new Dimension(200,25));
        paramType.setMinimumSize(new Dimension(20, 25));
        paramType.setMaximumSize(new Dimension(20, 25));

        paramValue.setPreferredSize(new Dimension(30, textFieldHeight));
        //paramValue.setMaximumSize(new Dimension(100, 25));

        JButton valueButton = new JButton("...");
        valueButton.setPreferredSize(
                new Dimension(textFieldHeight, textFieldHeight));

        JPanel valuePanel = new JPanel();
        valuePanel.setLayout(new BorderLayout(0, 0));
        valuePanel.setMaximumSize(new Dimension(30, textFieldHeight));
        valuePanel.add(BorderLayout.CENTER, paramValue);

        paramValue.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // validate on KEY Enter (like TAB)
                ((JComponent) e.getSource()).transferFocus();
            }
        });

        if (Date.class
                .equals(jrParameter.getValueClass())) {
            paramValue.setInputVerifier(
                    new DateInputVerifier(jrParameter, params));
            if (params.get(jrParameter.getName()) != null) {
                Date d = (Date) params.get(jrParameter.getName());
                paramValue.setText(DateFormat.getDateInstance().format(d));
            }
            paramValue.setToolTipText(
                    "Format: "
                    + ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM)).toPattern());
            valueButton.addActionListener(new DateActionListener());
            valuePanel.add(BorderLayout.EAST, valueButton);
        } else if (Image.class
                .equals(jrParameter.getValueClass())) {
            paramValue.setInputVerifier(
                    new ImageInputVerifier(jrParameter, params));
            if (params.get(jrParameter.getName()) != null) {
                Image image = (Image) params.get(jrParameter.getName());
                paramValue.setText(image.toString());
            }
            paramValue.setToolTipText(
                    "Relative or full path to image.");
            valueButton.addActionListener(new FileActionListener());
            valuePanel.add(BorderLayout.EAST, valueButton);
        } else if (hasStringConstructor) {
            paramValue.setInputVerifier(
                    new GenericInputVerifier(jrParameter, params));
            paramValue.setText((String) params.get(jrParameter.getName()));
        } else {
            paramValue.setText("<non supported type>");
            paramValue.setEnabled(false);
        }

        JLabel description = new JLabel();
        if (jrParameter.getDescription() != null) {
            description.setText(jrParameter.getDescription());
            description.setMaximumSize(new Dimension(180, 25));
            description.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        }
        this.add(BorderLayout.WEST, paramName);
        this.add(BorderLayout.CENTER, valuePanel);
        this.add(BorderLayout.EAST, paramType);
        this.add(BorderLayout.SOUTH, description);
    }

    private class DateInputVerifier extends ParameterInputVerifier {

        DateInputVerifier(JRParameter jrParameter, Map<String, Object> params) {
            super(jrParameter, params);
        }

        @Override
        public boolean verify(JComponent input) {
            String text = ((JTextField) input).getText();
            try {
                Object o = null;
                if (!text.equals("")) {
                    o = DateFormat.getDateInstance().parse(text);
                    ((JTextField) input).setText(DateFormat.getDateInstance()
                            .format((Date) o));
                }
                //debugSink.println("DateInputVerifier: ok");
                params.put(jrParameter.getName(), o);
                input.setBackground(uiDefaults.getColor("TextField.background"));
                valid.set(true);
                return true;
            } catch (ParseException e) {
                //debugSink.println("DateInputVerifier: exception");
                input.setBackground(Color.RED);
                ((JTextField) input).selectAll();
                Toolkit.getDefaultToolkit().beep();
                valid.set(false);
                return false;
            }
        }
    }

    private class ImageInputVerifier extends ParameterInputVerifier {

        ImageInputVerifier(JRParameter jrParameter, Map<String, Object> params) {
            super(jrParameter, params);
        }

        @Override
        public boolean verify(JComponent input) {
            String text = ((JTextField) input).getText();
            try {
                Object o = null;
                Image imageParam = (Image) params.get(jrParameter.getName());
                String imageName = "";
                if (imageParam != null) {
                    imageName = imageParam.toString();
                }
                if (!text.equals("") && !text.equals(imageName)) {

                    File imageFile = new File(text);
                    if (!imageFile.isFile()) {
                        throw new IllegalArgumentException(imageFile.getName()
                                + "is not a valid file.");
                    }
                    Image image =
                            Toolkit.getDefaultToolkit().createImage(
                            JRLoader.loadBytes(imageFile));
                    MediaTracker traker = new MediaTracker(new Panel());
                    traker.addImage(image, 0);
                    try {
                        traker.waitForID(0);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(
                                "Image tracker error: " + e.getMessage(), e);
                    }
                    o = image;


                    ((JTextField) input).setText(imageFile.getAbsolutePath());
                }
                //debugSink.println("ImageInputVerifier: OK");
                if (!text.equals(imageName)) {
                    // don't overwrite a given image
                    params.put(jrParameter.getName(), o);
                }
                input.setBackground(uiDefaults.getColor("TextField.background"));
                valid.set(true);
                return true;
            } catch (Exception e) {
                //debugSink.println("ImageInputVerifier: exception");
                input.setBackground(Color.RED);
                ((JTextField) input).selectAll();
                Toolkit.getDefaultToolkit().beep();
                valid.set(false);
                return false;
            }
        }
    }

    private class GenericInputVerifier extends ParameterInputVerifier {

        GenericInputVerifier(JRParameter jrParameter, Map<String, Object> params) {
            super(jrParameter, params);
        }

        @Override
        public boolean verify(JComponent input) {
            String text = ((JTextField) input).getText();
            try {
                Object o = null;
                if (!text.equals("")) {
                    o = jrParameter.getValueClass().getConstructor(String.class)
                            .newInstance(text);
                    ((JTextField) input).setText(o.toString());
                }
                //debugSink.println("GenericInputVerifier: ok");
                params.put(jrParameter.getName(), o);
                input.setBackground(uiDefaults.getColor("TextField.background"));
                valid.set(true);
                return true;
            } catch (Exception e) {
                //debugSink.println("GenericInputVerifier: exception");
                input.setBackground(Color.RED);
                ((JTextField) input).selectAll();
                Toolkit.getDefaultToolkit().beep();
                valid.set(false);
                return false;
            }
        }
    }

    private abstract class ParameterInputVerifier extends InputVerifier {

        JRParameter jrParameter;
        Map<String, Object> params;

        ParameterInputVerifier(JRParameter jrParameter, Map<String, Object> params) {
            this.jrParameter = jrParameter;
            this.params = params;
        }

        @Override
        public abstract boolean verify(JComponent input);
    }

    private class FileActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            final JFileChooser fc = new JFileChooser();
            File file;
            file = new File(paramValue.getText());
            if (file.isFile()) {
                fc.setSelectedFile(file);
            }
            int returnVal = fc.showOpenDialog((Component) e.getSource());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                paramValue.setText(file.getAbsolutePath());
                // trigger a InputVerifier.verify() and move to next component
                // (like KEY Enter or TAB)
                paramValue.requestFocusInWindow();
                ((JComponent) e.getSource()).requestFocusInWindow();
                ((JComponent) e.getSource()).transferFocus();
            }
        }
    }

    private class DateActionListener implements ActionListener {

        JLabel selectedDate;

        @Override
        public void actionPerformed(ActionEvent e) {

            final JCalendar cal;
            if (!paramValue.getText().equals("")) {
                Date date;
                try {
                    date = DateFormat.getDateInstance().parse(paramValue.getText());

                } catch (ParseException ex) {
                    date = new Date();
                }
                cal = new JCalendar(date);
            } else {
                cal = new JCalendar();
            }

            cal.setDecorationBackgroundColor(Color.LIGHT_GRAY);
            cal.setBorder(BorderFactory.createEtchedBorder());

            JPanel calpanel = new JPanel(new BorderLayout(0, 5));

            JPanel buttonpanel = new JPanel(new BorderLayout(0, 0));
            buttonpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            JButton prev = new JButton("<");
            JButton next = new JButton(">");
            JButton today = new JButton(
                    DateFormat.getDateInstance(
                    DateFormat.MEDIUM).format(new Date()));

            prev.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //cal.getMonthChooser().getMonth();
                    if (cal.getMonthChooser().getMonth() == 0) {
                        cal.getYearChooser().setYear(cal.getYearChooser().getYear() - 1);
                        cal.getMonthChooser().setMonth(11);
                    } else {
                        cal.getMonthChooser().setMonth(cal.getMonthChooser().getMonth() - 1);
                    }
                }
            });

            next.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (cal.getMonthChooser().getMonth() == 11) {
                        cal.getYearChooser().setYear(cal.getYearChooser().getYear() + 1);
                        cal.getMonthChooser().setMonth(0);
                    } else {
                        cal.getMonthChooser().setMonth(cal.getMonthChooser().getMonth() + 1);
                    }
                }
            });

            today.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cal.setDate(new Date());
                }
            });

            selectedDate = new JLabel();
            selectedDate.setHorizontalAlignment(SwingConstants.CENTER);
            selectedDate.setFont(new Font("sansserif", Font.BOLD, 16));

            //cal.getDayChooser().addPropertyChangeListener();
            cal.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    selectedDate.setText(DateFormat.getDateInstance(
                            DateFormat.MEDIUM).format(cal.getDate()));
                }
            });

            buttonpanel.add(BorderLayout.WEST, prev);
            buttonpanel.add(BorderLayout.CENTER, today);
            buttonpanel.add(BorderLayout.EAST, next);

            calpanel.add(BorderLayout.NORTH, selectedDate);
            calpanel.add(BorderLayout.CENTER, cal);
            calpanel.add(BorderLayout.SOUTH, buttonpanel);

            int retval =
                    JOptionPane.showConfirmDialog((Component) e.getSource(), calpanel,
                    "JasperStarter - Date Picker ",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (retval == JOptionPane.OK_OPTION) {
                paramValue.setText(DateFormat.getDateInstance().format(cal.getDate()));
                // trigger a InputVerifier.verify() and move to next component
                // (like KEY Enter or TAB)
                paramValue.requestFocusInWindow();
                ((JComponent) e.getSource()).requestFocusInWindow();
                ((JComponent) e.getSource()).transferFocus();
            }
        }
    }
}
