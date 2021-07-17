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

import lunartools.colorquantizer.DitheringAlgorithm;
import lunartools.colorquantizer.GPAC;
import lunartools.colorquantizer.QuantizerAlgorithm;
import lunartools.ganimed.gui.optionscolourreduction.model.ColourReductionModel;
import lunartools.ganimed.gui.optionsgif.model.OptionsGifModel;

public class GifAnimCreator implements AnimCreator{
	private GanimedController ganimedController;
	private ByteArrayOutputStream baos;
	private ImageOutputStream imageOutputStream;
	private ImageWriter imageWriter;
	private ImageWriteParam imageWriteParams;
	private IIOMetadata iooMetadata;
	private int delay;
	private GPAC gpac;
	private boolean loop;
	private ImageData previousImageData;
	private int currentImageDelay;

	public GifAnimCreator(OptionsGifModel optionsGifModel,GanimedController ganimedController) {
		this.ganimedController=ganimedController;
		gpac=new GPAC();
		ColourReductionModel colourReductionModel=optionsGifModel.getColourReductionModel();
		switch(colourReductionModel.getQuantizerAlgorithm()) {
		case MEDIAN_CUT:
			gpac.setQuantizerAlgorithm(QuantizerAlgorithm.MEDIAN_CUT);
			break;
		default:
			throw new RuntimeException("colour quantizer algorithm not supported: "+colourReductionModel.getQuantizerAlgorithm());
		}
		switch(colourReductionModel.getDitheringAlgorithm()) {
		case NO_DITHERING:
			gpac.setDitheringAlgorithm(DitheringAlgorithm.NO_DITHERING);
			break;
			//case SIMPLE_DITHERING1:
			//	gpac.setDitheringAlgorithm(DitheringAlgorithm.SIMPLE_DITHERING1);
			//	break;
		case FLOYD_STEINBERG:
			gpac.setDitheringAlgorithm(DitheringAlgorithm.FLOYD_STEINBERG);
			break;
		case JARVIS_JUDICE_NINKE:
			gpac.setDitheringAlgorithm(DitheringAlgorithm.JARVIS_JUDICE_NINKE);
			break;
		case STUCKI:
			gpac.setDitheringAlgorithm(DitheringAlgorithm.STUCKI);
			break;
		case ATKINSON:
			gpac.setDitheringAlgorithm(DitheringAlgorithm.ATKINSON);
			break;
		case BURKES:
			gpac.setDitheringAlgorithm(DitheringAlgorithm.BURKES);
			break;
		case SIERRA:
			gpac.setDitheringAlgorithm(DitheringAlgorithm.SIERRA);
			break;
		case TWO_ROW_SIERRA:
			gpac.setDitheringAlgorithm(DitheringAlgorithm.TWO_ROW_SIERRA);
			break;
		case SIERRA_LITE:
			gpac.setDitheringAlgorithm(DitheringAlgorithm.SIERRA_LITE);
			break;
		default:
			throw new RuntimeException("dithering algorithm not supported: "+colourReductionModel.getDitheringAlgorithm());
		}
	}

	public void addImage(ImageData imageData, int delay, boolean loop) throws IOException {
		this.loop=loop;
		if(previousImageData==null) {
			previousImageData=imageData;
			currentImageDelay=delay;
			return;
		}
		if(previousImageData==imageData) {
			currentImageDelay+=delay;
			return;
		}
		writeCurrentImage();
		previousImageData=imageData;
		currentImageDelay=delay;
	}

	private void writeCurrentImage() throws IOException {
		BufferedImage bufferedImage=previousImageData.getResultBufferedImage();
		gpac.quantizeColours(bufferedImage,256);
		if(iooMetadata==null) {
			this.delay=currentImageDelay;
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
		if(this.delay!=currentImageDelay) {
			this.delay=currentImageDelay;
			configureRootMetadata(iooMetadata,delay, loop);
		}

		imageWriter.writeToSequence(new IIOImage(bufferedImage, null, iooMetadata), imageWriteParams);
		previousImageData=null;
	}
	
	public byte[] toByteArray() throws IOException {
		writeCurrentImage();
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
		commentsNode.setAttribute("CommentExtension", "created with "+GanimedModel.PROGRAMNAME+" "+GanimedModel.determineProgramVersion());

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
