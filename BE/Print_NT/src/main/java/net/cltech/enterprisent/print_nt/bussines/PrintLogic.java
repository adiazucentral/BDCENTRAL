package net.cltech.enterprisent.print_nt.bussines;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import net.cltech.enterprisent.print_nt.App;
import net.cltech.enterprisent.print_nt.bussines.connectors.TrayConnector;
import net.cltech.enterprisent.print_nt.bussines.domain.Attachment;
import net.cltech.enterprisent.print_nt.bussines.domain.PrintConfiguration;
import net.cltech.enterprisent.print_nt.bussines.domain.PrintNode;
import net.cltech.enterprisent.print_nt.bussines.domain.ReportOrder;
import net.cltech.enterprisent.print_nt.bussines.persistence.NTNodePersistence;
import net.cltech.enterprisent.print_nt.bussines.persistence.NTPersistence;
import net.cltech.enterprisent.print_nt.bussines.storage.Storage;
import net.cltech.enterprisent.print_nt.forms.FrmMain;
import net.cltech.enterprisent.print_nt.tools.Log;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

/**
 *
 * @author equijano
 */
public class PrintLogic
{

    private final Gson gson;
    private final NTNodePersistence node;
    private final NTPersistence nt;
    private final App app;
    private final PrintConfiguration printConfiguration;
    private final Email email;
    private final String directoryTemp;
    private final FrmMain main;

    public PrintLogic(NTNodePersistence node, App app, NTPersistence nt, PrintConfiguration printConfiguration, FrmMain main)
    {
        this.gson = new Gson();
        this.node = node;
        this.app = app;
        this.nt = nt;
        this.printConfiguration = printConfiguration;
        this.email = new Email(nt, app, printConfiguration);
        this.directoryTemp = Log.pathDir() + "temp\\";
        this.main = main;
    }

