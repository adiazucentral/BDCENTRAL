package net.cltech.services;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Encrypter
{
    private Cipher eCipher;
    private Cipher dCipher;

    public Encrypter(String passPhrase)
    {
        try
        {
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray());
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            eCipher = Cipher.getInstance(key.getAlgorithm());
            dCipher = Cipher.getInstance(key.getAlgorithm());
            byte[] salt = {-87, -101, -56, 50, 86, 53, -29, 3};
            int iterationCount = 19;
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            eCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (InvalidAlgorithmParameterException | InvalidKeySpecException | NoSuchPaddingException |
                 NoSuchAlgorithmException | InvalidKeyException e)
        {
            e.printStackTrace(System.out);
        }
    }

    public String encrypt(String string)
    {
        try
        {
            byte[] utf8 = string.getBytes(StandardCharsets.UTF_8);
            byte[] enc = eCipher.doFinal(utf8);
            return Base64.getEncoder().encodeToString(enc);
        } catch (BadPaddingException | IllegalBlockSizeException e)
        {
            System.out.println("Error to encrypt " + string + ", not valid.");
            return string;
        }
    }

    public String decrypt(String string)
    {
        try
        {
            byte[] dec = Base64.getDecoder().decode(string);
            byte[] utf8 = dCipher.doFinal(dec);
            return new String(utf8, StandardCharsets.UTF_8);
        } catch (BadPaddingException | IllegalBlockSizeException | IllegalArgumentException e)
        {
            System.out.println("Error to decrypt " + string + ", not valid.");
            return string;
        }
    }
}