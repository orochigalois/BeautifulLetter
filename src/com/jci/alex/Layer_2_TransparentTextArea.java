package com.jci.alex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class Layer_2_TransparentTextArea extends JTextArea
{
	
	public Layer_2_TransparentTextArea()
	{
		
		Font font;
		try
		{
			InputStream istream = getClass().getResourceAsStream("/com/jci/alex/res/font/Alex.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, istream);
			font = font.deriveFont(Font.BOLD, 22f);
			
			this.setFont(font);
		} catch (FontFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setOpaque(false);
		setLineWrap(true);
		setWrapStyleWord(true);
		//setSize(700, 400);
		// this.setDocument
		// (new JTextFieldLimit(this));
		this.addKeyListener(new KeyListener()
		{
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				
			}
			
			@Override
			public void keyReleased(KeyEvent e)
			{
				
//				JTextArea t = (JTextArea) e.getSource();
//				try
//				{
//					/*1700 is the maximum words' number when font size is set to 22f*/
//					if (t.getDocument().getLength() > 1700)
//						t.setText(t.getText().substring(0, 1700));
//					
//					int lastPosition = t.getCaretPosition();
//					
//					Rectangle rectangle = t.modelToView(t.getDocument()
//							.getLength());
//					
//					while (rectangle.y + rectangle.height > 1040)
//					{
//						t.setText(t.getText().substring(0, t.getText().length() - 1));
//						rectangle = t.modelToView(t.getCaretPosition());
//					}
//					if (lastPosition >= t.getDocument().getLength())
//						t.setCaretPosition(t.getDocument().getLength());
//					else
//						t.setCaretPosition(lastPosition);
//				} catch (BadLocationException e1)
//				{
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				
			}
			
			@Override
			public void keyTyped(KeyEvent e)
			{
				
			}
			
		});
		
		
//		setBorder(new CompoundBorder(new EmptyBorder(50, 10, 10, 10),
//				new LineBorder(Color.blue, 1)));
	}
	
	
	@Override
	protected void paintComponent(Graphics g)
	{
//		g.setColor(new Color(255, 255, 3, 30));
//		Insets insets = getInsets();
//		int x = insets.left;
//		int y = insets.top;
//		int width = getWidth() - (insets.left + insets.right);
//		int height = getHeight() - (insets.top + insets.bottom);
//		g.fillRect(x, y, width, height);
		super.paintComponent(g);
	}
	
}
