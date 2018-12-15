package com.github.davidbolet.jpascalcoin.crypto.helper;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.github.davidbolet.jpascalcoin.common.model.PascPublicKey;

public class EncryptionUtils {
	
	public static byte[] encryptWithPublicKey(PascPublicKey publicKey, byte[] payload) {
		
//		byte[] envelope_key =new byte[64];
//		byte[] iv=new byte[16];
//		byte[] block=new byte[32];
//		
//	    final MessageDigest md = MessageDigest.getInstance("MD5");
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
//
//		IvParameterSpec iVector = new IvParameterSpec(longIv);
//		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
//
//		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iVector);
//
//		byte[] encrypted = cipher.doFinal(payload);
//		
//		outputStream.write( encrypted);
//
//		return outputStream.toByteArray();
		
		throw new UnsupportedOperationException();
	} 
}

/*
function ECIESEncrypt(EC_OpenSSL_NID : Word; PubKey: EC_POINT; const MessageToEncrypt: AnsiString): TRawBytes;
Var PK,PEphemeral : PEC_KEY;
  i,key_length,block_length,envelope_length,body_length : Integer;
  mac_length : Cardinal;
  envelope_key : Array[1..SHA512_DIGEST_LENGTH] of byte;
  iv: Array[1..EVP_MAX_IV_LENGTH] of byte;
  block:Array[1..EVP_MAX_BLOCK_LENGTH] of byte;
  cryptex : Psecure_t;
  pcipher : PEVP_CIPHER_CTX;
  body,aux : Pointer;
  phmac : PHMAC_CTX;
  {$IFDEF OpenSSL10}
  cipher : EVP_CIPHER_CTX;
  hmac : HMAC_CTX;
  {$ENDIF}
begin
  Result := '';
  if length(MessageToEncrypt)>CT_Max_Bytes_To_Encrypt then begin
    TLog.NewLog(lterror,'ECIES','Max bytes to encrypt: '+inttostr(length(MessageToEncrypt))+'>'+Inttostr(CT_Max_Bytes_To_Encrypt));
    exit;
  end;
  // Make sure we are generating enough key material for the symmetric ciphers.
  key_length := (EVP_CIPHER_key_length(EVP_aes_256_cbc));
  if (key_length*2)>SHA512_DIGEST_LENGTH then begin
    TLog.NewLog(lterror,'ECIES',Format('The key derivation method will not produce enough envelope key material for the chosen ciphers. {envelope = %i / required = %zu}',
      [SHA512_DIGEST_LENGTH DIV 8,(key_length * 2) DIV 8]));
    exit;
  end;
  // Convert the user's public key from hex into a full EC_KEY structure.
  PK := EC_KEY_new_by_curve_name(EC_OpenSSL_NID);
  PEphemeral := EC_KEY_new_by_curve_name(EC_OpenSSL_NID);
  try
    if (EC_KEY_set_public_key(PK,@PubKey)<>1) then begin
      TLog.NewLog(lterror,'ECIES','Invalid public key provided');
      exit;
    end;
    if (EC_KEY_generate_key(PEphemeral)<>1) then begin
      TLog.NewLog(lterror,'ECIES','An error occurred while trying to generate the ephemeral key');
      exit;
    end;
    // Use the intersection of the provided keys to generate the envelope data used by the ciphers below. The ecies_key_derivation_512() function uses
    // SHA 512 to ensure we have a sufficient amount of envelope key material and that the material created is sufficiently secure.
    if (ECDH_compute_key(@envelope_key,SHA512_DIGEST_LENGTH,EC_KEY_get0_public_key(PK),PEphemeral,ecies_key_derivation_512)<>SHA512_DIGEST_LENGTH) then begin
      TLog.NewLog(lterror,'ECIES',Format('An error occurred while trying to compute the envelope key {error = %s}',[ERR_error_string(ERR_get_error(), nil)]));
      exit;
    end;
    // Determine the envelope and block lengths so we can allocate a buffer for the result.
    block_length := EVP_CIPHER_block_size(EVP_aes_256_cbc);
    if (block_length=0) or (block_length>EVP_MAX_BLOCK_LENGTH) then begin
      TLog.NewLog(lterror,'ECIES',Format('Invalid block length {block = %zu}',[block_length]));
      exit;
    end;
    envelope_length := EC_POINT_point2oct(EC_KEY_get0_group(PEphemeral),EC_KEY_get0_public_key(PEphemeral),POINT_CONVERSION_COMPRESSED,nil,0,nil);
    if (envelope_length=0) then begin
      TLog.NewLog(lterror,'ECIES',Format('Invalid envelope length {envelope = %zu}',[envelope_length]));
      exit;
    end;
    // We use a conditional to pad the length if the input buffer is not evenly divisible by the block size.
    if (Length(MessageToEncrypt) MOD block_length)=0 then i := 0
    else i := block_length - (Length(MessageToEncrypt) MOD block_length);
    cryptex := secure_alloc(envelope_length,EVP_MD_size(ECIES_HASHER),Length(MessageToEncrypt), Length(MessageToEncrypt) + i);
    try
      // Store the public key portion of the ephemeral key.
      If EC_POINT_point2oct(EC_KEY_get0_group(PEphemeral),EC_KEY_get0_public_key(PEphemeral),
           POINT_CONVERSION_COMPRESSED,secure_key_data(cryptex),envelope_length,Nil)<>envelope_length then begin
        TLog.NewLog(lterror,'ECIES',Format('An error occurred while trying to record the public portion of the envelope key {error = %s}',
          [ERR_error_string(ERR_get_error(),nil)]));
        exit;
      end;
      // For now we use an empty initialization vector.
      {$IFDEF FPC}
      FillByte(iv,EVP_MAX_IV_LENGTH,0);
      {$ELSE}
      FillMemory(@iv,EVP_MAX_IV_LENGTH,0);
      {$ENDIF}
      // Setup the cipher context, the body length, and store a pointer to the body buffer location.

      {$IFDEF OpenSSL10}
      EVP_CIPHER_CTX_init(@cipher);
      pcipher := @cipher;
      {$ELSE}
      pcipher := EVP_CIPHER_CTX_new;
      {$ENDIF}
      try
        body := secure_body_data(cryptex);
        body_length := secure_body_length(cryptex);
        // Initialize the cipher with the envelope key.
        if (EVP_EncryptInit_ex(pcipher,EVP_aes_256_cbc,nil,@envelope_key,@iv)<>1) or
          (EVP_CIPHER_CTX_set_padding(pcipher,0)<>1) or
          (EVP_EncryptUpdate(pcipher,body,body_length,@MessageToEncrypt[1],
            Length(MessageToEncrypt) - (Length(MessageToEncrypt) MOD block_length))<>1) then begin
              TLog.NewLog(lterror,'ECIES',Format('An error occurred while trying to secure the data using the chosen symmetric cipher. {error = %s}',
              [ERR_error_string(ERR_get_error(),nil)]));
              exit;
            end;
        // Check whether all of the data was encrypted. If they don't match up, we either have a partial block remaining, or an error occurred.
        if (body_length<>Length(MessageToEncrypt)) then begin
          // Make sure all that remains is a partial block, and their wasn't an error
          if (Length(MessageToEncrypt) - body_length >= block_length) then begin
            TLog.NewLog(lterror,'ECIES',Format('Unable to secure the data using the chosen symmetric cipher. {error = %s}',
            [ERR_error_string(ERR_get_error(),nil)]));
            exit;
          end;
          // Copy the remaining data into our partial block buffer. The memset() call ensures any extra bytes will be zero'ed out.
          //SetLength(block,EVP_MAX_BLOCK_LENGTH);
          {$IFDEF FPC}
          FillByte(block,length(block),0);
          {$ELSE}
          FillMemory(@block,length(block),0);
          {$ENDIF}
          CopyMemory(@block,Pointer(PtrInt(@MessageToEncrypt[1])+body_length),Length(MessageToEncrypt)-body_length);
          // Advance the body pointer to the location of the remaining space, and calculate just how much room is still available.
          body := Pointer(PtrInt(body)+body_length);
          body_length := secure_body_length(cryptex) - body_length;
          if (body_length <0) then begin
             TLog.NewLog(lterror,'ECIES','The symmetric cipher overflowed!');
             exit;
          end;
          // Pass the final partially filled data block into the cipher as a complete block.
          // The padding will be removed during the decryption process.
          if (EVP_EncryptUpdate(pcipher, body, body_length, @block, block_length)<>1) then begin
            TLog.NewLog(lterror,'ECIES',Format('Unable to secure the data using the chosen symmetric cipher. {error = %s}',
            [ERR_error_string(ERR_get_error(),nil)]));
            exit;
          end;
        end;
        // Advance the pointer, then use pointer arithmetic to calculate how much of the body buffer has been used. The complex logic is needed so that we get
        // the correct status regardless of whether there was a partial data block.
        body := Pointer(PtrInt(body)+body_length);
        body_length := secure_body_length(cryptex) - (PtrInt(body)-PtrInt(secure_body_data(cryptex)));
        if (body_length < 0) then begin
          TLog.NewLog(lterror,'ECIES','The symmetric cipher overflowed!');
          exit;
        end;
        if (EVP_EncryptFinal_ex(pcipher, body, body_length)<>1) then begin
          TLog.NewLog(lterror,'ECIES',Format('Unable to secure the data using the chosen symmetric cipher. {error = %s}',
          [ERR_error_string(ERR_get_error(),nil)]));
          exit;
        end;
      finally
        {$IFDEF OpenSSL10}
        EVP_CIPHER_CTX_cleanup(pcipher);
        {$ELSE}
        EVP_CIPHER_CTX_free(pcipher);
        {$ENDIF}
      end;
      // Generate an authenticated hash which can be used to validate the data during decryption.
      {$IFDEF OpenSSL10}
      HMAC_CTX_init(@hmac);
      phmac := @hmac;
      {$ELSE}
      phmac := HMAC_CTX_new;
      {$ENDIF}
      Try
        mac_length := secure_mac_length(cryptex);
        // At the moment we are generating the hash using encrypted data. At some point we may want to validate the original text instead.
        aux := Pointer(PtrInt(@envelope_key) + key_length);
        if (HMAC_Init_ex(phmac, aux, key_length, ECIES_HASHER, nil)<>1)
          OR (HMAC_Update(phmac, secure_body_data(cryptex), secure_body_length(cryptex))<>1)
          OR (HMAC_Final(phmac, secure_mac_data(cryptex),mac_length)<>1) then begin
          TLog.NewLog(lterror,'ECIES',Format('Unable to generate a data authentication code. {error = %s}',
          [ERR_error_string(ERR_get_error(),nil)]));
          exit;
        end;
      Finally
        {$IFDEF OpenSSL10}
        HMAC_CTX_cleanup(phmac);
        {$ELSE}
        HMAC_CTX_free(phmac);
        {$ENDIF}
      End;
      SetLength(Result,secure_total_length(cryptex));
      CopyMemory(@Result[1],cryptex,length(Result));
    finally
      secure_free(cryptex);
    end;
  finally
    EC_KEY_free(PK);
    EC_KEY_free(PEphemeral);
  end;
end;                                         


*/