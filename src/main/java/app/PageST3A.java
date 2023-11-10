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
public class PageST3A implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3A.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html += """
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Subtask 3.1</title>
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
        html = html + "<body class='task3a-body'>";

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
                <li><a href="page3A.html" class="active">The Gap</a></li>
                <li><a href="page3B.html">LGA Comparison</a></li>
            </ul>
            </header>
                """;

            // hero image
            html += """
            <section id="the-gap-header">
                <div class="overlay"></div>
                <div class="overlay-text">
                    <h2>Changing Gap between Indigenous & Non-Indigenous </h2>
                    <p>Find unbiased information about Changing Gap between Indigenous and Non-Indigenous People from 2016 to 2021</p>
                </div>
            </section>
                """;      
                
                
            // Form
            html = html + """
                <form action="/page3A.html" method="post">
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
                        <form action="/page3A.html" method="post">
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

                String lga_state = context.formParam("LGA_STATE");
                String sex1 = context.formParam("sex1");
                String sex2 = context.formParam("sex2");
                String sex3 = context.formParam("sex3");
                String sex4 = context.formParam("sex4");
                String sort = context.formParam("sort");
                String sort1 = context.formParam("sort1");
                String sort2 = context.formParam("sort2");
                String min = context.formParam("pop_min");
                String max = context.formParam("pop_max");
                String data = context.formParam("data");

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
                if (sex1 == null) {
                    html = html + "";
                } else if (lga_state.equals("lga")) {
                    String query1_1 = """
                        With Indig2021 as (
                            select lga.name as name1, lga.code as code1, LTHC.Long_Term as ED1, sum(LTHC.count) as count1, sex as s1 from LTHC left join lga on (LTHC.lga_code = lga.code and LTHC.lga_year = lga.year) where LTHC.lga_year = 2021 and indigenous_status = "indig" and 
                            Ed1 in (
                            """;
                            for (int i = 0; i < LTHC2.size(); i++) {
                                if (LTHC2.size() - 1 != i) {
                                    query1_1 = query1_1 + "'" + LTHC2.get(i) + "'" + ",";
                                } else {
                                    query1_1 = query1_1 + "'" + LTHC2.get(i) + "'";
                                }
                            }
                            query1_1 += " ) ";
                            if (!sex1.equals("both")) {
                                query1_1 += " and s1 = " + "'" + sex1 + "' ";
                            }  
                    query1_1 += """
                                 group by code1),
                                Noindig2021 as (
                            select lga.name as name2, lga.code as code2, LTHC.Long_Term as ED2, sum(LTHC.count) as count2, sex as s2 from LTHC left join lga on (LTHC.lga_code = lga.code and LTHC.lga_year = lga.year) where LTHC.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                            ED2 in (
                            """;
                            for (int i = 0; i < LTHC2.size(); i++) {
                                if (LTHC2.size() - 1 != i) {
                                    query1_1 = query1_1 + " '" + LTHC2.get(i) + "'" + ",";
                                } else {
                                    query1_1 = query1_1 + " '" + LTHC2.get(i) + "' ";
                                }
                            }
                            query1_1 += " ) ";
                            if (!sex1.equals("both")) {
                                query1_1 += " and s2 = " + "'" + sex1 + "' ";
                            }  
                    query1_1 += """
                                group by code2)
                            select name1, code1, count1, count2, (count1 - count2) as count_1 ,  round(((count1 - count2)*100.0)/(count2 + count1),2) as count1_1 from Indig2021 
                                join Noindig2021 on (Indig2021.name1 = Noindig2021.name2)
                            """;
                    query1_1 +=  " order by " + sort2 + " " + sort + ";";
                    html += "<h3>LGA data where The Long Term Health condition are ";
                    for (int j = 0; j < LTHC2.size(); j++) {
                        if (LTHC2.size() - 1 != j) {
                            html = html + " " +LTHC2.get(j) + ",";
                        } else {
                            html = html + " " +LTHC2.get(j);
                        }
                    }
                    html +=  " and sex is " + getSex(sex1) + " " + getSort2(sort2) + " Ranked by " + getSort(sort) + "</h3>";
                    html += "<p class='para'> The Gap in Number is the difference between the number of Indigenous population and the Non-Indigenous population";
                    html += ", Higher number means Higher number of Indigenous population suffering from the health condition while Lower number means less Indigenous population suffering from the health condition<br><br>";
                    html += "The Gap in Percentage is the percentage difference between the Indigenous population and the Non-Indigenous population";
                    html += " where Higher percentage means Higher amount of Indigenous population suffering from the health condition compared to Non-Indigenous populationn";
                    html += " while Lower Percentage means less Indigenous population suffering from the health condition compared to the Non-Indigenous population<br><br>";
                    html += "The Gap between Indigenous and Non-Indigenous shrinks as the Percentage gets closer to zero while the Gap widens as the percentage diviates away from zero </p>";
                    ArrayList<String> LTHC_lga_code1 = new ArrayList<String>();
                    ArrayList<Integer> LTHC_lga_code2 = new ArrayList<Integer>();
                    ArrayList<Integer> LTHC_lga_code3 = new ArrayList<Integer>();
                    ArrayList<String> LTHC_lga_value = new ArrayList<String>();
                    ArrayList<Integer> LTHC_lga_code4 = new ArrayList<Integer>();
                    ArrayList<Integer> LTHC_lga_code5 = new ArrayList<Integer>();
                    LTHC_lga_code1 = getTableS(query1_1, "name1");
                    LTHC_lga_code2 = getTable(query1_1, "code1");
                    LTHC_lga_code3 = getTable(query1_1, "count_1");
                    LTHC_lga_code4 = getTable(query1_1, "count1");
                    LTHC_lga_code5 = getTable(query1_1, "count2");
                    LTHC_lga_value = getTableS(query1_1, "count1_1");
                    LTHC_lga_value = removedNull(LTHC_lga_value);
                    html += CreateTableLTHC(LTHC_lga_code1,LTHC_lga_code2,LTHC_lga_code3,LTHC_lga_value, LTHC_lga_code4, LTHC_lga_code5 );
                } else if (lga_state.equals("state")) {
                    String query1_1 = """
                        With Indig2021 as (
                            select lga.state_abbr as name1, lga.code as code1, LTHC.Long_Term as ED1, sum(LTHC.count) as count1, sex as s1 from LTHC left join lga on (LTHC.lga_code = lga.code and LTHC.lga_year = lga.year) where LTHC.lga_year = 2021 and indigenous_status = "indig" and 
                            Ed1 in (
                            """;
                            for (int i = 0; i < LTHC2.size(); i++) {
                                if (LTHC2.size() - 1 != i) {
                                    query1_1 = query1_1 + "'" + LTHC2.get(i) + "'" + ",";
                                } else {
                                    query1_1 = query1_1 + "'" + LTHC2.get(i) + "'";
                                }
                            }
                            query1_1 += " ) ";
                            if (!sex1.equals("both")) {
                                query1_1 += " and s1 = " + "'" + sex1 + "' ";
                            }  
                    query1_1 += """
                                 group by name1),
                                Noindig2021 as (
                            select lga.state_abbr as name2, lga.code as code2, LTHC.Long_Term as ED2, sum(LTHC.count) as count2, sex as s2 from LTHC left join lga on (LTHC.lga_code = lga.code and LTHC.lga_year = lga.year) where LTHC.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                            ED2 in (
                            """;
                            for (int i = 0; i < LTHC2.size(); i++) {
                                if (LTHC2.size() - 1 != i) {
                                    query1_1 = query1_1 + " '" + LTHC2.get(i) + "'" + ",";
                                } else {
                                    query1_1 = query1_1 + " '" + LTHC2.get(i) + "' ";
                                }
                            }
                            query1_1 += " ) ";
                            if (!sex1.equals("both")) {
                                query1_1 += " and s2 = " + "'" + sex1 + "' ";
                            }  
                    query1_1 += """
                                group by name2)
                            select name1,count1 ,count2, (count1 - count2) as count_1, round(((count1 - count2)*100.0)/(count2 + count1),2) as count1_1 from Indig2021 
                                join Noindig2021 on (Indig2021.name1 = Noindig2021.name2)
                            """;
                    query1_1 +=  " order by " + sort2 + " " + sort + ";";
                    html += "<h3>State data " + " where The Long Term Health condition are ";
                    for (int j = 0; j < LTHC2.size(); j++) {
                        if (LTHC2.size() - 1 != j) {
                            html = html + "'" + LTHC2.get(j) + "'" + ",";
                        } else {
                            html = html + "'" + LTHC2.get(j) + "'";
                        }
                    }
                    html +=  " and sex is " + getSex(sex1) + " " + getSort2(sort2) + " Ranked by " + getSort(sort) + "</h3>";
                    html += "<p class='para'> The Gap in Number is the difference between the number of Indigenous population and the Non-Indigenous population";
                    html += ", Higher number means Higher number of Indigenous population suffering from the health condition while Lower number means less Indigenous population suffering from the health condition<br><br>";
                    html += "The Gap in Percentage is the percentage difference between the Indigenous population and the Non-Indigenous population";
                    html += " where Higher percentage means Higher amount of Indigenous population suffering from the health condition compared to Non-Indigenous populationn";
                    html += " while Lower Percentage means less Indigenous population suffering from the health condition compared to the Non-Indigenous population<br><br>";
                    html += "The Gap between Indigenous and Non-Indigenous shrinks as the Percentage gets closer to zero while the Gap widens as the percentage diviates away from zero </p>";
                    ArrayList<String> LTHC_lga_code4 = new ArrayList<String>();
                    ArrayList<String> LTHC_lga_value = new ArrayList<String>();
                    ArrayList<String> LTHC_lga_value1 = new ArrayList<String>();
                    ArrayList<String> LTHC_lga_value2 = new ArrayList<String>();
                    ArrayList<String> LTHC_lga_value3 = new ArrayList<String>();
                    LTHC_lga_code4 = getTableS(query1_1, "name1");
                    LTHC_lga_value = getTableS(query1_1, "count1");
                    LTHC_lga_value1 = getTableS(query1_1, "count2");
                    LTHC_lga_value2 = getTableS(query1_1, "count_1");
                    LTHC_lga_value3 = getTableS(query1_1, "count1_1");
                    LTHC_lga_value = removedNull(LTHC_lga_value);
                    html += CreateTableLTHC1(LTHC_lga_code4, LTHC_lga_value, LTHC_lga_value1, LTHC_lga_value2, LTHC_lga_value3);
                }
                
                // Non_School Eduation
                if (sex4 == null) {
                    html += "";
                } else if (lga_state.equals("lga") && data.equals("Raw")) {
                    html += "<h3>Raw LGA data where the Non-School Education is ";
                    for (int j = 0; j < NonSCLALL.size(); j++) {
                        if (NonSCLALL.size() - 1 != j) {
                            html = html + " " + NonSclConvert1(NonSCLALL.get(j)) + ",";
                        } else {
                            html = html + " " + NonSclConvert1(NonSCLALL.get(j));
                        }
                    }
                    html +=  " and sex is " + getSex(sex4) + " " + getSort1(sort1) + " and Ranked by " + getSort(sort) + "</h3>";
                    String query1_4 = """
                                with Indig2016 as (
                                    select lga.name as name1, lga.code as code1, Non_School_ED.education as ED1, sum(Non_School_ED.count) as count1, sex as s1 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2016 and indigenous_status = "indig" and 
                                    ed1 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex4 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by code1), 
                                        Noindig2016 as (
                                    select lga.name as name2, lga.code as code2, Non_School_ED.education as ED2, sum(Non_School_ED.count) as count2, sex as s2 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                    ed2 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex4 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by code2),
                                    Indig2021 as (
                                select lga.name as name3, lga.code as code3, Non_School_ED.education as ED3, sum(Non_School_ED.count) as count3, sex as s3 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2021 and indigenous_status = "indig" and 
                                    ed3 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex4 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by code3),
                                    Noindig2021 as (
                                select lga.name as name4, lga.code as code4, Non_School_ED.education as ED4, sum(Non_School_ED.count) as count4, sex as s4 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                    ed4 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex4 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by code4)
                                
                                select name1, code1, count1, count2, (count1 - count2) as count1_1, count3, count4, (count3 - count4) as count1_2, ((count3 - count4) - (count1 - count2)) as count1_3 from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4) 
                                        """;
                    
                    query1_4 +=  " order by " + sort1 + " " + sort + ";";
                    html += "<p class='para'> The Gap in Number is the number difference between the Indigenous population and the Non-Indigenous population";
                    html += " where Higher Number means Higher amount of Indigenous population recieved this non-school education compared to Non-Indigenous population";
                    html += " while Lower Percentage means less Indigenous population recieved this non-school education compared to the Non-Indigenous population<br><br>";
                    html += "The Gap between Indigenous and Non-Indigenous shrinks as the Number gets closer to zero while the Gap widens as Number diviates away from zero <br><br>";
                    html += "The Gap change compares the gap between Indigenous and Non-Indigenous population who recieved this non-school education, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                    ArrayList<String> Non_lga_code1 = new ArrayList<String>();
                    ArrayList<Integer> Non_lga_code2 = new ArrayList<Integer>();
                    ArrayList<String> Non_lga_code3 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code4 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code5 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code6 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code7 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code8 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code9 = new ArrayList<String>();
                    Non_lga_code1 = getTableS(query1_4, "name1");
                    Non_lga_code2 = getTable(query1_4, "code1");
                    Non_lga_code3 = getTableS(query1_4, "count1");
                    Non_lga_code4 = getTableS(query1_4, "count2");
                    Non_lga_code5 = getTableS(query1_4, "count1_1");
                    Non_lga_code6 = getTableS(query1_4, "count3");
                    Non_lga_code7 = getTableS(query1_4, "count4");
                    Non_lga_code8 = getTableS(query1_4, "count1_2");
                    Non_lga_code9 = getTableS(query1_4, "count1_3");
                    html += CreateTableNon_1(Non_lga_code1,Non_lga_code2, Non_lga_code3, Non_lga_code4, Non_lga_code5, Non_lga_code6, Non_lga_code7, Non_lga_code8, Non_lga_code9);
                } else if (lga_state.equals("state")  && data.equals("Raw")) {
                    html += "<h3>Raw State data " + " where Non-School Education is ";
                    for (int j = 0; j < NonSCLALL.size(); j++) {
                        if (NonSCLALL.size() - 1 != j) {
                            html = html + " " + NonSclConvert1(NonSCLALL.get(j)) + ",";
                        } else {
                            html = html + " " + NonSclConvert1(NonSCLALL.get(j));
                        }
                    }
                    html +=  " and sex is " + getSex(sex4) + " " + getSort1(sort1) + " and Ranked by " + getSort(sort) + "</h3>";
                    String query1_4 = """
                                with Indig2016 as (
                                    select lga.state_abbr as name1, lga.code as code1, Non_School_ED.education as ED1, sum(Non_School_ED.count) as count1, sex as s1 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2016 and indigenous_status = "indig" and 
                                    ed1 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }

                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex4 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by name1), 
                                        Noindig2016 as (
                                    select lga.state_abbr as name2, lga.code as code2, Non_School_ED.education as ED2, sum(Non_School_ED.count) as count2, sex as s2 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                    ed2 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex4 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by name2),
                                    Indig2021 as (
                                select lga.state_abbr as name3, lga.code as code3, Non_School_ED.education as ED3, sum(Non_School_ED.count) as count3, sex as s3 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2021 and indigenous_status = "indig" and 
                                    ed3 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex4 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by name3),
                                    Noindig2021 as (
                                select lga.state_abbr as name4, lga.code as code4, Non_School_ED.education as ED4, sum(Non_School_ED.count) as count4, sex as s4 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                    ed4 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex4 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by name4)
                                
                                select name1, count1, count2, (count1 - count2) as count1_1, count3, count4, (count3 - count4) as count1_2, ((count3 - count4) - (count1 - count2)) as count1_3 from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4)
                                        """;
                    query1_4 +=  " order by " + sort1 + " " + sort + ";";
                    html += "<p class='para'> The Gap in Number is the number difference between the Indigenous population and the Non-Indigenous population";
                    html += " where Higher Number means Higher amount of Indigenous population recieved this non-school education compared to Non-Indigenous population";
                    html += " while Lower Number means less Indigenous population recieved this non-school educationcompared to the Non-Indigenous population<br><br>";
                    html += "The Gap between Indigenous and Non-Indigenous shrinks as the Number gets closer to zero while the Gap widens as Number diviates away from zero <br><br>";
                    html += "The Gap change compares the gap between Indigenous and Non-Indigenous population who recieved this non-school education, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                    ArrayList<String> Non_lga_code1 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code2 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code3 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code4 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code5 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code6 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code7 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code8 = new ArrayList<String>();
                    Non_lga_code1 = getTableS(query1_4, "name1");
                    Non_lga_code2 = getTableS(query1_4, "count1");
                    Non_lga_code3 = getTableS(query1_4, "count2");
                    Non_lga_code4 = getTableS(query1_4, "count1_1");
                    Non_lga_code5 = getTableS(query1_4, "count3");
                    Non_lga_code6 = getTableS(query1_4, "count4");
                    Non_lga_code7 = getTableS(query1_4, "count1_2");
                    Non_lga_code8 = getTableS(query1_4, "count1_3");
                    html += CreateTableNon1_1(Non_lga_code1, Non_lga_code2, Non_lga_code3, Non_lga_code4, Non_lga_code5, Non_lga_code6, Non_lga_code7, Non_lga_code8);
                } else if (lga_state.equals("lga") && data.equals("Proportional")) {
                    html += "<h3>Proportional LGA data " + "For " + " where Non-School Education is ";
                    for (int j = 0; j < NonSCLALL.size(); j++) {
                        if (NonSCLALL.size() - 1 != j) {
                            html = html + " " + NonSclConvert1(NonSCLALL.get(j)) + ",";
                        } else {
                            html = html + " " + NonSclConvert1(NonSCLALL.get(j));
                        }
                    }
                    html +=  " and sex is " + getSex(sex4) + " " + getSort1(sort1) + " and Ranked by " + getSort(sort) + "</h3>";
                    String query1_4 = """
                                with Indig2016 as (
                                    select lga.name as name1, lga.code as code1, Non_School_ED.education as ED1, sum(Non_School_ED.count) as count1, sex as s1 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2016 and indigenous_status = "indig" and 
                                    ed1 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex4 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by code1), 
                                        Noindig2016 as (
                                    select lga.name as name2, lga.code as code2, Non_School_ED.education as ED2, sum(Non_School_ED.count) as count2, sex as s2 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                    ed2 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex4 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by code2),
                                    Indig2021 as (
                                select lga.name as name3, lga.code as code3, Non_School_ED.education as ED3, sum(Non_School_ED.count) as count3, sex as s3 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2021 and indigenous_status = "indig" and 
                                    ed3 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex4 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by code3),
                                    Noindig2021 as (
                                select lga.name as name4, lga.code as code4, Non_School_ED.education as ED4, sum(Non_School_ED.count) as count4, sex as s4 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                    ed4 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex4 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by code4)
                                
                                select name1, code1, round(((count1 - count2)*100.0)/(count2 + count1),2) as count1_1, round(((count3 - count4)*100.0)/(count4 + count3),2) as count1_2, round((((count3 - count4)*100)/(count4 + count3)) - (((count1 - count2)*100.0)/(count2 + count1)),2) as count1_3 from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4) 
                                        """;
                    
                    query1_4 +=  " order by " + sort1 + " " + sort + ";";
                    html += "<p class='para'> The Gap in Percentage is the percentage difference between the Indigenous population and the Non-Indigenous population";
                    html += " where Higher percentage means Higher amount of Indigenous population recieved this non-school education when compared to Non-Indigenous population";
                    html += " while Lower Percentage means less Indigenous population recieved this non-school education compared to the Non-Indigenous population<br><br>";
                    html += "The Gap between Indigenous and Non-Indigenous shrinks as the Percentage gets closer to zero while the Gap widens as the percentage diviates away from zero <br><br>";
                    html += "The Gap change compares the gap between Indigenous and Non-Indigenous population who recieved this non-school education, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                    ArrayList<String> Non_lga_code1 = new ArrayList<String>();
                    ArrayList<Integer> Non_lga_code2 = new ArrayList<Integer>();
                    ArrayList<String> Non_lga_code4 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code5 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code6 = new ArrayList<String>();
                    Non_lga_code1 = getTableS(query1_4, "name1");
                    Non_lga_code2 = getTable(query1_4, "code1");
                    Non_lga_code4 = getTableS(query1_4, "count1_1");
                    Non_lga_code5 = getTableS(query1_4, "count1_2");
                    Non_lga_code6 = getTableS(query1_4, "count1_3");
                    Non_lga_code4 = removedNull(Non_lga_code4);
                    Non_lga_code5 = removedNull(Non_lga_code5);
                    Non_lga_code6 = removedNull(Non_lga_code6);
                    html += CreateTableNon(Non_lga_code1,Non_lga_code2,Non_lga_code4, Non_lga_code5, Non_lga_code6);
                } else if (lga_state.equals("state")  && data.equals("Proportional")) {
                    html += "<h3>Proportional State data " + " where Non-School Education is ";
                    for (int j = 0; j < NonSCLALL.size(); j++) {
                        if (NonSCLALL.size() - 1 != j) {
                            html = html + " " + NonSclConvert1(NonSCLALL.get(j)) + ",";
                        } else {
                            html = html + " " + NonSclConvert1(NonSCLALL.get(j));
                        }
                    }
                    html +=  " and sex is " + getSex(sex4) + " " + getSort1(sort1) + " and Ranked by " + getSort(sort) + "</h3>";
                    String query1_4 = """
                                with Indig2016 as (
                                    select lga.state_abbr as name1, lga.code as code1, Non_School_ED.education as ED1, sum(Non_School_ED.count) as count1, sex as s1 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2016 and indigenous_status = "indig" and 
                                    ed1 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }

                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex4 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by name1), 
                                        Noindig2016 as (
                                    select lga.state_abbr as name2, lga.code as code2, Non_School_ED.education as ED2, sum(Non_School_ED.count) as count2, sex as s2 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                    ed2 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex4 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by name2),
                                    Indig2021 as (
                                select lga.state_abbr as name3, lga.code as code3, Non_School_ED.education as ED3, sum(Non_School_ED.count) as count3, sex as s3 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2021 and indigenous_status = "indig" and 
                                    ed3 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex4 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by name3),
                                    Noindig2021 as (
                                select lga.state_abbr as name4, lga.code as code4, Non_School_ED.education as ED4, sum(Non_School_ED.count) as count4, sex as s4 from Non_School_ED left join lga on (Non_School_ED.lga_code = lga.code and Non_School_ED.lga_year = lga.year) where Non_School_ED.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                    ed4 in (
                                        """;

                                        for (int i = 0; i < NonSCLALL.size(); i++) {
                                            if (NonSCLALL.size() - 1 != i) {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'" + ",";
                                            } else {
                                                query1_4 = query1_4 + "'" + NonSCLALL.get(i) + "'";
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex4.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex4 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by name4)
                                
                                select name1,  round(((count1 - count2)*100.0)/(count2 + count1),2) as count1_1, round(((count3 - count4)*100.0)/(count4 + count3),2) as count1_2, round((((count3 - count4)*100)/(count4 + count3)) - (((count1 - count2)*100.0)/(count2 + count1)),2) as count1_3  from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4)
                                        """;
                    query1_4 +=  " order by " + sort1 + " " + sort + ";";
                    html += "<p class='para'> The Gap in Percentage is the percentage difference between the Indigenous population and the Non-Indigenous population";
                    html += " where Higher percentage means Higher amount of Indigenous population recieved this non-school education when compared to Non-Indigenous population";
                    html += " while Lower Percentage means less Indigenous population recieved this non-school education compared to the Non-Indigenous population<br><br>";
                    html += "The Gap between Indigenous and Non-Indigenous shrinks as the Percentage gets closer to zero while the Gap widens as the percentage diviates away from zero <br><br>";
                    html += "The Gap change compares the gap between Indigenous and Non-Indigenous population who recieved this non-school education, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                    ArrayList<String> Non_lga_code1 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code4 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code5 = new ArrayList<String>();
                    ArrayList<String> Non_lga_code6 = new ArrayList<String>();
                    Non_lga_code1 = getTableS(query1_4, "name1");
                    Non_lga_code4 = getTableS(query1_4, "count1_1");
                    Non_lga_code5 = getTableS(query1_4, "count1_2");
                    Non_lga_code6 = getTableS(query1_4, "count1_3");
                    Non_lga_code4 = removedNull(Non_lga_code4);
                    Non_lga_code5 = removedNull(Non_lga_code5);
                    Non_lga_code6 = removedNull(Non_lga_code6);
                    html += CreateTableNon1(Non_lga_code1, Non_lga_code4, Non_lga_code5, Non_lga_code6);
                }
                
                // Population
                if (sex2 == null) {
                    html += "";
                } else if (lga_state.equals("lga") && data.equals("Proportional")) {
                    if (Integer.valueOf(min) > Integer.valueOf(max)) {
                        html += "<h2> Please select a Minmum age that is smaller than the Maximum age </h2>";
                    } else {
                        html += "<h3>Proportional LGA data " + " where the Age range is between " + min + " and " + max + " and sex is " + getSex(sex2) + " " + getSort1(sort1) + " Ranked by " + getSort(sort) + "</h3>";
                        String query1_4 = """
                                with Indig2016 as (
                                    select lga.name as name1, lga.code as code1, population.age_category as ED1, sum(population.count) as count1, sex as s1 from Population left join lga on (population.lga_code = lga.code and population.lga_year = lga.year) where population.lga_year = 2016 and indigenous_status = "indig" and 
                                        """;

                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex2 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by code1), 
                                        Noindig2016 as (
                                    select lga.name as name2, lga.code as code2, population.age_category as ED2, sum(population.count) as count2, sex as s2 from Population left join lga on (population.lga_code = lga.code and population.lga_year = lga.year) where population.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                        """;

                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex2 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by code2),
                                    Indig2021 as (
                                select lga.name as name3, lga.code as code3, population.age_category as ED3, sum(population.count) as count3, sex as s3 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2021 and indigenous_status = "indig" and 
                                    
                                        """;

                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex2 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by code3),
                                    Noindig2021 as (
                                select lga.name as name4, lga.code as code4, Population.age_category as ED4, Population.count as count4, sex as s4 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                    
                                        """;

                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex2 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by code4)
                                
                                select name1, code1,  round(((count1 - count2)*100.0)/(count2 + count1),2) as count1_1, round(((count3 - count4)*100.0)/(count4 + count3),2) as count1_2, round((((count3 - count4)*100)/(count4 + count3)) - (((count1 - count2)*100.0)/(count2 + count1)),2) as count1_3  from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4)
                                    ;   
                                        """;
                        query1_4 +=  " order by " + sort1 + " " + sort + ";";
                        html += "<p class='para'> The Gap in Percentage is the percentage difference between the Indigenous population and the Non-Indigenous population";
                        html += " where Higher percentage means Higher amount of Indigenous population belongs to this Age Category when compared to Non-Indigenous population";
                        html += " while Lower Percentage means less Indigenous population belongs to this Age Category compared to the Non-Indigenous population<br><br>";
                        html += "The Gap between Indigenous and Non-Indigenous shrinks as the Percentage gets closer to zero while the Gap widens as the percentage diviates away from zero <br><br>";
                        html += "The Gap change compares the gap between Indigenous and Non-Indigenous population in this Age Category, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                        ArrayList<String> Pop_lga_code1 = new ArrayList<String>();
                        ArrayList<Integer> Pop_lga_code2 = new ArrayList<Integer>();
                        ArrayList<String> Pop_lga_code4 = new ArrayList<String>();
                        ArrayList<String> Pop_lga_code5 = new ArrayList<String>();
                        ArrayList<String> Pop_lga_code6 = new ArrayList<String>();
                        Pop_lga_code1 = getTableS(query1_4, "name1");
                        Pop_lga_code2 = getTable(query1_4, "code1");
                        Pop_lga_code4 = getTableS(query1_4, "count1_1");
                        Pop_lga_code5 = getTableS(query1_4, "count1_2");
                        Pop_lga_code6 = getTableS(query1_4, "count1_3");
                        Pop_lga_code4 = removedNull(Pop_lga_code4);
                        Pop_lga_code5 = removedNull(Pop_lga_code5);
                        Pop_lga_code6 = removedNull(Pop_lga_code6);
                        html += CreateTableNon(Pop_lga_code1,Pop_lga_code2,Pop_lga_code4, Pop_lga_code5, Pop_lga_code6);
                    }
                } else if (lga_state.equals("state") && data.equals("Proportional")) {
                    html += "<h3>Proportional State data " + " where Age range is between " + min + " and " + max + " and sex is " + getSex(sex2) + " " + getSort1(sort1) + " Ranked by " + getSort(sort) + "</h3>";
                    if (Integer.valueOf(min) > Integer.valueOf(max)) {
                        html += "<h2> Please select a Minmum age that is smaller than the Maximum age </h2>";
                    } else {
                        String query1_4 = """
                                with Indig2016 as (
                                    select lga.state_abbr as name1, lga.code as code1, Population.age_category as ED1, sum(Population.count) as count1, sex as s1 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2016 and indigenous_status = "indig" and 
                                        """;
                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex2 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by name1), 
                                        Noindig2016 as (
                                    select lga.state_abbr as name2, lga.code as code2, Population.age_category as ED2, sum(Population.count) as count2, sex as s2 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                        """;
                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex2 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by name2),
                                    Indig2021 as (
                                select lga.state_abbr as name3, lga.code as code3, Population.age_category as ED3, sum(Population.count) as count3, sex as s3 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2021 and indigenous_status = "indig" and 
                                        """;
                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex2 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by name3),
                                    Noindig2021 as (
                                select lga.state_abbr as name4, lga.code as code4, Population.age_category as ED4, sum(Population.count) as count4, sex as s4 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                        """;
                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex2 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by name4)
                                
                                select name1,  round(((count1 - count2)*100.0)/(count2 + count1),2) as count1_1, round(((count3 - count4)*100.0)/(count4 + count3),2) as count1_2, round((((count3 - count4)*100)/(count4 + count3)) - (((count1 - count2)*100.0)/(count2 + count1)),2) as count1_3  from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4)
                                        """;
                        query1_4 +=  " order by " + sort1 + " " + sort + ";";
                        html += "<p class='para'> The Gap in Percentage is the percentage difference between the Indigenous population and the Non-Indigenous population";
                        html += " where Higher percentage means Higher amount of Indigenous population belongs to this Age Category when compared to Non-Indigenous population";
                        html += " while Lower Percentage means less Indigenous population belongs to this Age Category compared to the Non-Indigenous population<br><br>";
                        html += "The Gap between Indigenous and Non-Indigenous shrinks as the Percentage gets closer to zero while the Gap widens as the percentage diviates away from zero <br><br>";
                        html += "The Gap change compares the gap between Indigenous and Non-Indigenous population in this Age Category, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                        ArrayList<String> Pop_lga_code1 = new ArrayList<String>();
                        ArrayList<String> Pop_lga_code4 = new ArrayList<String>();
                        ArrayList<String> Pop_lga_code5 = new ArrayList<String>();
                        ArrayList<String> Pop_lga_code6 = new ArrayList<String>();
                        Pop_lga_code1 = getTableS(query1_4, "name1");
                        Pop_lga_code4 = getTableS(query1_4, "count1_1");
                        Pop_lga_code5 = getTableS(query1_4, "count1_2");
                        Pop_lga_code6 = getTableS(query1_4, "count1_3");
                        Pop_lga_code4 = removedNull(Pop_lga_code4);
                        Pop_lga_code5 = removedNull(Pop_lga_code5);
                        Pop_lga_code6 = removedNull(Pop_lga_code6);
                        html += CreateTableNon1(Pop_lga_code1,Pop_lga_code4, Pop_lga_code5, Pop_lga_code6);
                    }
                } else if (lga_state.equals("lga") && data.equals("Raw")) {
                    if (Integer.valueOf(min) > Integer.valueOf(max)) {
                        html += "<h2> Please select a Minmum age that is smaller than the Maximum age </h2>";
                    } else {
                        html += "<h3>Raw LGA data " + " where Age range is between " + min + " and " + max + " and sex is " + getSex(sex2) + " " + getSort1(sort1) + " Ranked by " + getSort(sort) + "</h3>";
                        String query1_4 = """
                                with Indig2016 as (
                                    select lga.name as name1, lga.code as code1, population.age_category as ED1, sum(population.count) as count1, sex as s1 from Population left join lga on (population.lga_code = lga.code and population.lga_year = lga.year) where population.lga_year = 2016 and indigenous_status = "indig" and 
                                        """;

                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex2 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by code1), 
                                        Noindig2016 as (
                                    select lga.name as name2, lga.code as code2, population.age_category as ED2, sum(population.count) as count2, sex as s2 from Population left join lga on (population.lga_code = lga.code and population.lga_year = lga.year) where population.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                        """;

                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex2 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by code2),
                                    Indig2021 as (
                                select lga.name as name3, lga.code as code3, population.age_category as ED3, sum(population.count) as count3, sex as s3 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2021 and indigenous_status = "indig" and 
                                    
                                        """;

                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex2 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by code3),
                                    Noindig2021 as (
                                select lga.name as name4, lga.code as code4, Population.age_category as ED4, Population.count as count4, sex as s4 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                    
                                        """;

                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex2 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by code4)
                                
                                select name1, code1, count1, count2, (count1 - count2) as count1_1, count3, count4, (count3 - count4) as count1_2, ((count3 - count4) - (count1 - count2)) as count1_3 from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4)
                                    ;   
                                        """;
                        query1_4 +=  " order by " + sort1 + " " + sort + ";";
                        html += "<p class='para'> The Gap in Number is the number difference between the Indigenous population and the Non-Indigenous population";
                        html += " where Higher Number means Higher amount of Indigenous population belongs to this Age Category when compared to Non-Indigenous population";
                        html += " while Lower Number means less Indigenous population belonging to this Age Category compared to the Non-Indigenous population<br><br>";
                        html += "The Gap between Indigenous and Non-Indigenous shrinks as the Number gets closer to zero while the Gap widens as Number diviates away from zero <br><br>";
                        html += "The Gap change compares the gap between Indigenous and Non-Indigenous population in this Age Category, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                        ArrayList<String> Non_lga_code1 = new ArrayList<String>();
                        ArrayList<Integer> Non_lga_code2 = new ArrayList<Integer>();
                        ArrayList<String> Non_lga_code3 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code4 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code5 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code6 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code7 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code8 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code9 = new ArrayList<String>();
                        Non_lga_code1 = getTableS(query1_4, "name1");
                        Non_lga_code2 = getTable(query1_4, "code1");
                        Non_lga_code3 = getTableS(query1_4, "count1");
                        Non_lga_code4 = getTableS(query1_4, "count2");
                        Non_lga_code5 = getTableS(query1_4, "count1_1");
                        Non_lga_code6 = getTableS(query1_4, "count3");
                        Non_lga_code7 = getTableS(query1_4, "count4");
                        Non_lga_code8 = getTableS(query1_4, "count1_2");
                        Non_lga_code9 = getTableS(query1_4, "count1_3");
                        html += CreateTableNon_1(Non_lga_code1,Non_lga_code2, Non_lga_code3, Non_lga_code4, Non_lga_code5, Non_lga_code6, Non_lga_code7, Non_lga_code8, Non_lga_code9);
                    }
                } else if (lga_state.equals("state") && data.equals("Raw")) {
                    html += "<h3>Raw State data " + " where Age range is between " + min + " and " + max + " and sex is " + getSex(sex2) + " " + getSort1(sort1) + " Ranked by " + getSort(sort) + "</h3>";
                    if (Integer.valueOf(min) > Integer.valueOf(max)) {
                        html += "<h2> Please select a Minmum age that is smaller than the Maximum age </h2>";
                    } else {
                        String query1_4 = """
                                with Indig2016 as (
                                    select lga.state_abbr as name1, lga.code as code1, Population.age_category as ED1, sum(Population.count) as count1, sex as s1 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2016 and indigenous_status = "indig" and 
                                        """;
                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex2 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by name1), 
                                        Noindig2016 as (
                                    select lga.state_abbr as name2, lga.code as code2, Population.age_category as ED2, sum(Population.count) as count2, sex as s2 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                        """;
                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex2 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by name2),
                                    Indig2021 as (
                                select lga.state_abbr as name3, lga.code as code3, Population.age_category as ED3, sum(Population.count) as count3, sex as s3 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2021 and indigenous_status = "indig" and 
                                        """;
                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex2 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by name3),
                                    Noindig2021 as (
                                select lga.state_abbr as name4, lga.code as code4, Population.age_category as ED4, sum(Population.count) as count4, sex as s4 from Population left join lga on (Population.lga_code = lga.code and Population.lga_year = lga.year) where Population.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                        """;
                                        query1_4 += " age_min >= " + min + " and age_max <= " + max + " ";
                                        if (!sex2.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex2 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by name4)
                                
                                select name1, count1, count2, (count1 - count2) as count1_1, count3, count4, (count3 - count4) as count1_2, ((count3 - count4) - (count1 - count2)) as count1_3 from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4)
                                        """;
                        query1_4 +=  " order by " + sort1 + " " + sort + ";";
                        html += "<p class='para'> The Gap in Number is the number difference between the Indigenous population and the Non-Indigenous population";
                        html += " where Higher Number means Higher amount of Indigenous population belongs to this Age Category when compared to Non-Indigenous population";
                        html += " while Lower Number means less Indigenous population belonging to this Age Category compared to the Non-Indigenous population<br><br>";
                        html += "The Gap between Indigenous and Non-Indigenous shrinks as the Number gets closer to zero while the Gap widens as Number diviates away from zero <br><br>";
                        html += "The Gap change compares the gap between Indigenous and Non-Indigenous population in this Age Category, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                        ArrayList<String> Non_lga_code1 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code2 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code3 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code4 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code5 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code6 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code7 = new ArrayList<String>();
                        ArrayList<String> Non_lga_code8 = new ArrayList<String>();
                        Non_lga_code1 = getTableS(query1_4, "name1");
                        Non_lga_code2 = getTableS(query1_4, "count1");
                        Non_lga_code3 = getTableS(query1_4, "count2");
                        Non_lga_code4 = getTableS(query1_4, "count1_1");
                        Non_lga_code5 = getTableS(query1_4, "count3");
                        Non_lga_code6 = getTableS(query1_4, "count4");
                        Non_lga_code7 = getTableS(query1_4, "count1_2");
                        Non_lga_code8 = getTableS(query1_4, "count1_3");
                        html += CreateTableNon1_1(Non_lga_code1, Non_lga_code2, Non_lga_code3, Non_lga_code4, Non_lga_code5, Non_lga_code6, Non_lga_code7, Non_lga_code8);
                    }
                }

                // School Year
                if (sex3 == null) {
                    html += "";
                } else if (edu_min == null){
                    html += "";
                } else if (lga_state.equals("lga") && data.equals("Proportional")) {
                    if (Integer.valueOf(edu_min) > Integer.valueOf(edu_max)) {
                        html += "<h2> Please select a Minmum School year that is smaller than the Maximum School year </h2>";
                    } else {
                            String query1_4 = """
                                with Indig2016 as (
                                    select lga.name as name1, lga.code as code1, SchoolYear.School_Year as ED1, sum(SchoolYear.count) as count1, sex as s1 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2016 and indigenous_status = "indig" and 
                                    ed1 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex3 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by code1), 
                                        Noindig2016 as (
                                    select lga.name as name2, lga.code as code2, SchoolYear.School_Year as ED2, sum(SchoolYear.count) as count2, sex as s2 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                    ed2 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex3 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by code2),
                                    Indig2021 as (
                                select lga.name as name3, lga.code as code3, SchoolYear.School_Year as ED3, sum(SchoolYear.count) as count3, sex as s3 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2021 and indigenous_status = "indig" and 
                                    ed3 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex3 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by code3),
                                    Noindig2021 as (
                                select lga.name as name4, lga.code as code4, SchoolYear.School_Year as ED4, sum(SchoolYear.count) as count4, sex as s4 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                    ed4 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex3 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by code4)
                                
                                select name1, code1, round(((count1 - count2)*100.0)/(count2 + count1),2) as count1_1, round(((count3 - count4)*100.0)/(count4 + count3),2) as count1_2, round((((count3 - count4)*100)/(count4 + count3)) - (((count1 - count2)*100.0)/(count2 + count1)),2) as count1_3  from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4)
                                        """;
                            query1_4 +=  " order by " + sort1 + " " + sort + ";";
                            html += "<h3>Proportional LGA data ";
                            if (Integer.valueOf(edu_min) == 7) {
                                html += " where the Population Did not got to School";
                            } else {
                                html += " where School year is  between " + edu_min + " and " + edu_max;
                            }
                            html += " and sex is " + getSex(sex3) + " " + getSort1(sort1) + " and Ranked by " + getSort(sort) + "</h3>";
                            html += "<p class='para'> The Gap in Percentage is the percentage difference between the Indigenous population and the Non-Indigenous population";
                            html += " where Higher percentage means Higher amount of Indigenous population belongs to this School Year Category when compared to Non-Indigenous population";
                            html += " while Lower Percentage means less Indigenous population belongs to this School Year Category compared to the Non-Indigenous population<br><br>";
                            html += "The Gap between Indigenous and Non-Indigenous shrinks as the Percentage gets closer to zero while the Gap widens as the percentage diviates away from zero <br><br>";
                            html += "The Gap change compares the gap between Indigenous and Non-Indigenous population in this School Year Category, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                            ArrayList<String> Non_lga_code1 = new ArrayList<String>();
                            ArrayList<Integer> Non_lga_code2 = new ArrayList<Integer>();
                            ArrayList<String> Non_lga_code4 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code5 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code6 = new ArrayList<String>();
                            Non_lga_code1 = getTableS(query1_4, "name1");
                            Non_lga_code2 = getTable(query1_4, "code1");
                            Non_lga_code4 = getTableS(query1_4, "count1_1");
                            Non_lga_code5 = getTableS(query1_4, "count1_2");
                            Non_lga_code6 = getTableS(query1_4, "count1_3");
                            Non_lga_code4 = removedNull(Non_lga_code4);
                            Non_lga_code5 = removedNull(Non_lga_code5);
                            Non_lga_code6 = removedNull(Non_lga_code6);
                            html += CreateTableNon(Non_lga_code1,Non_lga_code2,Non_lga_code4, Non_lga_code5, Non_lga_code6);
                    }
                } else if (lga_state.equals("state") && data.equals("Proportional")) {
                    if (Integer.valueOf(edu_min) > Integer.valueOf(edu_max)) {
                        html += "<h2> Please select a Minmum School year that is smaller than the Maximum School year </h2>";
                    } else {
                        String query1_4 = """
                                with Indig2016 as (
                                    select lga.state_abbr as name1, lga.code as code1, SchoolYear.School_Year as ED1, sum(SchoolYear.count) as count1, sex as s1 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2016 and indigenous_status = "indig" and 
                                    ed1 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex3 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by name1), 
                                        Noindig2016 as (
                                    select lga.state_abbr as name2, lga.code as code2, SchoolYear.School_Year as ED2, sum(SchoolYear.count) as count2, sex as s2 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                    ed2 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex3 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by name2),
                                    Indig2021 as (
                                select lga.state_abbr as name3, lga.code as code3, SchoolYear.School_Year as ED3, sum(SchoolYear.count) as count3, sex as s3 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2021 and indigenous_status = "indig" and 
                                    ed3 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex3 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by name3),
                                    Noindig2021 as (
                                select lga.state_abbr as name4, lga.code as code4, SchoolYear.School_Year as ED4, sum(SchoolYear.count) as count4, sex as s4 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                    ed4 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex3 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by name4)
                                
                                select name1, round(((count1 - count2)*100.0)/(count2 + count1),2) as count1_1, round(((count3 - count4)*100.0)/(count4 + count3),2) as count1_2, round((((count3 - count4)*100)/(count4 + count3)) - (((count1 - count2)*100.0)/(count2 + count1)),2) as count1_3 from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4) 
                                        """;
                            query1_4 +=  " order by " + sort1 + " " + sort + ";";
                            html += "<h3>Proportional State data ";
                            if (Integer.valueOf(edu_min) == 7) {
                                html += " where the Population Did not got to School";
                            } else {
                                html += " where School year is  between " + edu_min + " and " + edu_max;
                            }
                            html += " and sex is " + getSex(sex3) + " " + getSort1(sort1) + " and Ranked by " + getSort(sort) + "</h3>";
                            html += "<p class='para'> The Gap in Percentage is the percentage difference between the Indigenous population and the Non-Indigenous population";
                            html += " where Higher percentage means Higher amount of Indigenous population belongs to this School Year Category when compared to Non-Indigenous population";
                            html += " while Lower Percentage means less Indigenous population belongs to this School Year Category compared to the Non-Indigenous population<br>";
                            html += "The Gap between Indigenous and Non-Indigenous shrinks as the Percentage gets closer to zero while the Gap widens as the percentage diviates away from zero <br>";
                            html += "The Gap change compares the gap between Indigenous and Non-Indigenous population in this School Year Category, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                            ArrayList<String> Non_lga_code1 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code4 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code5 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code6 = new ArrayList<String>();
                            Non_lga_code1 = getTableS(query1_4, "name1");
                            Non_lga_code4 = getTableS(query1_4, "count1_1");
                            Non_lga_code5 = getTableS(query1_4, "count1_2");
                            Non_lga_code6 = getTableS(query1_4, "count1_3");
                            Non_lga_code4 = removedNull(Non_lga_code4);
                            Non_lga_code5 = removedNull(Non_lga_code5);
                            Non_lga_code6 = removedNull(Non_lga_code6);
                            html += CreateTableNon1(Non_lga_code1,Non_lga_code4, Non_lga_code5, Non_lga_code6);
                    }
                } else if (lga_state.equals("lga") && data.equals("Raw")) {
                    if (Integer.valueOf(edu_min) > Integer.valueOf(edu_max)) {
                        html += "<h2> Please select a Minmum School year that is smaller than the Maximum School year </h2>";
                    } else {
                            String query1_4 = """
                                with Indig2016 as (
                                    select lga.name as name1, lga.code as code1, SchoolYear.School_Year as ED1, sum(SchoolYear.count) as count1, sex as s1 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2016 and indigenous_status = "indig" and 
                                    ed1 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex3 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by code1), 
                                        Noindig2016 as (
                                    select lga.name as name2, lga.code as code2, SchoolYear.School_Year as ED2, sum(SchoolYear.count) as count2, sex as s2 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                    ed2 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex3 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by code2),
                                    Indig2021 as (
                                select lga.name as name3, lga.code as code3, SchoolYear.School_Year as ED3, sum(SchoolYear.count) as count3, sex as s3 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2021 and indigenous_status = "indig" and 
                                    ed3 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex3 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by code3),
                                    Noindig2021 as (
                                select lga.name as name4, lga.code as code4, SchoolYear.School_Year as ED4, sum(SchoolYear.count) as count4, sex as s4 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                    ed4 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex3 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by code4)
                                
                                select name1, code1, count1, count2, (count1 - count2) as count1_1, count3, count4, (count3 - count4) as count1_2, ((count3 - count4) - (count1 - count2)) as count1_3 from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4)
                                        """;
                            query1_4 +=  " order by " + sort1 + " " + sort + ";";
                            html += "<h3>Raw LGA data ";
                            if (Integer.valueOf(edu_min) == 7) {
                                html += " where the Population Did not got to School";
                            } else {
                                html += " where School year is  between " + edu_min + " and " + edu_max;
                            }
                            html += " and sex is " + getSex(sex3) + " " + getSort1(sort1) + " and Ranked by " + getSort(sort) + "</h3>";
                            html += "<p class='para'> The Gap in Number is the number difference between the Indigenous population and the Non-Indigenous population";
                            html += " where Higher Number means Higher amount of Indigenous population belongs to this School Year Category when compared to Non-Indigenous population";
                            html += " while Lower Number means less Indigenous population belonging to this School Year Category compared to the Non-Indigenous population<br><br>";
                            html += "The Gap between Indigenous and Non-Indigenous shrinks as the Number gets closer to zero while the Gap widens as Number diviates away from zero <br><br>";
                            html += "The Gap change compares the gap between Indigenous and Non-Indigenous population in this School Year Category, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                            ArrayList<String> Non_lga_code1 = new ArrayList<String>();
                            ArrayList<Integer> Non_lga_code2 = new ArrayList<Integer>();
                            ArrayList<String> Non_lga_code3 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code4 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code5 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code6 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code7 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code8 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code9 = new ArrayList<String>();
                            Non_lga_code1 = getTableS(query1_4, "name1");
                            Non_lga_code2 = getTable(query1_4, "code1");
                            Non_lga_code3 = getTableS(query1_4, "count1");
                            Non_lga_code4 = getTableS(query1_4, "count2");
                            Non_lga_code5 = getTableS(query1_4, "count1_1");
                            Non_lga_code6 = getTableS(query1_4, "count3");
                            Non_lga_code7 = getTableS(query1_4, "count4");
                            Non_lga_code8 = getTableS(query1_4, "count1_2");
                            Non_lga_code9 = getTableS(query1_4, "count1_3");
                            html += CreateTableNon_1(Non_lga_code1,Non_lga_code2, Non_lga_code3, Non_lga_code4, Non_lga_code5, Non_lga_code6, Non_lga_code7, Non_lga_code8, Non_lga_code9);
                    }
                } else if (lga_state.equals("state") && data.equals("Raw")) {
                    if (Integer.valueOf(edu_min) > Integer.valueOf(edu_max)) {
                        html += "<h2> Please select a Minmum School year that is smaller than the Maximum School year </h2>";
                    } else {
                        String query1_4 = """
                                with Indig2016 as (
                                    select lga.state_abbr as name1, lga.code as code1, SchoolYear.School_Year as ED1, sum(SchoolYear.count) as count1, sex as s1 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2016 and indigenous_status = "indig" and 
                                    ed1 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s1 = " + "'" + sex3 + "' ";
                                        }  
                                query1_4 += """ 
                                         group by name1), 
                                        Noindig2016 as (
                                    select lga.state_abbr as name2, lga.code as code2, SchoolYear.School_Year as ED2, sum(SchoolYear.count) as count2, sex as s2 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2016 and indigenous_status in ("non_indig","indig_ns") and 
                                    ed2 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s2 = " + "'" + sex3 + "' ";
                                        }  
                    
                                query1_4 += """
                                      group by name2),
                                    Indig2021 as (
                                select lga.state_abbr as name3, lga.code as code3, SchoolYear.School_Year as ED3, sum(SchoolYear.count) as count3, sex as s3 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2021 and indigenous_status = "indig" and 
                                    ed3 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s3 = " + "'" + sex3 + "' ";
                                        }  
        
                                        query1_4 += """
                                      group by name3),
                                    Noindig2021 as (
                                select lga.state_abbr as name4, lga.code as code4, SchoolYear.School_Year as ED4, sum(SchoolYear.count) as count4, sex as s4 from SchoolYear left join lga on (SchoolYear.lga_code = lga.code and SchoolYear.lga_year = lga.year) where SchoolYear.lga_year = 2021 and indigenous_status in ("non_indig","indig_ns") and
                                    ed4 in (
                                        """;

                                        if (Integer.valueOf(edu_min) == 7) {
                                            query1_4 += "'" + "did_not_go_to_school" + "'";
                                        } else {
                                            for (int i = 0; i < School1_2.size(); i++) {
                                                if (School1_2.size() - 1 != i) {
                                                    query1_4 += "'" + School1_2.get(i) + "'" + ",";
                                                } else {
                                                    query1_4 += "'" + School1_2.get(i) + "'";
                                                }  
                                            }
                                        }
                                        query1_4 += " ) ";
                                        if (!sex3.equals("both")) {
                                            query1_4 += " and s4 = " + "'" + sex3 + "' ";
                                        }  
        
                                        query1_4 += """
                                     group by name4)
                                
                                select name1, count1, count2, (count1 - count2) as count1_1, count3, count4, (count3 - count4) as count1_2, ((count3 - count4) - (count1 - count2)) as count1_3 from Indig2016 
                                    join Noindig2016 on (Indig2016.name1 = Noindig2016.name2 and Indig2016.code1 = Noindig2016.code2)
                                    join Indig2021 on (Indig2016.name1 = Indig2021.name3 and Indig2016.code1 = Indig2021.code3)
                                    join Noindig2021 on (Indig2016.name1 = Noindig2021.name4 and Indig2016.code1 = Noindig2021.code4) 
                                        """;
                            query1_4 +=  " order by " + sort1 + " " + sort + ";";
                            html += "<h3>Raw State data ";
                            if (Integer.valueOf(edu_min) == 7) {
                                html += " where the Population Did not got to School";
                            } else {
                                html += " where School year is  between " + edu_min + " and " + edu_max;
                            }
                            html += " and sex is " + getSex(sex3) + " " + getSort1(sort1) + " and Ranked by " + getSort(sort) + "</h3>";
                            html += "<p class='para'> The Gap in Number is the number difference between the Indigenous population and the Non-Indigenous population";
                            html += " where Higher Number means Higher amount of Indigenous population belongs to this School Year Category when compared to Non-Indigenous population";
                            html += " while Lower Number means less Indigenous population belonging to this School Year Category compared to the Non-Indigenous population<br><br>";
                            html += "The Gap between Indigenous and Non-Indigenous shrinks as the Number gets closer to zero while the Gap widens as Number diviates away from zero <br><br>";
                            html += "he Gap change compares the gap between Indigenous and Non-Indigenous population in this School Year Category, positive percentage means an increase in gap while negative percentage means a decrease in gap </p>";
                            ArrayList<String> Non_lga_code1 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code2 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code3 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code4 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code5 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code6 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code7 = new ArrayList<String>();
                            ArrayList<String> Non_lga_code8 = new ArrayList<String>();
                            Non_lga_code1 = getTableS(query1_4, "name1");
                            Non_lga_code2 = getTableS(query1_4, "count1");
                            Non_lga_code3 = getTableS(query1_4, "count2");
                            Non_lga_code4 = getTableS(query1_4, "count1_1");
                            Non_lga_code5 = getTableS(query1_4, "count3");
                            Non_lga_code6 = getTableS(query1_4, "count4");
                            Non_lga_code7 = getTableS(query1_4, "count1_2");
                            Non_lga_code8 = getTableS(query1_4, "count1_3");
                            html += CreateTableNon1_1(Non_lga_code1, Non_lga_code2, Non_lga_code3, Non_lga_code4, Non_lga_code5, Non_lga_code6, Non_lga_code7, Non_lga_code8);
                    }
                }
                

        // Add header content block

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content

        // Close Content div
        html = html + "</div>";

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
            <form action="/page3A.html" method="post">
                <label for="lga_state">Choose LGA or STATE:</label>
                <select id="drop_down" name="LGA_STATE">
                    <option value="lga">LGA</option>
                    <option value="state">State</option>
                </select>

                <label for="sex">Gender:</label>
                <select id="drop_down" name="sex1">
                    <option value="m">Male</option>
                    <option value="f">Female</option>
                    <option value="both">Both</option>
                </select>

                <label for="sort2">Rank by</label>
                <select id="drop_down" name="sort2">
                    <option value="count_1">The Gap in Number</option>
                    <option value="count1_1">The Gap in Percentage</option>
                </select>

                <label for="sort">Sorted Order:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>

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
            <form action="/page3A.html" method="post">
                <label for="lga_state">Choose LGA or STATE:</label>
                <select id="drop_down" name="LGA_STATE">
                    <option value="lga">LGA</option>
                    <option value="state">State</option>
                </select>

                <label for="sex">Gender:</label>
                <select id="drop_down" name="sex2">
                    <option value="m">Male</option>
                    <option value="f">Female</option>
                    <option value="both">Both</option>
                </select>

                <label for="sort1">Sort By:</label>
                <select id="drop_down" name="sort1">
                    <option value="count1_1">The Gap in 2016 data</option>
                    <option value="count1_2">The Gap in 2021 data</option>
                    <option value="count1_3">The Gap between 2016 and 2021 data</option>
                </select>

                <label for="data">Data Type:</label>
                <select id="drop_down" name="data">
                    <option value="Raw">Raw Datatype</option>
                    <option value="Proportional">Proportional Datatype</option>
                </select>

                <label for="sort">Sorted by:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>
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
            <form action="/page3A.html" method="post">
                <label for="lga_state">Choose LGA or STATE:</label>
                <select id="drop_down" name="LGA_STATE">
                    <option value="lga">LGA</option>
                    <option value="state">State</option>
                </select>

                <label for="sex">Gender:</label>
                <select id="drop_down" name="sex3">
                    <option value="m">Male</option>
                    <option value="f">Female</option>
                    <option value="both">Both</option>
                </select>

                <label for="data">Data Type:</label>
                <select id="drop_down" name="data">
                    <option value="Raw">Raw Datatype</option>
                    <option value="Proportional">Proportional Datatype</option>
                </select>

                <label for="sort1">Sort By:</label>
                <select id="drop_down" name="sort1">
                    <option value="count1_1">The Gap in 2016 data</option>
                    <option value="count1_2">The Gap in 2021 data</option>
                    <option value="count1_3">The Gap between 2016 and 2021 data</option>
                </select>

                <label for="sort">Sorted by:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>

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
            <form action="/page3A.html" method="post">
                <label for="lga_state">Choose LGA or STATE:</label>
                <select id="drop_down" name="LGA_STATE">
                    <option value="lga">LGA</option>
                    <option value="state">State</option>
                </select>

                <label for="sex">Gender:</label>
                <select id="drop_down" name="sex3">
                    <option value="m">Male</option>
                    <option value="f">Female</option>
                    <option value="both">Both</option>
                </select>

                <label for="data">Data Type:</label>
                <select id="drop_down" name="data">
                    <option value="Raw">Raw Datatype</option>
                    <option value="Proportional">Proportional Datatype</option>
                </select>

                <label for="sort1">Sort By:</label>
                <select id="drop_down" name="sort1">
                    <option value="count1_1">The Gap in 2016 data</option>
                    <option value="count1_2">The Gap in 2021 data</option>
                    <option value="count1_3">The Gap between 2016 and 2021 data</option>
                </select>

                <label for="sort">Sorted by:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>
                <div>
                <label for="eduation1">Choose a School age range Min:</label>
                <input type="number" required value="" id="quantity" name="edu_min" min="8" max="12">
                </div>
                <div>
                <label for="eduation2">Choose a School age range Max:</label>
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
            <form action="/page3A.html" method="post">
                <label for="lga_state">Choose LGA or STATE:</label>
                <select id="drop_down" name="LGA_STATE">
                    <option value="lga">LGA</option>
                    <option value="state">State</option>
                </select>
            
                <label for="sex">Gender:</label>
                <select id="drop_down" name="sex4">
                    <option value="m">Male</option>
                    <option value="f">Female</option>
                    <option value="both">Both</option>
                </select>

                <label for="data">Data Type:</label>
                <select id="drop_down" name="data">
                    <option value="Raw">Raw Datatype</option>
                    <option value="Proportional">Proportional Datatype</option>
                </select>

                <label for="sort1">Sort By:</label>
                <select id="drop_down" name="sort1">
                    <option value="count1_1">The Gap in 2016 data</option>
                    <option value="count1_2">The Gap in 2021 data</option>
                    <option value="count1_3">The Gap between 2016 and 2021 data</option>
                </select>

                <label for="sort">Sorted by:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>
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
    public String CreateTableLTHC(ArrayList<String> LTHC1, ArrayList<Integer> LTHC2, ArrayList<Integer> LTHC3, ArrayList<String> LTHC4,  ArrayList<Integer> LTHC5,  ArrayList<Integer> LTHC6) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>Ranking</th>
              <th>LGA Name</th>
              <th>LGA Code</th>
              <th>Number of Indigenous indivdual</th>
              <th>Number of Non-Indigenous indivdual</th>
              <th>The Gap in Number</th>
              <th>The Gap in Percentage</th>
            </tr>
            """;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + (i + 1) + "</td>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td>" + LTHC2.get(i) + "</td> ";
            html = html + "<td>" + LTHC5.get(i) + "</td> ";
            html = html + "<td>" + LTHC6.get(i) + "</td> ";
            html = html + "<td>" + LTHC3.get(i) + "</td> ";
            html = html + "<td> " + LTHC4.get(i) + "%" + "</td>";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public String CreateTableLTHC1(ArrayList<String> LTHC1,ArrayList<String> LTHC2, ArrayList<String> LTHC3, ArrayList<String> LTHC4, ArrayList<String> LTHC5) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>Ranking</th>
              <th>State Name</th>
              <th>Number of Indigenous indivdual</th>
              <th>Number of Non-Indigenous indivdual</th>
              <th>The Gap in Number</th>
              <th>The Gap in Percetage</th>
            </tr>
            """;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + (i + 1) + "</td>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td> " + LTHC2.get(i) + "</td>";
            html = html + "<td> " + LTHC3.get(i) + "</td>";
            html = html + "<td> " + LTHC4.get(i) + "</td>";
            html = html + "<td> " + LTHC5.get(i) + "%" + "</td>";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public String CreateTableNon(ArrayList<String> LTHC1, ArrayList<Integer> LTHC2, ArrayList<String> LTHC4, ArrayList<String> LTHC5, ArrayList<String> LTHC6) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>Ranking</th>
              <th>LGA Name</th>
              <th>LGA Code</th>
              <th>The Percentage of Change in 2016</th>
              <th>The Percentage of Change in 2021</th>
              <th>The Gap Change between 2016 an 2021</th>
            </tr>
            """;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + (i + 1) + "</td>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td>" + LTHC2.get(i) + "</td> ";
            html = html + "<td> " + LTHC4.get(i) + "%" + "</td>";
            html = html + "<td> " + LTHC5.get(i) + "%" + "</td>";
            html = html + "<td> " + LTHC6.get(i) + "%" + "</td>";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public String CreateTableNon_1(ArrayList<String> LTHC1, ArrayList<Integer> LTHC2, ArrayList<String> LTHC3, ArrayList<String> LTHC4, ArrayList<String> LTHC5, ArrayList<String> LTHC6, ArrayList<String> LTHC7, ArrayList<String> LTHC8, ArrayList<String> LTHC9) {
        String html = "";
        html = html + """
            <table>
            <tr>
                <th>Ranking</th>
              <th>LGA Name</th>
              <th>LGA Code</th>
              <th>Number of Indigenous indivdual in 2016</th>
              <th>Number of Non-Indigenous indivdual in 2016</th>
              <th>The Gap in 2016</th>
              <th>Number of Indigenous indivdual in 2021</th>
              <th>Number of Non-Indigenous indivdual in 2021</th>
              <th>The Gap in 2021</th>
              <th>The Gap Change between 2016 an 2021</th>
            </tr>
            """;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + (i + 1) + "</td>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td>" + LTHC2.get(i) + "</td> ";
            html = html + "<td>" + LTHC3.get(i) + "</td> ";
            html = html + "<td> " + LTHC4.get(i) + "</td>";
            html = html + "<td> " + LTHC5.get(i) + "</td>";
            html = html + "<td> " + LTHC6.get(i) + "</td>";
            html = html + "<td>" + LTHC7.get(i) + "</td> ";
            html = html + "<td>" + LTHC8.get(i) + "</td> ";
            html = html + "<td>" + LTHC9.get(i) + "</td> ";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public String CreateTableNon1(ArrayList<String> LTHC1, ArrayList<String> LTHC4, ArrayList<String> LTHC5, ArrayList<String> LTHC6) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>State Name</th>
              <th>Percentage of Change in 2016</th>
              <th>Percentage of Change in 2021</th>
              <th>Percentage of Change between 2021 and 2016</th>
            </tr>
            """;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td> " + LTHC4.get(i) + "%" + "</td>";
            html = html + "<td> " + LTHC5.get(i) + "%" + "</td>";
            html = html + "<td> " + LTHC6.get(i) + "%" + "</td>";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public String CreateTableNon1_1(ArrayList<String> LTHC1, ArrayList<String> LTHC2, ArrayList<String> LTHC3, ArrayList<String> LTHC4, ArrayList<String> LTHC5, ArrayList<String> LTHC6, ArrayList<String> LTHC7, ArrayList<String> LTHC8) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>State Name</th>
              <th>Number of Indigenous indivdual in 2016</th>
              <th>Number of Non-Indigenous indivdual in 2016</th>
              <th>The Gap in 2016</th>
              <th>Number of Indigenous indivdual in 2021</th>
              <th>Number of Non-Indigenous indivdual in 2021</th>
              <th>The Gap in 2021</th>
              <th>Percentage of Change between 2021 and 2016</th>
            </tr>
            """;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td> " + LTHC2.get(i) + "</td>";
            html = html + "<td> " + LTHC3.get(i) + "</td>";
            html = html + "<td> " + LTHC4.get(i) + "</td>";
            html = html + "<td> " + LTHC5.get(i) + "</td>";
            html = html + "<td> " + LTHC6.get(i) + "</td>";
            html = html + "<td> " + LTHC7.get(i) + "</td>";
            html = html + "<td> " + LTHC8.get(i) + "</td>";
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
            lol = "Descending order";
        }
        return lol;
    }
    public String getSort1(String sort){
        String lol;
        if (sort.equals("count1_1")) {
            lol = "Sorted by the 2016 The Gap dataset";
        } else if (sort.equals("count1_2")) {
            lol = "Sorted by the 2021 The Gap dataset";
        } else {
            lol = "Sorted by the Change between 2021 and 2016";
        }
        return lol;
    }
    public String getSort2(String sort){
        String lol;
        if (sort.equals("count_1")) {
            lol = "Sorted by The Gap in Number";
        } else {
            lol = "Sorted by The Gap in Percentage";
        }
        return lol;
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
    public ArrayList<String> removedNull(ArrayList<String> lol) {
        for (int i = 0; i < lol.size(); i++) {
            if (lol.get(i) == null) {
                lol.set(i, "No Data");
            }
        }
        return lol;
    }


}