    /**
     * Metodos para imprimir etiquetas de codigo de barras directamente
     *
     * @param code
     * @param typePrinter
     */
    public void print(String code, int typePrinter) throws Exception
    {
        try
        {
            main.changeOrderNumber("");
            Properties properties = Storage.loadConfigurationCredentials();

            PrintService pri = findPrintService(properties.getProperty(typePrinter == 0 ? Storage.PRINTERLABEL : Storage.PRINTERREPORT));
            if (typePrinter == 0)
            {
                barcodePrint(pri, code);
            } else
            {

                PrintNode print = gson.fromJson(code, new TypeToken<PrintNode>()
                {
                }.getType());

                final List<ReportOrder> reports = new ArrayList<>();
                PrintNode printAux = new PrintNode(print.getLabelsreport(), print.getVariables(), print.isAttached(), print.getEncryptionReportResult(), properties.getProperty(Storage.SERVERBACKEND));

                boolean encryptReport = (printAux.getEncryptionReportResult() == true && print.getPrintingMedium() == 3) ? true : false;

                if (print.getBufferReport() != null)
                {
                    byte[] decodedBytes = Base64.getDecoder().decode(print.getBufferReport());
                    String nameFile = createReport(decodedBytes, null, false, print.getNameFile(), encryptReport, null);
                    reports.add(new ReportOrder(Long.parseLong(print.getOrder()), nameFile, print.getNameFile()));
                    if (print.getPrintingMedium() == 3 && (print.getSendEmail() == 1 || print.getSendEmail() == 3))
                    {
                        try
                        {
                            if (!print.getPatientEmail().isEmpty())
                            {
                                if (!(Integer.parseInt(properties.getProperty(Storage.CREATEFILEBYEMAIL)) > 0))
                                {
                                    email.sendEmail(Arrays.asList(print.getPatientEmail()), print.getEmailBody(), print.getEmailSubjectPatient(), nameFile, print.getNameFile() + ".pdf");
                                } else
                                {
                                    email.sendEmail(print, nameFile);
                                }

                                app.showMessage("Se envio un correo a " + print.getPatientEmail(), TrayConnector.INFO_MESSAGE);

                            }
                        } catch (Exception ex)
                        {

                            Logger.getLogger(PrintLogic.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else
                {
                    print.getPrintOrder().getListOrders().stream().forEach(o ->
                    {
                        printAux.setOrder(o.getJsonOrder());
                        try
                        {
                            String nameFile = createReport(node.buildReport(gson.toJson(printAux)).getData(), o.getAttachments(), printAux.isAttached(), o.getNameFile(), encryptReport, o.getPatientHistory());
                            reports.add(new ReportOrder(o.getOrderNumber(), nameFile));
                            if (print.getPrintingMedium() == 3 && (print.getSendEmail() == 1 || print.getSendEmail() == 3))
                            {
                                try
                                {

                                    if (!o.getPatientEmail().isEmpty())
                                    {
                                        email.sendEmail(Arrays.asList(o.getPatientEmail()), print.getEmailBody(), print.getEmailSubjectPatient(), nameFile, o.getNameFile() + ".pdf");
                                        app.showMessage("Se envio un correo a " + o.getPatientEmail(), TrayConnector.INFO_MESSAGE);
                                    }
                                } catch (Exception ex)
                                {
                                    Logger.getLogger(PrintLogic.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (IOException | HttpException ex)
                        {
                            Log.error(ex);
                            app.showMessage("Error al imprimir", TrayConnector.ERROR_MESSAGE);
                        }
                    });
                }
                Log.info("PrintingMedium()" + print.getPrintingMedium());
                switch (print.getPrintingMedium())
                {
                    case 1:
                        //Impresion
                        reports.stream().forEach(r ->
                        {
                            main.changeOrderNumber(r.getOrder().toString());
                            reportPrint(pri, print.getNumberCopies(), r.getReport());
                        });
                        break;
                    case 2:
                        //PDF
                        if (reports.size() > 1)
                        {
                            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                            String nameZip2 = format.format(new Date()) + ".zip";
                            createFilesPdf(reports, properties.getProperty(Storage.DIRECTORY) + "\\" + nameZip2);
                        } else
                        {
                            try
                            {

                                String folder = properties.getProperty(Storage.DIRECTORY);
                                Log.info("orden" + reports.get(0).getOrder());
                                Log.info("nombre" + reports.get(0).getName());
                                File inFile = new File(reports.get(0).getReport());
                                File outFile = new File(folder + "\\" + reports.get(0).getName() + ".pdf");

                                FileInputStream in = new FileInputStream(inFile);
                                FileOutputStream out = new FileOutputStream(outFile);

                                int c;
                                while ((c = in.read()) != -1)
                                {
                                    out.write(c);
                                }

                                in.close();
                                out.close();
                                app.showMessage("Se descargo un compilado", TrayConnector.INFO_MESSAGE);
                            } catch (IOException e)
                            {
                                app.showMessage("Problemas al general el archivo " + e, TrayConnector.INFO_MESSAGE);
                            }
                        }
                        break;
                    case 3:
                        Log.info("email medico" + print.getPhysicianEmail());
                        //Email
                        String emailPhysician = "";
                        if (print.getBufferReport() != null && print.getPrintOrder() != null)
                        {
                            emailPhysician = print.getPrintOrder().getPhysician().getEmail();
                        } else
                        {
                            emailPhysician = print.getPhysicianEmail();
                        }
                        Log.info("email medico" + emailPhysician);
                        if ((print.getSendEmail() == 2 || print.getSendEmail() == 3) && !"".equals(emailPhysician))
                        {
                            if (emailPhysician != null)
                            {
                                
                                if(reports.size() > 1) {
                                    SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String nameZip = format2.format(new Date()) + ".zip";
                                    createFilesPdf(reports, directoryTemp + "\\" + nameZip);
                                    email.sendEmail(Arrays.asList(emailPhysician), print.getEmailBody(), print.getEmailSubjectPhysician(), directoryTemp + "\\" + nameZip, nameZip);
                                    app.showMessage("Se envio un correo a " + emailPhysician, TrayConnector.INFO_MESSAGE);
                                } else {
                                    byte[] decodedBytes = Base64.getDecoder().decode(print.getBufferReport());
                                    String nameFile = createReport(decodedBytes, null, false, print.getNameFile(), encryptReport, null);
                                    reports.add(new ReportOrder(Long.parseLong(print.getOrder()), nameFile, print.getNameFile()));
                                    email.sendEmail(Arrays.asList(emailPhysician), print.getEmailBody(), print.getEmailSubjectPhysician(), nameFile, print.getNameFile() + ".pdf");
                                }
                            }
                        }
                        
                        break;
                    default:
                        break;
                }
            }

        } catch (PrintException | InterruptedException | IOException ex)
        {
            Log.error(ex);
            app.showMessage("Error al imprimir", TrayConnector.ERROR_MESSAGE);
        } finally
        {
            deleteFolderOrFile(directoryTemp);
        }
    }

    private void createFilesPdf(List<ReportOrder> reports, String nameZip)
    {
        try
        {
            ZipOutputStream zous = new ZipOutputStream(new FileOutputStream(nameZip));
            reports.stream().forEach(r ->
            {
                try
                {
                    //nombre con el que se va guardar el archivo dentro del zip
                    ZipEntry entrada = new ZipEntry(r.getReport().replace(directoryTemp, ""));
                    zous.putNextEntry(entrada);
                    //obtiene el archivo para irlo comprimiendo
                    FileInputStream fis = new FileInputStream(r.getReport());
                    int leer;
                    byte[] buffer = new byte[1024];
                    while (0 < (leer = fis.read(buffer)))
                    {
                        zous.write(buffer, 0, leer);
                    }
                    fis.close();
                    zous.closeEntry();
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            });
            zous.close();
        } catch (FileNotFoundException ex)
        {
            Log.error(ex);
        } catch (IOException ex)
        {
            Log.error(ex);
        }
    }

    /**
     * Buscar el servicio de la impresora por nombre
     *
     * @param printerName
     * @return
     */
    private PrintService findPrintService(String printerName)
    {
        printerName = printerName.toLowerCase();
        PrintService service = null;
        PrintService services[] = PrinterJob.lookupPrintServices();
        for (int index = 0; service == null && index < services.length; index++)
        {
            if (services[index].getName().toLowerCase().contains(printerName))
            {
                service = services[index];
            }
        }
        return service;
    }

    /**
     * Enviar a imprimir la etiqueta
     *
     * @param printer
     * @param cod
     * @throws PrintException
     * @throws InterruptedException
     */
    private void barcodePrint(PrintService printer, String cod) throws PrintException, InterruptedException
    {
        Log.info("Impresion de etiqueta: " + cod);
        cod = cod.replace("à", "a");
        cod = cod.replace("è", "e");
        cod = cod.replace("ì", "i");
        cod = cod.replace("ò", "o");
        cod = cod.replace("ù", "u");

        cod = cod.replace("á", "a");
        cod = cod.replace("é", "e");
        cod = cod.replace("í", "i");
        cod = cod.replace("ó", "o");
        cod = cod.replace("ú", "u");

        cod = cod.replace("Á", "A");
        cod = cod.replace("É", "E");
        cod = cod.replace("Í", "I");
        cod = cod.replace("Ó", "O");
        cod = cod.replace("Ú", "U");

        cod = cod.replace("Ñ", "N");
        cod = cod.replace("ñ", "n");

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        DocPrintJob job = printer.createPrintJob();
        StringBuilder buffer = new StringBuilder();
        buffer.append(cod);
        Doc doc = new SimpleDoc(buffer.toString().getBytes(), flavor, null);
        job.print(doc, null);
        Thread.sleep(20L);
    }

    /**
     * Crear archivos en pdf
     *
     * @param cod
     * @param attachments
     * @param attachment
     * @param nameFile
     * @return
     */
    private String createReport(byte[] cod, List<Attachment> attachments, boolean attachment, String nameFile, boolean encryptReport, String patientHistory)
    {
        OutputStream out = null;
        String nameTemp = "";
        String directory = directoryTemp + nameFile + "\\";
        try
        {
            File directionTemp = new File(directory);
            if (!directionTemp.exists())
            {
                directionTemp.mkdirs();
            }

            if (attachment)
            {
                nameTemp = directoryTemp + nameFile + "\\" + nameFile + ".pdf";
                out = new FileOutputStream(nameTemp);
                out.write(cod);
                out.close();
                createAttachment(attachments, directory, nameFile, directoryTemp, encryptReport, patientHistory);
            } else
            {
                nameTemp = directoryTemp + nameFile + ".pdf";
                out = new FileOutputStream(nameTemp);
                out.write(cod);
            }
        } catch (FileNotFoundException ex)
        {
            Log.error(ex);
        } catch (IOException ex)
        {
            Log.error(ex);
        } finally
        {
            try
            {
                out.close();
            } catch (IOException ex)
            {
                Log.error(ex);
            }
        }
        return directoryTemp + nameFile + ".pdf";
    }

    private void createAttachment(List<Attachment> attachments, String routeAbolsute, String nameFile, String fileFinal, boolean encryptReport, String patientHistory)
    {
        try
        {
            List<InputStream> pdfs = new ArrayList<>();
            pdfs.add(new FileInputStream(routeAbolsute + nameFile + ".pdf"));
            File root = new File(routeAbolsute);
            Document document = new Document();
            for (int i = 0; i < attachments.size(); i++)
            {
                if (attachments.get(i).getExtension().toLowerCase().equals("pdf"))
                {
                    OutputStream out = new FileOutputStream(routeAbolsute + attachments.get(i).getName() + ".pdf");
                    byte[] decodedBytes = Base64.getDecoder().decode(attachments.get(i).getFile());
                    out.write(decodedBytes);
                    out.close();
                } else
                {
                    PdfWriter.getInstance(document, new FileOutputStream(new File(root, attachments.get(i).getName() + ".pdf")));
                    document.open();
                    document.newPage();
                    byte[] decodedBytes = Base64.getDecoder().decode(attachments.get(i).getFile());
                    Image image = Image.getInstance(decodedBytes);
                    document.add(image);
                    document.close();
                }
                pdfs.add(new FileInputStream(root + "\\" + attachments.get(i).getName() + ".pdf"));
            }
            OutputStream output = new FileOutputStream(fileFinal + nameFile + ".pdf");
            String path = fileFinal + nameFile + ".pdf";
            concatPDFs(pdfs, output, true, encryptReport, patientHistory);
        } catch (FileNotFoundException ex)
        {
            Log.error(ex);
        } catch (DocumentException | IOException ex)
        {
            Log.error(ex);
        } finally
        {
            deleteFolderOrFile(routeAbolsute);
        }
    }

    /**
     * Funcion que concatena los archivos pdfs
     *
     * @param streamOfPDFFiles
     * @param outputStream
     * @param paginate
     * @param printingMedium
     */
    public static void concatPDFs(List<InputStream> streamOfPDFFiles,
            OutputStream outputStream, boolean paginate, boolean encryptReport, String patientHistory)
    {
        Document document = new Document();
        try
        {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();
            while (iteratorPDFs.hasNext())
            {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }

            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            if (encryptReport == true)
            {
                writer.setEncryption("cltech".getBytes(), patientHistory.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            }

            document.open();
            PdfContentByte cb = writer.getDirectContent();

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            while (iteratorPDFReader.hasNext())
            {
                PdfReader pdfReader = iteratorPDFReader.next();

                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages())
                {
                    Rectangle rectangle = pdfReader.getPageSizeWithRotation(1);
                    document.setPageSize(rectangle);
                    document.newPage();

                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader,
                            pageOfCurrentReaderPDF);
                    switch (rectangle.getRotation())
                    {
                        case 0:
                            cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                            break;
                        case 90:
                            cb.addTemplate(page, 0, -1f, 1f, 0, 0, pdfReader
                                    .getPageSizeWithRotation(1).getHeight());
                            break;
                        case 180:
                            cb.addTemplate(page, -1f, 0, 0, -1f, 0, 0);
                            break;
                        case 270:
                            cb.addTemplate(page, 0, 1.0F, -1.0F, 0, pdfReader
                                    .getPageSizeWithRotation(1).getWidth(), 0);
                            break;
                        default:
                            break;
                    }
                    if (paginate)
                    {
                        cb.beginText();
                        cb.getPdfDocument().getPageSize();
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }

            outputStream.flush();
            document.close();
            outputStream.close();

        } catch (DocumentException | IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (document.isOpen())
            {
                document.close();
            }
            try
            {
                if (outputStream != null)
                {
                    outputStream.close();
                }
            } catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Enviar a imprimir el reporte
     *
     * @param printer
     * @param cod
     * @param numberCopies
     */
    private void reportPrint(PrintService printer, int numberCopies, String file)
    {
        try
        {
            if (!file.equals(""))
            {
                FileInputStream fis = new FileInputStream(file);
                PDDocument pdf = PDDocument.load(fis);
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintService(printer);
                job.setPageable(new PDFPageable(pdf));
                for (int i = 0; i < numberCopies; i++)
                {
                    job.print();
                }
                fis.close();
                app.showMessage("Se realizo una impresion", TrayConnector.INFO_MESSAGE);
                Thread.sleep(20L);
            }
        } catch (InterruptedException | FileNotFoundException | PrinterException ex)
        {
            Log.error(ex);
            app.showMessage("Error al imprimir", TrayConnector.ERROR_MESSAGE);
        } catch (IOException ex)
        {
            Log.error(ex);
            app.showMessage("Error al imprimir", TrayConnector.ERROR_MESSAGE);
        } finally
        {
            deleteFolderOrFile(directoryTemp);
        }
    }

    /**
     * Eliminar archivos temporales
     *
     * @param folderOrFile
     */
    private void deleteFolderOrFile(String folderOrFile)
    {
        File fileTemp = new File(folderOrFile);
        File[] files = fileTemp.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                if (file.isDirectory())
                {
                    deleteFolderOrFile(file.getPath());
                }
                file.delete();
            }
        }
        fileTemp.delete();
    }

}
