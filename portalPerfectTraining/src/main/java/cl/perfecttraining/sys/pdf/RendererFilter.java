/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.sys.pdf;

/**
 *
 * @author Freddy Jimenez
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;

import javax.imageio.ImageIO;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.simple.Graphics2DRenderer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lowagie.text.DocumentException;

public class RendererFilter implements Filter {

	FilterConfig config;
	private DocumentBuilder documentBuilder;
	
	public void init(FilterConfig config) throws ServletException {
		try {
			this.config = config;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			documentBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new ServletException(e);
		}
	}

	public void doFilter(ServletRequest req, ServletResponse resp, 
						 FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		//Check to see if this filter should apply.
		String renderType = request.getParameter("RenderOutputType");
		if(renderType != null){
			//Capture the content for this request
			ContentCaptureServletResponse capContent = new ContentCaptureServletResponse(response);
			filterChain.doFilter(request,capContent);
		
			try {
				//Parse the XHTML content to a document that is readable by the XHTML renderer.
				StringReader contentReader = new StringReader(capContent.getContent());
				InputSource source = new InputSource(contentReader);
				Document xhtmlContent = documentBuilder.parse(source);
				
				if(renderType.equals("pdf")){
					ITextRenderer renderer = new ITextRenderer();
					renderer.setDocument(xhtmlContent,"");
					renderer.layout();
					
					response.setContentType("application/pdf");
					OutputStream browserStream = response.getOutputStream();
					renderer.createPDF(browserStream);
					return;
				}
				
				//For the other formats, you might need to specify a width and a height.
				int width = 850;
				int height = 500;
				
				try{
					if(request.getParameter("width") != null) width = Integer.parseInt(request.getParameter("width"));
					if(request.getParameter("height") != null) height = Integer.parseInt(request.getParameter("height"));
				}catch(NumberFormatException ne){ /*Nothing much to do here*/}
				
				Graphics2DRenderer renderer = new Graphics2DRenderer();
				renderer.setDocument(xhtmlContent,"");
				
				if(renderType.equals("image")){
					
					BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
					Graphics2D imageGraphics = (Graphics2D)image.getGraphics();
					imageGraphics.setColor(Color.white);
					imageGraphics.fillRect(0, 0, width, height);
					renderer.layout(imageGraphics,new Dimension(width,height));
					renderer.render(imageGraphics);
					
					//Now finally output the image to PNG using the ImageIO libraries.
					OutputStream browserStream = response.getOutputStream();
					response.setContentType("image/png");
					ImageIO.write(image, "png", browserStream);
					
					return;
				}
				
				
			} catch (SAXException e) {
				throw new ServletException(e);
			} catch (DocumentException e) {
				throw new ServletException(e);
			}
			
			
		}else{
			//Normal processing
			filterChain.doFilter(request,response);
		}
	}

	
	public void destroy() { }
}
