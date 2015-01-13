package com.jci.alex;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class JPaperChooser extends JDialog
{
	
	private PaperEntry papers[] =
	{ new PaperEntry("1", "p1.png"), new PaperEntry("2", "p2.png"),
			new PaperEntry("3", "p3.png"), new PaperEntry("4", "p4.png"),
			new PaperEntry("5", "p5.png"), new PaperEntry("6", "p6.png"),
			new PaperEntry("7", "p7.png"), new PaperEntry("8", "p8.png"),
			new PaperEntry("9", "p9.png"), new PaperEntry("10", "p10.png"),
			new PaperEntry("11", "p11.png"), new PaperEntry("12", "p12.png"),
			new PaperEntry("13", "p13.png")};
	
	private JList paperlist;
	
	public JPaperChooser(JFrame parent,final Layer_0_BackgroundPaper layer0)
	{
		
		super(parent, "Choose the paper", true);
		
	
		
		paperlist = new JList(papers);
		paperlist.setCellRenderer(new PaperCellRenderer());
		paperlist.setSelectedIndex(0);
		paperlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		JScrollPane sP1 = new JScrollPane(paperlist, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sP1.getVerticalScrollBar().setUnitIncrement(10);
		
		getContentPane().add(sP1, "Center");
		
		JPanel p2 = new JPanel();
		JButton ok = new JButton("Ok");
		p2.add(ok);
		getContentPane().add(p2, "South");
		
		ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				
				//layer0.updateBg(paperlist.getSelectedIndex()+1);
				setVisible(false);
			}
		});
		
		ListSelectionListener listSelectionListener = new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent listSelectionEvent)
			{
				boolean adjust = listSelectionEvent.getValueIsAdjusting();

				if (!adjust)
				{
					layer0.updateBg(paperlist.getSelectedIndex()+1);
				}
			}
		};
		paperlist.addListSelectionListener(listSelectionListener);
		
		
		
		setSize(200, 500);
	}
	
}

class PaperEntry
{
	private final String title;
	
	private final String imageName;
	
	private ImageIcon image;
	
	public PaperEntry(String title, String imageName)
	{
		this.title = title;
		this.imageName = imageName;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public ImageIcon getImage()
	{
		if (image == null)
		{
			image = new ImageIcon(this.getClass().getResource(
					"/com/jci/alex/res/img/" + imageName));
		}
		return image;
	}
	
	// Override standard toString method to give a useful result
	public String toString()
	{
		return title;
	}
}

class PaperCellRenderer extends JLabel implements ListCellRenderer
{
	private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);
	
	public PaperCellRenderer()
	{
		setOpaque(true);
		setIconTextGap(36);
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus)
	{
		PaperEntry entry = (PaperEntry) value;
		setText(entry.getTitle());
		setIcon(entry.getImage());
		if (isSelected)
		{
			setBackground(HIGHLIGHT_COLOR);
			setForeground(Color.white);
		}
		else
		{
			setBackground(Color.white);
			setForeground(Color.black);
		}
		return this;
	}
}
