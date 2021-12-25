package com.practice;

public class AES256 {
	
	public static void main(String[] args) throws Exception{
		String key = "abcdefghijklmnopqrstuvwxyz123456";

		String plainText;
		String encodeText;
		String decodeText;
		// Encrypt
		plainText  = "imcore.net";
		encodeText = AES256Cipher.AES_Encode(plainText, key);		
		System.out.println("AES256_Encode : "+encodeText);
		 
		// Decrypt
		decodeText = AES256Cipher.AES_Decode(encodeText, key);
		System.out.println("AES256_Decode : "+decodeText);
		
	}
}
