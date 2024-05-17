/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.tools;

/**
 *
 * @author hpoveda
 */
public class HtmlToTxt {

    public static String htmlToString(String html) {
        
        
        String tagP = "</p>";
        String tagH2 = "</h2>";
        String tagA = "</a>";
        if (html.contains(tagP)) {
            html = html.trim().replace("</p>", "<br />").replace("<p>", "");
        }
        if (html.contains(tagH2)) {
            html = html.trim().replace("</h2>", "<br />").replace("<h2>", "");
        }
        if (html.contains(tagA)) {
            html = html.trim().replace("</a>", "<br />").replace("<a>", "");
        }
        String[] txt = html.trim().replace("\\n", "<br />").replace("<br />", "\n").replace("&nbsp;", " ").replace("&iacute;", "í").replace("&ntilde;", "ñ").replace("<div>", "").split("</div>");
        StringBuilder txtConver = new StringBuilder();
        for (String string : txt) {
            txtConver.append(string);
        }
        return txtConver.toString().replaceAll("\\<.*?\\>", " "); //txtConver.toString();

    }
    
    

}
