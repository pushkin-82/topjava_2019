<%@ page import="ru.javawebinar.topjava.web.MealServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<style>
    .table {
        border: 1px solid black;
    }
    .tableHead {
        font-family: monospace;
        font-size: 20px;
        font-weight: bolder;
        background-color: beige;
        text-align: center;
        padding: 10px;
    }
    .rowOdd {
        font-family: monospace;
        font-size: 15px;
        background-color: lightblue;
        text-align: center;
    }
    .rowEven{
        font-family: monospace;
        font-size: 15px;
        background-color: dodgerblue;
    }
</style>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>


<table class="table" cellspacing="0" cellpadding="15">
    <tr class="tableHead">
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>

    <jsp:useBean id="meals" scope="request" type="java.util.List<ru.javawebinar.topjava.model.MealTo>"/>
    <c:forEach var="mealTo" items="${meals}">
        <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo" />

        <tr class="rowOdd" style="color: ${mealTo.excess ? "green" : "red"}">
            <td>
                    ${mealTo.dateTime.format( DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))}
            </td>
            <td>
                    ${mealTo.description}
            </td>
            <td>
                    ${mealTo.calories}
            </td>
            <td>update</td>
            <td>delete</td>
        </tr>
    </c:forEach>

</table>


</body>
</html>