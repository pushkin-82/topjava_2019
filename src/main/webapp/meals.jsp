<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>


<html>
<head>
    <title>Meals</title>
    <link href="resources/styles.css" rel="stylesheet" type="text/css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<hr>
<h3>
    <a href="meals?action=create">Create new entry</a>
</h3>


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

        <tr class="${mealTo.id % 2 == 0 ? "rowEven" : "rowOdd"}" style="color: ${mealTo.excess ? "red" : "green"}">
            <td>
                    ${mealTo.dateTime.format( DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))}
            </td>
            <td>
                    ${mealTo.description}
            </td>
            <td>
                    ${mealTo.calories}
            </td>
            <td>
                <a href="meals?action=update&id=${mealTo.id}">update</a>
            </td>
            <td>
                <a href="meals?action=delete&id=${mealTo.id}">x</a>
            </td>
        </tr>
    </c:forEach>

</table>


</body>
</html>