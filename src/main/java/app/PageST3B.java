package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageST3B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3B.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html += """
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Subtask 3.2</title>
                """;

        // Add some CSS (external file)
        html += """
            <link rel='stylesheet' type='text/css' href='common.css' />
            <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css">
            <link rel="shortcut icon" href="/draft.ico" type="image/x-icon">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css" />
            <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
                """;
        html = html + "</head>";

        // Add the body
        html = html + "<body class='task3b-body'>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html += """
            <header>
            <div class="logo">
                <a href="/"><img src="logo.png" alt=""></a>
            </div>
            <ul id="navbar">
                <li><a href="/">Home Page</a></li>
                <li><a href="mission.html">Our Mission</a></li>
                <li><a href="page2A.html">Age & Health</a></li>
                <li><a href="page2B.html">Education</a></li>
                <li><a href="page3A.html">The Gap</a></li>
                <li><a href="page3B.html" class="active">LGA Comparison</a></li>
            </ul>
            </header>
                """;

            // hero image
            html += """
            <section id="the-lga-header">
                <div class="overlay"></div>
                <div class="overlay-text">
                    <h2>Finding LGAs with Similar Characteristics</h2>
                    <p>Find unbiased information about similar LGAs that are in similar situations</p>
                </div>
            </section>
                """;   

                // Form
                html = html + """
                    <form action="/page3B.html" method="post">
                        <h2>Select A Database to view</h2>
                        <div>
                            <input type="radio" id="rad" name="DataBase1" value="LTHC">
                            <label for="DataBase">Long Term Health</label>
                        </div>
                        <div>
                            <input type="radio" id="rad" name="DataBase1" value="Age_Cat">
                            <label for="Database">Age Category</label>
                        </div>
                        <div>
                            <input type="radio" id="rad" name="DataBase1" value="school">
                            <label for="DataBase">School Year</label>
                        </div>
                        <div>
                            <input type="radio" id="rad" name="DataBase1" value="non_school">
                            <label for="Database">Non School Education</label>
                        </div>
                            <input type="submit" value="Submit">
                    </form>
                    """;
                
                    //Code for Selector
                    String edu_min = "7";
                    String edu_max = "12";
                    ArrayList<String> School1_2 = new ArrayList<String>();
                    String DataBase1_1 = context.formParam("DataBase1");
                    if (DataBase1_1 == null) {
                        html = html + "";
                    } else if (DataBase1_1.equals("LTHC")) {
                        html = html + outputDataBase1_1(DataBase1_1, 2021);
                    } else if (DataBase1_1.equals("Age_Cat")) {
                        html = html + outputDataBase1_2(DataBase1_1);
                    } else if (DataBase1_1.equals("school")) {
                        html += """
                            <form action="/page3B.html" method="post">
                            <h2>Did the user go to School?</h2>
                            <div>
                                <input type="radio" id="rad2" name="School_go" value="yes">
                                <label for="DataBase">Yes</label>
                            </div>
                            <div>
                                <input type="radio" id="rad2" name="School_go" value="no">
                                <label for="Database">No</label>
                            </div>
                                <input type="submit" value="Submit">
                            </form>
                            """;
                    } else if (DataBase1_1.equals("non_school")) {
                        html = html + outputDataBase1_4(DataBase1_1, 2021);
                    } else {
                        html = html + "";
                    }
    
                    String School_go = context.formParam("School_go");
                    if (School_go == null) {
                        html += "";
                    } else if (School_go.equals("no")) {
                        edu_min = "7";
                        html = html + outputDataBase1_3();
                    } else if (School_go.equals("yes")) {
                        html = html + outputDataBase1_3_2();
                    }
    
                    if (context.formParam("edu_min") != null) {
                        edu_min = context.formParam("edu_min");
                        edu_max = context.formParam("edu_max");
                        School1_2 = getSchoolYear(Integer.valueOf(edu_min), Integer.valueOf(edu_max));
                    }
    
                    String indig1 = context.formParam("indige1");
                    String indig2 = context.formParam("indige2");
                    String indig3 = context.formParam("indige3");
                    String indig4 = context.formParam("indige4");
                    String sex = context.formParam("sex");
                    String sort = context.formParam("sort");
                    String sort2 = context.formParam("sort2");
                    String min = context.formParam("pop_min");
                    String max = context.formParam("pop_max");
                    String year = context.formParam("year");
                    String lga = context.formParam("LGA");
                    String max_lga = context.formParam("max_lga");
    
                    // Getting the User Choosed LTHC and placing them in LTHC2
                    ArrayList<String> LTHC1 = new ArrayList<String>();
                    ArrayList<String> LTHC2 = new ArrayList<String>();
                    LTHC1 = getLTHC(2021);
                    String LTHC;
                    for (int i = 0; i < LTHC1.size(); i++) {
                        LTHC = context.formParam(LTHC1.get(i));
                        if (LTHC != null) {
                            LTHC2.add(LTHC);
                        }
                    }
    
                    // Getting the User Choosed NonSCL and placing them in NonSCLALL
                    ArrayList<String> NonSCL = new ArrayList<String>();
                    ArrayList<String> NonSCLALL = new ArrayList<String>();
                    NonSCL = getNonSCL(2016);
                    String NonSCL10;
                    for (int i = 0; i < NonSCL.size(); i++) {
                        NonSCL10 = context.formParam(NonSCL.get(i));
                        if (NonSCL10 != null) {
                            NonSCLALL.add(NonSCL10);
                        }
                    }
    
                    // Code for generating the LTHC table
                    if (indig1 == null) {
                        html = html + "";
                    } else {
                        // Heading code
                        html += "<h3>Comparison LGA data " + "For " + getIndig(indig1) + " where Long Term Health condition is ";
                        for (int j = 0; j < LTHC2.size(); j++) {
                            if (LTHC2.size() - 1 != j) {
                                html = html + " " + LTHC2.get(j) + ",";
                            } else {
                                html = html + " " + LTHC2.get(j);
                            }
                        }
                        html +=  " and sex is " + getSex(sex) + " Ranked by " + getSort(sort) + "</h3>";
                        String query1 = "select lga.name as name1, lga.code as code1, LTHC.Long_Term as ED1, sum(LTHC.count) as count1, sex as s1 from LTHC left join lga on (LTHC.lga_code = lga.code and LTHC.lga_year = lga.year)";
                        query1 += " where LTHC.lga_year = 2021 ";
                        if (!indig1.equals("both")) {
                            query1 += " and indigenous_status = " + "'" + indig1 + "' ";
                        }
                        query1 += "  and Ed1 in ( ";
                        for (int i = 0; i < LTHC2.size(); i++) {
                            if (LTHC2.size() - 1 != i) {
                                query1 = query1 + "'" + LTHC2.get(i) + "'" + ",";
                            } else {
                                query1 = query1 + "'" + LTHC2.get(i) + "'";
                            }
                        }
                        query1 += " ) "; 
                        if (!sex.equals("both")) {
                            query1 += " and s1 = " + "'" + sex + "'";
                        } 
                        query1 += " and name1 = " + "'" + lga + "'";
                        query1 += " group by code1 " + ";";
                        int num = getTableI(query1, "count1");
                        String query2 = "with Indig2021 as ( ";
                            query2 += " select lga.name as name2, lga.code as code2, sum(LTHC.count) as count2, sex as s2, abs(sum(LTHC.count) - "+ num +") as count2_2 from LTHC left join lga on (LTHC.lga_code = lga.code and LTHC.lga_year = lga.year) ";
                            query2 += " where LTHC.lga_year = 2021 ";
                            if (!indig1.equals("both")) {
                                query2 += " and indigenous_status = " + "'" + indig1 + "' ";
                            }
                            query2 += " and LTHC.Long_Term in ( ";
                            for (int i = 0; i < LTHC2.size(); i++) {
                                if (LTHC2.size() - 1 != i) {
                                    query2 = query2 + "'" + LTHC2.get(i) + "'" + ",";
                                } else {
                                    query2 = query2 + "'" + LTHC2.get(i) + "'";
                                }
                            }
                            query2 += " ) and name2 is not " + "'" + lga + "'";
                            if (!sex.equals("both")) {
                                query2 += " and sex = " + "'" + sex + "'";
                            }
                            query2 += " group by code2 order by count2_2 "+ sort2;
                            query2 += " limit " + max_lga +")";
                                
                            query2 += " select name2, code2, count2 from Indig2021 order by count2 " + sort + ";";
                        html += "<h3>The User choosen LGA is " + lga + " with population of " + num + " sorted by " + getSort2(sort2) + " with list of " + max_lga + " results" + "</h3>";
                        ArrayList<String> LTHC_lga_code1 = new ArrayList<String>();
                        ArrayList<Integer> LTHC_lga_code2 = new ArrayList<Integer>();
                        ArrayList<Integer> LTHC_lga_value = new ArrayList<Integer>();
                        LTHC_lga_code1 = getTableS(query2, "name2");
                        LTHC_lga_code2 = getTable(query2, "code2");
                        LTHC_lga_value = getTable(query2, "count2");
                        html += CreateTableLTHC(LTHC_lga_code1,LTHC_lga_code2,LTHC_lga_value);
                    }
                    
                    // Non_School Eduation
                    if (indig4 == null) {
                        html += "";
                    } else {
                        html += "<h3>Comparison LGA data " + "For " + getIndig(indig4) + " where Non-School Education is ";
                        for (int j = 0; j < NonSCLALL.size(); j++) {
                            if (NonSCLALL.size() - 1 != j) {
                                html = html + " " + NonSclConvert1(NonSCLALL.get(j)) + ",";
                            } else {
                                html = html + " " + NonSclConvert1(NonSCLALL.get(j));
                            }
                        }
                        html += " and sex is " + getSex(sex) + " Ranked by " + getSort(sort) + "</h3>";
                        String query1 = "select lga.name as name1, lga.code as code1, Non_School_ED.education as ED1, sum(Non_School_ED.count) as count1, sex as s1 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year)";
                        query1 += " where Non_School_ED.lga_year = " + year;
                        if (!indig4.equals("both")) {
                            query1 += " and indigenous_status = " + "'" + indig4 + "' ";
                        }
                        query1 += "  and Ed1 in ( ";
                        for (int i = 0; i < NonSCLALL.size(); i++) {
                            if (NonSCLALL.size() - 1 != i) {
                                query1 = query1 + "'" + NonSCLALL.get(i) + "'" + ",";
                            } else {
                                query1 = query1 + "'" + NonSCLALL.get(i) + "'";
                            }
                        }
                        query1 += " ) "; 
                        if (!sex.equals("both")) {
                            query1 += " and s1 = " + "'" + sex + "'";
                        } 
                        query1 += " and name1 = " + "'" + lga + "'";
                        query1 += " group by code1 " + ";";
                        int num = getTableI(query1, "count1");
                        String query2 = "with Indig2021 as ( ";
                            query2 += " select lga.name as name2, lga.code as code2, sum(Non_School_ED.count) as count2, sex as s2, abs(sum(Non_School_ED.count) - "+ num +") as count2_2 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) ";
                            query2 += " where Non_School_ED.lga_year = " + year;
                            if (!indig4.equals("both")) {
                                query2 += " and indigenous_status = " + "'" + indig4 + "' ";
                            }
                            query2 += " and Non_School_ED.education in ( ";
                            for (int i = 0; i < NonSCLALL.size(); i++) {
                                if (NonSCLALL.size() - 1 != i) {
                                    query2 = query2 + "'" + NonSCLALL.get(i) + "'" + ",";
                                } else {
                                    query2 = query2 + "'" + NonSCLALL.get(i) + "'";
                                }
                            }
                            query2 += " ) and name2 is not " + "'" + lga + "'";
                            if (!sex.equals("both")) {
                                query2 += " and sex = " + "'" + sex + "'";
                            }
                            query2 += " group by code2 order by count2_2 "+ sort2;
                            query2 += " limit " + max_lga + ")";
                                
                            query2 += " select name2, code2, count2 from Indig2021 order by count2 " + sort + ";";
                            html += "<h3>The User choosen LGA is " + lga + " with population of " + num + " sorted by " + getSort2(sort2) + " with list of " + max_lga + " results" + "</h3>";
                        ArrayList<String> LTHC_lga_code1 = new ArrayList<String>();
                        ArrayList<Integer> LTHC_lga_code2 = new ArrayList<Integer>();
                        ArrayList<Integer> LTHC_lga_value = new ArrayList<Integer>();
                        LTHC_lga_code1 = getTableS(query2, "name2");
                        LTHC_lga_code2 = getTable(query2, "code2");
                        LTHC_lga_value = getTable(query2, "count2");
                        html += CreateTableLTHC(LTHC_lga_code1,LTHC_lga_code2,LTHC_lga_value);

                    }
                    
                    // Population
                    if (indig2 == null) {
                        html += "";
                    } else {
                        if (Integer.valueOf(min) > Integer.valueOf(max)) {
                            html += "<h2> Please select a Minmum age that is smaller than the Maximum age </h2>";
                        } else {
                            html += "<h3> Comparison LGA data " + "For " + getIndig(indig2) + " where Age range is between " + min + " and " + max + " and sex is " + getSex(sex) + " Ranked by " + getSort(sort) + "</h3>";
                            String query1 = "select lga.name as name1, lga.code as code1, sum(population.count) as count1, sex as s1 from population left join lga on (population.lga_code = lga.code and population.lga_year = lga.year)";
                        query1 += " where population.lga_year = " + year;
                        if (!indig2.equals("both")) {
                            query1 += " and indigenous_status = " + "'" + indig2 + "' ";
                        }
                        query1 += " and Age_min >= " + min + " and Age_max <= " + max;
                        if (!sex.equals("both")) {
                            query1 += " and s1 = " + "'" + sex + "'";
                        } 
                        query1 += " and name1 = " + "'" + lga + "'";
                        query1 += " group by code1 " + ";";
                        int num = getTableI(query1, "count1");
                        String query2 = "with Indig2021 as ( ";
                            query2 += " select lga.name as name2, lga.code as code2, sum(population.count) as count2, sex as s2, abs(sum(population.count) - "+ num +") as count2_2 from population left join lga on (population.lga_code = lga.code and population.lga_year = lga.year) ";
                            query2 += " where population.lga_year = " + year;
                            if (!indig2.equals("both")) {
                                query2 += " and indigenous_status = " + "'" + indig2 + "' ";
                            }
                            query2 += " and Age_min >= " + min + " and Age_max <= " + max;
                            query2 += "  and name2 is not " + "'" + lga + "'";
                            if (!sex.equals("both")) {
                                query2 += " and sex = " + "'" + sex + "'";
                            }
                            query2 += " group by code2 order by count2_2 "+ sort2;
                            query2 += " limit " + max_lga + ")";
                                
                            query2 += " select name2, code2, count2 from Indig2021 order by count2 " + sort + ";";
                            html += "<h3>The User choosen LGA is " + lga + " with population of " + num + " sorted by " + getSort2(sort2) + " with list of " + max_lga + " results" + "</h3>";
                        ArrayList<String> LTHC_lga_code1 = new ArrayList<String>();
                        ArrayList<Integer> LTHC_lga_code2 = new ArrayList<Integer>();
                        ArrayList<Integer> LTHC_lga_value = new ArrayList<Integer>();
                        LTHC_lga_code1 = getTableS(query2, "name2");
                        LTHC_lga_code2 = getTable(query2, "code2");
                        LTHC_lga_value = getTable(query2, "count2");
                        html += CreateTableLTHC(LTHC_lga_code1,LTHC_lga_code2,LTHC_lga_value);
                        }
                    }
    
                    // School Year
                    if (indig3 == null) {
                        html += "";
                    } else if (edu_min == null){
                        html += "";
                    } else {
                        if (Integer.valueOf(edu_min) > Integer.valueOf(edu_max)) {
                            html += "<h2> Please select a Minmum School year that is smaller than the Maximum School year </h2>";
                        } else {
                            String query1 = "select lga.name as name1, lga.code as code1, sum(SchoolYear.count) as count1, sex as s1 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year)";
                        query1 += " where SchoolYear.lga_year = " + year;
                        if (!indig3.equals("both")) {
                            query1 += " and indigenous_status = " + "'" + indig3 + "' ";
                        }
                        query1 += " and SchoolYear.School_Year in ( ";
                        html += "<h3>Comparison LGA data " + "For " + getIndig(indig3);
                                if (Integer.valueOf(edu_min) == 7) {
                                    query1 += "'" + "did_not_go_to_school" + "'";
                                    html += " where the Population Did not got to School";
                                } else {
                                    html += " where School year is  between " + edu_min + " and " + edu_max;
                                    for (int i = 0; i < School1_2.size(); i++) {
                                        if (School1_2.size() - 1 != i) {
                                            query1 = query1 + "'" + School1_2.get(i) + "'" + ",";
                                        } else {
                                            query1 = query1 + "'" + School1_2.get(i) + "'";
                                        }  
                                    }
                                }
                        query1 += " ) ";
                        if (!sex.equals("both")) {
                            query1 += " and s1 = " + "'" + sex + "'";
                        } 
                        query1 += " and name1 = " + "'" + lga + "'";
                        query1 += " group by code1 " + ";";
                        int num = getTableI(query1, "count1");
                        String query2 = "with Indig2021 as ( ";
                            query2 += " select lga.name as name2, lga.code as code2, sum(SchoolYear.count) as count2, sex as s2, abs(sum(SchoolYear.count) - "+ num +") as count2_2 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) ";
                            query2 += " where SchoolYear.lga_year = " + year;
                            if (!indig3.equals("both")) {
                                query2 += " and indigenous_status = " + "'" + indig3 + "' ";
                            }
                            query2 += " and SchoolYear.School_Year in ( ";
                                if (Integer.valueOf(edu_min) == 7) {
                                    query2 += "'" + "did_not_go_to_school" + "'";
                                } else {
                                    for (int i = 0; i < School1_2.size(); i++) {
                                        if (School1_2.size() - 1 != i) {
                                            query2 = query2 + "'" + School1_2.get(i) + "'" + ",";
                                        } else {
                                            query2 = query2 + "'" + School1_2.get(i) + "'";
                                        }  
                                    }
                                }
                        query2 += " ) ";
                            query2 += "  and name2 is not " + "'" + lga + "'";
                            if (!sex.equals("both")) {
                                query2 += " and sex = " + "'" + sex + "'";
                            }
                            query2 += " group by code2 order by count2_2 "+ sort2;
                            query2 += " limit " + max_lga + ")";
                            query2 += " select name2, code2, count2 from Indig2021 order by count2 " + sort + ";";
                        html += " and sex is " + getSex(sex) + " Ranked by " + getSort(sort) + "</h3>";
                        html += "<h3>The User choosen LGA is " + lga + " with population of " + num + " sorted by " + getSort2(sort2) + " with list of " + max_lga + " results" + "</h3>";
                        ArrayList<String> LTHC_lga_code1 = new ArrayList<String>();
                        ArrayList<Integer> LTHC_lga_code2 = new ArrayList<Integer>();
                        ArrayList<Integer> LTHC_lga_value = new ArrayList<Integer>();
                        LTHC_lga_code1 = getTableS(query2, "name2");
                        LTHC_lga_code2 = getTable(query2, "code2");
                        LTHC_lga_value = getTable(query2, "count2");
                        html += CreateTableLTHC(LTHC_lga_code1,LTHC_lga_code2,LTHC_lga_value);
                        }
                    }
                    
        // Footer
        html = html + """
            <footer>
            <div class="footer-container">
                <div class="footer footer-left">
                    <a href="/"><img src="/logo.png" alt="logo"></a>
                </div>
    
                <div class="footer footer-left-02">
                    <ul>
                        <li><a href="/">Home Page</a></li>
                        <li><a href="mission.html">Our Mission</a></li>
                        <li><a href="page2A.html">Age & Health</a></li>
                        <li><a href="page2B.html">Education</a></li>
                        <li><a href="page3A.html">The Gap</a></li>
                        <li><a href="page3B.html">LGA Comparison</a></li>
                    </ul>
                </div>
    
                <div class="footer footer-center">
                    <div>
                        <i class="fa fa-map-marker"></i>
                        <p>Melbourne, Victoria</p>
                    </div>
    
                    <div>
                        <i class="fa fa-phone"></i>
                        <p>+61449990065</p>
                    </div>
                    <div>
                        <i class="fa fa-envelope"></i>
                        <p><a href="mailto:mail@gmail.com">mail@gmail.com</a></p>
                    </div>
                </div>
    
                <div class="footer footer-right">
                    <h4>
                        About the Website
                    </h4>
                    <p>
                        <strong>Voice To Parliament Data Hub</strong> Our website simply give you an idea of the cencus data in 2016 & 2021 related to voice to parliament referendum in a unbiased way. 
                    </p>
                    <div class="footer-icons">
                        <a href="https://www.facebook.com" target="_blank"><img src="facebook.svg"></a>
                        <a href="https://www.instagram.com" target="_blank"><img src="instagram.svg"></a>
                        <a href="https://www.linkedin.com" target="_blank"><img src="linkedin-in.svg"></a>
                        <a href="https://www.twitter.com" target="_blank"><img src="twitter.svg"></a>
                        <a href="https://www.youtube.com" target="_blank"><img src="youtube.svg"></a>
                    </div>
                </div>
            </div>
    
            <p class="footer-copyright">
                Copyright © 2023 | All Rights Reserved <strong> | RMIT</strong> 
            </p>
    
        </footer>
        """;


        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }
    public ArrayList<String> getState(int year) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> lgas = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT Distinct state_name FROM STATE WHERE year= " + year + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String name16  = results.getString("state_name");

                // Add the lga object to the array
                lgas.add(name16);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lgas;
    }
    public ArrayList<String> getLTHC(int year) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> LTHC = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT Distinct Long_Term FROM LTHC WHERE lga_year = " + year + " order by Long_Term;";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String name16  = results.getString("Long_Term");

                // Add the lga object to the array
                LTHC.add(name16);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return LTHC;
    }
    public ArrayList<String> getAgeCat(int year) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> lgas = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT Distinct age_category FROM population WHERE lga_year = " + year + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String name16  = results.getString("age_category");

                // Add the lga object to the array
                lgas.add(name16);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lgas;
    }
    public ArrayList<String> AgeConvert(ArrayList<String> age1){
        ArrayList<String> age2 = new ArrayList<String>();
        String word;
        for (int i = 0; i < age1.size(); i++) {
            if (age1.get(i).equals("_0_4")) {
                word = "0 to 4 Years old";
            } else if (age1.get(i).equals("_5_9")) {
                word = "5 to 9 Years old";
            } else if (age1.get(i).equals("_10_14")) {
                word = "10 to 14 Years old";
            } else if (age1.get(i).equals("_15_19")) {
                word = "15 to 19 Years old";
            } else if (age1.get(i).equals("_20_24")) {
                word = "20 to 24 Years old";
            } else if (age1.get(i).equals("_25_29")) {
                word = "25 to 29 Years old";
            } else if (age1.get(i).equals("_30_34")) {
                word = "30 to 34 Years old";
            } else if (age1.get(i).equals("_35_39")) {
                word = "35 to 39 Years old";
            } else if (age1.get(i).equals("_40_44")) {
                word = "40 to 44 Years old";
            } else if (age1.get(i).equals("_45_49")) {
                word = "45 to 49 Years old";
            } else if (age1.get(i).equals("_50_54")) {
                word = "50 to 54 Years old";
            } else if (age1.get(i).equals("_54_59")) {
                word = "55 to 59 Years old";
            } else if (age1.get(i).equals("_60_64")) {
                word = "60 to 64 Years old";
            } else {
                word = "65 Years old and above";
            }
            age2.add(word);
        }
        return age2;
    }

    public ArrayList<String> getNonSCL(int year) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> nonScl = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT DISTINCT Education FROM Non_School_ED WHERE lga_year = " + year + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String non_scl  = results.getString("Education");

                // Add the lga object to the array
                nonScl.add(non_scl);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return nonScl;
    }
    public String outputDataBase1_1(String type, int Year) {
        String html = "";
        // Select Disease
         html = html + """
            <form action="/page3B.html" method="post">

            <label for="lga">Choose LGA to compare it to</label>
            <select id="drop_down" name="LGA">
                """;
                ArrayList<String> LGA = new ArrayList<String>();
                LGA = getLGA(2021);
                String LGAname;
                
                for (int i = 0; i < LGA.size(); i ++) {
                    LGAname = LGA.get(i);
                    html = html + "<option value= " + LGAname + " > " + LGAname + "</option>";
                }
            html += """
                </select>
            
                <label for="indige1">Indigenous Status:</label>
                <select id="drop_down" name="indige1">
                    <option value="indig">Indigenous</option>
                    <option value="non_indig">Non Indigenous</option>
                    <option value="both">Both</option>
                </select>

                <label for="sex">Gender:</label>
                <select id="drop_down" name="sex">
                    <option value="m">Male</option>
                    <option value="f">Female</option>
                    <option value="both">Both</option>
                </select>

                <label for="sort2">Sorted by:</label>
                <select id="drop_down" name="sort2">
                    <option value="ASC">Most Simular results</option>
                    <option value="DESC">Least Simular results</option>
                </select>

                <label for="sort">Sorted by:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>
                <div>
                <label for="max_lga">Choose number of lga in results:</label>
                <input type="number" required value="" id="max_lga" name="max_lga" min="0" max="563">
                </div>

                <label for="LTHC">Choose a Long Term Health Condition:</label>
            """;
        ArrayList<String> LTHC = new ArrayList<String>();
        LTHC = getLTHC(Year);
        String LTHCname;
        
        for (int i = 0; i < LTHC.size(); i ++) {
            LTHCname = LTHC.get(i);
            html = html + "<div><input type= \"checkbox\" id= \"LTHC1\" name= " + "'" + LTHCname + "'" + " value= " + "'" + LTHCname + "'" +" >"
            + "<label for= \"LTHC\"> " + LTHCname +  "</label></div>";
        }

        html = html + """
            <input type="submit">
            </form>
            """;
        return html;
    }
    public String outputDataBase1_2(String type) {
        String html = "";
        // Select Disease
         html = html + """
            <form action="/page3B.html" method="post">
                
            <label for="lga">Choose LGA to compare it to</label>
            <select id="drop_down" name="LGA">
                """;
                ArrayList<String> LGA = new ArrayList<String>();
                LGA = getLGA(2021);
                String LGAname;
                
                for (int i = 0; i < LGA.size(); i ++) {
                    LGAname = LGA.get(i);
                    html = html + "<option value= " + LGAname + " > " + LGAname + "</option>";
                }
        html = html + """
                </select>

                <label for="year">Choose 2016 or 2021:</label>
                <select id="drop_down" name="year">
                    <option value="2016">2016</option>
                    <option value="2021">2021</option>
                </select>
            
                <label for="indige1">Indigenous Status:</label>
                <select id="drop_down" name="indige2">
                    <option value="indig">Indigenous</option>
                    <option value="non_indig">Non Indigenous</option>
                    <option value="both">Both</option>
                </select>

                <label for="sex">Gender:</label>
                <select id="drop_down" name="sex">
                    <option value="m">Male</option>
                    <option value="f">Female</option>
                    <option value="both">Both</option>
                </select>

                <label for="sort2">Sorted by:</label>
                <select id="drop_down" name="sort2">
                    <option value="ASC">Most Simular results</option> 
                    <option value="DESC">Least Simular results</option>
                </select>

                <label for="sort">Sorted by:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>

                <div>
                <label for="max_lga">Choose number of lga in results:</label>
                <input type="number" required value="" id="max_lga" name="max_lga" min="0" max="563">
                </div>
                <div>
                <label for="population1">Choose a Age range Min:</label>
                <input type="number" required value="" id="quantity" name="pop_min" min="0" max="200">
                </div>
                <div>
                <label for="population2">Choose a Age range Max:</label>
                <input type="number" required value="" id="quantity" name="pop_max" min="0" max="200">
                </div>

            """;
        html = html + """
            <input type="submit">
            </form>
            """;
        return html;
    }
    public String outputDataBase1_3() {
        String html = "";
        // Select Disease
         html = html + """
            <form action="/page3B.html" method="post">
                
            <label for="lga">Choose LGA to compare it to</label>
            <select id="drop_down" name="LGA">
                """;
                ArrayList<String> LGA = new ArrayList<String>();
                LGA = getLGA(2021);
                String LGAname;
                
                for (int i = 0; i < LGA.size(); i ++) {
                    LGAname = LGA.get(i);
                    html = html + "<option value= " + LGAname + " > " + LGAname + "</option>";
                }
        html = html + """
                </select>

                <label for="year">Choose 2016 or 2021:</label>
                <select id="drop_down" name="year">
                    <option value="2016">2016</option>
                    <option value="2021">2021</option>
                </select>
            
                <label for="indige3">Indigenous Status:</label>
                <select id="drop_down" name="indige3">
                    <option value="indig">Indigenous</option>
                    <option value="non_indig">Non Indigenous</option>
                    <option value="both">Both</option>
                </select>

                <label for="sex">Gender:</label>
                <select id="drop_down" name="sex">
                    <option value="m">Male</option>
                    <option value="f">Female</option>
                    <option value="both">Both</option>
                </select>

                <label for="sort2">Sorted by:</label>
                <select id="drop_down" name="sort2">
                    <option value="ASC">Most Simular results</option>
                    <option value="DESC">Least Simular results</option>
                </select>

                <label for="sort">Sorted by:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>
                <div>
                <label for="max_lga">Choose number of lga in results:</label>
                <input type="number" required value="" id="max_lga" name="max_lga" min="0" max="563">
                </div>

            """;
        html = html + """
            <input type="submit">
            </form>
            """;
        return html;
    }
    public String outputDataBase1_3_2() {
        String html = "";
        // Select Disease
         html = html + """
            <form action="/page3B.html" method="post">
                
            <label for="lga">Choose LGA to compare it to</label>
            <select id="drop_down" name="LGA">
                """;
                ArrayList<String> LGA = new ArrayList<String>();
                LGA = getLGA(2021);
                String LGAname;
                
                for (int i = 0; i < LGA.size(); i ++) {
                    LGAname = LGA.get(i);
                    html = html + "<option value= " + LGAname + " > " + LGAname + "</option>";
                }
        html = html + """
                </select>

                <label for="year">Choose 2016v or 2021:</label>
                <select id="drop_down" name="year">
                    <option value="2016">2016</option>
                    <option value="2021">2021</option>
                </select>
            
                <label for="indige3">Indigenous Status:</label>
                <select id="drop_down" name="indige3">
                    <option value="indig">Indigenous</option>
                    <option value="non_indig">Non Indigenous</option>
                    <option value="both">Both</option>
                </select>

                <label for="sex">Gender:</label>
                <select id="drop_down" name="sex">
                    <option value="m">Male</option>
                    <option value="f">Female</option>
                    <option value="both">Both</option>
                </select>

                <label for="sort">Sorted by:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>

                <label for="sort2">Sorted by:</label>
                <select id="drop_down" name="sort2">
                    <option value="ASC">Most Simular results</option>
                    <option value="DESC">Least Simular results</option>
                </select>

                <div>
                <label for="max_lga">Choose number of lga in results:</label>
                <input type="number" required value="" id="Max_lga" name="max_lga" min="0" max="563">
                </div>
                <div>
                <label for="eduation1">Choose a School Year range Min:</label>
                <input type="number" required value="" id="quantity" name="edu_min" min="8" max="12">
                </div>
                <div>
                <label for="eduation2">Choose a School Year range Max:</label>
                <input type="number" required value="" id="quantity" name="edu_max" min="8" max="12">
                </div>


            """;

        html = html + """
            <input type="submit">
            </form>
            """;
        return html;
    }
    public String outputDataBase1_4(String type, int Year) {
        String html = "";
        // Select Disease
         html = html + """
            <form action="/page3B.html" method="post">

                <label for="lga">Choose LGA to compare it to</label>
            <select id="drop_down" name="LGA">
                """;
                ArrayList<String> LGA = new ArrayList<String>();
                LGA = getLGA(2021);
                String LGAname;
                
                for (int i = 0; i < LGA.size(); i ++) {
                    LGAname = LGA.get(i);
                    html = html + "<option value= " + LGAname + " > " + LGAname + "</option>";
                }
        html = html + """
                </select>

                <label for="year">Choose 2016v or 2021:</label>
                <select id="drop_down" name="year">
                    <option value="2016">2016</option>
                    <option value="2021">2021</option>
                </select>
            
                <label for="indige4">Indigenous Status:</label>
                <select id="drop_down" name="indige4">
                    <option value="indig">Indigenous</option>
                    <option value="non_indig">Non Indigenous</option>
                    <option value="both">Both</option>
                </select>

                <label for="sex">Gender:</label>
                <select id="drop_down" name="sex">
                    <option value="m">Male</option>
                    <option value="f">Female</option>
                    <option value="both">Both</option>
                </select>

                <label for="sort2">Sorted by:</label>
                <select id="drop_down" name="sort2">
                    <option value="ASC">Most Simular results</option>
                    <option value="DESC">Least Simular results</option>
                </select>

                <label for="sort">Sorted by:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>

                <div>
                <label for="max_lga">Choose number of lga in results:</label>
                <input type="number" required value="" id="max_lga" name="max_lga" min="0" max="563">
                </div>


                <label for="NonScl">Choose a Non-School Education:</label>
            """;
        ArrayList<String> NonSCL = new ArrayList<String>();
        NonSCL = getNonSCL(2021);
        String NonSCLname;
        ArrayList<String> NonSCL1 = new ArrayList<String>();
        NonSCL1 = NonSclConvert(NonSCL);
        String NonSCLname1;
        
        for (int i = 0; i < NonSCL.size(); i ++) {
            NonSCLname = NonSCL.get(i);
            NonSCLname1 = NonSCL1.get(i);
            html = html + "<div><input type= \"checkbox\" id= \"NonScl\" name= " + "'" +  NonSCLname + "'" +" value= " + "'" + NonSCLname + "'" +" >"
            + "<label for= \"NonScl\">" + NonSCLname1 +  "</label></div>";
        }
        html = html + """
            <input type="submit">
            </form>
            """;
        return html;
    }
    public int getTableI(String query1, String column) {
        // Create the ArrayList of LGA objects to return
        int lga_code = 0;

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = query1;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                int count  = results.getInt(column);
                lga_code = count;

                // Add the lga object to the array
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lga_code;
    }
    public ArrayList<Integer> getTable(String query1, String column) {
        // Create the ArrayList of LGA objects to return
        ArrayList<Integer> lga_code = new ArrayList<Integer>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = query1;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                int count  = results.getInt(column);

                // Add the lga object to the array
                lga_code.add(count);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lga_code;
    }
    public ArrayList<String> getTableS(String query1, String column) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> lga_code = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = query1;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String count  = results.getString(column);

                // Add the lga object to the array
                lga_code.add(count);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        // Finally we return all of the lga
        return lga_code;
    }
    public String CreateTableLTHC(ArrayList<String> LTHC1, ArrayList<Integer> LTHC2, ArrayList<Integer> LTHC4) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>Ranking</th>
              <th>LGA Name</th>
              <th>LGA Code</th>
              <th>Count</th>
            </tr>
            """;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + (i + 1) + "</td>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td>" + LTHC2.get(i) + "</td> ";
            html = html + "<td> " + LTHC4.get(i) + "</td>";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public String CreateTableNon(ArrayList<String> LTHC1, ArrayList<Integer> LTHC2, ArrayList<Integer> LTHC4, ArrayList<Integer> LTHC5, ArrayList<String> LTHC6) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>LGA Name</th>
              <th>LGA Code</th>
              <th>Count 2016</th>
              <th>Count 2021</th>
              <th>Percentage of Change between 2021 and 2016</th>
            </tr>
            """;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td>" + LTHC2.get(i) + "</td> ";
            html = html + "<td> " + LTHC4.get(i) + "</td>";
            html = html + "<td> " + LTHC5.get(i) + "</td>";
            html = html + "<td> " + LTHC6.get(i) + "</td>";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public String CreateTablePop(ArrayList<String> LTHC1, ArrayList<Integer> LTHC2, ArrayList<Integer> LTHC4, ArrayList<Integer> LTHC5, ArrayList<String> LTHC6) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>LGA Name</th>
              <th>LGA Code</th>
              <th>Count 2016</th>
              <th>Count 2021</th>
              <th>Percentage of Change between 2021 and 2016</th>
            </tr>
            """;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td>" + LTHC2.get(i) + "</td> ";
            html = html + "<td> " + LTHC4.get(i) + "</td>";
            html = html + "<td> " + LTHC5.get(i) + "</td>";
            html = html + "<td> " + LTHC6.get(i) + "</td>";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public ArrayList<Integer> getSchoolYear(ArrayList<String> school) {
        ArrayList<Integer> Year = new ArrayList<Integer>();
        for (String item : school) {
            if (item.equals("did_not_go_to_school")){
                Year.add(0);
            } else if (item.equals("y8_below")){
                Year.add(8);
            } else if (item.equals("y9_equivalent")){
                Year.add(9);
            } else if (item.equals("y10_equivalent")){
                Year.add(10);
            } else if (item.equals("y11_equivalent")){
                Year.add(11);
            } else if (item.equals("y12_equivalent")){
                Year.add(12);
            } else {
                Year.add(-1);
            }
        }
        return Year;
    }
    public ArrayList<String> getSchoolYear(int edu_min, int edu_max) {
        ArrayList<String> School = new ArrayList<String>();
        if (edu_min <= 8 && edu_max >= 8) {
            School.add("y8_below");
        }
        if (edu_min <= 9 && edu_max >= 9) {
            School.add("y9_equivalent");
        }
        if (edu_min <= 10 && edu_max >= 10) {
            School.add("y10_equivalent");
        }
        if (edu_min <= 11 && edu_max >= 11) {
            School.add("y11_equivalent");
        }
        if (edu_min <= 12 && edu_max >= 12) {
            School.add("y12_equivalent");
        }
        return School;
    }
    public String getIndig(String indig){
        if (indig.equals("indig")){
            indig = "Indigenous";
        } else if (indig.equals("non_indig")){
            indig = "Non Indigenous";
        } else if (indig.equals("indig_ns")){
            indig = "Indigenous not Stated";
        } else {
            return "Both";
        }
        return indig;
    }
    public String getSort(String sort){
        String lol;
        if (sort.equals("ASC")) {
            lol = "Ascending order";
        } else {
            lol = "Decending order";
        }
        return lol;
    }
    public String getPercentage(int divide1, int divide2){
        double y = (divide1 * 100.0) / divide2;
        return String.format("%.2f", y);
    }

    //Get All LGAs
    public ArrayList<String> getLGAs() {
        ArrayList<String> lga = new ArrayList<>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT DISTINCT name FROM LGA;";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String LGAName = results.getString("name");
                lga.add(LGAName);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return lga;
    }

    public ArrayList<String> NonSclConvert(ArrayList<String> non_scl_1){
        ArrayList<String> non_scl_2 = new ArrayList<String>();
        String word = "";
        for (int i = 0; i < non_scl_1.size(); i++) {
            if (non_scl_1.get(i).equals("adip")) {
                word = "Advanced Diploma";
            } else if (non_scl_1.get(i).equals("bd")) {
                word = "Bachelor Degree Level";
            } else if (non_scl_1.get(i).equals("ct_i_ii")) {
                word = "Certificate I & II Level";
            } else if (non_scl_1.get(i).equals("ct_iii_iv")) {
                word = "Certificate III & IV Level";
            } else if (non_scl_1.get(i).equals("pd_gd_gc")){
                word = "Postgraduate Degree Level, Graduate Diploma and Graduate Certificate Level";
            }
            non_scl_2.add(word);
        }
        return non_scl_2;
    }

    public ArrayList<String> getLGA(int year) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> nonScl = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT name FROM lga WHERE year = " + year + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String non_scl  = results.getString("name");

                // Add the lga object to the array
                nonScl.add(non_scl);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return nonScl;
    }
    public String getSex(String sort){
        String lol;
        if (sort.equals("m")) {
            lol = "Male";
        } else {
            lol = "Female";
        }
        return lol;
    }
    public String NonSclConvert1(String non_scl_1){
        String word = "";
            if (non_scl_1.equals("adip")) {
                word = "Advanced Diploma";
            } else if (non_scl_1.equals("bd")) {
                word = "Bachelor Degree Level";
            } else if (non_scl_1.equals("ct_i_ii")) {
                word = "Certificate I & II Level";
            } else if (non_scl_1.equals("ct_iii_iv")) {
                word = "Certificate III & IV Level";
            } else if (non_scl_1.equals("pd_gd_gc")){
                word = "Postgraduate Degree Level, Graduate Diploma and Graduate Certificate Level";
            }
        return word;
    }
    public String getSort2(String sort){
        String lol;
        if (sort.equals("ASC")) {
            lol = "most Simular results";
        } else {
            lol = "least Simular results";
        }
        return lol;
    }

}

