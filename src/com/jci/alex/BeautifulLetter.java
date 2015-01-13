package com.jci.alex;

import javax.swing.*;
import javax.swing.SwingWorker.StateValue;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.languagetool.Language;
import org.languagetool.gui.MyLanguageToolSupport;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.Scanner;
import java.io.*;

/***
 * TODO: 1.add to github 2.right click highlight 3.search next ctrl+k shift
 * ctrl+k 4.sync to gmail 5.mac os
 * 6.exit save
 * 7.dnd
 * 8.extra paper/font
 * 9.on top cover setting windows
 * 10.export png should be 1 page,but actually is 2page
 * @author X
 * 
 */
public class BeautifulLetter extends JFrame implements DropTargetListener
{
	private Layer_2_TransparentTextArea textArea = new Layer_2_TransparentTextArea();
	
	
	/*Spell Check*/
	MyLanguageToolSupport lt = new MyLanguageToolSupport(this, textArea);
	JMenuItem spellCheckItem = new JMenuItem("Spell Check: Off");
	int ltCurrentMode = 0;
	
	private Layer_0_BackgroundPaper layer0;
	final JFontChooser fontChooser = new JFontChooser();
	
	/**/
	String dirPNG;
	private TaskExportPNG taskExportPNG;
	
	String dirPDF;
	private TaskExportPDF taskExportPDF;
	
	String title = "Untitled.txt";
	String originalText = "";
	String currentFilePath = "";
	boolean isChanged = false;
	
	
	/*DnD support*/
	DropTarget dtSupport;
	
	/*Always On Top Support*/
	JMenuItem alwaysTopItem = new JMenuItem("Aways On Top: Off");
	boolean isAlwaysTop = false;
	
