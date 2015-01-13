package com.jci.alex;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ExportPNGDialog extends JDialog
{
	
	public JProgressBar progressBar;
	public TaskExportPNG taskExportPNG;
	public ExportPNGDialog(JFrame parent,TaskExportPNG _taskExportPNG)
	{
		
		super(parent, "Exporting", true);
		
		taskExportPNG=_taskExportPNG;
		progressBar = new JProgressBar(0, 100);
		Box b = Box.createVerticalBox();
		b.add(Box.createGlue());
		b.add(progressBar);
		b.add(Box.createGlue());
		getContentPane().add(b, "Center");
		
		JPanel p2 = new JPanel();
		JButton cancel = new JButton("Cancel");
		p2.add(cancel);
		getContentPane().add(p2, "South");
		
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				taskExportPNG.cancel(true);
				
				setVisible(false);
			}
		});
		
		setSize(250, 150);
	}
	
}