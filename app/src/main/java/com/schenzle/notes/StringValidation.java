package com.schenzle.notes;


public class StringValidation
{
    /**
     * Check if a given string is void
     *
     * @param subject String
     * @return boolean
     */
    public static boolean isFieldEmpty(String subject)
    {
        return (subject.length() == 0);
    }
}