	public BeautifulLetter()
	{
		
		
		
		dtSupport = new DropTarget(textArea, this);
		setTitle(title + " - BeautifulLetter");
		setSize(832, 800);
		
		setResizable(false);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		layer0 = new Layer_0_BackgroundPaper();
		JScrollPane Layer0_sP = new JScrollPane(layer0,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		Layer_1_TransparentViewPort viewport = new Layer_1_TransparentViewPort();
		viewport.setView(textArea);
		JScrollPane Layer1_sP = new JScrollPane();
		
		Layer1_sP.setOpaque(false);
		Layer1_sP.setViewport(viewport);
		Layer1_sP.setBorder(new CompoundBorder(new EmptyBorder(60, 20, 10, 0),
				BorderFactory.createEmptyBorder()));
		
		textArea.getDocument().addDocumentListener(new DocumentListener()
		{
			
			@Override
			public void changedUpdate(DocumentEvent arg0)
			{
				if (!originalText.equals(textArea.getText()))
				{
					isChanged = true;
					setTitle("*" + title + " - BeautifulLetter");
				}
				else
				{
					isChanged = false;
					setTitle(title + " - BeautifulLetter");
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0)
			{
				if (!originalText.equals(textArea.getText()))
				{
					isChanged = true;
					setTitle("*" + title + " - BeautifulLetter");
				}
				else
				{
					isChanged = false;
					setTitle(title + " - BeautifulLetter");
				}
			}
			
			@Override
			public void removeUpdate(DocumentEvent arg0)
			{
				if (!originalText.equals(textArea.getText()))
				{
					isChanged = true;
					setTitle("*" + title + " - BeautifulLetter");
				}
				else
				{
					isChanged = false;
					setTitle(title + " - BeautifulLetter");
				}
			}
		});
		
		// Layer1_sP.setBorder(new CompoundBorder(new EmptyBorder(50, 10, 10,
		// 10),
		// new LineBorder(Color.blue, 1)));
		layer0.add(Layer1_sP);
		getContentPane().add(Layer0_sP);
		
		// frame.pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		/* Menu */
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem newItem = new JMenuItem("New");
		newItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				newFile();
			}
		});
		newItem.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(newItem);
		
		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				openFile();
			}
		});
		openItem.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(openItem);
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				saveFile();
			}
		});
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(saveItem);
		
		JMenuItem saveAsItem = new JMenuItem("SaveAs");
		saveAsItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				saveAsFile();
			}
		});
		saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
		fileMenu.add(saveAsItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);
		
		JMenu editMenu = new JMenu("Edit");
		
		editMenu.add(lt.undo.undoAction);
		
		editMenu.add(lt.undo.redoAction);
		
		JMenuItem findItem = new JMenuItem("Find");
		findItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				// choosePaper();
				
			}
		});
		editMenu.add(findItem);
		
		JMenu optionMenu = new JMenu("Option");
		JMenuItem fontItem = new JMenuItem("Font");
		fontItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				chooseFont();
				
			}
		});
		optionMenu.add(fontItem);
		
		JMenuItem colorItem = new JMenuItem("Color");
		colorItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				chooseColor();
				
			}
		});
		optionMenu.add(colorItem);
		
		JMenuItem paperItem = new JMenuItem("Paper");
		paperItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				choosePaper();
				
			}
		});
		optionMenu.add(paperItem);
		
		JMenu exportMenu = new JMenu("Export");
		
		JMenuItem pngItem = new JMenuItem("PNG");
		pngItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				exportPNG();
			}
		});
		exportMenu.add(pngItem);
		
		JMenuItem pdfItem = new JMenuItem("PDF");
		pdfItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				exportPDF();
			}
		});
		exportMenu.add(pdfItem);
		
		JMenu utilitiesMenu = new JMenu("Utilities");
		
		JMenuItem dictCNItem = new JMenuItem("DictCn");
		dictCNItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				openDictCn();
			}
		});
		dictCNItem.setAccelerator(KeyStroke.getKeyStroke('1', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		utilitiesMenu.add(dictCNItem);
		
		JMenuItem googleTranslateE2CItem = new JMenuItem("Eng To Chs");
		googleTranslateE2CItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				openE2C();
			}
		});
		googleTranslateE2CItem.setAccelerator(KeyStroke.getKeyStroke('2',
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		utilitiesMenu.add(googleTranslateE2CItem);
		
		JMenuItem googleTranslateC2EItem = new JMenuItem("Chs To Eng");
		googleTranslateC2EItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				openC2E();
			}
		});
		googleTranslateC2EItem.setAccelerator(KeyStroke.getKeyStroke('3',
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		utilitiesMenu.add(googleTranslateC2EItem);
		
		JMenuItem bingPicItem = new JMenuItem("BingPic");
		bingPicItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				openBingPic();
			}
		});
		bingPicItem.setAccelerator(KeyStroke.getKeyStroke('4', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		utilitiesMenu.add(bingPicItem);
		
		spellCheckItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				
				spellCheck();
				
			}
		});
		utilitiesMenu.add(spellCheckItem);
		
		alwaysTopItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				
				alwaysTop();
				
			}
		});
		alwaysTopItem.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		utilitiesMenu.add(alwaysTopItem);
		
		
		
		JMenu helpMenu = new JMenu("Help");
		
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				openAbout();
			}
		});
		helpMenu.add(aboutItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(optionMenu);
		menuBar.add(exportMenu);
		menuBar.add(utilitiesMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
		
		lt.setBackgroundCheckEnabled(false);
		lt.removeHighlights();
		lt.clearAll();
		
	}
	
	public static int yesnocancel(String theMessage)
	{
		int result = JOptionPane
				.showConfirmDialog((Component) null, theMessage,
						"BeautifulLetter", JOptionPane.YES_NO_CANCEL_OPTION);
		return result;
	}
	
	public void newFile()
	{
		if (isChanged)
		{
			int i = yesnocancel("Save to " + title + "?");
			if (i == 0)// yes
			{
				saveFile();
				beginNewFile();
			}
			else if (i == 1)// no
			{
				beginNewFile();
			}
			else if (i == 2)// cancel
			{
				
			}
			else
			{
				
			}
		}
		else
			beginNewFile();
		
	}
	public void beginNewFile()
	{
		this.textArea.setText("");
		title = "Untitled";
		originalText = "";
		currentFilePath = "";
		setTitle("Untitled - BeautifulLetter");
	}
	public void openFile()
	{
		if (isChanged)
		{
			int i = yesnocancel("Save to " + title + "?");
			if (i == 0)// yes
			{
				saveFile();
				beginOpenFile();
			}
			else if (i == 1)// no
			{
				beginOpenFile();
			}
			else if (i == 2)// cancel
			{
				
			}
			else
			{
				
			}
		}
		else
			beginOpenFile();
		
	}
	
	public void beginOpenFile()
	{
		JFileChooser open = new JFileChooser();
		
		open.setCurrentDirectory(new File("."));
		
		open.setFileFilter(new javax.swing.filechooser.FileFilter()
		{
			public boolean accept(File f)
			{
				return f.isDirectory()
						|| f.getName().toLowerCase().endsWith(".txt");
			}
			
			public String getDescription()
			{
				return "txt";
			}
		});
		
		int option = open.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION)
		{
			
			originalText = "";
			StringBuilder stringBuilder = new StringBuilder();
			
			try
			{
				
				title = open.getSelectedFile().getName();
				currentFilePath = open.getSelectedFile().getPath();
				setTitle(title + " - BeautifulLetter");
				Scanner scan = new Scanner(new FileReader(open
						.getSelectedFile().getPath()));
				while (scan.hasNext())
					
					stringBuilder.append(scan.nextLine() + "\n");
				
				originalText = stringBuilder.toString();
				
				this.textArea.setText(originalText);
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
	}
	
	public void saveFile()
	{
		try
		{
			if (currentFilePath == "")
			{
				saveAsFile();
			}
			else
			{
				BufferedWriter out = new BufferedWriter(new FileWriter(
						currentFilePath));
				out.write(this.textArea.getText());
				
				out.close();
				isChanged=false;
				setTitle(title + " - BeautifulLetter");
			}
			
			
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public void saveAsFile()
	{
		JFileChooser save = new JFileChooser();
		
		save.setFileFilter(new javax.swing.filechooser.FileFilter()
		{
			public boolean accept(File f)
			{
				return f.isDirectory()
						|| f.getName().toLowerCase().endsWith(".txt");
			}
			
			public String getDescription()
			{
				return "txt";
			}
		});
		int option = save.showSaveDialog(this);
		
		if (option == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				
				BufferedWriter out = new BufferedWriter(new FileWriter(save
						.getSelectedFile().getPath() + ".txt"));
				out.write(this.textArea.getText());
				
				out.close();
				
				currentFilePath=save
						.getSelectedFile().getPath() + ".txt";
				title = save.getSelectedFile().getName()+".txt";
				setTitle(title + " - BeautifulLetter");
				isChanged=false;
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
	}
	public void alwaysTop()
	{
		if(!isAlwaysTop)
		{
			setAlwaysOnTop( true );
			isAlwaysTop=true;
			alwaysTopItem.setText("Aways On Top: On");
		}
		else
		{
			setAlwaysOnTop( false );
			isAlwaysTop=false;
			alwaysTopItem.setText("Aways On Top: Off");
		}
	}
	public void spellCheck()
	{
		if (ltCurrentMode == 0)
		{
			ltCurrentMode = 1;
			spellCheckItem.setText("Spell Check On: USA English");
			
			lt.setLanguage(Language
					.getLanguageForLocale(new Locale("en", "US")));
			lt.setBackgroundCheckEnabled(true);
		}
		else if (ltCurrentMode == 1)
		{
			ltCurrentMode = 2;
			spellCheckItem.setText("Spell Check On: British English");
			
			lt.setLanguage(Language
					.getLanguageForLocale(new Locale("en", "GB")));
			
			lt.setBackgroundCheckEnabled(true);
		}
		else if (ltCurrentMode == 2)
		{
			ltCurrentMode = 0;
			spellCheckItem.setText("Spell Check: Off");
			
			lt.setBackgroundCheckEnabled(false);
			lt.removeHighlights();
			lt.clearAll();
		}
		
	}
	
	public void exportPDF()
	{
		JFileChooser c;
		if (null != dirPDF)
			c = new JFileChooser(dirPDF);
		else
			c = new JFileChooser(".");
		
		c.setDialogTitle("chooser location");
		c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		c.setAcceptAllFileFilterUsed(false);
		
		if (c.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			dirPDF = c.getSelectedFile().getAbsolutePath();
			callPDFTask(dirPDF);
		}
	}
	
	public void callPDFTask(String dirPDF)
	{
		
		taskExportPDF = new TaskExportPDF(textArea, layer0, dirPDF);
		final ExportPDFDialog f = new ExportPDFDialog(this, taskExportPDF);
		f.setDefaultCloseOperation(f.DO_NOTHING_ON_CLOSE);
		
		taskExportPDF.addPropertyChangeListener(new PropertyChangeListener()
		{
			@Override
			public void propertyChange(final PropertyChangeEvent event)
			{
				// System.out.println(event.getPropertyName());
				if (event.getPropertyName() == "progress")
				{
					f.progressBar.setIndeterminate(false);
					f.progressBar.setValue((Integer) event.getNewValue());
				}
				else if (event.getPropertyName() == "state")
				{
					switch ((StateValue) event.getNewValue())
					{
						case DONE:
							
							Toolkit.getDefaultToolkit().beep();
							f.setVisible(false);
							
							if (!taskExportPDF.isCancelled())
								JOptionPane.showMessageDialog(null,
										"Completed!");
							
							taskExportPDF = null;
							break;
						case STARTED:
						case PENDING:
							
							f.progressBar.setIndeterminate(true);
							break;
					}
				}
				
			}
		});
		
		taskExportPDF.execute();
		
		f.setLocationRelativeTo(this);
		f.setVisible(true);
		
	}
	
	public void exportPNG()
	{
		
		JFileChooser c;
		if (null != dirPNG)
			c = new JFileChooser(dirPNG);
		else
			c = new JFileChooser(".");
		
		c.setDialogTitle("chooser location");
		c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		c.setAcceptAllFileFilterUsed(false);
		
		if (c.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			dirPNG = c.getSelectedFile().getAbsolutePath();
			callPNGTask(dirPNG);
		}
		
	}
	
	public void callPNGTask(String dirPNG)
	{
		
		taskExportPNG = new TaskExportPNG(textArea, layer0, dirPNG);
		final ExportPNGDialog f = new ExportPNGDialog(this, taskExportPNG);
		f.setDefaultCloseOperation(f.DO_NOTHING_ON_CLOSE);
		
		taskExportPNG.addPropertyChangeListener(new PropertyChangeListener()
		{
			@Override
			public void propertyChange(final PropertyChangeEvent event)
			{
				// System.out.println(event.getPropertyName());
				if (event.getPropertyName() == "progress")
				{
					f.progressBar.setIndeterminate(false);
					f.progressBar.setValue((Integer) event.getNewValue());
				}
				else if (event.getPropertyName() == "state")
				{
					switch ((StateValue) event.getNewValue())
					{
						case DONE:
							
							Toolkit.getDefaultToolkit().beep();
							f.setVisible(false);
							
							if (!taskExportPNG.isCancelled())
								JOptionPane.showMessageDialog(null,
										"Completed!");
							
							taskExportPNG = null;
							break;
						case STARTED:
						case PENDING:
							
							f.progressBar.setIndeterminate(true);
							break;
					}
				}
				
			}
		});
		
		taskExportPNG.execute();
		
		f.setLocationRelativeTo(this);
		f.setVisible(true);
		
	}
	
	public void openDictCn()
	{
		try
		{
			String words = textArea.getSelectedText();
			if (words != null)
			{
				words = words.replaceAll(" ", "%20");
				Runtime.getRuntime().exec(new String[]
				{ "cmd", "/c", "start chrome http://dict.cn/" + words });
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openE2C()
	{
		try
		{
			String words = textArea.getSelectedText();
			if (words != null)
			{
				words = words.replaceAll(" ", "%20");
				Runtime.getRuntime().exec(
						new String[]
						{
								"cmd",
								"/c",
								"start chrome https://translate.google.com/#en/zh-CN/"
										+ words });
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void openC2E()
	{
		try
		{
			String words = textArea.getSelectedText();
			if (words != null)
			{
				words = words.replaceAll(" ", "%20");
				Runtime.getRuntime().exec(
						new String[]
						{
								"cmd",
								"/c",
								"start chrome https://translate.google.com/#zh-CN/en/"
										+ words });
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openBingPic()
	{
		try
		{
			String words = textArea.getSelectedText();
			if (words != null)
			{
				words = words.replaceAll(" ", "%20");
				Runtime.getRuntime().exec(
						new String[]
						{
								"cmd",
								"/c",
								"start chrome http://www.bing.com/images/search?q="
										+ words });
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openAbout()
	{
		JDialog f = new AboutDialog(this);
		
		f.setLocationRelativeTo(this);
		
		f.setVisible(true);
		
	}
	
	public void chooseColor()
	{
		
		Color fontColor = JColorChooser.showDialog(null, "Choose ink color",
				Color.black);
		if (fontColor != null)
		{
			textArea.setForeground(fontColor);
		}
	}
	
	public void choosePaper()
	{
		
		JDialog f = new JPaperChooser(this, layer0);
		
		f.setLocationRelativeTo(this);
		
		f.setVisible(true);
		
	}
	
	public void chooseFont()
	{
		
		int result = fontChooser.showDialog(this);
		if (result == JFontChooser.OK_OPTION)
		{
			Font font = fontChooser.getSelectedFont();
			textArea.setFont(font);
			
		}
	}
	
	public static void main(String args[]) throws FontFormatException,
			IOException
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					// These 2 Statements enable anti aliasing in arbitrary
					// Commenting these will use system anti aliasing by default
					System.setProperty("awt.useSystemAAFontSettings", "on");
					System.setProperty("swing.aatext", "true");
					
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception ex)
				{
				}
				new BeautifulLetter();
				
			}
		});
	}

	@Override
	public void dragEnter(DropTargetDragEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop(DropTargetDropEvent dtde)
	{
		try
		{
			// Ok, get the dropped object and try to figure out what it is
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++)
			{
				System.out.println("Possible flavor: "
						+ flavors[i].getMimeType());
				// Check for file lists specifically
				if (flavors[i].isFlavorJavaFileListType())
				{
					// Great! Accept copy drops...
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
//					textArea.setText("Successful file list drop.\n\n");
					
					// And add the list of file names to our text area
					java.util.List list = (java.util.List) tr
							.getTransferData(flavors[i]);
					
					if(list.size()>1)
					{
						JOptionPane.showMessageDialog(null, "Only one file can be opened at one time");
						
					}
					else if(list.size()==1)
					{
						originalText = "";
						StringBuilder stringBuilder = new StringBuilder();
						
						try
						{
							
							title = list.get(0).toString();
							currentFilePath = list.get(0).toString();
							setTitle(title + " - BeautifulLetter");
							Scanner scan = new Scanner(new FileReader(currentFilePath));
							while (scan.hasNext())
								
								stringBuilder.append(scan.nextLine() + "\n");
							
							originalText = stringBuilder.toString();
							
							this.textArea.setText(originalText);
						} catch (Exception ex)
						{
							System.out.println(ex.getMessage());
						}
					}
					else
					{
						
					}
//					for (int j = 0; j < list.size(); j++)
//					{
//						textArea.append(list.get(j) + "\n");
//					}
					
					// If we made it this far, everything worked.
					dtde.dropComplete(true);
					return;
				}
				// Ok, is it another Java object?
				else if (flavors[i].isFlavorSerializedObjectType())
				{
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					textArea.setText("Successful text drop.\n\n");
					Object o = tr.getTransferData(flavors[i]);
					textArea.append("Object: " + o);
					dtde.dropComplete(true);
					return;
				}
				// How about an input stream?
				else if (flavors[i].isRepresentationClassInputStream())
				{
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					textArea.setText("Successful text xxdrop.\n\n");
					textArea.read(new InputStreamReader((InputStream) tr
							.getTransferData(flavors[i])),
							"from system clipboard");
					dtde.dropComplete(true);
					return;
				}
			}
			// Hmm, the user must not have dropped a file list
			System.out.println("Drop failed: " + dtde);
			dtde.rejectDrop();
		} catch (Exception e)
		{
			e.printStackTrace();
			dtde.rejectDrop();
		}
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
}
