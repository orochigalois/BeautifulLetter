package com.jci.alex;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JViewport;

public class Layer_0_BackgroundPaper extends JPanel
{
	
	private BufferedImage background;
	public int currentPaper;
	
	public Layer_0_BackgroundPaper()
	{
		
		try
		{
			background = ImageIO.read(this.getClass().getResource("/com/jci/alex/res/img/paper1.png"));
			currentPaper=1;
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void updateBg(int i)
	{
		try
		{
			background = ImageIO.read(this.getClass().getResource("/com/jci/alex/res/img/paper"+i+".png"));
			currentPaper=i;
			repaint();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public Dimension getPreferredSize()
	{
		return background == null ? super.getPreferredSize()
				: new Dimension(background.getWidth(),
						background.getHeight());
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (background != null)
		{
			int x = (getWidth() - background.getWidth()) / 2;
			int y = (getHeight() - background.getHeight()) / 2;
			g.drawImage(background, x, y, this);
		}
	}
	
}