package com.jmonkey.office.jwp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.jmonkey.export.Format;
import com.jmonkey.export.Runtime;

/**
 * A dialog that shows the chosen colours in a table.
 */
public final class ColourPropertySheet extends JDialog {
	private static final long serialVersionUID = -4297825448329394101L;

	private JWP m_app;
	private Properties m_props = null;
	private boolean m_allowAdd = false;
	private PairTableModel m_model = null;

	/**
	 * Create a new ColourPropertySheet dialog.
	 * 
	 * @param app
	 *            the current JWP application.
	 * @param p
	 *            the Properties object containing the colours.
	 * @param allowAdd
	 *            true if colours can be added.
	 */
	public ColourPropertySheet(JWP app, Properties p, boolean allowAdd) {
		super(app);
		m_app = app;
		m_props = p;
		m_allowAdd = allowAdd;
		init();
		pack();
		setLocationRelativeTo(app);
		setVisible(true);
	}

	private JWP getMain() {
		return m_app;
	}

	protected final Properties getProperties() {
		return m_props;
	}

	private void doExit() {
		dispose();
	}

	private void init() {
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		JPanel spacerPanel = new JPanel();
		spacerPanel.setLayout(new GridLayout());
		if (m_allowAdd) {
			JButton addButton = new JButton("Add Colour");
			addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String inputValue = JOptionPane
							.showInputDialog("What is the name of the\ncolour you want to add?");
					if (inputValue != null) {
						if (inputValue.trim().length() > 0) {
							try {
								m_props.setProperty(inputValue, Format.colorToHex(JColorChooser
										.showDialog(getMain(), "Choose a colour...", null)));
							} catch (Throwable t) {
								// the colour chooser was most likely
								// canceled, so ignore the exception.
							}
							// redraw the table
							if (m_model != null) {
								m_model.fireTableDataChanged();
							}
						}
					}
				}
			});
			spacerPanel.add(addButton);
		}

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doExit();
			}
		});
		spacerPanel.add(closeButton);
		buttonPanel.add(spacerPanel, BorderLayout.EAST);

		content.add(buttonPanel, BorderLayout.SOUTH);

		m_model = new PairTableModel();
		JTable tbl = new JTable(m_model);
		content.add(new JScrollPane(tbl), BorderLayout.CENTER);

		tbl.getColumnModel().getColumn(1).setPreferredWidth(5);
		tbl.getColumnModel().getColumn(1).setCellRenderer(new ColourCellRenderer());

		setContentPane(content);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				doExit();
			}
		});
	}

	/**
	 * Renders colours in the colour properties table. Sets the background to
	 * the colour and picks a contrasting colour for the foregroud.
	 */
	private final class ColourCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -3775028200991338713L;

		private final Color defaultBackground = getBackground();

		private final Color defaultForeground = getForeground();

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			setValue(value);

			if (!isSelected & column == 1) {
				try {
					Color c = Format.hexToColor((String) value);
					setBackground(c);
					setForeground(Runtime.getContrastingTextColor(c));
				} catch (Throwable t) {
					// Ignore this, its just a bad colour.
					setBackground(Color.black);
					setForeground(Color.white);
					setValue("#000000");
				}
			} else {
				setBackground(defaultBackground);
				setForeground(defaultForeground);
			}
			return this;
		}
	}

	/**
	 * The model for the colour properties table.
	 */
	private final class PairTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -6732752958615518115L;

		public PairTableModel() {
			super();
		}

		@Override
		public int getRowCount() {
			return getProperties().size();
		}

		@Override
		public int getColumnCount() {
			// Two columns
			return 2;
		}

		@Override
		public String getColumnName(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return "Colour Name";
			case 1:
				return "RGB Hex";
			default:
				return null;
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			// All columns are strings
			switch (columnIndex) {
			case 0:
				return java.lang.String.class;
			case 1:
				return java.lang.String.class;
			default:
				return java.lang.String.class;
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			// Only column 1 is editable
			switch (columnIndex) {
			case 0:
				return false;
			case 1:
				return true;
			default:
				return false;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return getProperties().keySet().toArray()[rowIndex].toString();
			case 1:
				return getProperties().getProperty(
						getProperties().keySet().toArray()[rowIndex].toString());
			default:
				return "";
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				// Column 0 is not editable
				// getProperties().keySet().toArray()[rowIndex] =
				// aValue.toString();
				break;
			case 1:
				getProperties().setProperty(
						getProperties().keySet().toArray()[rowIndex].toString(), aValue.toString());
				break;
			}
		}
	}
}
