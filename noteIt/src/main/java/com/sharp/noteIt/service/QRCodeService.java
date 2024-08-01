/*
 * package com.sharp.noteIt.service;
 * 
 * import java.awt.image.BufferedImage; import java.io.IOException; import
 * java.io.InputStream;
 * 
 * import javax.imageio.ImageIO;
 * 
 * import org.springframework.stereotype.Service;
 * 
 * import com.google.zxing.BinaryBitmap; import
 * com.google.zxing.MultiFormatReader; import
 * com.google.zxing.NotFoundException; import com.google.zxing.Result; import
 * com.google.zxing.client.j2se.BufferedImageLuminanceSource; import
 * com.google.zxing.common.HybridBinarizer;
 *//**
	* 
	*//*
		 * @Service public class QRCodeService {
		 * 
		 * public String decodeQRCode(InputStream qrCodeImage) throws IOException {
		 * BufferedImage bufferedImage = ImageIO.read(qrCodeImage); BinaryBitmap bitmap
		 * = new BinaryBitmap(new HybridBinarizer(new
		 * BufferedImageLuminanceSource(bufferedImage)));
		 * 
		 * try { Result result = new MultiFormatReader().decode(bitmap); return
		 * result.getText(); } catch (NotFoundException e) {
		 * System.out.println("There is no QR code in the image"); return null; } }
		 * 
		 * }
		 */