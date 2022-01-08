package fr.tangv.sorcicubecore.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class RepartedHeadArrow {

	public static void main(String[] args) {
		try {
			if (args.length >= 2) {
				String arg = args[0];
				String rest = args[1];
				for (int i = 2; i < args.length; i++)
					rest += " "+args[i];
				//input
				InputStream in;
				if (arg.equalsIgnoreCase("-file")) {
					in = new FileInputStream(new File(rest));
				} else if (arg.equalsIgnoreCase("-url")) {
					in = new URL(rest).openStream();
				} else {
					throw new Exception("first argument invalid, argument: (-file or -url possible) (path or url)");
				}
				BufferedImage img = ImageIO.read(in);
				Graphics2D g = img.createGraphics();
				g.setComposite(AlphaComposite.Src);
				//copy 1
				g.copyArea(8, 8, 8, 8, -8, 0);
				g.copyArea(8, 8, 8, 8, 8, 0);
				g.copyArea(8, 8, 8, 8, 16, 0);
				//copy 2
				g.copyArea(40, 8, 8, 8, -8, 0);
				g.copyArea(40, 8, 8, 8, 8, 0);
				g.copyArea(40, 8, 8, 8, 16, 0);
				//dispose
				g.dispose();
				//ouput
				File outputFile;
				int i = 0;
				do {
					i++;
					outputFile = new File("./output/output_"+i+".png");
				} while (outputFile.exists());
				//create parent if
				if (!outputFile.getParentFile().exists())
					outputFile.getParentFile().mkdirs();
				//create file
				outputFile.createNewFile();
				if (!outputFile.exists())
					outputFile.createNewFile();
				ImageIO.write(img, "png", outputFile);
				System.out.println("File Output: "+outputFile.getPath());
			} else {
				throw new Exception("Arguments is invalid, argument: (-file or -url possible) (path or url)");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
