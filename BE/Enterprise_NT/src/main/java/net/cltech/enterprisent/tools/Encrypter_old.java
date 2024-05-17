/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.tools;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Encrypter_old
{

    private Cipher ecipher;
    private Cipher dcipher;
    private byte[] salt =
    {
        -87, -101, -56, 50, 86, 53, -29, 3
    };
    private int iterationCount = 19;

    public Encrypter_old(String passPhrase)
    {
        try
        {
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray());
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (InvalidAlgorithmParameterException | InvalidKeySpecException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e)
        {
            e.printStackTrace(System.out);
        }
    }

    public String encrypt(String str)
    {
        try
        {
            if (str == null || str.isEmpty())
            {
                return str;
            }
            else{
                byte[] utf8 = str.getBytes("UTF-8");
                byte[] enc = ecipher.doFinal(utf8);
                return Base64.getEncoder().encodeToString(enc);
            }
        } catch (BadPaddingException | IllegalBlockSizeException | IOException e)
        {
            e.printStackTrace(System.out);
        }
        return null;
    }

    public String decrypt(String str)
    {
        str = str.replace(" ","");
        if (str != null && !str.isEmpty())
        {
            try
            {
                byte[] dec = Base64.getDecoder().decode(str);
                byte[] utf8 = dcipher.doFinal(dec);
                return new String(utf8, "UTF-8");
            }
            catch (BadPaddingException | IllegalBlockSizeException | IOException e)
            {
                 e.printStackTrace(System.out);
            }
        }           
        else
        {
            return str;
        }
        return null;
    }

}
