encrypt:
	help = Encrypt a file using answers from a questions file. To decrypt the file, use the 'decrypt' command and answer the questions correctly.
	properties:
		verbose:
			help = Display additional information
			markers = -v --verbose
		file:
			help = File to encrypt
			count = 1 1
		questions:
			help =
				File containing the questions. To create a valid questions file, use the 'create' command.
				The questions will be embedded within the encrypted file, thus, the 'decrypt' command does not require this file.
			count = 1 1
		keyBuffer:
			help = Set the key buffer method. Must be either 'concat' or 'xor'
			count = 1 1
			markers = --buffer -b
			default = xor
		algorithm:
			help = Set the encryption algorithm
			count = 1 1
			markers = --algorithm -a
			default = AES
		keySize:
			help = Set the encryption key size
			count = 1 1
			markers = --key -k
			default = 128
			type = integer

decrypt:
	help = Decrypt a file using a questions-based password
	properties:
		encrypt.verbose:
		file:
			help = File to decrypt (must have the extension '$1')
			count = 1 1

generate:
	help = Generate a password using a questions-based aggregator
	properties:
		encrypt.verbose:
		questions:
			help = File containing the questions. To create a valid questions file, use the 'create' command.
			count = 1 1
		encrypt.keyBuffer:
		encrypt.algorithm:
		encrypt.keySize:
