package com.itextpdf;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.File;
import java.io.IOException;

public class SampleCode {
    public static final String BASE_URI = "src/main/resources/";
    public static final String SRC = BASE_URI + "input.pdf";
    public static final String DEST = BASE_URI + "watermarked_output.pdf";

    public static void main(String[] args) throws Exception {
        new SampleCode().manipulatePdf(SRC,DEST);
    }

    protected void manipulatePdf(String inFile, String outFile) throws IOException {
      
        var rotation = 45f;  // Rotate watermark at 45 degrees, For complex angles such as 45.1,45.2 we can use float.
        float opacityForWatermark = 0.5f;  // Setting opacity to 50% for the watermark
        float fontSize = 40f;

        // Opening the input.PDF and preparing it for watermarking
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(inFile), new PdfWriter(outFile));

        // Use Graphic State to implement transperancy to the watermark (opacity) 
        PdfExtGState GS = new PdfExtGState();
        GS.setFillOpacity(opacityForWatermark);

        // Getting total no. of pages in the input file using pdfDoc
        int numPages = pdfDoc.getNumberOfPages();
        // Iterating through every page to make sure they are watermarked.
        for (int i = 1; i <= numPages; i++) {
            PdfCanvas pdfCanvas = new PdfCanvas(pdfDoc.getPage(i));
            pdfCanvas.setExtGState(GS);  // Apply the opacity state

            // Set up the Canvas for the watermark text and changing the fontSize
            Canvas watermarkCanvas = new Canvas(pdfCanvas, pdfDoc.getDefaultPageSize()).setFontSize(fontSize);
            //Adding different color to differentiate content in doc and watermark
            watermarkCanvas.setFontColor(new DeviceRgb(0, 0, 255));  // Set color to blue

            // Aligning watermark to the center of every page
            watermarkCanvas.showTextAligned("Owned By: John Doe",
                    pdfDoc.getDefaultPageSize().getWidth() / 2,  // Center horizontally
                    pdfDoc.getDefaultPageSize().getHeight() / 2, // Center vertically
                    TextAlignment.CENTER,
                    VerticalAlignment.MIDDLE,
                    rotation);
            // Closing the watermarkCanvas after it has painted the pdfs
            watermarkCanvas.close();
        }
        
        pdfDoc.close();  // Save the changes and close the document
    }
}