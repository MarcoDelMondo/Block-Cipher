public class block {
	
	// rotRight - This function rotates an input byte right
	// This shifts all bits to the right, and shuffles the last
	// bit in the byte to the front, i.e. 10000111 becomes 11000011
	// The number of bits to shift is set by the integer shift
	public static byte rotRight(byte input, int shift) {
		return (byte) ((input & 0xFF) >>> shift | (input & 0xFF) << (8 - shift));
	}
	
	// rotLeft - This function rotates an input byte left
	// This shifts all bits to the left, and shuffles the first
	// bit in the byte to the back, i.e. 10000111 becomes 00001111
	// The number of bits to shift is set by the integer shift
	public static byte rotLeft(byte input, int shift) {
		return (byte) ((input & 0xFF) << shift | (input & 0xFF) >>> (8 - shift));
	}
	
	// round - This function takes two bytes, and should perform
	// an ARX-style mutation on the two bytes using consecutive
	// addition, XOR, and rotation functions. It returns an array
	// of the two bytes after mixing.
	public static byte[] round(byte a, byte b) {
		a ^= b;
		b += a;
		b = rotRight(b, 3);
		byte[] out = {a, b};
		return out;
	}
	
	// roundInverse - This function takes two bytes and performs
	// the inverse of the round() function on them. This means it
	// reverses the mixing performed in the round() function, and
	// returns the original values of a and b before mixing as an
	// array of two bytes.
	public static byte[] roundInverse(byte a, byte b) {
		b = rotLeft(b, 3);
		b -= a;
		a ^= b;
		byte [] out = {a, b};
		return out;
	}
	
	public static byte[] encryptBlock(byte[] data, byte[] key, int rounds) {
		int i = 0;
		byte[] temp;
		for(i = 0; i < rounds; i++) {
			data[0] ^= key[0];
			data[1] ^= key[1];
			temp = round(data[0], data[1]);
			data[0] = temp[0];
			data[1] = temp[1];
		}
		return data;
	}
	
	public static byte[] decryptBlock(byte[] data, byte[] key, int rounds) {
		int i = 0;
		byte[] temp;
		for(i = 0; i < rounds; i++) {
			temp = roundInverse(data[0], data[1]);
			temp[0] ^= key[0];
			temp[1] ^= key[1];
			data[0] = temp[0];
			data[1] = temp[1];
		}
		return data;
	}
	
	public static byte[] ecbEncrypt(byte[] data, byte[] key) {
		byte[] out = new byte[data.length];
		int i = 0;
		for(i = 0; i < data.length; i += 2) {
			byte[] temp;
			byte[] block = {data[i], data[i + 1]};
			temp = (encryptBlock(block, key, 10));
			out[i] = temp[0];
			out[i + 1] = temp[1];
		}
		return out;
	}
	
	public static byte[] ecbDecrypt(byte[] data, byte[] key) {
		byte[] out = new byte[data.length];
		int i = 0;
		for(i = 0; i < data.length; i += 2) {
			byte[] temp;
			byte[] block = {data[i], data[i + 1]};
			temp = decryptBlock(block, key, 10);
			out[i] = temp[0];
			out[i + 1] = temp[1];
		}
		return out;
	}
	
	public static void main(String[] args) {
		// This will help test your internal functions
		// Test data input
		byte[] testInput = {22, (byte) 144};
		// Test key
		byte[] testKey = {(byte) 192, 44};
		byte[] roundTest = round(testInput[0], testInput[1]);
		byte[] inverseRoundTest = roundInverse(roundTest[0], roundTest[1]);
		System.out.println(inverseRoundTest[0]);
		System.out.println(inverseRoundTest[1]);
		byte[] ciphertext = encryptBlock(testInput, testKey, 10);
		byte[] plaintext = decryptBlock(ciphertext, testKey, 10);
		System.out.println(plaintext[0]);
		System.out.println(plaintext[1] & 0xFF);
		// This will help you test the block-cipher ECB mode encryption
		byte[] testLong = "Hello World, this is my block cipher".getBytes();
		// Encrypt the message
		byte[] ecbEncrypt = ecbEncrypt(testLong, testKey);
		// Decrypt the message
		byte[] ecbDecrypt = ecbDecrypt(ecbEncrypt, testKey);
		System.out.println(new String(ecbDecrypt));
	}

}