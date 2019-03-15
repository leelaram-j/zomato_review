package com.txt.read;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Txt_Read
{
	public static String[] txtRead(String filename)
	{
		FileReader fileReader;
		List<String> lines = new ArrayList<String>();
		BufferedReader bufferedReader;
		try 
		{
			fileReader = new FileReader(filename);
			bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) 
			{
			lines.add(line);
			}
			bufferedReader.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
		}
		catch (IOException e)
		{
			System.out.println("IO Error");
		}
		
		return lines.toArray(new String[lines.size()]);
	}

}
