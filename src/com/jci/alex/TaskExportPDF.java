package com.jci.alex;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public class TaskExportPDF extends SwingWorker<Void, Void>
{

	Layer_2_TransparentTextArea textArea;
	Layer_0_BackgroundPaper layer0;
	String filePath;
	public TaskExportPDF(Layer_2_TransparentTextArea _textArea,Layer_0_BackgroundPaper _layer0,String _filePath)
	{
		this.textArea=_textArea;
		this.layer0=_layer0;
		this.filePath=_filePath;
	}
	@Override
	protected Void doInBackground() throws Exception
	{
		makePDF(filePath);
		return null;
	}
	
	public void makePDF(String filePath)
	{
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_OVER,
				1.0F);
		
		BufferedImage imgText = new BufferedImage(textArea.getWidth(),
				textArea.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2dText = imgText.createGraphics();
		g2dText.setComposite(ac);
		
		
//		g2dText.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
                
                
		textArea.printAll(g2dText);
		
		int pageHeight=0;
		int lineHeight=textArea.getFontMetrics(textArea.getFont()).getHeight();
	
		while(pageHeight<=950)
		{
			pageHeight=pageHeight+lineHeight;
		}
		
		int h = textArea.getHeight();
		int total=h;
		int i = 0;
		while (h > 0&&!isCancelled())
		{
			BufferedImage imgBg = new BufferedImage(806, 1120,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2dBg = imgBg.createGraphics();
			g2dBg.setComposite(ac);
			
			g2dBg.drawImage(imgText, 85, 70, 85 + textArea.getWidth(), 70+pageHeight,
					0,i * pageHeight, textArea.getWidth(), i * pageHeight + pageHeight, null);
			
			try
			{
				
				g2dBg.drawImage(
						ImageIO.read(this.getClass().getResource(
								"/com/jci/alex/res/img/paper"
										+ layer0.currentPaper + ".png")), 0, 0,
						806, 1120, null);
				
				ImageIO.write(imgBg, "png", new File(filePath + "page" + i
						+ ".png"));
				
				
		
				
				
				g2dBg.dispose();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			h = h - pageHeight;
			i++;
			if(h>=0)
				setProgress(70*(total-h)/total);
			else
				setProgress(70);
			
		}
		
		g2dText.dispose();
		
		/*add PNGs to PDF*/
		Document document = new Document(new Rectangle(806, 1120), 0, 0, 0, 0);
		try
		{
			PdfWriter.getInstance(document, new FileOutputStream(filePath+"mybook.pdf"));
			document.open();
			
			for(int j=0;j<i;j++)
			{
				Image image = Image.getInstance(filePath + "page" + j
						+ ".png");
			
				
				document.add(image);
				setProgress(70+20*j/i);
			}
			
			
		} catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
		document.close();
		
		/*delete all PNG*/
		for(int j=0;j<i;j++)
		{
			File file = new File(filePath + "page" + j
					+ ".png");
			
			file.delete();
			setProgress(90+10*j/i);
		}
		
		
	}
}
