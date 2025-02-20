class PreProcessor
{
    public static String preprocess(String input)
    {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        int len = input.length();

        for (int i = 0; i < len; i++)
        {
            if (inBlockComment)
            {
                if (i + 1 < len && input.charAt(i) == '*' && input.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++;
                }
            }
            else if (inLineComment)
            {
                if (input.charAt(i) == '\n')
                {
                    inLineComment = false;
                    result.append('\n');
                }
            }
            else
            {
                if (i + 1 < len)
                {
                    if (input.charAt(i) == '/' && input.charAt(i + 1) == '*')
                    {
                        inBlockComment = true;
                        i++;
                        continue;
                    }
                    else if (input.charAt(i) == '/' && input.charAt(i + 1) == '/')
                    {
                        inLineComment = true;
                        i++;
                        continue;
                    }
                }
                if (!inBlockComment && !inLineComment)
                {
                    result.append(input.charAt(i));
                }
            }
        }
        return result.toString();
    }
}
