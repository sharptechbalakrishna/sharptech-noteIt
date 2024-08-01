/*
 * package com.sharp.noteIt.controller;
 *//**
	 * @author Admin
	 *//*
		 * import java.io.IOException;
		 * 
		 * import org.springframework.beans.factory.annotation.Autowired; import
		 * org.springframework.http.HttpStatus; import
		 * org.springframework.http.ResponseEntity; import
		 * org.springframework.web.bind.annotation.PostMapping; import
		 * org.springframework.web.bind.annotation.RequestMapping; import
		 * org.springframework.web.bind.annotation.RequestParam; import
		 * org.springframework.web.bind.annotation.RestController; import
		 * org.springframework.web.multipart.MultipartFile;
		 * 
		 * import com.sharp.noteIt.service.QRCodeService;
		 * 
		 * @RestController
		 * 
		 * @RequestMapping("/api") public class QRCodeController {
		 * 
		 * @Autowired private QRCodeService qrCodeService;
		 * 
		 * @PostMapping("/decode-qr") public ResponseEntity<String>
		 * decodeQRCode(@RequestParam("file") MultipartFile file) { if (file.isEmpty())
		 * { return new ResponseEntity<>("The file is empty", HttpStatus.BAD_REQUEST); }
		 * try { String decodedText = qrCodeService.decodeQRCode(file.getInputStream());
		 * if (decodedText == null) { return new
		 * ResponseEntity<>("No QR code found in the image", HttpStatus.BAD_REQUEST); }
		 * return new ResponseEntity<>(decodedText, HttpStatus.OK); } catch (IOException
		 * e) { return new ResponseEntity<>("Could not decode QR code",
		 * HttpStatus.INTERNAL_SERVER_ERROR); } }
		 * 
		 * }
		 */