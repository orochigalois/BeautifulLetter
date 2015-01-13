package com.jci.alex;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class Layer_1_TransparentViewPort extends JViewport
{

	public Layer_1_TransparentViewPort()
	{
		setOpaque(false);
	}
	public Dimension getPreferredSize()
	{
		return new Dimension(660,
				985);
	}
}
