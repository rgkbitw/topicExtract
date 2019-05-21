import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.text.PDFTextStripper;
import java.util.*;
import java.nio.file.*;
import javax.swing.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.interactive.annotation.*;

class HelloJNI
{
    static
    {
        System.loadLibrary( "hello" );
    }

    // Declare a native method sayHello() that receives nothing and returns void
    private static native void sayHello();
    public static void work()
    {
        sayHello();
    }
    public static void main( String[] args )
    {
        work();
    }
}

class JFileChooserDemo extends JFrame
{
    final JTextArea outputArea;
    public JFileChooserDemo() throws IOException
    {

        super("OOAD Project");
        outputArea = new JTextArea();
        add(new JScrollPane(outputArea));
        analyzePath();
    }

    public void analyzePath() throws IOException
    {
        Path path = getFileOrDirectoryPath();
        File dir = new File("mallet", "Data" );
        dir.mkdir();
        File file = new File( path.toString() );
        PDDocument document = PDDocument.load( file );
        int n = document.getNumberOfPages();
        for( int i = 1; i <= n; i++ )
        {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage( i );
            pdfStripper.setEndPage( i );
            String text = pdfStripper.getText( document );
            String filename = i + ".txt";
            File actualFile = new File (dir, filename );
            PrintWriter writer = new PrintWriter( actualFile, "UTF-8" );
            writer.print( text );
            writer.close();
        }
        new HelloJNI().work();

        List<Integer> t1 = new ArrayList<Integer>();
		List<Integer> t2 = new ArrayList<Integer>();
		List<Integer> t3 = new ArrayList<Integer>();
        Scanner input;
        input = new Scanner( Paths.get( "mallet/tutorial_composition.txt" ) );
        int a;
        String b;
        double c;
        double d;
        double e;
        while( input.hasNext() )
        {
            a = input.nextInt();
            b = input.next();
            c = input.nextDouble();
            d = input.nextDouble();
            e = input.nextDouble();
            int num = 0, p = 1, x = b.length() - 5;
            while (b.charAt(x) != '/') {
                num +=  ( b.charAt(x) - 48 ) * p;
                p = p * 10;
                x--;
            }
            if (c > d && c > e)
            {
                t1.add( num + 1);
            }
            else if( d > c && d > e )
            {
                t2.add( num + 1 );
            }
            else
            {
                t3.add( num  + 1 );
            }
        }
		StringBuilder builder = new StringBuilder();
        builder.append( "Output PDF" + "\n"+ "Table of Contents" + "\n" );
		input = new Scanner( Paths.get( "mallet/tutorial_keys.txt" ) );
		String[][] topics = new String[ 3 ][ 20 ];
		int k = 0;
		while( input.hasNext() )
		{
			int y = input.nextInt();
			double w = input.nextDouble();
			for( int j = 0; j <= 19; j++ )
			{
				topics[ k ][ j ] = input.next();
			}
			k++;
		}
		builder.append( "Topic 1: " + Arrays.toString( topics[ 0 ] ) + " : " + Arrays.toString( t1.toArray() ) + "\n"
 +  
						"Topic 2: " + Arrays.toString( topics[ 1 ] ) + " : " +  Arrays.toString( t2.toArray() ) + "\n"
 + 
						"Topic 3: " + Arrays.toString( topics[ 2 ] ) + " : " + Arrays.toString( t3.toArray() ) + "\n"
 );
        outputArea.setText( builder.toString() );
		System.out.println( builder.toString());
        PDDocument copy = new PDDocument();
        PDPage contents = new PDPage();

        copy.addPage( contents );
        contents = copy.getPage( 0 );

    PDPageContentStream contentStream = new PDPageContentStream(copy, contents);

PDFont pdfFont = PDType1Font.HELVETICA;
    float fontSize = 20;
    float leading = 1.5f * fontSize;

    PDRectangle mediabox = contents.getMediaBox();
    float margin = 72;
    float width = mediabox.getWidth() - 2*margin;
    float startX = mediabox.getLowerLeftX() + margin;
    float startY = mediabox.getUpperRightY() - margin;

    String textNL = builder.toString();
    List<String> lines = new ArrayList<String>();
	for (String text : textNL.split("\n"))
{	
	int lastSpace = -1;
    while (text.length() > 0)
    {
        int spaceIndex = text.indexOf(' ', lastSpace + 1);
        if (spaceIndex < 0)
            spaceIndex = text.length();
        String subString = text.substring(0, spaceIndex);
        float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
        System.out.printf("'%s' - %f of %f\n", subString, size, width);
		PDPageXYZDestination dest = new PDPageXYZDestination();
		PDActionGoTo action = new PDActionGoTo();
				//action.setDestination( dest );
		PDAnnotationLink link = new PDAnnotationLink();
		PDRectangle rect = new PDRectangle();
		link.setAction(action);
				link.setDestination(dest);
		dest.setPage(document.getPage( 0 ));
		
		contents.getAnnotations().add(link);
        if (size > width)
        {
            if (lastSpace < 0)
                lastSpace = spaceIndex;
            subString = text.substring(0, lastSpace);
            lines.add(subString);
            text = text.substring(lastSpace).trim();
            System.out.printf("'%s' is line\n", subString);
            lastSpace = -1;
        }
        else if (spaceIndex == text.length())
        {
            lines.add(text);
            System.out.printf("'%s' is line\n", text);
            text = "";
        }
        else
        {
            lastSpace = spaceIndex;
        }
    }
}

    contentStream.beginText();
    contentStream.setFont(pdfFont, fontSize);
    contentStream.newLineAtOffset(startX, startY);
    for (String line: lines)
    {
        contentStream.showText(line);
        contentStream.newLineAtOffset(0, -leading);
    }
    contentStream.endText(); 
    contentStream.close();

        for( int i = 0; i <= n - 1; i++ )
        {
            PDPage temp1 = document.getPage( i );
            copy.addPage( temp1 );
        }
        copy.save("/home/avi/copy.pdf");
        System.out.println("PDF created");
        copy.close();
		document.close();
    }

    private Path getFileOrDirectoryPath()
    {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION )
            System.exit(1);

        return fileChooser.getSelectedFile().toPath();

    }
}
public class solve
{
    public static void main(String[] args) throws IOException
    {
        JFileChooserDemo application = new JFileChooserDemo();
        application.setSize(1280, 800);
        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        application.setVisible(true);
    }
}
