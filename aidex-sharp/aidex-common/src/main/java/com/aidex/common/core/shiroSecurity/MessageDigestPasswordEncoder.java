package com.aidex.common.core.shiroSecurity;

import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestPasswordEncoder extends BaseDigestPasswordEncoder
{

    public MessageDigestPasswordEncoder(String algorithm)
    {
        this(algorithm, false);
    }

    public MessageDigestPasswordEncoder(String algorithm, boolean encodeHashAsBase64)
        throws IllegalArgumentException
    {
        iterations = 1;
        this.algorithm = algorithm;
        setEncodeHashAsBase64(encodeHashAsBase64);
        getMessageDigest();
    }

    @Override
    public String encodePassword(String rawPass, Object salt)
    {
        String saltedPass = mergePasswordAndSalt(rawPass, salt, false);
        MessageDigest messageDigest = getMessageDigest();
        byte digest[];
        try
        {
            digest = messageDigest.digest(saltedPass.getBytes("UTF-8"));
        }
        catch(UnsupportedEncodingException e)
        {
            throw new IllegalStateException("UTF-8 not supported!");
        }
        for(int i = 1; i < iterations; i++){
            digest = messageDigest.digest(digest);}

        if(getEncodeHashAsBase64()){
            return Utf8.decode(Base64.encode(digest));}
        else{
            return new String(Hex.encode(digest));}
    }

    protected final MessageDigest getMessageDigest()
        throws IllegalArgumentException
    {
        try
        {
            return MessageDigest.getInstance(algorithm);
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("No such algorithm [").append(algorithm).append("]").toString());
        }
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt)
    {
        String pass1 = (new StringBuilder()).append("").append(encPass).toString();
        String pass2 = encodePassword(rawPass, salt);
        return pass1.equals(pass2);
        //return PasswordEncoderUtils.equals(pass1, pass2);
    }

    public String getAlgorithm()
    {
        return algorithm;
    }

    public void setIterations(int iterations)
    {
        Assert.isTrue(iterations > 0, "Iterations value must be greater than zero");
        this.iterations = iterations;
    }

    private final String algorithm;
    private int iterations;
}
