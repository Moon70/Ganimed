package lunartools.ganimed;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class GifAnimCreator {
	private ByteArrayOutputStream baos;
	private ImageOutputStream imageOutputStream;
	private ImageWriter imageWriter;
	private ImageWriteParam imageWriteParams;
	private IIOMetadata iooMetadata;
	private int delay;
	
	public void addImage(BufferedImage bufferedImage, int delay, boolean loop) throws IOException {
		if(iooMetadata==null) {
			this.delay=delay;
			baos=new ByteArrayOutputStream();
			imageOutputStream = new MemoryCacheImageOutputStream(baos);
			
			ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(bufferedImage.getType());
			imageWriter = ImageIO.getImageWritersBySuffix("gif").next();
			imageWriteParams = imageWriter.getDefaultWriteParam();
			iooMetadata = imageWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParams);
			configureRootMetadata(iooMetadata,delay, loop);
			imageWriter.setOutput(imageOutputStream);
			imageWriter.prepareWriteSequence(null);
		}
		if(this.delay!=delay) {
			this.delay=delay;
			configureRootMetadata(iooMetadata,delay, loop);
		}
		
		imageWriter.writeToSequence(new IIOImage(bufferedImage, null, iooMetadata), imageWriteParams);
	}
	
	public byte[] toByteArray() throws IOException {
		imageWriter.endWriteSequence();
		imageOutputStream.close();
		return baos.toByteArray();
	}
	
	private static void configureRootMetadata(IIOMetadata iooMetadata, int delay, boolean loop) throws IIOInvalidTreeException {
		String metaFormatName = iooMetadata.getNativeMetadataFormatName();
		IIOMetadataNode root = (IIOMetadataNode) iooMetadata.getAsTree(metaFormatName);

		IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
		graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
		graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
		graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
		graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString((delay+5) / 10));
		graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

		IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
		commentsNode.setAttribute("CommentExtension", "Created with Ganimed 1.0");

		IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
		IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
		child.setAttribute("applicationID", "NETSCAPE");
		child.setAttribute("authenticationCode", "2.0");

		child.setUserObject(new byte[]{ 0x1, 0, 0});
		appExtensionsNode.appendChild(child);
		
		iooMetadata.setFromTree(metaFormatName, root);
	}
	
	private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName){
		for (int i = 0; i < rootNode.getLength(); i++){
			if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)){
				return (IIOMetadataNode) rootNode.item(i);
			}
		}
		IIOMetadataNode nodeMetadata = new IIOMetadataNode(nodeName);
		rootNode.appendChild(nodeMetadata);
		return(nodeMetadata);
	}

}
