package com.dinesh.task.utilities;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.w3c.dom.Node;

import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UploadReader {
	private static final String question_pattern = "^[Q].\\d+[)]";
	private static final String answer_pattern = "^[a-z][)]";
	private static final String canswer_pattern = "^\\*[a-z][)]";
	private static final String sec_pattern = "^[S].\\d+[)]";
	private static File stylesheet = new File("OMML2MML.XSL");
	private static TransformerFactory tFactory = TransformerFactory.newInstance();
	private static StreamSource stylesource = new StreamSource(stylesheet);
	private static String data="";
	private static String file_location="";
	private static List<Question>questions;
	public static String parseToJson(String docLocation, String pictureLocation) throws Exception{
	   	 FileInputStream fis = new FileInputStream(docLocation);
	   	 XWPFDocument xdoc=new XWPFDocument(OPCPackage.open(fis));
		 file_location=pictureLocation;
		 File file=new File(pictureLocation);
		 if(!file.exists())
			 file.mkdir();
//		 List<Question> questions=new ArrayList<Question>();	
		 questions = new ArrayList<Question>();
         List<XWPFParagraph> paragraphList =  xdoc.getParagraphs();
         Iterator<IBodyElement> elements=xdoc.getBodyElementsIterator();
         Pattern s_pattern = Pattern.compile(sec_pattern);
         Pattern q_pattern = Pattern.compile(question_pattern);
         Pattern a_pattern = Pattern.compile(answer_pattern);
         Pattern ca_pattern = Pattern.compile(canswer_pattern);
         int count = 0;	         
         Question qobj=null;
         while(elements.hasNext()) {
        	 
        	 IBodyElement element=elements.next();
        	 String table=""; 
        	 String line="";
        	 String formula="";
        	 String picture="";
        	 if(element instanceof XWPFTable) {        		 
//        		 System.out.println("Table Found " + ++count);
        		 XWPFTable xtable=(XWPFTable) element;
        		 table=extractTableHtml(xtable);
//        		 System.out.println(table);
        	 }   
        	 
        	 if(element instanceof XWPFParagraph) {
//        		 System.out.println("Paragraph Found " + ++count);
        		 XWPFParagraph paragraph=(XWPFParagraph) element;
            	 boolean question_found=false, answer_found=false;		        	 
            	 count++;
            	 line=paragraph.getText();
//            	 System.out.println(line);
            	 Matcher s = s_pattern.matcher(line);
                 Matcher q = q_pattern.matcher(line);
                 Matcher a = a_pattern.matcher(line);
                 Matcher ca = ca_pattern.matcher(line);
                 formula=findAndGetFormula(paragraph);
                 picture=findAndGetPicture(paragraph);
                 
//                 if(s.find()) {
//                	 String section_number=line.substring(s.start(), s.end()-1);
//               	  	 String sectionName=line.substring(s.end(), line.length());
//               	  	 
//               	  	 qobj.setSectionName(sectionName);
//               	  	 qobj.setSectionNumber(section_number);
//               	  	 
//               	  	 questions.add(qobj);
//                 }
//                 
                 if(q.find()) {	
                	  String question_number=line.substring(q.start(), q.end()-1);
                	  String question=line.substring(q.end(), line.length()) + formula + picture + table;
                	  
                	  qobj=new Question(question_number, question);
                	  
                	  questions.add(qobj);
                	  
                	  continue;
                 }
                 if(ca.find()) {   
                	 answer_found=true;
                	 String canswer_number=line.substring(ca.start()+1, ca.end()-1);
                	 String canswer=line.substring(ca.end(), line.length()) + formula + picture + table;

                	 qobj.setCorrectAnswer(new Answer(canswer_number, canswer));
                	 
                	 String answer_number=line.substring(ca.start()+1, ca.end()-1);
                	 String answer=line.substring(ca.end(), line.length()) + formula + picture + table;
                	 
                	 qobj.getAnswers().add(new Answer(answer_number, answer));
                	 
                	 continue;
                 }	             
                 if(a.find()) {	       
                	 answer_found=true;
                	 String answer_number=line.substring(a.start(), a.end()-1);
                	 String answer=line.substring(a.end(), line.length()) + formula + picture + table;
                	 
                	 qobj.getAnswers().add(new Answer(answer_number, answer));
                	 
                	 continue;
                 } 
                 
                
                 
             }
//             if(qobj.getAnswers()!=null)
//             if(qobj.getAnswers().size()==0) {		            	 
//            	 qobj.setQuestion(qobj.getQuestion()+ "<br>" + line + formula + picture + table);
//            	 
//             }else {
//            	 String answer_number=qobj.getAnswers().get(qobj.getAnswers().size()-1).getAnswerNumber();
//            	 String answer = qobj.getAnswers().get(qobj.getAnswers().size()-1).getAnswer() + "<br>" + line + formula + picture + table;		            	 
//            	 qobj.getAnswers().set(qobj.getAnswers().size()-1, new Answer(answer_number, answer));			            	 
//             }        	 
         }

         Gson gson = new GsonBuilder().setPrettyPrinting().create();		         
         String data=gson.toJson(questions);
         return data;
   }
	public String getData() {
		return this.data;
	}
	
	private static String extractTableHtml(XWPFTable table) {
		 String tableHtml="<table border=\"1px\">";		      		 
		 for(XWPFTableRow row : table.getRows()) {
			 tableHtml+="<tr>";
			 for(XWPFTableCell cell: row.getTableCells()) {
				 if(cell.getTables().size()==0) {
					 tableHtml+="<td>"+ cell.getText() + "</td>";	 
				 }else {
					 tableHtml+= "<td>";
					 for(XWPFTable t : cell.getTables()) {						 
						 tableHtml+= extractTableHtml(t);
					 }
					 tableHtml+= "</td>";
				 }
				 
			 }
			 tableHtml+="</tr>";
		 }
		 tableHtml+="</table>";
		 return tableHtml;		 
	 }

	private static String findAndGetPicture(XWPFParagraph paragraph) throws Exception{
		String picture_files="";
		 for(XWPFRun run : paragraph.getRuns()) {
			 List<XWPFPicture> pictures=run.getEmbeddedPictures();
			 for(XWPFPicture pic : pictures) {				 
				 byte[] bytepic = pic.getPictureData().getData();	               
				 BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytepic));
				 UUID uuid2 = Generators.randomBasedGenerator().generate();
				 
				 //Picture Resource Management
				 picture_files+="<img width='50%' src='__dexters_resource_location_9836758498731097845__" + uuid2.toString()+ pic.getPictureData().getFileName() +"?access_token=__dexters_access_token_9836758498731097845__'>";
				 
				 
				 
//				 picture_files+="<img width='50%' src='http://192.168.1.9:8088/uploads/images/" + uuid2.toString()+ pic.getPictureData().getFileName() +"?access_token=dexters_access_token_9836758498731097845'>"; 
//				 picture_files+="<img width='50%' v-auth-img='http://192.168.1.9:8088/uploads/images/" + uuid2.toString()+ pic.getPictureData().getFileName() +"'>";
				 
				 
				 
				 ImageIO.write(imag, "jpg", new File(file_location + "/" + uuid2.toString()+ pic.getPictureData().getFileName()));				 
			 }
		 }		
		return picture_files;		
	}
	private static String findAndGetFormula(XWPFParagraph paragraph) throws Exception{
		String formulas="";
	    for (CTOMath ctomath : paragraph.getCTP().getOMathList()) {
	        formulas=formulas + getMathML(ctomath) + "<br>";
	    }
	    return formulas;
	}
	private static String getMathML(CTOMath ctomath) throws Exception {
		  Transformer transformer = tFactory.newTransformer(stylesource);

		  Node node = ctomath.getDomNode();

		  DOMSource source = new DOMSource(node);
		  StringWriter stringwriter = new StringWriter();
		  StreamResult result = new StreamResult(stringwriter);
		  transformer.setOutputProperty("omit-xml-declaration", "yes");
		  transformer.transform(source, result);

		  String mathML = stringwriter.toString();
		  stringwriter.close();

		  //The native OMML2MML.XSL transforms OMML into MathML as XML having special name spaces.
		  //We don't need this since we want using the MathML in HTML, not in XML.
		  //So ideally we should changing the OMML2MML.XSL to not do so.
		  //But to take this example as simple as possible, we are using replace to get rid of the XML specialities.
		  mathML = mathML.replaceAll("xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"", "");
		  mathML = mathML.replaceAll("xmlns:mml", "xmlns");
		  mathML = mathML.replaceAll("mml:", "");

		  return mathML;
	}
	public static Long getQuestions(Long totalQues) {
		Long noOfQues= (long) questions.size() ;
		if(totalQues>noOfQues) {
			Long totalQuestions = totalQues-noOfQues;
			return totalQuestions;
		} else if(noOfQues>totalQues) {
			Long totalQuestions = noOfQues-totalQues;
			return totalQuestions;
		} else {
			Long totalQuestions = (long) 0;
			return totalQuestions;
		}

	}
	
	public static List<String> getAnswers(Long totalAns) {
		List<String> noOfAns=new ArrayList<String>(0);
		for(int i=0;i<questions.size();i++) {
			if(questions.get(i).getAnswers().size()!=totalAns) {
				noOfAns.add(questions.get(i).getQuestionNumber()+"-"+questions.get(i).getAnswers().size());
				continue;
			}
		}
		return noOfAns;
	}
	
	public static List<String> getCorrectAns() {
		List<String> correctAns=new ArrayList<String>(0);
		for(int i=0;i<questions.size();i++) {
			if(questions.get(i).getCorrectAnswer()==null) {
				correctAns.add(questions.get(i).getQuestionNumber());
				continue;
			}
		}
		return correctAns;
		
	}
	
	public static List<Question> getQuestionList() {
		
		return questions;
		
	}
		
} 

